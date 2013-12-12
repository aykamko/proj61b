package make;

import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.LinkedList;

/** Tests for TargetBuilder class.
 *  @author Aleks Kamko
 */
public class TargetBuilderTesting {

    @Test (expected = MakeException.class)
    public void testCircularDependency() {
        Map<String, Rule> ruleMap = new HashMap<String, Rule>();
        Rule r1 = new Rule("T1");
        r1.addPrereq("T2");
        ruleMap.put(r1.target(), r1);
        Rule r2 = new Rule("T2");
        r2.addPrereq("T1");
        ruleMap.put(r2.target(), r2);

        TargetBuilder tb = new TargetBuilder(ruleMap, null, null);

        callPrivateMethod(tb, "checkMakefileUsingGraph", new Class<?>[]{});
    }

    @Test (expected = MakeException.class)
    public void testNonexistantTarget() {
        Map<String, Rule> ruleMap = new HashMap<String, Rule>();
        Rule r1 = new Rule("T1");
        r1.addPrereq("T2");
        ruleMap.put(r1.target(), r1);

        Map<String, Long> changeMap = new HashMap<String, Long>();
        changeMap.put("T1", 200L);
        changeMap.put("T2", 300L);

        List<String> targetList = new LinkedList<String>();
        targetList.add("T1");
        targetList.add("bad");

        TargetBuilder tb = new TargetBuilder(ruleMap, changeMap, targetList);
        tb.buildTargets();
    }

    /** Returns the castable Object returned by invoking the private method
     *  NAME with parameters PARAMS of object OBJ with arguments ARGS. Throws
     *  a MakeException if the underlying method threw an exception. */
    private Object callPrivateMethod(Object obj, String name,
            Class<?>[] params, Object... args) {
        try {
            Method method = obj.getClass().getDeclaredMethod(name, params);
            method.setAccessible(true);
            return method.invoke(obj, (Object[]) args);
        } catch (NoSuchMethodException | IllegalAccessException
                | InvocationTargetException e) {
            throw new MakeException();
        }
    }

}
