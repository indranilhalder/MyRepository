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
import com.tisl.mpl.model.cms.components.SmallBrandMobileAppComponentModel;


/**
 * @author 584443
 *
 */
public class DefaultMobileSubBrandComponentPopulator implements Populator<SmallBrandMobileAppComponentModel, ComponentData>
{

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final SmallBrandMobileAppComponentModel source, final ComponentData target) throws ConversionException
	{
		// YTODO Auto-generated method stub
		target.setLeftImage(source.getImage1() != null ? source.getImage1().getURL() : null);
		target.setRightImage(source.getImage2() != null ? source.getImage2().getURL() : null);
		target.setDescription(source.getDescription());
		target.setLink(source.getLinkText());
		//target.setTargetCode(source.getTargetCategory() != null ? source.getTargetCategory().getCode() : null);
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
		target.setTitle(source.getSubTitle());
	}

}
