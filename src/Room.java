public class Room
{
    // -------------------------------------------------------------------------------------- Attributs
    private String name;
    private Seance[] seances;
    private char[][] roomSeatMap;

    /**
     * Type de place et informations
     * Exmeple dimention et caractèristiques
     * - 0 dispo
     * - 1 indispo
     * - X Impossiblen, n'existe pas
     * - P Pmr
     * - D Place double toujours D D côte a côtes
     */

    // -------------------------------------------------------------------------------------- Constructeurs
    /**
     * Constructeur superchargé
     * @param name
     */
    Room(String name) {
        this.name = name;
        this.roomSeatMap = new char[5][10];
        initializeDefaultLayout();
    }

    Room(String name, char[][] customLayout) {
        this.name = name;
        this.roomSeatMap = cloneSeatMap(customLayout);
    }

    /**
     * Layout par défaut
     */
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

    /**
     * Clone profond pour éviter les références partagées
     * @param template
     * @return
     */
    private char[][] cloneSeatMap(char[][] template) {
        char[][] clone = new char[template.length][];
        for (int i = 0; i < template.length; i++) {
            clone[i] = template[i].clone();
        }
        return clone;
    }

    /**
     * Définir un layout complet
     * @param layout
     */
    public void setRoomLayout(char[][] layout) {
        if (layout.length != roomSeatMap.length || layout[0].length != roomSeatMap[0].length) {
            throw new IllegalArgumentException("Layout dimensions incorrectes");
        }
        this.roomSeatMap = cloneSeatMap(layout);
    }

    /**
     * Modifier une place spécifique du template
     * @param row
     * @param col
     * @param type
     */
    public void setSeatType(int row, int col, char type) {
        if (row < 0 || row >= roomSeatMap.length || col < 0 || col >= roomSeatMap[0].length) {
            throw new IndexOutOfBoundsException("Position invalide");
        }

        if (type != '0' && type != 'X' && type != 'P' && type != 'D' && type != 'x') {
            throw new IllegalArgumentException("Type de siège invalide: " + type);
        }

        roomSeatMap[row][col] = type;
    }

    /**
     * Validation du layout (places doubles cohérentes)
     * @return
     */
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

    // -------------------------------------------------------------------------------------- Fonctions de gestions
    /**
     * Ajouter une séance à la salle de cinéma
     * @param seance
     */
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

    /**
     * Supprimer une séance à la salle de cinéma
     * @param seance
     */
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

    // -------------------------------------------------------------------------------------- Setters
    /**
     * Setter Name
     * @param name
     */
    private void setName(String name){
        this.name = name;
    }

    /**
     * Setter Seances
     * @param seances
     */
    private void setSeance(Seance[] seances){
        this.seances = seances;
    }

    // -------------------------------------------------------------------------------------- Getters

    /**
     * Return the Name of the Room
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Return all Seances of the Room
     * @return
     */
    public Seance[] getSeances() {
        return this.seances;
    }

    /**
     * Renvoie un cloe de la seatMap
     * @return
     */
    public char[][] getRoomSeatMap() {
        return cloneSeatMap(this.roomSeatMap);
    }
}
