package fr.univlyon1.m1if.m1if03.daos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;

import fr.univlyon1.m1if.m1if03.classes.Salon;
import fr.univlyon1.m1if.m1if03.classes.User;

public class SalonDao extends AbstractMapDao<Salon> {

    @Override
    protected Serializable getKeyForElement(Salon salon) {
        return salon.getId();
    }

    /**
     * Crée un nouveau salon et l'ajoute au DAO.
     * 
     * @param salonName Le nom du salon
     * @param salonOwner Le propriétaire du salon
     * 
     * @throws NameAlreadyBoundException Si un salon avec le même
     * nom et le même propriétaire existe déjà
     */
    public void createSalon(String salonName, User salonOwner) throws NameAlreadyBoundException {
    	Salon salon = new Salon(salonName, salonOwner);
    	
    	add(salon);
    }
    /**
     * Supprime un salon.<br>

     * @param name le nom du salon à supprimer
     */
    public void removeSalonByName(String name) throws NameNotFoundException {
        delete(findByName(name));
    }

    /**
     * Renvoie un salon à partir de son nom.<br>

     * @param name le nom à rechercher
     * @return le salon dont le nom est celui passé en paramètre
     * @throws NameNotFoundException Si le nom du salon à rechercher n'a pas été trouvé
     */
    public Salon findByName(String name) throws NameNotFoundException {
        for(Salon salon: this.collection.values()) {
            if(salon.getName().equals(name)) {
                return salon;
            }
        }
        throw new NameNotFoundException(name);
    }

    /**
     * Renvoie un salon à partir de son Id.<br>

     * @param id l'Id à rechercher
     * @return le salon dont l'Id est celui passé en paramètre
     */
    public Salon findById(String id) {
        return this.collection.get(id);
    }

    /**
     * Renvoie la liste des salons d'un propriétaire.<br>

     * @param owner le propriétaire des salons à rechercher
     * @return La liste des salons dont le propriétaire est celui passé en paramètre
     */
    public List<Salon> getSalonsByOwner(User owner) {
        List<Salon> salons = new ArrayList<Salon>();
        for(Salon salon: this.collection.values()) {
            if (salon.getOwnerLogin().equals(owner.getLogin())) {
                salons.add(salon);
            }
        }
        return salons;
    }

    /**
     * Renvoie la liste de tous les participants à un salon.<br>

     * @param salon le salon dont on veut chercher les membres
     * @return la liste des participants au salon passé en paramètre
     */
    public List<User> getMembresSalons(Salon salon) {
        return salon.getAllMembres();
    }

    /**
     * Renvoie la liste de tous les salons auxquels appartient un utilisateur.<br>

     * @param user l'utilisateur salon dont on veut chercher les salons
     * @return la liste de tous les salons aux quels appartient l'utilisateur passé en paramètre
     */
    public List<Salon> getSalonsOfMembre(User user) {
        List<Salon> salons = new ArrayList<Salon>();
        for(Salon salon: this.collection.values()) {
            if(salon.hasMembre(user)) {
                salons.add(salon);
            }
        }
        return salons;
    }
};
