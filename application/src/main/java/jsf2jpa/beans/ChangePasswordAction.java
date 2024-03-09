package jsf2jpa.beans;

import jsf2jpa.entity.User;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("changePassword")
@RequestScoped
public class ChangePasswordAction extends SimpleAction {

    private User user;

    @Inject
    private BookingSession bookingSession;
    
    @PostConstruct
    public void init()
    {
        user = bookingSession.getUser();
    }
    
    public User getUser()
    {
        if (user == null)
        {
            user = new User();
        }
        return user;
    }    
    
    private String verify;

    public String changePassword() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (user.getPassword().equals(verify))
        {
            try
            {
                getEntityManager().getTransaction().begin();
                user = getEntityManager().merge(user);
                getEntityManager().getTransaction().commit();
            }
            catch(Exception e)
            {
                facesContext.addMessage(null, new FacesMessage("Error when register user on database"));
                if (getEntityManager().getTransaction().isActive())
                {
                    getEntityManager().getTransaction().rollback();
                }
            }
            facesContext.addMessage(null, new FacesMessage("Password updated"));
            return "main";
        } else {
            facesContext.addMessage("register:verify", new FacesMessage("Re-enter new password"));
            revertUser();
            verify = null;
            return null;
        }
    }

    private void revertUser() {
        user = getEntityManager().find(User.class, user.getUsername());
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }
}
