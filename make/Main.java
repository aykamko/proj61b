package make;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.util.ArrayList;
import java.util.List;

import java.util.Map;
import java.util.HashMap;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.Scanner;

import static java.lang.System.out;

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

        make(makefileName, fileInfoName, targets);
    }

    /** Carry out the make procedure using MAKEFILENAME as the makefile,
     *  taking information on the current file-system state from FILEINFONAME,
     *  and building TARGETS, or the first target in the makefile if TARGETS
     *  is empty.
     */
    private static void make(String makefileName, String fileInfoName,
                             List<String> targets) {
        Map<String, Long> changeMap = new HashMap<String, Long>();
        Map<String, Rule> ruleMap = new HashMap<String, Rule>();
        Rule firstRule = null;

        try {
            readInfoFile(new File(fileInfoName), changeMap);
            firstRule = readMakeFile(new File(makefileName), ruleMap);
        } catch (IOException | IllegalArgumentException
                | NoSuchElementException e) {
            usage();
        }

        if (targets.isEmpty()) {
            targets.add(firstRule.target());
        }

        TargetBuilder builder =
            new TargetBuilder(ruleMap, changeMap, targets);
        try {
            builder.buildTargets();
        } catch (MakeException e) {
            reportError(e.getMessage());
            System.exit(1);
        }

    }

    /** Reads the INFOFILE file and stores the current time and changetime for
     *  each name in the CHANGEMAP Map. Throws an exception if the file does
     *  not exist or if there is some format error. */
    private static void readInfoFile(File infoFile,
            Map<String, Long> changeMap)
        throws IOException, IllegalArgumentException, NoSuchElementException {
        _scn = new Scanner(infoFile);

        long lastBuildTime = 0;

        Matcher m = CURTIME_REGEX.matcher(_scn.nextLine());
        if (m.matches()) {
            lastBuildTime = Long.parseLong(m.group(1));
        } else {
            throw new IllegalArgumentException();
        }

        String mapping, name;
        Long changeDate;
        while (_scn.hasNextLine()) {
            mapping = _scn.nextLine();
            m = NAMECHANGE_REGEX.matcher(mapping);
            if (m.matches()) {
                name = m.group(1);
                changeDate = Long.valueOf(m.group(2));
                if (changeDate >= lastBuildTime) {
                    throw new IllegalArgumentException();
                }
                changeMap.put(name, changeDate);
            } else {
                throw new IllegalArgumentException();
            }
        }

        _scn.close();
    }

    /** Reads the MAKEFILE and stores each rule as a Rule object in RULEMAP.
     *  Returns the first Rule to be added to RULEMAP. Throws an exception if
     *  the file does not exist or if there is some format error. */
    private static Rule readMakeFile(File makeFile, Map<String, Rule> ruleMap)
        throws IOException, IllegalArgumentException {
        _scn = new Scanner(makeFile);

        Matcher ignoreMatcher, headerMatcher, commandMatcher;
        Rule rule, firstRule;
        String target, line;

        boolean duplicate = false;
        rule = firstRule = null;
        target = line = null;
        if (!_scn.hasNextLine()) {
            throw new MakeException("empty makefile");
        }
        line = _scn.nextLine();
        headerMatcher = HEADER_REGEX.matcher(line);
        if (!headerMatcher.matches()) {
            throw new MakeException("no rule for command set");
        }
        target = headerMatcher.group(1);
        firstRule = rule = makeRule(target, headerMatcher.group(2));
        ruleMap.put(target, rule);

        while (_scn.hasNextLine()) {
            line = _scn.nextLine();

            headerMatcher = HEADER_REGEX.matcher(line);
            if (headerMatcher.matches()) {
                target = headerMatcher.group(1);
                rule = ruleMap.get(target);
                if (rule == null) {
                    rule = makeRule(target, headerMatcher.group(2));
                    ruleMap.put(target, rule);
                    duplicate = false;
                } else {
                    extendRule(target, headerMatcher.group(2), rule);
                    if (!rule.commandList().isEmpty()) {
                        duplicate = true;
                    }
                }
                continue;
            }

            commandMatcher = CMND_REGEX.matcher(line);
            if (commandMatcher.matches() && !duplicate) {
                rule.addCommand(line);
                continue;
            }

            ignoreMatcher = IGNORE.matcher(line);
            if (!ignoreMatcher.matches()) {
                throw new MakeException("syntax error in command: " + line);
            }
        }
        ruleMap.put(target, rule);
        return firstRule;
    }

    /** Adds targets specified in HEADER to TARGET's RULE. */
    private static void extendRule(String target, String header, Rule rule) {
        if (!target.equals(rule.target())) {
            throw new MakeException("wrong rule for header");
        }
        _headScn = new Scanner(header);
        while (_headScn.hasNext()) {
            rule.addPrereq(_headScn.next());
        }
    }

    /** Returns a Rule made from the header string HEADER with target TARGET. */
    private static Rule makeRule(String target, String header) {
        _headScn = new Scanner(header);
        Rule rule = new Rule(target);
        while (_headScn.hasNext()) {
            rule.addPrereq(_headScn.next());
        }
        return rule;
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

    /** Prints an error to System.err. Format is the same as printf:
     *  ERROR is the format string, and ARGS are its arguments. */
    private static void reportError(String error, Object... args) {
        System.err.print("Error: ");
        System.err.printf(error, args);
        System.err.println();
    }

    /** Scanner for input files. */
    private static Scanner _scn;
    /** Scanner for rule headers. */
    private static Scanner _headScn;

    /** Regex to match current time at the first line of a fileInfo file. */
    private static final Pattern CURTIME_REGEX =
        Pattern.compile("\\s*?(\\d+)\\s*?");
    /** Regex to match a NAME CHANGEDATE on a line of a fileInfo file. */
    private static final Pattern NAMECHANGE_REGEX =
        Pattern.compile("\\s*?([^:=#\\s\\\\]+)\\s+(\\d+)\\s*?");

    /** Regex to match a header of a Rule. */
    private static final Pattern HEADER_REGEX =
        Pattern.compile("([^:=#\\s\\\\]+):\\s*([^:=#\\\\]*)");
    /** Regex to match a command for a Rule. */
    private static final Pattern CMND_REGEX =
        Pattern.compile("[\\s\\t]+.+");

    /** Regex to match ignored lines. */
    private static final Pattern IGNORE =
        Pattern.compile("([\\s\\t]*)|(#.*)");

    /** Location of usage message resource. */
    static final String USAGE = "make/Usage.txt";

}
