/**
 *
 */
package com.tisl.mpl.facades.populators;


import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import com.tisl.mpl.core.model.LandMarksModel;
import com.tisl.mpl.facade.data.LandMarksData;
import com.tisl.mpl.facades.data.PincodeData;
import com.tisl.mpl.facades.product.data.StateData;
import com.tisl.mpl.model.StateModel;


/**
 * @author Dileep
 *
 */
public class MplPincodePopulator implements Populator<PincodeModel, PincodeData>
{

	private Converter<StateModel, StateData> mplStateConverter;
	private Converter<LandMarksModel, LandMarksData> mplLandMarksConverter;

	/**
	 * @description Based on pincode getting all the city, state,
	 * @param source
	 * @param target
	 */
	@Override
	public void populate(final PincodeModel source, final PincodeData target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setPincode(source.getPincode());
		target.setCityName(source.getCityName());
		if (null != source.getState())
		{
			target.setCountryCode(source.getState().getCountrykey());
			target.setState(getMplStateConverter().convert(source.getState()));
		}
		if (null != source.getLandmarks() && !source.getLandmarks().isEmpty())
		{
			final List<LandMarksModel> allLandmarks = source.getLandmarks();
			final List<LandMarksModel> activeLandmarks = new ArrayList<LandMarksModel>();
			for (final LandMarksModel l : allLandmarks)
			{
				if (l.getIsActive().booleanValue())
				{
					activeLandmarks.add(l);
				}
			}
			target.setLandMarks(Converters.convertAll(activeLandmarks, getMplLandMarksConverter()));
		}
	}

	/**
	 * @return the mplStateConverter
	 */
	public Converter<StateModel, StateData> getMplStateConverter()
	{
		return mplStateConverter;
	}

	/**
	 * @param mplStateConverter
	 *           the mplStateConverter to set
	 */
	public void setMplStateConverter(final Converter<StateModel, StateData> mplStateConverter)
	{
		this.mplStateConverter = mplStateConverter;
	}

	/**
	 * @return the mplLandMarksConverter
	 */
	public Converter<LandMarksModel, LandMarksData> getMplLandMarksConverter()
	{
		return mplLandMarksConverter;
	}

	/**
	 * @param mplLandMarksConverter
	 *           the mplLandMarksConverter to set
	 */
	public void setMplLandMarksConverter(final Converter<LandMarksModel, LandMarksData> mplLandMarksConverter)
	{
		this.mplLandMarksConverter = mplLandMarksConverter;
	}


}
