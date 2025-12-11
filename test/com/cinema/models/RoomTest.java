package com.cinema.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de la classe Room")
public class RoomTest {

    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room("Salle Test");
    }

    @Test
    @DisplayName("Le constructeur par défaut crée une salle avec layout standard")
    void testDefaultConstructor() {
        assertNotNull(room);
        assertEquals("Salle Test", room.getName());
        assertNotNull(room.getSeances());
        assertTrue(room.getSeances().isEmpty());
        assertNotNull(room.getRoomSeatMap());
    }

    @Test
    @DisplayName("Le constructeur par défaut crée un plan de 5x10")
    void testDefaultConstructorSeatMapSize() {
        char[][] seatMap = room.getRoomSeatMap();
        assertEquals(5, seatMap.length);
        assertEquals(10, seatMap[0].length);
    }

    @Test
    @DisplayName("Le constructeur avec layout custom utilise le layout fourni")
    void testCustomLayoutConstructor() {
        char[][] customLayout = {
                {'0', '0', '0'},
                {'0', '0', '0'},
                {'0', '0', '0'}
        };
        Room customRoom = new Room("Custom Room", customLayout);

        char[][] resultMap = customRoom.getRoomSeatMap();
        assertEquals(3, resultMap.length);
        assertEquals(3, resultMap[0].length);
    }

    @Test
    @DisplayName("Le layout par défaut contient des places doubles")
    void testDefaultLayoutHasDoubleSeats() {
        char[][] seatMap = room.getRoomSeatMap();
        boolean hasDoubleSeats = false;

        for (char[] row : seatMap) {
            for (char seat : row) {
                if (seat == 'D' || seat == 'x') {
                    hasDoubleSeats = true;
                    break;
                }
            }
        }

        assertTrue(hasDoubleSeats);
    }

    @Test
    @DisplayName("Le layout par défaut contient des places PMR")
    void testDefaultLayoutHasPMRSeats() {
        char[][] seatMap = room.getRoomSeatMap();
        boolean hasPMR = false;

        for (char[] row : seatMap) {
            for (char seat : row) {
                if (seat == 'P') {
                    hasPMR = true;
                    break;
                }
            }
        }

        assertTrue(hasPMR);
    }

    @Test
    @DisplayName("setRoomLayout modifie le layout de la salle")
    void testSetRoomLayout() {
        char[][] newLayout = new char[5][10];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                newLayout[i][j] = '0';
            }
        }

        room.setRoomLayout(newLayout);
        char[][] resultMap = room.getRoomSeatMap();

        assertEquals('0', resultMap[0][0]);
        assertEquals('0', resultMap[4][9]);
    }

    @Test
    @DisplayName("setRoomLayout lance une exception si les dimensions sont incorrectes")
    void testSetRoomLayoutInvalidDimensions() {
        char[][] invalidLayout = {
                {'0', '0'},
                {'0', '0'}
        };

        assertThrows(IllegalArgumentException.class, () -> {
            room.setRoomLayout(invalidLayout);
        });
    }

    @Test
    @DisplayName("setSeatType modifie correctement le type d'un siège")
    void testSetSeatType() {
        room.setSeatType(0, 0, 'P');
        char[][] seatMap = room.getRoomSeatMap();

        assertEquals('P', seatMap[0][0]);
    }

    @Test
    @DisplayName("setSeatType lance une exception pour une position invalide")
    void testSetSeatTypeInvalidPosition() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            room.setSeatType(10, 10, '0');
        });

        assertThrows(IndexOutOfBoundsException.class, () -> {
            room.setSeatType(-1, 0, '0');
        });
    }

    @Test
    @DisplayName("setSeatType lance une exception pour un type invalide")
    void testSetSeatTypeInvalidType() {
        assertThrows(IllegalArgumentException.class, () -> {
            room.setSeatType(0, 0, 'Z');
        });
    }

    @Test
    @DisplayName("setSeatType accepte tous les types valides")
    void testSetSeatTypeValidTypes() {
        assertDoesNotThrow(() -> {
            room.setSeatType(0, 0, '0');
            room.setSeatType(1, 0, 'X');
            room.setSeatType(2, 0, 'P');
            room.setSeatType(3, 0, 'D');
            room.setSeatType(4, 0, 'x');
        });
    }

    @Test
    @DisplayName("validateLayout retourne true pour le layout par défaut")
    void testValidateLayoutDefault() {
        assertTrue(room.validateLayout());
    }

    @Test
    @DisplayName("validateLayout détecte les places doubles invalides")
    void testValidateLayoutInvalidDouble() {
        room.setSeatType(0, 0, 'D');
        room.setSeatType(0, 1, '0');

        assertFalse(room.validateLayout());
    }

    @Test
    @DisplayName("validateLayout accepte les places doubles valides")
    void testValidateLayoutValidDouble() {
        room.setSeatType(2, 2, 'D');
        room.setSeatType(2, 3, 'x');

        assertTrue(room.validateLayout());
    }

    @Test
    @DisplayName("addSeance ajoute une séance à la salle")
    void testAddSeance() {
        Seance seance = new Seance("10/12/2025", "20h00", "Test Film", room);
        room.addSeance(seance);

        assertEquals(1, room.getSeances().size());
        assertEquals(seance, room.getSeances().get(0));
    }

    @Test
    @DisplayName("addSeance ajoute plusieurs séances")
    void testAddMultipleSeances() {
        Seance seance1 = new Seance("10/12/2025", "14h00", "Film 1", room);
        Seance seance2 = new Seance("10/12/2025", "17h00", "Film 2", room);
        Seance seance3 = new Seance("10/12/2025", "20h00", "Film 3", room);

        room.addSeance(seance1);
        room.addSeance(seance2);
        room.addSeance(seance3);

        assertEquals(3, room.getSeances().size());
    }

    @Test
    @DisplayName("removeSeance supprime une séance de la salle")
    void testRemoveSeance() {
        Seance seance1 = new Seance("10/12/2025", "14h00", "Film 1", room);
        Seance seance2 = new Seance("10/12/2025", "17h00", "Film 2", room);

        room.addSeance(seance1);
        room.addSeance(seance2);
        assertEquals(2, room.getSeances().size());

        room.removeSeance(seance1);
        assertEquals(1, room.getSeances().size());
        assertEquals(seance2, room.getSeances().get(0));
    }

    @Test
    @DisplayName("removeSeance ne fait rien si la séance n'existe pas")
    void testRemoveSeanceNotFound() {
        Seance seance1 = new Seance("10/12/2025", "14h00", "Film 1", room);
        Seance seance2 = new Seance("10/12/2025", "17h00", "Film 2", room);

        room.addSeance(seance1);
        assertEquals(1, room.getSeances().size());

        room.removeSeance(seance2);
        assertEquals(1, room.getSeances().size());
    }

    @Test
    @DisplayName("getName retourne le nom de la salle")
    void testGetName() {
        assertEquals("Salle Test", room.getName());
    }

    @Test
    @DisplayName("getSeances retourne une ArrayList")
    void testGetSeancesReturnsList() {
        assertNotNull(room.getSeances());
        assertTrue(room.getSeances().isEmpty());
    }

    @Test
    @DisplayName("getRoomSeatMap retourne une copie défensive")
    void testGetRoomSeatMapDefensiveCopy() {
        char[][] originalMap = room.getRoomSeatMap();
        originalMap[0][0] = 'Z';

        char[][] newMap = room.getRoomSeatMap();
        assertNotEquals('Z', newMap[0][0]);
    }

    @Test
    @DisplayName("Le constructeur custom initialise une liste de séances vide")
    void testCustomConstructorInitializesSeances() {
        char[][] customLayout = new char[5][10];
        Room customRoom = new Room("Custom", customLayout);

        assertNotNull(customRoom.getSeances());
        assertTrue(customRoom.getSeances().isEmpty());
    }
}
