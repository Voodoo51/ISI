package edziekanat.isi.dto;


import edziekanat.isi.models.User;
import edziekanat.isi.models.UserRole;

public class UserPublicData {
    private long id;
    private String email;
    private String name;
    private String surname;
    private UserRole role;

    public UserPublicData(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.role = user.getRole();
        this.role = user.getRole();
    }

    public UserPublicData(long id, String email, String name, String surname, UserRole role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
