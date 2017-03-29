package com.hradecek.ticket.cdi;

import javax.ejb.*;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static javax.ejb.LockType.*;
import static javax.ejb.LockType.READ;

@Singleton
@Startup
@AccessTimeout(value = 5, unit = TimeUnit.MINUTES)
public class TheatreBox {

    @Inject
    private Event<Seat> seatEvent;

    private Map<Integer, Seat> seats;

    @Lock(WRITE)
    public void buyTicket(int seatId) {
        final Seat seat = getSeat(seatId);
        final Seat bookedSeat = seat.getBookedSeat();
        addSeat(bookedSeat);

        seatEvent.fire(bookedSeat);
    }
    private void addSeat(Seat seat) {
        seats.put(seat.getId(), seat);
    }

    @Lock(READ)
    public Collection<Seat> getSeats() {
        return Collections.unmodifiableCollection(seats.values());
    }

    @Lock(READ)
    public int getSeatPrice(int seatId) {
        return getSeat(seatId).getPrice();
    }

    @Lock(READ)
    public Seat getSeat(int seatId) {
        return seats.get(seatId);
    }
}
