package jsfbench.beans;

import jakarta.inject.Named;

import java.io.Serializable;

import jsfbench.entity.Booking;

/**
 *
 * @author lu4242
 */
@Named("bookingBean")
@jakarta.enterprise.context.SessionScoped
public class BookingBean implements Serializable {
    
    private Booking booking;

    public BookingBean()
    {
    }
    
    /**
     * @return the booking
     */
    public Booking getBooking() {
        return booking;
    }

    /**
     * @param booking the booking to set
     */
    public void setBooking(Booking booking) {
        this.booking = booking;
    }
    
}
