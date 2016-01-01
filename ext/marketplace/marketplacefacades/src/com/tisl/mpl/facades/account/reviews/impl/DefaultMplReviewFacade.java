/**
 *
 */
package com.tisl.mpl.facades.account.reviews.impl;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.reviews.MplReviewFacade;
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



}
