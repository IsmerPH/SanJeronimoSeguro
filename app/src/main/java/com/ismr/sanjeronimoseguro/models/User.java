package com.ismr.sanjeronimoseguro.models;

public class User {

    String id;
    String name;
    String celular;
    String email;

    public User(){

    }

    public User(String id, String name, String celular, String email) {//se creo un costructor
    //public User(String id, String name, String email) {//se creo un costructor
        this.id = id;
        this.name = name;
        this.celular = celular;
        this.email = email;
    }
// se crea los metodos setter y getter
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

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
