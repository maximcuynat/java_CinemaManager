package com.cinema.cli;

import com.cinema.models.*;
import com.cinema.enums.SeatType;
import com.cinema.utils.RoomBuilder;
import java.util.*;

public class InteractiveCLI {
    private ArrayList<Cinema> cinemas;
    private Cinema currentCinema;
    private Scanner scanner;

    public InteractiveCLI() {
        this.cinemas = new ArrayList<>();
        this.currentCinema = null;
        this.scanner = new Scanner(System.in);
    }

    private void clearConsole() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }

    public void start() {
        clearConsole();
        System.out.println("=== Bienvenue dans le système de gestion de cinémas ===");

        boolean running = true;
        while (running) {
            clearConsole();
            displayCinemaSelectionMenu();
            int choice = getIntInput("Choisissez une option : ");

            switch (choice) {
                case 1:
                    createCinema();
                    break;
                case 2:
                    selectCinema();
                    break;
                case 3:
                    listCinemas();
                    waitForEnter();
                    break;
                case 0:
                    if (confirmAction("Voulez-vous vraiment quitter ?")) {
                        running = false;
                        System.out.println("Au revoir !");
                    }
                    break;
                default:
                    System.out.println("Option invalide.");
                    waitForEnter();
            }
        }
    }

    private void displayCinemaSelectionMenu() {
        System.out.println("\n=== Menu Principal ===");
        System.out.println("Cinémas enregistrés : " + cinemas.size());
        if (currentCinema != null) {
            System.out.println("Cinéma actuel : " + currentCinema.getName());
        }
        System.out.println("\n1. Créer un nouveau cinéma");
        System.out.println("2. Sélectionner/Gérer un cinéma");
        System.out.println("3. Afficher tous les cinémas");
        System.out.println("0. Quitter");
    }

    private void createCinema() {
        System.out.println("\n=== Créer un Nouveau Cinéma ===");
        String name = getStringInput("Entrez le nom du cinéma : ");
        if (name.trim().isEmpty()) {
            System.out.println("Le nom ne peut pas être vide.");
            waitForEnter();
            return;
        }

        Cinema newCinema = new Cinema(name);
        cinemas.add(newCinema);
        currentCinema = newCinema;
        System.out.println("Cinéma '" + name + "' créé avec succès !");
        System.out.println("Vous êtes maintenant dans le cinéma : " + name);
        waitForEnter();
        manageCinema();
    }

    private void selectCinema() {
        if (cinemas.isEmpty()) {
            System.out.println("Aucun cinéma disponible. Veuillez d'abord créer un cinéma.");
            waitForEnter();
            return;
        }

        System.out.println("\n=== Sélectionner un Cinéma ===");
        listCinemas();
        int cinemaIndex = getIntInput("Sélectionnez un cinéma (entrez le numéro) : ") - 1;

        if (cinemaIndex < 0 || cinemaIndex >= cinemas.size()) {
            System.out.println("Numéro de cinéma invalide.");
            waitForEnter();
            return;
        }

        currentCinema = cinemas.get(cinemaIndex);
        System.out.println("Vous avez sélectionné : " + currentCinema.getName());
        waitForEnter();
        manageCinema();
    }

    private void listCinemas() {
        if (cinemas.isEmpty()) {
            System.out.println("Aucun cinéma enregistré.");
            return;
        }

        System.out.println("\n=== Liste des Cinémas ===");
        for (int i = 0; i < cinemas.size(); i++) {
            Cinema cinema = cinemas.get(i);
            String indicator = (cinema == currentCinema) ? " (actuel)" : "";
            System.out.println((i + 1) + ". " + cinema.getName() + indicator +
                    " - " + cinema.getRooms().size() + " salle(s)");
        }
    }

    private void manageCinema() {
        if (currentCinema == null) {
            System.out.println("Aucun cinéma sélectionné.");
            waitForEnter();
            return;
        }

        boolean managing = true;
        while (managing) {
            clearConsole();
            displayCinemaManagementMenu();
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
                    waitForEnter();
                    break;
                case 6:
                    searchSeances();
                    waitForEnter();
                    break;
                case 7:
                    displayReservationHistory();
                    waitForEnter();
                    break;
                case 8:
                    displayCinemaInfo();
                    waitForEnter();
                    break;
                case 0:
                    managing = false;
                    System.out.println("Retour au menu principal...");
                    break;
                default:
                    System.out.println("Option invalide.");
                    waitForEnter();
            }
        }
    }

    private void displayCinemaManagementMenu() {
        System.out.println("\n=== Gestion du Cinéma : " + currentCinema.getName() + " ===");
        System.out.println("1. Gérer les salles");
        System.out.println("2. Gérer les séances");
        System.out.println("3. Réserver un siège");
        System.out.println("4. Annuler une réservation");
        System.out.println("5. Afficher la carte des sièges");
        System.out.println("6. Rechercher une séance");
        System.out.println("7. Historique des réservations");
        System.out.println("8. Informations du cinéma");
        System.out.println("0. Retour au menu principal");
    }

    private void displayCinemaInfo() {
        System.out.println("\n=== Informations du Cinéma ===");
        System.out.println("Nom : " + currentCinema.getName());
        System.out.println("Nombre de salles : " + currentCinema.getRoomCount());
        System.out.println("Nombre de séances : " + currentCinema.getTotalSeances());
        System.out.println("Nombre de réservations : " + currentCinema.getTotalReservations());
    }

    private void waitForEnter() {
        System.out.print("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
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
                    waitForEnter();
                    break;
                case 2:
                    displayRooms();
                    waitForEnter();
                    break;
                case 3:
                    deleteRoom();
                    waitForEnter();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Option invalide.");
                    waitForEnter();
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
                    waitForEnter();
                    break;
                case 2:
                    displaySeances();
                    waitForEnter();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Option invalide.");
                    waitForEnter();
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

        System.out.println("\nType de salle :");
        System.out.println("1. Salle simple (par défaut 5x10)");
        System.out.println("2. Salle rectangulaire personnalisée");
        System.out.println("3. Salle trapézoïdale (plus large au fond)");
        System.out.println("4. Salle en arc (comme les vrais cinémas)");
        System.out.println("5. Construction interactive (ligne par ligne)");

        int choice = getIntInput("Choisissez le type : ");
        Room room;

        switch (choice) {
            case 1:
                room = new Room(name);
                break;
            case 2:
                int rows = getIntInput("Nombre de rangées : ");
                int cols = getIntInput("Nombre de colonnes : ");
                char[][] rectLayout = RoomBuilder.createRectangular(rows, cols);
                room = new Room(name, rectLayout);
                break;
            case 3:
                int trapRows = getIntInput("Nombre de rangées : ");
                int minWidth = getIntInput("Largeur minimale (devant) : ");
                int maxWidth = getIntInput("Largeur maximale (fond) : ");
                char[][] trapLayout = RoomBuilder.createTrapezoid(trapRows, minWidth, maxWidth);
                room = new Room(name, trapLayout);
                break;
            case 4:
                int arcRows = getIntInput("Nombre de rangées : ");
                int centerWidth = getIntInput("Largeur centrale : ");
                char[][] arcLayout = RoomBuilder.createArched(arcRows, centerWidth);
                room = new Room(name, arcLayout);
                break;
            case 5:
                RoomBuilder builder = new RoomBuilder();
                builder.buildInteractive(scanner);
                builder.preview();

                if (confirmAction("Confirmer cette configuration ?")) {
                    char[][] customLayout = builder.build();
                    room = new Room(name, customLayout);
                } else {
                    System.out.println("Création annulée.");
                    return;
                }
                break;
            default:
                System.out.println("Choix invalide, salle par défaut créée.");
                room = new Room(name);
        }

        currentCinema.addRoom(room);
        System.out.println("✓ Salle '" + name + "' créée avec succès !");
    }

    private void displayRooms() {
        if (currentCinema.getRooms().isEmpty()) {
            System.out.println("Aucune salle disponible.");
            return;
        }

        System.out.println("\n=== Liste des Salles ===");
        List<String[]> rows = new ArrayList<>();
        for (Room room : currentCinema.getRooms()) {
            rows.add(new String[]{room.getName(), String.valueOf(room.getSeances().size())});
        }

        displayTable(new String[]{"Nom", "Nombre de séances"}, rows);
    }

    private void deleteRoom() {
        if (currentCinema.getRooms().isEmpty()) {
            System.out.println("Aucune salle disponible.");
            return;
        }

        displayRooms();
        int roomIndex = getIntInput("Sélectionnez une salle à supprimer (entrez le numéro) : ") - 1;
        if (roomIndex < 0 || roomIndex >= currentCinema.getRooms().size()) {
            System.out.println("Numéro de salle invalide.");
            return;
        }

        if (confirmAction("Voulez-vous vraiment supprimer cette salle ?")) {
            Room room = currentCinema.getRooms().get(roomIndex);
            currentCinema.removeRoom(room);
            System.out.println("Salle '" + room.getName() + "' supprimée avec succès !");
        }
    }

    private void createSeance() {
        if (currentCinema.getRooms().isEmpty()) {
            System.out.println("Aucune salle disponible. Veuillez d'abord créer une salle.");
            return;
        }

        System.out.println("\n=== Créer une Séance ===");
        displayRooms();
        int roomIndex = getIntInput("Sélectionnez une salle (entrez le numéro) : ") - 1;
        if (roomIndex < 0 || roomIndex >= currentCinema.getRooms().size()) {
            System.out.println("Numéro de salle invalide.");
            return;
        }

        Room selectedRoom = currentCinema.getRooms().get(roomIndex);
        String date = getStringInput("Entrez la date de la séance (ex. 10/12/2025) : ");
        String time = getStringInput("Entrez l'heure de la séance (ex. 18h15) : ");
        String movie = getStringInput("Entrez le titre du film : ");

        Seance seance = new Seance(date, time, movie, selectedRoom);
        selectedRoom.addSeance(seance);
        System.out.println("Séance '" + movie + "' créée avec succès !");
    }

    private void displaySeances() {
        if (currentCinema.getRooms().isEmpty()) {
            System.out.println("Aucune salle disponible.");
            return;
        }

        System.out.println("\n=== Liste des Séances ===");
        List<String[]> rows = new ArrayList<>();
        int seanceIndex = 1;
        for (Room room : currentCinema.getRooms()) {
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
        if (currentCinema.getRooms().isEmpty()) {
            System.out.println("Aucune salle disponible.");
            waitForEnter();
            return;
        }

        System.out.println("\n=== Réserver un Siège ===");
        displaySeances();
        int seanceIndex = getIntInput("Sélectionnez une séance (entrez l'ID) : ") - 1;

        List<Seance> allSeances = getAllSeances();
        if (seanceIndex < 0 || seanceIndex >= allSeances.size()) {
            System.out.println("ID de séance invalide.");
            waitForEnter();
            return;
        }

        Seance selectedSeance = allSeances.get(seanceIndex);
        int numberOfPeople = getIntInput("Combien de personnes pour cette réservation ? ");

        if (numberOfPeople < 1) {
            System.out.println("Le nombre de personnes doit être au moins 1.");
            waitForEnter();
            return;
        }

        System.out.println("\n--- Personne principale (titulaire de la réservation) ---");
        Person holder = createPerson();

        selectedSeance.displaySeatMap();
        int row = getIntInput("Entrez la ligne du siège pour " + holder.getFirstName() + " : ");
        int col = getIntInput("Entrez la colonne du siège pour " + holder.getFirstName() + " : ");
        SeatType type = getSeatTypeInput("Entrez le type de siège (NORMAL, PMR, DOUBLE) : ");
        int[] holderSeat = new int[]{row, col};

        try {
            selectedSeance.reserve(holderSeat, type);
            holder.assignSeat(row, col);

            Reservation reservation = new Reservation(holder);

            for (int i = 2; i <= numberOfPeople; i++) {
                System.out.println("\n--- Personne " + i + " ---");
                Person additionalPerson = createPerson();

                selectedSeance.displaySeatMap();
                int additionalRow = getIntInput("Entrez la ligne du siège pour " + additionalPerson.getFirstName() + " : ");
                int additionalCol = getIntInput("Entrez la colonne du siège pour " + additionalPerson.getFirstName() + " : ");
                SeatType additionalType = getSeatTypeInput("Entrez le type de siège (NORMAL, PMR, DOUBLE) : ");
                int[] additionalSeat = new int[]{additionalRow, additionalCol};

                try {
                    selectedSeance.reserve(additionalSeat, additionalType);
                    reservation.addPersonToReservation(additionalPerson, additionalSeat);
                    System.out.println("Siège réservé pour " + additionalPerson.getFullName() + " !");
                } catch (Exception e) {
                    System.out.println("Erreur pour " + additionalPerson.getFirstName() + " : " + e.getMessage());
                }
            }

            selectedSeance.addReservation(reservation);

            System.out.println("\n=== Réservation complète ===");
            System.out.println("Réservation #" + reservation.getId() + " créée avec succès !");
            System.out.println("Titulaire : " + holder.getFullName());
            if (reservation.getOthers().size() > 0) {
                System.out.println("Invités : ");
                for (Person guest : reservation.getOthers()) {
                    System.out.println("  - " + guest.getFullName());
                }
            }
            System.out.println("Sièges : " + reservation.getSeatString());

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }

        waitForEnter();
    }

    private Person createPerson() {
        String firstName = getStringInput("Entrez le prénom : ");
        String lastName = getStringInput("Entrez le nom de famille : ");
        return new Person(firstName, lastName);
    }

    private void cancelReservation() {
        if (currentCinema.getRooms().isEmpty()) {
            System.out.println("Aucune salle disponible.");
            waitForEnter();
            return;
        }

        System.out.println("\n=== Annuler une Réservation ===");
        displaySeances();
        int seanceIndex = getIntInput("Sélectionnez une séance (entrez l'ID) : ") - 1;

        List<Seance> allSeances = getAllSeances();
        if (seanceIndex < 0 || seanceIndex >= allSeances.size()) {
            System.out.println("ID de séance invalide.");
            waitForEnter();
            return;
        }

        Seance selectedSeance = allSeances.get(seanceIndex);

        if (selectedSeance.getReservations().isEmpty()) {
            System.out.println("Aucune réservation pour cette séance.");
            waitForEnter();
            return;
        }

        System.out.println("\n=== Réservations pour cette séance ===");
        List<String[]> rows = new ArrayList<>();
        for (Reservation reservation : selectedSeance.getReservations()) {
            rows.add(new String[]{
                    String.valueOf(reservation.getId()),
                    reservation.getHolder().getFullName(),
                    String.valueOf(reservation.getTotalPeople()),
                    reservation.getSeatString()
            });
        }
        displayTable(new String[]{"ID", "Titulaire", "Nb Pers.", "Sièges"}, rows);

        int reservationId = getIntInput("Entrez l'ID de la réservation à annuler : ");

        Reservation toCancel = null;
        for (Reservation reservation : selectedSeance.getReservations()) {
            if (reservation.getId() == reservationId) {
                toCancel = reservation;
                break;
            }
        }

        if (toCancel == null) {
            System.out.println("Réservation introuvable.");
            waitForEnter();
            return;
        }

        try {
            selectedSeance.cancelReservation(toCancel);
            System.out.println("Réservation #" + reservationId + " annulée avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'annulation : " + e.getMessage());
        }

        waitForEnter();
    }

    private void displaySeatMap() {
        if (currentCinema.getRooms().isEmpty()) {
            System.out.println("Aucune salle disponible.");
            return;
        }

        System.out.println("\n=== Carte des Sièges ===");
        displaySeances();
        int seanceIndex = getIntInput("Sélectionnez une séance (entrez l'ID) : ") - 1;

        List<Seance> allSeances = getAllSeances();
        if (seanceIndex < 0 || seanceIndex >= allSeances.size()) {
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
        for (Room room : currentCinema.getRooms()) {
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
        for (Room room : currentCinema.getRooms()) {
            for (Seance seance : room.getSeances()) {
                for (Reservation reservation : seance.getReservations()) {
                    StringBuilder names = new StringBuilder();
                    names.append(reservation.getHolder().getFullName()).append(" (Titulaire)");

                    if (!reservation.getOthers().isEmpty()) {
                        for (Person guest : reservation.getOthers()) {
                            names.append(", ").append(guest.getFullName());
                        }
                    }

                    rows.add(new String[]{
                            String.valueOf(reservation.getId()),
                            seance.getMovie(),
                            seance.getDate() + " " + seance.getTime(),
                            room.getName(),
                            String.valueOf(reservation.getTotalPeople()),
                            names.toString(),
                            reservation.getSeatString()
                    });
                }
            }
        }

        if (rows.isEmpty()) {
            System.out.println("Aucune réservation trouvée.");
        } else {
            displayTable(new String[]{"ID Rés.", "Film", "Date et Heure", "Salle", "Nb Pers.", "Noms", "Sièges"}, rows);
        }
    }

    private List<Seance> getAllSeances() {
        List<Seance> allSeances = new ArrayList<>();
        for (Room room : currentCinema.getRooms()) {
            allSeances.addAll(room.getSeances());
        }
        return allSeances;
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