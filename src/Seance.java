import java.util.ArrayList;

public class Seance
{
    // -------------------------------------------------------------------------------------- Attributs
    private String date;
    private String time;
    private String movie;
    private Room room;
    private ArrayList<Reservation> reservations;  // ✓ ArrayList au lieu de []
    private char[][] seatMap;

    /** Nodes Liste des places de cinéma
     *
     * Liste des places de cinéma
     *         private boolean[][] seatMap = new boolean[5][10];
     *             0 1 2 3 4 5 6 7 8 9
     *         0 - 0 0 0 0 0 0 0 0 0 0
     *         1 - 0 0 0 0 0 0 0 0 0 0
     *         2 - 0 0 0 0 0 0 0 0 0 0
     *         3 - 0 0 0 0 0 0 0 0 0 0
     *         4 - 0 0 1 0 0 0 0 0 0 0
     *
     *                 Idée de salle différente (Romain)
     *             0 1 2 3 4 5 6 7 8 9
     *         0 - 0 0 X 0 0 0 0 X 0 0
     *         1 - 0 0 X 0 0 0 0 X 0 0
     *         2 - 0 0 X 0 0 0 0 x 0 0
     *         3 - 0 0 X 0 0 0 0 X 0 0
     *         4 - 0 0 X 0 0 0 0 X 0 0
     *
     *         Idée de salle différente (Romain) + PMR char '0' ''
     *             0 1 2 3 4 5 6 7 8 9
     *         0 - D x X P P P P X D x
     *         1 - D x X D x D x X D x
     *         2 - 0 0 X 0 0 0 0 X 0 0
     *         3 - 0 0 X 0 0 0 0 X 0 0
     *         4 - 0 0 X 0 0 0 0 X 0 0
     *
     *         room.seatMap
     *
     *             0 1 2 3 4 5 6 7 8 9
     *         0 - 1 1 X P P P P X D x
     *         1 - D x X D x D x X D x
     *         2 - 0 0 X 0 0 0 0 X 0 0
     *         3 - 0 0 X 0 0 0 0 X 0 0
     *         4 - 0 0 X 0 0 0 0 X 0 0
     *
     *         this.seatMap copie de room.seatMap
     *
     *         1 = Vraie OQP
     *         0 = Faux  Libre
     *         X = Pas Possible
     *         P = PMR
     *         O = Occuper
     *         D = Place doubles D D D D Ne pas pouvoir réserver une place double sur deux réservations différente.
     *         Donne row = 4 et col = 2
     *         -> 1
     */

    // -------------------------------------------------------------------------------------- Constructeurs
    /**
     * Setter d'une séance de Cinema
     * @param date
     * @param time
     * @param movie
     * @param room
     */
    Seance(String date, String time, String movie, Room room) {
        this.date = date;
        this.time = time;
        this.movie = movie;
        this.room = room;
        this.reservations = new ArrayList<>();
        this.seatMap = cloneSeatMap(room.getRoomSeatMap());
    }

    // -------------------------------------------------------------------------------------- Fonctions

    // ------------------------------------------- Réservation
    /**
     * Ajouter une réservation
     * @param reservation
     */
    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
    }

    /**
     * Supprimer une réservation
     * @param reservation
     */
    public void removeReservation(Reservation reservation) {
        this.reservations.remove(reservation);
    }

    // ------------------------------------------- Seatmap
    /**
     * Clone du template
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

    // ------------------------------------------- Seat
    /**
     * Système de réservation
     * @param seat
     * @param type
     * @return
     */
    public boolean reserveSeats(int[] seat, SeatType type) {
        if (!canReserve(seat[0], seat[1], type)) {
            return false;
        }

        applyReservation(seat[0], seat[1], type);
        return true;
    }

    /**
     * Possible de réserver
     * @param row
     * @param col
     * @param type
     * @return
     */
    private boolean canReserve(int row, int col, SeatType type) {
        if (row < 0 || row >= seatMap.length || col < 0 || col >= seatMap[0].length) {
            return false;
        }

        switch (type) {
            case NORMAL:
                return seatMap[row][col] == '0';
            case PMR:
                return seatMap[row][col] == 'P';
            case DOUBLE:
                return col + 1 < seatMap[0].length
                        && seatMap[row][col] == 'D'
                        && seatMap[row][col + 1] == 'x';
            default:
                return false;
        }
    }

    /**
     * Appliqer la réservation
     * @param row
     * @param col
     * @param type
     */
    private void applyReservation(int row, int col, SeatType type) {
        seatMap[row][col] = 'O';
        if (type == SeatType.DOUBLE) {
            seatMap[row][col + 1] = 'O';
        }
    }

    /**
     * Rollback depuis le template
     * @param seat
     * @param type
     */
    public void cancelReservation(int[] seat, SeatType type) {
        char[][] template = room.getRoomSeatMap();
        seatMap[seat[0]][seat[1]] = template[seat[0]][seat[1]];

        if (type == SeatType.DOUBLE) {
            seatMap[seat[0]][seat[1] + 1] = template[seat[0]][seat[1] + 1];
        }
    }

    // -------------------------------------------------------------------------------------- Setter
    private void setDate(String date){
        this.date = date;
    }

    private void setTime(String time){
        this.time = time;
    }

    private void setMovie(String movie){
        this.movie = movie;
    }

    private void setRoom(Room room){
        this.room = room;
    }

    private void setSeatMap(char[][] seatmap){
        this.seatMap = seatmap;
    }

    // -------------------------------------------------------------------------------------- Getters
    public String getDate(){
        return this.date;
    }

    public String getTime(){
        return this.time;
    }

    public String getMovie(){
        return this.movie;
    }

    public Room getRoom(){
        return this.room;
    }

    public char[][] getSeatMap(){
        return this.seatMap;
    }

    public ArrayList<Reservation> getReservations() {
        return this.reservations;
    }
}

enum SeatType { NORMAL, PMR, DOUBLE }
