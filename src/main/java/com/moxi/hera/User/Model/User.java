package com.moxi.hera.User.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.*;
import java.util.Date;

@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String name;
    private String inscription;
    private String email;
    private String password;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public User(String username, String name, String inscription, String email, String password) {
        this.username = username;
        this.name = name;
        this.inscription = inscription;
        this.email = email;
        this.password = passwordEncoder.encode(password);
    }
    public User(){}
}
