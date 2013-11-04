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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * <code>CreateOnWriteMap</code> is intended to be used for most likely empty maps within objects
 * that are intended to keep in session and therefore may occupy memory for long time.&nbsp;
 * <code>CreateOnWriteMap</code> in best usage case will use 0 bytes of memory and still provides a
 * fully operable Map implementation using a call-back method to create the real map before first
 * {@link Map#put(Object, Object)} operation is executed.
 * <p>
 * Memory analysis for different types of maps show:
 * </p>
 * 
 * <pre>
 * Nr Test case                                           retained   shallow
 * == ==================================================  =========  =======
 *  1 HashMap (default size)                                  120       40
 *  2 HashMap (size 0)                                         56       40
 *  3 LinkedHashMap (empty)                                   160       48
 *  4 CreatOnWriteMap (empty, assigned to variable)            32       16
 *  5 CreatOnWriteMap (return new from getList() method)        0        0
 * </pre>
 * 
 * <h5>Example code for above test nr 4</h5>
 * 
 * <pre>
 * public class MyOwner {
 * 	private Map&lt;String, String&gt; map;
 * 
 * 	public MyOwner() {
 * 		map = new CreateOnWriteMap&lt;String, String&gt;() {
 * 			&#064;Override
 * 			public Map&lt;String, String&gt; newMap() {
 * 				map = new HashMap&lt;String, String&gt;(2); // size will grow to 2 on first put anyway
 * 				return map;
 * 			}
 * 		};
 * 	}
 * 
 * 	public Map&lt;String, String&gt; getMap() {
 * 		return map;
 * 	}
 * }
 * </pre>
 * 
 * <h5>Example code for above test nr 5</h5>
 * 
 * <pre>
 * public class MyOwner {
 * 	private Map&lt;String, String&gt; map;
 * 
 * 	public MyOwner() {
 * 	}
 * 
 * 	public Map&lt;String, String&gt; getMap() {
 * 		if (map == null) {
 * 			return new CreateOnWriteMap&lt;String, String&gt;() {
 * 				&#064;Override
 * 				public Map&lt;String, String&gt; newMap() {
 * 					map = new HashMap&lt;String, String&gt;(2); // size will grow to 2 on first put anyway
 * 					return map;
 * 				}
 * 			};
 * 		}
 * 		return map;
 * 	}
 * }
 * </pre>
 * 
 * <h5>Which variant to use?</h5>
 * <p>
 * It is recommended to return new CreateOnWriteMap within the get-method as shown in
 * "example code for test nr 5" unless you have very frequent access to empty maps without putting
 * entries.
 * </p>
 * 
 * @author Hanspeter D&uuml;nnenberger
 */
public abstract class CreateOnWriteMap<K, V> implements Map<K, V> {

	/**
	 * To keep the just created map as delegate in case this CreateOnWriteMap is kept on local
	 * variable.
	 */
	private Map<K, V> wrapped;

	/**
	 * Return the just created real Map after assigning it to the owning object's member variable.
	 * 
	 * Example use:
	 * 
	 * <pre>
	 * public class OwningType {
	 * 	private Map&lt;String, String&gt; map;
	 * 
	 * 	public Map&lt;String, String&gt; getMap() {
	 * 		if (map == null) {
	 * 			return new CreateOnWriteMap&lt;String, String&gt;() {
	 * 
	 * 				&#064;Override
	 * 				public Map&lt;String, String&gt; newMap() {
	 * 					map = new HashMap&lt;String&gt;(2); // init size 2, would grow to 2 on first put anyway
	 * 					return map;
	 * 				}
	 * 			};
	 * 		}
	 * 		return map;
	 * 	}
	 * }
	 * </pre>
	 * 
	 * @return the just created real Map after assigning it to the owning object's member variable.
	 */
	public abstract Map<K, V> newMap();

	/**
	 * Make sure wrapped is assigned from {@link #newMap()} return the wrapped Map.
	 * 
	 * @return the real Map as returned from the call-back.
	 */
	private Map<K, V> getRealMap() {
		if (wrapped == null) {
			wrapped = newMap();
		}
		return wrapped;
	}

	@Override
	public void clear() {
		if (wrapped != null) {
			wrapped.clear();
		}
	}

	@Override
	public boolean containsKey(Object key) {
		if (wrapped != null) {
			return wrapped.containsKey(key);
		}
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		if (wrapped != null) {
			return wrapped.containsValue(value);
		}
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		if (wrapped != null) {
			return wrapped.entrySet();
		}
		return Collections.EMPTY_MAP.entrySet();
	}

	@Override
	public V get(Object key) {
		if (wrapped != null) {
			return wrapped.get(key);
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		if (wrapped != null) {
			return wrapped.isEmpty();
		}
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<K> keySet() {
		if (wrapped != null) {
			return wrapped.keySet();
		}
		return Collections.EMPTY_MAP.keySet();
	}

	/**
	 * Make sure wrapped is assigned from {@link #newMap()} and delegate the passed argument to the
	 * wrapped Map.
	 * 
	 * @see Map#put(Object, Object)
	 */
	@Override
	public V put(K key, V value) {
		return getRealMap().put(key, value);
	}

	/**
	 * Make sure wrapped is assigned from {@link #newMap()} and delegate the passed argument to the
	 * wrapped Map.
	 * 
	 * @see Map#putAll(Map)
	 */
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		getRealMap().putAll(m);
	}

	@Override
	public V remove(Object key) {
		if (wrapped != null) {
			return wrapped.remove(key);
		}
		return null;
	}

	@Override
	public int size() {
		if (wrapped != null) {
			return wrapped.size();
		}
		return 0;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<V> values() {
		if (wrapped != null) {
			return wrapped.values();
		}
		return Collections.EMPTY_MAP.values();
	}

	@Override
	public boolean equals(Object obj) {
		if (wrapped != null) {
			return wrapped.equals(obj);
		}
		return super.equals(obj);
	}
}
