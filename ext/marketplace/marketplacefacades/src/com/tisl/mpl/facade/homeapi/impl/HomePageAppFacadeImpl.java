/**
 *
 */
package com.tisl.mpl.facade.homeapi.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.facade.homeapi.HomePageAppFacade;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;
import com.tisl.mpl.service.HomePageAppService;
import com.tisl.mpl.wsdto.ComponentRequestDTO;
import com.tisl.mpl.wsdto.ThemeOffersDTO;
import com.tisl.mpl.wsdto.ThemeOffersJSONDTO;
import com.tisl.mpl.wsdto.ThemeOffersRequestDTO;
import com.tisl.mpl.wsdto.productsDTO;


/**
 * @author TCS
 *
 */
public class HomePageAppFacadeImpl implements HomePageAppFacade
{
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
	 * @see
	 * com.tisl.mpl.facade.homeapi.HomePageAppFacade#getThemeOffersComponentDTO(com.tisl.mpl.wsdto.ThemeOffersRequestDTO,
	 * com.tisl.mpl.wsdto.ThemeOffersJSONDTO)
	 */
	@Override
	public ThemeOffersDTO getThemeOffersComponentDTO(final ThemeOffersRequestDTO themeOffersRequestDTO,
			final String themeOffersJSONString)
	{
		final ObjectMapper mapper = new ObjectMapper();

		try
		{
			final List<String> productCodes = new ArrayList<String>();
			List<BuyBoxModel> buyBoxModelList = new ArrayList<BuyBoxModel>();
			final ThemeOffersJSONDTO themeOffersJSONDTO = mapper.readValue(themeOffersJSONString, ThemeOffersJSONDTO.class);
			final List<productsDTO> productsdTO = themeOffersJSONDTO.getItemIds();
			for (final productsDTO productdto : productsdTO)
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

		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
