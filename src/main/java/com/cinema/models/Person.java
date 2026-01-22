package com.cinema.models;

public class Person {
    private String firstName;
    private String lastName;
    private int[] seat;

    public Person() {
        this.firstName = "";
        this.lastName = "";
        this.seat = null;
    }

    public Person(String firstName, String lastName) {
        setFirstName(firstName);
        setLastName(lastName);
        this.seat = null;
    }

    public void assignSeat(int row, int col) {
        this.seat = new int[]{row, col};
    }

    public void clearSeat() {
        this.seat = null;
    }

    public boolean hasAssignedSeat() {
        return seat != null;
    }

    public void setFirstName(String firstName) {
        this.firstName = (firstName != null && !firstName.trim().isEmpty())
                ? firstName.trim()
                : "No firstname";
    }

    public void setLastName(String lastName) {
        this.lastName = (lastName != null && !lastName.trim().isEmpty())
                ? lastName.trim()
                : "No lastName";
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int[] getSeat() {
        return seat != null ? seat.clone() : null;
    }
}