package jsfbench.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * CDI Producer for request-scoped EntityManager instances.
 */
@ApplicationScoped
public class EntityManagerProducer {

    @Inject
    private BookingApplication bookingApplication;

    @Produces
    @RequestScoped
    public EntityManager createEntityManager() {
        return bookingApplication.getEntityManagerFactory().createEntityManager();
    }

    public void closeEntityManager(@Disposes EntityManager em) {
        if (em != null && em.isOpen()) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }
}
