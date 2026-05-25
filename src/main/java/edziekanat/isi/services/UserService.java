package edziekanat.isi.services;

import edziekanat.isi.dto.RegisterRequest;
import edziekanat.isi.dto.UserPublicData;
import edziekanat.isi.misc.LoginData;
import edziekanat.isi.models.User;
import edziekanat.isi.models.UserRole;
import edziekanat.isi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserPublicData register(RegisterRequest registerRequest) {

        User user = new User(
                registerRequest.getRole(),
                registerRequest.getName(),
                registerRequest.getSurname(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()));

        userRepository.save(user);

        return new UserPublicData(user);
    }
}
