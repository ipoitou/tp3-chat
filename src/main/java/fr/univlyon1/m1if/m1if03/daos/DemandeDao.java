package fr.univlyon1.m1if.m1if03.daos;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import javax.naming.NameAlreadyBoundException;

import fr.univlyon1.m1if.m1if03.classes.Demande;

public class DemandeDao extends AbstractMapDao<Demande> {
	private int keyCounter = 0;
	
	@Override
	public Serializable add(Demande demande) throws NameAlreadyBoundException {
		Serializable id = super.add(demande);
		demande.setId((Integer)id);
		
		return id;
	}
	
    public Collection<Demande> getDemandesByDemandeur(String userLogin) {
        Collection<Demande> demandesFound = new HashSet<>();

        for(Demande demande : super.collection.values()) {
            if(demande.getDemandeurLogin().equals(userLogin)) {
                demandesFound.add(demande);
            }
        }

        return demandesFound;
    }

    public Collection<Demande> getDemandesByDestinataire(String salon) {
        Collection<Demande> demandesFound = new HashSet<>();

        for(Demande demande : super.collection.values()) {
            if(demande.getSalonId().equals(salon)) {
                demandesFound.add(demande);
            }
        }

        return demandesFound;
    }

    public Collection<Demande> getDemandesByDestAndAction(String userLogin, String action) {
        Collection<Demande> demandesFound = new HashSet<>();

        for(Demande demande : super.collection.values()) {
            if(demande.getDemandeurLogin().equals(userLogin) && demande.getAction().equals(action)) {
                demandesFound.add(demande);
            }
        }

        return demandesFound;
    }

    @Override
    protected Serializable getKeyForElement(Demande demande) {
    	Integer demandeId = demande.getId();
    	
        if(demandeId == null) {
            return keyCounter++;
        } else {
            return demandeId;
        }
    }
}
