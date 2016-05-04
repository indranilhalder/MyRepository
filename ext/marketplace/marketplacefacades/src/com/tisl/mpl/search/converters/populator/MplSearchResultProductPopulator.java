/**
 *
 */
package com.tisl.mpl.search.converters.populator;


import de.hybris.platform.acceleratorservices.search.comparators.SizeAttributeComparator;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.converters.populator.SearchResultVariantProductPopulator;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl.SolrFirstVariantCategoryManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;


/**
 * This class populates data into product data from solr search results
 *
 * @author 360641
 *
 */
public class MplSearchResultProductPopulator extends SearchResultVariantProductPopulator
{

	@SuppressWarnings("unused")
	private SolrFirstVariantCategoryManager categoryManager;

	@Autowired
	private SizeAttributeComparator sizeAttributeComparator;

	@Override
	public void populate(final SearchResultValueData source, final ProductData target)
	{
		super.populate(source, target);
		if (source.getValues() != null)
		{
			populatePrices(source, target);

			if (getValue(source, "swatchColor") != null)
			{
				target.setSwatchColor((List<String>) getValue(source, "swatchColor"));
			}

			if (getValue(source, "displaySize") != null)
			{
				final List<String> displaySize = (List<String>) getValue(source, "displaySize");
				Collections.sort(displaySize, sizeAttributeComparator);
				target.setDisplaySize((List<String>) getValue(source, "displaySize"));
			}

			if (getValue(source, "mplAvgRating") != null)
			{
				target.setAverageRating(this.<Double> getValue(source, "mplAvgRating"));
			}

			if (getValue(source, "ratingCount") != null)
			{
				target.setRatingCount(this.<Integer> getValue(source, "ratingCount"));
			}

			if (getValue(source, "productCategoryType") != null)
			{
				target.setProductCategoryType(this.<String> getValue(source, "productCategoryType"));
			}

			if (getValue(source, "searchCapacity") != null)
			{
				target.setCapacity((List<String>) getValue(source, "searchCapacity"));
			}

			if (getValue(source, "inStockFlag") != null)
			{
				target.setInStockFlag(this.<Boolean> getValue(source, "inStockFlag"));
			}

			if (getValue(source, "isVariant") != null)
			{
				target.setIsVariant(this.<Boolean> getValue(source, "isVariant"));
			}

			if (getValue(source, "mobileBrandName") != null)
			{
				target.setMobileBrandName(this.<String> getValue(source, "mobileBrandName"));
			}

			if (getValue(source, "isProductNew") != null)
			{
				target.setIsProductNew(this.<Boolean> getValue(source, "isProductNew"));
			}
			/*
			 * if (getValue(source, "isOffersExisting") != null) { target.setIsOfferExisting(this.<Boolean>
			 * getValue(source, "isOffersExisting")); }
			 */
			if (getValue(source, "ussID") != null)
			{
				target.setUssID(this.<String> getValue(source, "ussID"));
			}
			if (getValue(source, "immediateCategory") != null)
			{
				target.setImmediateCategory(this.<String> getValue(source, "immediateCategory"));
			}
			if (getValue(source, "onlineExclusive") != null)
			{
				target.setIsOnlineExclusive(this.<Boolean> getValue(source, "onlineExclusive"));
			}
			if (getValue(source, "leastSizeProduct") != null)
			{
				target.setLeastSizeProduct(this.<String> getValue(source, "leastSizeProduct"));
			}
			if (getValue(source, "displayPrice") != null)
			{
				final List<String> displayPrice = (List<String>) getValue(source, "displayPrice");
				target.setDisplayPrice(displayPrice);
			}
			if (getValue(source, "displayUrl") != null)
			{
				final List<String> displayUrl = (List<String>) getValue(source, "displayUrl");
				target.setDisplayUrl(displayUrl);
			}
			if (getValue(source, "displayStock") != null)
			{
				final List<String> displayStock = (List<String>) getValue(source, "displayStock");
				target.setDisplayStock(displayStock);
			}

			if (getValue(source, "displayMrpPrice") != null)
			{

				final List<String> displayMrpPrice = (List<String>) getValue(source, "displayMrpPrice");
				target.setDisplayMrp(displayMrpPrice);
			}
			if (getValue(source, "displayPromotion") != null)
			{
				final List<String> displayPromotion = (List<String>) getValue(source, "displayPromotion");
				target.setDisplayPromotion(displayPromotion);
			}

			if (getValue(source, "allPromotions") != null)
			{

				final List<String> promotions = (List<String>) getValue(source, "allPromotions");

				if (CollectionUtils.isEmpty(promotions))
				{

					target.setIsOfferExisting(Boolean.FALSE);
				}
				else
				{

					target.setIsOfferExisting(Boolean.TRUE);
					/*
					 * TISPRD-216 :: This change has been made to change the url for actual product which has variant not the
					 * lowest size variant which may not have promotion
					 */
					final String url = this.<String> getValue(source, "url");
					if (StringUtils.isEmpty(url))
					{
						// Resolve the URL and set it on the product data
						target.setUrl(getProductDataUrlResolver().resolve(target));
					}
					else
					{
						target.setUrl(url);
					}

					//					String url = "";
					//					try
					//					{
					//						url = getProductDataUrlResolver().resolve(target);
					//					}
					//					catch (final UnknownIdentifierException e)
					//					{
					//						target.setUrl(url);
					//					}
				}
				/*
				 * if( { target.setLeastSizeProduct(this.<String> getValue(source, "allPromotions")); }
				 */
			}
		}
	}


