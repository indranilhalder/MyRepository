/**
 * @author TCS
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.log4j.Logger;

import com.tisl.mpl.core.model.ExchangeCouponValueModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.data.ExchangeGuideData;



/**
 *
 * @author TCS
 *
 * @param <SOURCE>
 * @param <TARGET>
 */
public class ExchangeGuidePopulator<SOURCE extends ExchangeCouponValueModel, TARGET extends ExchangeGuideData> implements
		Populator<SOURCE, TARGET>
{

	protected static final Logger LOG = Logger.getLogger(ExchangeGuidePopulator.class);
	private static final String WORKING = "Working";
	private static final String NON_WORKING = "Working";


	/**
	 * @description method is to populate basic ExchangeGuideData details in pdp
	 * @param exchangeCouponValueModel
	 * @param exchangeGuideData
	 * @throws ConversionException
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public void populate(final SOURCE exchangeCouponValueModel, final TARGET exchangeGuideData) throws ConversionException,
			EtailNonBusinessExceptions
	{


		if (null != exchangeCouponValueModel)
		{
			exchangeGuideData.setL3category(exchangeCouponValueModel.getThirdLevelCategory().getName());
			if (exchangeCouponValueModel.isIsWorking())
			{
				exchangeGuideData.setIsWorking(WORKING);
			}
			else
			{
				exchangeGuideData.setIsWorking(NON_WORKING);
			}

			exchangeGuideData.setL4category(exchangeCouponValueModel.getL4categoryName());
			exchangeGuideData.setPrice(exchangeCouponValueModel.getCouponValue().toString());

		}

	}


}