package trip;

import java.util.LinkedList;
import java.util.List;

import java.util.NoSuchElementException;

class Trip {

    Trip(String end) {
        _end = end;
        _path = new LinkedList<Road>();
        _directions = new LinkedList<Character>();
        _length = 0;
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
        _length += 1;
    }

    private final String _end;
    private final LinkedList<Road> _path;
    private final LinkedList<Character> _directions;
    private int _length;
}
