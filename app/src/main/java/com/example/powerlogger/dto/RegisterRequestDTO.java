package com.example.powerlogger.dto;

import java.util.ArrayList;
import java.util.List;

public class RegisterRequestDTO {
    private String username;
    private String email;
    private String password;
    private List<Integer> roles;
    private String weight;

    public RegisterRequestDTO(String username, String email, String password, String weight) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = new ArrayList<>();
        this.weight = weight;
        roles.add(1);
    }

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

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Integer> getRoles() {
        return roles;
    }

    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
