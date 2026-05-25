package edziekanat.isi.services;

import edziekanat.isi.dto.LoginRequest;
import edziekanat.isi.exceptions.BadRequestException;
import edziekanat.isi.exceptions.UserNotFoundException;
import edziekanat.isi.misc.GithubToken;
import edziekanat.isi.misc.LoginData;
import edziekanat.isi.misc.TokenRequest;
import edziekanat.isi.models.User;
import edziekanat.isi.dto.UserPublicData;
import edziekanat.isi.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.web.client.RestClient;

@Service
public class AuthorizationService {
    @Autowired
    private UserRepository userRepository;
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
    public void githubCallback(String code)
    {
        TokenRequest request = new TokenRequest(clientId, clientSecret, code, redirectUri);
        RestClient client = RestClient.create();

        ResponseEntity<GithubToken> response = client.post().uri("https://github.com/login/oauth/access_token")
                .header("Accept", "application/json")
                .body(request).retrieve().toEntity(GithubToken.class);

        if(response.getStatusCode().value() != 200) throw new BadRequestException("Github token fuckup."); // change this later

        GithubToken token = response.getBody();

        if(token.getError() != null) throw new BadRequestException("Github token fuckup."); // change this later
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
