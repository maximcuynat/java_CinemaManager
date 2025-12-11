package com.cinema.models;

import java.util.ArrayList;

public class Room {
    private String name;
    private ArrayList<Seance> seances;
    private char[][] roomSeatMap;

    public Room(String name) {
        this.name = name;
        this.seances = new ArrayList<>();
        this.roomSeatMap = new char[5][10];
        initializeDefaultLayout();
    }

    public Room(String name, char[][] customLayout) {
        this.name = name;
        this.seances = new ArrayList<>();
        this.roomSeatMap = cloneSeatMap(customLayout);
    }

    private void initializeDefaultLayout() {
        char[][] defaultLayout = {
                {'D', 'x', 'X', 'P', 'P', 'P', 'P', 'X', 'D', 'x'},
                {'D', 'x', 'X', 'D', 'x', 'D', 'x', 'X', 'D', 'x'},
                {'0', '0', 'X', '0', '0', '0', '0', 'X', '0', '0'},
                {'0', '0', 'X', '0', '0', '0', '0', 'X', '0', '0'},
                {'0', '0', 'X', '0', '0', '0', '0', 'X', '0', '0'}
        };

        for (int row = 0; row < roomSeatMap.length; row++) {
            for (int col = 0; col < roomSeatMap[0].length; col++) {
                roomSeatMap[row][col] = defaultLayout[row][col];
            }
        }
    }

    private char[][] cloneSeatMap(char[][] template) {
        char[][] clone = new char[template.length][];
        for (int i = 0; i < template.length; i++) {
            clone[i] = template[i].clone();
        }
        return clone;
    }

    public void setRoomLayout(char[][] layout) {
        if (layout.length != roomSeatMap.length || layout[0].length != roomSeatMap[0].length) {
            throw new IllegalArgumentException("Layout dimensions incorrectes");
        }
        this.roomSeatMap = cloneSeatMap(layout);
    }

    public void setSeatType(int row, int col, char type) {
        if (row < 0 || row >= roomSeatMap.length || col < 0 || col >= roomSeatMap[0].length) {
            throw new IndexOutOfBoundsException("Position invalide");
        }

        if (type != '0' && type != 'X' && type != 'P' && type != 'D' && type != 'x') {
            throw new IllegalArgumentException("Type de siège invalide: " + type);
        }

        roomSeatMap[row][col] = type;
    }

    public boolean validateLayout() {
        for (int row = 0; row < roomSeatMap.length; row++) {
            for (int col = 0; col < roomSeatMap[0].length; col++) {
                if (roomSeatMap[row][col] == 'D') {
                    if (col + 1 >= roomSeatMap[0].length || roomSeatMap[row][col + 1] != 'x') {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void addSeance(Seance seance) {
        this.seances.add(seance);
    }

    public void removeSeance(Seance seance) {
        this.seances.remove(seance);
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setSeances(ArrayList<Seance> seances) {
        this.seances = seances;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Seance> getSeances() {
        return this.seances;
    }

    public char[][] getRoomSeatMap() {
        return cloneSeatMap(this.roomSeatMap);
    }
}