package ch.dueni.prefs.servlet;

import java.util.AbstractMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

public class SessionMap extends AbstractMap<String, Object> {

	private HttpSession session;

	public SessionMap(HttpSession httpSession) {
		session = httpSession;
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		Enumeration<?> names = session.getAttributeNames();
		Map<String, Object> entries = new HashMap<String, Object>();
		while (names.hasMoreElements()) {
			String name = (String)names.nextElement();
			entries.put(name, session.getAttribute(name));
		}
		return entries.entrySet();
	}

	@Override
	public Object put(String key, Object value) {
		Object oldValue = session.getAttribute(key);
		session.setAttribute(key, value);
		return oldValue;
	}

	@Override
	public Object get(Object key) {
		return session.getAttribute((String)key);
	}

	@Override
	public Object remove(Object key) {
		String name = (String)key;
		Object oldValue = session.getAttribute(name);
		session.removeAttribute(name);
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
