public class Person
{
    // -------------------------------------------------------------------------------------- Attributs
    private String firstName;
    private String lastName;
    int[] place = new int[2];

    // -------------------------------------------------------------------------------------- Constructeurs
    /**
     * Constructeur Vide.
     */
    Person() {
        this.firstName = "";
        this.lastName = "";
    }

    /**
     * Constructeur avec Prénom et Nom
     * @param firstName
     * @param lastName
     */
    Person(String firstName, String lastName){
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    // -------------------------------------------------------------------------------------- Fonctions
    /**
     * Fonction pour sauvegarder l'emplacement de la personne
     * @param seat
     */
    public void saveSeat(int[] seat){
        this.place[0] = seat[0];
        this.place[1] = seat[1];
    }

    // -------------------------------------------------------------------------------------- Setters
    /**
     * Vérification si firstName n'est pas vide sinon : No firstname
     * @param firstName
     */
    private void setFirstName(String firstName){
        if(!firstName.isEmpty())
        {
            this.firstName = firstName;
        } else {
            this.firstName = "No firstname";
        }
    }

    /**
     * Vérification si lastName n'est pas vide sinon : No lastName
     * @param lastName
     */
    private void setLastName(String lastName){
        if(!lastName.isEmpty())
        {
            this.lastName = lastName;
        } else {
            this.lastName = "No lastName";
        }
    }

    // -------------------------------------------------------------------------------------- Getter
    /**
     * Renvoie "firstName lastName"
     * @return
     */
    public String getPerson() {
        return this.firstName + " - " + this.lastName;
    }
}