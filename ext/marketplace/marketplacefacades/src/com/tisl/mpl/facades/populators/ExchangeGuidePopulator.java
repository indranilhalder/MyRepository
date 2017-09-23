/**
 * @author TCS
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.commons.lang.StringUtils;
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
	private static final String NON_WORKING = "Non-Working";


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
			//EQA Comments Incorp
			final CategoryModel thirdCategory = exchangeCouponValueModel.getThirdLevelCategory();
			if (thirdCategory != null && StringUtils.isNotEmpty(thirdCategory.getName()))
			{
				exchangeGuideData.setL3category(thirdCategory.getName());
			}

			if (exchangeCouponValueModel.isIsWorking())
			{
				exchangeGuideData.setIsWorking(WORKING);
			}
			else
			{
				exchangeGuideData.setIsWorking(NON_WORKING);
			}
			//EQA Comments Incorp
			final String couponValue = exchangeCouponValueModel.getL4categoryName();
			if (StringUtils.isNotEmpty(couponValue))
			{
				exchangeGuideData.setL4category(couponValue);
			}
			//EQA Comments Incorp
			final String price = exchangeCouponValueModel.getCouponValue().toString();
			if (StringUtils.isNotEmpty(price))
			{
				exchangeGuideData.setPrice(price);
			}

		}

	}

}