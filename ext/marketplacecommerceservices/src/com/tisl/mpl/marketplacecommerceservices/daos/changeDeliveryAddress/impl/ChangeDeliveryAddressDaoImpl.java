/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress.impl;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.TemproryAddressModel;
import com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress.ChangeDeliveryAddressDao;


/**
 * @author pankajk
 *
 */
public class ChangeDeliveryAddressDaoImpl implements ChangeDeliveryAddressDao
{
	private static final Logger LOG = Logger.getLogger(ChangeDeliveryAddressDaoImpl.class);
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private ModelService modelService;


	@Override
	public String saveAsTemproryAddressForCustomerDao(final String orderCode, final AddressData addressData)
	{
		String status;
		try
		{
			status = "fail";
			if (StringUtils.isNotEmpty(orderCode) && addressData != null)
			{
				final TemproryAddressModel temproryAddressModel = new TemproryAddressModel();
				temproryAddressModel.setOrderId(orderCode);
				temproryAddressModel.setFirstname(addressData.getFirstName());
				temproryAddressModel.setLastname(addressData.getLastName());
				temproryAddressModel.setAddressType(addressData.getAddressType());
				temproryAddressModel.setLine1(addressData.getLine1());
				temproryAddressModel.setLine2(addressData.getLine2());
				temproryAddressModel.setLandmark(addressData.getLandmark());
				temproryAddressModel.setPhone1(addressData.getPhone());
				temproryAddressModel.setPostalcode(addressData.getPostalCode());
				temproryAddressModel.setCity(addressData.getCity());
				temproryAddressModel.setState(addressData.getState());
				modelService.save(temproryAddressModel);
				status = "sucess";
			}
		}
		catch (final NullPointerException nullPointerException)
		{
			LOG.debug("Null Pointer Exception ");
			status = "fail";

			return status;
		}
		return status;
	}
}
