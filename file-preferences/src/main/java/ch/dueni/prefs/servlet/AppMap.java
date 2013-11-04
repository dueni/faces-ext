package ch.dueni.prefs.servlet;

import java.util.AbstractMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

public class AppMap extends AbstractMap<String, Object> {

	private ServletContext context;

	public AppMap(ServletContext servletContext) {
		context = servletContext;
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		Enumeration<?> names = context.getAttributeNames();
		Map<String, Object> entries = new HashMap<String, Object>();
		while (names.hasMoreElements()) {
			String name = (String)names.nextElement();
			entries.put(name, context.getAttribute(name));
		}
		return entries.entrySet();
	}

	@Override
	public Object put(String key, Object value) {
		Object oldValue = context.getAttribute(key);
		context.setAttribute(key, value);
		return oldValue;
	}

	@Override
	public Object get(Object key) {
		return context.getAttribute((String)key);
	}

	@Override
	public Object remove(Object key) {
		String name = (String)key;
		Object oldValue = context.getAttribute(name);
		context.removeAttribute(name);
		return oldValue;
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsKey(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}
}
