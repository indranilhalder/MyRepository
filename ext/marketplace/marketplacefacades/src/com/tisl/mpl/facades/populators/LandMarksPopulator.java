/**
 * 
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;

import com.tisl.mpl.core.model.LandMarksModel;
import com.tisl.mpl.facade.data.LandMarksData;

/**
 * @author Dileep
 *
 */
public class LandMarksPopulator implements Populator<LandMarksModel, LandMarksData>
{

	/**
	 * @author Techouts
	 * @Description Populating LandMarks Model to LandMarks Data
	 */
	@Override
	public void populate(LandMarksModel source, LandMarksData target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setLandmark(source.getLandmark());
		target.setLandmarksCode(source.getLandmarksCode());
		target.setIsActive(source.getIsActive());
	}

}
