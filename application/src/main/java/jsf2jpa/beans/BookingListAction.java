package jsf2jpa.beans;


import jsf2jpa.entity.Booking;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.io.Serializable;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named("bookingList")
@SessionScoped
public class BookingListAction implements Serializable {

    protected static final Logger log = LogManager.getLogger(HotelBookingAction.class);        
    
    @Inject
    private BookingSession bookingSession;

    private List<Booking> bookings;

    public void loadBookings()
    {
        loadBookings(getEntityManager());
    }
    
    public void loadBookings(EntityManager em)
    {
        Query query = em.createQuery("select b from Booking b"
                + " where b.user.username = :username order by b.checkinDate");
        query.setParameter("username", bookingSession.getUser().getUsername());
        bookings = query.getResultList();
    }    

    public void cancel(Booking booking) {
        if (booking == null)
        {
            return;   
        }
        if (BookingApplication.LOG_ENABLED)
        {
            log.info("Cancel booking: "+booking.getId()+" for "+booking.getUser().getUsername());
        }
        EntityManager em = getEntityManager();
        Booking cancelled = em.find(Booking.class, booking.getId());
        if (cancelled != null) {
            em.getTransaction().begin();
            em.remove(cancelled);
            em.getTransaction().commit();
        }
        loadBookings(em);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage("Booking cancelled for confirmation number "+cancelled.getId()));
    }

    /**
     * @return the bookings
     */
    public List<Booking> getBookings() {
        if (bookings == null)
        {
            loadBookings();
        }
        return bookings;
    }

    /**
     * @return the hotelSearchAction
     */
    EntityManager getEntityManager()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().evaluateExpressionGet(
                facesContext, "#{jpaRequestCycle}", JpaRequestCycle.class).getEntityManager();
    }
    
    public boolean isPageEmpty()
    {
        return !(bookings != null && !bookings.isEmpty());
    }

}
