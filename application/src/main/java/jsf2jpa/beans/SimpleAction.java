package jsf2jpa.beans;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 *
 * @author lu4242
 */
public class SimpleAction
{
    @Inject
    private JpaRequestCycle jpaRequestCycle;

    @Inject
    protected BookingSession session;
    
    EntityManager getEntityManager()
    {
        return jpaRequestCycle.getEntityManager();
    }

    /**
     * @return the bookingApplication
     */
    public BookingApplication getBookingApplication() {
        return jpaRequestCycle.getBookingApplication();
    }

    /**
     * @param bookingApplication the bookingApplication to set
     */
    public void setBookingApplication(BookingApplication bookingApplication) {
        jpaRequestCycle.setBookingApplication(bookingApplication);
    }

    /**
     * @return the jpaRequestCycle
     */
    public JpaRequestCycle getJpaRequestCycle() {
        return jpaRequestCycle;
    }

    /**
     * @param jpaRequestCycle the jpaRequestCycle to set
     */
    public void setJpaRequestCycle(JpaRequestCycle jpaRequestCycle) {
        this.jpaRequestCycle = jpaRequestCycle;
    }
}
