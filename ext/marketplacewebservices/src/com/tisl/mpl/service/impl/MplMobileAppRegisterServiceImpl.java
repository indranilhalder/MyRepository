/**
 *
 */
package com.tisl.mpl.service.impl;

import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.dao.MplMobileAppRegisterDao;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.service.MplMobileAppRegisterService;
import com.tisl.mpl.wsdto.MplUserResultWsDto;




/**
 * @author TCS
 *
 */
public class MplMobileAppRegisterServiceImpl implements MplMobileAppRegisterService
{
	@Autowired
	private MplMobileAppRegisterDao mplMobileAppRegisterDao;

	/**
	 * Save device info for push notification
	 *
	 * @param originalUid
	 * @param platform
	 * @param deviceKey
	 * @param isActive
	 * @return MplUserResultWsDto
	 */
	@Override
	public MplUserResultWsDto mobileKeyRegistration(final String originalUid, final String platform, final String deviceKey,
			final String isActive)
	{

		try
		{

			return mplMobileAppRegisterDao.mobileKeyRegistration(originalUid, platform, deviceKey, isActive);
		}
		catch (final ModelNotFoundException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B9061);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B9060);
		}

	}


}
