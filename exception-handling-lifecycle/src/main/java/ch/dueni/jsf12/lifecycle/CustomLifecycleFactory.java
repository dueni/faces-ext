package ch.dueni.jsf12.lifecycle;

import java.util.Iterator;

import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.webapp.FacesServlet;

/**
 * <code>CustomLifecycleFactory</code> enhances the standard JSF <code>LifecycleFactory</code> with
 * additional Lifecyle implementations that
 * can be activated with the <code>FacesServlet</code> init-param
 * {@value FacesServlet#LIFECYCLE_ID_ATTR}.
 * <p>
 * You can activate s specific Lifecycle implementation by configuring this lifecycle id to be used
 * by FacesServlet:
 * </p>
 * <ul>
 * <li>{@value #INTERCEPTING_LIFECYCLE_ID}</li>
 * </ul>
 * 
 */
public class CustomLifecycleFactory extends LifecycleFactory {

	/**
	 * The id of the intercepting lifecycle.
	 */
	public static final String INTERCEPTING_LIFECYCLE_ID = "InterceptingLifecycle";

	/**
	 * LifecycleFactory delegate.
	 */
	private LifecycleFactory delegate;

	/**
	 * Delegate injecting constructor.
	 * 
	 * @param defaultFactory
	 *          the injected delegate LifecycleFactory.
	 */
	public CustomLifecycleFactory(LifecycleFactory defaultFactory) {
		delegate = defaultFactory;

		// 1. get default lifecycle to be wrapped by our lifecycle
		Lifecycle defaultLifecycle = delegate.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);

		// 2. register intercepting lifecycle implementation.
		addLifecycle(INTERCEPTING_LIFECYCLE_ID, new InterceptingLifecycle(defaultLifecycle));
	}

	@Override
	public void addLifecycle(String lifecycleId, Lifecycle lifecycle) {
		delegate.addLifecycle(lifecycleId, lifecycle);
	}

	@Override
	public Lifecycle getLifecycle(String lifecycleId) {
		return delegate.getLifecycle(lifecycleId);
	}

	@Override
	public Iterator<String> getLifecycleIds() {
		return delegate.getLifecycleIds();
	}

}
