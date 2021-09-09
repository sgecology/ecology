package net.ecology.mvp.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 * Created by rafael-pestano on 22/06/17.
 */
@Named("dfLevel2MB")
@ViewScoped
public class DFLevel2MB implements Serializable {

    /**
   * 
   */
  private static final long serialVersionUID = -8241707890965707921L;

    public void openLevel3() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("appendTo", "@(body)");
        options.put("styleClass", "dlg3");
        PrimeFaces.current().dialog().openDynamic("level3", options, null);

    }

        //pass back to level 1
    public void onReturnFromLevel3(SelectEvent<?> event) {
        PrimeFaces.current().dialog().closeDynamic(event.getObject());
    }
}
