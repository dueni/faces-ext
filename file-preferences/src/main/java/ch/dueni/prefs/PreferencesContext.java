package ch.dueni.prefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PreferencesContext {

	private static ThreadLocal<PreferencesContext> instance = new ThreadLocal<PreferencesContext>() {
		protected PreferencesContext initialValue() {
			return (null);
		}
	};

	private Map<String, Object> appScope;

	private Map<String, Object> userScope;

	private String storePath;

	private String userName;

	private List<XmlFilePreferences.Root> toSave; 
	
	public static PreferencesContext getCurrentInstance() {
		PreferencesContext current = instance.get();
		if (current == null) {
			throw new IllegalStateException("Context must be initialized before use!");
		}
		return current;
	}

	public static void setCurrentInstance(PreferencesContext currentInstance) {
		instance.set(currentInstance);
	}

	public static void reset() {
		instance.remove();
	}

	public void setAppScope(Map<String, Object> appScope) {
		this.appScope = appScope;
	}

	public Map<String, Object> getAppScope() {
		return appScope;
	}

	public void setUserScope(Map<String, Object> userScope) {
		this.userScope = userScope;
	}

	public Map<String, Object> getUserScope() {
		return userScope;
	}

	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}

	public String getStorePath() {
		return storePath;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}
	
	public void addToSave(XmlFilePreferences.Root rootTypeToSave) {
		if (toSave == null) {
			toSave = new ArrayList<XmlFilePreferences.Root>(2);
		}
		if (!toSave.contains(rootTypeToSave)) {
			toSave.add(rootTypeToSave);
		}
	}
	
	public List<XmlFilePreferences.Root> getToSave() {
		if (toSave == null) {
			return Collections.emptyList();
		}
		return toSave;
	}
}
