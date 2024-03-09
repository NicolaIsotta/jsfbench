package jsf2jpa.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;

import org.hibernate.validator.constraints.CreditCardNumber;

@Entity
public class Booking implements Serializable {

    private Long id;
    private User user;
    private Hotel hotel;
    private Date checkinDate;
    private Date checkoutDate;
    private String creditCard;
    private String creditCardName;
    private int creditCardExpiryMonth;
    private int creditCardExpiryYear;
    private boolean smoking;
    private int beds;

    public Booking() { }

    public Booking(Hotel hotel, User user) {
        this.hotel = hotel;
        this.user = user;
    }

    @Transient
    public BigDecimal getTotal() {
        return hotel.getPrice().multiply(new BigDecimal(getNights()));
    }

    @Transient
    public int getNights() {
        return (int) (checkoutDate.getTime() - checkinDate.getTime()) / 1000 / 60 / 60 / 24;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    @Basic
    @Temporal(TemporalType.DATE)
    public Date getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(Date datetime) {
        this.checkinDate = datetime;
    }

    @ManyToOne
    @NotNull
    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    @ManyToOne
    @NotNull
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @NotNull
    public Date getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    @NotNull(message = "Credit card number is required")
    @CreditCardNumber
    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    @Transient
    public String getDescription() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        return hotel == null ? null : hotel.getName() +
                ", " + df.format(getCheckinDate()) +
                " to " + df.format(getCheckoutDate());
    }

    public boolean isSmoking() {
        return smoking;
    }

    public void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    @NotNull(message = "Credit card name is required")
    @Size(min = 3, max = 70, message = "Credit card name is required")
    public String getCreditCardName() {
        return creditCardName;
    }

    public void setCreditCardName(String creditCardName) {
        this.creditCardName = creditCardName;
    }

    public int getCreditCardExpiryMonth() {
        return creditCardExpiryMonth;
    }

    public void setCreditCardExpiryMonth(int creditCardExpiryMonth) {
        this.creditCardExpiryMonth = creditCardExpiryMonth;
    }

    public int getCreditCardExpiryYear() {
        return creditCardExpiryYear;
    }

    public void setCreditCardExpiryYear(int creditCardExpiryYear) {
        this.creditCardExpiryYear = creditCardExpiryYear;
    }

    @Override
    public String toString() {
        return "Booking(" + user + "," + hotel + ")";
    }
}
