package it.unimib.camminatori.mysherpa.model;

public class User {

    public String name, email;

    public User(){

    }

    public User(String email, String name){
        this.email = email;
        this.name = name;
    }
}
