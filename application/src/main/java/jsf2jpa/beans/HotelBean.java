package jsf2jpa.beans;

import jsf2jpa.entity.Hotel;

import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author lu4242
 */
@Named("hotelBean")
@jakarta.enterprise.context.SessionScoped
public class HotelBean implements Serializable
{
    private String searchString;
    
    private List<Hotel> hotels;
    
    private int pageSize = 10;
    private int page;

    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    /**
     * @return the hotels
     */
    public List<Hotel> getHotels()
    {
        return hotels;
    }

    /**
     * @param hotels the hotels to set
     */
    public void setHotels(List<Hotel> hotels)
    {
        this.hotels = hotels;
    }

    /**
     * @return the page
     */
    public int getPage()
    {
        return page;
    }

    /**
     * @param page the page to set
     */
    public void setPage(int page) 
    {
        this.page = page;
    }

    public boolean isNextPageAvailable()
    {
        return hotels != null && hotels.size() == pageSize;
    }

    public boolean isPageEmpty()
    {
        return !(hotels != null && !hotels.isEmpty());
    }
    
    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

}
