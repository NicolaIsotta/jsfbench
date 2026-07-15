package jsf2jpa.beans;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.TypedQuery;

import java.io.Serializable;

import jsf2jpa.entity.Hotel;


@Named("hotelSearch")
@jakarta.enterprise.context.RequestScoped
public class HotelSearchingAction extends SimpleAction implements Serializable
{

    @Inject
    private HotelBean hotelBean;

    public void find(AjaxBehaviorEvent event)
    {
        getHotelBean().setPage(0);
        queryHotels();
    }
    
    public void find(ActionEvent event)
    {
        getHotelBean().setPage(0);
        queryHotels();
    }

    public void find()
    {
        getHotelBean().setPage(0);
        queryHotels();
    }
    
    public void nextPage() {
        getHotelBean().setPage(getHotelBean().getPage()+1);
        queryHotels();
    }
    
    public boolean isNextPageAvailable()
    {
        return getHotelBean().isNextPageAvailable();
    }

    private void queryHotels()
    {
        String pattern = getSearchString() == null ? "%" : '%' + getSearchString().toLowerCase().replace('*', '%') + '%';
        TypedQuery<Hotel> query = getEntityManager().createQuery("select h from Hotel h"
                + " where lower(h.name) like :pattern"
                + " or lower(h.city) like :pattern"
                + " or lower(h.zip) like :pattern"
                + " or lower(h.address) like :pattern",
                Hotel.class);
        query.setParameter("pattern", pattern);
        query.setMaxResults(getHotelBean().getPageSize());
        query.setFirstResult(getHotelBean().getPage() * getHotelBean().getPageSize());
        getHotelBean().setHotels(query.getResultList());
    }

    public String getSearchString() {
        return getHotelBean().getSearchString();
    }

    public void setSearchString(String searchString) {
        this.getHotelBean().getSearchString();
    }

    /**
     * @return the hotelBean
     */
    public HotelBean getHotelBean() {
        return hotelBean;
    }

    /**
     * @param hotelBean the hotelBean to set
     */
    public void setHotelBean(HotelBean hotelBean) {
        this.hotelBean = hotelBean;
    }
}
