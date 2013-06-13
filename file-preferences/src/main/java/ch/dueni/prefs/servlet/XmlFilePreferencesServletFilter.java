package ch.dueni.prefs.servlet;

import java.io.IOException;
import java.util.prefs.PreferencesFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import ch.dueni.prefs.PreferencesContext;
import ch.dueni.prefs.XmlFilePreferencesFactory;

public class XmlFilePreferencesServletFilter implements Filter {

	private ServletContext context;

	private String storePath;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		context = filterConfig.getServletContext();
		storePath = context.getInitParameter("ch.dueni.prefs.PREFERENCES_STORE_PATH");
		if (storePath == null) {
			storePath = "/temp/file-prefs";
		}
		String property = PreferencesFactory.class.getName();
		if (System.getProperty(property) == null) {
			System.setProperty(property, XmlFilePreferencesFactory.class.getName());
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// init PreferencesContext
		HttpServletRequest httpReq = (HttpServletRequest)request;
		PreferencesContext prefsCtx = new PreferencesContext();
		prefsCtx.setAppScope(new AppMap(context));
		prefsCtx.setUserScope(new SessionMap(httpReq.getSession()));
		prefsCtx.setUserName(httpReq.getRemoteUser());
		prefsCtx.setStorePath(storePath);
		PreferencesContext.setCurrentInstance(prefsCtx);

		// continue process request filter
		chain.doFilter(request, response);

		// reset PreferencesContext
		PreferencesContext.reset();
	}

	@Override
	public void destroy() {

	}

}
