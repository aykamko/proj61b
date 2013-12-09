package trip;

import java.util.LinkedList;
import java.util.List;

import java.util.Iterator;

import java.util.NoSuchElementException;

class Trip {

    Trip(String start, String end) {
        _start = start;
        _end = end;
        _path = new LinkedList<Road>();
        _directions = new LinkedList<Character>();
    }

    /** Returns the start of this road. */
    public String start() {
        return _start;
    }

    /** Returns the end of this road. */
    public String end() {
        return _end;
    }

    public void addRoad(Road road, Character direction) {
        Road prevRoad = null;
        Character prevDirection = null;
        try {
            prevRoad = _path.getLast();
            prevDirection = _directions.getLast();
        } catch (NoSuchElementException e) {
            /** Ignore NoSuchElementException. */
        }

        if (prevRoad != null && prevDirection != null) {
            String prevName = prevRoad.name();
            String curName = road.name();
            if (prevName.equals(curName) && prevDirection == direction) {
                _path.removeLast();
                Double newLength = prevRoad.length() + road.length();
                Road collapsed = 
                    new Road(null, curName, newLength, null, null);
                _path.addLast(collapsed);
                return;
            }
        }

        _path.addLast(road);
        _directions.addLast(direction);
    }

    public Iterator<String> routeIterator() {
        return new DirectionGiver();
    }

    private class DirectionGiver implements Iterator<String> {
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
                    next.name(), getDirection(next), next.length(), addendum);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("cannot remove a path");
        }

        private Iterator<Road> _roadIter;
    }

    private String getDirection(Road road) {
        char direction = road.direction().charAt(1);
        String result = null;
        switch (direction) {
            case 'N': {
                result = "north";
                break;
            } case 'E': {
                result = "east";
                break;
            } case 'S': {
                result = "south";
                break;
            } case 'W': {
                result = "west";
                break;
            }
        }
        return result;
    }

    private final String _start;
    private final String _end;
    private final LinkedList<Road> _path;
    private final LinkedList<Character> _directions;
}
