package ch.dueni.insight2jsf.mock;

import org.easymock.EasyMock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class MockList implements java.util.Collection<Object> {

	private Collection<Object> delegate = new ArrayList<Object>();

	public boolean add(Object o) {
		return delegate.add(o);
	}

	public boolean addAll(Collection<? extends Object> c) {
		return delegate.addAll(c);
	}

	public void clear() {
		delegate.clear();
	}

	public boolean contains(Object o) {
		return delegate.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return delegate.containsAll(c);
	}

	public boolean equals(Object o) {
		return delegate.equals(o);
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	public Iterator<Object> iterator() {
		return delegate.iterator();
	}

	public boolean remove(Object o) {
		return delegate.remove(o);
	}

	public boolean removeAll(Collection<?> c) {
		return delegate.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return delegate.retainAll(c);
	}

	public int size() {
		return delegate.size();
	}

	public Object[] toArray() {
		return delegate.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return delegate.toArray(a);
	}

	@SuppressWarnings("unchecked")
	public <T> T getMock(Class<T> clazz) {
		for (Object mock : delegate) {
			if (clazz.isAssignableFrom(mock.getClass())) {
				return (T) mock;
			}
		}

		return null;
	}

	public void replay() {
		for (Object mock : delegate) {
			EasyMock.replay(mock);
		}
	}

	public void verify() {
		for (Object mock : delegate) {
			EasyMock.verify(mock);
		}
	}

	public void reset() {
		for (Object mock : delegate) {
			EasyMock.reset(mock);
		}
	}

	public void verifyAndReset() {
		for (Object mock : delegate) {
			EasyMock.verify(mock);
			EasyMock.reset(mock);
		}
	}

	public <T> T createMock(Class<T> clazz) {
		T mock = EasyMock.createMock(clazz);
		this.add((Object) mock);
		return mock;
	}

}
