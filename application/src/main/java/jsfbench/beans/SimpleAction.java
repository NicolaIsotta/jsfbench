package jsfbench.beans;

import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.io.Serializable;

/**
 * Base action bean providing access to the current request-scoped EntityManager
 * and session state.
 *
 * @author lu4242
 */
public abstract class SimpleAction implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager entityManager;

    @Inject
    private BookingApplication bookingApplication;

    @Inject
    protected BookingSession session;

    @Inject
    protected FacesContext facesContext;
    
    EntityManager getEntityManager()
    {
        return entityManager;
    }

    /**
     * @return the bookingApplication
     */
    public BookingApplication getBookingApplication() {
        return bookingApplication;
    }

    /**
     * @param bookingApplication the bookingApplication to set
     */
    public void setBookingApplication(BookingApplication bookingApplication) {
        this.bookingApplication = bookingApplication;
    }
}
