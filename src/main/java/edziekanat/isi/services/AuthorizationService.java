package edziekanat.isi.services;

import edziekanat.isi.dto.LoginRequest;
import edziekanat.isi.exceptions.BadRequestException;
import edziekanat.isi.exceptions.InternalServerErrorException;
import edziekanat.isi.exceptions.UserNotFoundException;
import edziekanat.isi.dto.GithubToken;
import edziekanat.isi.dto.GithubTokenRequest;
import edziekanat.isi.dto.GithubUserData;
import edziekanat.isi.misc.CustomUserDetails;
import edziekanat.isi.models.OAuthProvider;
import edziekanat.isi.models.OAuthUser;
import edziekanat.isi.models.User;
import edziekanat.isi.dto.UserPublicData;
import edziekanat.isi.models.UserRole;
import edziekanat.isi.repositories.OAuthProviderRepository;
import edziekanat.isi.repositories.OAuthUserRepository;
import edziekanat.isi.repositories.UserRepository;
import edziekanat.isi.repositories.UserRoleRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorizationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private OAuthProviderRepository oAuthProviderRepository;
    @Autowired
    private OAuthUserRepository oAuthUserRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Value("${OATH_CLIENT_ID}")
    private String clientId;

    @Value("${OATH_CLIENT_SECRET}")
    private String clientSecret;
    @Value("${OATH_REDIRECT}")
    private String redirectUri;
    // http://localhost:8080/auth/github/callback
    private String scope = "user:email,read:user";

    public String redirectGithub()
    {
        return "https://github.com/login/oauth/authorize?" +
                "client_id=" + clientId + "&redirect_uri=" + redirectUri +
                "&response_type=code&scope=" + scope;
    }

    @Transactional
    public void githubCallback(String code, HttpServletRequest request)
    {
        Optional<OAuthProvider> provider = oAuthProviderRepository.findByName("github"); // should already exist but just in case
        if (provider.isEmpty()) throw new InternalServerErrorException("Provider not found: github");

        GithubTokenRequest githubTokenRequest = new GithubTokenRequest(clientId, clientSecret, code, redirectUri);
        RestClient client = RestClient.create();

        ResponseEntity<GithubToken> githubTokenResponse = client
                .post().uri("https://github.com/login/oauth/access_token")
                .header("Accept", "application/json")
                .body(githubTokenRequest).retrieve().toEntity(GithubToken.class);

        if(githubTokenResponse.getStatusCode().value() != 200) throw new BadRequestException("Github token fuckup."); // change this later

        GithubToken token = githubTokenResponse.getBody();
        if(token == null || token.getError() != null) throw new BadRequestException("Github token fuckup."); // change this later

        client = RestClient.create();
        ResponseEntity<GithubUserData> userDataResponse = client
                .get().uri("https://api.github.com/user")
                .header("Authorization", "Bearer " + token.getAccess_token())
                .header("Accept", "application/vnd.github+json")
                .retrieve().toEntity(GithubUserData.class);

        GithubUserData githubUserData = userDataResponse.getBody();
        if (githubUserData == null) throw new BadRequestException("GitHub user data error.");

        User authenticatedUser;

        Optional<OAuthUser> oAuthUser = oAuthUserRepository.findByProviderUserId(githubUserData.getId().toString());
        if(oAuthUser.isPresent()) {
            authenticatedUser = oAuthUser.get().getUser();
            oAuthUser.get().setToken(token.getAccess_token());
        }
        else {
            Optional<UserRole> userRole = userRoleRepository.findByName("student");
            // I think it's fair to count it as internal server error because the role should exist
            if(userRole.isEmpty()) throw new InternalServerErrorException("User role not found.");

            User newUser = new User(userRole.get(), githubUserData.getLogin(), "", githubUserData.getEmail(), "");
            userRepository.save(newUser);

            OAuthUser newOAuthUser = new OAuthUser(newUser, provider.get(), githubUserData.getId().toString(), token.getAccess_token());
            oAuthUserRepository.save(newOAuthUser);

            authenticatedUser = newUser;
        }

        CustomUserDetails userDetails = new CustomUserDetails(authenticatedUser);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
    }

    public UserPublicData login(LoginRequest loginRequest) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getEmail(),
                                loginRequest.getPassword()
                        )
                );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new UserNotFoundException());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new UserPublicData(user);

        /*
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new UserNotFoundException());
        */

        /*
        User user = userRepository.findByEmail(loginData.getEmail());

        if(user == null) return null;
        if(!BCrypt.checkpw(loginData.getPassword(), user.getPassword())) return null;

        return new UserPublicData(user);

         */
    }
}
