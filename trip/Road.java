package trip;

import graph.Weighted;

/** Represents a Road in a graph of the map.
 *  @author Aleks Kamko
 */
class Road implements Weighted, Comparable<Road> {

    /** Default constructor for Road. Takes in a start location START,
     *  a name NAME, a length LENGTH, a direction DIRECTION, and an
     *  end location END. */
    Road(String start, String name, Double length, Direction direction,
            String end) {
        _start = start;
        _end = end;
        _name = name;
        _length = length;
        _direction = direction;
    }

    /** Returns the start of this road. */
    public String start() {
        return _start;
    }

    /** Returns the end of this road. */
    public String end() {
        return _end;
    }

    /** Returns the name of this road. */
    public String name() {
        return _name;
    }

    /** Returns the length of this road. */
    public Double length() {
        return _length;
    }

    /** Returns the direction of this road. */
    public Direction direction() {
        return _direction;
    }

    @Override
    public double weight() {
        return length();
    }

    @Override
    public int compareTo(Road other) {
        return _name.compareTo(other.name());
    }

    /** Start of this road. */
    private final String _start;
    /** End of this road. */
    private final String _end;
    /** Name of this road. */
    private final String _name;
    /** Length/weight of this road. */
    private final Double _length;
    /** Direction of this road. */
    private final Direction _direction;

}
