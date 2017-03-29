package com.hradecek.ticket.client;

import com.hradecek.ticket.boundary.TheatreBookerRemote;
import com.hradecek.ticket.boundary.TheatreInfoRemote;
import com.hradecek.ticket.exception.NoEnoughMoneyException;
import com.hradecek.ticket.exception.NoSuchSeatException;
import com.hradecek.ticket.exception.SeatBookedException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketAgencyClient {

    private static final Logger logger = Logger.getLogger(TicketAgencyClient.class.getName());

    public static void main(String[] args) throws Exception {
        new TicketAgencyClient().run();
    }

    private final Context context;
    private final List<Future<String>> lastBookings = new ArrayList<>();
    private TheatreInfoRemote theatreInfo;
    private TheatreBookerRemote theatreBooker;

    public TicketAgencyClient() throws NamingException {
        Logger.getLogger("org.jboss").setLevel(Level.SEVERE);
        Logger.getLogger("org.jboss.xnio").setLevel(Level.SEVERE);
        final Properties jndiProperties = new Properties();
        jndiProperties.setProperty(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        this.context = new InitialContext(jndiProperties);
    }

    private enum Command {
        BOOKASYNC, BOOK, LIST, MAIL, MONEY, QUIT, INVALID;

        public static Command parseCommand(String stringCommand) {
            try {
                return valueOf(stringCommand.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                return INVALID;
            }
        }
    }

    private void run() throws NamingException, IOException {
        this.theatreInfo = lookupTheatreInfoEjb();
        this.theatreBooker = lookupTheatreBookerEjb();

        showWelcomeMessage();

        for (;;) {
            Scanner sc = new Scanner(System.in);
            final String stringCommand = sc.nextLine().toLowerCase();
            final Command command = Command.parseCommand(stringCommand);

            switch (command) {
                case BOOKASYNC:
                    handleBookAsync();
                    break;
                case BOOK:
                    handleBook();
                    break;
                case LIST:
                    handleList();
                    break;
                case MAIL:
                    handleMail();
                    break;
                case MONEY:
                    handleMoney();
                    break;
                case QUIT:
                    handleQuit();
                    break;
                default:
                    logger.warning("Unknown command: " + stringCommand);
            }
        }
    }

    private void handleBookAsync() {
        Scanner sc = new Scanner(System.in);
        int seatId = sc.nextInt();

        lastBookings.add(theatreBooker.bookSeatAsync(seatId));
        logger.info("Booking issued. Verify your mail!");
    }

    private void handleBook() {
        int seatId;

        Scanner sc = new Scanner(System.in);
        seatId = sc.nextInt();

        try {
            final String retVal = theatreBooker.bookSeat(seatId);
            System.out.println(retVal);
        } catch (SeatBookedException | NoSuchSeatException | NoEnoughMoneyException e) {
            logger.warning(e.getMessage());
            return;
        }
    }

    private void handleList() {
        logger.info(theatreInfo.printSeatList());
    }

    private void handleMail() {
        boolean isDisplayed = false;
        final List<Future<String>> notFinished = new ArrayList<>();

        for (Future<String> booking : lastBookings) {
            if (booking.isDone()) {
                try {
                    final String result = booking.get();
                    logger.info("Mail received: " + result);
                    isDisplayed = true;
                } catch (InterruptedException | ExecutionException e) {
                    logger.warning(e.getMessage());
                }
            } else {
                notFinished.add(booking);
            }
        }

        lastBookings.retainAll(notFinished);
        if (!isDisplayed) {
            logger.info("No mail received!");
        }
    }

    private void handleMoney() {
        final int accountBalance = theatreBooker.getAccountBalance();
        logger.info("You have: " + accountBalance + " money left.");
    }

    private void handleQuit() {
        logger.info("Bye");
        System.exit(0);
    }

    private TheatreInfoRemote lookupTheatreInfoEjb() throws NamingException {
        return (TheatreInfoRemote) context.lookup(
                "ejb:/ejb//TheatreInfo!com.hradecek.ticket.boundary.TheatreInfoRemote");
    }

    private TheatreBookerRemote lookupTheatreBookerEjb() throws NamingException {
        return (TheatreBookerRemote) context.lookup(
                "ejb:/ejb//TheatreBooker!com.hradecek.ticket.boundary.TheatreBookerRemote?stateful");
    }

    private void showWelcomeMessage() {
        System.out.println("Theatre booking system");
        System.out.println("======================");
        System.out.println("Commands: book, list, money, quit");
    }
}
