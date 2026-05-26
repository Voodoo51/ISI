package edziekanat.isi.models;

/*
public enum UserPermissions {
    Admin,
    Supervisor,
    Worker,
    Student
}
*/

import jakarta.persistence.*;

@Entity
@Table(name="app_user")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(name="role_id", nullable = false)
    private UserRole role;
    private String name;
    private String surname;
    private String email;
    private String password;

    public User(){}

    public User(long id, UserRole role, String name, String surname, String email, String password) {
        this.id = id;
        this.role = role;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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


    /*
    public UserPermissions getUserPermissions() {
        return userPermissions;
    }

    public void setUserPermissions(UserPermissions userPermissions) {
        this.userPermissions = userPermissions;
    }

    public static UserPermissions idToRole(int roleId) {
        UserPermissions userPermissions;
        switch (roleId) {
            case 0 -> userPermissions = User.UserPermissions.Admin;
            case 1 -> userPermissions = User.UserPermissions.Supervisor;
            case 2 -> userPermissions = User.UserPermissions.Worker;
            default -> userPermissions = User.UserPermissions.Student; //3
        }

        return userPermissions;
    }
    */
}
