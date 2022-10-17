package fr.univlyon1.m1if.m1if03.utils;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

public abstract class EasyDebug {
	/**
     * Debug function qui affiche les paramètres d'une requête sous forme d'une liste.
     */
    public static void printRequestParameters(HttpServletRequest request) {
    	Map<String, String[]> params = request.getParameterMap();
    	
    	for(String param : params.keySet()) {
    		System.out.println(param + ": " +  params.get(param)[0]);
    	}
    }
}
