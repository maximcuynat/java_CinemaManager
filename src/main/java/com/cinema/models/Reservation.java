package com.cinema.models;

import java.util.ArrayList;
import java.util.Arrays;

public class Reservation {
    private int idReservation;
    private Person holder;
    private ArrayList<Person> others;

    public Reservation(Person holder) {
        setHolder(holder);
        this.others = new ArrayList<>();
    }

    public Reservation(Person holder, ArrayList<Person> others) {
        setHolder(holder);
        this.others = others;
    }

    public void addPersonToReservation(Person person, int[] seat, boolean isPmr) {
        person.saveSeat(seat);
        this.others.add(person);
    }

    // Méthode pour obtenir tous les sièges de la réservation
    public ArrayList<int[]> getSeats() {
        ArrayList<int[]> seats = new ArrayList<>();
        seats.add(holder.getSeat());
        for (Person person : others) {
            seats.add(person.getSeat());
        }
        return seats;
    }

    // Méthode pour obtenir les sièges sous forme de chaîne
    public String getSeatString() {
        StringBuilder seatString = new StringBuilder();
        for (int[] seat : getSeats()) {
            seatString.append(Arrays.toString(seat)).append(", ");
        }
        if (!getSeats().isEmpty()) {
            seatString.setLength(seatString.length() - 2); // Supprime la dernière virgule
        }
        return seatString.toString();
    }

    private void setIdReservation(int id) {
        this.idReservation = id;
    }

    private void setHolder(Person holder) {
        this.holder = holder;
    }

    private void setOthers(ArrayList<Person> others) {
        this.others = others;
    }

    public int getIdReservation() {
        return this.idReservation;
    }

    public Person getHolder() {
        return this.holder;
    }

    public ArrayList<Person> getOthers() {
        return this.others;
    }
}