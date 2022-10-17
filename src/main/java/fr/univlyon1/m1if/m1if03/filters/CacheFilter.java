package fr.univlyon1.m1if.m1if03.filters;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter(filterName = "Cache")
public class CacheFilter extends HttpFilter {
	//Map d'id de salon -> date de dernière modification du salon
	private Map<String, Date> salonCache;
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		this.salonCache = new HashMap<>();
	}
	
	@Override
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String salonId = (String) request.getSession().getAttribute("salonId");
		if(salonId == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			
			return;
		}
		
		long lastModifiedRequest = request.getDateHeader("If-Modified-Since");
		if (request.getMethod().equals("POST")) {
			//Nouveau message posté dans le salon, on va mettre à jour le cache
			salonCache.put(salonId, new Date());
			
		} else if (request.getMethod().equals("GET")) {
			Date lastModifiedServer = salonCache.get(salonId);
			if(lastModifiedServer == null) {
				salonCache.put(salonId, new Date());
				lastModifiedServer = salonCache.get(salonId);
			}
			
			if(lastModifiedRequest >= lastModifiedServer.getTime()) {
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				
				return;
			}
			
			response.addDateHeader("Last-Modified", new Date().getTime());
		}
		
		chain.doFilter(request, response);
	}
}
