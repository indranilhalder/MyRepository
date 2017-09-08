/**
 *
 */
package com.tisl.mpl.storefront.interceptors.beforeview;

import de.hybris.platform.acceleratorfacades.device.DeviceDetectionFacade;
import de.hybris.platform.acceleratorfacades.device.data.DeviceData;
import de.hybris.platform.commercefacades.user.UserFacade;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.tisl.mpl.storefront.interceptors.BeforeViewHandler;


/**
 * @author TCS
 *
 */
public class PromotionalForcedLoginBeforeViewHandler implements BeforeViewHandler
{
	/**
	 * This class checks whether user browsing is logged in or anonymous user.
	 */
	@Resource(name = "userFacade")
	private UserFacade userFacade;
	@Resource(name = "deviceDetectionFacade")
	private DeviceDetectionFacade deviceDetectionFacade;

	@Override
	public void beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView)
			throws Exception
	{
		final String forcedLoginAttr = request.getParameter("boxed-login");
		if (null != forcedLoginAttr && userFacade.isAnonymousUser())
		{
			modelAndView.addObject("forced_login_user", "Y");
			//device detection
			deviceDetectionFacade.initializeRequest(request);
			final DeviceData deviceData = deviceDetectionFacade.getCurrentDetectedDevice();
			modelAndView.addObject("is_mobile", deviceData.getMobileBrowser());
		}
		else
		{
			modelAndView.addObject("forced_login_user", "N");
		}

	}

}
