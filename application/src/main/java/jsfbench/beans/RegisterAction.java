package jsfbench.beans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;

import jsfbench.entity.User;

@Named("register")
@jakarta.enterprise.context.RequestScoped
public class RegisterAction extends SimpleAction
{
    private User user;

    @PostConstruct
    public void init()
    {
        user = new User();
    }
    
    public User getUser()
    {
        return user;
    }

    private String verify;

    public String register()
    {
        if (getUser().getPassword().equals(verify))
        {
            EntityManager em = getEntityManager();
            Query query = em.createQuery("select u.username from User u where u.username = :username");
            query.setParameter("username", user.getUsername());
            List existing = query.getResultList();
            if(existing.isEmpty())
            {
                try
                {
                    em.getTransaction().begin();
                    em.persist(getUser());
                    em.getTransaction().commit();
                }
                catch (Exception e)
                {
                    facesContext.addMessage(null, new FacesMessage("Error when register user on database"));
                    if (em.getTransaction().isActive())
                    {
                        em.getTransaction().rollback();
                    }
                    return null;
                }
                facesContext.addMessage(null, new FacesMessage("Successfully registered as "+user.getUsername()));
                facesContext.getExternalContext().getFlash().setKeepMessages(true);
                return "home?faces-redirect=true";
            }
            else
            {
                facesContext.addMessage("register:username", new FacesMessage("Username "+user.getUsername()+" already exists"));
                return null;
            }
        }
        else
        {
            facesContext.addMessage("register:verify", new FacesMessage("Re-enter your password"));
            verify = null;
            return null;
        }
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getPattern()
    {
        return "^\\w*$";
    }
}
