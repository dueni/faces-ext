package ch.dueni.prefs.jsf;

import java.util.prefs.PreferencesFactory;

import javax.faces.context.ExternalContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import ch.dueni.prefs.PreferencesContext;
import ch.dueni.prefs.XmlFilePreferencesFactory;


public class JsfXmlPreferencesPhaseListener implements PhaseListener {
	

	private static final long serialVersionUID = 1L;

	/**
	 * If this class is used it should also activate XmlFilePreferencesFactory 
	 */
	static {
		String property = PreferencesFactory.class.getName();
		if (System.getProperty(property) == null) {
			System.setProperty(property, XmlFilePreferencesFactory.class.getName());
		}
	}
	
	@Override
	public void afterPhase(PhaseEvent event) {
		if (PhaseId.RENDER_RESPONSE.equals(event.getPhaseId())) {
			PreferencesContext.reset();
		}

	}

	@Override
	public void beforePhase(PhaseEvent event) {
		if (PhaseId.RESTORE_VIEW.equals(event.getPhaseId())) {

			ExternalContext extCtx = event.getFacesContext().getExternalContext();
			PreferencesContext prefsCtx = new PreferencesContext();
			prefsCtx.setAppScope(extCtx.getApplicationMap());
			prefsCtx.setUserScope(extCtx.getSessionMap());
			prefsCtx.setUserName(extCtx.getRemoteUser());
			prefsCtx.setStorePath("/data/prefs-jsf");
			PreferencesContext.setCurrentInstance(prefsCtx);
		}
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

}
