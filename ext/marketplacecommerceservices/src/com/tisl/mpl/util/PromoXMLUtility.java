/**
 *
 */
package com.tisl.mpl.util;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.promotions.jalo.AbstractPromotionRestriction;
import de.hybris.platform.promotions.jalo.PromotionPriceRow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.jalo.BuyAPercentageDiscount;
import com.tisl.mpl.jalo.EtailSellerSpecificRestriction;
import com.tisl.mpl.jalo.ManufacturesRestriction;
import com.tisl.mpl.jalo.SellerInformation;
import com.tisl.mpl.pojo.PromotionXMLData;


/**
 * @author TCS
 *
 */
public class PromoXMLUtility
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PromoXMLUtility.class.getName());

	/**
	 * @Description : Populate ProductPromotion Data
	 * @param item
	 * @return promoData
	 */
	public PromotionXMLData getPromoXMLData(final Item item)
	{
		PromotionXMLData promoData = new PromotionXMLData();
		if (item instanceof BuyAPercentageDiscount)
		{
			promoData = getBuyAPercentageDiscountData(item);
		}

		return promoData;

	}

	/**
	 * @Description : Populate BuyAPercentageDiscount Data
	 * @param item
	 */
	private PromotionXMLData getBuyAPercentageDiscountData(final Item item)
	{
		final PromotionXMLData promoData = new PromotionXMLData();
		try
		{
			if (null != item.getAttribute("promotionType"))
			{
				promoData.setPromoType(item.getAttribute("promotionType").toString());
			}
			else
			{
				promoData.setPromoType(MarketplacecommerceservicesConstants.EMPTY);
			}


			if (null != item.getAttribute("code"))
			{
				promoData.setPromoCode(item.getAttribute("code").toString());
			}
			else
			{
				promoData.setPromoCode(MarketplacecommerceservicesConstants.EMPTY);
			}


			if (null != item.getAttribute("priority"))
			{
				promoData.setPriority(item.getAttribute("priority").toString());
			}
			else
			{
				promoData.setPriority(MarketplacecommerceservicesConstants.EMPTY);
			}


			if (null != item.getAttribute("startDate"))
			{
				promoData.setPromoStartDate(item.getAttribute("startDate").toString());
			}
			else
			{
				promoData.setPromoStartDate(MarketplacecommerceservicesConstants.EMPTY);
			}


			if (null != item.getAttribute("endDate"))
			{
				promoData.setPromoEndDate(item.getAttribute("endDate").toString());
			}
			else
			{
				promoData.setPromoEndDate(MarketplacecommerceservicesConstants.EMPTY);
			}


			if (null != item.getAttribute("description"))
			{
				promoData.setDescription(item.getAttribute("description").toString());
			}
			else
			{
				promoData.setDescription(MarketplacecommerceservicesConstants.EMPTY);
			}


			if (null != item.getAttribute("enabled"))
			{
				final String data = populateBooleanData(item.getAttribute("enabled"));
				promoData.setEnabled(data);
			}
			else
			{
				promoData.setEnabled(MarketplacecommerceservicesConstants.NO);
			}

			if (null != item.getAttribute("channel"))
			{
				promoData.setChannel(item.getAttribute("channel").toString());
			}
			else
			{
				promoData.setChannel(MarketplacecommerceservicesConstants.EMPTY);
			}


			if (null != item.getAttribute("products"))
			{
				final List<Product> productData = (List<Product>) item.getAttribute("products");
				final List<String> productList = new ArrayList<String>();
				if (null != productData && !productData.isEmpty())
				{
					for (final Product product : productData)
					{
						productList.add(product.getCode());
					}
					promoData.setPrimaryProduct(productList);
				}
			}
			else
			{
				final List<String> productList = new ArrayList<String>();
				promoData.setPrimaryProduct(productList);
			}

			if (null != item.getAttribute("categories"))
			{
				final List<Category> categoryData = (List<Category>) item.getAttribute("categories");
				final List<String> categoryList = new ArrayList<String>();
				if (null != categoryData && !categoryData.isEmpty())
				{
					for (final Category category : categoryData)
					{
						categoryList.add(category.getCode());
					}
					promoData.setPrimaryCategory(categoryList);
				}
			}
			else
			{
				final List<String> categoryList = new ArrayList<String>();
				promoData.setPrimaryCategory(categoryList);
			}

			if (null != item.getAttribute("percentageOrAmount"))
			{
				final String data = populateBooleanData(item.getAttribute("percentageOrAmount"));
				promoData.setIsPercentageDiscount(data);

				if (data.equalsIgnoreCase(MarketplacecommerceservicesConstants.YES)
						&& null != item.getAttribute("percentageDiscount"))
				{
					promoData.setDiscount(item.getAttribute("percentageDiscount").toString() + "%");
				}
				else if (null != item.getAttribute("discountPrices"))
				{
					final List<PromotionPriceRow> priceRowData = new ArrayList<PromotionPriceRow>(
							(Collection<PromotionPriceRow>) item.getAttribute("discountPrices"));
					for (final PromotionPriceRow priceRow : priceRowData)
					{
						if (null != priceRow.getAttribute("price"))
						{
							promoData.setDiscount(priceRow.getAttribute("price").toString());
						}
					}
				}
			}
			else
			{
				promoData.setIsPercentageDiscount(MarketplacecommerceservicesConstants.EMPTY);
				promoData.setDiscount(MarketplacecommerceservicesConstants.EMPTY);
			}

			if (null != item.getAttribute("quantity"))
			{
				promoData.setQcount(item.getAttribute("quantity").toString());
			}
			else
			{
				promoData.setQcount(MarketplacecommerceservicesConstants.EMPTY);
			}


			if (null != item.getAttribute("excludedProducts"))
			{
				final List<Product> productData = (List<Product>) item.getAttribute("excludedProducts");
				final List<String> productList = new ArrayList<String>();
				if (null != productData && !productData.isEmpty())
				{
					for (final Product product : productData)
					{
						productList.add(product.getCode());
					}
					promoData.setExcludedProduct(productList);
				}
			}
			else
			{
				final List<String> productList = new ArrayList<String>();
				promoData.setExcludedProduct(productList);
			}


			if (null != item.getAttribute("restrictions"))
			{
				final List<String> restrictionCodes = new ArrayList<String>();
				final Collection<AbstractPromotionRestriction> restrictionCol = (Collection<AbstractPromotionRestriction>) (item
						.getAttribute("restrictions"));

				final Collection<AbstractPromotionRestriction> restrictionData = new ArrayList<AbstractPromotionRestriction>(
						restrictionCol);
				if (!restrictionData.isEmpty())
				{
					for (final AbstractPromotionRestriction restriction : restrictionData)
					{
						restrictionCodes.add(restriction.getRestrictionType());
						if (restriction instanceof EtailSellerSpecificRestriction)
						{
							final EtailSellerSpecificRestriction seller = (EtailSellerSpecificRestriction) restriction;
							final List<SellerInformation> sellerList = seller.getSellerDetailsList();
							List<String> sellerData = new ArrayList<String>();
							if (null != sellerList && !sellerList.isEmpty())
							{
								for (final SellerInformation info : sellerList)
								{
									sellerData.add(info.getSellerID());
								}
								promoData.setSeller(sellerData);
							}
							else
							{
								sellerData = new ArrayList<String>();
								promoData.setSeller(sellerData);
							}
						}
						else if (restriction instanceof ManufacturesRestriction)
						{
							final ManufacturesRestriction manufacturesRestriction = (ManufacturesRestriction) restriction;
							final List<String> brandData = new ArrayList<String>();
							final List<Category> brandList = (List<Category>) manufacturesRestriction.getManufacturers();
							for (final Category restrBrand : brandList)
							{
								brandData.add(restrBrand.getName());
							}
							promoData.setBrand(brandData);
							//brandData = manufacturesRestriction.getManufacturers();
							//							if (null != brandData && !brandData.isEmpty())
							//							{
							//								promoData.setBrand(brandData);
							//							}
							//							else
							//							{
							//								brandData = new ArrayList<String>();
							//								promoData.setBrand(brandData);
							//							}
						}
					}
					promoData.setRestriction(restrictionCodes);
				}
			}
			else
			{
				final List<String> restrictionCodes = new ArrayList<String>();
				promoData.setRestriction(restrictionCodes);
				promoData.setSeller(restrictionCodes);
				promoData.setBrand(restrictionCodes);
			}

			if (null != item.getAttribute("minimumAmount"))
			{
				promoData.setCategoryMinimumValue(item.getAttribute("minimumAmount").toString());
			}
			else
			{
				promoData.setCategoryMinimumValue(MarketplacecommerceservicesConstants.EMPTY);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
		}

		return promoData;
	}

	/**
	 * @Description : Populate Data for boolean attributes
	 * @param attribute
	 */
	private String populateBooleanData(final Object attribute)
	{
		boolean flag = false;
		String data = MarketplacecommerceservicesConstants.EMPTY;
		flag = ((Boolean) attribute).booleanValue();
		if (flag)
		{
			data = MarketplacecommerceservicesConstants.YES;
		}
		else
		{
			data = MarketplacecommerceservicesConstants.NO;
		}
		return data;
	}


}
