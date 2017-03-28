package com.hradecek.ticket.boundary;

import com.hradecek.ticket.exception.NoEnoughMoneyException;
import com.hradecek.ticket.exception.NoSuchSeatException;
import com.hradecek.ticket.exception.SeatBookedException;

public interface TheatreBookerRemote {

    int getAccountBalance();

    String bookSeat(int seatId) throws NoEnoughMoneyException, SeatBookedException, NoSuchSeatException;
}
