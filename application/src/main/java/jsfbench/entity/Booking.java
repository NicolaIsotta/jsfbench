package jsfbench.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;

import org.hibernate.validator.constraints.CreditCardNumber;

@Entity
public class Booking implements Serializable {

    private Long id;
    private User user;
    private Hotel hotel;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private String creditCard;
    private String creditCardName;
    private YearMonth creditCardExpiry;
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
        return (int) ChronoUnit.DAYS.between(checkinDate, checkoutDate);
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @FutureOrPresent
    @NotNull
    @Basic
    public LocalDate getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(LocalDate datetime) {
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
    @Future
    @NotNull
    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
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
        DateTimeFormatter df = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
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

    public YearMonth getCreditCardExpiry() {
        return creditCardExpiry;
    }

    public void setCreditCardExpiry(YearMonth creditCardExpiry) {
        this.creditCardExpiry = creditCardExpiry;
    }

    @Override
    public String toString() {
        return "Booking(" + user + "," + hotel + ")";
    }
}
