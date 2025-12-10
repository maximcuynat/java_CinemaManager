import java.util.ArrayList;

public class Cinema
{
    // -------------------------------------------------------------------------------------- Attributs
    private String name;            // Nom du cinéma
    private ArrayList<Room> rooms;  // Liste des salle de cinéma

    // -------------------------------------------------------------------------------------- Constructeurs
    /**
     * Constructeur
     * @param name
     */
    Cinema(String name){
        setName(name);
        this.rooms = new ArrayList<>();
    }

    // -------------------------------------------------------------------------------------- Fonctions de gestions
    /**
     * Renommer un cinéma
     * @param newName
     */
    public void reName(String newName){
        this.name = newName;
    }

    /**
     * Ajouter une salle au cinéma
     * @param room
     */
    public void addRoom(Room room){
        this.rooms.add(room);
    }

    /**
     * Ajouter une liste de rooms déjà existante
     * @param rooms
     */
    public void addRooms(ArrayList<Room> rooms){
        setRooms(rooms);
    }

    /**
     * Afficher toutes les salles
     */
    public void displayAllRooms(){
        if(this.rooms.isEmpty()){
            System.out.println("Aucune salle dans ce cinéma");
            return;
        }

        System.out.println("Salles du cinéma :" + this.name + ":");
        for(Room room : this.rooms){
            System.out.println(" - " + room.getName());
        }
    }

    /**
     * Obtenir le nombre de salles
     * @return
     */
    public int getRoomCount(){
        return this.rooms.size();
    }

    // -------------------------------------------------------------------------------------- Setter
    /**
     * Setter of the Name
     * @param name
     */
    private void setName(String name){
        this.name = name;
    }

    /**
     * Setter of the Rooms of the Cinema
     * @param rooms
     */
    private void setRooms(ArrayList<Room> rooms){
        this.rooms = rooms;
    }

    // -------------------------------------------------------------------------------------- Getter
    /**
     * Return the Name of the Cinema
     * @return
     */
    public String getName(){
        return this.name;
    }

    /**
     * Return all Rooms of the Cinema
     * @return
     */
    public ArrayList<Room> getRooms(){
        return this.rooms;
    }
}