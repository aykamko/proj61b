package trip;

class Road {

    Road(String start, String name, Integer length, String direction,
            String end) {
        _start = start;
        _end = end;
        _name = name;
        _length = length;
        _direction = direction;
    }

    /** Start of this road. */
    private final String _start;
    /** End of this road. */
    private final String _end;
    /** Name of this road. */
    private final String _name;
    /** Length of this road. */
    private final Integer _length;
    /** Direction of this road. */
    private final String _direction;


}
