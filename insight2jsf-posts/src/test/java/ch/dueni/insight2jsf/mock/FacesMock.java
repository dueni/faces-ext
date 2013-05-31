package ch.dueni.insight2jsf.mock;

import static org.easymock.EasyMock.expect;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;
import javax.faces.application.Application;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.easymock.IAnswer;

public class FacesMock {

	protected static MockList mockList;
	protected static PhaseId currentPhaseId;
	protected static Map<String, Object> applicationMap = new HashMap<String, Object>();
	protected static Map<String, Object> sessionMap = new HashMap<String, Object>();
	protected static Map<String, Object> requestMap = new HashMap<String, Object>();
	protected static Map<String, String> initParams = new HashMap<String, String>();
	protected static UIViewRoot viewRoot;

	/**
	 * Allows setting a mock for calls to FacesContext.getCurrentInstance();
	 * 
	 * @param facesContext
	 */
	protected static void setFacesContextCurrentInstance(FacesContext facesContext) {
		try {
			Method setFacesContext = FacesContext.class.getDeclaredMethod("setCurrentInstance",
					FacesContext.class);
			setFacesContext.setAccessible(true);

			setFacesContext.invoke(null, facesContext);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	protected static void addManagedBean(String name, Object managedBean) {
		ELResolver elr = mockList.getMock(ELResolver.class);
		expect(elr.getValue(mockList.getMock(ELContext.class), null, name)).andReturn(managedBean)
				.anyTimes();
	}

	protected static void prepareMockList() {

		mockList = new MockList();

		FacesContext facesContext = mockList.createMock(FacesContext.class);
		setFacesContextCurrentInstance(facesContext);
		expect(facesContext.getViewRoot()).andAnswer(new IAnswer<UIViewRoot>() {
			@Override
			public UIViewRoot answer() throws Throwable {
				return viewRoot;
			}
		}).anyTimes();
		expect(facesContext.isReleased()).andReturn(Boolean.FALSE).anyTimes();

		expect(facesContext.getCurrentPhaseId()).andAnswer(new IAnswer<PhaseId>() {
			public PhaseId answer() throws Throwable {
				return currentPhaseId;
			}
		}).anyTimes();

		Application application = mockList.createMock(Application.class);
		expect(facesContext.getApplication()).andStubReturn(application);

		ELContext elContext = mockList.createMock(ELContext.class);
		expect(facesContext.getELContext()).andStubReturn(elContext);

		FunctionMapper fm = mockList.createMock(FunctionMapper.class);
		expect(elContext.getFunctionMapper()).andStubReturn(fm);
		VariableMapper vm = mockList.createMock(VariableMapper.class);
		expect(elContext.getVariableMapper()).andStubReturn(vm);

		ELResolver elResolver = mockList.createMock(ELResolver.class);
		expect(elContext.getELResolver()).andStubReturn(elResolver);
		expect(application.getELResolver()).andStubReturn(elResolver);
		elContext.setPropertyResolved(false);

		ExternalContext externalContext = mockList.createMock(ExternalContext.class);
		expect(facesContext.getExternalContext()).andStubReturn(externalContext);
		expect(externalContext.getApplicationMap()).andReturn(applicationMap);
		expect(externalContext.getSessionMap()).andReturn(sessionMap);
		expect(externalContext.getRequestMap()).andReturn(requestMap).anyTimes();

		expect(externalContext.getInitParameterMap()).andReturn(initParams).anyTimes();
	}

}
