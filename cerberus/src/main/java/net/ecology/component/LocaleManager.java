/**
 * 
 */
package net.ecology.component;

import java.util.Locale;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.omnifaces.util.Faces;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import net.ecology.common.CommonUtility;
import net.ecology.global.GlobeConstants;

/**
 * @author ducbq
 *
 */
@ManagedBean
@SessionScoped
public class LocaleManager {
	private Locale locale;

  @Inject
  private HttpSession session;

  @PostConstruct
  public void init() {
      locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
  }

  public Locale getLocale() {
      return locale;
  }

  public String getLanguage() {
      return locale.getLanguage();
  }

  public void setLanguage(String language) {
  	if (CommonUtility.LOCALE_USA.getLanguage().equalsIgnoreCase(language)) {
    	locale = Locale.US;
  	} else {
  		locale = CommonUtility.LOCALE_VIETNAM;
  	}

  	String currentRequestPath = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
  	this.session.setAttribute(GlobeConstants.WORKING_LOCALE, locale);
  	//GlobalI18nRepository.builder().build().switchLocale(locale);
    FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    try {
			Faces.redirect(currentRequestPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
  }

  public void changeLocale(String language){
  	try {
  		//Locale currentLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
  		System.out.println("..............................................................");
    	if (CommonUtility.LOCALE_USA.getLanguage().equalsIgnoreCase(language)) {
      	locale = Locale.US;
    	} else {
    		locale = CommonUtility.LOCALE_VIETNAM;
    	}

    	String currentRequestPath = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
    	this.session.setAttribute(GlobeConstants.WORKING_LOCALE, locale);
    	//this.globalLocaleRepository.switchLocale(locale);
      FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
      try {
  			Faces.redirect(currentRequestPath);
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  	} catch (Exception e) {
			e.printStackTrace();
		}
  }
}
