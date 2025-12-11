package com.cinema.models;

import com.cinema.interfaces.Reservable;
import com.cinema.enums.SeatType;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Représente une séance de cinéma, avec gestion des réservations et des sièges.
 */
public class Seance implements Reservable {
    private String date;
    private String time;
    private String movie;
    private Room room;
    private final ArrayList<Reservation> reservations;
    private char[][] seatMap;

    /**
     * Exception personnalisée pour les erreurs de réservation.
     */
    public static class SeatReservationException extends Exception {
        public SeatReservationException(String message) {
            super(message);
        }
    }

    /**
     * Exception personnalisée pour les erreurs d'annulation.
     */
    public static class SeatCancellationException extends Exception {
        public SeatCancellationException(String message) {
            super(message);
        }
    }

    /**
     * Constructeur de la classe Seance.
     *
     * @param date  Date de la séance.
     * @param time  Heure de la séance.
     * @param movie Film projeté.
     * @param room  Salle de cinéma.
     * @throws IllegalArgumentException Si les paramètres sont invalides.
     */
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

    /**
     * Ajoute une réservation à la liste.
     *
     * @param reservation Réservation à ajouter.
     * @throws IllegalArgumentException Si la réservation est null.
     */
    public void addReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("La réservation ne peut pas être null.");
        }
        this.reservations.add(reservation);
    }

    /**
     * Supprime une réservation de la liste.
     *
     * @param reservation Réservation à supprimer.
     * @throws IllegalArgumentException Si la réservation est null.
     */
    public void removeReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("La réservation ne peut pas être null.");
        }
        this.reservations.remove(reservation);
    }

    /**
     * Clone la carte des sièges pour éviter les modifications externes.
     *
     * @param template Carte des sièges à cloner.
     * @return Une copie de la carte des sièges.
     */
    private char[][] cloneSeatMap(char[][] template) {
        char[][] clone = new char[template.length][];
        for (int i = 0; i < template.length; i++) {
            clone[i] = template[i].clone();
        }
        return clone;
    }

    /**
     * Affiche la carte des sièges avec légende.
     */
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

    /**
     * Réserve un siège pour un type donné.
     *
     * @param seat Coordonnées du siège.
     * @param type Type de siège.
     * @throws SeatReservationException Si la réservation échoue.
     * @throws IllegalArgumentException Si le siège est invalide.
     */
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

    /**
     * Annule la réservation d'un siège.
     *
     * @param seat Coordonnées du siège.
     * @param type Type de siège.
     * @throws SeatCancellationException Si l'annulation échoue.
     * @throws IllegalArgumentException Si le siège est invalide.
     */
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

    /**
     * Vérifie si un siège peut être réservé.
     *
     * @param row Ligne du siège.
     * @param col Colonne du siège.
     * @param type Type de siège.
     * @return true si le siège peut être réservé, false sinon.
     */
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

    /**
     * Vérifie si un siège peut être libéré.
     *
     * @param row Ligne du siège.
     * @param col Colonne du siège.
     * @param type Type de siège.
     * @return true si le siège peut être libéré, false sinon.
     */
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

    /**
     * Applique la réservation d'un siège.
     *
     * @param row Ligne du siège.
     * @param col Colonne du siège.
     * @param type Type de siège.
     */
    private void applyReservation(int row, int col, SeatType type) {
        seatMap[row][col] = 'O';
        if (type == SeatType.DOUBLE) {
            seatMap[row][col + 1] = 'O';
        }
    }

    /**
     * Applique l'annulation d'un siège.
     *
     * @param row Ligne du siège.
     * @param col Colonne du siège.
     * @param type Type de siège.
     */
    private void applyCancellation(int row, int col, SeatType type) {
        char[][] template = room.getRoomSeatMap();
        seatMap[row][col] = template[row][col];
        if (type == SeatType.DOUBLE) {
            seatMap[row][col + 1] = template[row][col + 1];
        }
    }

    // Getters et Setters (inchangés)
    public String getDate() { return this.date; }
    public String getTime() { return this.time; }
    public String getMovie() { return this.movie; }
    public Room getRoom() { return this.room; }
    public char[][] getSeatMap() { return this.seatMap; }
    public ArrayList<Reservation> getReservations() { return this.reservations; }
}
