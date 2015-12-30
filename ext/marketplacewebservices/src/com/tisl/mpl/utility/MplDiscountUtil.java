/**
 *
 */
package com.tisl.mpl.utility;


import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotions.model.OrderPromotionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionPriceRowModel;
import de.hybris.platform.util.localization.Localization;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.data.FiredPromoData;
import com.tisl.mpl.data.MplPromotionData;
import com.tisl.mpl.data.PotentialPromoData;
import com.tisl.mpl.enums.DiscTypesEnum;
import com.tisl.mpl.model.BuyAAboveXGetPercentageOrAmountOffModel;
import com.tisl.mpl.model.BuyABFreePrecentageDiscountModel;
import com.tisl.mpl.model.BuyAGetPrecentageDiscountCashbackModel;
import com.tisl.mpl.model.BuyAGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyAPercentageDiscountModel;
import com.tisl.mpl.model.BuyAandBGetPrecentageDiscountCashbackModel;
import com.tisl.mpl.model.BuyAandBGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyAandBPrecentageDiscountModel;
import com.tisl.mpl.model.BuyAandBgetCModel;
import com.tisl.mpl.model.BuyAboveXGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyXItemsofproductAgetproductBforfreeModel;
import com.tisl.mpl.model.CartOrderThresholdDiscountCashbackModel;
import com.tisl.mpl.model.CartOrderThresholdDiscountPromotionModel;
import com.tisl.mpl.model.CustomOrderThresholdFreeGiftPromotionModel;
import com.tisl.mpl.model.CustomProductBOGOFPromotionModel;



/**
 * @author TCS
 *
 */
public class MplDiscountUtil
{
	@Autowired
	private PriceDataFactory priceDataFactory;

	/**
	 * @Description : For Filtering Product Promotions
	 * @param productPromotion
	 * @param cart
	 * @return responseData
	 */
	public MplPromotionData populateData(final ProductPromotionModel productPromotion, final CartModel cart)
	{
		MplPromotionData promoData = new MplPromotionData();
		if (productPromotion instanceof BuyAPercentageDiscountModel)
		{
			promoData = getBuyAPercentageDiscountData(productPromotion, cart);

		}
		else if (productPromotion instanceof BuyAandBPrecentageDiscountModel)
		{
			promoData = getBuyAandBPrecentageDiscountData(productPromotion, cart);
		}
		else if (productPromotion instanceof CustomProductBOGOFPromotionModel)
		{
			promoData = getBOGOData(productPromotion, cart);
		}
		else if (productPromotion instanceof BuyAGetPrecentageDiscountCashbackModel)
		{
			promoData = getBuyACashBackData(productPromotion, cart);
		}
		else if (productPromotion instanceof BuyAandBGetPrecentageDiscountCashbackModel)
		{
			promoData = getBuyAandBCashBackData(productPromotion, cart);
		}
		else if (productPromotion instanceof BuyAAboveXGetPercentageOrAmountOffModel)
		{
			promoData = getBuyAAboveXData(productPromotion, cart);
		}
		////////////////////////////////////////
		else if (productPromotion instanceof BuyAandBgetCModel)
		{
			promoData = getBuyAandBgetCData(productPromotion, cart);
		}
		else if (productPromotion instanceof BuyAandBGetPromotionOnShippingChargesModel)
		{
			promoData = getBuyAandBGetPromotionOnShippingChargesData(productPromotion, cart);
		}
		else if (productPromotion instanceof BuyABFreePrecentageDiscountModel)
		{
			promoData = getABFreePrecentageDiscountData(productPromotion, cart);
		}
		else if (productPromotion instanceof BuyAGetPromotionOnShippingChargesModel)
		{
			promoData = getBuyAGetPromotionOnShippingChargesData(productPromotion, cart);

		}
		else if (productPromotion instanceof BuyXItemsofproductAgetproductBforfreeModel)
		{
			promoData = getBuyXItemsofproductAgetproductBforfreeData(productPromotion, cart);

		}
		////////////////////////////////////
		return promoData;
	}





	/**
	 * @Description : For Filtering Cart Promotions
	 * @param orderPromotion
	 * @param cart
	 * @return MplPromotionData
	 */
	public MplPromotionData populateCartPromoData(final OrderPromotionModel orderPromotion, final CartModel cart)
	{
		MplPromotionData promoData = new MplPromotionData();
		if (orderPromotion instanceof CartOrderThresholdDiscountPromotionModel)
		{
			promoData = getCartDiscountPromoData(orderPromotion, cart);
		}

		if (orderPromotion instanceof BuyAboveXGetPromotionOnShippingChargesModel)
		{
			promoData = getCartShippingDiscountPromoData(orderPromotion, cart);
		}

		return promoData;
	}

