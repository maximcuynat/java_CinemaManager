package com.cinema.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de la classe Cinema")
public class CinemaTest {

    private Cinema cinema;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        cinema = new Cinema("Pathé Gaumont");
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Le constructeur crée un cinéma avec un nom")
    void testConstructor() {
        assertNotNull(cinema);
        assertEquals("Pathé Gaumont", cinema.getName());
        assertNotNull(cinema.getRooms());
        assertTrue(cinema.getRooms().isEmpty());
    }

    @Test
    @DisplayName("reName modifie correctement le nom du cinéma")
    void testReName() {
        cinema.reName("UGC Ciné Cité");
        assertEquals("UGC Ciné Cité", cinema.getName());
    }

    @Test
    @DisplayName("addRoom ajoute une salle au cinéma")
    void testAddRoom() {
        Room room = new Room("Salle 1");
        cinema.addRoom(room);

        assertEquals(1, cinema.getRoomCount());
        assertEquals("Salle 1", cinema.getRooms().get(0).getName());
    }

    @Test
    @DisplayName("addRoom ajoute plusieurs salles")
    void testAddMultipleRooms() {
        Room room1 = new Room("Salle 1");
        Room room2 = new Room("Salle 2");
        Room room3 = new Room("Salle 3");

        cinema.addRoom(room1);
        cinema.addRoom(room2);
        cinema.addRoom(room3);

        assertEquals(3, cinema.getRoomCount());
    }

    @Test
    @DisplayName("addRooms remplace la liste des salles")
    void testAddRooms() {
        ArrayList<Room> rooms = new ArrayList<>();
        rooms.add(new Room("Grande Salle"));
        rooms.add(new Room("Petite Salle"));

        cinema.addRooms(rooms);

        assertEquals(2, cinema.getRoomCount());
        assertEquals("Grande Salle", cinema.getRooms().get(0).getName());
        assertEquals("Petite Salle", cinema.getRooms().get(1).getName());
    }

    @Test
    @DisplayName("addRooms remplace complètement l'ancienne liste")
    void testAddRoomsReplacesExisting() {
        cinema.addRoom(new Room("Ancienne Salle"));
        assertEquals(1, cinema.getRoomCount());

        ArrayList<Room> newRooms = new ArrayList<>();
        newRooms.add(new Room("Nouvelle Salle"));
        cinema.addRooms(newRooms);

        assertEquals(1, cinema.getRoomCount());
        assertEquals("Nouvelle Salle", cinema.getRooms().get(0).getName());
    }

    @Test
    @DisplayName("getRoomCount retourne 0 pour un cinéma vide")
    void testGetRoomCountEmpty() {
        assertEquals(0, cinema.getRoomCount());
    }

    @Test
    @DisplayName("getRoomCount retourne le bon nombre de salles")
    void testGetRoomCountCorrectValue() {
        cinema.addRoom(new Room("Salle 1"));
        cinema.addRoom(new Room("Salle 2"));

        assertEquals(2, cinema.getRoomCount());
    }

    @Test
    @DisplayName("displayAllRooms affiche un message quand il n'y a pas de salles")
    void testDisplayAllRoomsEmpty() {
        cinema.displayAllRooms();

        String output = outContent.toString();
        assertTrue(output.contains("Aucune salle dans ce cinéma"));
    }

    @Test
    @DisplayName("displayAllRooms affiche toutes les salles")
    void testDisplayAllRoomsWithRooms() {
        cinema.addRoom(new Room("Salle A"));
        cinema.addRoom(new Room("Salle B"));

        cinema.displayAllRooms();

        String output = outContent.toString();
        assertTrue(output.contains("Pathé Gaumont"));
        assertTrue(output.contains("Salle A"));
        assertTrue(output.contains("Salle B"));
    }

    @Test
    @DisplayName("getName retourne le nom du cinéma")
    void testGetName() {
        assertEquals("Pathé Gaumont", cinema.getName());
    }

    @Test
    @DisplayName("getRooms retourne une ArrayList")
    void testGetRoomsReturnsList() {
        assertNotNull(cinema.getRooms());
        assertTrue(cinema.getRooms() instanceof ArrayList);
    }

    @Test
    @DisplayName("La liste des salles est modifiable")
    void testRoomsListIsMutable() {
        assertEquals(0, cinema.getRoomCount());

        cinema.addRoom(new Room("Test Room"));

        assertEquals(1, cinema.getRoomCount());
    }
}
