/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.constants.MarketplacecheckoutaddonConstants;
import com.tisl.mpl.data.NotificationData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.register.NotificationFacade;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 */
@Controller
@Scope("tenant")
@RequestMapping(value = RequestMappingUrlConstants.HEADER_TRACK_ORDER)
public class TrackOrderHeaderController
{
	@Resource(name = "notificationFacade")
	private NotificationFacade notificationFacade;

	@Autowired
	private UserService userService;

	/**
	 * @param model
	 * @param request
	 * @return fragments/home/trackOrderPanel
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String get(final Model model, final HttpServletRequest request)
	{
		try
		{
			final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
			if (!getUserService().isAnonymousUser(currentCustomer))
			{

				List<NotificationData> notificationMessagelist = new ArrayList<NotificationData>();
				final String customerUID = currentCustomer.getUid();

				if (null != customerUID)
				{
					notificationMessagelist = getNotificationFacade().getNotificationDetail(customerUID, true);

					if (null != notificationMessagelist && !notificationMessagelist.isEmpty())
					{

						model.addAttribute(ModelAttributetConstants.NOTIFICATION_MESSAGE_LIST, notificationMessagelist);

						final int notificationCount = notificationMessagelist.size();

						//						int notificationCount = 0;
						//						for (final NotificationData single : notificationMessagelist)
						//						{
						//							if (single.getNotificationRead() != null && !single.getNotificationRead().booleanValue())
						//							{
						//								notificationCount++;
						//							}
						//
						//						}
						model.addAttribute(ModelAttributetConstants.NOTIFICATION_COUNT, notificationCount);
						model.addAttribute(ModelAttributetConstants.IS_SIGNED_IN, "yes");
					}

					model.addAttribute(ModelAttributetConstants.NOTIFICATION_MESSAGE_LIST, notificationMessagelist);
				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
		}

		return ControllerConstants.Views.Fragments.Home.TrackOrderPanel;
	}


	/* Mark Notification As Read */

	@ResponseBody
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MARKREAD, method = RequestMethod.GET, produces = "application/json")
	public void markAsRead(final String emailId, final String currentId, final String consignmentNo, final String shopperStatus)
	{
		try
		{
			final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
			//final String customerUID = currentCustomer.getUid();
			final String customerUID = currentCustomer.getOriginalUid();
			if (null != customerUID)
			{
				getNotificationFacade().markNotificationRead(emailId, customerUID, currentId, consignmentNo, shopperStatus);
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
		}

	}


	/**
	 * @return the notificationFacade
	 */
	public NotificationFacade getNotificationFacade()
	{
		return notificationFacade;
	}


	/**
	 * @param notificationFacade
	 *           the notificationFacade to set
	 */
	public void setNotificationFacade(final NotificationFacade notificationFacade)
	{
		this.notificationFacade = notificationFacade;
	}


	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}


	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}




}
