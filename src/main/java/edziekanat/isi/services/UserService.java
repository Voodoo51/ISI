package edziekanat.isi.services;

import edziekanat.isi.dto.ChangePasswordRequest;
import edziekanat.isi.dto.RegisterRequest;
import edziekanat.isi.dto.UserPublicData;
import edziekanat.isi.exceptions.EmailAlreadyExistsException;
import edziekanat.isi.exceptions.UserNotFoundException;
import edziekanat.isi.exceptions.WrongCredentialsException;
import edziekanat.isi.misc.LoginData;
import edziekanat.isi.models.User;
import edziekanat.isi.models.UserRole;
import edziekanat.isi.repositories.UserRepository;
import edziekanat.isi.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserPublicData register(RegisterRequest registerRequest) {

        if(userRepository.findByEmail(registerRequest.getEmail()).isPresent()) throw new EmailAlreadyExistsException("Email already exists");

        UserRole role = userRoleRepository.findById(registerRequest.getRole().getId()).orElseThrow();

        User user = new User();

        user.setRole(role);
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setName(registerRequest.getName());
        user.setSurname(registerRequest.getSurname());

        userRepository.save(user);
        return new UserPublicData(user);
    }

    @PreAuthorize("hasAnyRole('ADMIN','WORKER')")
    public Page<UserPublicData> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserPublicData::new);
    }

    public UserPublicData getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        return new UserPublicData(user);
    }

    public void changePassword(Long userId, ChangePasswordRequest dto){
        User user = userRepository.findById(userId).orElseThrow();

        if(!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) throw new WrongCredentialsException();

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }
}
