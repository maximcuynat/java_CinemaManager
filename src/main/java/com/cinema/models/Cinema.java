package com.cinema.models;

import java.util.ArrayList;

public class Cinema {
    private String name;
    private ArrayList<Room> rooms;

    public Cinema(String name) {
        setName(name);
        this.rooms = new ArrayList<>();
    }

    public void reName(String newName) {
        this.name = newName;
    }

    public void addRoom(Room room) {
        this.rooms.add(room);
    }

    public void addRooms(ArrayList<Room> rooms) {
        setRooms(rooms);
    }

    public void displayAllRooms() {
        if (this.rooms.isEmpty()) {
            System.out.println("Aucune salle dans ce cinéma");
            return;
        }

        System.out.println("Salles du cinéma :" + this.name + ":");
        for (Room room : this.rooms) {
            System.out.println(" - " + room.getName());
        }
    }

    public int getRoomCount() {
        return this.rooms.size();
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Room> getRooms() {
        return this.rooms;
    }
}