/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.commercefacades.product.converters.populator.BaseOptionDataPopulator;
import de.hybris.platform.commercefacades.product.data.BaseOptionData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantProductModel;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;


/**
 * @author TCS
 *
 */
public class MplBaseOptionDataPopulator extends BaseOptionDataPopulator
{

	protected static final Logger LOG = Logger.getLogger(MplBaseOptionDataPopulator.class);

	@Override
	public void populate(final VariantProductModel source, final BaseOptionData target)
	{
		LOG.debug("Populating BaseOptionData from VariantProductModel by MplBaseOptionDataPopulator");

		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		final ProductModel baseProduct = source.getBaseProduct();

		if (null != baseProduct.getVariantType())
		{
			target.setVariantType(baseProduct.getVariantType().getCode());
		}

		target.setSelected(getVariantOptionDataConverter().convert(source));
	}
}
