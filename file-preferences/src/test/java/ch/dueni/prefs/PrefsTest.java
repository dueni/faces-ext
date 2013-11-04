package ch.dueni.prefs;

import java.io.File;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.dueni.prefs.servlet.AppMap;
import ch.dueni.prefs.servlet.SessionMap;

import static org.junit.Assert.*;

public class PrefsTest {

	@BeforeClass
	public static void  init() {
		System.setProperty(PreferencesFactory.class.getName(), XmlFilePreferencesFactory.class.getName());
	}

	@Before
	public void setup() {
		PreferencesContext prefsCtx = new PreferencesContext();
		prefsCtx.setAppScope(new AppMap(new TestServletContext()));
		prefsCtx.setUserScope(new SessionMap(new TestHttpSession()));
		prefsCtx.setUserName("A123456");
		File target = new File("target/prefs-test");
		prefsCtx.setStorePath(target.getAbsolutePath());
		PreferencesContext.setCurrentInstance(prefsCtx);
	}

	@After
	public void tearDown() {
		PreferencesContext.cleanup();
	}

	@Test
	public void testSaveBackingStoreWithNoChanges() {
		PreferencesContext ctx = PreferencesContext.getCurrentInstance();
		ctx.saveToBackingStore();
		ctx.saveToBackingStore();
	}
	
	@Test
	public void storeAndRetrieveUserPrefs() throws Exception {
		Preferences myNode = Preferences.userRoot().node("userPath").node("myNode");
		myNode.put("1st", "1st");
		myNode.put("2nd", "2nd");
		myNode.putInt("1stNum", 1);
		Preferences subNode = myNode.node("subNode");
		subNode.putBoolean("defined", true);
		myNode.flush();
		PreferencesContext.getCurrentInstance().saveToBackingStore();
		
		myNode = null;
		subNode = null;
		assertNull(myNode);
		assertNull(subNode);
		myNode = Preferences.userRoot().node("userPath").node("myNode");
		assertNotNull(myNode);
		assertEquals("1st", myNode.get("1st", null));
		assertEquals("2nd", myNode.get("2nd", null));
		assertEquals(1, myNode.getInt("1stNum", -1));
		subNode = myNode.node("subNode");
		assertNotNull(subNode);
		assertTrue(subNode.getBoolean("defined", false));
	}

	@Test
	public void storeAndRetrieveSystemPrefs() throws Exception {
		Preferences myNode = Preferences.systemRoot().node("systemPath").node("myNode");
		myNode.put("1st", "1st");
		myNode.put("2nd", "2nd");
		myNode.putInt("1stNum", 1);
		Preferences subNode = myNode.node("subNode");
		subNode.putBoolean("defined", true);
		myNode.flush();
		PreferencesContext.getCurrentInstance().saveToBackingStore();
		
		myNode = null;
		subNode = null;
		assertNull(myNode);
		assertNull(subNode);
		myNode = Preferences.systemRoot().node("systemPath").node("myNode");
		assertNotNull(myNode);
		assertEquals("1st", myNode.get("1st", null));
		assertEquals("2nd", myNode.get("2nd", null));
		assertEquals(1, myNode.getInt("1stNum", -1));
		subNode = myNode.node("subNode");
		assertNotNull(subNode);
		assertTrue(subNode.getBoolean("defined", false));
	}

	@Test
	public void storeAndRetrieveUserPrefsNoBackingStore() throws Exception {
		PreferencesContext ctx = PreferencesContext.getCurrentInstance();
		ctx.setUserName("A111111");
		Preferences myNode = Preferences.userRoot().node("userPath").node("myNode");
		myNode.put("1st", "1st");
		Preferences subNode = myNode.node("subNode");
		subNode.putBoolean("defined", true);
		myNode.flush();
		ctx.saveToBackingStore();
		myNode = null;
		subNode = null;
		assertNull(myNode);
		assertNull(subNode);
		
		// now make backing store unavailable for read
		ctx.setReadable(false);
		Preferences.userRoot().sync(); // force reload of userRoot
		Preferences userRoot = Preferences.userRoot();
		try {
			userRoot.nodeExists("userPath");
		} catch (BackingStoreException bse) {
			// as expected
			ctx.setReadable(true);
		}
		// now load should work
		userRoot.sync();
		userRoot = Preferences.userRoot();
		userRoot.nodeExists("userPath");
		myNode = userRoot.node("userPath").node("myNode");
		assertNotNull(myNode);
		assertEquals("1st", myNode.get("1st", null));
		subNode = myNode.node("subNode");
		assertNotNull(subNode);
		assertTrue(subNode.getBoolean("defined", false));

		ctx.setWritable(false);
		try {
			userRoot.flush();
			ctx.saveToBackingStore();
		} catch (BackingStoreException bse) {
			// as expected
		} catch (Exception e) {
			fail("unexpected exception: " + e.getMessage());
		}
	}

	@Test
	public void addRemoveAddNodeTest() throws Exception {
		Preferences userRoot = Preferences.userRoot();
		Preferences node = userRoot.node("newNode");
		assertTrue(userRoot.nodeExists("newNode"));
		node.removeNode();
		assertFalse(userRoot.nodeExists("newNode"));
		// test removed flag
		assertFalse(node.nodeExists(""));
		node = userRoot.node("newNode");
		assertNotNull(node);
		assertTrue(userRoot.nodeExists("newNode"));
		// test removed flag
		assertTrue(node.nodeExists(""));
		userRoot.sync();
	}
}
