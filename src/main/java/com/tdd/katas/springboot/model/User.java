package com.tdd.katas.springboot.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

public class User {

    @Id
    private Long id;
    @Column
    @NotNull
    private String login;
    @Column
    @NotNull
    private String password;
    @Column
    @NotNull
    private String email;

    public User(Long id, String login, String password, String email) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
