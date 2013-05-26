package ch.dueni.jsf12.lifecycle;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;

public class InterceptingLifecycle extends Lifecycle {

	private Lifecycle wrapped;

	public InterceptingLifecycle(Lifecycle standardLifecycle) {
		wrapped = standardLifecycle;
	}

	Lifecycle getWrapped() {
		return wrapped;
	}

	@Override
	public void addPhaseListener(PhaseListener listener) {
		System.out.println("addPhaseListener(" + listener + ")");
		getWrapped().addPhaseListener(listener);
	}

	@Override
	public void execute(FacesContext context) throws FacesException {
		try {
			System.out.println("execute(...)");
			getWrapped().execute(context);
		} catch (FacesException intercepted) {
			System.out.println("===>>> Intercepted Throwable from execute()");
			intercepted.printStackTrace();
			FacesContext.getCurrentInstance().renderResponse();
			throw intercepted;
		}
	}

	@Override
	public PhaseListener[] getPhaseListeners() {
		return getWrapped().getPhaseListeners();
	}

	@Override
	public void removePhaseListener(PhaseListener listener) {
		getWrapped().removePhaseListener(listener);
	}

	@Override
	public void render(FacesContext context) throws FacesException {
		try {
			System.out.println("render(...)");
			getWrapped().render(context);
		} catch (FacesException intercepted) {
			System.out.println("===>>> Intercepted Throwable from render()");
			intercepted.printStackTrace();
			throw intercepted;
		}
	}

}
