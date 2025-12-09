public class Seance {
    /**
     * Créer une séance, une séance à une date et un film
     */
    private String date;
    private String movie;
    private Reservation[] reservations;

    Seance(String date, String movie){
        this.date = date;
        this.movie = movie;
    }

    public void addReservation(Reservation res)
    {
        if (this.reservations == null){
            // Si le tableau est vide, créer un tableau de taille 1
            this.reservations = new Reservation[1];
            this.reservations[0] = res;
        } else {
            Reservation[] newReservation = new Reservation[this.reservations.length + 1];

            // Copier les anciens
            for(int i = 0; i < this.reservations.length; i++) {
                newReservation[i] = this.reservations[i];
            }

            // Ajouter la nouvelle personne
            newReservation[this.reservations.length] = res;

            // Remplacer l'ancien tableau
            this.reservations = newReservation;
        }
    }
}
