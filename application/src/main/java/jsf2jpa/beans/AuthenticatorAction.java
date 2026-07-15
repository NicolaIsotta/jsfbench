package jsf2jpa.beans;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jsf2jpa.entity.User;

@Named("authenticator")
@jakarta.enterprise.context.RequestScoped
public class AuthenticatorAction extends SimpleAction
{
    protected static final Logger logger = LogManager.getLogger(AuthenticatorAction.class);
    
    private User user;
    
    public User getUser()
    {
        if (user == null)
        {
            user = new User();
        }
        return user;
    }

    public String authenticate() {
        EntityManager em = getEntityManager();
        TypedQuery<User> query = em.createQuery("select u from User u"
                + " where u.username = :username and u.password = :password", User.class);
        query.setParameter("username", user.getUsername());
        query.setParameter("password", user.getPassword());
        List<User> users = query.getResultList();
        FacesContext facesContext = FacesContext.getCurrentInstance();
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
        return "main?faces-redirect=true";
    }
}
