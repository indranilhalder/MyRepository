/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.TemproryAddressModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress.MplChangeDeliveryAddressDao;


/**
 * @author pankajk
 *
 */
public class MplChangeDeliveryAddressDaoImpl implements MplChangeDeliveryAddressDao
{
	private static final Logger LOG = Logger.getLogger(MplChangeDeliveryAddressDaoImpl.class);
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/***
	 * OrderId Based On We will get TemproryAddressModel
	 * 
	 * @param orderCode
	 * @return TemproryAddressModel
	 */
	@Override
	public TemproryAddressModel geTemproryAddressModel(String orderCode) throws EtailNonBusinessExceptions
	{
		TemproryAddressModel temproryAddressModel = new TemproryAddressModel();

		try
		{
			if (StringUtils.isNotEmpty(orderCode))
			{
				LOG.info(":Based on orderId get TemproryAddressModel");
				temproryAddressModel.setOrderId(orderCode);
				temproryAddressModel = flexibleSearchService.getModelByExample(temproryAddressModel);
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e);
			
		}
		return temproryAddressModel;
	}


}
