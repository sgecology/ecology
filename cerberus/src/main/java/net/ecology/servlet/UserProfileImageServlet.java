/**
 * 
 *//*
package net.ecology.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ecology.auth.service.UserAccountService;
import net.ecology.common.CommonUtility;
import net.ecology.css.service.stock.InventoryService;
import net.ecology.framework.logging.LogService;
import net.ecology.model.auth.UserAccountProfile;

*//**
 * @author ducbq
 *
 *//*
public class UserProfileImageServlet extends ServletCore {
	*//**
	 * 
	 *//*
	private static final long serialVersionUID = -1741403523440066873L;
	
	private UserAccountService businessService;

	@Override
	protected void onInit() throws ServletException {
		this.businessService = (UserAccountService)this.getBean(InventoryService.class);
		this.log = (LogService)this.getBean(LogService.class);
	}

	@Override
	protected void onGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String userProfileIdParam = request.getParameter("userProfileId");
			if (null == this.businessService) {
				businessService = (UserAccountService)this.getBean(UserAccountService.class);
			}

			UserAccountProfile businessObject = businessService.getObject(CommonUtility.parseLong(userProfileIdParam));
			if (null != businessObject && null != businessObject.getAttachment()) {
				response.getOutputStream().write(businessObject.getAttachment().getData());
			}
		} catch (Exception e) {
			log.error(e);
		}
	}
	
}
*/