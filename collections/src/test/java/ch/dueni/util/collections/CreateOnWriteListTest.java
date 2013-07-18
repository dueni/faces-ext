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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.junit.Before;
import org.junit.Test;

public class CreateOnWriteListTest {

	// use _ prefix for better distinction with test local variables 
	private List<String> _list;

	public List<String> getListPreset() {
		if (_list == null) {
			_list = new CreateOnWriteList<String>() {
				@Override
				public List<String> newList() {
					_list = new ArrayList<String>(1);
					return _list;
				}
			};
		}
		return _list;
	}

	public List<String> getListNull() {
		if (_list == null) {
			return new CreateOnWriteList<String>() {
				@Override
				public List<String> newList() {
					_list = new ArrayList<String>(1);
					return _list;
				}
			};
		}
		return _list;
	}

	@Before
	public void beforeTest() {
		_list = null;
	}

	// test CreateOnWriteList assigned to owning object local variable

	@Test
	public void testListPresetReadApi() {
		assertNull(_list);
		List<String> l = getListPreset();
		assertTrue(_list instanceof CreateOnWriteList);
		assertTrue(l instanceof CreateOnWriteList);

		l.clear(); // no exception expected
		assertFalse(l.contains("a"));
		assertFalse(l.containsAll(getCollection()));
		assertNull(l.get(1));
		assertEquals(-1, l.indexOf("a"));
		assertTrue(l.isEmpty());
		assertFalse(l.iterator().hasNext());
		assertEquals(-1, l.lastIndexOf("a"));
		assertFalse(l.listIterator().hasNext());
		assertFalse(l.listIterator().hasPrevious());
		assertFalse(l.listIterator(1).hasNext());
		assertFalse(l.listIterator(1).hasPrevious());
		assertNull(l.remove(1));
		assertFalse(l.remove("a"));
		assertFalse(l.removeAll(getCollection()));
		assertFalse(l.retainAll(getCollection()));
		assertEquals(0, l.size());
		assertTrue(l.subList(0, 1).isEmpty());
		assertEquals(0, l.toArray().length);
		assertEquals(0, l.toArray(new String[l.size()]).length);
	}

	@Test
	public void testListPresetAdd() {
		assertNull(_list);
		List<String> l = getListPreset();
		assertTrue(_list instanceof CreateOnWriteList);
		assertTrue(l instanceof CreateOnWriteList);
		assertTrue(getListPreset().add("a"));
		l = getListPreset();
		assertTrue(_list instanceof ArrayList);
		assertTrue(l instanceof ArrayList);
		assertEquals(1, l.size());
		assertEquals("a", l.get(0));
		assertEquals(0, l.indexOf("a"));
	}

	@Test
	public void testListPresetAddWithIndex() {
		assertNull(_list);
		List<String> l = getListPreset();
		assertTrue(_list instanceof CreateOnWriteList);
		assertTrue(l instanceof CreateOnWriteList);
		getListPreset().add(0, "a");
		assertTrue(_list instanceof ArrayList);
		l = getListPreset();
		assertTrue(l instanceof ArrayList);
		assertEquals(1, l.size());
		assertEquals("a", l.get(0));
		assertEquals(0, l.indexOf("a"));
	}

	@Test
	public void testListPresetAddAll() {
		assertNull(_list);
		List<String> l = getListPreset();
		assertTrue(_list instanceof CreateOnWriteList);
		assertTrue(l instanceof CreateOnWriteList);
		assertTrue(getListPreset().addAll(getCollection()));
		assertTrue(_list instanceof ArrayList);
		l = getListPreset();
		assertTrue(l instanceof ArrayList);
		assertEquals(getCollection().size(), l.size());
		assertEquals("a", l.get(0));
		assertEquals("b", l.get(1));
		assertEquals("c", l.get(2));
		assertEquals(0, l.indexOf("a"));
		assertEquals(1, l.indexOf("b"));
		assertEquals(2, l.indexOf("c"));
		assertEquals(-1, l.indexOf("d"));
	}

