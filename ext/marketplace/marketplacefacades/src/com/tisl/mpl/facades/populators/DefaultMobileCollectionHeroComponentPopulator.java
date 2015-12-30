/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.tisl.mpl.facades.cms.data.CollectionHeroComponentData;
import com.tisl.mpl.model.cms.components.MobileAppCollectionHeroComponentModel;


/**
 * @author 584443
 *
 */
public class DefaultMobileCollectionHeroComponentPopulator
		implements Populator<MobileAppCollectionHeroComponentModel, CollectionHeroComponentData>
{

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final MobileAppCollectionHeroComponentModel source, final CollectionHeroComponentData target)
			throws ConversionException
	{
		target.setBackgroundImage(source.getImage() != null ? source.getImage().getURL() : null);
		target.setDescription1(source.getDescription1());
		target.setDescription2(source.getDescription2());
		target.setDescription3(source.getDescription3());
		target.setSubTitle(source.getSubTitle());
		target.setTitle(source.getTitle());
		target.setLinkText(source.getLinkText());

	}

}
