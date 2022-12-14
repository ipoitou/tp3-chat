package fr.univlyon1.m1if.m1if03.servlets;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;

import fr.univlyon1.m1if.m1if03.classes.Salon;
import fr.univlyon1.m1if.m1if03.classes.User;
import fr.univlyon1.m1if.m1if03.daos.SalonDao;
import fr.univlyon1.m1if.m1if03.daos.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "SalonServlet", value = "/salons")
public class SalonServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		SalonDao salonDao = (SalonDao) this.getServletContext().getAttribute("salonDao");
		
		request.setAttribute("userSalonsOwned", salonDao.getSalonsByOwner(user));
		request.setAttribute("userSalonsMember", salonDao.getSalonsOfMembre(user));
		request.setAttribute("salonsNotMember", getSalonsNotMember(salonDao, user));
		
		request.getRequestDispatcher("/WEB-INF/components/salons.jsp").include(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SalonDao salonDao = (SalonDao) this.getServletContext().getAttribute("salonDao");
		
		String salonName = request.getParameter("salon");
		User salonOwner = (User)request.getSession().getAttribute("user"); 
		
		if (request.getParameter("creation") != null) {
			createSalon(request, salonDao, salonOwner, salonName);
		} else if (request.getParameter("suppression") != null) {
			removeSalon(request, salonDao, salonOwner, salonName);
		} else if (request.getParameter("ajoutMembre") != null && request.getParameter("membre") != null) {
			addMemberToSalon(request, salonDao, salonOwner, salonName);
		}
		
		doGet(request, response);
	}
	
	/**
	 * Cr??e un salon ?? partir des informations donn??es et 
	 * l'ajoute au SalonDAO. Cette m??thode g??re les erreurs
	 * qui peuvent survenir 
	 *  
	 * @param request La requ??te actuelle
	 * @param salonDao Le SalonDao
	 * @param salonOwner Le propri??taire du salon 
	 * (la personne qui est en train de cr??er un salon)
	 * @param salonName Le nom du salon
	 */
	private void createSalon(HttpServletRequest request, SalonDao salonDao, User salonOwner, String salonName) {
		if(salonName.equals("")) {
			request.setAttribute("requestResult", "Vous devez donner un nom au salon.");
			
			return;
		}
		
    	try {
    		salonDao.createSalon(salonName, salonOwner);

    		request.setAttribute("requestResult", "Le salon \"" + salonName + "\" a ??t?? cr????.");
    	} catch (NameAlreadyBoundException e) {
    		request.setAttribute("requestResult", "Un salon \"" + salonName + "\" existe d??j??.");
    	}
	}
	
	/**
	 * Supprime un salon du SalonDAO en v??rifiant que 
	 * le User pass?? en param??tre est bien propri??taire 
	 * du salon et g??re les erreurs qui peuvent survenir
	 * 
	 * @param request La requ??te actuelle
	 * @param salonDao Le SalonDao
	 * @param salonOwner Le propri??taire du salon 
	 * (la personne qui est en train de cr??er un salon)
	 * @param salonName Le nom du salon
	 */
	private void removeSalon(HttpServletRequest request, SalonDao salonDao, User salonOwner, String salonName) {
		Salon salon = null;
    	
		try {
			salon = salonDao.findByName(salonName);
			
			//V??rification que l'utilisateur qui essaie de supprimer le salon est bien le propri??taire du salon
			if(!salonOwner.getLogin().equals(salon.getOwnerLogin())) {
				request.setAttribute("requestResult", "Vous n'??tes pas le propri??taire de ce salon. Vous ne pouvez pas le supprimer.");
			} else { //C'est bien le propri??taire
				salonDao.delete(salon);
				
				request.setAttribute("requestResult", "Le salon \"" + salonName + "\" a ??t?? supprim??.");
			}
		} catch (NameNotFoundException e1) {
			request.setAttribute("requestResult", "Le salon \"" + salonName + "\" n'existe pas.");
		}
	}
	
	/**
	 * Ajoute un membre ?? un salon et g??re les erreurs qui peuvent survenir
	 * 
	 * @param request La requ??te actuelle
	 * @param salonDao Le SalonDao
	 * @param salonOwner Le propri??taire du salon 
	 * (la personne qui est en train de cr??er un salon)
	 * @param salonName Le nom du salon
	 */
	private void addMemberToSalon(HttpServletRequest request, SalonDao salonDao, User salonOwner, String salonName) {
		UserDao userDao = (UserDao) this.getServletContext().getAttribute("userDao");
		
		String memberToAddName = request.getParameter("membre");
		if(memberToAddName.equals("")) {
			request.setAttribute("requestResult", "Vous devez entrez le login d'un utilisateur.");
			
			return;
		}
		
		User memberToAdd = null;
		
		try {
			memberToAdd = userDao.findOne(memberToAddName);
			
			//V??rification qu'on essaie pas d'ajouter le propri??taire lui m??me aux membres. Le propri??taire
			//n'est pas "membre" de ses salons
			if(memberToAdd.equals(salonOwner)) {
				request.setAttribute("requestResult", "Vous ne pouvez pas vous ajouter vous m??me au salon.");
				
				return;
			}
			
			try {
				Salon salon = salonDao.findByName(salonName);
				
				salon.addMembre(memberToAdd);
				
				request.setAttribute("requestResult", "L'utilisateur \"" + memberToAdd.getLogin() + "\" a ??t?? ajout?? au salon " + salonName);
			} catch (NameAlreadyBoundException e) {
				request.setAttribute("requestResult", "L'utilisateur \"" + memberToAdd.getLogin() + "\" est d??j?? membre du salon " + salonName);
			}
		} catch (NameNotFoundException e1) {
			request.setAttribute("requestResult", "L'utilisateur qui a pour login \"" + memberToAddName + "\" n'existe pas.");
		}
	}
	
	private Collection<Salon> getSalonsNotMember(SalonDao salonDao, User user) {
		Collection<Salon> salonsNotMember = new HashSet<>();
		for (Salon salon : salonDao.findAll()) {
			if (!salon.hasMembre(user) && !salon.getOwnerLogin().equals(user.getLogin())) {
					salonsNotMember.add(salon);
			}
		}
		
		return salonsNotMember;
	}
}
