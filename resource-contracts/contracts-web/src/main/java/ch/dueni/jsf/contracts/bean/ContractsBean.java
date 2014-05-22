package ch.dueni.jsf.contracts.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ContractsBean implements Serializable {
    
	private static final long serialVersionUID = 2134551344109504345L;

	private String contracts;

    public String getContracts() {
        return contracts;
    }

    public void setContracts(String contracts) {
    		this.contracts = contracts;
    }
}