	@Test
	public void testListPresetAddAllWithIndex() {
		assertNull(_list);
		List<String> l = getListPreset();
		assertTrue(_list instanceof CreateOnWriteList);
		assertTrue(l instanceof CreateOnWriteList);
		assertTrue(getListPreset().addAll(0, getCollection()));
		assertTrue(_list instanceof ArrayList);
		l = getListPreset();
		assertTrue(l instanceof ArrayList);
		assertEquals(getCollection().size(), l.size());
		assertEquals("a", l.get(0));
		assertEquals("b", l.get(1));
		assertEquals("c", l.get(2));
		assertEquals(0, l.indexOf("a"));
		assertEquals(1, l.indexOf("b"));
		assertEquals(2, l.indexOf("c"));
		assertEquals(-1, l.indexOf("d"));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testListPresetSet() {
		assertNull(_list);
		List<String> l = getListPreset();
		assertTrue(_list instanceof CreateOnWriteList);
		assertTrue(l instanceof CreateOnWriteList);
		assertNull(getListPreset().set(0, "a"));
	}

	// test CreateOnWriteList NOT assigned to owning object local variable

	@Test
	public void testListNullReadApi() {
		assertNull(_list);
		List<String> l = getListNull();
		assertNull(_list);
		assertTrue(l instanceof CreateOnWriteList);

		l.clear(); // no exception expected
		assertFalse(l.contains("a"));
		assertFalse(l.containsAll(getCollection()));
		assertNull(l.get(1));
		assertEquals(-1, l.indexOf("a"));
		assertTrue(l.isEmpty());
		assertFalse(l.iterator().hasNext());
		assertEquals(-1, l.lastIndexOf("a"));
		assertFalse(l.listIterator().hasNext());
		assertFalse(l.listIterator().hasPrevious());
		assertFalse(l.listIterator(1).hasNext());
		assertFalse(l.listIterator(1).hasPrevious());
		assertNull(l.remove(1));
		assertFalse(l.remove("a"));
		assertFalse(l.removeAll(getCollection()));
		assertFalse(l.retainAll(getCollection()));
		assertEquals(0, l.size());
		assertTrue(l.subList(0, 1).isEmpty());
		assertEquals(0, l.toArray().length);
		assertEquals(0, l.toArray(new String[l.size()]).length);
	}

	@Test
	public void testListNullAdd() {
		assertNull(_list);
		List<String> l = getListNull();
		assertNull(_list);
		assertTrue(l instanceof CreateOnWriteList);
		assertTrue(getListNull().add("a"));
		l = getListNull();
		assertTrue(_list instanceof ArrayList);
		assertTrue(l instanceof ArrayList);
		assertEquals(1, l.size());
		assertEquals("a", l.get(0));
		assertEquals(0, l.indexOf("a"));
	}

	@Test
	public void testListNullAddWithIndex() {
		assertNull(_list);
		List<String> l = getListNull();
		assertNull(_list);
		assertTrue(l instanceof CreateOnWriteList);
		getListNull().add(0, "a");
		assertTrue(_list instanceof ArrayList);
		l = getListNull();
		assertTrue(l instanceof ArrayList);
		assertEquals(1, l.size());
		assertEquals("a", l.get(0));
		assertEquals(0, l.indexOf("a"));
	}

	@Test
	public void testListNullAddAll() {
		assertNull(_list);
		List<String> l = getListNull();
		assertNull(_list);
		assertTrue(l instanceof CreateOnWriteList);
		assertTrue(getListNull().addAll(getCollection()));
		assertTrue(_list instanceof ArrayList);
		l = getListNull();
		assertTrue(l instanceof ArrayList);
		assertEquals(getCollection().size(), l.size());
		assertEquals("a", l.get(0));
		assertEquals("b", l.get(1));
		assertEquals("c", l.get(2));
		assertEquals(0, l.indexOf("a"));
		assertEquals(1, l.indexOf("b"));
		assertEquals(2, l.indexOf("c"));
		assertEquals(-1, l.indexOf("d"));
	}

	@Test
	public void testListNullAddAllWithIndex() {
		assertNull(_list);
		List<String> l = getListNull();
		assertNull(_list);
		assertTrue(l instanceof CreateOnWriteList);
		assertTrue(getListNull().addAll(0, getCollection()));
		assertTrue(_list instanceof ArrayList);
		l = getListNull();
		assertTrue(l instanceof ArrayList);
		assertEquals(getCollection().size(), l.size());
		assertEquals("a", l.get(0));
		assertEquals("b", l.get(1));
		assertEquals("c", l.get(2));
		assertEquals(0, l.indexOf("a"));
		assertEquals(1, l.indexOf("b"));
		assertEquals(2, l.indexOf("c"));
		assertEquals(-1, l.indexOf("d"));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testListNullSet() {
		assertNull(_list);
		List<String> l = getListNull();
		assertNull(_list);
		assertTrue(l instanceof CreateOnWriteList);
		assertNull(getListNull().set(0, "a"));
	}

	// test wrapper handling when CreatOnWriteList is kept on local variable
	
	@Test
	public void testCreateOnWriteListAsWrapper()  {
		List<String> l = getListNull();
		assertNull(_list);
		
		// first write - _list is created and also wrapped in CreatOnWriteList
		l.add("D");
		assertTrue(_list instanceof ArrayList);
		assertTrue(l instanceof CreateOnWriteList);
		assertTrue(l.equals(_list));
		
		// test list operations through CreatOnWrietList wrapper
		assertEquals(l.size(),_list.size());
		assertEquals(l.get(0),_list.get(0));
		assertTrue(l.contains("D"));
		assertFalse(l.containsAll(getCollection()));
		assertEquals("D", l.get(0));
		assertEquals(0, l.indexOf("D"));
		assertFalse(l.isEmpty());
		assertTrue(l.iterator().hasNext());
		assertEquals(0, l.lastIndexOf("D"));
		assertTrue(l.listIterator().hasNext());
		assertFalse(l.listIterator().hasPrevious());
		assertTrue(l.listIterator(0).hasNext());
		assertFalse(l.listIterator(0).hasPrevious());
		assertEquals("D", l.remove(0));
		assertFalse(l.remove("D"));
		assertFalse(l.removeAll(getCollection()));
		assertFalse(l.retainAll(getCollection()));
		assertEquals(0, l.size());
		assertTrue(l.subList(0, 0).isEmpty());
		assertEquals(0, l.toArray().length);
		assertEquals(0, l.toArray(new String[l.size()]).length);

		l.clear();
		assertEquals(0,l.size());
		l.addAll(getCollection());
		assertEquals(3,l.size());
		assertEquals(3,_list.size());

		ListIterator<String> listIterator = l.listIterator();
		assertTrue(listIterator.hasNext());
		assertEquals("a",listIterator.next());
		assertEquals("b",listIterator.next());
		assertEquals("c",listIterator.next());
		assertFalse(listIterator.hasNext());
		assertTrue(listIterator.hasPrevious());
		
		assertTrue(l.listIterator(1).hasPrevious());
		assertTrue(l.listIterator(1).hasNext());

		assertTrue(l.removeAll(getCollection()));
		assertEquals(0,l.size());
		List<String> retain = getCollection();
		l.addAll(retain);
		assertEquals(3,l.size());

		retain.remove(1);
		assertTrue(l.retainAll(retain));
		assertEquals(2, l.size());
		assertTrue(l.contains("a"));
		assertTrue(l.contains("c"));
		
	}
	
	
	
	

	// === private methods ===

	private List<String> getCollection() {
		List<String> c = new ArrayList<String>();
		c.add("a");
		c.add("b");
		c.add("c");
		return c;
	}
}
