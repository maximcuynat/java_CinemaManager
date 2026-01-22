package com.cinema.utils;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Builder pour créer des salles de cinéma avec des formes irrégulières.
 * Permet de définir des rangées de longueurs variables et des décalages.
 */
public class RoomBuilder {
    private ArrayList<RowDefinition> rows;
    private int maxWidth;

    public RoomBuilder() {
        this.rows = new ArrayList<>();
        this.maxWidth = 0;
    }

    /**
     * Classe interne représentant une rangée de sièges.
     */
    public static class RowDefinition {
        int leftPadding;      // Espaces vides à gauche (décalage)
        String seatPattern;   // Pattern de la rangée (ex: "000XX000")

        public RowDefinition(int leftPadding, String seatPattern) {
            this.leftPadding = leftPadding;
            this.seatPattern = seatPattern;
        }

        public int getTotalWidth() {
            return leftPadding + seatPattern.length();
        }
    }

    /**
     * Ajoute une rangée à la salle.
     *
     * @param leftPadding Nombre d'espaces vides à gauche
     * @param seatPattern Pattern de sièges (0=normal, X=allée, P=PMR, D=double, x=continuation)
     */
    public RoomBuilder addRow(int leftPadding, String seatPattern) {
        RowDefinition row = new RowDefinition(leftPadding, seatPattern);
        rows.add(row);
        maxWidth = Math.max(maxWidth, row.getTotalWidth());
        return this;
    }

    /**
     * Construction interactive d'une salle ligne par ligne.
     */
    public RoomBuilder buildInteractive(Scanner scanner) {
        System.out.println("\n=== Construction Interactive de Salle ===");
        System.out.println("Symboles disponibles :");
        System.out.println("  0 = Siège normal");
        System.out.println("  X = Allée/Inexistant");
        System.out.println("  P = Siège PMR");
        System.out.println("  D = Place double (suivie de 'x')");
        System.out.println("  x = Continuation place double");
        System.out.println();

        int numRows = getIntInput(scanner, "Nombre de rangées : ");

        for (int i = 0; i < numRows; i++) {
            System.out.println("\n--- Rangée " + (i + 1) + " ---");
            int leftPadding = getIntInput(scanner, "Décalage à gauche (espaces vides) : ");
            String pattern = getStringInput(scanner, "Pattern de sièges (ex: 00XX00D0) : ");

            // Validation basique
            if (!isValidPattern(pattern)) {
                System.out.println("⚠ Pattern invalide, mais je l'ajoute quand même.");
            }

            addRow(leftPadding, pattern);
            System.out.println("✓ Rangée ajoutée");
        }

        return this;
    }

    /**
     * Crée une salle rectangulaire simple.
     */
    public static char[][] createRectangular(int rows, int cols) {
        char[][] layout = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                layout[r][c] = '0';
            }
        }
        return layout;
    }

    /**
     * Crée une salle trapézoïdale (plus large au fond).
     */
    public static char[][] createTrapezoid(int rows, int minWidth, int maxWidth) {
        RoomBuilder builder = new RoomBuilder();

        for (int i = 0; i < rows; i++) {
            // Calcul de la largeur pour cette rangée
            int width = minWidth + (maxWidth - minWidth) * i / (rows - 1);
            int padding = (maxWidth - width) / 2;

            StringBuilder pattern = new StringBuilder();
            for (int j = 0; j < width; j++) {
                pattern.append('0');
            }

            builder.addRow(padding, pattern.toString());
        }

        return builder.build();
    }

    /**
     * Crée une salle en arc (comme dans les vrais cinémas).
     */
    public static char[][] createArched(int rows, int centerWidth) {
        RoomBuilder builder = new RoomBuilder();

        for (int i = 0; i < rows; i++) {
            // Les rangées du milieu sont plus larges
            double arcFactor = Math.sin(Math.PI * i / (rows - 1));
            int width = (int)(centerWidth + arcFactor * (centerWidth * 0.3));
            int padding = (int)((centerWidth * 1.3 - width) / 2);

            StringBuilder pattern = new StringBuilder();
            for (int j = 0; j < width; j++) {
                pattern.append('0');
            }

            builder.addRow(padding, pattern.toString());
        }

        return builder.build();
    }

    /**
     * Construit la matrice finale char[][] pour la salle.
     */
    public char[][] build() {
        if (rows.isEmpty()) {
            // Retourne une salle par défaut 5x10
            return createRectangular(5, 10);
        }

        // Calculer la largeur maximale
        int finalWidth = maxWidth;
        char[][] layout = new char[rows.size()][finalWidth];

        // Remplir toutes les cases avec 'X' (inexistant)
        for (int r = 0; r < rows.size(); r++) {
            for (int c = 0; c < finalWidth; c++) {
                layout[r][c] = 'X';
            }
        }

        // Placer les sièges selon les définitions de rangées
        for (int r = 0; r < rows.size(); r++) {
            RowDefinition rowDef = rows.get(r);
            int startCol = rowDef.leftPadding;

            for (int i = 0; i < rowDef.seatPattern.length(); i++) {
                if (startCol + i < finalWidth) {
                    layout[r][startCol + i] = rowDef.seatPattern.charAt(i);
                }
            }
        }

        return layout;
    }

    /**
     * Affiche un aperçu de la salle en construction.
     */
    public void preview() {
        char[][] layout = build();
        System.out.println("\n=== Aperçu de la Salle ===");
        System.out.println("Dimensions : " + layout.length + " rangées x " + layout[0].length + " colonnes");
        System.out.println();

        for (int r = 0; r < layout.length; r++) {
            System.out.print("Rangée " + r + ": ");
            for (int c = 0; c < layout[r].length; c++) {
                System.out.print(layout[r][c] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Valide un pattern de sièges.
     */
    private boolean isValidPattern(String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            return false;
        }

        for (char c : pattern.toCharArray()) {
            if (c != '0' && c != 'X' && c != 'P' && c != 'D' && c != 'x') {
                return false;
            }
        }

        // Vérifier que chaque 'D' est suivi de 'x'
        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == 'D') {
                if (i + 1 >= pattern.length() || pattern.charAt(i + 1) != 'x') {
                    return false;
                }
            }
        }

        return true;
    }

    // Méthodes utilitaires pour l'input
    private int getIntInput(Scanner scanner, String prompt) {
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

    private String getStringInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim().toUpperCase();
    }
}
