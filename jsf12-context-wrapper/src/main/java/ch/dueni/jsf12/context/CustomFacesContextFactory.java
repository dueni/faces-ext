package ch.dueni.jsf12.context;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;

public class CustomFacesContextFactory extends FacesContextFactory {

	private FacesContextFactory delegate;

	public CustomFacesContextFactory(FacesContextFactory facesContextFactory) {
		delegate = facesContextFactory;
	}

	public FacesContext getFacesContext(Object context, Object request, Object response,
			Lifecycle lifecycle) throws FacesException {
		System.out.println("==>> CustomFacesContextFactory.getFacesContext()");
		return new CustomFacesContext(delegate.getFacesContext(context, request, response, lifecycle));
	}
}
