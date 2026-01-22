package com.cinema.models;

import com.cinema.interfaces.Reservable;
import com.cinema.enums.SeatType;
import java.util.ArrayList;

public class Seance implements Reservable {
    private String date;
    private String time;
    private String movie;
    private Room room;
    private final ArrayList<Reservation> reservations;
    private char[][] seatMap;

    public static class SeatReservationException extends Exception {
        public SeatReservationException(String message) {
            super(message);
        }
    }

    public static class SeatCancellationException extends Exception {
        public SeatCancellationException(String message) {
            super(message);
        }
    }

    public Seance(String date, String time, String movie, Room room) {
        if (date == null || time == null || movie == null || room == null) {
            throw new IllegalArgumentException("Aucun paramètre ne peut être null.");
        }
        this.date = date;
        this.time = time;
        this.movie = movie;
        this.room = room;
        this.reservations = new ArrayList<>();
        this.seatMap = cloneSeatMap(room.getRoomSeatMap());
    }

    public void addReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("La réservation ne peut pas être null.");
        }
        reservations.add(reservation);
    }

    public void removeReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("La réservation ne peut pas être null.");
        }
        reservations.remove(reservation);
    }

    public void cancelReservation(Reservation reservation) throws SeatCancellationException {
        if (reservation == null) {
            throw new IllegalArgumentException("La réservation ne peut pas être null.");
        }

        ArrayList<int[]> seats = reservation.getSeats();
        for (int[] seat : seats) {
            SeatType type = determineSeatType(seat[0], seat[1]);
            cancel(seat, type);
        }

        for (Person person : reservation.getAllPeople()) {
            person.clearSeat();
        }

        reservations.remove(reservation);
    }

    public Reservation findReservationBySeat(int row, int col) {
        for (Reservation reservation : reservations) {
            for (int[] seat : reservation.getSeats()) {
                if (seat[0] == row && seat[1] == col) {
                    return reservation;
                }
            }
        }
        return null;
    }

    private SeatType determineSeatType(int row, int col) {
        if (row < 0 || row >= seatMap.length || col < 0 || col >= seatMap[0].length) {
            return SeatType.NORMAL;
        }

        char current = seatMap[row][col];
        if (current == 'O') {
            char[][] template = room.getRoomSeatMap();
            char original = template[row][col];

            if (original == 'P') return SeatType.PMR;
            if (original == 'D') return SeatType.DOUBLE;
        }

        return SeatType.NORMAL;
    }

    private char[][] cloneSeatMap(char[][] template) {
        char[][] clone = new char[template.length][];
        for (int i = 0; i < template.length; i++) {
            clone[i] = template[i].clone();
        }
        return clone;
    }

    public void displaySeatMap() {
        System.out.println("\n=== Séance: " + movie + " - " + date + " " + time + " ===");
        System.out.println("Salle: " + room.getName());
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
        System.out.println("\nRéservations actives: " + reservations.size());
    }

    @Override
    public void reserve(int[] seat, SeatType type) throws SeatReservationException {
        if (seat == null || seat.length != 2) {
            throw new IllegalArgumentException("Le siège doit être un tableau de 2 entiers.");
        }
        if (!canReserve(seat[0], seat[1], type)) {
            throw new SeatReservationException("Le siège ne peut pas être réservé (déjà pris ou type incompatible).");
        }
        applyReservation(seat[0], seat[1], type);
    }

    @Override
    public void cancel(int[] seat, SeatType type) throws SeatCancellationException {
        if (seat == null || seat.length != 2) {
            throw new IllegalArgumentException("Le siège doit être un tableau de 2 entiers.");
        }
        if (!canCancel(seat[0], seat[1], type)) {
            throw new SeatCancellationException("Le siège ne peut pas être libéré (déjà libre ou type incompatible).");
        }
        applyCancellation(seat[0], seat[1], type);
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

    private boolean canCancel(int row, int col, SeatType type) {
        if (row < 0 || row >= seatMap.length || col < 0 || col >= seatMap[0].length) {
            return false;
        }
        switch (type) {
            case NORMAL:
            case PMR:
                return seatMap[row][col] == 'O';
            case DOUBLE:
                return col + 1 < seatMap[0].length
                        && seatMap[row][col] == 'O'
                        && seatMap[row][col + 1] == 'O';
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

    private void applyCancellation(int row, int col, SeatType type) {
        char[][] template = room.getRoomSeatMap();
        seatMap[row][col] = template[row][col];
        if (type == SeatType.DOUBLE) {
            seatMap[row][col + 1] = template[row][col + 1];
        }
    }

    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getMovie() { return movie; }
    public Room getRoom() { return room; }
    public char[][] getSeatMap() { return seatMap; }
    public ArrayList<Reservation> getReservations() { return new ArrayList<>(reservations); }
}