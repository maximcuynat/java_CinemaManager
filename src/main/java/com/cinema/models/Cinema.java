package com.cinema.models;

import java.util.ArrayList;

public class Cinema {
    private String name;
    private ArrayList<Room> rooms;

    public Cinema(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du cinéma ne peut pas être vide");
        }
        this.name = name.trim();
        this.rooms = new ArrayList<>();
    }

    public void rename(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nouveau nom ne peut pas être vide");
        }
        this.name = newName.trim();
    }

    public void addRoom(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("La salle ne peut pas être null");
        }
        if (!rooms.contains(room)) {
            rooms.add(room);
        }
    }

    public void addRooms(ArrayList<Room> newRooms) {
        if (newRooms == null) {
            throw new IllegalArgumentException("La liste de salles ne peut pas être null");
        }
        for (Room room : newRooms) {
            addRoom(room);
        }
    }

    public void removeRoom(Room room) {
        rooms.remove(room);
    }

    public Room getRoomByName(String name) {
        for (Room room : rooms) {
            if (room.getName().equalsIgnoreCase(name)) {
                return room;
            }
        }
        return null;
    }

    public void displayAllRooms() {
        if (rooms.isEmpty()) {
            System.out.println("Aucune salle dans ce cinéma");
            return;
        }

        System.out.println("Salles du cinéma " + name + ":");
        for (Room room : rooms) {
            System.out.println(" - " + room.getName() + " (" + room.getSeances().size() + " séance(s))");
        }
    }

    public int getRoomCount() {
        return rooms.size();
    }

    public int getTotalSeances() {
        int total = 0;
        for (Room room : rooms) {
            total += room.getSeances().size();
        }
        return total;
    }

    public int getTotalReservations() {
        int total = 0;
        for (Room room : rooms) {
            for (Seance seance : room.getSeances()) {
                total += seance.getReservations().size();
            }
        }
        return total;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Room> getRooms() {
        return new ArrayList<>(rooms);
    }
}