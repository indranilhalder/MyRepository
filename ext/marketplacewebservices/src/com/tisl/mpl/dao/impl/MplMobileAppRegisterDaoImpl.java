/**
 *
 */
package com.tisl.mpl.dao.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.dao.MplMobileAppRegisterDao;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.wsdto.MplUserResultWsDto;


/**
 *
 * @author TCS
 *
 */
public class MplMobileAppRegisterDaoImpl implements MplMobileAppRegisterDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private ModelService modelService;

	/**
	 * Save device info for mobile
	 *
	 * @param originalUid
	 * @param platform
	 * @param deviceKey
	 * @param isActive
	 * @return MplUserResultWsDto
	 */
	@Override
	public MplUserResultWsDto mobileKeyRegistration(final String originalUid, final String platform, final String deviceKey,
			final String isActive) throws EtailNonBusinessExceptions
	{

		final MplUserResultWsDto result = new MplUserResultWsDto();
		CustomerModel customer = null;
		try
		{

			final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery("SELECT {c." + CustomerModel.PK + "} FROM {"
					+ CustomerModel._TYPECODE + " AS c} WHERE  {c." + CustomerModel.ORIGINALUID + "} = ?originalUid");
			flexibleSearchQuery.addQueryParameter("originalUid", originalUid);

			customer = this.flexibleSearchService.searchUnique(flexibleSearchQuery);
			if (customer == null)
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9061);
			}
			// 	update customer
			customer.setDeviceKey(deviceKey);
			customer.setMobilePlatform(platform);
			customer.setIsActive(isActive);
			modelService.save(customer);
			result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);

		}
		catch (final ModelNotFoundException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B9061);
		}

		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B9060);
		}

		return result;

	}



}
