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
public class MplLandMarksPopulator implements Populator<LandMarksModel, LandMarksData>
{

	/**
	 * @Description Populating LandMarks Model to LandMarks Data
	 * @param source
	 * @param target
	 */
	@Override
	public void populate(final LandMarksModel source, final LandMarksData target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setLandmark(source.getLandmark());
		target.setLandmarksCode(source.getLandmarksCode());
		target.setIsActive(source.getIsActive());
	}

}
