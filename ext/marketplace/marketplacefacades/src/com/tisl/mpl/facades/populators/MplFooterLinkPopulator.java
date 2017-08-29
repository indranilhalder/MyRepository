/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;

import com.tisl.mpl.core.model.MplFooterLinkModel;
import com.tisl.mpl.facades.cms.data.FooterLinkData;


/**
 * @author TCS
 *
 */
public class MplFooterLinkPopulator implements Populator<MplFooterLinkModel, FooterLinkData>
{

	/*
	 * TPR-5733 (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final MplFooterLinkModel source, final FooterLinkData target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		if (source.getFooterLinkRow() != null)
		{
			target.setFooterLinkRow(source.getFooterLinkRow());
		}
		if (source.getFooterLinkColumn() != null)
		{
			target.setFooterLinkColumn(source.getFooterLinkColumn());
		}
		if (source.getFooterLinkName() != null)
		{
			target.setFooterLinkName(source.getFooterLinkName());
		}
		if (source.getFooterLinkURL() != null)
		{
			target.setFooterLinkURL(source.getFooterLinkURL());
		}
	}
}
