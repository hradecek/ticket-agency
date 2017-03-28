package com.hradecek.ticket.boundary;

public interface TheatreBookerRemote {

    int getAccountBalance();

    String bookSeat(int seatId);
}
