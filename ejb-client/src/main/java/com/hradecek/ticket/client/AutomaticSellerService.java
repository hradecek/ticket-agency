package com.hradecek.ticket.client;

import com.hradecek.ticket.control.Seat;
import com.hradecek.ticket.control.TheatreBox;
import com.hradecek.ticket.exception.NoSuchSeatException;
import com.hradecek.ticket.exception.SeatBookedException;
import org.jboss.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.*;
import java.util.Collection;
import java.util.Optional;

@Stateless
public class AutomaticSellerService {

    private static final Logger logger = Logger.getLogger(AutomaticSellerService.class);

    @EJB
    private TheatreBox theatreBox;

    @Resource
    private TimerService timerService;

    @Schedule(hour = "*", minute = "*/1", persistent = false)
    public void automaticCustomer() throws NoSuchSeatException {
        final Optional<Seat> seatOptional = findFreeSeat();
        if (!seatOptional.isPresent()) {
            cancelTimers();
            logger.info("Scheduler gone!");
            return;
        }

        final Seat seat = seatOptional.get();
        try {
            theatreBox.buyTicket(seat.getId());
        } catch (SeatBookedException ex) {

        }
        logger.info("Somebody has just booked seat number " + seat.getId());
    }

    private Optional<Seat> findFreeSeat() {
        final Collection<Seat> list = theatreBox.getSeats();
        return list.stream()
                   .filter(seat -> !seat.isBooked())
                   .findFirst();
    }

    private void cancelTimers() {
        for (Timer timer : timerService.getTimers()) {
            timer.cancel();
        }
    }
}
