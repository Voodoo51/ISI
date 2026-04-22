package edziekanat.isi.models;

public class User
{
    public enum UserPermissions {
        Admin,
        Supervisor,
        Worker,
        Student
    }

    private int id;
    private String name;
    private String surname;
    private UserPermissions userPermissions;

    public User(int id, String name, String surname, UserPermissions userPermissions) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.userPermissions = userPermissions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
