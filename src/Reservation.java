public class Reservation {
    private int idReservation;
    private Person holder;
    private Person[] others;

    Reservation(Person holder){
        this.holder = holder;
    }

    Reservation(Person holder, Person[] others){
        this.holder = holder;
        this.others = others;
    }

    public void addPersonToReservation(Person person)
    {
        if (this.others == null){
            // Si le tableau est vide, créer un tableau de taille 1
            this.others = new Person[1];
            this.others[0] = person;
        } else {
            Person[] newOthers = new Person[this.others.length + 1];

            // Copier les anciens
            for(int i = 0; i < this.others.length; i++) {
                newOthers[i] = this.others[i];
            }

            // Ajouter la nouvelle personne
            newOthers[this.others.length] = person;

            // Remplacer l'ancien tableau
            this.others = newOthers;
        }
    }
}
