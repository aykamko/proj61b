package trip;

import java.util.LinkedList;

import java.util.Iterator;

import java.util.NoSuchElementException;

/** Represents a trip from one location to another. Takes care of collapsing
 *  roads of the same name and direction.
 *  @author Aleks Kamko
 */
class Trip {

    /** Constructs a trip with a starting location START and an ending
     *  location END. */
    Trip(String start, String end) {
        _start = start;
        _end = end;
        _path = new LinkedList<Road>();
    }

    /** Returns the start of this road. */
    public String start() {
        return _start;
    }

    /** Returns the end of this road. */
    public String end() {
        return _end;
    }

    /** Adds the road ROAD to this trip. If ROAD has the same name and
     *  direction as the previously added road, then both are collapsed into
     *  one road. */
    public void addRoad(Road road) {
        Road prevRoad = null;
        try {
            prevRoad = _path.getLast();
        } catch (NoSuchElementException e) {
            /* Ignore NoSuchElementException. */
        }

        if (prevRoad != null) {
            String prevName = prevRoad.name();
            Direction prevDirection = prevRoad.direction();
            String curName = road.name();
            Direction curDirection = road.direction();
            if (prevName.equals(curName) && prevDirection == curDirection) {
                _path.removeLast();
                Double newLength = prevRoad.length() + road.length();
                Road collapsed =
                    new Road(null, curName, newLength, curDirection, null);
                _path.addLast(collapsed);
                return;
            }
        }

        _path.addLast(road);
    }

    /** Returns an Iterator<String> over the route. Formats each individual
     *  direction according to the trip spec. */
    public Iterator<String> routeIterator() {
        return new DirectionGiver();
    }

    /** An Iterator<String> that returns formatted directions for this Trip,
     *  road for road. */
    private class DirectionGiver implements Iterator<String> {

        /** Default constructor for DirectionGiver. Constructs an iterator
         *  over this Trip's _path List. */
        DirectionGiver() {
            _roadIter = _path.iterator();
        }

        @Override
        public boolean hasNext() {
            return _roadIter.hasNext();
        }

        @Override
        public String next() {
            Road next = _roadIter.next();
            String path = "Take %s %s for %.1f miles%s.";
            String addendum = "";
            if (!_roadIter.hasNext()) {
                addendum = String.format(" to %s", _end);
            }
            return String.format(path,
                    next.name(), next.direction(), next.length(), addendum);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("cannot remove a path");
        }

        /** This DirectionGiver's Road iterator. */
        private Iterator<Road> _roadIter;
    }

    /** The start location of this Trip. */
    private final String _start;
    /** The ending location of this Trip. */
    private final String _end;
    /** Roads to take to get from _start to _end. */
    private final LinkedList<Road> _path;
}
