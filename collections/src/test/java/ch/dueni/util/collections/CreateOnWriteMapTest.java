/**
 * Copyright 2012 by dueni.ch
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

package ch.dueni.util.collections;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CreateOnWriteMapTest {

	// use _ prefix for better distinction with test local variables
	private Map<String, String> _map;

	/**
	 * Return map preset to CreateOnWriteMap.
	 * 
	 * @return map preset to CreateOnWriteMap.
	 */
	public Map<String, String> getMapPreset() {
		if (_map == null) {
			_map = new CreateOnWriteMap<String, String>() {
				@Override
				public Map<String, String> newMap() {
					// replace list attribute with real List and return list for current operation
					_map = new HashMap<String, String>(2);
					return _map;
				}
			};
		}
		return _map;
	}

	/**
	 * Return new CreateOnWriteMap unless map was created by first put.
	 * 
	 * @return new CreateOnWriteMap unless map was created by first put.
	 */
	public Map<String, String> getMapNull() {
		if (_map == null) {
			return new CreateOnWriteMap<String, String>() {
				@Override
				public Map<String, String> newMap() {
					// replace list attribute with real List and return list for current operation
					_map = new HashMap<String, String>(2);
					return _map;
				}
			};
		}
		return _map;
	}

	@Before
	public void beforeTest() {
		_map = null;
	}

	// test CreateOnWriteMap assigned to owning object local variable

	@Test
	public void testPresetMap() {
		// with CreateOnWriteMap it is recommended to not store the map locally, since first put will
		// replace the map on the owning object - however, for unit tests it help to verify the API 
		assertNull(_map);
		Map<String, String> m = getMapPreset();
		assertTrue(_map instanceof CreateOnWriteMap);
		assertTrue(m instanceof CreateOnWriteMap);
		// test all non entry-adding methods on Map interface
		assertTrue(m.isEmpty());
		m.clear(); // no exception expected
		assertFalse(m.containsKey("a"));
		assertFalse(m.containsValue("a"));
		assertEquals(0, m.entrySet().size());
		assertNull(m.get("a"));
		assertEquals(0, m.keySet().size());
		assertNull(m.remove("a"));
		assertEquals(0, m.size());
		assertEquals(0, m.values().size());
	}

	@Test
	public void testPresetMapPut() {
		assertNull(_map);
		assertTrue(getMapPreset() instanceof CreateOnWriteMap);
		assertNull(getMapPreset().put("a", "a"));
		// now we should get a real map
		Map<String, String> m = getMapPreset();
		assertTrue(m instanceof HashMap);
		assertEquals(1, m.size());
		assertEquals("a", m.get("a"));
		assertNull(m.get("b"));
	}

	@Test
	public void testPresetMapPutAll() {
		Map<String, String> arg = new HashMap<String, String>();
		arg.put("a", "a");
		arg.put("b", "b");
		arg.put("c", "c");

		assertNull(_map);
		assertTrue(getMapPreset() instanceof CreateOnWriteMap);
		getMapPreset().putAll(arg); // no exception expected
		// now we should get a real map
		Map<String, String> m = getMapPreset();
		assertTrue(m instanceof HashMap);
		assertTrue(_map instanceof HashMap);
		assertEquals(3, m.size());
		assertEquals("a", m.get("a"));
		assertEquals("b", m.get("b"));
		assertEquals("c", m.get("c"));
		assertNull(m.get("d"));
	}

	// test CreateOnWriteMap NOT assigned to owning object local variable

	@Test
	public void testNullMap() {
		// with CreateOnWriteMap it is recommended to not store the map locally, since first put will
		// replace the map on the owning object - however, for unit tests it help to verify the API  
		assertNull(_map);
		Map<String, String> m = getMapNull();
		assertNull(_map);
		assertTrue(m instanceof CreateOnWriteMap);
		// test all non entry-adding methods on Map interface
		assertTrue(m.isEmpty());
		m.clear(); // no exception expected
		assertFalse(m.containsKey("a"));
		assertFalse(m.containsValue("a"));
		assertEquals(0, m.entrySet().size());
		assertNull(m.get("a"));
		assertEquals(0, m.keySet().size());
		assertNull(m.remove("a"));
		assertEquals(0, m.size());
		assertEquals(0, m.values().size());
	}

	@Test
	public void testNullMapPut() {
		assertNull(_map);
		assertTrue(getMapNull() instanceof CreateOnWriteMap);
		assertNull(_map);
		assertNull(getMapPreset().put("a", "a"));
		// now we should get a real map
		Map<String, String> m = getMapPreset();
		assertTrue(m instanceof HashMap);
		assertTrue(_map instanceof HashMap);
		assertEquals(1, m.size());
		assertEquals("a", m.get("a"));
		assertNull(m.get("b"));
	}

	@Test
	public void testNullMapPutAll() {
		Map<String, String> arg = new HashMap<String, String>();
		arg.put("a", "a");
		arg.put("b", "b");
		arg.put("c", "c");

		assertNull(_map);
		assertTrue(getMapNull() instanceof CreateOnWriteMap);
		assertNull(_map);
		getMapNull().putAll(arg); // no exception expected
		// now we should get a real map
		Map<String, String> m = getMapNull();
		assertTrue(m instanceof HashMap);
		assertTrue(_map instanceof HashMap);
		assertEquals(3, m.size());
		assertEquals("a", m.get("a"));
		assertEquals("b", m.get("b"));
		assertEquals("c", m.get("c"));
		assertNull(m.get("d"));
	}

	// make sure wrapped Map delegation is correct in case CreatOnWriteMap was kept on local variable

	@Test
	public void testCreateOnWriteMapAsWrapper() {
		Map<String, String> all = new HashMap<String, String>();
		all.put("a", "a");
		all.put("b", "b");
		all.put("c", "c");

		assertNull(_map);
		Map<String, String> m = getMapNull();
		assertTrue(m instanceof CreateOnWriteMap);
		assertNull(_map);
		assertEquals(0, m.size());
		m.put("K", "V");
		assertNotNull(_map);
		assertTrue(m.equals(_map));
		assertFalse(m.isEmpty());
		assertTrue(m.containsKey("K"));
		assertTrue(m.containsValue("V"));
		assertEquals("V", m.get("K"));
		assertTrue(m.entrySet().iterator().hasNext());
		assertFalse(m.keySet().isEmpty());
		assertEquals("V", m.remove("K"));
		assertEquals(0, m.size());
		m.putAll(all);
		assertEquals(3, m.size());
		m.put("K", "V");
		assertEquals(4, m.size());
		assertEquals(4, m.values().size());
		m.clear();
		assertEquals(0, m.size());
		assertTrue(m.isEmpty());

	}

}
