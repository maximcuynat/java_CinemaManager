package com.cinema;

import com.cinema.models.Cinema;
import com.cinema.models.Room;
import com.cinema.models.Seance;
import com.cinema.enums.SeatType;
import com.cinema.cli.InteractiveCLI;

public class Main {
    public static void main(String[] args) {
        // Initialisation du cinéma et des données
        Cinema cPathe = new Cinema("Pathé plan de campagne");
        Room room1 = new Room("Salle 1");
        Seance seance1 = new Seance("10/12/2025", "18h15", "FNAF 2", room1);
        room1.addSeance(seance1);
        cPathe.addRoom(room1);

        // Démarrer le CLI interactif
        InteractiveCLI cli = new InteractiveCLI(cPathe);
        cli.start();
    }
}
