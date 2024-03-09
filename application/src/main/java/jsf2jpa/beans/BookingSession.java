package jsf2jpa.beans;

import jsf2jpa.entity.User;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.Serializable;

@Named("bookingSession")
@jakarta.enterprise.context.SessionScoped
public class BookingSession implements Serializable
{    
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
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        message, message ));
    }
    
    public String logout()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().invalidateSession();
        return "home";
    }
}
