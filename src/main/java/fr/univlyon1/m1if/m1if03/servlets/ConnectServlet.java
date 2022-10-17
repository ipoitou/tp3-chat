package fr.univlyon1.m1if.m1if03.servlets;

import java.io.IOException;

import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;

import fr.univlyon1.m1if.m1if03.classes.User;
import fr.univlyon1.m1if.m1if03.daos.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Cette servlet récupère les infos de l'utilisateur dans sa session et affiche l'interface du chat (sans modifier l'URL).<br>
 * Renvoie une erreur 409 (Conflict) si le login de l'utilisateur existe déjà.<br>
 * &Agrave; noter le fait que l'URL à laquelle elle répond ("/chat") n'est pas le nom de la servlet.
 */
@WebServlet(name = "ConnectServlet", value = "/chat", loadOnStartup = 1)
public class ConnectServlet extends HttpServlet {
    private User createUserSession(HttpServletRequest request, String login, String name) {
    	HttpSession session = request.getSession(true);
    	
    	User user = new User(login, name);
    	session.setAttribute("user", user);
    	
    	return user;
    }
    
    /**
     * Ajoute l'utilisateur passé en paramètre au DAO des utilisateurs.
     *   
     * @param response La réponse HTTP. Un code status 409 CONFLICT sera renvoyé si
     * l'utilisateur existe déjà 
     * @param user L'utilisateur à ajouter
     * 
     * @return True ou false si tout s'est bien passé ou non.
     * False si l'utilisateur existait déjà dans le DAO, true s'il n'existait pas.
     * 
     * @throws IOException Voir doc de <a href="https://docs.oracle.com/javaee/6/api/javax/servlet/http/HttpServletResponse.html#sendError(int)">HttpServletResponse.sendError</a>
     */
    private boolean addUserToDao(HttpServletResponse response, User user) throws IOException {
    	UserDao userDao = (UserDao) this.getServletContext().getAttribute("userDao");
    	
		try {
			userDao.add(user);
		} catch (NameAlreadyBoundException e) {
			response.sendError(HttpServletResponse.SC_CONFLICT, "Le login " + user.getLogin() + " n'est plus disponible.");
			
			return false;
		}
		
		return true;
    }
    
    /**
     * Met à jour un utilisateur dans le DAO des utilisateurs à partir des paramètres
     * de la requête passée en argument de la méthode.
     * 
     * @param request La requête contenant les informations de l'utilisateur
     * qui doit être mis à jour
     * @param response La réponse de la requête. Un code status 400 BAD_REQUEST sera renvoyé si
     * l'utilisateur à mettre à jour n'a pas pu être trouvé à partir du login trouvé dans les
     * paramètres de la requête
     * 
     * @throws IOException Voir doc de <a href="https://docs.oracle.com/javaee/6/api/javax/servlet/http/HttpServletResponse.html#sendError(int)">HttpServletResponse.sendError</a>
     */
    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	String login = request.getParameter("login");
    	String name = request.getParameter("name");
    	
    	try {
    		UserDao userDao = (UserDao)this.getServletContext().getAttribute("userDao");
    		User user = userDao.findByLogin(login);
    		user.setName(name);
    		
		} catch (NameNotFoundException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String requestURL = request.getRequestURI().replace(request.getContextPath(), "");
    
    	String login = request.getParameter("login");
    	String name = request.getParameter("name");
    	String action = request.getParameter("action");
    	if(requestURL.equals("/chat") && 
    			login != null && !login.equals("") &&
    			name != null && !name.equals("") &&
    			action != null && action.equals("connectUser")) {
    		User user = createUserSession(request, login, name);
    		boolean allGood = addUserToDao(response, user);
    		if(!allGood) {
    			//Une réponse HTTP a déjà été donnée, on retourne sans rien faire de plus
    			return;
    	 	}
         } else if (action.equals("updateUser")) {
    		 updateUser(request, response);
    	 } else {
    		 response.sendRedirect("index.html");
    		 
    		 return;
    	 }
        
        // Utilise un RequestDispatcher pour "transférer" la requête à un autre objet, en interne du serveur.
        // Ceci n'est pas une redirection HTTP ; le client n'est pas informé de cette redirection et "voit" toujours la même URL.
        // Note :
        //     il existe deux méthodes pour transférer une requête (et une réponse) à l'aide d'un RequestDispatcher : include et forward
        //     voir les différences ici : https://docs.oracle.com/javaee/6/tutorial/doc/bnagi.html
        request.getRequestDispatcher("/WEB-INF/components/interface.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Ceci est une redirection HTTP ; le client est informé qu'il doit requêter une autre ressource
        response.sendRedirect("index.html");
    }
}
