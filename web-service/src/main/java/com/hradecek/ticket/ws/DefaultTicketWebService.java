package com.hradecek.ticket.ws;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import java.io.Serializable;
import java.util.stream.Collectors;

@WebService(targetNamespace = "http://www.hradecek.com/", serviceName = "TicketWebService")
public class DefaultTicketWebService implements TicketWebService, Serializable {

    @Inject
    private TheatreBox theatreBox;

    @WebMethod
    @WebResult(name = "listSeats")
    public List<SeatDto> getSeats() {
        return theatreBox.getSeats()
                         .stream()
                         .map(SeatDto::fromSeat)
                         .collect(Collectors::toList);
    }

    @WebMethod
    public void bookSeat(@WebParam(name = "seatId") int seatId) {
        theatreBox.buyTicket(seatId);
    }
}
