package jsfbench.beans;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

import jsfbench.entity.User;

@Named("bookingSession")
@jakarta.enterprise.context.SessionScoped
public class BookingSession implements Serializable
{
    @Inject
    private FacesContext facesContext;

    private User user;
    
    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public void info(String message)
    {
        facesContext.addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        message, message ));
    }
    
    public String logout()
    {
        facesContext.getExternalContext().invalidateSession();
        return "home?faces-redirect=true";
    }
}
