import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.beans.BeanGenerator;

/**
 * 
 */

/**
 * @ClassName: Test
 * @Description:
 * @author albert
 * @date 2018年10月10日 上午8:59:03
 * 
 */
public class Test {
	private static Object generateBean(Map<String, Class<?>> propertyMap) {
		BeanGenerator generator = new BeanGenerator();
		propertyMap.forEach((key, value) -> {
			generator.addProperty(key, value);
		});
		return generator.create();
	}
	
	public static void main(String[] args) {
		Map<String, Class<?>> map = new HashMap<>();
		map.put("name", String.class);
		map.put("age", Integer.class);
		Object obj = generateBean(map);
		Class clazz = obj.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            System.out.println(methods[i].getName());
        }
	}
}
