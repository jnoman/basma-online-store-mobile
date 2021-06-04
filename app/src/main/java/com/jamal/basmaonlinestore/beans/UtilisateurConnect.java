package com.jamal.basmaonlinestore.beans;

import java.util.List;

public class UtilisateurConnect {
    private Long id;
    private String firstName;
    private String lastName;
    private String sub;
    private String email;
    private List<String> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public UtilisateurConnect() {
    }

    public UtilisateurConnect(Long id, String firstName, String lastName, String sub, String email, List<String> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sub = sub;
        this.email = email;
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UtilisateurConnect{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", sub='" + sub + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }
}
