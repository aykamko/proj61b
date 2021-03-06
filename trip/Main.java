package trip;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.FileNotFoundException;

import java.util.LinkedList;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.Scanner;

import java.io.IOException;

import java.io.File;

import static trip.Direction.*;
import static java.lang.System.out;

import graph.Graph;
import graph.DirectedGraph;

/** Initial class for the 'trip' program.
 *  @author Aleks Kamko
 */
public final class Main {

    /** Entry point for the CS61B trip program.  ARGS may contain options
     *  and targets:
     *      [ -m MAP ] [ -o OUT ] [ REQUEST ]
     *  where MAP (default Map) contains the map data, OUT (default standard
     *  output) takes the result, and REQUEST (default standard input) contains
     *  the locations along the requested trip.
     */
    public static void main(String... args) {
        String mapFileName;
        String outFileName;
        String requestFileName;

        mapFileName = "Map";
        outFileName = requestFileName = null;

        int a;
        for (a = 0; a < args.length; a += 1) {
            if (args[a].equals("-m")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    mapFileName = args[a];
                }
            } else if (args[a].equals("-o")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    outFileName = args[a];
                }
            } else if (args[a].startsWith("-")) {
                usage();
            } else {
                break;
            }
        }

        if (a == args.length - 1) {
            requestFileName = args[a];
        } else if (a > args.length) {
            usage();
        }

        if (requestFileName != null) {
            try {
                System.setIn(new FileInputStream(requestFileName));
            } catch  (FileNotFoundException e) {
                System.err.printf("Could not open %s.%n", requestFileName);
                System.exit(1);
            }
        }

        if (outFileName != null) {
            try {
                System.setOut(new PrintStream(new FileOutputStream(outFileName),
                                              true));
            } catch  (FileNotFoundException e) {
                System.err.printf("Could not open %s for writing.%n",
                                  outFileName);
                System.exit(1);
            }
        }

        trip(mapFileName);
    }

    /** Print a trip for the request on the standard input to the stsndard
     *  output, using the map data in MAPFILENAME.
     */
    private static void trip(String mapFileName) {
        try {
            readMapFile(new File(mapFileName));
        } catch (IOException | IllegalArgumentException e) {
            usage();
        }

        DirectedGraph<Location, Road> g =
            new DirectedGraph<Location, Road>();
        Map<String, Graph<Location, Road>.Vertex> vmap =
            new HashMap<String, Graph<Location, Road>.Vertex>();
        MapGraphBuilder.buildMapGraph(_roads, _locations, g, vmap);

        TripFinder tFinder = new TripFinder(g, vmap);
        TripPrinter tPrinter = new TripPrinter(tFinder.findTrips());
        tPrinter.printTrip();
    }

    /** Reads the map file MAPFILE and stores Locations and Distances. Throws
     *  an exception if the file does not exist or if there is some format
     *  error. */
    private static void readMapFile(File mapFile)
        throws IOException, IllegalArgumentException {
        _scn = new Scanner(mapFile);
        _locations = new LinkedList<Location>();
        _roads = new LinkedList<Road>();

        Matcher locMatcher, roadMatcher;
        String line;
        while (_scn.hasNextLine()) {
            line = _scn.nextLine();

            locMatcher = LOCATION_REGEX.matcher(line);
            if (locMatcher.matches()) {
                _locations.add(new Location(locMatcher.group(1),
                                            new Double(locMatcher.group(2)),
                                            new Double(locMatcher.group(3))));
                continue;
            }

            roadMatcher = ROAD_REGEX.matcher(line);
            if (roadMatcher.matches()) {
                String start = roadMatcher.group(1);
                String name = roadMatcher.group(2);
                Double length = new Double(roadMatcher.group(3));
                Direction direction = firstCharDirection(roadMatcher.group(4));
                String end = roadMatcher.group(5);

                _roads.add(new Road(start, name, length, direction, end));
                _roads.add(new Road(end, name, length, direction.opposite(),
                                    start));
                continue;
            } else if (!line.isEmpty()) {
                throw new TripException("incorrect mapfile format");
            }
        }
    }

    /** Print a brief usage message and exit program abnormally. */
    private static void usage() {
        try {
            InputStream resource =
                Main.class.getClassLoader().getResourceAsStream(USAGE);
            BufferedReader str =
                new BufferedReader(new InputStreamReader(resource));
            for (String s = str.readLine(); s != null; s = str.readLine())  {
                out.println(s);
            }
            str.close();
            out.flush();
        } catch (IOException excp) {
            out.printf("No usage found.");
            out.flush();
        }
        System.exit(1);
    }

    /** Regex for a Location entry. */
    private static final Pattern LOCATION_REGEX =
        Pattern.compile("\\s*?L\\s+"
                      + "([\\w\\p{Punct}]+)\\s+"
                      + "([-+]?\\d*(?:\\.\\d+)?)\\s+"
                      + "([-+]?\\d*(?:\\.\\d+)?)\\s*?");
    /** Regex for a Road entry. */
    private static final Pattern ROAD_REGEX =
        Pattern.compile("\\s*?R\\s+"
                      + "([\\w\\p{Punct}]+)\\s+"
                      + "([\\w\\p{Punct}]+)\\s+"
                      + "([-+]?\\d*(?:\\.\\d+)?)\\s+"
                      + "(NS|EW|WE|SN)\\s+"
                      + "([\\w\\p{Punct}]+)\\s*?");

    /** List of roads from MAPFILE. */
    private static List<Road> _roads;
    /** List of locations from MAPFILE. */
    private static List<Location> _locations;
    /** Scanner for map file and requests. */
    private static Scanner _scn;

    /** Location of usage message resource. */
    static final String USAGE = "trip/Usage.txt";

}
