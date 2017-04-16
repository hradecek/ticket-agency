package com.hradecek.ticket.ws;

import javax.jws.WebService;

@WebService
public interface TicketWebService {

    List<SeatDto> getSeats();

    void bookSeat(int seatId);
}
