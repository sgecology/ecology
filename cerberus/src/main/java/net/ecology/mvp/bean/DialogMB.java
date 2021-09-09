package net.ecology.mvp.bean;

import javax.faces.view.ViewScoped;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by rmpestano on 02/02/17.
 */
@Named
@ViewScoped
public class DialogMB implements Serializable {

    /**
   * 
   */
  private static final long serialVersionUID = 8613697045143684378L;

    public void destroyWorld() {
        addMessage("System Error", "Please try again later.");
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

}
