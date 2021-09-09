package net.ecology.mvp.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.omnifaces.util.Messages;

/**
 * Created by rmpestano on 14/01/17.
 */
@Named
@ViewScoped
public class MessagesMB implements Serializable{

    /**
   * 
   */
  private static final long serialVersionUID = 6803819513767960159L;

    public void info() {
        Messages.create("Info").detail("AdminFaces info message.").add();
    }

    public void warn() {
        Messages.create("Warning!").warn().detail("AdminFaces Warning message.").add();
    }

    public void error() {
        Messages.create("Error!").error().detail("AdminFaces Error message.").add();
    }

    public void fatal() {
        Messages.create("Fatal!").fatal().detail("AdminFaces Fatal message.").add();
    }
}
