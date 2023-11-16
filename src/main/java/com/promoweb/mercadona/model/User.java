package com.promoweb.mercadona.model;



import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le prénom ne peut pas être vide")
    @Size(min = 3, message = "Le prénom doit contenir au minimum 3 caractères")
    private String firstname;

    @NotBlank(message = "Le nom ne peut pas être vide")
    @Size(min = 3, message = "Le nom doit contenir au minimum 3 caractères")
    private String lastname;

    @Email(message = "L'adresse e-mail doit être valide")
    private String email;
    @NotBlank(message = "Le nom d'utilisateur ne peut pas être vide")
    private String username;


    @Pattern(regexp ="^(?=.*[@$!%*#?&])?.{8,}$", message = "Le mot de passe  doit contenir au minimum 8 caractères, lettres, chiffres et caractères spéciaux")
    private String password;

    @NotBlank(message = "Le rôle ne peut pas être vide")
    @Size(min = 5, message = "Le rôle doit contenir au minimum 5 caractères")
    private String role;

    @Column(name ="status")
    private Boolean status = false;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Product> products = new ArrayList<>();

    public User() {
    }

    public User(Long id, String username, String password, String role) {
        this.id =id;
        this.username = username;
        this.password = password;
        this.role= role;

    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retournez une liste d'attributs GrantedAuthority représentant les rôles de l'utilisateur
        return Collections.singletonList(new SimpleGrantedAuthority(role.toUpperCase()));
    }

    @Override
    public String toString() {
        return "User{" +
                "ID=" + id + '\'' +
                ",username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", lastname='" + lastname + '\'' +
                ", status=" + status + '\'' +
                '}';
    }

    public void setId(Long id) {
        this.id=id;
    }


}
