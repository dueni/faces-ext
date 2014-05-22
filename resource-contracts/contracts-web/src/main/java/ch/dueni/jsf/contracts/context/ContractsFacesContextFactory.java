package ch.dueni.jsf.contracts.context;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;

public class ContractsFacesContextFactory extends FacesContextFactory {
	
	private FacesContextFactory wrapped;
	
	public ContractsFacesContextFactory(FacesContextFactory toWrap) {
		wrapped = toWrap;
	}

	@Override
	public FacesContext getFacesContext(Object context, Object request,
			Object response, Lifecycle lifecycle) throws FacesException {
		FacesContext jsf = getWrapped().getFacesContext(context, request, response, lifecycle);
		return new ContractsFacesContext(jsf);
	}
	
	@Override
	public FacesContextFactory getWrapped() {
		return wrapped;
	}

}