	@Override
	protected void populatePrices(final SearchResultValueData source, final ProductData target)
	{
		// Pull the volume prices flag
		final Boolean volumePrices = this.<Boolean> getValue(source, "volumePrices");
		target.setVolumePricesFlag(volumePrices == null ? Boolean.FALSE : volumePrices);

		// Pull the price value for the current currency
		final Double priceValue = this.<Double> getValue(source, "priceValue");
		if (priceValue != null)
		{
			final PriceData priceData = getPriceDataFactory().create(PriceDataType.BUY,
					BigDecimal.valueOf(priceValue.doubleValue()), getCommonI18NService().getCurrentCurrency());
			target.setPrice(priceData);
		}

		// Pull the special price value for the current currency
		final Double mrpPriceValue = this.<Double> getValue(source, "mrpPriceValue");

		if (mrpPriceValue != null)
		{
			final PriceData priceData = getPriceDataFactory().create(PriceDataType.BUY,
					BigDecimal.valueOf(mrpPriceValue.doubleValue()), getCommonI18NService().getCurrentCurrency());
			target.setProductMRP(priceData);
		}

		// set the savings for the current currency

		if (null != mrpPriceValue && null != priceValue)
		{
			final PriceData priceData = getPriceDataFactory().create(PriceDataType.BUY,
					BigDecimal.valueOf(mrpPriceValue.doubleValue() - priceValue.doubleValue()),
					getCommonI18NService().getCurrentCurrency());
			target.setSavingsOnProduct(priceData);

		}


	}



	@Override
	protected List<ImageData> createImageData(final SearchResultValueData source)
	{
		final List<ImageData> result = new ArrayList<ImageData>();


		addImageData(source, "searchPage", result);


		return result;
	}


	/*
	 * @Override protected void addImageData(final SearchResultValueData source, final String imageFormat, final String
	 * mediaFormatQualifier, final ImageDataType type, final List<ImageData> images) {
	 *
	 * final Object imgObj = getValue(source, "img-" + mediaFormatQualifier); List<String> imgList = new ArrayList(); if
	 * (imgObj instanceof ArrayList) { imgList = (List) imgObj; } else { final String imgStr = (String) imgObj;
	 * imgList.add(imgStr); }
	 *
	 *
	 * if (!imgList.isEmpty()) { for (int i = 0; i < imgList.size(); i++) { final ImageData imageSearchData =
	 * createImageData(); imageSearchData.setImageType(type); imageSearchData.setFormat(imageFormat);
	 * imageSearchData.setUrl(imgList.get(i)); images.add(imageSearchData);
	 *
	 *
	 * }
	 *
	 *
	 * } }
	 */

	@Override
	@Required
	public void setCategoryManager(final SolrFirstVariantCategoryManager categoryManager)
	{
		this.categoryManager = categoryManager;
	}

}
