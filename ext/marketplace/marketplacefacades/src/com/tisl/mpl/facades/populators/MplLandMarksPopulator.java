/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.LandMarksModel;
import com.tisl.mpl.facade.data.LandMarksData;


/**
 * @author Dileep
 *
 */
public class MplLandMarksPopulator implements Populator<LandMarksModel, LandMarksData>
{

	private static final Logger LOG = Logger.getLogger(MplLandMarksPopulator.class);
	
	/**
	 * @Description Populating LandMarks Model to LandMarks Data
	 * @param source
	 * @param target
	 */
	@Override
	public void populate(final LandMarksModel source, final LandMarksData target) throws ConversionException
	{
		Assert.notNull(source, MarketplacecommerceservicesConstants.SOURCENOTNULL);
		Assert.notNull(target, MarketplacecommerceservicesConstants.TARGETNOTNULL);
		LOG.debug("Populating LandMarksData from LandMarksModel");	
		target.setLandmark(source.getLandmark());
		target.setLandmarksCode(source.getLandmarksCode());
		target.setIsActive(source.getIsActive());
	}

}
