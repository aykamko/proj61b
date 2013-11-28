package make;

import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;

import java.util.Scanner;

import java.io.IOException;
import java.util.NoSuchElementException;

import java.io.File;

/** Initial class for the 'make' program.
 *  @author
 */
public final class Main {

    /** Entry point for the CS61B make program.  ARGS may contain options
     *  and targets:
     *      [ -f MAKEFILE ] [ -D FILEINFO ] TARGET1 TARGET2 ...
     */
    public static void main(String... args) {
        String makefileName;
        String fileInfoName;

        if (args.length == 0) {
            usage();
        }

        makefileName = "Makefile";
        fileInfoName = "fileinfo";

        int a;
        for (a = 0; a < args.length; a += 1) {
            if (args[a].equals("-f")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    makefileName = args[a];
                }
            } else if (args[a].equals("-D")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    fileInfoName = args[a];
                }
            } else if (args[a].startsWith("-")) {
                usage();
            } else {
                break;
            }
        }

        ArrayList<String> targets = new ArrayList<String>();

        for (; a < args.length; a += 1) {
            targets.add(args[a]);
        }

        make(makefileName, fileInfoName, targets);
    }

    /** Carry out the make procedure using MAKEFILENAME as the makefile,
     *  taking information on the current file-system state from FILEINFONAME,
     *  and building TARGETS, or the first target in the makefile if TARGETS
     *  is empty.
     */
    private static void make(String makefileName, String fileInfoName,
                             List<String> targets) {
        try {
            readFileInfoFile(new File(fileInfoName));
            readMakeFile(new File(makefileName));
        } catch (IOException | IllegalArgumentException
                | NoSuchElementException e) {
            usage();
        }

        //TODO: other stuff
    }

    /** Reads the infoFile file and stores the current time and changetime for
     *  each name. Throws an exception if the file does not exist or if there 
     *  is some format error. */
    private static void readFileInfoFile(File infoFile)
        throws IOException, IllegalArgumentException, NoSuchElementException {
        Scanner scanner = new Scanner(infoFile);

        Matcher m = CURTIME_REGEX.matcher(scanner.nextLine());
        if (m.matches()) {
            _curTime = Integer.parseInt(m.group(1));
        } else {
            throw new IllegalArgumentException();
        }

        _nameChangeMap = new HashMap<String, Integer>();
        String mapping, name, changeDate;
        while (scanner.hasNextLine()) {
            mapping = scanner.nextLine();
            m = NAMECHANGE_REGEX.matcher(mapping);
            if (m.matches()) {
                name = m.group(1);
                changeDate = m.group(2);
                _nameChangeMap.put(name, Integer.valueOf(changeDate));
            } else {
                throw new IllegalArgumentException();
            }
        }

        scanner.close();
    }

    /** Reads the MAKEFILE and stores each rule as a Rule object. Throws an
     *  exception if the file does not exist or if there is some format 
     *  error. */
    private static void readMakeFile(File makeFile)
        throws IOException, IllegalArgumentException {
        Scanner scanner = new Scanner(makeFile);
        _ruleList = new ArrayList<Rule>();

        Rule rule = null;
        while (true) {
            if (scanner.findWithinHorizon(MAKEFILE_REGEX, 0) != null) {
                MatchResult m = scanner.match();
                if (m.end(TARGET_TOKEN) > -1) {
                    rule = new Rule(m.group(TARGET_TOKEN));
                } else if (m.end(PREREQ_TOKEN) > -1) {
                    if (rule != null) {
                        rule.addCommand(m.group(PREREQ_TOKEN));
                    } else {
                        throw new IllegalArgumentException();
                    }
                } else if (m.end(ERROR_TOKEN) > -1) {
                    throw new IllegalArgumentException();
                } else {
                    if (rule != null) {
                        _ruleList.add(rule);
                        rule = null;
                    }
                }
            } else {
                scanner.close();
                break;
            }
        }
    }

    /** Print a brief usage message and exit program abnormally. */
    private static void usage() {
        // FILL THIS IN
        System.exit(1);
    }

    /** Current time of fileInfo file. */
    private static int _curTime;
    /** Mappings from NAME to CHANGEDATE. */
    private static HashMap<String, Integer> _nameChangeMap;
    /** List of Rules from the MAKEFILE. */
    private static List<Rule> _ruleList;

    /** Regex to match current time at the first line of a fileInfo file. */
    private static final Pattern CURTIME_REGEX = 
        Pattern.compile("\\s*?(\\d+)\\s*?");
    /** Regex to match a NAME CHANGEDATE on a line of a fileInfo file. */
    private static final Pattern NAMECHANGE_REGEX =
        Pattern.compile("\\s*?(\\w+)\\s+(\\d+)\\s*?");

    /** Regex to read a MAKEFILE. */
    private static final Pattern MAKEFILE_REGEX =
        Pattern.compile("(?s)(\\p{Blank}+)"
                      + "|(\\r?\\n((?:\\r?\\n)+)?)"
                      + "|(#[^\\n]*?\\n)"
                      + "|([^:=#\\\\]:)"
                      + "|([^:=#\\\\])"
                      + "|(.)");

    /** Tokens that are matched in a makefile. */
    private static final int
        /** A target from a Rule. */
        TARGET_TOKEN = 4,
        /** A prerequisite for a target. */
        PREREQ_TOKEN = 5,
        /** A character that shouldn't be there. */
        ERROR_TOKEN = 6;

}
