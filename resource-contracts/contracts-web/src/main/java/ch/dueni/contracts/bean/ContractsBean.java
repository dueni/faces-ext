package ch.dueni.contracts.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ContractsBean implements Serializable {
    
    String contracts;

    public String getContracts() {
        return contracts;
    }

    public void setContracts(String contracts) {
    		this.contracts = contracts;
    }
}
