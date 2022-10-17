package fr.univlyon1.m1if.m1if03.classes;

/**
 * Représente une demande faite par un utilisateur à un autre utilisateur au sujet d'une ressource.<br>
 * Dans un premier temps, les instances seront uniquement des demandes d'adhésion à des salons.
 * Plus tard, ce pourront être des demandes de modification / suppression de ressources.
 */
public class Demande {
	private final String demandeurLogin;
	private final String cibleDemandeLogin;
    private final String salonId;
    private final String action;
    private Integer id;

    private enum State {
        pending,
        accepted,
        refused
    };
    private State state;

    /**
     * Crée une nouvelle demande.
     * @param demandeurLogin Login de l'utilisateur à l'origine de la demande
     * @param cibleDemandeLogin Login de l'utilisateur a qui est destinée la demande
     * @param salonId L'id du salon concerné par la demande
     * @param action Action demandée par l'utilisateur ; pour un salon, c'est soit une adhésion (register), soit une désinscription (quit)
     */
    public Demande(String demandeurLogin, String cibleDemandeLogin, String salonId, String action) {
        this.demandeurLogin = demandeurLogin;
        this.cibleDemandeLogin = cibleDemandeLogin;
        this.salonId = salonId;
        this.action = action;
        this.id = null;

        this.state = State.pending;
    }

    /**
     * Renvoie le login de l'utilisateur à l'origine de la demande.
     * @return Le login de l'utilisateur à l'origine de la demande
     */
    public String getDemandeurLogin() {
        return demandeurLogin;
    }
    
    /**
     * Retourn l'id de la demande.
     * @return L'id de la demande
     */
    public Integer getId() {
		return id;
	}
    
    /**
     * Redéfinit l'id de la demande.
     * 
     * @param id Le nouvel id de la demande
     */
    public void setId(Integer id) {
		this.id = id;
	}
    
    /**
     * Renvoie le login de l'utilisateur a qui est destinée la demande.
     * @return Le login de l'utilisateur a qui est destinée la demande
     */
    public String getCibleDemandeLogin() {
		return cibleDemandeLogin;
	}

    /**
     * Renvoie l'id du salon concerné par la demande.
     * @return L'id du salon concerné par la demande
     */
    public String getSalonId() {
        return salonId;
    }

    /**
     * Renvoie l'action demandée par l'utilisateur.
     * @return L'action demandée par l'utilisateur (<code>register</code> ou <code>quit</code>)
     */
    public String getAction() {
        return action;
    }

    /**
     * Renvoie l'état courant de la demande.
     * @return L'état courant de la demande
     */
    public String getState() {
        return state == State.pending ? "En cours" : (state == State.accepted ? "Acceptée" : "Refusée");
    }
    
    /**
     * Accepte la demande.
     * L'état de la demande est modifié pour <code>accepted</code>
     */
    public void accept() {
        this.state = State.accepted;
    }

    /**
     * Refuse la demande.
     * L'état de la demande est modifié pour <code>refused</code>
     */
    public void refuse() {
        this.state = State.refused;
    }
}
