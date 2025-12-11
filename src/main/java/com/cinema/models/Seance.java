package com.cinema.models;

import com.cinema.interfaces.Reservable;
import com.cinema.enums.SeatType;
import java.util.ArrayList;

public class Seance implements Reservable {
    private String date;
    private String time;
    private String movie;
    private Room room;
    private ArrayList<Reservation> reservations;
    private char[][] seatMap;

    public Seance(String date, String time, String movie, Room room) {
        this.date = date;
        this.time = time;
        this.movie = movie;
        this.room = room;
        this.reservations = new ArrayList<>();
        this.seatMap = cloneSeatMap(room.getRoomSeatMap());
    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
    }

    public void removeReservation(Reservation reservation) {
        this.reservations.remove(reservation);
    }

    private char[][] cloneSeatMap(char[][] template) {
        char[][] clone = new char[template.length][];
        for (int i = 0; i < template.length; i++) {
            clone[i] = template[i].clone();
        }
        return clone;
    }

    public void displaySeatMap() {
        System.out.println("\n=== Séance: " + this.movie + " - " + this.date + " " + this.time + " ===");
        System.out.println("Salle: " + this.room.getName());
        System.out.println();

        System.out.print("    ");
        for (int col = 0; col < seatMap[0].length; col++) {
            System.out.print(col + " ");
        }
        System.out.println();

        for (int row = 0; row < seatMap.length; row++) {
            System.out.print(row + " - ");
            for (int col = 0; col < seatMap[row].length; col++) {
                System.out.print(seatMap[row][col] + " ");
            }
            System.out.println();
        }

        System.out.println("\nLégende:");
        System.out.println("  0 = Libre");
        System.out.println("  O = Occupé");
        System.out.println("  X = Allée/Inexistant");
        System.out.println("  P = PMR (libre)");
        System.out.println("  D = Place double (libre)");
        System.out.println("  x = Continuation place double");
        System.out.println("\nRéservations actives: " + this.reservations.size());
    }

    @Override
    public boolean reserve(int[] seat, SeatType type) {
        if (!canReserve(seat[0], seat[1], type)) {
            return false;
        }
        applyReservation(seat[0], seat[1], type);
        return true;
    }

    @Override
    public boolean cancel(int[] seat, SeatType type) {
        char[][] template = room.getRoomSeatMap();

        if (seat[0] < 0 || seat[0] >= seatMap.length ||
                seat[1] < 0 || seat[1] >= seatMap[0].length) {
            return false;
        }

        seatMap[seat[0]][seat[1]] = template[seat[0]][seat[1]];

        if (type == SeatType.DOUBLE && seat[1] + 1 < seatMap[0].length) {
            seatMap[seat[0]][seat[1] + 1] = template[seat[0]][seat[1] + 1];
        }

        return true;
    }

    private boolean canReserve(int row, int col, SeatType type) {
        if (row < 0 || row >= seatMap.length || col < 0 || col >= seatMap[0].length) {
            return false;
        }

        switch (type) {
            case NORMAL:
                return seatMap[row][col] == '0';
            case PMR:
                return seatMap[row][col] == 'P';
            case DOUBLE:
                return col + 1 < seatMap[0].length
                        && seatMap[row][col] == 'D'
                        && seatMap[row][col + 1] == 'x';
            default:
                return false;
        }
    }

    private void applyReservation(int row, int col, SeatType type) {
        seatMap[row][col] = 'O';
        if (type == SeatType.DOUBLE) {
            seatMap[row][col + 1] = 'O';
        }
    }

    private void setDate(String date) {
        this.date = date;
    }

    private void setTime(String time) {
        this.time = time;
    }

    private void setMovie(String movie) {
        this.movie = movie;
    }

    private void setRoom(Room room) {
        this.room = room;
    }

    private void setSeatMap(char[][] seatmap) {
        this.seatMap = seatmap;
    }

    public String getDate() {
        return this.date;
    }

    public String getTime() {
        return this.time;
    }

    public String getMovie() {
        return this.movie;
    }

    public Room getRoom() {
        return this.room;
    }

    public char[][] getSeatMap() {
        return this.seatMap;
    }

    public ArrayList<Reservation> getReservations() {
        return this.reservations;
    }
}