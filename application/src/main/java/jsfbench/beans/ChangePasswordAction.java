package jsfbench.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Named;

import jsfbench.entity.User;

@Named("changePassword")
@RequestScoped
public class ChangePasswordAction extends SimpleAction {

    private User user;

    @PostConstruct
    public void init()
    {
        user = session.getUser();
        if (user == null)
        {
            user = new User();
        }
    }
    
    public User getUser()
    {
        return user;
    }    
    
    private String verify;

    public String changePassword() {
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
                return null;
            }
            facesContext.addMessage(null, new FacesMessage("Password updated"));
            facesContext.getExternalContext().getFlash().setKeepMessages(true);
            return "main?faces-redirect=true";
        } else {
            facesContext.addMessage("setpassword:verify", new FacesMessage("Re-enter new password"));
            revertUser();
            verify = null;
            return null;
        }
    }

    private void revertUser() {
        user = getEntityManager().find(User.class, user.getUsername());
        session.setUser(user);
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }
}
