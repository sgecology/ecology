/**
 * 
 */
package net.ecology.component;

import java.util.Locale;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.omnifaces.util.Faces;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import net.ecology.common.CommonUtility;
import net.ecology.framework.component.BasisComp;
import net.ecology.global.GlobeConstants;

/**
 * @author ducbq
 *
 */
@ManagedBean
@SessionScoped
public class LocaleDispatchManager extends BasisComp {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3311366117336969365L;

  @Inject
  private HttpSession session;

  public void changeLocale(String targetLocaleLanguage){
  	try {
  		Locale locale = CommonUtility.LOCALE_USA.getLanguage().equalsIgnoreCase(targetLocaleLanguage)?CommonUtility.LOCALE_USA:CommonUtility.LOCALE_VIETNAM;
    	String currentRequestPath = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
    	this.session.setAttribute(GlobeConstants.WORKING_LOCALE, locale);
      FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
 			Faces.redirect(currentRequestPath);
  	} catch (Exception e) {
			logger.error(e);
		}
  }
}
