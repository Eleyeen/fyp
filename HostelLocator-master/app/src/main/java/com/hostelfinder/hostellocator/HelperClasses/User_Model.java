package com.hostelfinder.hostellocator.HelperClasses;

public class User_Model {

    private String id;
    private String username;
    private String fullname;
    private String password;
    private String email;

    public User_Model(String id, String username, String fullname, String password, String email) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.password = password;
        this.email = email;
    }

    public User_Model() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
