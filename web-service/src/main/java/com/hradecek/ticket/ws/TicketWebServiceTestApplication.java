package com.hradecek.ticket.ws;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

public class TicketWebServiceTestApplication {

    private static Logger logger = Logger.getLogger(TicketWebServiceTestApplication.class.getName());

    public static void main(String... args) throws MalformedURLException {
        final int seatId = 1;
        logger.info("TEST SOAP WS Service");
        final URL wsdlURL = new URL("http://localhost:8080/ticket-agency-ws/TicketWebService?wsdl");
        final QName SERVICE_NAME = new QName("http://www.hradecek.com/TicketWebService");
        final Service service = Service.create(wsdlURL, SERVICE_NAME);
        final TicketWebService infoService = service.getPort(TicketWebService.class);
        logger.info("Got the service: " + infoService);
        infoService.bookSeat(seatId);
        logger.info("Ticket booked with JAX-WS Service");
        final List<SeatDto> list = infoService.getSeats();
        dumpSeatList(list);
    }

    private static void dumpSeatList(Collection<SeatDto> list) {
        logger.info("======= Available Ticket List ========");
        list.stream().forEach(seat -> logger.info(seat.toString()));
    }
}
