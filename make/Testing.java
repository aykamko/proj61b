package make;

/* You MAY add public @Test methods to this class.  You may also add
 * additional public classes containing "Testing" in their name. These
 * may not be part of your make package per se (that is, it must be
 * possible to remove them and still have your package work). */

import org.junit.Test;
import ucb.junit.textui;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import java.util.Set;
import java.util.HashSet;

/** Unit tests for the make package. */
public class Testing {

    /** Run all JUnit tests in the make package. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(make.Testing.class,
                                      make.TargetBuilderTesting.class));
    }

    @Test
    public void testMakeRule() {
        String target = "T1";
        String header = "P1 P2    P5  P4 P1 P2";
        Main makeMain = new Main();

        Rule rule =
            (Rule) callPrivateMethod(makeMain, "makeRule",
                    new Class<?>[]{ String.class,
                                    String.class },
                                    target, header);

        Set<String> prereqsCompare = new HashSet<String>();
        prereqsCompare.add("P1");
        prereqsCompare.add("P2");
        prereqsCompare.add("P4");
        prereqsCompare.add("P5");

        assertEquals("incorrect: Main.makeRule()",
                target, rule.target());
        assertEquals("incorrect: Main.makeRule()",
                prereqsCompare, rule.prereqSet());
    }

    @Test
    public void testExtendRule1() {
        String target = "T1";
        String header = "P9 P8 P7";
        Main makeMain = new Main();

        Rule rule = new Rule(target);
        rule.addPrereq("P1");
        rule.addPrereq("P2");
        rule.addPrereq("P4");
        rule.addPrereq("P5");

        callPrivateMethod(makeMain, "extendRule",
                    new Class<?>[]{ String.class,
                                    String.class,
                                    Rule.class },
                                    target, header, rule);

        Set<String> prereqsCompare = new HashSet<String>();
        prereqsCompare.add("P1");
        prereqsCompare.add("P2");
        prereqsCompare.add("P4");
        prereqsCompare.add("P5");
        prereqsCompare.add("P7");
        prereqsCompare.add("P8");
        prereqsCompare.add("P9");

        assertEquals("incorrect: Main.extendRule()",
                prereqsCompare, rule.prereqSet());
    }

    @Test (expected = MakeException.class)
    public void testExtendRule2() {
        String target = "T1";
        String header = "P9 P8 P7";
        Main makeMain = new Main();

        Rule rule = new Rule("NotT1");
        rule.addPrereq("P1");

        callPrivateMethod(makeMain, "extendRule",
                    new Class<?>[]{ String.class,
                                    String.class,
                                    Rule.class },
                                    target, header, rule);
    }

    /** Returns the castable Object returned by invoking the private method
     *  NAME with parameters PARAMS of object OBJ with arguments ARGS. */
    private Object callPrivateMethod(Object obj, String name,
            Class<?>[] params, Object... args) {
        try {
            Method method = obj.getClass().getDeclaredMethod(name, params);
            method.setAccessible(true);
            return method.invoke(obj, (Object[]) args);
        } catch (NoSuchMethodException | InvocationTargetException
            | IllegalAccessException e) {
            throw new MakeException();
        }
    }

}
