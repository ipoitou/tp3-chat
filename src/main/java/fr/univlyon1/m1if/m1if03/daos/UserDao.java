package fr.univlyon1.m1if.m1if03.daos;

import java.io.Serializable;

import javax.naming.NameNotFoundException;

import fr.univlyon1.m1if.m1if03.classes.User;

public class UserDao extends AbstractMapDao<User> {
    //<editor-fold desc="Implémentation de la méthode de la classe générique">
    @Override
    protected Serializable getKeyForElement(User element) {
        return element.getLogin();
    }
    //</editor-fold>

    //<editor-fold desc="Méthode métier spécifique au DAO d'utilisateurs">
    /**
     * Renvoie un utilisateur à partir de son nom.<br>
     * renvoie le premier utilisateur trouvé avec le nom demandé<br>

     * @param name Le nom de l'utilisateur à rechercher
     * @return un <code>User</code> dont le nom est celui passé en paramètre
     * @throws NameNotFoundException Si le nom de l'utilisteur à rechercher n'a pas été trouvé
     */
    public User findByName(String name) throws NameNotFoundException {
        for(User user: this.collection.values()) {
            if(user.getName().equals(name)) {
                return user;
            }
        }
        throw new NameNotFoundException(name);
    }
    
    /**
     * Renvoie un utilisateur à partir de son login.<br>
     * renvoie le premier utilisateur trouvé avec le login demandé<br>

     * @param login Le login de l'utilisateur à rechercher
     * @return un <code>User</code> dont le login est celui passé en paramètre
     * @throws NameNotFoundException Si le login de l'utilisteur à rechercher n'a pas été trouvé
     */
    public User findByLogin(String login) throws NameNotFoundException {
        for(User user: this.collection.values()) {
            if(user.getLogin().equals(login)) {
                return user;
            }
        }
        throw new NameNotFoundException(login);
    }
    //</editor-fold>
}
