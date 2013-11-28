package trip;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.List;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.Scanner;

import java.io.IOException;

import java.io.File;

/** Initial class for the 'trip' program.
 *  @author
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
    }

    /** Reads the map files and stores Locations and Distances. Throws an
     *  exception if the file does not exist or if there is some format
     *  error. */
    private static void readMapFile(File mapFile)
        throws IOException, IllegalArgumentException {
        Scanner scanner = new Scanner(mapFile);

        Matcher locMatcher;
        Matcher roadMatcher;
        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();

            locMatcher = LOCATION_REGEX.matcher(line);
            if (locMatcher.matches()) {
                _locations.add(new Location(locMatcher.group(1),
                                            new Integer(locMatcher.group(2)),
                                            new Integer(locMatcher.group(3))));
                continue;
            }
            
            roadMatcher = ROAD_REGEX.matcher(line);
            if (roadMatcher.matches()) {
                _roads.add(new Road(roadMatcher.group(1),
                                    roadMatcher.group(2),
                                    new Integer(roadMatcher.group(3)),
                                    roadMatcher.group(4),
                                    roadMatcher.group(5)));
                continue;
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    /** Print a brief usage message and exit program abnormally. */
    private static void usage() {
        // FILL THIS IN
        System.exit(1);
    }

    /** Regex for a Location entry. */
    private static final Pattern LOCATION_REGEX =
        Pattern.compile("\\s*?L\\s+(\\w+)\\s+(\\d+)\\s+(\\d+)\\s*?");
    /** Regex for a Road entry. */
    private static final Pattern ROAD_REGEX =
        Pattern.compile("\\s*?R\\s+(\\w+)\\s+(\\w+)\\s+(\\d+)\\s+"
                      + "(NS|EW|WE|SN)\\s+(\\w+)\\s*?");

    /** List of roads from MAPFILE. */
    private static List<Road> _roads;
    /** List of locations from MAPFILE. */
    private static List<Location> _locations;

}