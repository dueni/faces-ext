package ch.dueni.jsf12.context;

import java.util.Iterator;

import javax.el.ELContext;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;

public class CustomFacesContext extends FacesContext {

	private FacesContext delegate;

	private ExternalContext externalContext;
	
	public CustomFacesContext(FacesContext context) {
		delegate = context;
		
    // 1. assign the new FacesContext instance to the ThreadLocal	
    setCurrentInstance(this);

    // 2. put CustomFacesContext as FacesContext to ELContext
    getELContext().putContext(FacesContext.class, this);

    // 3. preset wrapping CustomExternalContext
    externalContext = new CustomExternalContext(delegate.getExternalContext());
	}

	public void addMessage(String clientId, FacesMessage message) {
		delegate.addMessage(clientId, message);
	}

	public Application getApplication() {
		return delegate.getApplication();
	}

	public Iterator<String> getClientIdsWithMessages() {
		return delegate.getClientIdsWithMessages();
	}

	public ELContext getELContext() {
		return delegate.getELContext();
	}
	
	public ExternalContext getExternalContext() {
		return externalContext; 
	}

	public Severity getMaximumSeverity() {
		return delegate.getMaximumSeverity();
	}

	public Iterator<FacesMessage> getMessages() {
		return delegate.getMessages();
	}

	public Iterator<FacesMessage> getMessages(String clientId) {
		return delegate.getMessages(clientId);
	}

	public RenderKit getRenderKit() {
		return delegate.getRenderKit();
	}

	public boolean getRenderResponse() {
		return delegate.getRenderResponse();
	}

	public boolean getResponseComplete() {
		return delegate.getResponseComplete();
	}

	public ResponseStream getResponseStream() {
		return delegate.getResponseStream();
	}

	public ResponseWriter getResponseWriter() {
		return delegate.getResponseWriter();
	}

	public UIViewRoot getViewRoot() {
		return delegate.getViewRoot();
	}

	public void release() {
		delegate.release();
	}

	public void renderResponse() {
		delegate.renderResponse();
	}

	public void responseComplete() {
		delegate.responseComplete();
	}

	public void setResponseStream(ResponseStream responseStream) {
		delegate.setResponseStream(responseStream);
	}

	public void setResponseWriter(ResponseWriter responseWriter) {
		delegate.setResponseWriter(responseWriter);
	}

	public void setViewRoot(UIViewRoot root) {
		delegate.setViewRoot(root);
	}

}
