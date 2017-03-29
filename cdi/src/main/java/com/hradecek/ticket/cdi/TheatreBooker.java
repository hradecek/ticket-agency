package com.hradecek.ticket.cdi;

import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;

// @Named @SessionScoped
@Model
public class TheatreBooker implements Serializable {

    @Inject
    private Logger logger;

    @Inject
    private TheatreBox box;

    @Inject
    FacesContext facesContext;

    private int money;

    @PostConstruct
    public void createCustomer() {
        this.money = 100;
    }

    public void bookSeat(int seatId) {
        logger.info("Booking seat " + seatId);
        final int seatPrice = box.getSeatPrice(seatId);

        if (seatPrice > money) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Not enought money!", "Registration unsuccessful");
            facesContext.addMessage(null, msg);
            return;
        }

        box.buyTicket(seatId);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Booked!", "Booking successful");
        facesContext.addMessage(null, msg);
        logger.info("Seat booked.");

        money -= seatPrice;
    }

    public int getMoney() {
        return money;
    }
}
