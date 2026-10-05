package jsfbench.beans;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jsfbench.entity.User;

@Named("authenticator")
@jakarta.enterprise.context.RequestScoped
public class AuthenticatorAction extends SimpleAction
{
    protected static final Logger logger = LogManager.getLogger(AuthenticatorAction.class);

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String authenticate() {
        EntityManager em = getEntityManager();
        TypedQuery<User> query = em.createQuery("select u from User u"
                + " where u.username = :username and u.password = :password", User.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        List<User> users = query.getResultList();
        if (users.isEmpty()) {
            if (BookingApplication.LOG_ENABLED)
            {
                logger.error("Login failed");
            }
            facesContext.addMessage(null, new FacesMessage("Login failed"));
            return null;
        }
        User user = users.get(0);
        session.setUser(user);
        if (BookingApplication.LOG_ENABLED)
        {
            logger.info("Login succeeded");
        }
        facesContext.addMessage(null, new FacesMessage("Login succeeded"));
        session.info("Welcome, " + user.getUsername());
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "main?faces-redirect=true";
    }
}
