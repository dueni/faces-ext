package ch.dueni.prefs;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

public class TestHttpSession implements HttpSession {

	private Map<String, Object> attMap = new HashMap<String, Object>();

	@Override
	public long getCreationTime() {

		return 0;
	}

	@Override
	public String getId() {

		return null;
	}

	@Override
	public long getLastAccessedTime() {

		return 0;
	}

	@Override
	public ServletContext getServletContext() {

		return null;
	}

	@Override
	public void setMaxInactiveInterval(int interval) {

	}

	@Override
	public int getMaxInactiveInterval() {

		return 0;
	}

	@Override
	public HttpSessionContext getSessionContext() {

		return null;
	}

	@Override
	public Object getAttribute(String name) {

		return attMap.get(name);
	}

	@Override
	public Object getValue(String name) {

		return null;
	}

	@Override
	public Enumeration getAttributeNames() {
		return Collections.enumeration(attMap.keySet());
	}

	@Override
	public String[] getValueNames() {

		return null;
	}

	@Override
	public void setAttribute(String name, Object value) {
		attMap.put(name, value);
	}

	@Override
	public void putValue(String name, Object value) {

	}

	@Override
	public void removeAttribute(String name) {
		attMap.remove(name);
	}

	@Override
	public void removeValue(String name) {

	}

	@Override
	public void invalidate() {

	}

	@Override
	public boolean isNew() {

		return false;
	}

}
