package com.example.powerlogger.dto;

import java.util.ArrayList;
import java.util.List;

public class RegisterRequestDTO {
//    private String firstName;
    private String username;
    private String email;
    private String password;
    private List<Integer> roles;

    public RegisterRequestDTO(String username, String email, String password) {
//        this.firstName = firstName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = new ArrayList<>();
        roles.add(1);
    }

//    public String getFirstName() {
//        return firstName;
//    }

//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }

//    public String getLastName() {
//        return lastName;
//    }

//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }

    public String getUsername() {
        return username;
    }

    public void setUsernaame(String usernaame) {
        this.username = usernaame;
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
