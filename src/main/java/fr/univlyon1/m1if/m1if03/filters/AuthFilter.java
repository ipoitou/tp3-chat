package fr.univlyon1.m1if.m1if03.filters;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtre d'authentification.
 * N'autorise l'accès qu'aux clients ayant déjà une session existante ou ayant rempli le formulaire de la page <code>index.html</code>.
 * Dans ce dernier cas, le filtre crée la session de l'utilisateur, crée un objet User et l'ajoute en attribut de la session.
 * Laisse toutefois passer les URLs "/" et "/index.html".
 */
@WebFilter(filterName = "Auth")
public class AuthFilter extends HttpFilter {
    private final String[] whitelist = {"/", "/index.html", "/deco", "/css/style.css"};

    @Override
    public void init(FilterConfig config) throws ServletException {
        super.init(config);
    }

    /**
     * Filtre les accès en fonction des URLs demandées et de l'existence de la session de l'utilisateur.
     * Autorise les requêtes suivantes :
     * <ol>
     *     <li>URLs ne nécessitant pas d'authentification (whitelist)</li>
     *     <li>Requêtes d'authentification</li>
     *     <li>Requêtes d'utilisateurs déjà authentifiés</li>
     * </ol>
     * Dans les cas contraires, renvoie un code d'erreur HTTP 403 (Forbidden).
     * @param request Voir doc de <a href="https://jakarta.ee/specifications/platform/9/apidocs/jakarta/servlet/http/httpfilter#doFilter-jakarta.servlet.http.HttpServletRequest-jakarta.servlet.http.HttpServletResponse-jakarta.servlet.FilterChain-">HttpFilter.doFilter</a>
     * @param response Voir doc de <a href="https://jakarta.ee/specifications/platform/9/apidocs/jakarta/servlet/http/httpfilter#doFilter-jakarta.servlet.http.HttpServletRequest-jakarta.servlet.http.HttpServletResponse-jakarta.servlet.FilterChain-">HttpFilter.doFilter</a>
     * @param chain Voir doc de <a href="https://jakarta.ee/specifications/platform/9/apidocs/jakarta/servlet/http/httpfilter#doFilter-jakarta.servlet.http.HttpServletRequest-jakarta.servlet.http.HttpServletResponse-jakarta.servlet.FilterChain-">HttpFilter.doFilter</a>
     * @throws IOException Voir doc de <a href="https://jakarta.ee/specifications/platform/9/apidocs/jakarta/servlet/http/httpfilter#doFilter-jakarta.servlet.http.HttpServletRequest-jakarta.servlet.http.HttpServletResponse-jakarta.servlet.FilterChain-">HttpFilter.doFilter</a>
     * @throws ServletException Voir doc de <a href="https://jakarta.ee/specifications/platform/9/apidocs/jakarta/servlet/http/httpfilter#doFilter-jakarta.servlet.http.HttpServletRequest-jakarta.servlet.http.HttpServletResponse-jakarta.servlet.FilterChain-">HttpFilter.doFilter</a>
     */
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Permet de retrouver la fin de l'URL (après l'URL du contexte) -> indépendant de l'URL de déploiement
        String url = request.getRequestURI().replace(request.getContextPath(), "");

        // 1) Laisse passer les URLs ne nécessitant pas d'authentification
        for(String tempUrl: whitelist) {
            if(url.equals(tempUrl)) {
                chain.doFilter(request, response);
                return;
            }
        }

        //L'utilisateur veut se connecter. On le laisse passer
        if(request.getParameter("action") != null &&
           request.getParameter("action").equals("connectUser")) {
        	chain.doFilter(request, response);
        	
        	return;
        }

        // 3) Traite les requêtes qui doivent être authentifiées
        // Note :
        //   le paramètre false dans request.getSession(false) permet de récupérer null si la session n'est pas déjà créée.
        //   Sinon, l'appel de la méthode getSession() la crée automatiquement.

        if(request.getSession(false) != null) {
            chain.doFilter(request, response);
            return;
        }
        // Bloque les autres requêtes
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vous devez vous connecter pour accéder au site ici.");
    }
}
