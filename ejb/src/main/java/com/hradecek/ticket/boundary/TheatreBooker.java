package com.hradecek.ticket.boundary;

import com.hradecek.ticket.control.TheatreBox;
import com.hradecek.ticket.exception.NoEnoughMoneyException;
import com.hradecek.ticket.exception.NoSuchSeatException;
import com.hradecek.ticket.exception.SeatBookedException;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.AccessTimeout;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import java.util.concurrent.TimeUnit;

@Stateful
@Remote(TheatreBookerRemote.class)
@AccessTimeout(value = 5, unit = TimeUnit.MINUTES)
public class TheatreBooker {

    private static final Logger logger = Logger.getLogger(TheatreBooker.class);

    @EJB
    private TheatreBox box;

    private int money;

    @PostConstruct
    public void createCustomer() {
        this.money = 100;
    }

    @Override
    public int getAccountBalance() {
        return money;
    }

    @Override
    public String bookSeat(int seatId) throws SeatBookedException, NoSuchSeatException, NoEnoughMoneyException {
        final int seatPrice = box.getSeatPrice(seatId);
        if (seatPrice > money) {
            throw new NoEnoughMoneyException("You don't have enough money to buy this " + seatId + " seat!");
        }

        box.buyTicket(seatId);
        money = money - seatPrice;
        logger.infov("Seat {0} booked", seatId);

        return "Seat booked.";
    }
}
