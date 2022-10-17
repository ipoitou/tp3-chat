package fr.univlyon1.m1if.m1if03.servlets;

import java.io.IOException;

import fr.univlyon1.m1if.m1if03.daos.DemandeDao;
import fr.univlyon1.m1if.m1if03.daos.MessageDao;
import fr.univlyon1.m1if.m1if03.daos.SalonDao;
import fr.univlyon1.m1if.m1if03.daos.UserDao;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "InitServlet")
public class InitServlet extends HttpServlet {
	@Override
	public void init(ServletConfig config) throws ServletException {
        //Récupère le contexte applicatif
        ServletContext context = config.getServletContext();

        //Création des DAOs
        context.setAttribute("userDao", new UserDao());
        context.setAttribute("messageDao", new MessageDao());
        context.setAttribute("salonDao", new SalonDao());
        context.setAttribute("demandeDao", new DemandeDao());
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendError(HttpServletResponse.SC_FORBIDDEN);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendError(HttpServletResponse.SC_FORBIDDEN);
	}
}
