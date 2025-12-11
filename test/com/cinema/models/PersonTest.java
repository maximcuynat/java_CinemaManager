package com.cinema.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de la classe Person")
public class PersonTest {

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person();
    }

    @Test
    @DisplayName("Le constructeur par défaut initialise correctement les champs")
    void testDefaultConstructor() {
        assertEquals(" - ", person.getPerson());
        assertNotNull(person.getPlace());
        assertEquals(2, person.getPlace().length);
        assertEquals(0, person.getPlace()[0]);
        assertEquals(0, person.getPlace()[1]);
    }

    @Test
    @DisplayName("Le constructeur avec paramètres valides crée une personne correctement")
    void testConstructorWithValidParameters() {
        Person validPerson = new Person("Jean", "Dupont");
        assertEquals("Jean - Dupont", validPerson.getPerson());
    }

    @Test
    @DisplayName("Le constructeur gère les prénoms vides")
    void testConstructorWithEmptyFirstName() {
        Person emptyFirstName = new Person("", "Dupont");
        assertEquals("No firstname - Dupont", emptyFirstName.getPerson());
    }

    @Test
    @DisplayName("Le constructeur gère les noms vides")
    void testConstructorWithEmptyLastName() {
        Person emptyLastName = new Person("Jean", "");
        assertEquals("Jean - No lastName", emptyLastName.getPerson());
    }

    @Test
    @DisplayName("Le constructeur gère les deux champs vides")
    void testConstructorWithBothEmpty() {
        Person emptyBoth = new Person("", "");
        assertEquals("No firstname - No lastName", emptyBoth.getPerson());
    }

    @Test
    @DisplayName("saveSeat enregistre correctement la position")
    void testSaveSeat() {
        int[] seat = {2, 5};
        person.saveSeat(seat);

        assertArrayEquals(new int[]{2, 5}, person.getPlace());
    }

    @Test
    @DisplayName("saveSeat met à jour la position précédente")
    void testSaveSeatOverwrite() {
        int[] firstSeat = {1, 3};
        int[] secondSeat = {4, 7};

        person.saveSeat(firstSeat);
        assertArrayEquals(new int[]{1, 3}, person.getPlace());

        person.saveSeat(secondSeat);
        assertArrayEquals(new int[]{4, 7}, person.getPlace());
    }

    @Test
    @DisplayName("getPlace retourne un tableau de 2 éléments")
    void testGetPlaceReturnsCorrectSize() {
        assertEquals(2, person.getPlace().length);
    }

    @Test
    @DisplayName("getPerson retourne le format correct")
    void testGetPersonFormat() {
        Person testPerson = new Person("Marie", "Martin");
        String result = testPerson.getPerson();

        assertTrue(result.contains(" - "));
        assertTrue(result.startsWith("Marie"));
        assertTrue(result.endsWith("Martin"));
    }

    @Test
    @DisplayName("getPlace retourne les valeurs par défaut avant saveSeat")
    void testGetPlaceDefaultValues() {
        int[] place = person.getPlace();
        assertEquals(0, place[0]);
        assertEquals(0, place[1]);
    }
}
