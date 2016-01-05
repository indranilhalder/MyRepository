/**
 *
 */
package com.tisl.mpl.storefront.controllers.cms;

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



	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final TrackOrderHeaderComponentModel component)
	{
		//do nothing

		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();

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
					if (single.getNotificationRead() != null && single.getNotificationRead() == false)
					{
						notificationCount++;
					}

				}


				model.addAttribute("notificationCount", notificationCount);

			}
		}

	}

}
