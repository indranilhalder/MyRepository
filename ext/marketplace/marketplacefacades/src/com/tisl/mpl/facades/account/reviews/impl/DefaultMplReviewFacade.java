/**
 *
 */
package com.tisl.mpl.facades.account.reviews.impl;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.reviews.MplReviewFacade;
import com.tisl.mpl.facades.order.impl.MplDefaultPriceDataFactory;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.wsdto.GigyaProductReviewWsDTO;


/**
 * @author TCS
 *
 */
public class DefaultMplReviewFacade implements MplReviewFacade
{
	private static final Logger LOG = Logger.getLogger(DefaultMplReviewFacade.class);
	@Autowired
	private BuyBoxFacade buyBoxFacade;

	@Autowired
	private MplDefaultPriceDataFactory priceDataFactory;


	/*
	 * @Desc fetching ProductData from DTOlist
	 *
	 * @param commentsWithProductData
	 *
	 * @return GigyaProductReviewWsDTO
	 */
	@Override
	public List<GigyaProductReviewWsDTO> getReviewedProductPrice(final List<GigyaProductReviewWsDTO> commentsWithProductData)
	{
		ProductData productData = null;
		PriceData priceFinal = null;
		final List<GigyaProductReviewWsDTO> modifiedDTOList = new ArrayList<GigyaProductReviewWsDTO>();

		for (final GigyaProductReviewWsDTO list : commentsWithProductData)
		{
			try
			{
				productData = list.getProductData();
				final BuyBoxData productPrice = buyBoxFacade.buyboxPrice(productData.getCode());
				PriceData price = null;
				final Double zeroValue = new Double(0.0);
				if (productPrice.getSpecialPrice() != null && productPrice.getSpecialPrice().getDoubleValue() != zeroValue)
				{
					price = productPrice.getSpecialPrice();
				}
				else if (null != productPrice.getPrice() && productPrice.getPrice().getDoubleValue() != zeroValue)
				{
					price = productPrice.getPrice();
				}
				else
				{
					price = productPrice.getMrp();
				}

				priceFinal = priceDataFactory.create(price.getPriceType(), price.getValue(), price.getCurrencyIso());
				LOG.debug("price :" + priceFinal);
				productData.setProductMOP(priceFinal);

				list.setProductData(productData);
				modifiedDTOList.add(list);
			}
			catch (final Exception ex)
			{
				LOG.error(MarketplacecommerceservicesConstants.E0000 + ex.getMessage());
				list.setProductData(productData);
				modifiedDTOList.add(list);
			}
		}

		return modifiedDTOList;
	}

	/*
	 * @Desc checking product price present in order or Review comments
	 *
	 * @param commentsWithProductData,orderModels
	 *
	 * @return GigyaProductReviewWsDTO
	 */
	@Override
	public List<GigyaProductReviewWsDTO> getProductPrice(final List<GigyaProductReviewWsDTO> commentsWithProductData,
			final List<OrderModel> orderModels)
	{

		final List<GigyaProductReviewWsDTO> orderedProductDtoList = new ArrayList<GigyaProductReviewWsDTO>();
		List<GigyaProductReviewWsDTO> reviewedProductPriceWsDTOList = new ArrayList<GigyaProductReviewWsDTO>();
		PriceData price = null;
		String productCode = "";
		String listingId = "";
		/* fetching ordered product price */
		try
		{
			for (final OrderModel order : orderModels)
			{
				for (final OrderModel sellerOrder : order.getChildOrders())
				{
					for (final AbstractOrderEntryModel entry : sellerOrder.getEntries())
					{
						/* comparing products with code present in both order and in reviewComment section */
						for (final GigyaProductReviewWsDTO commentedProduct : commentsWithProductData)
						{
							productCode = entry.getProduct().getCode();
							listingId = commentedProduct.getProductData().getListingId();
							if (listingId != null)
							{
								if (productCode.equalsIgnoreCase(listingId))
								{

									final Double netPrice = entry.getNetAmountAfterAllDisc();
									price = createPrice(order, netPrice);
									LOG.debug("ordered product price>>>>> " + price);
									ProductData productData = new ProductData();
									productData = commentedProduct.getProductData();
									productData.setProductMOP(price);
									commentedProduct.setProductData(productData);
									if (!orderedProductDtoList.contains(commentedProduct))
									{
										orderedProductDtoList.add(commentedProduct);
									}

									break;
								}

							}
						}


					}
				}
			}
			commentsWithProductData.removeAll(orderedProductDtoList);
			reviewedProductPriceWsDTOList = getReviewedProductPrice(commentsWithProductData);
			if (!CollectionUtils.isEmpty(reviewedProductPriceWsDTOList))
			{
				orderedProductDtoList.addAll(reviewedProductPriceWsDTOList);
			}
			return orderedProductDtoList;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);

		}
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
}
