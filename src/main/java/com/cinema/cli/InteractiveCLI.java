package com.cinema.cli;

import com.cinema.models.*;
import com.cinema.enums.SeatType;
import java.util.Scanner;

public class InteractiveCLI {
    private Cinema cinema;
    private Scanner scanner;

    public InteractiveCLI(Cinema cinema) {
        this.cinema = cinema;
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
                    displayRooms();
                    break;
                case 2:
                    displaySeances();
                    break;
                case 3:
                    reserveSeat();
                    break;
                case 4:
                    cancelReservation();
                    break;
                case 5:
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
        System.out.println("1. Afficher les salles");
        System.out.println("2. Afficher les séances");
        System.out.println("3. Réserver un siège");
        System.out.println("4. Annuler une réservation");
        System.out.println("5. Afficher la carte des sièges");
        System.out.println("0. Quitter");
    }

    private void displayRooms() {
        System.out.println("\n=== Liste des Salles ===");
        for (Room room : cinema.getRooms()) {
            System.out.println("- " + room.getName());
        }
    }

    private void displaySeances() {
        System.out.println("\n=== Liste des Séances ===");
        for (Room room : cinema.getRooms()) {
            System.out.println("\nSalle : " + room.getName());
            for (Seance seance : room.getSeances()) {
                System.out.printf("  - %s : %s à %s%n", seance.getMovie(), seance.getDate(), seance.getTime());
            }
        }
    }

    private void reserveSeat() {
        System.out.println("\n=== Réserver un Siège ===");
        displaySeances();
        int seanceIndex = getIntInput("Sélectionnez une séance (entrez le numéro) : ") - 1;

        Room selectedRoom = null;
        Seance selectedSeance = null;
        int currentIndex = 0;
        for (Room room : cinema.getRooms()) {
            for (Seance seance : room.getSeances()) {
                if (currentIndex == seanceIndex) {
                    selectedRoom = room;
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

        try {
            selectedSeance.reserve(new int[]{row, col}, type);
            System.out.println("Siège réservé avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void cancelReservation() {
        System.out.println("\n=== Annuler une Réservation ===");
        displaySeances();
        int seanceIndex = getIntInput("Sélectionnez une séance (entrez le numéro) : ") - 1;

        Room selectedRoom = null;
        Seance selectedSeance = null;
        int currentIndex = 0;
        for (Room room : cinema.getRooms()) {
            for (Seance seance : room.getSeances()) {
                if (currentIndex == seanceIndex) {
                    selectedRoom = room;
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
        System.out.println("\n=== Carte des Sièges ===");
        displaySeances();
        int seanceIndex = getIntInput("Sélectionnez une séance (entrez le numéro) : ") - 1;

        Room selectedRoom = null;
        Seance selectedSeance = null;
        int currentIndex = 0;
        for (Room room : cinema.getRooms()) {
            for (Seance seance : room.getSeances()) {
                if (currentIndex == seanceIndex) {
                    selectedRoom = room;
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
