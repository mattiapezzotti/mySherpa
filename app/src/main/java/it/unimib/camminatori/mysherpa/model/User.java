package it.unimib.camminatori.mysherpa.model;

public class User {

    private String name, email;

    public Double getKmTot() {
        return kmTot;
    }

    public void setKmTot(Double kmTot) {
        this.kmTot = kmTot;
    }

    private Double kmTot;

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

    public User(String email, String name){
        this.email = email;
        this.name = name;
        this.kmTot = 0.0;
    }

    public User(){}
}
