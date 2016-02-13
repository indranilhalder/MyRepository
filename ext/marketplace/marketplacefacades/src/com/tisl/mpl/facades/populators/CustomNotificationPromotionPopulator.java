/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.log4j.Logger;

import com.tisl.mpl.data.NotificationData;


/**
 * @author TCS
 *
 */
public class CustomNotificationPromotionPopulator<SOURCE extends AbstractPromotionModel, TARGET extends NotificationData>
		implements Populator<SOURCE, TARGET>
{



	protected static final Logger LOG = Logger.getLogger(CustomNotificationPromotionPopulator.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final SOURCE AbstractPromotionModel, final TARGET notificationData) throws ConversionException
	{
		if (null != AbstractPromotionModel)
		{
			notificationData.setPromotionIdentifier(AbstractPromotionModel.getCode());
			notificationData.setNotificationRead(Boolean.FALSE);
			notificationData.setNotificationCreationDate(AbstractPromotionModel.getStartDate());
			notificationData.setPromotionDescription(AbstractPromotionModel.getDescription());
			notificationData.setNotificationCustomerStatus("Promotion @ is available");




		}

	}


}