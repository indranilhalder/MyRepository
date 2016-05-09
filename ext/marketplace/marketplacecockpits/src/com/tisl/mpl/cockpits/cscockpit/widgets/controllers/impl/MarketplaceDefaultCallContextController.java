/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.widgets.controllers.impl;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketplaceCallContextController;
import com.tisl.mpl.cockpits.cscockpit.widgets.models.impl.MarketplaceSearchResultWidgetModel;
import com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl.MarketPlaceAlternateContactDetailsWidgetRenderer;
import com.tisl.mpl.facades.account.register.MplOrderFacade;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.widgets.WidgetConfig;
import de.hybris.platform.cockpit.widgets.models.WidgetModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultCallContextController;
import de.hybris.platform.cscockpit.widgets.models.impl.SearchResultWidgetModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

/**
 * @author 890223
 *
 */
public class MarketplaceDefaultCallContextController extends
		DefaultCallContextController  implements MarketplaceCallContextController{
	private static final Logger LOG = Logger
			.getLogger(MarketplaceDefaultCallContextController.class);
	
	private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*&.]).{8,16})";
	
	@Autowired
	private MplOrderFacade mplOrderFacade;

	@Override
	public UserModel getCurrentUser() {
		return  UISessionUtils.getCurrentSession().getUser();
	}
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SessionService sessionService;

	int totalAttempts = 5;
	/**
	 * @param userName
	 * @param previousPassword
	 * @return boolean
	 */
	@Override
	public String changeAgentPassword(String newPassword,String confirmPassword)
	{
		String message =null;
		UserModel agent = UISessionUtils.getCurrentSession().getUser();
		String previousPwd = getUserService().getPassword(agent);
		final EmployeeModel emp = (EmployeeModel) userService
				.getUserForUID(agent.getUid());
		if (totalAttempts <= 0) {
			message = "EXCEEDED";
			emp.setLoginDisabled(true);
			getModelService().save(emp);			
		} else {
		if( ! StringUtils.equals(newPassword, confirmPassword) ){
			message ="DIFFER";
			totalAttempts = totalAttempts - 1;
		}
		else if(  StringUtils.equals(newPassword, previousPwd) ){
			message ="SAMEASPREVIOUS";
			totalAttempts = totalAttempts - 1;
		}
		else if(passwordValidator(newPassword)){
			getUserService().setPasswordWithDefaultEncoding(agent, newPassword);
			Calendar cal = Calendar.getInstance();
			Date today = cal.getTime();
			emp.setLastPasswordChanged(today);
			getModelService().save(emp);
			getModelService().save(agent);			
			message ="SUCCESS";
			
		} else{
			message ="FAILED";
			totalAttempts = totalAttempts - 1;
		}
		}
		return message;


	}
	
	private boolean passwordValidator(final String password)
	{
		boolean match = false;
		if(password.matches(PASSWORD_PATTERN)) {
			match =true;
		}
		return match;
	}
	
	/**
	 * End call.
	 * The pincode should be removed from the session upon pressing the end call button
	 * @return true, if successful
	 */
	@Override
	public boolean endCall() {
		
		WidgetConfig widgetConfig=getBrowserToSwitchOnEndCall().getWidgetConfig("basketResultWidget");
		((MarketplaceSearchResultWidgetModel) widgetConfig.getWidgetModel()).setPinCode(null);			
		sessionService.removeAttribute(MarketplaceCockpitsConstants.PIN_CODE);

		return super.endCall();
	}

	@Override
	public TypedObject crmTicketGeneration(OrderModel mainOrder,String customerId,String source) {
		
		mplOrderFacade.createcrmTicketForCockpit(mainOrder, customerId,source);
		return null;
	}
	
	

}