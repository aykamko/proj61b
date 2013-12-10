package trip;

import java.util.List;
import java.util.Iterator;

import static java.lang.System.out;

/** Class that prints all inputted Trips using the format specified in the
 *  trip spec.
 *  @author Aleks Kamko
 */
class TripPrinter {

    /** Default constructor for TripPrinter. Takes in a List<Trip> TRIPS to
     *  print. */
    TripPrinter(List<Trip> trips) {
        _trips = trips;
    }

    /** Prints all Trips using in specified format. */
    public void printTrip() {
        String startingLocation = _trips.get(0).start();
        out.printf("From %s:%n%n", startingLocation);

        int i = 1;
        for (Trip t : _trips) {
            Iterator<String> routeIter = t.routeIterator();
            while (routeIter.hasNext()) {
                out.printf("%d. %s%n", i, routeIter.next());
                i += 1;
            }
        }
    }

    /** Trips for this instance of TripPrinter. */
    private final List<Trip> _trips;
}
