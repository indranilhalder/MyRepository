/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Collection;

import com.tisl.mpl.core.model.MplShopByLookModel;
import com.tisl.mpl.facades.cms.data.HeroComponentData;
import com.tisl.mpl.model.cms.components.MobileAppHeroComponentModel;


/**
 * @author 584443
 *
 */
public class DefaultMobileHeroComponentPopulator implements Populator<MobileAppHeroComponentModel, HeroComponentData>
{

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final MobileAppHeroComponentModel source, final HeroComponentData target) throws ConversionException
	{

		target.setBackgroundImage(source.getImage() != null ? source.getImage().getURL() : null);
		target.setDescription(source.getDescription());
		target.setLogoImage(source.getLogo() != null ? source.getLogo().getURL() : null);
		target.setTitle(source.getTitle());
		//target.setTargetCode(source.getTargetCategory().getCode());
		/*
		 * if (source.getTargetCategory() != null) { target.setTargetCode(source.getTargetCategory().getCode());
		 * target.setTargetType("Category"); } else if (source.getTargetProduct() != null) {
		 * target.setTargetCode(source.getTargetProduct().getCode()); target.setTargetType("Product"); } else if
		 * (source.getTargetCollection() != null) { target.setTargetCode(source.getTargetCollection().getCollectionId());
		 * target.setTargetType("Collection"); }
		 */
		if (source.getAssociatedCategory() != null && !source.getAssociatedCategory().isEmpty())
		{
			final Collection<CategoryModel> categoryCollection = source.getAssociatedCategory();
			for (final CategoryModel category : categoryCollection)
			{
				target.setTargetCode(category.getCode());
				target.setTargetType("Category");

			}
		}

		else if (source.getAssociatedProduct() != null && !source.getAssociatedProduct().isEmpty())
		{
			final Collection<ProductModel> productCollection = source.getAssociatedProduct();
			for (final ProductModel product : productCollection)
			{
				target.setTargetCode(product.getCode());
				target.setTargetType("Product");
			}
		}
		else if (source.getAssociatedCollection() != null && !source.getAssociatedCollection().isEmpty())
		{
			final Collection<MplShopByLookModel> shopByLookCollection = source.getAssociatedCollection();
			for (final MplShopByLookModel shopByLook : shopByLookCollection)
			{
				target.setTargetCode(shopByLook.getCollectionId());
				target.setTargetType("Collection");
			}
		}
		target.setLinkText(source.getLinkText());

	}

}
