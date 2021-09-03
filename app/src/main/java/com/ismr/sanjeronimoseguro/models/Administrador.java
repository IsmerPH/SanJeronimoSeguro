package com.ismr.sanjeronimoseguro.models;

public class Administrador {
    String id;
    String name;
    String email;
    String celular;

    public Administrador(String id, String name, String email, String celular) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.celular = celular;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }
}
