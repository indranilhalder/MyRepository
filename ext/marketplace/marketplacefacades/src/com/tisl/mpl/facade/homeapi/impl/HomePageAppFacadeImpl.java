/**
 *
 */
package com.tisl.mpl.facade.homeapi.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanToPropertyValueTransformer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.facade.homeapi.HomePageAppFacade;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;
import com.tisl.mpl.service.HomePageAppService;
import com.tisl.mpl.wsdto.ComponentRequestDTO;
import com.tisl.mpl.wsdto.HomeProductsDTO;
import com.tisl.mpl.wsdto.ThemeOffersDTO;
import com.tisl.mpl.wsdto.ThemeOffersJSONDTO;
import com.tisl.mpl.wsdto.ThemeOffersRequestDTO;


/**
 * @author TCS
 *
 */
public class HomePageAppFacadeImpl implements HomePageAppFacade
{
	private static final Logger LOG = Logger.getLogger(HomePageAppFacadeImpl.class);

	@Resource(name = "homePageAppService")
	private HomePageAppService homePageAppService;

	private BuyBoxService buyBoxService;

	/**
	 * @return the buyBoxService
	 */
	public BuyBoxService getBuyBoxService()
	{
		return buyBoxService;
	}

	/**
	 * @param buyBoxService
	 *           the buyBoxService to set
	 */
	public void setBuyBoxService(final BuyBoxService buyBoxService)
	{
		this.buyBoxService = buyBoxService;
	}

	/**
	 * @return the homePageAppService
	 */
	public HomePageAppService getHomePageAppService()
	{
		return homePageAppService;
	}

	/**
	 * @param homePageAppService
	 *           the homePageAppService to set
	 */
	public void setHomePageAppService(final HomePageAppService homePageAppService)
	{
		this.homePageAppService = homePageAppService;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.homeapi.HomePageAppFacade#getAdobeTargetData()
	 */
	@Override
	public void getAdobeTargetDataOfferWidget(final ComponentRequestDTO ComponentRequestDTO)
	{
		// YTODO Auto-generated method stub
		final String st = homePageAppService.getAdobeTargetDataOfferWidget(ComponentRequestDTO);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.facade.homeapi.HomePageAppFacade#getThemeOffersComponentDTO(com.tisl.mpl.wsdto.ThemeOffersRequestDTO,
	 * com.tisl.mpl.wsdto.ThemeOffersJSONDTO)
	 */
	@Override
	public ThemeOffersDTO getThemeOffersComponentDTO(final ThemeOffersRequestDTO themeOffersRequestDTO,
			final String themeOffersJSONString)
	{
		final ObjectMapper mapper = new ObjectMapper();
		List<HomeProductsDTO> finalProductsDTO = new ArrayList<HomeProductsDTO>();
		final ThemeOffersDTO finalThemeOffersDTO = new ThemeOffersDTO();
		ThemeOffersJSONDTO themeOffersJSONDTO = null;
		try
		{
			final List<String> productCodes = new ArrayList<String>();
			List<BuyBoxModel> buyBoxModelList = new ArrayList<BuyBoxModel>();
			themeOffersJSONDTO = mapper.readValue(themeOffersJSONString, ThemeOffersJSONDTO.class);
			final List<HomeProductsDTO> productsdto = themeOffersJSONDTO.getItemIds();
			for (final HomeProductsDTO productdto : productsdto)
			{
				if (StringUtils.isNotEmpty(productdto.getPrdId()))
				{
					productCodes.add(productdto.getPrdId().toUpperCase());
				}
			}

			if (CollectionUtils.isNotEmpty(productCodes))
			{
				final String commaSepartedProductCodes = StringUtils.join(productCodes, ",");
				buyBoxModelList = buyBoxService.buyboxPrice(commaSepartedProductCodes);
			}

			if (CollectionUtils.isNotEmpty(buyBoxModelList))
			{
				finalProductsDTO = this.compareDataWithBuyBox(themeOffersJSONDTO.getItemIds(), buyBoxModelList);

			}
			finalThemeOffersDTO.setOffers(themeOffersJSONDTO.getItems());
			finalThemeOffersDTO.setProducts(finalProductsDTO);
		}
		catch (final IOException | ClassCastException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			LOG.error("HomePageAppFacadeImpl error occured:: " + e.getMessage());
		}

		return finalThemeOffersDTO;
	}

	/**
	 * @param themeOffersJSONDTO
	 * @param buyBoxModelList
	 * @return
	 */
	private List<HomeProductsDTO> compareDataWithBuyBox(final List<HomeProductsDTO> productsdto,
			final List<BuyBoxModel> buyBoxModelList) throws ArrayIndexOutOfBoundsException, ClassCastException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		// YTODO Auto-generated method stub
		final List<HomeProductsDTO> finalProductsDTO = new ArrayList<HomeProductsDTO>();
		HomeProductsDTO productdto = null;
		final List<String> listingIds = (List<String>) CollectionUtils.collect(productsdto, new BeanToPropertyValueTransformer(
				"prdId"));
		for (final BuyBoxModel buyBoxModel : buyBoxModelList)
		{
			final String buyBoxListingIdLwrCase = buyBoxModel.getProduct().toLowerCase();
			if (listingIds.contains(buyBoxListingIdLwrCase))
			{
				productdto = (HomeProductsDTO) CollectionUtils.find(productsdto, new Predicate()
				{
					@Override
					public boolean evaluate(final Object obj)
					{
						final HomeProductsDTO finder = (HomeProductsDTO) obj;
						return finder.getPrdId().equals(buyBoxListingIdLwrCase);
					}
				});

				if (buyBoxModel.getAvailable().intValue() > 0)
				{
					finalProductsDTO.add(productdto);
				}
				else
				{
					continue;
				}

				if (buyBoxModel.getSpecialPrice() != null && Double.compare(buyBoxModel.getSpecialPrice().doubleValue(), 0.0) > 0)
				{
					productdto.getDiscountedPrice().setDoubleValue(buyBoxModel.getSpecialPrice());
					productdto.getDiscountedPrice().setFormattedValue(buyBoxModel.getSpecialPrice().toString());
				}
				if (buyBoxModel.getMrp() != null && Double.compare(buyBoxModel.getMrp().doubleValue(), 0.0) > 0)
				{
					productdto.getMrpPrice().setDoubleValue(buyBoxModel.getMrp());
					productdto.getMrpPrice().setFormattedValue(buyBoxModel.getMrp().toString());
				}


			}

		}
		return finalProductsDTO;
	}
}
