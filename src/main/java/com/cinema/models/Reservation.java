package com.cinema.models;

import java.util.ArrayList;
import java.util.Arrays;

public class Reservation {
    private static int nextId = 1;
    private final int id;
    private Person holder;
    private ArrayList<Person> others;

    public Reservation(Person holder) {
        if (holder == null) {
            throw new IllegalArgumentException("Le titulaire ne peut pas être null");
        }
        this.id = nextId++;
        this.holder = holder;
        this.others = new ArrayList<>();
    }

    public Reservation(Person holder, ArrayList<Person> others) {
        if (holder == null) {
            throw new IllegalArgumentException("Le titulaire ne peut pas être null");
        }
        this.id = nextId++;
        this.holder = holder;
        this.others = others != null ? new ArrayList<>(others) : new ArrayList<>();
    }

    public void addPersonToReservation(Person person, int[] seat) {
        if (person == null) {
            throw new IllegalArgumentException("La personne ne peut pas être null");
        }
        if (seat == null || seat.length != 2) {
            throw new IllegalArgumentException("Le siège doit être un tableau de 2 entiers");
        }
        person.assignSeat(seat[0], seat[1]);
        others.add(person);
    }

    public ArrayList<int[]> getSeats() {
        ArrayList<int[]> seats = new ArrayList<>();
        if (holder.hasAssignedSeat()) {
            seats.add(holder.getSeat());
        }
        for (Person person : others) {
            if (person.hasAssignedSeat()) {
                seats.add(person.getSeat());
            }
        }
        return seats;
    }

    public String getSeatString() {
        StringBuilder seatString = new StringBuilder();
        for (int[] seat : getSeats()) {
            seatString.append("[").append(seat[0]).append(",").append(seat[1]).append("], ");
        }
        if (seatString.length() > 0) {
            seatString.setLength(seatString.length() - 2);
        }
        return seatString.toString();
    }

    public int getTotalPeople() {
        return 1 + others.size();
    }

    public ArrayList<Person> getAllPeople() {
        ArrayList<Person> allPeople = new ArrayList<>();
        allPeople.add(holder);
        allPeople.addAll(others);
        return allPeople;
    }

    public int getId() {
        return id;
    }

    public Person getHolder() {
        return holder;
    }

    public ArrayList<Person> getOthers() {
        return new ArrayList<>(others);
    }
}