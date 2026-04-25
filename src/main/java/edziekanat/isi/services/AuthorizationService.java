package edziekanat.isi.services;

import edziekanat.isi.misc.LoginData;
import edziekanat.isi.models.User;
import edziekanat.isi.dto.UserPublicData;
import edziekanat.isi.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    @Autowired
    private UserRepository userRepository;

    public UserPublicData login(LoginData loginData) {
        User user = userRepository.findByEmail(loginData.getEmail());

        if(user == null) return null;
        if(!BCrypt.checkpw(loginData.getPassword(), user.getPassword())) return null;

        return new UserPublicData(user);
    }
}
