/**
 *
 */
package com.tisl.mpl.facades.account.reviews.impl;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.reviews.MplReviewFacade;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.helper.ProductDetailsHelper;
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
	private ProductDetailsHelper productDetailsHelper;
	@Autowired
	private ProductFacade productFacade;

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
		final List<GigyaProductReviewWsDTO> modifiedDTOList = new ArrayList<GigyaProductReviewWsDTO>();
		try
		{

			for (final GigyaProductReviewWsDTO list : commentsWithProductData)
			{
				productData = list.getProductData();
				final BuyBoxData productPrice = buyBoxFacade.buyboxPrice(productData.getCode());
				PriceData price = null;
				if (productPrice.getSpecialPrice() != null)
				{
					price = productPrice.getSpecialPrice();
				}
				else
				{
					price = productPrice.getPrice();
				}
				final String priceFinal = price.getFormattedValue();
				LOG.debug("price :" + priceFinal);
				productData.setProductMOP(price);
				list.setProductData(productData);
				modifiedDTOList.add(list);
			}
			return modifiedDTOList;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}

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
									price = productDetailsHelper.formPriceData(netPrice);
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
			if (!CollectionUtils.isEmpty(getReviewedProductPrice(commentsWithProductData)))
			{
				orderedProductDtoList.addAll(getReviewedProductPrice(commentsWithProductData));
			}
			return orderedProductDtoList;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);

		}
	}
}
