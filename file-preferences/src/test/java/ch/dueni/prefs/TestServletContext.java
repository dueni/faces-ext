package ch.dueni.prefs;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class TestServletContext implements ServletContext {
	
	private Map<String, Object> attrMap = new HashMap<String, Object>();

	@Override
	public String getContextPath() {

		return null;
	}

	@Override
	public ServletContext getContext(String uripath) {

		return null;
	}

	@Override
	public int getMajorVersion() {

		return 0;
	}

	@Override
	public int getMinorVersion() {

		return 0;
	}

	@Override
	public String getMimeType(String file) {

		return null;
	}

	@Override
	public Set getResourcePaths(String path) {

		return null;
	}

	@Override
	public URL getResource(String path) throws MalformedURLException {

		return null;
	}

	@Override
	public InputStream getResourceAsStream(String path) {

		return null;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {

		return null;
	}

	@Override
	public RequestDispatcher getNamedDispatcher(String name) {

		return null;
	}

	@Override
	public Servlet getServlet(String name) throws ServletException {

		return null;
	}

	@Override
	public Enumeration getServlets() {

		return null;
	}

	@Override
	public Enumeration getServletNames() {

		return null;
	}

	@Override
	public void log(String msg) {

	}

	@Override
	public void log(Exception exception, String msg) {

	}

	@Override
	public void log(String message, Throwable throwable) {

	}

	@Override
	public String getRealPath(String path) {

		return null;
	}

	@Override
	public String getServerInfo() {

		return null;
	}

	@Override
	public String getInitParameter(String name) {

		return null;
	}

	@Override
	public Enumeration getInitParameterNames() {

		return null;
	}

	@Override
	public Object getAttribute(String name) {
		return attrMap.get(name);
	}

	@Override
	public Enumeration getAttributeNames() {
		return Collections.enumeration(attrMap.keySet());
	}

	@Override
	public void setAttribute(String name, Object object) {
		attrMap.put(name, object);
	}

	@Override
	public void removeAttribute(String name) {
		attrMap.remove(name);
	}

	@Override
	public String getServletContextName() {

		return null;
	}

}
