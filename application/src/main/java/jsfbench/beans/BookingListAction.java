package jsfbench.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.io.Serializable;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jsfbench.entity.Booking;

@Named("bookingList")
@SessionScoped
public class BookingListAction extends SimpleAction implements Serializable {

    protected static final Logger log = LogManager.getLogger(HotelBookingAction.class);

    private List<Booking> bookings;

    @PostConstruct
    public void init()
    {
        loadBookings(getEntityManager());
    }

    public void loadBookings(EntityManager em)
    {
        TypedQuery<Booking> query = em.createQuery("select b from Booking b"
                + " where b.user.username = :username order by b.checkinDate", Booking.class);
        query.setParameter("username", session.getUser().getUsername());
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
            facesContext.addMessage(null, new FacesMessage("Booking cancelled for confirmation number " + cancelled.getId()));
        }
        loadBookings(em);
    }

    /**
     * @return the bookings
     */
    public List<Booking> getBookings() {
        return bookings;
    }
    
    public boolean isPageEmpty()
    {
        return !(bookings != null && !bookings.isEmpty());
    }

}
