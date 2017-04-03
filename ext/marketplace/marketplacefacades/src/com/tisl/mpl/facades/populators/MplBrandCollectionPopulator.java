/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.component.data.BrandComponentData;
import de.hybris.platform.commercefacades.component.data.CMSSubbrandData;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.BrandComponentModel;
import com.tisl.mpl.core.model.CMSSubbrandModel;


/**
 * @author TCS
 *
 */
public class MplBrandCollectionPopulator implements Populator<BrandComponentModel, BrandComponentData>
{

	private Converter<CMSSubbrandModel, CMSSubbrandData> cmsSubBrandConvertor;
	private Converter<CategoryModel, CategoryData> categoryConverter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final BrandComponentModel source, final BrandComponentData target) throws ConversionException
	{
		Assert.notNull(source, MarketplacecommerceservicesConstants.SOURCENOTNULL);
		Assert.notNull(target, MarketplacecommerceservicesConstants.TARGETNOTNULL);
		target.setUid(source.getUid());

		if (null != source.getLayout())
		{
			target.setLayout(source.getLayout().getCode());
		}
		target.setMasterBrandName(source.getMasterBrandName());
		target.setMasterBrandURL(source.getMasterBrandURL());
		if (CollectionUtils.isNotEmpty(source.getSubBrandList()))
		{
			final List<CMSSubbrandData> subBrandDataList = new ArrayList<CMSSubbrandData>();
			for (final CMSSubbrandModel subBrandModel : source.getSubBrandList())
			{
				final CMSSubbrandData cmsSubBrandData = cmsSubBrandConvertor.convert(subBrandModel);
				subBrandDataList.add(cmsSubBrandData);
			}
			target.setSubBrandList(subBrandDataList);
		}
		if (CollectionUtils.isNotEmpty(source.getSubBrands()))
		{
			final List<CategoryData> categorydata = new ArrayList<CategoryData>();
			for (final CategoryModel categoryModel : source.getSubBrands())
			{
				categorydata.add(categoryConverter.convert(categoryModel));
			}
			target.setSubBrands(categorydata);
		}

	}

	/**
	 * @return the cmsSubBrandConvertor
	 */
	public Converter<CMSSubbrandModel, CMSSubbrandData> getCmsSubBrandConvertor()
	{
		return cmsSubBrandConvertor;
	}

	/**
	 * @param cmsSubBrandConvertor
	 *           the cmsSubBrandConvertor to set
	 */
	public void setCmsSubBrandConvertor(final Converter<CMSSubbrandModel, CMSSubbrandData> cmsSubBrandConvertor)
	{
		this.cmsSubBrandConvertor = cmsSubBrandConvertor;
	}

	/**
	 * @return the categoryConverter
	 */
	public Converter<CategoryModel, CategoryData> getCategoryConverter()
	{
		return categoryConverter;
	}

	/**
	 * @param categoryConverter
	 *           the categoryConverter to set
	 */
	public void setCategoryConverter(final Converter<CategoryModel, CategoryData> categoryConverter)
	{
		this.categoryConverter = categoryConverter;
	}


}