	/**
	 * @Description : Populate Potential Product Promotion Data
	 * @param productPromotion
	 * @param cart
	 * @return MplPromotionData
	 */
	public MplPromotionData populatePotentialPromoData(final ProductPromotionModel productPromotion, final CartModel cart)
	{
		MplPromotionData promoData = null;

		if (productPromotion instanceof BuyAPercentageDiscountModel)
		{
			promoData = new MplPromotionData();
			final List<ProductModel> productList = new ArrayList<ProductModel>();
			final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
			final BuyAPercentageDiscountModel oModel = (BuyAPercentageDiscountModel) productPromotion;
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(oModel.getDescription());
			potentialPromo.setPromoStartDate(oModel.getStartDate());
			potentialPromo.setPromoEndDate(oModel.getEndDate());
			final Collection<ProductModel> products = oModel.getProducts();
			if (null != products)
			{
				productList.addAll(products);
			}
			potentialPromo.setPromoPotProducts(productList);
			final Collection<CategoryModel> categories = oModel.getCategories();
			if (null != categories)
			{
				categoryList.addAll(categories);
			}
			potentialPromo.setPromoPotCategories(categoryList);
			potentialPromo.setPromoType(oModel.getPromotionType());
			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
				promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.POTENTIAL_PROMO_MESSAGE);
			}
		}
		else if (productPromotion instanceof BuyAandBPrecentageDiscountModel)
		{
			final BuyAandBPrecentageDiscountModel oModel = (BuyAandBPrecentageDiscountModel) productPromotion;
			promoData = new MplPromotionData();
			final List<ProductModel> productList = new ArrayList<ProductModel>();
			final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(oModel.getDescription());
			potentialPromo.setPromoStartDate(oModel.getStartDate());
			potentialPromo.setPromoEndDate(oModel.getEndDate());
			final Collection<ProductModel> products = oModel.getProducts();
			if (null != products)
			{
				productList.addAll(products);
			}
			potentialPromo.setPromoPotProducts(productList);
			final Collection<CategoryModel> categories = oModel.getCategories();
			if (null != categories)
			{
				categoryList.addAll(categories);
			}
			potentialPromo.setPromoPotCategories(categoryList);
			potentialPromo.setPromoType(oModel.getPromotionType());
			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
				promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.POTENTIAL_PROMO_MESSAGE);
			}

		}
		else if (productPromotion instanceof CustomProductBOGOFPromotionModel)
		{
			final CustomProductBOGOFPromotionModel oModel = (CustomProductBOGOFPromotionModel) productPromotion;
			promoData = new MplPromotionData();
			final List<ProductModel> productList = new ArrayList<ProductModel>();
			final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(oModel.getDescription());
			potentialPromo.setPromoStartDate(oModel.getStartDate());
			potentialPromo.setPromoEndDate(oModel.getEndDate());
			final Collection<ProductModel> products = oModel.getProducts();
			if (null != products)
			{
				productList.addAll(products);
			}
			potentialPromo.setPromoPotProducts(productList);
			final Collection<CategoryModel> categories = oModel.getCategories();
			if (null != categories)
			{
				categoryList.addAll(categories);
			}
			potentialPromo.setPromoPotCategories(categoryList);
			potentialPromo.setPromoType(oModel.getPromotionType());
			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
				promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.POTENTIAL_PROMO_MESSAGE);
			}
		}
		else if (productPromotion instanceof BuyAGetPrecentageDiscountCashbackModel)
		{
			final BuyAGetPrecentageDiscountCashbackModel cashBackModel = (BuyAGetPrecentageDiscountCashbackModel) productPromotion;
			promoData = new MplPromotionData();
			final List<ProductModel> productList = new ArrayList<ProductModel>();
			final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(cashBackModel.getDescription());
			potentialPromo.setPromoStartDate(cashBackModel.getStartDate());
			potentialPromo.setPromoEndDate(cashBackModel.getEndDate());
			final Collection<ProductModel> products = cashBackModel.getProducts();
			if (null != products)
			{
				productList.addAll(products);
			}
			potentialPromo.setPromoPotProducts(productList);
			final Collection<CategoryModel> categories = cashBackModel.getCategories();
			if (null != categories)
			{
				categoryList.addAll(categories);
			}
			potentialPromo.setPromoPotCategories(categoryList);
			potentialPromo.setPromoType(cashBackModel.getPromotionType());
			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
				promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.POTENTIAL_PROMO_MESSAGE);
			}
		}
		else if (productPromotion instanceof BuyABFreePrecentageDiscountModel)
		{
			final BuyABFreePrecentageDiscountModel oModel = (BuyABFreePrecentageDiscountModel) productPromotion;
			promoData = new MplPromotionData();
			final List<ProductModel> productList = new ArrayList<ProductModel>();
			final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(oModel.getDescription());
			potentialPromo.setPromoStartDate(oModel.getStartDate());
			potentialPromo.setPromoEndDate(oModel.getEndDate());
			final Collection<ProductModel> products = oModel.getProducts();
			if (null != products)
			{
				productList.addAll(products);
			}
			potentialPromo.setPromoPotProducts(productList);
			final Collection<CategoryModel> categories = oModel.getCategories();
			if (null != categories)
			{
				categoryList.addAll(categories);
			}
			potentialPromo.setPromoPotCategories(categoryList);
			potentialPromo.setPromoType(oModel.getPromotionType());
			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
				promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.POTENTIAL_PROMO_MESSAGE);
			}
		}
		else if (productPromotion instanceof BuyAandBGetPrecentageDiscountCashbackModel)
		{
			final BuyAandBGetPrecentageDiscountCashbackModel cashBackModel = (BuyAandBGetPrecentageDiscountCashbackModel) productPromotion;
			promoData = new MplPromotionData();
			final List<ProductModel> productList = new ArrayList<ProductModel>();
			final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(cashBackModel.getDescription());
			potentialPromo.setPromoPotMessage(cashBackModel.getTitle());
			potentialPromo.setPromoStartDate(cashBackModel.getStartDate());
			potentialPromo.setPromoEndDate(cashBackModel.getEndDate());
			final Collection<ProductModel> products = cashBackModel.getProducts();
			if (null != products)
			{
				productList.addAll(products);
			}
			potentialPromo.setPromoPotProducts(productList);
			final Collection<CategoryModel> categories = cashBackModel.getCategories();
			if (null != categories)
			{
				categoryList.addAll(categories);
			}
			potentialPromo.setPromoPotCategories(categoryList);
			potentialPromo.setPromoType(cashBackModel.getPromotionType());
			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
				promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.POTENTIAL_PROMO_MESSAGE);
			}

		}
		else if (productPromotion instanceof BuyXItemsofproductAgetproductBforfreeModel)
		{
			final BuyXItemsofproductAgetproductBforfreeModel freebieModel = (BuyXItemsofproductAgetproductBforfreeModel) productPromotion;
			promoData = new MplPromotionData();
			final List<ProductModel> productList = new ArrayList<ProductModel>();
			final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(freebieModel.getDescription());
			potentialPromo.setPromoPotMessage(freebieModel.getTitle());
			potentialPromo.setPromoStartDate(freebieModel.getStartDate());
			potentialPromo.setPromoEndDate(freebieModel.getEndDate());
			final Collection<ProductModel> products = freebieModel.getProducts();
			if (null != products)
			{
				productList.addAll(products);
			}
			potentialPromo.setPromoPotProducts(productList);
			final Collection<CategoryModel> categories = freebieModel.getCategories();
			if (null != categories)
			{
				categoryList.addAll(categories);
			}
			potentialPromo.setPromoPotCategories(categoryList);
			potentialPromo.setPromoType(freebieModel.getPromotionType());
			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
				promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.POTENTIAL_PROMO_MESSAGE);
			}
		}
		else if (productPromotion instanceof BuyAandBgetCModel)
		{
			final BuyAandBgetCModel freebieModel = (BuyAandBgetCModel) productPromotion;
			promoData = new MplPromotionData();
			final List<ProductModel> productList = new ArrayList<ProductModel>();
			final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(freebieModel.getDescription());
			potentialPromo.setPromoPotMessage(freebieModel.getTitle());
			potentialPromo.setPromoStartDate(freebieModel.getStartDate());
			potentialPromo.setPromoEndDate(freebieModel.getEndDate());
			final Collection<ProductModel> products = freebieModel.getProducts();
			if (null != products)
			{
				productList.addAll(products);
			}
			potentialPromo.setPromoPotProducts(productList);
			final Collection<CategoryModel> categories = freebieModel.getCategories();
			if (null != categories)
			{
				categoryList.addAll(categories);
			}
			potentialPromo.setPromoPotCategories(categoryList);
			potentialPromo.setPromoType(freebieModel.getPromotionType());
			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
				promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.POTENTIAL_PROMO_MESSAGE);
			}

		}
		else if (productPromotion instanceof BuyAandBGetPromotionOnShippingChargesModel)
		{
			final BuyAandBGetPromotionOnShippingChargesModel oModel = (BuyAandBGetPromotionOnShippingChargesModel) productPromotion;
			promoData = new MplPromotionData();
			final List<ProductModel> productList = new ArrayList<ProductModel>();
			final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(oModel.getDescription());
			potentialPromo.setPromoPotMessage(oModel.getTitle());
			potentialPromo.setPromoStartDate(oModel.getStartDate());
			potentialPromo.setPromoEndDate(oModel.getEndDate());
			final Collection<ProductModel> products = oModel.getProducts();
			if (null != products)
			{
				productList.addAll(products);
			}
			potentialPromo.setPromoPotProducts(productList);
			final Collection<CategoryModel> categories = oModel.getCategories();
			if (null != categories)
			{
				categoryList.addAll(categories);
			}
			potentialPromo.setPromoPotCategories(categoryList);
			potentialPromo.setPromoType(oModel.getPromotionType());
			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
				promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.POTENTIAL_PROMO_MESSAGE);
			}

		}
		else if (productPromotion instanceof BuyAGetPromotionOnShippingChargesModel)
		{
			final BuyAGetPromotionOnShippingChargesModel oModel = (BuyAGetPromotionOnShippingChargesModel) productPromotion;
			promoData = new MplPromotionData();
			final List<ProductModel> productList = new ArrayList<ProductModel>();
			final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
			final PotentialPromoData potentialPromo = new PotentialPromoData();

			potentialPromo.setPromoMessage(oModel.getDescription());
			potentialPromo.setPromoPotMessage(oModel.getTitle());
			potentialPromo.setPromoStartDate(oModel.getStartDate());
			potentialPromo.setPromoEndDate(oModel.getEndDate());
			final Collection<ProductModel> products = oModel.getProducts();
			if (null != products)
			{
				productList.addAll(products);
			}
			potentialPromo.setPromoPotProducts(productList);
			final Collection<CategoryModel> categories = oModel.getCategories();
			if (null != categories)
			{
				categoryList.addAll(categories);
			}
			potentialPromo.setPromoPotCategories(categoryList);
			potentialPromo.setPromoType(oModel.getPromotionType());
			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
				promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.POTENTIAL_PROMO_MESSAGE);
			}

		}
		return promoData;
	}

	/**
	 * @Description: Populate Potential Promotion Data for Order Promotion
	 * @param orderPromotion
	 * @param cart
	 * @return responseData
	 */
	public MplPromotionData populatePotentialOrderPromoData(final OrderPromotionModel orderPromotion, final CartModel cart)
	{
		MplPromotionData promoData = null;

		if (orderPromotion instanceof CartOrderThresholdDiscountPromotionModel)
		{
			final CartOrderThresholdDiscountPromotionModel oModel = (CartOrderThresholdDiscountPromotionModel) orderPromotion;
			promoData = new MplPromotionData();
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(oModel.getDescription());
			potentialPromo.setPromoPotMessage(oModel.getTitle());
			potentialPromo.setPromoStartDate(oModel.getStartDate());
			potentialPromo.setPromoEndDate(oModel.getEndDate());
			potentialPromo.setPromoType(oModel.getPromotionType());

			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
				promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.POTENTIAL_PROMO_MESSAGE);
			}
		}


		if (orderPromotion instanceof CustomOrderThresholdFreeGiftPromotionModel)
		{
			final CustomOrderThresholdFreeGiftPromotionModel oModel = (CustomOrderThresholdFreeGiftPromotionModel) orderPromotion;
			promoData = new MplPromotionData();
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(oModel.getDescription());
			potentialPromo.setPromoPotMessage(oModel.getTitle());
			potentialPromo.setPromoStartDate(oModel.getStartDate());
			potentialPromo.setPromoEndDate(oModel.getEndDate());
			potentialPromo.setPromoType(oModel.getPromotionType());
			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
				promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.POTENTIAL_PROMO_MESSAGE);
			}

		}

		if (orderPromotion instanceof CartOrderThresholdDiscountCashbackModel)
		{
			final CartOrderThresholdDiscountCashbackModel oModel = (CartOrderThresholdDiscountCashbackModel) orderPromotion;
			promoData = new MplPromotionData();
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(oModel.getDescription());
			potentialPromo.setPromoPotMessage(oModel.getTitle());
			potentialPromo.setPromoStartDate(oModel.getStartDate());
			potentialPromo.setPromoEndDate(oModel.getEndDate());
			potentialPromo.setPromoType(oModel.getPromotionType());

			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
				promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.POTENTIAL_PROMO_MESSAGE);
			}

		}

		if (orderPromotion instanceof BuyAboveXGetPromotionOnShippingChargesModel)
		{
			final BuyAboveXGetPromotionOnShippingChargesModel oModel = (BuyAboveXGetPromotionOnShippingChargesModel) orderPromotion;
			promoData = new MplPromotionData();
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(oModel.getDescription());
			potentialPromo.setPromoPotMessage(oModel.getTitle());
			potentialPromo.setPromoStartDate(oModel.getStartDate());
			potentialPromo.setPromoEndDate(oModel.getEndDate());
			potentialPromo.setPromoType(oModel.getPromotionType());
			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
				promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.POTENTIAL_PROMO_MESSAGE);
			}
		}
		return promoData;
	}

	/**
	 * @Description : For Cart Level Discount Promotion
	 * @param orderPromotion
	 * @param cart
	 * @return responseData
	 */
	private MplPromotionData getCartDiscountPromoData(final OrderPromotionModel orderPromotion, final CartModel cart)
	{
		final MplPromotionData cartPromoData = new MplPromotionData();
		final List<PromotionPriceRowModel> priceRowList = new ArrayList<PromotionPriceRowModel>();

		final CartOrderThresholdDiscountPromotionModel cartModel = (CartOrderThresholdDiscountPromotionModel) orderPromotion;
		if (null != cartModel)
		{
			cartPromoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.CART_PROMO);
			if (null != cartModel.getDiscountPrices())
			{
				final Collection<PromotionPriceRowModel> discountPriceRows = cartModel.getDiscountPrices();
				if (null != discountPriceRows)
				{
					priceRowList.addAll(discountPriceRows);
				}

				if (priceRowList.size() > 0 && null != priceRowList.get(0).getCurrency()
						&& null != priceRowList.get(0).getCurrency().getIsocode() && null != priceRowList.get(0).getPrice())
				{
					final PriceData discountPrice = createPrice(cart, priceRowList.get(0).getPrice());
					cartPromoData.setDiscountPrice(discountPrice);
				}
			}
			if (null != cartModel.getPercentageOrAmount())
			{
				cartPromoData.setIsPercentage(cartModel.getPercentageOrAmount().toString());
			}

			if (null != cartModel.getPercentageDiscount() && cartModel.getPercentageOrAmount().booleanValue())
			{
				cartPromoData.setPercentagePromotion(cartModel.getPercentageDiscount().toString());
			}

			if (null != cartModel.getMessageFired())
			{
				final PotentialPromoData potentialPromo = new PotentialPromoData();
				potentialPromo.setPromoMessage(cartModel.getMessageFired());
				if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
				{
					cartPromoData.setPotentialPromotion(potentialPromo);
				}
			}

			if (null != cartModel.getMessageCouldHaveFired())
			{
				final FiredPromoData firedPromo = new FiredPromoData();
				firedPromo.setPromoMessage(cartModel.getMessageFired());
				if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
				{
					cartPromoData.setFiredPromotion(firedPromo);
				}
			}
		}
		return cartPromoData;
	}


	/**
	 * @Description : For Cart Level Discount Promotion
	 * @param orderPromotion
	 * @param cart
	 * @return responseData
	 */
	private MplPromotionData getCartShippingDiscountPromoData(final OrderPromotionModel orderPromotion, final CartModel cart)
	{
		final MplPromotionData cartPromoData = new MplPromotionData();
		final List<PromotionPriceRowModel> priceRowList = new ArrayList<PromotionPriceRowModel>();

		final BuyAboveXGetPromotionOnShippingChargesModel cartModel = (BuyAboveXGetPromotionOnShippingChargesModel) orderPromotion;
		if (null != cartModel)
		{
			cartPromoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.CART_PROMO);
			if (null != cartModel.getDiscountPrices())
			{
				final Collection<PromotionPriceRowModel> discountPriceRows = cartModel.getDiscountPrices();
				if (null != discountPriceRows)
				{
					priceRowList.addAll(discountPriceRows);
				}

				if (priceRowList.size() > 0 && null != priceRowList.get(0).getCurrency()
						&& null != priceRowList.get(0).getCurrency().getIsocode() && null != priceRowList.get(0).getPrice())
				{
					final PriceData discountPrice = createPrice(cart, priceRowList.get(0).getPrice());
					cartPromoData.setDiscountPrice(discountPrice);
				}
			}


			if (null != cartModel.getDiscTypesOnShippingCharges()
					&& StringUtils.isNotEmpty(cartModel.getDiscTypesOnShippingCharges().getCode())
					&& cartModel.getDiscTypesOnShippingCharges().getCode().equalsIgnoreCase(DiscTypesEnum.PERCENTAGE.getCode()))
			{
				cartPromoData.setIsPercentage(MarketplacewebservicesConstants.TRUE);
			}
			else if (null != cartModel.getDiscTypesOnShippingCharges()
					&& StringUtils.isNotEmpty(cartModel.getDiscTypesOnShippingCharges().getCode())
					&& cartModel.getDiscTypesOnShippingCharges().getCode().equalsIgnoreCase(DiscTypesEnum.AMOUNT.getCode()))
			{
				cartPromoData.setIsPercentage(MarketplacewebservicesConstants.FALSE);
			}
			else if (null != cartModel.getDiscTypesOnShippingCharges()
					&& StringUtils.isNotEmpty(cartModel.getDiscTypesOnShippingCharges().getCode())
					&& cartModel.getDiscTypesOnShippingCharges().getCode().equalsIgnoreCase(DiscTypesEnum.FREE.getCode()))
			{
				cartPromoData.setIsPercentage(DiscTypesEnum.FREE.getCode());
			}


			if (null != cartModel.getPercentageDiscount())
			{
				cartPromoData.setPercentagePromotion(cartModel.getPercentageDiscount().toString());
			}

			if (null != cartModel.getMessageFired())
			{
				final PotentialPromoData potentialPromo = new PotentialPromoData();
				potentialPromo.setPromoMessage(cartModel.getMessageFired());
				if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
				{
					cartPromoData.setPotentialPromotion(potentialPromo);
				}
			}

			if (null != cartModel.getMessageCouldHaveFired())
			{
				final FiredPromoData firedPromo = new FiredPromoData();
				firedPromo.setPromoMessage(cartModel.getMessageFired());
				if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
				{
					cartPromoData.setFiredPromotion(firedPromo);
				}
			}
		}
		return cartPromoData;
	}


	/**
	 * @Description : For Buy A Above X Get Percentage Or Amount Off Promotion
	 * @param productPromotion
	 * @param cart
	 * @return promoData
	 */
	private MplPromotionData getBuyAAboveXData(final ProductPromotionModel productPromotion, final CartModel cart)
	{
		final MplPromotionData promoData = new MplPromotionData();

		final BuyAAboveXGetPercentageOrAmountOffModel buyAAboveXDiscountModel = (BuyAAboveXGetPercentageOrAmountOffModel) productPromotion;
		if (null != buyAAboveXDiscountModel)
		{
			promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.PRODUCT_PROMO);
			promoData.setIsPercentage(buyAAboveXDiscountModel.getPercentageOrAmount().toString());
			final PriceData discountPrice = createPrice(cart, calculateDiscount(cart));
			promoData.setDiscountPrice(discountPrice);

			if (null != buyAAboveXDiscountModel.getPercentageDiscount())
			{
				promoData.setPercentagePromotion(buyAAboveXDiscountModel.getPercentageDiscount().toString());
			}

			if (null != buyAAboveXDiscountModel.getMessageFired())
			{
				final PotentialPromoData potentialPromo = new PotentialPromoData();
				potentialPromo.setPromoMessage(buyAAboveXDiscountModel.getMessageFired());
				if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
				{
					promoData.setPotentialPromotion(potentialPromo);
				}
			}

			if (null != buyAAboveXDiscountModel.getMessageCouldHaveFired())
			{
				final FiredPromoData firedPromo = new FiredPromoData();
				firedPromo.setPromoMessage(buyAAboveXDiscountModel.getMessageFired());
				if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
				{
					promoData.setFiredPromotion(firedPromo);
				}
			}
		}
		return promoData;
	}


	/**
	 * @Description : For Buy A Get Percentage/Amount CashBack
	 * @param productPromotion
	 * @param cart
	 * @return responseData
	 */
	private MplPromotionData getBuyACashBackData(final ProductPromotionModel productPromotion, final CartModel cart)
	{
		final MplPromotionData promoData = new MplPromotionData();

		final List<PromotionPriceRowModel> priceRowList = new ArrayList<PromotionPriceRowModel>();
		final BuyAGetPrecentageDiscountCashbackModel cashBackModel = (BuyAGetPrecentageDiscountCashbackModel) productPromotion;

		promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.CASHBACK_FIREDPROMO_MESSAGE);
		if (null != cashBackModel.getPercentageOrAmount() && cashBackModel.getPercentageOrAmount().booleanValue()
				&& null != cashBackModel.getPercentageDiscount())
		{

			final FiredPromoData firedPromo = new FiredPromoData();
			firedPromo.setPromoMessage(Localization.getLocalizedString("cashBack.promotion.firedMessage.ifPercentage"));
			if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
			{
				promoData.setFiredPromotion(firedPromo);
			}
		}
		else if (!cashBackModel.getPercentageOrAmount().booleanValue() && null != cashBackModel.getDiscountPrices())
		{
			final Collection<PromotionPriceRowModel> discountPriceRows = cashBackModel.getDiscountPrices();
			if (null != discountPriceRows)
			{
				priceRowList.addAll(discountPriceRows);
				if (priceRowList.size() > 0 && null != priceRowList.get(0).getPrice())
				{
					final Double cashbBackVal = priceRowList.get(0).getPrice();
					final PriceData discountPrice = createPrice(cart, cashbBackVal);
					promoData.setDiscountPrice(discountPrice);

					final FiredPromoData firedPromo = new FiredPromoData();
					firedPromo.setPromoMessage(Localization.getLocalizedString("cashBack.promotion.firedMessage.ifAmount"));
					if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
					{
						promoData.setFiredPromotion(firedPromo);
					}
				}
			}
		}
		return promoData;
	}


	/**
	 * @Description : For Buy A and B Get Percentage/Amount CashBack
	 * @param productPromotion
	 * @param cart
	 * @return responseData
	 */
	private MplPromotionData getBuyAandBCashBackData(final ProductPromotionModel productPromotion, final CartModel cart)
	{
		final MplPromotionData promoData = new MplPromotionData();
		final List<PromotionPriceRowModel> priceRowList = new ArrayList<PromotionPriceRowModel>();

		final BuyAandBGetPrecentageDiscountCashbackModel cashBackModel = (BuyAandBGetPrecentageDiscountCashbackModel) productPromotion;

		promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.CASHBACK_FIREDPROMO_MESSAGE);
		if (null != cashBackModel.getPercentageOrAmount() && cashBackModel.getPercentageOrAmount().booleanValue()
				&& null != cashBackModel.getPercentageDiscount())
		{
			final FiredPromoData firedPromo = new FiredPromoData();
			firedPromo.setPromoMessage(Localization.getLocalizedString("product.promotion.firedMessage.ifPercentage"));
			if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
			{
				promoData.setFiredPromotion(firedPromo);
			}
		}
		else if (!cashBackModel.getPercentageOrAmount().booleanValue() && null != cashBackModel.getDiscountPrices())
		{
			{
				final Collection<PromotionPriceRowModel> discountPriceRows = cashBackModel.getDiscountPrices();
				if (null != discountPriceRows)
				{
					priceRowList.addAll(discountPriceRows);
					if (priceRowList.size() > 0 && null != priceRowList.get(0).getPrice())
					{
						final Double cashbBackVal = priceRowList.get(0).getPrice();
						final PriceData discountPrice = createPrice(cart, cashbBackVal);
						promoData.setDiscountPrice(discountPrice);

						final FiredPromoData firedPromo = new FiredPromoData();
						firedPromo.setPromoMessage(Localization.getLocalizedString("product.promotion.firedMessage.ifAmount"));
						if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
						{
							promoData.setFiredPromotion(firedPromo);
						}
					}
				}
			}

		}
		return promoData;
	}



	/**
	 * @Description : For BOGO Promotion
	 * @param productPromotion
	 * @param cart
	 * @return responseData
	 */
	private MplPromotionData getBOGOData(final ProductPromotionModel productPromotion, final CartModel cart)
	{
		final MplPromotionData promoData = new MplPromotionData();
		final CustomProductBOGOFPromotionModel bogoModel = (CustomProductBOGOFPromotionModel) productPromotion;
		if (null != bogoModel)
		{
			promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.PRODUCT_PROMO);
			final PriceData discountPrice = createPrice(cart, calculateDiscount(cart));
			promoData.setDiscountPrice(discountPrice);

			if (null != bogoModel.getMessageFired())
			{
				final PotentialPromoData potentialPromo = new PotentialPromoData();
				potentialPromo.setPromoMessage(bogoModel.getMessageFired());
				if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
				{
					promoData.setPotentialPromotion(potentialPromo);
				}
			}

			if (null != bogoModel.getMessageCouldHaveFired())
			{
				final FiredPromoData firedPromo = new FiredPromoData();
				firedPromo.setPromoMessage(bogoModel.getMessageFired());
				if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
				{
					promoData.setFiredPromotion(firedPromo);
				}
			}
		}
		return promoData;
	}

	/**
	 * @Description : For Buy A and B Get Discount Promotion
	 * @param productPromotion
	 * @param cart
	 * @return MplPromotionData
	 */
	private MplPromotionData getBuyAandBPrecentageDiscountData(final ProductPromotionModel productPromotion, final CartModel cart)
	{
		final MplPromotionData promoData = new MplPromotionData();
		final List<PromotionPriceRowModel> priceRowList = new ArrayList<PromotionPriceRowModel>();

		final BuyAandBPrecentageDiscountModel oModel = (BuyAandBPrecentageDiscountModel) productPromotion;
		if (null != oModel)
		{
			promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.PRODUCT_PROMO);

			if (null != oModel.getPercentageOrAmount())
			{
				promoData.setIsPercentage(oModel.getPercentageOrAmount().toString());
			}

			if (null != oModel.getPercentageDiscount() && oModel.getPercentageOrAmount().booleanValue())
			{
				promoData.setPercentagePromotion(oModel.getPercentageDiscount().toString());
			}
			else if (!oModel.getPercentageOrAmount().booleanValue() && null != oModel.getDiscountPrices())
			{
				final Collection<PromotionPriceRowModel> discountPriceRows = oModel.getDiscountPrices();
				if (null != discountPriceRows)
				{
					priceRowList.addAll(discountPriceRows);
				}

				if (priceRowList.size() > 0 && null != priceRowList.get(0).getCurrency()
						&& null != priceRowList.get(0).getCurrency().getIsocode() && null != priceRowList.get(0).getPrice())
				{
					final PriceData discountPrice = createPrice(cart, priceRowList.get(0).getPrice());
					promoData.setDiscountPrice(discountPrice);
				}
			}

			if (null != oModel.getMessageFired())
			{
				final PotentialPromoData potentialPromo = new PotentialPromoData();
				potentialPromo.setPromoMessage(oModel.getMessageFired());
				if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
				{
					promoData.setPotentialPromotion(potentialPromo);
				}
			}

			if (null != oModel.getMessageCouldHaveFired())
			{
				final FiredPromoData firedPromo = new FiredPromoData();
				firedPromo.setPromoMessage(oModel.getMessageFired());
				if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
				{
					promoData.setFiredPromotion(firedPromo);
				}
			}
		}
		return promoData;
	}

	/**
	 * @Description : For Buy A Get Discount Promotion
	 * @param productPromotion
	 * @param cart
	 * @return responseData
	 */
	private MplPromotionData getBuyAPercentageDiscountData(final ProductPromotionModel productPromotion, final CartModel cart)
	{
		final MplPromotionData promoData = new MplPromotionData();
		final List<PromotionPriceRowModel> priceRowList = new ArrayList<PromotionPriceRowModel>();

		final BuyAPercentageDiscountModel buyAPercentageDiscountModel = (BuyAPercentageDiscountModel) productPromotion;
		if (null != buyAPercentageDiscountModel)
		{
			promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.PRODUCT_PROMO);
			promoData.setIsPercentage(buyAPercentageDiscountModel.getPercentageOrAmount().toString());
			if (null != buyAPercentageDiscountModel.getPercentageOrAmount())
			{
				promoData.setIsPercentage(buyAPercentageDiscountModel.getPercentageOrAmount().toString());
			}
			if (null != buyAPercentageDiscountModel.getPercentageDiscount()
					&& buyAPercentageDiscountModel.getPercentageOrAmount().booleanValue())
			{
				promoData.setPercentagePromotion(buyAPercentageDiscountModel.getPercentageDiscount().toString());
			}
			else if (!buyAPercentageDiscountModel.getPercentageOrAmount().booleanValue()
					&& null != buyAPercentageDiscountModel.getDiscountPrices())
			{
				final Collection<PromotionPriceRowModel> discountPriceRows = buyAPercentageDiscountModel.getDiscountPrices();
				if (null != discountPriceRows)
				{
					priceRowList.addAll(discountPriceRows);
				}

				if (priceRowList.size() > 0 && null != priceRowList.get(0).getCurrency()
						&& null != priceRowList.get(0).getCurrency().getIsocode() && null != priceRowList.get(0).getPrice())
				{
					final PriceData discountPrice = createPrice(cart, priceRowList.get(0).getPrice());
					promoData.setDiscountPrice(discountPrice);
				}
			}
			if (null != buyAPercentageDiscountModel.getMessageFired())
			{
				final PotentialPromoData potentialPromo = new PotentialPromoData();
				potentialPromo.setPromoMessage(buyAPercentageDiscountModel.getMessageFired());
				if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
				{
					promoData.setPotentialPromotion(potentialPromo);
				}
			}

			if (null != buyAPercentageDiscountModel.getMessageCouldHaveFired())
			{
				final FiredPromoData firedPromo = new FiredPromoData();
				firedPromo.setPromoMessage(buyAPercentageDiscountModel.getMessageFired());
				if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
				{
					promoData.setFiredPromotion(firedPromo);
				}
			}
		}
		return promoData;
	}

	////////////////////////////
	/**
	 * @Description : For Buy A and B get C
	 * @param productPromotion
	 * @param cart
	 * @return responseData
	 */
	private MplPromotionData getBuyAandBgetCData(final ProductPromotionModel productPromotion, final CartModel cart)
	{
		final MplPromotionData promoData = new MplPromotionData();
		final BuyAandBgetCModel oModel = (BuyAandBgetCModel) productPromotion;

		promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.PRODUCT_PROMO);

		if (null != oModel.getMessageFired())
		{
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(oModel.getMessageFired());
			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
			}
		}

		if (null != oModel.getMessageCouldHaveFired())
		{
			final FiredPromoData firedPromo = new FiredPromoData();
			firedPromo.setPromoMessage(oModel.getMessageFired());
			if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
			{
				promoData.setFiredPromotion(firedPromo);
			}
		}
		return promoData;
	}

	/**
	 * @Description : For BUY A get B Free Percentage Discount
	 * @param productPromotion
	 * @param cart
	 * @return responseData
	 */
	private MplPromotionData getABFreePrecentageDiscountData(final ProductPromotionModel productPromotion, final CartModel cart)
	{
		final MplPromotionData promoData = new MplPromotionData();
		final List<PromotionPriceRowModel> priceRowList = new ArrayList<PromotionPriceRowModel>();

		final BuyABFreePrecentageDiscountModel oModel = (BuyABFreePrecentageDiscountModel) productPromotion;

		promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.PRODUCT_PROMO);
		if (null != oModel.getPercentageOrAmount())
		{
			promoData.setIsPercentage(String.valueOf(oModel.getPercentageOrAmount().booleanValue()));
		}
		if (null != oModel.getPercentageOrAmount() && oModel.getPercentageOrAmount().booleanValue()
				&& null != oModel.getPercentageDiscount())
		{
			final FiredPromoData firedPromo = new FiredPromoData();
			firedPromo.setPromoMessage(Localization.getLocalizedString("product.promotion.firedMessage.ifPercentage"));
			if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
			{
				promoData.setFiredPromotion(firedPromo);
			}
		}
		else if (!oModel.getPercentageOrAmount().booleanValue() && null != oModel.getDiscountPrices())
		{


			final Collection<PromotionPriceRowModel> discountPriceRows = oModel.getDiscountPrices();
			if (null != discountPriceRows)
			{
				priceRowList.addAll(discountPriceRows);
			}

			if (priceRowList.size() > 0 && null != priceRowList.get(0).getCurrency()
					&& null != priceRowList.get(0).getCurrency().getIsocode() && null != priceRowList.get(0).getPrice())
			{
				final PriceData discountPrice = createPrice(cart, priceRowList.get(0).getPrice());
				promoData.setDiscountPrice(discountPrice);
			}

		}
		return promoData;
	}

	/**
	 * @Description : For Buy A Get Promotion On Shipping Charges
	 * @param productPromotion
	 * @param cart
	 * @return responseData
	 */
	private MplPromotionData getBuyAGetPromotionOnShippingChargesData(final ProductPromotionModel productPromotion,
			final CartModel cart)
	{
		final MplPromotionData promoData = new MplPromotionData();
		final List<PromotionPriceRowModel> priceRowList = new ArrayList<PromotionPriceRowModel>();

		final BuyAGetPromotionOnShippingChargesModel oModel = (BuyAGetPromotionOnShippingChargesModel) productPromotion;

		if (null != oModel.getDiscTypesOnShippingCharges()
				&& StringUtils.isNotEmpty(oModel.getDiscTypesOnShippingCharges().getCode())
				&& oModel.getDiscTypesOnShippingCharges().getCode().equalsIgnoreCase(DiscTypesEnum.PERCENTAGE.getCode()))
		{
			promoData.setIsPercentage(MarketplacewebservicesConstants.TRUE);
		}
		else if (null != oModel.getDiscTypesOnShippingCharges()
				&& StringUtils.isNotEmpty(oModel.getDiscTypesOnShippingCharges().getCode())
				&& oModel.getDiscTypesOnShippingCharges().getCode().equalsIgnoreCase(DiscTypesEnum.AMOUNT.getCode()))
		{
			promoData.setIsPercentage(MarketplacewebservicesConstants.FALSE);
		}
		else if (null != oModel.getDiscTypesOnShippingCharges()
				&& StringUtils.isNotEmpty(oModel.getDiscTypesOnShippingCharges().getCode())
				&& oModel.getDiscTypesOnShippingCharges().getCode().equalsIgnoreCase(DiscTypesEnum.FREE.getCode()))
		{
			promoData.setIsPercentage(DiscTypesEnum.FREE.getCode());
		}


		promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.PRODUCT_PROMO);
		if (null != oModel.getPercentageDiscount())
		{
			final FiredPromoData firedPromo = new FiredPromoData();
			firedPromo.setPromoMessage(Localization.getLocalizedString("product.promotion.firedMessage.ifPercentage"));
			if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
			{
				promoData.setFiredPromotion(firedPromo);
			}
		}
		else if (null != oModel.getPercentageDiscount() && null != oModel.getDiscountPrices())
		{
			final Collection<PromotionPriceRowModel> discountPriceRows = oModel.getDiscountPrices();
			if (null != discountPriceRows)
			{
				priceRowList.addAll(discountPriceRows);
			}

			if (priceRowList.size() > 0 && null != priceRowList.get(0).getCurrency()
					&& null != priceRowList.get(0).getCurrency().getIsocode() && null != priceRowList.get(0).getPrice())
			{
				final PriceData discountPrice = createPrice(cart, priceRowList.get(0).getPrice());
				promoData.setDiscountPrice(discountPrice);
			}

		}

		if (null != oModel.getMessageFired())
		{
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(oModel.getMessageFired());
			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
			}
		}

		if (null != oModel.getMessageCouldHaveFired())
		{
			final FiredPromoData firedPromo = new FiredPromoData();
			firedPromo.setPromoMessage(oModel.getMessageFired());
			if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
			{
				promoData.setFiredPromotion(firedPromo);
			}
		}
		return promoData;
	}

	/**
	 * @Description : For Buy A Get Promotion On Shipping Charges
	 * @param productPromotion
	 * @param cart
	 * @return responseData
	 */
	private MplPromotionData getBuyAandBGetPromotionOnShippingChargesData(final ProductPromotionModel productPromotion,
			final CartModel cart)
	{
		final MplPromotionData promoData = new MplPromotionData();
		final List<PromotionPriceRowModel> priceRowList = new ArrayList<PromotionPriceRowModel>();

		final BuyAandBGetPromotionOnShippingChargesModel oModel = (BuyAandBGetPromotionOnShippingChargesModel) productPromotion;

		if (null != oModel.getDiscTypesOnShippingCharges()
				&& StringUtils.isNotEmpty(oModel.getDiscTypesOnShippingCharges().getCode())
				&& oModel.getDiscTypesOnShippingCharges().getCode().equalsIgnoreCase(DiscTypesEnum.PERCENTAGE.getCode()))
		{
			promoData.setIsPercentage(MarketplacewebservicesConstants.TRUE);
		}
		else if (null != oModel.getDiscTypesOnShippingCharges()
				&& StringUtils.isNotEmpty(oModel.getDiscTypesOnShippingCharges().getCode())
				&& oModel.getDiscTypesOnShippingCharges().getCode().equalsIgnoreCase(DiscTypesEnum.AMOUNT.getCode()))
		{
			promoData.setIsPercentage(MarketplacewebservicesConstants.FALSE);
		}
		else if (null != oModel.getDiscTypesOnShippingCharges()
				&& StringUtils.isNotEmpty(oModel.getDiscTypesOnShippingCharges().getCode())
				&& oModel.getDiscTypesOnShippingCharges().getCode().equalsIgnoreCase(DiscTypesEnum.FREE.getCode()))
		{
			promoData.setIsPercentage(DiscTypesEnum.FREE.getCode());
		}


		promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.PRODUCT_PROMO);
		if (null != oModel.getPercentageDiscount())
		{
			final FiredPromoData firedPromo = new FiredPromoData();
			firedPromo.setPromoMessage(Localization.getLocalizedString("product.promotion.firedMessage.ifPercentage"));
			if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
			{
				promoData.setFiredPromotion(firedPromo);
			}
		}
		else if (null != oModel.getPercentageDiscount() && null != oModel.getDiscountPrices())
		{
			final Collection<PromotionPriceRowModel> discountPriceRows = oModel.getDiscountPrices();
			if (null != discountPriceRows)
			{
				priceRowList.addAll(discountPriceRows);
			}

			if (priceRowList.size() > 0 && null != priceRowList.get(0).getCurrency()
					&& null != priceRowList.get(0).getCurrency().getIsocode() && null != priceRowList.get(0).getPrice())
			{
				final PriceData discountPrice = createPrice(cart, priceRowList.get(0).getPrice());
				promoData.setDiscountPrice(discountPrice);
			}

		}

		if (null != oModel.getMessageFired())
		{
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(oModel.getMessageFired());
			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
			}
		}

		if (null != oModel.getMessageCouldHaveFired())
		{
			final FiredPromoData firedPromo = new FiredPromoData();
			firedPromo.setPromoMessage(oModel.getMessageFired());
			if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
			{
				promoData.setFiredPromotion(firedPromo);
			}
		}
		return promoData;
	}

	/**
	 * @Description : For Buy X Items of product A get product B for free
	 * @param productPromotion
	 * @param cart
	 * @return responseData
	 */
	private MplPromotionData getBuyXItemsofproductAgetproductBforfreeData(final ProductPromotionModel productPromotion,
			final CartModel cart)
	{
		final MplPromotionData promoData = new MplPromotionData();
		final BuyXItemsofproductAgetproductBforfreeModel oModel = (BuyXItemsofproductAgetproductBforfreeModel) productPromotion;

		promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.PRODUCT_PROMO);

		if (null != oModel.getMessageFired())
		{
			final PotentialPromoData potentialPromo = new PotentialPromoData();
			potentialPromo.setPromoMessage(oModel.getMessageFired());
			if (StringUtils.isNotEmpty(potentialPromo.getPromoMessage()))
			{
				promoData.setPotentialPromotion(potentialPromo);
			}
		}

		if (null != oModel.getMessageCouldHaveFired())
		{
			final FiredPromoData firedPromo = new FiredPromoData();
			firedPromo.setPromoMessage(oModel.getMessageFired());
			if (StringUtils.isNotEmpty(firedPromo.getPromoMessage()))
			{
				promoData.setFiredPromotion(firedPromo);
			}
		}
		return promoData;
	}

	///////////////////////////

	/**
	 * @Description : To Calculate Discount value
	 * @param cart
	 * @return discount
	 */
	private Double calculateDiscount(final CartModel cart)
	{
		double discount = 0.0d;
		double totalPrice = 0.0D;
		if (null != cart && null != cart.getEntries() && !cart.getEntries().isEmpty())
		{
			for (final AbstractOrderEntryModel entry : cart.getEntries())
			{
				totalPrice = totalPrice + (entry.getBasePrice().doubleValue() * entry.getQuantity().doubleValue());
			}

			discount = (totalPrice + cart.getDeliveryCost().doubleValue()) - cart.getTotalPriceWithConv().doubleValue();
		}
		return roundData(discount);
	}

	/**
	 * @Description : Round Discount value post calculation
	 * @param discount
	 * @return value
	 */
	private Double roundData(final double discount)
	{
		Double value = Double.valueOf(0);
		if (discount > 0.0D)
		{
			value = Double.valueOf(Math.round(discount * 100.0) / 100.0);
		}
		return value;
	}


	/**
	 * @description: It is creating price data for a price value
	 * @param source
	 * @param val
	 *
	 * @return PriceData
	 */
	public PriceData createPrice(final AbstractOrderModel source, final Double val)
	{
		if (source == null)
		{
			throw new IllegalArgumentException("source order must not be null");
		}

		final CurrencyModel currency = source.getCurrency();
		if (currency == null)
		{
			throw new IllegalArgumentException("source order currency must not be null");
		}

		// Get double value, handle null as zero
		final double priceValue = val != null ? val.doubleValue() : 0d;

		return priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(priceValue), currency);
	}


	/**
	 * @Description
	 * @param cartData
	 * @return promoPriceData
	 */
	public MplPromotionData populateNonPromoData(final CartData cartData)
	{
		final MplPromotionData promoData = new MplPromotionData();
		if (null != cartData && null != cartData.getTotalDiscounts())
		{
			promoData.setDiscountPrice(cartData.getTotalDiscounts());
			promoData.setPromoTypeIdentifier(MarketplacecommerceservicesConstants.OTHER_PROMO);
		}

		return promoData;
	}





	/**
	 * @return the priceDataFactory
	 */
	public PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}





	/**
	 * @param priceDataFactory
	 *           the priceDataFactory to set
	 */
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}



}
