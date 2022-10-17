package fr.univlyon1.m1if.m1if03.servlets;

import java.io.IOException;

import javax.naming.NameNotFoundException;

import fr.univlyon1.m1if.m1if03.classes.Message;
import fr.univlyon1.m1if.m1if03.classes.Salon;
import fr.univlyon1.m1if.m1if03.classes.User;
import fr.univlyon1.m1if.m1if03.daos.SalonDao;
import fr.univlyon1.m1if.m1if03.exceptions.SalonNotFoundException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//TODO question prof:
//Est-ce que ça serait pas mieux d'utiliser des entiers pour les id des classes plutôt que des châines de caractères
//Niveau performance et niveau façon de faire (c'est pas plus courant d'utiliser des entiers plutôt que des String pour les id ?)

/**
 * Cette servlet gère la conversation dans le chat.
 * Elle récupère les nouveaux messages et affiche la liste des messages.
 */
@WebServlet(name = "MessagesServlet", value = "/messages")
public class MessagesServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
        	Salon salon = getSalon(request);
        	
        	//TODO question prof:
        	//Avant qu'on commente la ligne en dessous, le placement de l'id du salon dans la session était fait par la servlet
        	//le problème en faisant comme ça est que on n'avait pas l'id du salon dans la session avant que le filtre Message
        	//fasse son travail (puisque le filtre message s'exécute avant la servlet Message). Ça veut dire que on arrivait
        	//dans le filtre message sans avoir l'id de session et ça plantait.
        	//Solution: placer l'id du salon dans la session DANS le filtre message et du coup on ne fait plus ça dans la servlet
        	//mais est-ce que c'est vraiment une bonne organisation de code de faire ça ?
        	
        	//Place le salon dans un attribut de session
        	//request.getSession().setAttribute("salonId", salon.getId());
            
            // Place les autres données dont messages.jsp va avoir besoin dans la requête
            request.setAttribute("nbUserOnline", salon.getAllMembres().size() + 1);
            //+1 pour le proprio
            request.setAttribute("salonMessages", salon.getAllMessages());
            
            // Transfère la gestion de l'interface à une JSP
            request.getRequestDispatcher("/WEB-INF/components/messages.jsp").include(request, response);
        } catch (SalonNotFoundException | NameNotFoundException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Salon salon = null;
            
			try {
				salon = getSalon(request);
			} catch (NameNotFoundException e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				
				return;
			}

            // Crée un nouveau message et l'ajoute au salon
            Message m = new Message((User) request.getSession().getAttribute("user"), request.getParameter("text"));
            salon.addMessage(m);
            
            // Reprend le comportement des requêtes GET
            doGet(request, response);
        } catch (SalonNotFoundException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            
            return;
        }
    }

    /**
     * Retrouve un salon dans la jungle des DAOs à partir des informations contenues dans la requête.
     * @param request Requête HTTP qui doit contenir soit des paramètres <code>owner</code> et <code>salon</code>,
     *                soit une session utilisateur avec un attribut <code>salon</code> déjà positionné
     * @return l'instance de <code>Salon</code> dont on veut la conversation
     * @throws SalonNotFoundException Si les paramètres n'existent pas, ne correspondent pas, et si aucun attribut de session n'est positionné
     * @throws NameNotFoundException 
     */
    private Salon getSalon(HttpServletRequest request) throws SalonNotFoundException, NameNotFoundException {
        SalonDao salonDao = (SalonDao) this.getServletContext().getAttribute("salonDao");
        Salon salon = salonDao.findById(request.getParameter("salonId"));
        
        if (salon != null) {
            return salon;
        } else {
        	salon = salonDao.findById((String) request.getSession().getAttribute("salonId"));
        	
        	if(salon != null) {
        		return salon;
        	}
        }
        
        throw new SalonNotFoundException("Impossible d'identifier le salon," +
                " soit parce que les paramètres passés à la requête ne le permettent pas, soit parce que ces paramètres sont absents.");
    }
}
