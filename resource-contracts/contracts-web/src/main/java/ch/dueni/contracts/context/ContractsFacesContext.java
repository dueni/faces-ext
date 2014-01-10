package ch.dueni.contracts.context;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextWrapper;
import javax.faces.event.PhaseId;

/**
 * Wrap FacesContext to allow general contracts activation through a managed
 * bean.
 * 
 * @author dueni
 * 
 */
public class ContractsFacesContext extends FacesContextWrapper {

	private static final Logger LOG = Logger.getLogger(ContractsFacesContext.class.getName());

	private static final String ACTIVE_CONTRACTS = "active-contracts";

	private FacesContext wrapped;

	private boolean activeContractsEvaluated = false;

	public ContractsFacesContext(FacesContext toWrap) {
		wrapped = toWrap;
	}

	@Override
	public FacesContext getWrapped() {
		return wrapped;
	}

	@Override
	public List<String> getResourceLibraryContracts() {
		if (!activeContractsEvaluated) {
			activeContractsEvaluated = true;
			String value = getExternalContext().getInitParameter(ACTIVE_CONTRACTS);
			if (value != null) {
				try {
					ELContext el = getELContext();
					ExpressionFactory elFactory = getApplication().getExpressionFactory();
					ValueExpression ve = elFactory.createValueExpression(el, value, Object.class);
					Object result = ve.getValue(el);
					if (result instanceof String && !((String)result).isEmpty()) {
						String[] contracts = ((String) result).split(",");
						getWrapped().setResourceLibraryContracts(Arrays.asList(contracts));
					}
				} catch (ELException elx) {
					LOG.log(Level.SEVERE, "Exception while evaluating '" + ACTIVE_CONTRACTS
							+ "' web.xml context-parameter!", elx);
				}
			}
		}
		return getWrapped().getResourceLibraryContracts();
	}
	
	@Override
	public void setCurrentPhaseId(PhaseId currentPhaseId) {
		if (currentPhaseId == PhaseId.RENDER_RESPONSE) {
			activeContractsEvaluated = false;
		}
		super.setCurrentPhaseId(currentPhaseId);
	}
}
