package ch.dueni.prefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

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

	private boolean readable = true;

	private boolean writable = true;

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

	public static void cleanup() {
		PreferencesContext ctx = instance.get();
		if (ctx != null) {
			ctx.saveToBackingStore();
		}
		instance.remove();
	}

	public void saveToBackingStore() {
		// if a flush() call notified to save, store to backing store before reset PreferencesContext
		if (!getToSave().isEmpty()) {
			for (XmlFilePreferences.Root root : getToSave()) {
				XmlFilePreferences.storePreferencesTree(root);
			}
			getToSave().clear();
		}
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

	/**
	 * Returns true if preferences can be read from the files. This is mainly for testing purpose to
	 * simulate unavailable backing store - if set to false, reading preferences from backing store
	 * fails and also any call to {@link Preferences#sync()}.
	 * 
	 * @return true if reading files (backing store of this implementation) is allowed.
	 */
	public boolean isReadable() {
		return readable;
	}

	/**
	 * Set reading permission - if set to false any access to preferences backing store will cause a
	 * {@link BackingStoreException}.
	 * 
	 * @param readable true to allow reading the XML files, false to prevent it.
	 */
	public void setReadable(boolean readable) {
		this.readable = readable;
	}

	/**
	 * Returns true if preferences can be written to the files. This is mainly for testing purpose to
	 * simulate unavailable backing store - if set to false writing preferences from backing store
	 * fails and also any call to {@link Preferences#flush()}.
	 * 
	 * @return true if writing files is allowed.
	 */
	public boolean isWritable() {
		return writable;
	}

	/**
	 * Set writing permission - if set to false any call to {@link Preferences#flush()} will cause a
	 * {@link BackingStoreException}.
	 * 
	 * @param writable true to allow writing the XML files, false to prevent it.
	 */
	public void setWritable(boolean writable) {
		this.writable = writable;
	}
}
