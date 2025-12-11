package com.cinema;

import com.cinema.models.*;
import com.cinema.enums.SeatType;
import com.cinema.models.Seance.SeatReservationException;

public class Main {
    public static void main(String[] args) {
        try {
            // Création du cinéma et de la salle
            Cinema cPathe = new Cinema("Pathé plan de campagne");
            Room room1 = new Room("Salle 1");

            // Création de la séance
            Seance seance1 = new Seance("10/12/2025", "18h15", "FNAF 2", room1);
            room1.addSeance(seance1);

            // Création de la personne et de la réservation
            Person pMaxim = new Person("CUYNAT", "Maxim");
            Reservation rMaxim = new Reservation(pMaxim);

            // Réservation d'un siège
            try {
                seance1.reserve(new int[]{2, 0}, SeatType.NORMAL);
                seance1.addReservation(rMaxim);
                System.out.println("Réservation effectuée avec succès !");
            } catch (SeatReservationException e) {
                System.err.println("Erreur lors de la réservation : " + e.getMessage());
            }

            // Affichage de la carte des sièges
            seance1.displaySeatMap();

            // Ajout de la salle au cinéma
            cPathe.addRoom(room1);

        } catch (IllegalArgumentException e) {
            System.err.println("Erreur dans les paramètres : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Une erreur inattendue est survenue : " + e.getMessage());
        }
    }
}
