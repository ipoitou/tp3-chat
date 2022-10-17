package fr.univlyon1.m1if.m1if03.filters;

import java.io.IOException;

import fr.univlyon1.m1if.m1if03.classes.Salon;
import fr.univlyon1.m1if.m1if03.classes.User;
import fr.univlyon1.m1if.m1if03.daos.SalonDao;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter(filterName = "Messages")
public class MessagesFilter extends HttpFilter {
	@Override
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String salonId = request.getParameter("salonId");
		if(salonId != null) { 
			request.getSession().setAttribute("salonId", salonId);
		} else {
			salonId = (String) request.getSession().getAttribute("salonId");
			
			//Si on a toujours pas récupéré l'id du salon
			if(salonId == null) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				
				return;
			}
		}
		
		User user = (User)request.getSession().getAttribute("user");
		SalonDao salonDao = (SalonDao) request.getServletContext().getAttribute("salonDao");
		Salon salon = salonDao.findById(salonId);
		
		if(salon.hasMembre(user) || salon.getOwnerLogin().equals(user.getLogin())) {
			chain.doFilter(request, response);
		} else {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Vous n'êtes pas membre de ce salon.");
		}
	}
}
