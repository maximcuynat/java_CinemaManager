package com.cinema.cli;

import com.cinema.models.*;
import com.cinema.enums.SeatType;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class InteractiveCLI {
    private Cinema cinema;
    private Scanner scanner;

    public InteractiveCLI() {
        this.cinema = new Cinema("Nouveau Cinéma");
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Bienvenue dans le système de gestion de cinéma ===");

        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Choisissez une option : ");

            switch (choice) {
                case 1:
                    createCinema();
                    break;
                case 2:
                    createRoom();
                    break;
                case 3:
                    createSeance();
                    break;
                case 4:
                    displayRooms();
                    break;
                case 5:
                    displaySeances();
                    break;
                case 6:
                    reserveSeat();
                    break;
                case 7:
                    cancelReservation();
                    break;
                case 8:
                    displaySeatMap();
                    break;
                case 0:
                    running = false;
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n=== Menu Principal ===");
        System.out.println("1. Créer un cinéma");
        System.out.println("2. Créer une salle");
        System.out.println("3. Créer une séance");
        System.out.println("4. Afficher les salles");
        System.out.println("5. Afficher les séances");
        System.out.println("6. Réserver un siège");
        System.out.println("7. Annuler une réservation");
        System.out.println("8. Afficher la carte des sièges");
        System.out.println("0. Quitter");
    }

    private void createCinema() {
        System.out.println("\n=== Créer un Cinéma ===");
        String name = getStringInput("Entrez le nom du cinéma : ");
        if (name.trim().isEmpty()) {
            System.out.println("Le nom ne peut pas être vide.");
            return;
        }
        this.cinema = new Cinema(name);
        System.out.println("Cinéma '" + name + "' créé avec succès !");
    }

    private void createRoom() {
        if (cinema == null) {
            System.out.println("Veuillez d'abord créer un cinéma.");
            return;
        }

        System.out.println("\n=== Créer une Salle ===");
        String name = getStringInput("Entrez le nom de la salle : ");
        if (name.trim().isEmpty()) {
            System.out.println("Le nom ne peut pas être vide.");
            return;
        }

        Room room = new Room(name);
        cinema.addRoom(room);
        System.out.println("Salle '" + name + "' créée avec succès !");
    }

    private void createSeance() {
        if (cinema.getRooms().isEmpty()) {
            System.out.println("Aucune salle disponible. Veuillez d'abord créer une salle.");
            return;
        }

        System.out.println("\n=== Créer une Séance ===");
        displayRooms();
        int roomIndex = getIntInput("Sélectionnez une salle (entrez le numéro) : ") - 1;
        if (roomIndex < 0 || roomIndex >= cinema.getRooms().size()) {
            System.out.println("Numéro de salle invalide.");
            return;
        }

        Room selectedRoom = cinema.getRooms().get(roomIndex);
        String date = getStringInput("Entrez la date de la séance (ex. 10/12/2025) : ");
        String time = getStringInput("Entrez l'heure de la séance (ex. 18h15) : ");
        String movie = getStringInput("Entrez le titre du film : ");

        Seance seance = new Seance(date, time, movie, selectedRoom);
        selectedRoom.addSeance(seance);
        System.out.println("Séance '" + movie + "' créée avec succès !");
    }

    private void displayRooms() {
        if (cinema.getRooms().isEmpty()) {
            System.out.println("Aucune salle disponible.");
            return;
        }

        System.out.println("\n=== Liste des Salles ===");
        int index = 1;
        for (Room room : cinema.getRooms()) {
            System.out.println(index++ + ". " + room.getName());
        }
    }

    private void displaySeances() {
        if (cinema.getRooms().isEmpty()) {
            System.out.println("Aucune salle disponible.");
            return;
        }

        System.out.println("\n=== Liste des Séances ===");
        int seanceIndex = 1;
        for (Room room : cinema.getRooms()) {
            System.out.println("\nSalle : " + room.getName());
            for (Seance seance : room.getSeances()) {
                System.out.printf("  %d. %s : %s à %s%n", seanceIndex++, seance.getMovie(), seance.getDate(), seance.getTime());
            }
        }
        if (seanceIndex == 1) {
            System.out.println("Aucune séance disponible.");
        }
    }

    private void reserveSeat() {
        if (cinema.getRooms().isEmpty()) {
            System.out.println("Aucune salle disponible.");
            return;
        }

        System.out.println("\n=== Réserver un Siège ===");
        displaySeances();
        int seanceIndex = getIntInput("Sélectionnez une séance (entrez le numéro) : ") - 1;
        if (seanceIndex < 0) {
            System.out.println("Numéro de séance invalide.");
            return;
        }

        Seance selectedSeance = null;
        int currentIndex = 0;
        for (Room room : cinema.getRooms()) {
            for (Seance seance : room.getSeances()) {
                if (currentIndex == seanceIndex) {
                    selectedSeance = seance;
                    break;
                }
                currentIndex++;
            }
            if (selectedSeance != null) break;
        }

        if (selectedSeance == null) {
            System.out.println("Séance introuvable.");
            return;
        }

        selectedSeance.displaySeatMap();
        int row = getIntInput("Entrez la ligne du siège : ");
        int col = getIntInput("Entrez la colonne du siège : ");
        SeatType type = getSeatTypeInput("Entrez le type de siège (NORMAL, PMR, DOUBLE) : ");

        Person person = createPerson();
        Reservation reservation = new Reservation(person);

        try {
            selectedSeance.reserve(new int[]{row, col}, type);
            selectedSeance.addReservation(reservation);
            System.out.println("Siège réservé avec succès pour " + person.getFirstName() + " " + person.getLastName() + " !");
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private Person createPerson() {
        System.out.println("\n=== Créer une Personne ===");
        String lastName = getStringInput("Entrez le nom de famille : ");
        String firstName = getStringInput("Entrez le prénom : ");
        return new Person(lastName, firstName);
    }

    private void cancelReservation() {
        if (cinema.getRooms().isEmpty()) {
            System.out.println("Aucune salle disponible.");
            return;
        }

        System.out.println("\n=== Annuler une Réservation ===");
        displaySeances();
        int seanceIndex = getIntInput("Sélectionnez une séance (entrez le numéro) : ") - 1;
        if (seanceIndex < 0) {
            System.out.println("Numéro de séance invalide.");
            return;
        }

        Seance selectedSeance = null;
        int currentIndex = 0;
        for (Room room : cinema.getRooms()) {
            for (Seance seance : room.getSeances()) {
                if (currentIndex == seanceIndex) {
                    selectedSeance = seance;
                    break;
                }
                currentIndex++;
            }
            if (selectedSeance != null) break;
        }

        if (selectedSeance == null) {
            System.out.println("Séance introuvable.");
            return;
        }

        selectedSeance.displaySeatMap();
        int row = getIntInput("Entrez la ligne du siège à libérer : ");
        int col = getIntInput("Entrez la colonne du siège à libérer : ");
        SeatType type = getSeatTypeInput("Entrez le type de siège (NORMAL, PMR, DOUBLE) : ");

        try {
            selectedSeance.cancel(new int[]{row, col}, type);
            System.out.println("Réservation annulée avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void displaySeatMap() {
        if (cinema.getRooms().isEmpty()) {
            System.out.println("Aucune salle disponible.");
            return;
        }

        System.out.println("\n=== Carte des Sièges ===");
        displaySeances();
        int seanceIndex = getIntInput("Sélectionnez une séance (entrez le numéro) : ") - 1;
        if (seanceIndex < 0) {
            System.out.println("Numéro de séance invalide.");
            return;
        }

        Seance selectedSeance = null;
        int currentIndex = 0;
        for (Room room : cinema.getRooms()) {
            for (Seance seance : room.getSeances()) {
                if (currentIndex == seanceIndex) {
                    selectedSeance = seance;
                    break;
                }
                currentIndex++;
            }
            if (selectedSeance != null) break;
        }

        if (selectedSeance != null) {
            selectedSeance.displaySeatMap();
        } else {
            System.out.println("Séance introuvable.");
        }
    }

    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Entrée invalide. Veuillez entrer un nombre.");
            scanner.next(); // Consomme l'entrée invalide
            System.out.print(prompt);
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // Consomme la nouvelle ligne
        return input;
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private SeatType getSeatTypeInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim().toUpperCase();
        try {
            return SeatType.valueOf(input);
        } catch (IllegalArgumentException e) {
            System.out.println("Type de siège invalide. Les types valides sont : NORMAL, PMR, DOUBLE.");
            return getSeatTypeInput(prompt);
        }
    }
}
