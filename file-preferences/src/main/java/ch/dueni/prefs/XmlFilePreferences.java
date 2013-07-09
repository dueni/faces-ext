/**
 * Copyright 2013 by dueni.ch
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.dueni.prefs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * <code>JsfXmlPreferences</code> implements {@link Preferences} handling systemRoot on application
 * scope and userRoot on session scope persisting to one XML file per preferences tree (1 systemRoot
 * tree and 1 userRoot tree per user).
 * 
 * @author hampidu@gmail.com
 */
public class XmlFilePreferences extends AbstractPreferences {

	public static final String SCOPE_PREFERENCES_KEY = XmlFilePreferences.class.getName();

	private Map<String, String> valueMap;

	private Map<String, XmlFilePreferences> childPrefs;

	private Root root;

	private static final String FILE_NAME_PREFIX = "JsfXmlPreferences-";

	private static final String FILE_NAME_SUFFIX = ".xml";

	public enum Root {
		system, user
	}

	protected XmlFilePreferences(XmlFilePreferences parent, String name) {
		super(parent, name);
	}

	void setRoot(Root root) {
		this.root = root;
	}

	Root getRoot() {
		return root;
	}

	@Override
	protected void putSpi(String key, String value) {
		if (valueMap == null) {
			valueMap = new HashMap<String, String>();
		}
		valueMap.put(key, value);
	}

	@Override
	protected String getSpi(String key) {
		return (valueMap == null) ? null : valueMap.get(key);
	}

	@Override
	protected void removeSpi(String key) {
		if (valueMap != null) {
			valueMap.remove(key);
		}
	}

	@Override
	protected void removeNodeSpi() throws BackingStoreException {
		((XmlFilePreferences)parent()).childPrefs.remove(this.name());
	}

	@Override
	protected String[] keysSpi() throws BackingStoreException {
		if (valueMap == null) {
			return new String[0];
		}
		return valueMap.keySet().toArray(new String[0]);
	}

	@Override
	protected String[] childrenNamesSpi() throws BackingStoreException {
		if (childPrefs == null) {
			return new String[0];
		}
		return childPrefs.keySet().toArray(new String[0]);
	}

	@Override
	protected AbstractPreferences childSpi(String name) {
		if (childPrefs == null) {
			childPrefs = new HashMap<String, XmlFilePreferences>();
		}
		XmlFilePreferences child = childPrefs.get(name);
		if (child == null) {
			child = new XmlFilePreferences(this, name);
			childPrefs.put(name, child);
		}
		return child;
	}

	@Override
	public void sync() throws BackingStoreException {
		XmlFilePreferences p = this;
		while (p.parent() != null) {
			p = (XmlFilePreferences)p.parent();
		}

		PreferencesContext ctx = PreferencesContext.getCurrentInstance();

		Root type = p.getRoot();
		if (ctx.getToSave().contains(type)) {
			// flush() pending on this root, abort sync() to not lose not-yet saved changes 
			return;
		}
		if (Root.user.equals(type)) {
			Map<String, Object> sessionMap = ctx.getUserScope();
			sessionMap.remove(SCOPE_PREFERENCES_KEY);
			getUserRoot();
		} else {
			Map<String, Object> appScope = ctx.getAppScope();
			appScope.remove(SCOPE_PREFERENCES_KEY);
			getSystemRoot();
		}

	}

	@Override
	protected void syncSpi() throws BackingStoreException {
		throw new UnsupportedOperationException(
				"Since this implementation manages full tree at once sync() method most be overridden!");
	}

	@Override
	public void flush() throws BackingStoreException {
		XmlFilePreferences p = this;
		while (p.parent() != null) {
			p = (XmlFilePreferences)p.parent();
		}
		Root type = p.getRoot();
		PreferencesContext.getCurrentInstance().addToSave(type);
		//storePreferencesTree(type);
	}

	public synchronized static void storePreferencesTree(Root root) {
		try {
			PreferencesContext ctx = PreferencesContext.getCurrentInstance();
			File storeFile = ensureStoreFile(ctx, root);
			XmlFilePreferences prefs =
					Root.user == root ? XmlFilePreferences.getUserRoot() : XmlFilePreferences.getSystemRoot();
			if (!storeFile.canWrite()) {
				storeFile.setWritable(true);
			}
			if (!storeFile.exists()) {
				storeFile.createNewFile();
			}
			FileOutputStream os = new FileOutputStream(storeFile);
			prefs.exportSubtree(os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void flushSpi() throws BackingStoreException {
		throw new UnsupportedOperationException(
				"Since this implementation manages full tree at once flush() method most be overridden!");
	}

	public static XmlFilePreferences getSystemRoot() {
		PreferencesContext ctx = PreferencesContext.getCurrentInstance();
		Map<String, Object> appScope = ctx.getAppScope();
		XmlFilePreferences systemRoot = (XmlFilePreferences)appScope.get(SCOPE_PREFERENCES_KEY);
		if (systemRoot == null) {
			systemRoot = new XmlFilePreferences(null, "");
			systemRoot.setRoot(Root.system);
			appScope.put(SCOPE_PREFERENCES_KEY, systemRoot);
			importPreferencesTree(ctx, Root.system);
		}
		return systemRoot;
	}

	public static XmlFilePreferences getUserRoot() {
		PreferencesContext ctx = PreferencesContext.getCurrentInstance();
		Map<String, Object> sessionMap = ctx.getUserScope();
		XmlFilePreferences userRoot = (XmlFilePreferences)sessionMap.get(SCOPE_PREFERENCES_KEY);
		if (userRoot == null) {
			userRoot = new XmlFilePreferences(null, "");
			userRoot.setRoot(Root.user);
			sessionMap.put(SCOPE_PREFERENCES_KEY, userRoot);
			importPreferencesTree(ctx, Root.user);
		}
		return userRoot;
	}

	static void importPreferencesTree(PreferencesContext ctx, Root root) {
		try {
			File storeFile = ensureStoreFile(ctx, root);

			if (storeFile.canRead()) {
				InputStream in = new FileInputStream(storeFile);
				Preferences.importPreferences(in);
			}
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	private static File ensureStoreFile(PreferencesContext ctx, Root root) {
		String storePath = ctx.getStorePath();
		if (storePath == null) {
			try {
				storePath = File.createTempFile("dummy", "txt").getParent();
			} catch (IOException e) {
				storePath = "/temp";
			}
		}
		File storeDir = new File(storePath, "prefs-store");
		if (!storeDir.exists()) {
			storeDir.mkdirs();
		}
		String name = root.toString();
		if (root == Root.user) {
			name = name + "-" + ctx.getUserName();
		}
		String fileName = FILE_NAME_PREFIX + name + FILE_NAME_SUFFIX;
		File xmlFile = new File(storeDir, fileName);
		if (!xmlFile.exists()) {
			// TODO: copy initial state file into store-dir			

		}
		return xmlFile;
	}
}
