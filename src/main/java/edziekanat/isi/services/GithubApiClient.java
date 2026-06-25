package edziekanat.isi.services;

import edziekanat.isi.dto.GithubToken;
import edziekanat.isi.dto.GithubTokenRequest;
import edziekanat.isi.dto.GithubUserData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Component
public class GithubApiClient {

    public ResponseEntity<GithubToken> getToken(GithubTokenRequest githubTokenRequest) {
        return RestClient.create()
                .post().uri("https://github.com/login/oauth/access_token")
                .header("Accept", "application/json")
                .body(githubTokenRequest).retrieve().toEntity(GithubToken.class);
    }

    public ResponseEntity<GithubUserData> getUserData(GithubToken token) {
        return RestClient.create()
                .get().uri("https://api.github.com/user")
                .header("Authorization", "Bearer " + token.getAccess_token())
                .header("Accept", "application/vnd.github+json")
                .retrieve().toEntity(GithubUserData.class);
    }
}
