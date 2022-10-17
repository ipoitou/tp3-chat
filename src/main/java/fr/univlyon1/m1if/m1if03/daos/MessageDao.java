package fr.univlyon1.m1if.m1if03.daos;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import javax.naming.NameAlreadyBoundException;

import fr.univlyon1.m1if.m1if03.classes.Message;

public class MessageDao extends AbstractMapDao<Message> {
	private int keyCounter = 0;
	
	@Override
	public Serializable add(Message message) throws NameAlreadyBoundException {
		Serializable id = super.add(message);
		message.setId((Integer)id);
		
		return id;
	}
	
    public Collection<Message> getMessagesByUserLogin(String userLogin) {
        Collection<Message> foundMessages = new HashSet<>();

        for(Message message : super.collection.values()) {
            if(message.getUserLogin().equals(userLogin)) {
                foundMessages.add(message);
            }
        }

        return foundMessages;
    }

    public Collection<Message> getMessagesBySalonId(int salonId) {
        Collection<Message> foundMessages = new HashSet<>();

        for(Message message : super.collection.values()) {
            if(message.getSalonId() == salonId) {
                foundMessages.add(message);
            }
        }

        return foundMessages;
    }

    @Override
    protected Serializable getKeyForElement(Message message) {
    	Integer messageId = message.getId();
    	
    	if(messageId == null) {
            return keyCounter++;
        } else {
            return message.getId();
        }
    }
}
