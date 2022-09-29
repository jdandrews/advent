package advent.y2015;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Day12 {

    public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper(); 
        File from = new File("src/advent/y2015/Day12.txt"); 
        TypeReference<HashMap<String,Object>> typeRef 
                = new TypeReference<HashMap<String,Object>>() {};

        HashMap<String,Object> o = mapper.readValue(from, typeRef); 
        System.out.println("got "+o);

        Set<Class<?>> classes = getClasses(o, false);
        System.out.println("top-level objects are "+classes);
        
        System.out.println("part 1: sum = "+sum);

        sum = 0;
        getClasses(o, true);
        System.out.println("part 2: sum = "+sum);
    }

    private static int sum = 0;

    private static Set<Class<?>> getClasses(Map o, boolean skipRed) {
        boolean hasRed=false;
        Set<Class<?>> classes = new HashSet<>();

        for (Object obj : o.values()) {
            if (skipRed && "red".equals(obj)) {
                hasRed=true;
            }
        }

        if (hasRed) {
            return classes;
        }

        for (Object obj : o.values()) {
            classes.add(obj.getClass());
            if (obj instanceof Map) {
                classes.addAll(getClasses((Map)obj, skipRed));
            } else if (obj instanceof List) {
                classes.addAll(getClasses((List)obj, skipRed));
            } else if (obj instanceof Integer) {
                sum += ((Integer)obj).intValue();
            }
        }
        return classes;
    }

    private static Collection<Class<?>> getClasses(List list, boolean skipRed) {
        Set<Class<?>> classes = new HashSet<>();
        for (Object obj : list) {
            classes.add(obj.getClass());
            if (obj instanceof Map) {
                classes.addAll(getClasses((Map)obj, skipRed));
            } else if (obj instanceof List) {
                classes.addAll(getClasses((List)obj, skipRed));
            } else if (obj instanceof Integer) {
                sum += ((Integer)obj).intValue();
            }
        }
        return classes;
    }
}
