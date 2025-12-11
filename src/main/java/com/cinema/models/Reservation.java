package com.cinema.models;

import java.util.ArrayList;

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