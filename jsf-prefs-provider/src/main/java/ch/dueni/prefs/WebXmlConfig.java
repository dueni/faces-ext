package ch.dueni.prefs;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

public enum WebXmlConfig {
	/**
	 * A path relative to WEB-INF or META-INF within a jar on the classpath where an initial state for
	 * systemRoot/userRoot preferences tree may be present.
	 */
	InitialFilePath("ch.dueni.prefs.INITIAL_FILE_PATH", "initial-prefs"),

	/**
	 * Absolute directory where JsfXmlPreferences may store the XML files to persist the system and
	 * user preferences in individual files.
	 */
	PersistenceDir("ch.dueni.prefs.PERSISTENCE_DIR");

	private String contextParamName;
	private String value;

	private WebXmlConfig(String contextParamName) {
		this(contextParamName, null);
	}

	private WebXmlConfig(String contextParamName, String defaultValue) {
		this.contextParamName = contextParamName;
		ExternalContext extCtx = FacesContext.getCurrentInstance().getExternalContext();
		String contextParamValue = extCtx.getInitParameter(contextParamName);
		if (contextParamValue != null) {
			this.value = contextParamValue;
		} else {
			this.value = defaultValue;
		}
	}

	public String getValue() {
		return value;
	}

	public String getContextParamName() {
		return contextParamName;
	}
}
