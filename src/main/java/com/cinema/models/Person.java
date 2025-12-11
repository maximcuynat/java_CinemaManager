package com.cinema.models;

public class Person {
    private String firstName;
    private String lastName;
    private int[] place = new int[2];

    public Person() {
        this.firstName = "";
        this.lastName = "";
    }

    public Person(String firstName, String lastName) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    public void saveSeat(int[] seat) {
        this.place[0] = seat[0];
        this.place[1] = seat[1];
    }

    private void setFirstName(String firstName) {
        if (!firstName.isEmpty()) {
            this.firstName = firstName;
        } else {
            this.firstName = "No firstname";
        }
    }

    private void setLastName(String lastName) {
        if (!lastName.isEmpty()) {
            this.lastName = lastName;
        } else {
            this.lastName = "No lastName";
        }
    }

    public String getPerson() {
        return this.firstName + " - " + this.lastName;
    }

    public int[] getPlace() {
        return this.place;
    }
}