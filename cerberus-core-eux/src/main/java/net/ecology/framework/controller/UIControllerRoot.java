/**
 * 
 */
package net.ecology.framework.controller;

import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.MessageSource;

import net.ecology.framework.component.BasisComp;

/**
 * @author bqduc
 *
 */
public abstract class UIControllerRoot extends BasisComp {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2445529753237451206L;

	protected static final String DEFAULT_PAGE_SIZE = "100";
	protected static final String DEFAULT_PAGE_NUM = "0";

	@Inject
	protected MessageSource persistenceMessageSource;//dbMessageSource

	@Inject
	protected HttpSession httpSession;

	@Inject
	protected HttpServletRequest request;
	
	/*@Inject 
	protected LocaleResolver localeResolver;*/

  @Inject 
  protected ServletContext servletContext;

  protected void cachePut(String key, Object data) {
		this.httpSession.setAttribute(key, data);
	}

	protected Object fetchCachedData(String key) {
		return this.httpSession.getAttribute(key);
	}

	/*protected void routingPage(String pageId) {
		try {
			ExternalContext context = Faces.getExternalContext();
			context.redirect(context.getRequestContextPath() + pageId);
		} catch (Exception e) {
			log.error(e);
		}
	}

	protected void routePage(String pageId) {
		this.routingPage(pageId);
	}

	protected void routePage(String pageId, Boolean invalidateSessionInfoFlag) {
		if (Boolean.TRUE.equals(invalidateSessionInfoFlag)) {
			Faces.getSession().invalidate();
		}
		this.routingPage(pageId);
	}*/

	protected Locale getCurrentLocale() {
		return null;//localizationManager.getLocale();
		//LocaleContextHolder.getLocale();
		
		//return localeResolver.resolveLocale(request);//((SessionLocaleResolver)localeResolver);//LocaleContextHolder.getLocale();
	}
}
