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
import com.tisl.mpl.facades.cms.data.ComponentData;
import com.tisl.mpl.model.cms.components.MobileAppComponentModel;


/**
 * @author 584443
 *
 */
public class DefaultMobileAppComponentPopulator implements Populator<MobileAppComponentModel, ComponentData>
{

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final MobileAppComponentModel source, final ComponentData target) throws ConversionException
	{
		target.setImage(source.getImage() != null ? source.getImage().getURL() : null);
		target.setDescription(source.getImageText());
		target.setAdditionalDescription(source.getImageText2());
		target.setLink(source.getLinkText());
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
				if (null != category.getThumbnail())
				{
					target.setCategoryImage(category.getThumbnail().getURL());
					target.setImageTitle(category.getName());
				}
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
		target.setYoutubeUrl(source.getYoutubeUrl());
		target.setSubTitle(source.getImageText2());
		target.setLogoImage(source.getLogoImage() != null ? source.getLogoImage().getURL() : null);
		/*
		 * if (null != source.getTargetCategory() && null != source.getTargetCategory().getThumbnail()) {
		 * target.setCategoryImage(source.getTargetCategory().getThumbnail().getURL());
		 * target.setImageTitle(source.getTargetCategory().getName()); }
		 */
	}

}

