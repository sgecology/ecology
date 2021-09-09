/**
 * 
 */
package net.ecology.framework.controller;

import javax.inject.Inject;

import net.ecology.framework.model.FilterBase;
import net.ecology.utility.FacesService;

/**
 * @author bqduc
 *
 */
public abstract class Home <E, F extends FilterBase> extends UIControllerRoot {
	private static final long serialVersionUID = -3418849331359820691L;

	//protected abstract E buildNoneObject();
	@Inject
	protected FacesService facesService;

	final public String delete() {
		return deleteSelectedObject();
	}

	protected String deleteSelectedObject() {
		return "success";
	}

	public void preRenderComponent () {
		onPreRenderComponent ();
	}

  public void postValidate () {
  	onPostValidate ();
  }

  public void preValidate () {
  	onPreValidate ();
  }

  public void postAddToView () {
  	onPostAddToView ();
  }

	protected void onPreRenderComponent () {
		//System.out.println("Fired preRenderComponent: " + Calendar.getInstance().getTime());
	}

	protected void onPostValidate () {
		//System.out.println("Fired postValidate: " + Calendar.getInstance().getTime());
  }

	protected void onPreValidate () {
		//System.out.println("Fired preValidate: " + Calendar.getInstance().getTime());
  }

	protected void onPostAddToView () {
		//System.out.println("Fired postAddToView: " + Calendar.getInstance().getTime());
  }
}
