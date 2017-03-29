package com.hradecek.ticket.boundary;

import com.hradecek.ticket.exception.NoEnoughMoneyException;
import com.hradecek.ticket.exception.NoSuchSeatException;
import com.hradecek.ticket.exception.SeatBookedException;

import java.util.concurrent.Future;

public interface TheatreBookerRemote {

    int getAccountBalance();

    String bookSeat(int seatId) throws NoEnoughMoneyException, SeatBookedException, NoSuchSeatException;

    Future<String> bookSeatAsync(int seatId) throws NoEnoughMoneyException;
}
