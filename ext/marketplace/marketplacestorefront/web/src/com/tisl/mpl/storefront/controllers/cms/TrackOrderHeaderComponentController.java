/**
 *
 */
package com.tisl.mpl.storefront.controllers.cms;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
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

import com.tisl.mpl.data.NotificationData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.register.NotificationFacade;
import com.tisl.mpl.model.cms.components.TrackOrderHeaderComponentModel;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */

@Controller("TrackOrderHeaderComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.TrackOrderHeaderComponent)
public class TrackOrderHeaderComponentController extends AbstractCMSComponentController<TrackOrderHeaderComponentModel>
{
	@Autowired
	private UserService userService;

	@Resource(name = "notificationFacade")
	private NotificationFacade notificationFacade;




	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final TrackOrderHeaderComponentModel component)
	{
		//do nothing
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
						int notificationCount = 0;
						for (final NotificationData single : notificationMessagelist)
						{
							if (single.getNotificationRead() != null && !single.getNotificationRead().booleanValue())
							{
								notificationCount++;
							}

						}

						model.addAttribute("notificationCount", notificationCount);
						model.addAttribute("isSignedInUser", "yes");
					}
				}
			}
			if (userService.isAnonymousUser(currentCustomer))
			{
				model.addAttribute("isSignedInUser", "no");
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

	@RequireHardLogIn
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String redirect()
	{
		return "redirect:/";
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




}
