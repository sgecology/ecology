package net.ecology.mvp.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 * Created by rafael-pestano on 22/06/17.
 */
@Named("dfRootMB")
@ViewScoped
public class DFRootMB implements Serializable {

    /**
   * 
   */
  private static final long serialVersionUID = -2637668953264515734L;

    public void openLevel1() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("appendTo", "@(body)");
          PrimeFaces.current().dialog().openDynamic("level1", options, null);
    }

    public void onReturnFromLevel1(SelectEvent<?> event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Data Returned", event.getObject().toString()));
    }
}
