package fr.univlyon1.m1if.m1if03.servlets;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;

import fr.univlyon1.m1if.m1if03.classes.Demande;
import fr.univlyon1.m1if.m1if03.classes.Salon;
import fr.univlyon1.m1if.m1if03.classes.User;
import fr.univlyon1.m1if.m1if03.daos.DemandeDao;
import fr.univlyon1.m1if.m1if03.daos.SalonDao;
import fr.univlyon1.m1if.m1if03.daos.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "DemandeServlet", value = "/demandes")
public class DemandeServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    if (request.getParameter("idDemande") != null && request.getParameter("result") != null) {
	    	handleDemandeResponse(request, response);
	    }

	    request.setAttribute("userDemandesToId", getUserDemandesToId(request));
	    
	    request.getRequestDispatcher("/WEB-INF/components/demandes.jsp").include(request, response);
	}
	
	private Map<Demande, Integer> getUserDemandesToId(HttpServletRequest request) {
		Map<Demande, Integer> userDemandes = new LinkedHashMap<>();

		User user = (User) request.getSession().getAttribute("user");
		DemandeDao demandeDao = (DemandeDao) this.getServletContext().getAttribute("demandeDao");
		
		for(Demande demande : demandeDao.findAll()) {
			if(demande.getCibleDemandeLogin().equals(user.getLogin())) {
				userDemandes.put(demande, demande.getId());
			}
		}
		
		return userDemandes;
	}
	
	private void handleDemandeResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
		DemandeDao demandeDao = (DemandeDao) this.getServletContext().getAttribute("demandeDao");
		SalonDao salonDao = (SalonDao) this.getServletContext().getAttribute("salonDao");
		UserDao userDao = (UserDao) this.getServletContext().getAttribute("userDao");
		
        try {
            Demande demande = demandeDao.findOne(Integer.parseInt(request.getParameter("idDemande")));
			Salon salon = salonDao.findById(demande.getSalonId());
            
			User userCible = userDao.findByLogin(demande.getDemandeurLogin());
			
            switch (request.getParameter("result")) {
                case "accept":
                    switch (demande.getAction()) {
                        case "register":
                        	salon.addMembre(userCible);
                            break;
                            
                        case "quit":
                        	salon.removeMembre(userCible);
                            break;
                        default:
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    }
                    demande.accept();
                    break;
                case "refuse":
                    demande.refuse();
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
            
            request.setAttribute("requestResult", "Votre réponse a bien été envoyée.");
        } catch (NameAlreadyBoundException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (NameNotFoundException e) {
        	response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String demandeurLogin = request.getParameter("demandeurLogin");
		String salonOwnerLogin = request.getParameter("salonOwnerLogin");
		String salonId = request.getParameter("salonId");
		String action = request.getParameter("action");
		
		if (salonOwnerLogin != null && salonId != null && 
			demandeurLogin != null && action != null) {
			demandeurLogin = request.getParameter("demandeurLogin");
			salonOwnerLogin = request.getParameter("salonOwnerLogin");
			salonId = request.getParameter("salonId");
			action = request.getParameter("action");
			
			DemandeDao demandeDao = (DemandeDao) this.getServletContext().getAttribute("demandeDao");
			try {
				demandeDao.add(new Demande(demandeurLogin, salonOwnerLogin, salonId, action));
			} catch (NameAlreadyBoundException e) {
				//Cette exception ne devrait jamais être levée
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
			
			request.setAttribute("requestResult", "Votre demande a bien été envoyée");
			request.getRequestDispatcher("/WEB-INF/components/salons.jsp").include(request, response);
		}
	}
}
