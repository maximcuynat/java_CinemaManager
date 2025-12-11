package com.cinema.models;

import com.cinema.enums.SeatType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de la classe Seance")
public class SeanceTest {

    private Room room;
    private Seance seance;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        room = new Room("Salle Test");
        seance = new Seance("10/12/2025", "20h00", "Test Movie", room);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Le constructeur crée une séance correctement")
    void testConstructor() {
        assertNotNull(seance);
        assertEquals("10/12/2025", seance.getDate());
        assertEquals("20h00", seance.getTime());
        assertEquals("Test Movie", seance.getMovie());
        assertEquals(room, seance.getRoom());
        assertNotNull(seance.getReservations());
        assertTrue(seance.getReservations().isEmpty());
    }

    @Test
    @DisplayName("Le constructeur clone le plan de salle")
    void testConstructorClonesSeatMap() {
        char[][] roomMap = room.getRoomSeatMap();
        char[][] seanceMap = seance.getSeatMap();

        assertNotSame(roomMap, seanceMap);
        assertEquals(roomMap.length, seanceMap.length);
        assertEquals(roomMap[0].length, seanceMap[0].length);
    }

    @Test
    @DisplayName("addReservation ajoute une réservation")
    void testAddReservation() {
        Person holder = new Person("Jean", "Dupont");
        Reservation reservation = new Reservation(holder);

        seance.addReservation(reservation);

        assertEquals(1, seance.getReservations().size());
        assertEquals(reservation, seance.getReservations().get(0));
    }

    @Test
    @DisplayName("removeReservation supprime une réservation")
    void testRemoveReservation() {
        Person holder = new Person("Jean", "Dupont");
        Reservation reservation = new Reservation(holder);

        seance.addReservation(reservation);
        assertEquals(1, seance.getReservations().size());

        seance.removeReservation(reservation);
        assertEquals(0, seance.getReservations().size());
    }

    @Test
    @DisplayName("reserve réserve une place normale avec succès")
    void testReserveNormalSeat() {
        int[] seat = {2, 0};
        boolean result = seance.reserve(seat, SeatType.NORMAL);

        assertTrue(result);
        assertEquals('O', seance.getSeatMap()[2][0]);
    }

    @Test
    @DisplayName("reserve échoue si la place est déjà occupée")
    void testReserveAlreadyOccupied() {
        int[] seat = {2, 0};
        seance.reserve(seat, SeatType.NORMAL);

        boolean result = seance.reserve(seat, SeatType.NORMAL);
        assertFalse(result);
    }

    @Test
    @DisplayName("reserve réserve une place PMR avec succès")
    void testReservePMRSeat() {
        int[] seat = {0, 3};
        boolean result = seance.reserve(seat, SeatType.PMR);

        assertTrue(result);
        assertEquals('O', seance.getSeatMap()[0][3]);
    }

    @Test
    @DisplayName("reserve échoue si on essaie de réserver une place normale comme PMR")
    void testReserveNormalAsPMRFails() {
        int[] seat = {2, 0};
        boolean result = seance.reserve(seat, SeatType.PMR);

        assertFalse(result);
    }

    @Test
    @DisplayName("reserve réserve une place double avec succès")
    void testReserveDoubleSeat() {
        int[] seat = {0, 0};
        boolean result = seance.reserve(seat, SeatType.DOUBLE);

        assertTrue(result);
        assertEquals('O', seance.getSeatMap()[0][0]);
        assertEquals('O', seance.getSeatMap()[0][1]);
    }

    @Test
    @DisplayName("reserve échoue pour une place double si la seconde n'est pas 'x'")
    void testReserveDoubleInvalidSecondSeat() {
        int[] seat = {2, 0};
        boolean result = seance.reserve(seat, SeatType.DOUBLE);

        assertFalse(result);
    }

    @Test
    @DisplayName("reserve échoue pour une position hors limites")
    void testReserveOutOfBounds() {
        int[] seat = {10, 10};
        boolean result = seance.reserve(seat, SeatType.NORMAL);

        assertFalse(result);
    }

    @Test
    @DisplayName("reserve échoue pour une position négative")
    void testReserveNegativePosition() {
        int[] seat = {-1, 0};
        boolean result = seance.reserve(seat, SeatType.NORMAL);

        assertFalse(result);
    }

