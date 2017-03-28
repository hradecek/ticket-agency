package com.hradecek.ticket.control;

import com.hradecek.ticket.exception.NoSuchSeatException;
import com.hradecek.ticket.exception.SeatBookedException;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.AccessTimeout;
import javax.ejb.Lock;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static javax.ejb.LockType.READ;
import static javax.ejb.LockType.WRITE;

@Singleton
@Startup
@AccessTimeout(value = 5, unit = TimeUnit.MINUTES)
public class TheatreBox {

    private static final Logger logger = Logger.getLogger(TheatreBox.class);

    private Map<Integer, Seat> seats;

    @PostConstruct
    public void setUpTheatre() {
        seats = new HashMap<>();
        for (int i = 0, id = 0; i < 5; ++i, ++id) {
            addSeat(new Seat(id, "Stalls", 40));
            addSeat(new Seat(id, "Circle", 20));
            addSeat(new Seat(id, "Balcony", 10));
        }
        logger.info("Seat Map constructed");
    }

    private void addSeat(Seat seat) {
        seats.put(seat.getId(), seat);
    }

    @Lock(READ)
    public Collection<Seat> getSeats() {
        return Collections.unmodifiableCollection(seats.values());
    }

    @Lock(READ)
    public int getSeatPrice(int seatId) throws NoSuchSeatException {
        return getSeat(seatId).getPrice();
    }

    @Lock(WRITE)
    public void buyTicket(int seatId) throws SeatBookedException, NoSuchSeatException {
        final Seat seat = getSeat(seatId);
        if (seat.isBooked()) {
            throw new SeatBookedException("Seat " + seatId + " was already booked!");
        }

        addSeat(seat.getBookedSeat());
    }

    @Lock(READ)
    public Seat getSeat(int seatId) throws NoSuchSeatException {
        final Seat seat = seats.get(seatId);
        if (null == seat) {
            throw new NoSuchSeatException("Seat " + seatId + " does not exist!");
        }

        return seat;
    }
}
