/**
 *
 */
package com.tisl.mpl.search.converters.populator;


import de.hybris.platform.acceleratorservices.search.comparators.SizeAttributeComparator;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.lux.facade.CommonUtils;


/**
 * This class populates data into product data from solr search results
 *
 * @author 360641
 *
 */
public class MplSearchResultProductPopulator extends MplSearchResultVariantProductPopulator
{


	private CommonUtils commonUtils;

	@Autowired
	private SizeAttributeComparator sizeAttributeComparator;




	/**
	 * @return the commonUtils
	 */
	public CommonUtils getCommonUtils()
	{
		return commonUtils;
	}



	/**
	 * @param commonUtils
	 *           the commonUtils to set
	 */
	public void setCommonUtils(final CommonUtils commonUtils)
	{
		this.commonUtils = commonUtils;
	}

	//private static final String DELIMETER = ":";
	//private static final String STOCK = "STOCK";
	private static final String DELIMETER = ":";

	@Override
	public void populate(final SearchResultValueData source, final ProductData target)
	{
		super.populate(source, target);
		//System.out.println("testing...........###############");
		populateStockDetails(source, target);
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
				//to set the length instead of size for the length category product in serp/plp
				final List<String> displaySizeFinal = displaySize;
				if (displaySize.contains("Length"))
				{
					target.setDisplayLength("Length");
					displaySizeFinal.remove("Length");
				}
				else if (displaySize.contains("Weight"))
				{
					target.setDisplayLength("Weight");
					displaySizeFinal.remove("Weight");
				}
				else if (displaySize.contains("Volume"))
				{
					target.setDisplayLength("Volume");
					displaySizeFinal.remove("Volume");
				}
				else
				{
					target.setDisplayLength("Size");
				}
				Collections.sort(displaySizeFinal, sizeAttributeComparator);
				target.setDisplaySize(displaySizeFinal);
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
				/* TPR-250 changes */
				poulateUssidData(source, target);
				/* TPR-250 changes */
				//target.setUssID(this.<String> getValue(source, "ussID"));
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

			/* TPR-1886 | JEWELLERY START */
			if (getValue(source, "priceRangeJewellery") != null)
			{
				final String priceRangeJewellery = (String) getValue(source, "priceRangeJewellery");
				target.setPriceRangeJewellery(priceRangeJewellery);
			}


			/* JEWELLERY END */
			if (getValue(source, "isOffersExisting") != null)
			{


				//if (this.<Boolean> getValue(source, "isOffersExisting").booleanValue() == false) Sonar critical fixes
				if (!this.<Boolean> getValue(source, "isOffersExisting").booleanValue())
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
		final Double mobilePriceValue = this.<Double> getValue(source, "mobileprice");
		if (mobilePriceValue != null)
		{
			final PriceData priceData = getPriceDataFactory().create(PriceDataType.BUY,
					BigDecimal.valueOf(mobilePriceValue.doubleValue()), getCommonI18NService().getCurrentCurrency());
			target.setMobileprice(priceData);
		}

		// Pull the special price value for the current currency
		final Double mrpPriceValue = this.<Double> getValue(source, "mrpPriceValue");

		if (mrpPriceValue != null)
		{
			final PriceData priceData = getPriceDataFactory().create(PriceDataType.BUY,
					BigDecimal.valueOf(mrpPriceValue.doubleValue()), getCommonI18NService().getCurrentCurrency());
			target.setProductMRP(priceData);
		}

		// TISCR-405: set the savings for the current currency

		if (null != mrpPriceValue && null != priceValue)
		{
			final double savingsAmt = mrpPriceValue.doubleValue() - priceValue.doubleValue();
			final double calculatedPerSavings = (savingsAmt / mrpPriceValue.doubleValue()) * 100;
			//final double roundedOffValuebefore = Math.round(calculatedPerSavings * 100.0) / 100.0;
			final double floorValue = Math.floor((calculatedPerSavings * 100.0) / 100.0);
			final PriceData priceData = getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf((int) floorValue),
					getCommonI18NService().getCurrentCurrency());

			target.setSavingsOnProduct(priceData);

		}


	}

	//@Override
	//CAR-257
	protected void populateStockDetails(final SearchResultValueData source, final ProductData target)
	{
		if (getValue(source, "stockLevelStatus") != null)
		{
			//final boolean stockLevelStatus = Boolean.parseBoolean(getValue(source, "stockLevelStatus").toString());

			//final boolean stockLevelStatus = Boolean.valueOf(getValue(source, "stockLevelStatus").toString()).booleanValue();
			//final boolean stockLevelStatus = Boolean.getBoolean(getValue(source, "stockLevelStatus").toString()); //Sonar fix
			final boolean stockLevelStatus = Boolean.parseBoolean(getValue(source, "stockLevelStatus").toString()); //modified Boolean.getBoolean() as it always return false
			try
			{
				// In case of low stock then make a call to the stock service to determine if in or out of stock.
				// In this case (low stock) it is ok to load the product from the DB and do the real stock check
				//CAR-257
				//final ProductModel productModel = getProductService().getProductForCode(target.getCode());
				//	if (productModel != null)
				//{
				target.setStockValue(stockLevelStatus);
				//}
			}
			catch (final UnknownIdentifierException ex)
			{
				// If the product is no longer visible to the customergroup then this exception can be thrown

				// We can't remove the product from the results, but we can mark it as out of stock
				target.setStockValue(stockLevelStatus);
			}
		}

	}




