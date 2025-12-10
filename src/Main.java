import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // Créer un cinéma
        Cinema cPathe = new Cinema("Pathé plan de campagne");

        // Créer une salle dans le cinéma
        Room room1 = new Room("Salle 1");

        // Ajouer une séance
        Seance seance1 = new Seance("10/12/2025", "18h15", "FNAF 2", room1);
        room1.addSeance(seance1);

        Person pMaxim = new Person("CUYNAT", "Maxim");
        Reservation rMaxim = new Reservation(pMaxim);

        seance1.reserveSeats(new int[]{2, 0}, SeatType.NORMAL);

        seance1.addReservation(rMaxim);

        seance1.displaySeatMap();

        // Ajouter la Salle au cinéma
        cPathe.addRoom(room1);
    }
}