package com.flowersAndGifts.model;

import java.util.Objects;

public class User {
    private Long id;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private Role role;
    private Boolean active = true;

    public User() {
    }

    public User(Long id, String email, String password, String firstname, String lastname, Role role, Boolean active) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.active = active;
    }

    public User(String email, String password, String firstname, String lastname, Role role, Boolean active) {
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.active = active;
    }

    public User(Long id, String email, String firstname, String lastname, Role role, Boolean active) {
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.active = active;
    }

    public User(String email, String firstname, String lastname, Role role, Boolean active) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(firstname, user.firstname) && Objects.equals(lastname, user.lastname) && role == user.role && Objects.equals(active, user.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, firstname, lastname, role, active);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", role=" + role +
                ", active=" + active +
                '}';
    }
}
