package ch.dueni.insight2jsf.bean;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;

import org.junit.BeforeClass;
import org.junit.Test;

import ch.dueni.insight2jsf.mock.FacesMock;

public class BindingsTest extends FacesMock {
	
	@BeforeClass
	public static void beforeClass() {
		prepareMockList();
		UIOutput comp = mockList.createMock(UIOutput.class);
		expect(comp.getId()).andReturn("boundCompId").anyTimes();
		Bindings bindings = new Bindings();
		addManagedBean("bindings", bindings);
		mockList.replay();

		bindings.put("boundComp", comp);
	}

	@Test
	public void bindings() {
		Bindings bindings = Bindings.getInstance();
		assertNotNull(bindings);
		assertEquals("boundCompId", bindings.get("boundComp").getId());
		
		// changed viewRoot must cause clear on Bindings map
		viewRoot = mockList.createMock(UIViewRoot.class);
		assertNull(bindings.get("boundComp"));
		
	}
}
