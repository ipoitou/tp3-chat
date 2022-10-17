package fr.univlyon1.m1if.m1if03.classes;

/**
 * Messages échangés dans le chat.
 * Cette classe pourra être améliorée dans la suite de l'UE.
 */
public class Message {
	private final User user;
    
    private String text;
    private Integer id;
    private int salonId;

    public Message(User user, String text) {
    	this.user = user;
        this.text = text;
        this.id = null;
    }

    public Integer getId() {
        return id;
    }
    
    public int getSalonId() {
        return salonId;
    }
    
    public String getText() {
        return text;
    }
    
    public String getUserName() {
		return user.getName();
	}

    public String getUserLogin() {
        return user.getLogin();
    }

    public void setId(Integer id) {
		this.id = id;
	}

    public void setSalonId(int salonId) {
		this.salonId = salonId;
	}

    public void setText(String text) {
        this.text = text;
    }
}
