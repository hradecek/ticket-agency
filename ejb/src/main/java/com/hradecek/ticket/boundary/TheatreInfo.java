package com.hradecek.ticket.boundary;

import com.hradecek.ticket.control.Seat;
import com.hradecek.ticket.control.TheatreBox;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.Collection;

@Stateless
@Remote(TheatreInfoRemote.class)
public class TheatreInfo implements TheatreInfoRemote {

    @EJB
    private TheatreBox box;

    @Override
    public String printSeatList() {
        final Collection<Seat> seats = box.getSeats();
        final StringBuilder sb = new StringBuilder();
        seats.forEach(s ->
                sb.append(s.toString())
                  .append(System.lineSeparator()));

        return sb.toString();
    }
}
