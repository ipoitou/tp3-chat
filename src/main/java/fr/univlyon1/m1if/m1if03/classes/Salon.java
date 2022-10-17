package fr.univlyon1.m1if.m1if03.classes;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NameAlreadyBoundException;

/**
 * Représente un salon privé.<br>
 * Un salon est créé par un utilisateur qui a donc un rôle de propriétaire.
 * Les utilisateurs peuvent s'inscrire et se désisncrire au salon y auront un rôle de membre.
 * On pourra attribuer aux autres utilisateurs un rôle de non-membre.
 * Les autres opérations de gestion des membres ne seront accessibles qu'au propriétaire du salon.
 * L'ajout d'un message et la récupération des messages sera accessible à tous les membres.
 * La suppression d'un message sera accessible uniquement à l'auteur de ce message et au propriétaire du salon.
 */
public class Salon {
    private final String id;
    private final String nom;
    private final String ownerLogin;
    private final String ownerName;
    private final List<User> membres = new ArrayList<>(); //TODO Ildès remplacer la liste de User et la liste de Message
                                                          // par des listes d'entier qui représente les id des Messages / des User.
    private final List<Message> messages = new ArrayList<>();

    //<editor-fold desc="Gestion des caractéristiques du salon">
    /**
     * Crée un salon avec le nom spécifié.
     * @param nom Nom du salon
     * @param owner Propriétaire du salon
     */
    public Salon(String nom, User owner) {
        this.nom = nom;
        this.ownerLogin = owner.getLogin();
        this.ownerName = owner.getName();
        this.id = ownerName + "/" + nom;
    }

    /**
     * Renvoie l'id du salon.
     * @return l'id du salon
     */
    public String getId() {
        return id;
    }

    /**
     * Renvoie le nom du salon.
     * @return le nom du salon
     */
    public String getName() {
        return nom;
    }

    /**
     * Renvoie le login du propriétaire du salon.
     * @return Le login du propriétaire du salon
     */
    public String getOwnerLogin() {
        return ownerLogin;
    }
    
    /**
     * Renvoie le nom du propriétaire du salon.
     * @return Le nom du propriétaire du salon
     */
    public String getOwnerName() {
		return ownerName;
	}
    
    //</editor-fold>

    //<editor-fold desc="Gestion des membres">
    /**
     * Ajoute un membre si celui-ci n'est pas déjà membre du salon.
     * @param membre User à rajouter comme membre
     * @return Index du membre dans la liste ; s'il était déjà membre, cette méthode renvoie l'index de sa première inscription dans le salon
     */
    public int addMembre(User membre) throws NameAlreadyBoundException {
        if(!this.membres.contains(membre)) {
            this.membres.add(membre);
            return this.membres.size() - 1;
        }
        throw new NameAlreadyBoundException();
    }

    /**
     * Supprime un membre si celui-ci est membre du salon.
     * @param membre User à supprimer des membres
     * @return un booléen qui indique si l'utilisateur a été supprimé
     */
    public boolean removeMembre(User membre) {
        // La méthode remove de List décale tous les éléments.
        // On préfère remplacer les éléments supprimés par NULL, quitte à augmenter la taille de la liste.
        int index = this.membres.indexOf(membre);
        if(index == -1) {
            return false;
        }
        this.membres.set(index, null);
        return true;
    }

    /**
     * Indique si un utilisateur est membre du salon.
     * @param user User à tester
     * @return un booléen répondant à la question
     */
    public boolean hasMembre(User user) {
        return this.membres.contains(user);
    }

    /**
     * Renvoie la liste complète des membres.
     * @return une <code>List&lt;User&gt;</code>
     */
    public List<User> getAllMembres() {
        return membres;
    }
    //</editor-fold>

    //<editor-fold desc="Gestion des messages">
    /**
     * Ajoute un message dans le salon.
     * @param message Message à ajouter
     * @return l'index du message ajouté
     */
    public int addMessage(Message message) {
        this.messages.add(message);
        return this.messages.size() -1;
    }

    /**
     * Supprime un message du salon.
     * @param message Message à supprimer
     * @return un booléen qui indique si le message a été supprimé
     */
    public boolean removeMessage(Message message) {
        // La méthode remove de List décale tous les éléments.
        // On préfère remplacer les éléments supprimés par NULL, quitte à augmenter la taille de la liste.
        int index = this.messages.indexOf(message);
        if(index == -1) {
            return false;
        }
        this.messages.set(index, null);
        return true;
    }

    /**
     * Indique si un message est dans le salon.
     * @param message Message à tester
     * @return un booléen répondant à la question
     */
    public boolean containsMessage(Message message) {
        return this.messages.contains(message);
    }

    /**
     * Renvoie la liste complète des messages.
     * @return une <code>List&lt;Message&gt;</code>
     */
    public List<Message> getAllMessages() {
        return messages;
    }
    //</editor-fold>
}
