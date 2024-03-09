package jsf2jpa.beans;

import jsf2jpa.entity.Booking;
import jsf2jpa.entity.Hotel;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;

import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Named("hotelBooking")
@jakarta.enterprise.context.RequestScoped
public class HotelBookingAction extends SimpleAction {

    protected static final Logger log = LogManager.getLogger(HotelBookingAction.class);        
    
    @Inject
    private BookingSession bookingSession;
    
    @Inject
    private BookingBean bookingBean;
    
    @Inject
    private BookingListAction bookingListAction;
    
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
            return "main";
        }
        return null;
    }
    
    public String bookHotel()
    {
        if (hotelId != null)
        {
            try
            {
                hotel = getEntityManager().find(Hotel.class, hotelId);
                Booking booking = new Booking(getHotel(), getBookingSession().getUser());
                Calendar calendar = Calendar.getInstance();
                booking.setCheckinDate(calendar.getTime());
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                booking.setCheckoutDate(calendar.getTime());
                getBookingBean().setBooking(booking);
                return "book";
            }
            catch (NumberFormatException e)
            {
                //Skip
            }
        }
        return null;
    }

    public String setBookingDetails() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        if (getBookingBean().getBooking().getCheckinDate().before(calendar.getTime())) {
            facesContext.addMessage("checkinDate", new FacesMessage("Check in date must be a future date"));
            bookingValid = false;
        } else if (!bookingBean.getBooking().getCheckinDate().before(bookingBean.getBooking().getCheckoutDate())) {
            facesContext.addMessage("checkoutDate", new FacesMessage("Check out date must be later than check in date"));
            bookingValid = false;
        } else {
            bookingValid = true;
        }
        if (bookingValid)
        {
            return "confirm";
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
        FacesContext facesContext = FacesContext.getCurrentInstance();
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
        return "main";
    }

    public String cancel()
    {
        getBookingBean().setBooking(null);
        return "main";
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
     * @return the bookingSession
     */
    public BookingSession getBookingSession() {
        return bookingSession;
    }

    /**
     * @param bookingSession the bookingSession to set
     */
    public void setBookingSession(BookingSession bookingSession) {
        this.bookingSession = bookingSession;
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
