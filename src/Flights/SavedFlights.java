package Flights;

import java.util.List;

public class SavedFlights {
    public String flightId;
    public String departureAirport;
    public String arrivalAirport;
    public String departureTime;
    public String arrivalTime;

    public SavedFlights() {
    } // Needed by Firebase

    public SavedFlights(String flightId, String depAir, String arrAir, String depTime, String arrTime) {
        this.flightId = flightId;
        this.departureAirport = depAir;
        this.arrivalAirport = arrAir;
        this.departureTime = depTime;
        this.arrivalTime = arrTime;
    }

    public String print() {
        return "this " + (this.flightId ) + (this.arrivalAirport ) +
                (this.departureAirport ) + (this.departureTime ) + (this.arrivalTime );
    }
}
