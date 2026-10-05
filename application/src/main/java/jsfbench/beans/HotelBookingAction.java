package jsfbench.beans;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.Flash;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;

import java.io.Serializable;
import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jsfbench.entity.Booking;
import jsfbench.entity.Hotel;

@Named("hotelBooking")
@ViewScoped
public class HotelBookingAction extends SimpleAction implements Serializable {

    private static final long serialVersionUID = 1L;

    protected static final Logger log = LogManager.getLogger(HotelBookingAction.class);        
    
    @Inject
    private BookingBean bookingBean;
    
    @Inject
    private BookingListAction bookingListAction;

    @Inject
    private Flash flash;

    private Hotel hotel;

    private Long hotelId;

    private boolean bookingValid;

    public String selectHotel()
    {
        if (getHotelId() != null)
        {
            hotel = getEntityManager().find(Hotel.class, getHotelId());
        }
        if (getHotel() == null)
        {
            return "main?faces-redirect=true";
        }
        return null;
    }
    
    public String bookHotel()
    {
        if (hotelId != null)
        {
            hotel = getEntityManager().find(Hotel.class, hotelId);
            Booking booking = new Booking(getHotel(), session.getUser());
            booking.setCheckinDate(LocalDate.now());
            booking.setCheckoutDate(LocalDate.now().plusDays(1));
            getBookingBean().setBooking(booking);
            return "book?faces-redirect=true";
        }
        return null;
    }

    public String setBookingDetails() {
        if (!bookingBean.getBooking().getCheckinDate().isBefore(bookingBean.getBooking().getCheckoutDate())) {
            facesContext.addMessage(null, new FacesMessage("Check out date must be later than check in date"));
            bookingValid = false;
        } else {
            bookingValid = true;
        }
        if (bookingValid)
        {
            return "confirm?faces-redirect=true";
        }
        else
        {
            return null;
        }
    }

    public boolean isBookingValid() {
        return bookingValid;
    }

    public String confirm()
    {
        Booking booking = getBookingBean().getBooking();
        EntityManager em = getEntityManager();
        try
        {
            em.getTransaction().begin();
            em.persist(booking);
            em.getTransaction().commit();
            facesContext.addMessage(null, new FacesMessage("Thank you, "+booking.getUser().getName()+
                    ", your confimation number for "+booking.getHotel().getName()+" is "+ booking.getId()));
            getBookingListAction().loadBookings(em);
        }
        catch (Exception e)
        {
            facesContext.addMessage(null, new FacesMessage("Error when register on database"));
            if (em.getTransaction().isActive())
            {
                em.getTransaction().rollback();
            }
        }
        if (BookingApplication.LOG_ENABLED)
        {
            log.info("New booking: "+booking.getId()+" for "+booking.getUser().getUsername());
        }
        flash.setKeepMessages(true);
        return "main?faces-redirect=true";
    }

    public String cancel()
    {
        getBookingBean().setBooking(null);
        return "main?faces-redirect=true";
    }

    /**
     * @return the hotelId
     */
    public Long getHotelId() {
        return hotelId;
    }

    /**
     * @param hotelId the hotelId to set
     */
    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    /**
     * @return the hotel
     */
    public Hotel getHotel() {
        return hotel;
    }

    /**
     * @return the booking
     */
    public Booking getBooking() {
        return getBookingBean().getBooking();
    }

    /**
     * @param booking the booking to set
     */
    public void setBooking(Booking booking) {
        getBookingBean().setBooking(booking);
    }

    /**
     * @return the bookingBean
     */
    public BookingBean getBookingBean() {
        return bookingBean;
    }

    /**
     * @param bookingBean the bookingBean to set
     */
    public void setBookingBean(BookingBean bookingBean) {
        this.bookingBean = bookingBean;
    }

    /**
     * @return the bookingListAction
     */
    public BookingListAction getBookingListAction() {
        return bookingListAction;
    }

    /**
     * @param bookingListAction the bookingListAction to set
     */
    public void setBookingListAction(BookingListAction bookingListAction) {
        this.bookingListAction = bookingListAction;
    }
}
