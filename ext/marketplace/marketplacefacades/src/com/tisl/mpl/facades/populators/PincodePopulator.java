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
public class PincodePopulator implements Populator<PincodeModel, PincodeData>
{
	private Converter<StateModel, StateData> stateConverter;
	private Converter<LandMarksModel, LandMarksData> landMarksConverter;

	/**
	 * @author Techouts
	 * @description Based on pincode getting all the city, state,
	 */
	@Override
	public void populate(PincodeModel source, PincodeData target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setPincode(source.getPincode());
		target.setCityName(source.getCityName());
		if (null != source.getState())
		{
			target.setCountryCode(source.getState().getCountrykey());
			target.setState(getStateConverter().convert(source.getState()));
		}
		if (null != source.getLandmarks() && !source.getLandmarks().isEmpty())
		{
			List<LandMarksModel> allLandmarks=source.getLandmarks();
			List<LandMarksModel> activeLandmarks=new ArrayList<LandMarksModel>();
			for(LandMarksModel l:allLandmarks){
				if(l.getIsActive().booleanValue()){
					activeLandmarks.add(l);
				}
			}
			target.setLandMarks(Converters.convertAll(activeLandmarks, getLandMarksConverter()));
		}
	}

	/**
	 * @return the stateConverter
	 */
	public Converter<StateModel, StateData> getStateConverter()
	{
		return stateConverter;
	}

	/**
	 * @param stateConverter
	 *           the stateConverter to set
	 */
	public void setStateConverter(Converter<StateModel, StateData> stateConverter)
	{
		this.stateConverter = stateConverter;
	}

	/**
	 * @return the landMarksConverter
	 */
	public Converter<LandMarksModel, LandMarksData> getLandMarksConverter()
	{
		return landMarksConverter;
	}

	/**
	 * @param landMarksConverter
	 *           the landMarksConverter to set
	 */
	public void setLandMarksConverter(Converter<LandMarksModel, LandMarksData> landMarksConverter)
	{
		this.landMarksConverter = landMarksConverter;
	}
}
