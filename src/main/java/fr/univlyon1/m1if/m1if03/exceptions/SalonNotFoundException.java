package fr.univlyon1.m1if.m1if03.exceptions;

/**
 * Exception lancée par une méthode qui soit ne trouve pas un salon, soit n'a pas les données pour l'identifier.
 */
public class SalonNotFoundException extends Exception {
    public SalonNotFoundException(String message) {
        super(message);
    }
}
