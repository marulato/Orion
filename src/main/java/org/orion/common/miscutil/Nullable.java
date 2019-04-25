package org.orion.common.miscutil;

import java.util.Collection;
import java.util.Map;

public final class Nullable {
	

	public static boolean isNull (Object object) {
		if (object == null) {
			return true;
		} else if (object.getClass().isArray()) {
			Object[] objs = (Object[]) object;
			return objs.length == 0;
		} else if (object instanceof Collection) {
			Collection<Object> objs = (Collection<Object>) object;
			return objs.size() == 0;
		} else if (object instanceof Map) {
			Map<Object, Object> objs = (Map<Object, Object>) object;
			return objs.size() == 0;
		} else if (object instanceof String) {
			String objStr = (String) object;
			return objStr.length() == 0;
		} else {
			return false;
		}
	}
	
	private Nullable() {}
}