    @Test
    @DisplayName("cancel annule une réservation normale")
    void testCancelNormalSeat() {
        int[] seat = {2, 0};
        seance.reserve(seat, SeatType.NORMAL);
        assertEquals('O', seance.getSeatMap()[2][0]);

        boolean result = seance.cancel(seat, SeatType.NORMAL);

        assertTrue(result);
        assertEquals('0', seance.getSeatMap()[2][0]);
    }

    @Test
    @DisplayName("cancel annule une réservation PMR")
    void testCancelPMRSeat() {
        int[] seat = {0, 3};
        seance.reserve(seat, SeatType.PMR);

        boolean result = seance.cancel(seat, SeatType.PMR);

        assertTrue(result);
        assertEquals('P', seance.getSeatMap()[0][3]);
    }

    @Test
    @DisplayName("cancel annule une réservation double")
    void testCancelDoubleSeat() {
        int[] seat = {0, 0};
        seance.reserve(seat, SeatType.DOUBLE);

        boolean result = seance.cancel(seat, SeatType.DOUBLE);

        assertTrue(result);
        assertEquals('D', seance.getSeatMap()[0][0]);
        assertEquals('x', seance.getSeatMap()[0][1]);
    }

    @Test
    @DisplayName("cancel échoue pour une position hors limites")
    void testCancelOutOfBounds() {
        int[] seat = {10, 10};
        boolean result = seance.cancel(seat, SeatType.NORMAL);

        assertFalse(result);
    }

    @Test
    @DisplayName("displaySeatMap affiche les informations de la séance")
    void testDisplaySeatMap() {
        seance.displaySeatMap();

        String output = outContent.toString();
        assertTrue(output.contains("Test Movie"));
        assertTrue(output.contains("10/12/2025"));
        assertTrue(output.contains("20h00"));
        assertTrue(output.contains("Salle Test"));
        assertTrue(output.contains("Légende"));
    }

    @Test
    @DisplayName("displaySeatMap affiche le nombre de réservations")
    void testDisplaySeatMapShowsReservationCount() {
        Person holder = new Person("Jean", "Dupont");
        seance.addReservation(new Reservation(holder));
        seance.addReservation(new Reservation(holder));

        seance.displaySeatMap();

        String output = outContent.toString();
        assertTrue(output.contains("Réservations actives: 2"));
    }

    @Test
    @DisplayName("getDate retourne la date")
    void testGetDate() {
        assertEquals("10/12/2025", seance.getDate());
    }

    @Test
    @DisplayName("getTime retourne l'heure")
    void testGetTime() {
        assertEquals("20h00", seance.getTime());
    }

    @Test
    @DisplayName("getMovie retourne le titre du film")
    void testGetMovie() {
        assertEquals("Test Movie", seance.getMovie());
    }

    @Test
    @DisplayName("getRoom retourne la salle")
    void testGetRoom() {
        assertEquals(room, seance.getRoom());
    }

    @Test
    @DisplayName("getSeatMap retourne le plan de salle")
    void testGetSeatMap() {
        assertNotNull(seance.getSeatMap());
        assertEquals(5, seance.getSeatMap().length);
        assertEquals(10, seance.getSeatMap()[0].length);
    }

    @Test
    @DisplayName("getReservations retourne la liste des réservations")
    void testGetReservations() {
        assertNotNull(seance.getReservations());
        assertTrue(seance.getReservations().isEmpty());
    }

    @Test
    @DisplayName("Plusieurs réservations peuvent être effectuées")
    void testMultipleReservations() {
        assertTrue(seance.reserve(new int[]{2, 0}, SeatType.NORMAL));
        assertTrue(seance.reserve(new int[]{2, 1}, SeatType.NORMAL));
        assertTrue(seance.reserve(new int[]{0, 3}, SeatType.PMR));

        assertEquals('O', seance.getSeatMap()[2][0]);
        assertEquals('O', seance.getSeatMap()[2][1]);
        assertEquals('O', seance.getSeatMap()[0][3]);
    }

    @Test
    @DisplayName("Le plan de salle de la séance est indépendant de la salle")
    void testSeatMapIndependence() {
        int[] seat = {2, 0};
        seance.reserve(seat, SeatType.NORMAL);

        char[][] roomMap = room.getRoomSeatMap();
        char[][] seanceMap = seance.getSeatMap();

        assertEquals('O', seanceMap[2][0]);
        assertEquals('0', roomMap[2][0]);
    }
}
