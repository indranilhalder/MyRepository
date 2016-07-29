/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplDeliveryInformationDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryInformationService;


/**
 * @author TCS
 *
 */
public class MplDeliveryInformationServiceImpl implements MplDeliveryInformationService
{
	@Autowired
	private MplDeliveryInformationDao mplDeliveryInformationDao;


	@Override
	public List<DeliveryModeModel> getDeliveryInformation(final List<String> code) throws EtailNonBusinessExceptions
	{
		final List<DeliveryModeModel> deliveryInformation = mplDeliveryInformationDao.getDeliveryInformation(code);

		return deliveryInformation;
	}


	/**
	 * @return the mplDeliveryInformationDao
	 */
	public MplDeliveryInformationDao getMplDeliveryInformationDao()
	{
		return mplDeliveryInformationDao;
	}


	/**
	 * @param mplDeliveryInformationDao
	 *           the mplDeliveryInformationDao to set
	 */
	public void setMplDeliveryInformationDao(final MplDeliveryInformationDao mplDeliveryInformationDao)
	{
		this.mplDeliveryInformationDao = mplDeliveryInformationDao;
	}
}
