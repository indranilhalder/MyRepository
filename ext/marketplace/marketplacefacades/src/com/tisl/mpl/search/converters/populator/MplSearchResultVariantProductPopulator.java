/**
 *
 */
package com.tisl.mpl.search.converters.populator;

import de.hybris.platform.acceleratorfacades.order.data.PriceRangeData;
import de.hybris.platform.b2bacceleratorfacades.product.data.SolrFirstVariantCategoryEntryData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.converters.populator.SearchResultVariantProductPopulator;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.entity.SolrPriceRange;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.entity.SolrPriceRangeEntry;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl.SolrFirstVariantCategoryManager;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * @author TCS
 *
 */
public class MplSearchResultVariantProductPopulator extends SearchResultVariantProductPopulator
{
	public static final String MULTIDIMENSIONAL = "multidimensional";
	public static final String PRICE_RANGE = "priceRange";
	public static final String FIRST_CATEGORY_NAME_LIST = "firstCategoryNameList";

	private SolrFirstVariantCategoryManager categoryManager;

	@Override
	public void populate(final SearchResultValueData source, final ProductData target)
	{
		//super.populate(source, target);
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		// Pull the values directly from the SearchResult object
		target.setCode(this.<String> getValue(source, "code"));
		target.setName(this.<String> getValue(source, "name"));
		target.setManufacturer(this.<String> getValue(source, "manufacturerName"));
		target.setDescription(this.<String> getValue(source, "description"));
		target.setSummary(this.<String> getValue(source, "summary"));
		target.setAverageRating(this.<Double> getValue(source, "averageRating"));
		target.setNumberOfReviews(this.<Integer> getValue(source, "noOfReviews"));

		populatePrices(source, target);

		// Populate product's classification features
		getProductFeatureListPopulator().populate(getFeaturesList(source), target);

		final List<ImageData> images = createImageData(source);
		if (CollectionUtils.isNotEmpty(images))
		{
			target.setImages(images);
		}

		populateUrl(source, target);
		populatePromotions(source, target);
		//	populateStock(source, target);
		if (source.getValues() != null)
		{
			target.setMultidimensional((Boolean) source.getValues().get(MULTIDIMENSIONAL));
			setPriceRange(source, target);
			setFirstCategoryNameList(source, target);
		}
	}


	@Override
	protected void setPriceRange(final SearchResultValueData source, final ProductData target)
	{
		final PriceRangeData priceRange = new PriceRangeData();
		final String priceRangeValue = (String) source.getValues().get(PRICE_RANGE);
		if (StringUtils.isNotEmpty(priceRangeValue))
		{
			final SolrPriceRange solrPriceRange = SolrPriceRange.buildSolrPriceRangePairFromProperty(priceRangeValue);
			if (solrPriceRange != null)
			{
				priceRange.setMinPrice(createPriceData(solrPriceRange.getLower()));
				priceRange.setMaxPrice(createPriceData(solrPriceRange.getHigher()));
			}
		}

		target.setPriceRange(priceRange);
	}

	@Override
	protected PriceData createPriceData(final SolrPriceRangeEntry priceInfo)
	{
		return getPriceDataFactory().create(PriceDataType.FROM, priceInfo.getValue(), priceInfo.getCurrencyIso());
	}

	/**
	 * Set list of first categories for the {@link ProductData}.
	 *
	 * @param source
	 *           The {@link SearchResultValueData} containing the priceRange.
	 * @param target
	 *           The {@link ProductData} to be modified.
	 */
	@Override
	protected void setFirstCategoryNameList(final SearchResultValueData source, final ProductData target)
	{
		final String categoryListSolr = (String) source.getValues().get(FIRST_CATEGORY_NAME_LIST);
		if (StringUtils.isNotEmpty(categoryListSolr))
		{
			final List<SolrFirstVariantCategoryEntryData> variantCategoryList = categoryManager
					.buildFirstVariantCategoryListFromSolrProperty(categoryListSolr);
			target.setFirstCategoryNameList(variantCategoryList);
		}
	}

	@Override
	@Required
	public void setCategoryManager(final SolrFirstVariantCategoryManager categoryManager)
	{
		this.categoryManager = categoryManager;
	}

}