	@Override
	protected List<ImageData> createImageData(final SearchResultValueData source)
	{
		final List<ImageData> result = new ArrayList<ImageData>();
		//TPR-796
		if (commonUtils.isLuxurySite() || getValue(source, "isLuxuryProduct") != null
				&& this.<Boolean> getValue(source, "isLuxuryProduct").booleanValue())
		{
			addImageData(source, "luxurySearchPage", result);
			addImageData(source, "luxuryModel", result);
			addImageData(source, "luxurySecondary", result);
		}
		else
		{
			addImageData(source, "searchPage", result);

		}
		addImageData(source, "product", result);

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
	/**
	 * TPR-250
	 *
	 * @param source
	 * @param target
	 */
	private void poulateUssidData(final SearchResultValueData source, final ProductData target)
	{
		final Map<String, String> ussidMap = new HashMap<String, String>();
		final Map<String, PriceData> priceMap = new HashMap<String, PriceData>();
		final Map<String, PriceData> mrpMap = new HashMap<String, PriceData>();
		final Map<String, PriceData> saveMap = new HashMap<String, PriceData>();
		final List<String> ussidList = (List<String>) getValue(source, "ussID");
		//CKD:PRDI-350:Start
		final Map<String, Integer> availabilityMap = new HashMap<String, Integer>();
		if (CollectionUtils.isNotEmpty(ussidList) && ussidList.get(0).contains(":"))
		{
			final String[] value = ussidList.get(0).split(DELIMETER);
			final String ussid = value[0];
			final String mrp = value[1];
			final String price = value[2];
			target.setUssID(ussid);
			final PriceData mrpPrice = getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf(Double.parseDouble(mrp)),
					getCommonI18NService().getCurrentCurrency());//SONAR FIX
			final PriceData mopPrice = getPriceDataFactory().create(PriceDataType.BUY,
					BigDecimal.valueOf(Double.parseDouble(price)), getCommonI18NService().getCurrentCurrency());//SONAR FIX
			final PriceData savings = getSavingsValue(Double.valueOf(mrp), Double.valueOf(price));
			priceMap.put(ussid.substring(0, 6), mrpPrice);
			mrpMap.put(ussid.substring(0, 6), mopPrice);
			saveMap.put(ussid.substring(0, 6), savings);


		}
		for (final String ussid : ussidList)
		{
			if (ussid.contains(":"))
			{
				final String[] value = ussid.split(DELIMETER);
				final String ussidVal = value[0];
				final String mrp = value[1];
				final String price = value[2];
				//CKD:PRDI-350
				Integer sellerStock = null;
				if (value.length > 3 && null != value[3] && StringUtils.isNotEmpty(value[3].trim()))
				{
					sellerStock = Integer.valueOf(value[3]);
				}
				final PriceData mrpVal = getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf(Double.parseDouble(mrp)),
						getCommonI18NService().getCurrentCurrency());//SONAR FIX
				final PriceData mopVal = getPriceDataFactory().create(PriceDataType.BUY,
						BigDecimal.valueOf(Double.parseDouble(price)), getCommonI18NService().getCurrentCurrency());//SONAR FIX
				final PriceData savVal = getSavingsValue(Double.valueOf(mrp), Double.valueOf(price));
				ussidMap.put(ussidVal.substring(0, 6), ussidVal);
				priceMap.put(ussid.substring(0, 6), mrpVal);
				mrpMap.put(ussid.substring(0, 6), mopVal);
				saveMap.put(ussid.substring(0, 6), savVal);
				//CKD:PRDI-350
				availabilityMap.put(ussid.substring(0, 6), sellerStock);
			}
		}
		target.setUssidList(ussidMap);
		target.setMrpMap(mrpMap);
		target.setPriceMap(priceMap);
		target.setSavingsMap(saveMap);
		//CKD:PRDI-350
		target.setAvailabilityMap(availabilityMap);
	}

	/**
	 * tpr-250 get saving data value
	 *
	 * @param mrp
	 * @param mop
	 * @return priceData
	 */
	private PriceData getSavingsValue(final Double mrp, final Double mop)
	{
		final double savingsAmt = mrp.doubleValue() - mop.doubleValue();
		final double calculatedPerSavings = (savingsAmt / mrp.doubleValue()) * 100;
		//final double roundedOffValuebefore = Math.round(calculatedPerSavings * 100.0) / 100.0;
		final double floorValue = Math.floor((calculatedPerSavings * 100.0) / 100.0);
		final PriceData priceData = getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf((int) floorValue),
				getCommonI18NService().getCurrentCurrency());
		return priceData;
	}

}
