package fr.univlyon1.m1if.m1if03.servlets;

import java.io.IOException;

import fr.univlyon1.m1if.m1if03.classes.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "UserServlet", value = "/user")
public class UserServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/components/user.jsp").include(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
        String login = request.getParameter("login");
        String name = request.getParameter("name");
        String action = request.getParameter("action");
        
        if(login != null && name != null && action != null) {
        	if(action.equals("updateUser")) {
        		if(name.strip().equals("")) {
        			request.setAttribute("requestResult", "Vous ne pouvez pas choisir un " +
        				"nom vide (ne contenant que des espaces / tabulations / ... ).");
        			request.getRequestDispatcher("/WEB-INF/components/user.jsp").include(request, response);
        			
        			return;
        		} else {
        			((User) request.getSession().getAttribute("user")).setName(name);
        		}
        	}
        }
        
        //TODO question prof: 
        // ------ Comment faire pour que quand l'utilisateur essaye de changer son nom avec un nom 
        //vide, ce qui est interdit, on reste sur la page /user avec un feedback mais en même temps 
        //(et c'est ça le problème), quand l'utilisateur change correctement son nom, on retourne sur 
        //la page de chat ? Le problème c'est que dans user.jsp, le formulaire a pour target "_top" ce 
        //qui nous empêche d'avoir à la fois de rester sur la page /user quand l'utilisateur ne change 
        //pas correctement son nom et à la fois de retourner vers la page /chat en entier quand 
        //l'utilisateur a bien changé son nom
        request.getRequestDispatcher("/WEB-INF/components/interface.jsp").forward(request, response);
	}
}
