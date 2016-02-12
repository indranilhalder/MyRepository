/**
 *
 */
package com.tisl.mpl.storefront.controllers.cms;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tisl.mpl.data.NotificationData;
import com.tisl.mpl.facades.account.register.NotificationFacade;
import com.tisl.mpl.model.cms.components.TrackOrderHeaderComponentModel;
import com.tisl.mpl.storefront.controllers.ControllerConstants;


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

	@Autowired
	private NotificationFacade notificationFacade;




	@SuppressWarnings("boxing")
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final TrackOrderHeaderComponentModel component)
	{
		//do nothing

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


					int notificationCount = Integer.valueOf(0);
					for (final NotificationData single : notificationMessagelist)
					{
						if (single.getNotificationRead() != null && !single.getNotificationRead())
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

	@RequireHardLogIn
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String redirect()
	{
		return "redirect:/";
	}

}
