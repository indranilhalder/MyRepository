/**
 * @author TCS
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.log4j.Logger;

import com.tisl.mpl.core.model.SizeGuideModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.data.SizeGuideData;



/**
 *
 * @author TCS
 *
 * @param <SOURCE>
 * @param <TARGET>
 */
public class SizeGuidePopulator<SOURCE extends SizeGuideModel, TARGET extends SizeGuideData> implements Populator<SOURCE, TARGET>
{

	protected static final Logger LOG = Logger.getLogger(SizeGuidePopulator.class);

	/**
	 * @description method is to populate basic SizeGuideData details in pdp
	 * @param sizeGuideModel
	 * @param sizeGuideData
	 * @throws ConversionException
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public void populate(final SOURCE sizeGuideModel, final TARGET sizeGuideData) throws ConversionException,
			EtailNonBusinessExceptions
	{
		//LOG.info("**************************SizeGuide*******************************************");

		if (null != sizeGuideModel)
		{
			//	sizeGuideData.setDimension(sizeGuideModel.getSizeGuideId());
			sizeGuideData.setDimension(sizeGuideModel.getDimension());
			sizeGuideData.setDimensionValue(sizeGuideModel.getValue());
			sizeGuideData.setImageURL(sizeGuideModel.getImageUrl());
			sizeGuideData.setDimensionUnit(sizeGuideModel.getUnit());
			sizeGuideData.setDimensionSize(sizeGuideModel.getSize());
			sizeGuideData.setEuroSize(sizeGuideModel.getEuroSize());
			sizeGuideData.setUsSize(sizeGuideModel.getUsSize());
		}

	}



}
