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
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
		if (!userService.isAnonymousUser(currentCustomer))
		{



			List<NotificationData> notificationMessagelist = new ArrayList<NotificationData>();
			final String customerUID = currentCustomer.getUid();

			if (null != customerUID)
			{
				notificationMessagelist = notificationFacade.getNotificationDetail(customerUID, true);


				if (null != notificationMessagelist && !notificationMessagelist.isEmpty())
				{

					model.addAttribute(ModelAttributetConstants.NOTIFICATION_MESSAGE_LIST, notificationMessagelist);

					int notificationCount = 0;
					for (final NotificationData single : notificationMessagelist)
					{
						if (single.getNotificationRead() != null && !single.getNotificationRead())
						{
							notificationCount++;
						}

					}
					model.addAttribute(ModelAttributetConstants.NOTIFICATION_COUNT, notificationCount);
					model.addAttribute(ModelAttributetConstants.IS_SIGNED_IN, "yes");
				}

				model.addAttribute(ModelAttributetConstants.NOTIFICATION_MESSAGE_LIST, notificationMessagelist);
			}
		}
		return ControllerConstants.Views.Fragments.Home.TrackOrderPanel;
	}


	/* Mark Notification As Read */

	@ResponseBody
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MARKREAD, method = RequestMethod.GET, produces = "application/json")
	public void markAsRead(final String currentId, final String consignmentNo, final String shopperStatus)
	{

		try
		{
			final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
			final String customerUID = currentCustomer.getUid();
			if (null != customerUID)
			{

				notificationFacade.markNotificationRead(customerUID, currentId, consignmentNo, shopperStatus);
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

	}
}
