package com.cinema;

import com.cinema.models.*;
import com.cinema.enums.SeatType;

public class Main {
    public static void main(String[] args) {
        Cinema cPathe = new Cinema("Pathé plan de campagne");

        Room room1 = new Room("Salle 1");

        Seance seance1 = new Seance("10/12/2025", "18h15", "FNAF 2", room1);
        room1.addSeance(seance1);

        Person pMaxim = new Person("CUYNAT", "Maxim");
        Reservation rMaxim = new Reservation(pMaxim);

        seance1.reserve(new int[]{2, 0}, SeatType.NORMAL);

        seance1.addReservation(rMaxim);

        seance1.displaySeatMap();

        cPathe.addRoom(room1);
    }
}