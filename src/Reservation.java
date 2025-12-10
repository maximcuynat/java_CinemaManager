import java.util.ArrayList;

public class Reservation
{
    // -------------------------------------------------------------------------------------- Attributs
    private int idReservation;
    private Person holder;
    private ArrayList<Person> others;

    // -------------------------------------------------------------------------------------- Constructeurs
    /**
     * Constructeur avec seulement un Holder
     * @param holder
     */
    Reservation(Person holder){
        setHolder(holder);
        this.others = new ArrayList<>();
    }

    /**
     * Constructeur avec Others
     * @param holder
     * @param others
     */
    Reservation(Person holder, ArrayList<Person> others){
        setHolder(holder);
        this.others = others;
    }

    // -------------------------------------------------------------------------------------- Fonctions
    /**
     * Ajouter une personne à une réservation
     * @param person
     * @param seat
     * @param isPmr
     */
    public void addPersonToReservation(Person person, int[] seat, boolean isPmr){
        person.saveSeat(seat);
        this.others.add(person);
    }

    //TODO Fonction pour changer le Holder

    //TODO Fonction pour changer de place une personne

    //TODO Fonction pour supprimer une personne d'une réservation

    // -------------------------------------------------------------------------------------- Setter
    /**
     * Assigner un id de réservation
     * @param id
     */
    private void setIdReservation(int id){
        this.idReservation = id;
    }

    /**
     * Definir le prpriétaire de la réservation
     * @param holder
     */
    private void setHolder(Person holder){
        this.holder = holder;
    }

    /**
     * Définir les autre personnes attaché à la réservation
     * @param others
     */
    private void setOthers(ArrayList<Person> others){
        this.others = others;
    }

    // -------------------------------------------------------------------------------------- Getters
    /**
     * Renvoyer la liste des autre personne de la réservation
     * @return
     */
    public ArrayList<Person> getOthers(){
        return this.others;
    }

}
