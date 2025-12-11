package com.cinema.cli;

import com.cinema.models.*;
import com.cinema.enums.SeatType;
import java.util.*;
import java.util.stream.Collectors;

public class InteractiveCLI {
    private Cinema cinema;
    private Scanner scanner;

    public InteractiveCLI() {
        this.cinema = new Cinema("Pathé Plan de Campagne");
        this.scanner = new Scanner(System.in);
    }

    // ==== AJOUT : Nettoyage de la console ====
    private void clearConsole() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Méthode fallback : scroll
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }

    public void start() {
        clearConsole();
        System.out.println("=== Bienvenue dans le système de gestion de cinéma ===");

        boolean running = true;
        while (running) {
            clearConsole();
            displayMainMenu();
            int choice = getIntInput("Choisissez une option : ");

            switch (choice) {
                case 1:
                    manageRooms();
                    break;
                case 2:
                    manageSeances();
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
                case 6:
                    searchSeances();
                    break;
                case 7:
                    displayReservationHistory();
                    break;
                case 0:
                    if (confirmAction("Voulez-vous vraiment quitter ? (Oui/Non)")) {
                        running = false;
                        System.out.println("Au revoir !");
                    }
                    break;
                default:
                    System.out.println("Option invalide.");
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n=== Menu Principal ===");
        System.out.println("1. Gérer les salles");
        System.out.println("2. Gérer les séances");
        System.out.println("3. Réserver un siège");
        System.out.println("4. Annuler une réservation");
        System.out.println("5. Afficher la carte des sièges");
        System.out.println("6. Rechercher une séance");
        System.out.println("7. Historique des réservations");
        System.out.println("0. Quitter");
    }

    private void manageRooms() {
        boolean back = false;
        while (!back) {
            clearConsole();
            System.out.println("\n=== Gestion des Salles ===");
            System.out.println("1. Créer une salle");
            System.out.println("2. Afficher les salles");
            System.out.println("3. Supprimer une salle");
            System.out.println("0. Retour");
            int choice = getIntInput("Choisissez une option : ");

            switch (choice) {
                case 1:
                    createRoom();
                    break;
                case 2:
                    displayRooms();
                    break;
                case 3:
                    deleteRoom();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Option invalide.");
            }
        }
    }

    private void manageSeances() {
        boolean back = false;
        while (!back) {
            clearConsole();
            System.out.println("\n=== Gestion des Séances ===");
            System.out.println("1. Créer une séance");
            System.out.println("2. Afficher les séances");
            System.out.println("0. Retour");
            int choice = getIntInput("Choisissez une option : ");

            switch (choice) {
                case 1:
                    createSeance();
                    break;
                case 2:
                    displaySeances();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Option invalide.");
            }
        }
    }

    private void createRoom() {
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

    private void displayRooms() {
        if (cinema.getRooms().isEmpty()) {
            System.out.println("Aucune salle disponible.");
            return;
        }

        System.out.println("\n=== Liste des Salles ===");
        List<String[]> rows = new ArrayList<>();
        for (Room room : cinema.getRooms()) {
            rows.add(new String[]{room.getName(), String.valueOf(room.getSeances().size())});
        }

        displayTable(new String[]{"Nom", "Nombre de séances"}, rows);
    }

    private void deleteRoom() {
        if (cinema.getRooms().isEmpty()) {
            System.out.println("Aucune salle disponible.");
            return;
        }

        displayRooms();
        int roomIndex = getIntInput("Sélectionnez une salle à supprimer (entrez le numéro) : ") - 1;
        if (roomIndex < 0 || roomIndex >= cinema.getRooms().size()) {
            System.out.println("Numéro de salle invalide.");
            return;
        }

        if (confirmAction("Voulez-vous vraiment supprimer cette salle ? (Oui/Non)")) {
            Room room = cinema.getRooms().get(roomIndex);
            cinema.getRooms().remove(roomIndex);
            System.out.println("Salle '" + room.getName() + "' supprimée avec succès !");
        }
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

    private void displaySeances() {
        if (cinema.getRooms().isEmpty()) {
            System.out.println("Aucune salle disponible.");
            return;
        }

        System.out.println("\n=== Liste des Séances ===");
        List<String[]> rows = new ArrayList<>();
        int seanceIndex = 1;
        for (Room room : cinema.getRooms()) {
            for (Seance seance : room.getSeances()) {
                rows.add(new String[]{
                        String.valueOf(seanceIndex++),
                        seance.getMovie(),
                        seance.getDate(),
                        seance.getTime(),
                        room.getName()
                });
            }
        }

        if (rows.isEmpty()) {
            System.out.println("Aucune séance disponible.");
        } else {
            displayTable(new String[]{"ID", "Film", "Date", "Heure", "Salle"}, rows);
        }
    }

    private void reserveSeat() {
        if (cinema.getRooms().isEmpty()) {
            System.out.println("Aucune salle disponible.");
            return;
        }

        System.out.println("\n=== Réserver un Siège ===");
        displaySeances();
        int seanceIndex = getIntInput("Sélectionnez une séance (entrez l'ID) : ") - 1;
        if (seanceIndex < 0) {
            System.out.println("ID de séance invalide.");
            return;
        }

        List<Seance> allSeances = new ArrayList<>();
        for (Room room : cinema.getRooms()) allSeances.addAll(room.getSeances());

        if (seanceIndex >= allSeances.size()) {
            System.out.println("ID de séance invalide.");
            return;
        }

        Seance selectedSeance = allSeances.get(seanceIndex);
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
        int seanceIndex = getIntInput("Sélectionnez une séance (entrez l'ID) : ") - 1;
        if (seanceIndex < 0) {
            System.out.println("ID de séance invalide.");
            return;
        }

        List<Seance> allSeances = new ArrayList<>();
        for (Room room : cinema.getRooms()) allSeances.addAll(room.getSeances());

        if (seanceIndex >= allSeances.size()) {
            System.out.println("ID de séance invalide.");
            return;
        }

        Seance selectedSeance = allSeances.get(seanceIndex);
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
        int seanceIndex = getIntInput("Sélectionnez une séance (entrez l'ID) : ") - 1;
        if (seanceIndex < 0) {
            System.out.println("ID de séance invalide.");
            return;
        }

        List<Seance> allSeances = new ArrayList<>();
        for (Room room : cinema.getRooms()) allSeances.addAll(room.getSeances());

        if (seanceIndex >= allSeances.size()) {
            System.out.println("ID de séance invalide.");
            return;
        }

        Seance selectedSeance = allSeances.get(seanceIndex);
        selectedSeance.displaySeatMap();
    }

    private void searchSeances() {
        System.out.println("\n=== Rechercher une Séance ===");
        String query = getStringInput("Rechercher un film (ou laisser vide pour tout afficher) : ").trim();

        List<String[]> rows = new ArrayList<>();
        int seanceIndex = 1;
        for (Room room : cinema.getRooms()) {
            for (Seance seance : room.getSeances()) {
                if (query.isEmpty() || seance.getMovie().toLowerCase().contains(query.toLowerCase())) {
                    rows.add(new String[]{
                            String.valueOf(seanceIndex++),
                            seance.getMovie(),
                            seance.getDate(),
                            seance.getTime(),
                            room.getName()
                    });
                }
            }
        }

        if (rows.isEmpty()) {
            System.out.println("Aucune séance trouvée.");
        } else {
            displayTable(new String[]{"ID", "Film", "Date", "Heure", "Salle"}, rows);
        }
    }

    private void displayReservationHistory() {
        System.out.println("\n=== Historique des Réservations ===");

        List<String[]> rows = new ArrayList<>();
        for (Room room : cinema.getRooms()) {
            for (Seance seance : room.getSeances()) {
                for (Reservation reservation : seance.getReservations()) {
                    rows.add(new String[]{
                            seance.getMovie(),
                            seance.getDate() + " " + seance.getTime(),
                            room.getName(),
                            reservation.getHolder().getFirstName() + " " + reservation.getHolder().getLastName(),
                            reservation.getSeatString()
                    });
                }
            }
        }

        if (rows.isEmpty()) {
            System.out.println("Aucune réservation trouvée.");
        } else {
            displayTable(new String[]{"Film", "Date et Heure", "Salle", "Nom", "Siège"}, rows);
        }
    }

    private void displayTable(String[] headers, List<String[]> rows) {
        int[] columnWidths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            columnWidths[i] = headers[i].length();
        }
        for (String[] row : rows) {
            for (int i = 0; i < row.length; i++) {
                if (row[i].length() > columnWidths[i]) {
                    columnWidths[i] = row[i].length();
                }
            }
        }

        System.out.print("+");
        for (int width : columnWidths) {
            System.out.print("-".repeat(width + 2) + "+");
        }
        System.out.println();

        System.out.print("|");
        for (int i = 0; i < headers.length; i++) {
            System.out.printf(" %-" + columnWidths[i] + "s |", headers[i]);
        }
        System.out.println();

        for (String[] row : rows) {
            System.out.print("+");
            for (int width : columnWidths) {
                System.out.print("-".repeat(width + 2) + "+");
            }
            System.out.println();
            System.out.print("|");
            for (int i = 0; i < row.length; i++) {
                System.out.printf(" %-" + columnWidths[i] + "s |", row[i]);
            }
            System.out.println();
        }

        System.out.print("+");
        for (int width : columnWidths) {
            System.out.print("-".repeat(width + 2) + "+");
        }
        System.out.println();
    }

    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Entrée invalide. Veuillez entrer un nombre.");
            scanner.next();
            System.out.print(prompt);
        }
        int input = scanner.nextInt();
        scanner.nextLine();
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

    private boolean confirmAction(String prompt) {
        String input = getStringInput(prompt + " (Oui/Non) : ").trim().toLowerCase();
        return input.equals("oui") || input.equals("o");
    }
}
