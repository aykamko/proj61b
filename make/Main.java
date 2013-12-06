package make;

import java.util.ArrayList;
import java.util.List;

import java.util.Map;
import java.util.HashMap;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;

import java.util.Scanner;

import java.io.IOException;
import java.util.NoSuchElementException;

import java.io.File;

/** Initial class for the 'make' program.
 *  @author Aleks Kamko
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

        //TODO: remove print statement
        for (String s : targets) {
            System.out.println(s);
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
        Map<String, Integer> changeMap = new HashMap<String, Integer>();
        List<Rule> ruleList = new ArrayList<Rule>();

        try {
            readFileInfoFile(new File(fileInfoName), changeMap);
            readMakeFile(new File(makefileName), ruleList);
        } catch (IOException | IllegalArgumentException
                | NoSuchElementException e) {
            usage();
        }

        if (targets.isEmpty()) {
            targets.add(ruleList.get(0).target());
        }

        TargetBuilder builder = 
            new TargetBuilder(ruleList, changeMap, targets);
        try {
            builder.buildTargets();
        } catch (MakeException e) {
            reportError(e.getMessage());
            System.exit(1);
        }

        //TODO: remove print statements
        for (Map.Entry<String, Integer> e : changeMap.entrySet()) {
            System.out.println(e);
        }
        for (Rule r : ruleList) {
            System.out.println(r);
        }

    }

    /** Reads the INFOFILE file and stores the current time and changetime for
     *  each name in the CHANGEDATES Map. Throws an exception if the file does
     *  not exist or if there is some format error. */
    private static int readFileInfoFile(File infoFile, 
            Map<String, Integer> changeMap)
        throws IOException, IllegalArgumentException, NoSuchElementException {
        _scn = new Scanner(infoFile);

        int lastBuildTime = 0;

        Matcher m = CURTIME_REGEX.matcher(_scn.nextLine());
        if (m.matches()) {
            lastBuildTime = Integer.parseInt(m.group(1));
        } else {
            throw new IllegalArgumentException();
        }

        String mapping, name;
        Integer changeDate;
        while (_scn.hasNextLine()) {
            mapping = _scn.nextLine();
            m = NAMECHANGE_REGEX.matcher(mapping);
            if (m.matches()) {
                name = m.group(1);
                changeDate = Integer.valueOf(m.group(2));
                if (changeDate >= lastBuildTime) {
                    throw new IllegalArgumentException();
                }
                changeMap.put(name, changeDate);
            } else {
                throw new IllegalArgumentException();
            }
        }

        _scn.close();
        return lastBuildTime;
    }

    /** Reads the MAKEFILE and stores each rule as a Rule object in RULELIST. 
     *  Throws an exception if the file does not exist or if there is some 
     *  format error. */
    private static void readMakeFile(File makeFile, List<Rule> ruleList)
        throws IOException, IllegalArgumentException {
        _scn = new Scanner(makeFile);

        Rule rule = null;
        while (true) {
            if (_scn.findWithinHorizon(MAKEFILE_REGEX, 0) != null) {
                MatchResult m = _scn.match();
                if (m.end(TARGET_TOKEN) > -1) {
                    if (rule != null) {
                        ruleList.add(rule);
                    }
                    rule = new Rule(m.group(TARGET_TOKEN));
                } else if (m.end(PREREQ_TOKEN) > -1) {
                    if (rule != null) {
                        rule.addCommand(m.group(PREREQ_TOKEN));
                    } else {
                        throw new IllegalArgumentException();
                    }
                } else if (m.end(ERROR_TOKEN) > -1) {
                    throw new IllegalArgumentException();
                } else if (m.end(IGNORE_TOKEN) == -1) {
                    //FIXME?
                }
            } else {
                if (rule != null) {
                    ruleList.add(rule);
                }
                _scn.close();
                break;
            }
        }
    }

    /** Print a brief usage message and exit program abnormally. */
    private static void usage() {
        System.err.println("USAGE");
        System.exit(1);
    }

    /** Prints an error to System.err. Format is the same as printf:
     *  ERROR is the format string, and ARGS are its arguments. */
    private static void reportError(String error, Object... args) {
        System.err.printf(error, args);
        System.out.println();
    }

    /** Scanner for input files. */
    private static Scanner _scn;

    /** Regex to match current time at the first line of a fileInfo file. */
    private static final Pattern CURTIME_REGEX = 
        Pattern.compile("\\s*?(\\d+)\\s*?");
    /** Regex to match a NAME CHANGEDATE on a line of a fileInfo file. */
    private static final Pattern NAMECHANGE_REGEX =
        Pattern.compile("\\s*?(\\w+)\\s+(\\d+)\\s*?");

    /** Regex to read a MAKEFILE. */
    private static final Pattern MAKEFILE_REGEX =
        Pattern.compile("(?m)((?::)?(?:\\s+|(?:#.*)$))"
                     + "|([^:=#\\s\\\\]+(?=:))"
                     + "|([^:=#\\s\\\\]+(?!:))"
                     + "|(.)");

    /** Tokens that are matched in a makefile. */
    private static final int
        /** Token for blanks, newlines, or comments. */
        IGNORE_TOKEN = 1,
        /** A target from a Rule. */
        TARGET_TOKEN = 2,
        /** A prerequisite for a target. */
        PREREQ_TOKEN = 3,
        /** A character that shouldn't be there. */
        ERROR_TOKEN = 4;

}
