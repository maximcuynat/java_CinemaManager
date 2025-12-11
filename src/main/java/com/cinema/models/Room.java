package com.cinema.models;

public class Room {
    private String name;
    private Seance[] seances;
    private char[][] roomSeatMap;

    public Room(String name) {
        this.name = name;
        this.roomSeatMap = new char[5][10];
        initializeDefaultLayout();
    }

    public Room(String name, char[][] customLayout) {
        this.name = name;
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
        if (this.seances == null) {
            this.seances = new Seance[1];
            this.seances[0] = seance;
        } else {
            Seance[] newSeance = new Seance[this.seances.length + 1];
            for (int i = 0; i < this.seances.length; i++) {
                newSeance[i] = this.seances[i];
            }
            newSeance[this.seances.length] = seance;
            this.seances = newSeance;
        }
    }

    public void removeSeance(Seance seance) {
        if (this.seances == null) return;

        int index = -1;
        for (int i = 0; i < this.seances.length; i++) {
            if (this.seances[i] == seance) {
                index = i;
                break;
            }
        }

        if (index == -1) return;

        Seance[] newSeances = new Seance[this.seances.length - 1];
        for (int i = 0, j = 0; i < this.seances.length; i++) {
            if (i != index) {
                newSeances[j++] = this.seances[i];
            }
        }
        this.seances = newSeances;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setSeance(Seance[] seances) {
        this.seances = seances;
    }

    public String getName() {
        return this.name;
    }

    public Seance[] getSeances() {
        return this.seances;
    }

    public char[][] getRoomSeatMap() {
        return cloneSeatMap(this.roomSeatMap);
    }
}