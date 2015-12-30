/**
 * TCS
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.PromotionData;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.jalo.AbstractPromotion;
import de.hybris.platform.promotions.jalo.ProductPromotion;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.AbstractPromotionRestrictionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.result.PromotionOrderResults;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.tisl.mpl.model.BuyABFreePrecentageDiscountModel;
import com.tisl.mpl.model.BuyAandBgetCModel;
import com.tisl.mpl.model.BuyXItemsofproductAgetproductBforfreeModel;
import com.tisl.mpl.model.EtailSellerSpecificRestrictionModel;
import com.tisl.mpl.model.SellerMasterModel;


/**
 * Converter implementation for {@link de.hybris.platform.promotions.model.AbstractPromotionModel} as source and
 * {@link de.hybris.platform.commercefacades.product.data.PromotionData} as target type.
 */

public class CustomPromotionsPopulator implements Populator<AbstractPromotionModel, PromotionData>
{
	private CartService cartService;
	private ModelService modelService;
	private PromotionsService promotionService;

	private Converter<ProductModel, ProductData> productConverter;

	@Required
	public void setProductConverter(final Converter<ProductModel, ProductData> productConverter)
	{
		this.productConverter = productConverter;
	}

	protected Converter<ProductModel, ProductData> getProductConverter()
	{
		return productConverter;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setPromotionService(final PromotionsService promotionService)
	{
		this.promotionService = promotionService;
	}

	protected PromotionsService getPromotionService()
	{
		return promotionService;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	protected CartService getCartService()
	{
		return cartService;
	}

	/**
	 * Converter implementation for {@link de.hybris.platform.promotions.model.AbstractPromotionModel} as source and
	 * {@link de.hybris.platform.commercefacades.product.data.PromotionData} as target type.
	 *
	 * @param source
	 * @param target
	 */
	@Override
	public void populate(final AbstractPromotionModel source, final PromotionData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setCode(source.getCode());
		target.setEndDate(source.getEndDate());
		target.setDescription(source.getDescription());
		target.setPromotionType(source.getPromotionType());
		//ProductDetailForProductCode Mobile Service
		target.setPriority(source.getPriority());
		target.setStartDate(source.getStartDate());
		target.setEnabled(source.getEnabled());
		target.setTitle(source.getTitle());
		final List<String> channelList = new ArrayList<String>();
		for (final SalesApplication channel : source.getChannel())
		{
			channelList.add(channel.getCode());
		}
		target.setChannels(channelList);

		//setting sellers from SellerRestriction
		if (source instanceof ProductPromotionModel && null != source.getRestrictions() && !(source.getRestrictions().isEmpty()))
		{
			//	if (null != source.getRestrictions() && !source.getRestrictions().isEmpty())
			//	{
			///logic moved to private method for sonar critical fixes
			setAllowedSellers(source, target);
			/*
			 * for (final AbstractPromotionRestrictionModel restriction : source.getRestrictions()) { if (restriction
			 * instanceof EtailSellerSpecificRestrictionModel) { final List<String> allowedSellerList = new
			 * ArrayList<String>(); final EtailSellerSpecificRestrictionModel sellerRestriction =
			 * (EtailSellerSpecificRestrictionModel) restriction; if (null != sellerRestriction.getSellerMasterList() &&
			 * !sellerRestriction.getSellerMasterList().isEmpty()) { final List<SellerMasterModel> sellerList =
			 * sellerRestriction.getSellerMasterList(); for (final SellerMasterModel seller : sellerList) {
			 * allowedSellerList.add(seller.getId()); } //setting allowed seller list
			 * target.setAllowedSellers(allowedSellerList); }
			 *
			 * } }
			 */
			//}
		}

		//add free gifts products into PromotionData FOR Buy x items of product A get product B for free
		if (source instanceof BuyXItemsofproductAgetproductBforfreeModel)
		{
			final BuyXItemsofproductAgetproductBforfreeModel promotion = (BuyXItemsofproductAgetproductBforfreeModel) source;
			final List<ProductModel> giftProducts = new ArrayList<ProductModel>(promotion.getGiftProducts());
			final List<ProductData> productDataList = new ArrayList<ProductData>();
			for (final ProductModel product : giftProducts)
			{
				productDataList.add(productConverter.convert(product));
			}
			target.setGiftProduct(productDataList);
		}
		//add free gifts products into PromotionData FOR Buy A and get B Free with % or Amount Discount
		if (source instanceof BuyABFreePrecentageDiscountModel)
		{
			final BuyABFreePrecentageDiscountModel promotion = (BuyABFreePrecentageDiscountModel) source;
			final List<ProductModel> giftProducts = new ArrayList<ProductModel>(promotion.getGiftProducts());
			final List<ProductData> productDataList = new ArrayList<ProductData>();
			for (final ProductModel product : giftProducts)
			{
				productDataList.add(productConverter.convert(product));
			}
			target.setGiftProduct(productDataList);
		}
		//add free gifts products into PromotionData FOR Buy A and B get C Free
		if (source instanceof BuyAandBgetCModel)
		{
			final BuyAandBgetCModel promotion = (BuyAandBgetCModel) source;
			final List<ProductModel> giftProducts = new ArrayList<ProductModel>(promotion.getGiftProducts());
			final List<ProductData> productDataList = new ArrayList<ProductData>();
			for (final ProductModel product : giftProducts)
			{
				productDataList.add(productConverter.convert(product));
			}
			target.setGiftProduct(productDataList);
		}
		processPromotionMessages(source, target);
	}

	/**
	 * @param source
	 * @param target
	 */
	private PromotionData setAllowedSellers(final AbstractPromotionModel source, final PromotionData target)
	{
		for (final AbstractPromotionRestrictionModel restriction : source.getRestrictions())
		{
			if (restriction instanceof EtailSellerSpecificRestrictionModel)
			{
				final List<String> allowedSellerList = new ArrayList<String>();
				final EtailSellerSpecificRestrictionModel sellerRestriction = (EtailSellerSpecificRestrictionModel) restriction;
				if (null != sellerRestriction.getSellerMasterList() && !sellerRestriction.getSellerMasterList().isEmpty())
				{
					final List<SellerMasterModel> sellerList = sellerRestriction.getSellerMasterList();
					for (final SellerMasterModel seller : sellerList)
					{
						allowedSellerList.add(seller.getId());
					}
					//setting allowed seller list
					target.setAllowedSellers(allowedSellerList);
				}

			}
		}
		return target;

	}

	protected void processPromotionMessages(final AbstractPromotionModel source, final PromotionData prototype)
	{
		if (getCartService().hasSessionCart())
		{
			final CartModel cartModel = getCartService().getSessionCart();
			
				final AbstractPromotion promotion = getModelService().getSource(source);

				final PromotionOrderResults promoOrderResults = getPromotionService().getPromotionResults(cartModel);
				if (promoOrderResults != null)
				{
					prototype.setCouldFireMessages(getCouldFirePromotionsMessages(promoOrderResults, promotion));
					prototype.setFiredMessages(getFiredPromotionsMessages(promoOrderResults, promotion));
				}
			}
		}
	

	protected List<String> getCouldFirePromotionsMessages(final PromotionOrderResults promoOrderResults,
			final AbstractPromotion promotion)
	{
		final List<String> descriptions = new LinkedList<String>();

		if (promotion instanceof ProductPromotion)
		{
			addDescriptions(descriptions, filter(promoOrderResults.getPotentialProductPromotions(), promotion));
		}
		else
		{
			addDescriptions(descriptions, filter(promoOrderResults.getPotentialOrderPromotions(), promotion));

		}
		return descriptions;
	}

	protected List<String> getFiredPromotionsMessages(final PromotionOrderResults promoOrderResults,
			final AbstractPromotion promotion)
	{
		final List<String> descriptions = new LinkedList<String>();

		if (promotion instanceof ProductPromotion)
		{
			addDescriptions(descriptions, filter(promoOrderResults.getFiredProductPromotions(), promotion));
		}
		else
		{
			addDescriptions(descriptions, filter(promoOrderResults.getFiredOrderPromotions(), promotion));
		}

		return descriptions;
	}

	protected void addDescriptions(final List<String> descriptions, final List<PromotionResult> promotionResults)
	{
		if (promotionResults != null)
		{
			for (final PromotionResult promoResult : promotionResults)
			{
				descriptions.add(promoResult.getDescription());
			}
		}
	}

	protected List<PromotionResult> filter(final List<PromotionResult> results, final AbstractPromotion promotion)
	{
		final List<PromotionResult> filteredResults = new LinkedList<PromotionResult>();
		for (final PromotionResult result : results)
		{
			if (result.getPromotion().equals(promotion))
			{
				filteredResults.add(result);
			}
		}
		return filteredResults;
	}
}
