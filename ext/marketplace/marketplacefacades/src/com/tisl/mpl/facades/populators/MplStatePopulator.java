/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.facades.product.data.StateData;
import com.tisl.mpl.model.StateModel;


/**
 * @author Dileep
 *
 */
public class MplStatePopulator implements Populator<StateModel, StateData>
{
	
	private static final Logger LOG = Logger.getLogger(MplStatePopulator.class);

	/**
	 * @Description Populating state Model to state Data
	 * @param source
	 * @param target
	 */
	@Override
	public void populate(final StateModel source, final StateData target) throws ConversionException
	{
		Assert.notNull(source, MarketplacecommerceservicesConstants.SOURCENOTNULL);
		Assert.notNull(target, MarketplacecommerceservicesConstants.TARGETNOTNULL);
		LOG.debug("Populating StateData from StateModel");
		target.setCode(source.getRegion());
		target.setCountryKey(source.getCountrykey());
		target.setName(source.getDescription());

	}

}
