package com.cinema.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de la classe Reservation")
public class ReservationTest {

    private Person holder;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        holder = new Person("Jean", "Dupont");
        reservation = new Reservation(holder);
    }

    @Test
    @DisplayName("Le constructeur simple crée une réservation avec holder uniquement")
    void testSimpleConstructor() {
        assertNotNull(reservation.getHolder());
        assertEquals("Jean - Dupont", reservation.getHolder().getPerson());
        assertNotNull(reservation.getOthers());
        assertTrue(reservation.getOthers().isEmpty());
    }

    @Test
    @DisplayName("Le constructeur avec liste crée une réservation complète")
    void testConstructorWithList() {
        ArrayList<Person> others = new ArrayList<>();
        others.add(new Person("Marie", "Martin"));
        others.add(new Person("Pierre", "Durand"));

        Reservation fullReservation = new Reservation(holder, others);

        assertNotNull(fullReservation.getHolder());
        assertEquals("Jean - Dupont", fullReservation.getHolder().getPerson());
        assertEquals(2, fullReservation.getOthers().size());
    }

    @Test
    @DisplayName("addPersonToReservation ajoute correctement une personne")
    void testAddPersonToReservation() {
        Person newPerson = new Person("Marie", "Martin");
        int[] seat = {3, 5};

        reservation.addPersonToReservation(newPerson, seat, false);

        assertEquals(1, reservation.getOthers().size());
        assertEquals("Marie - Martin", reservation.getOthers().get(0).getPerson());
    }

    @Test
    @DisplayName("addPersonToReservation enregistre le siège de la personne")
    void testAddPersonToReservationSavesSeat() {
        Person newPerson = new Person("Marie", "Martin");
        int[] seat = {2, 7};

        reservation.addPersonToReservation(newPerson, seat, false);

        Person addedPerson = reservation.getOthers().get(0);
        assertArrayEquals(new int[]{2, 7}, addedPerson.getPlace());
    }

    @Test
    @DisplayName("Plusieurs personnes peuvent être ajoutées à une réservation")
    void testAddMultiplePeople() {
        Person person1 = new Person("Marie", "Martin");
        Person person2 = new Person("Pierre", "Durand");
        Person person3 = new Person("Sophie", "Bernard");

        reservation.addPersonToReservation(person1, new int[]{1, 1}, false);
        reservation.addPersonToReservation(person2, new int[]{1, 2}, false);
        reservation.addPersonToReservation(person3, new int[]{1, 3}, true);

        assertEquals(3, reservation.getOthers().size());
    }

    @Test
    @DisplayName("getHolder retourne le bon titulaire")
    void testGetHolder() {
        Person returnedHolder = reservation.getHolder();
        assertNotNull(returnedHolder);
        assertEquals(holder, returnedHolder);
        assertEquals("Jean - Dupont", returnedHolder.getPerson());
    }

    @Test
    @DisplayName("getOthers retourne une ArrayList")
    void testGetOthersReturnsList() {
        assertNotNull(reservation.getOthers());
        assertTrue(reservation.getOthers() instanceof ArrayList);
    }

    @Test
    @DisplayName("getIdReservation retourne 0 par défaut (non initialisé)")
    void testGetIdReservationDefaultValue() {
        assertEquals(0, reservation.getIdReservation());
    }

    @Test
    @DisplayName("La liste others est modifiable après création")
    void testOthersListIsMutable() {
        assertTrue(reservation.getOthers().isEmpty());

        Person newPerson = new Person("Test", "User");
        reservation.addPersonToReservation(newPerson, new int[]{0, 0}, false);

        assertFalse(reservation.getOthers().isEmpty());
        assertEquals(1, reservation.getOthers().size());
    }

    @Test
    @DisplayName("Le paramètre isPmr est accepté mais non utilisé")
    void testIsPmrParameterAccepted() {
        Person person1 = new Person("Test1", "User1");
        Person person2 = new Person("Test2", "User2");

        assertDoesNotThrow(() -> {
            reservation.addPersonToReservation(person1, new int[]{0, 0}, true);
            reservation.addPersonToReservation(person2, new int[]{0, 1}, false);
        });

        assertEquals(2, reservation.getOthers().size());
    }
}
