package trip;

import java.util.List;
import java.util.Iterator;

import static java.lang.System.out;

class TripPrinter {

    TripPrinter(List<Trip> trips) {
        _trips = trips;
    }

    public void printTrip() {
        String startingLocation = _trips.get(0).start();
        out.printf("From %s:%n%n", startingLocation);

        int i = 1;
        for (Trip t : _trips) {
            Iterator<String> routeIter = t.routeIterator();
            while (routeIter.hasNext()) {
                out.printf("%d: %s%n", i, routeIter.next());
                i += 1;
            }
        }
    }

    private final List<Trip> _trips;
}
