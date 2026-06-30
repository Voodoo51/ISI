package edziekanat.isi.dto;

import edziekanat.isi.models.UserRole;
import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

public class RegisterRequest {
    @NotNull(message = "registerRoleEmptyError")
    private UserRole role;
    @NotNull(message = "registerNameEmptyError")
    private String name;
    @NotNull(message = "registerSurnmeEmptyError")
    private String surname;
    @Email(message = "registerWrongEmailError")
    @NotBlank(message = "registerEmptyEmailError")
    private String email;
    @Size(min = 8, max = 100, message = "registerPasswordTooShortError")
    private String password;

    public RegisterRequest(UserRole role, String name, String surname, String email, String password) {
        this.role = role;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
