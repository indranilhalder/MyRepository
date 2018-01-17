/**
 *
 */
package com.tisl.mpl.facade.homeapi.impl;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanToPropertyValueTransformer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.homeapi.HomePageAppFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;
import com.tisl.mpl.service.HomePageAppService;
import com.tisl.mpl.wsdto.AutomatedBrandProductCarouselDTO;
import com.tisl.mpl.wsdto.AutomatedBrandProductCarouselJSONDTO;
import com.tisl.mpl.wsdto.BannersCarouselDTO;
import com.tisl.mpl.wsdto.BannersCarouselJSONDTO;
import com.tisl.mpl.wsdto.ComponentRequestDTO;
import com.tisl.mpl.wsdto.HomeProductsDTO;
import com.tisl.mpl.wsdto.HomepageComponentRequestDTO;
import com.tisl.mpl.wsdto.ThemeOffersDTO;
import com.tisl.mpl.wsdto.ThemeOffersJSONDTO;
import com.tisl.mpl.wsdto.VideoProductCaraouselDTO;
import com.tisl.mpl.wsdto.VideoProductJSONDTO;


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

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;

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
	public ThemeOffersDTO getThemeOffersComponentDTO(final HomepageComponentRequestDTO themeOffersRequestDTO,
			final String themeOffersJSONString) throws EtailNonBusinessExceptions
	{
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<HomeProductsDTO> finalProductsDTO = new ArrayList<HomeProductsDTO>();
		final ThemeOffersDTO finalThemeOffersDTO = new ThemeOffersDTO();
		ThemeOffersJSONDTO themeOffersJSONDTO = null;
		try
		{
			final List<String> productCodes = new ArrayList<String>();
			List<BuyBoxModel> buyBoxModelList = new ArrayList<BuyBoxModel>();
			themeOffersJSONDTO = mapper.readValue(themeOffersJSONString, ThemeOffersJSONDTO.class);
			//			final JsonFactory factory = mapper.getFactory();
			//			final JsonParser parser = factory.createParser(themeOffersJSONString);
			//			final JsonNode actualObj = mapper.readTree(parser);
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
			finalThemeOffersDTO.setItems(finalProductsDTO);
		}
		catch (final IOException | ClassCastException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			LOG.error("HomePageAppFacadeImpl error occured:: " + e.getMessage());
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}

		return finalThemeOffersDTO;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.homeapi.HomePageAppFacade#getBannerProductCarouselDTO(com.tisl.mpl.facade.homeapi.
	 * HomepageComponentRequestDTO, java.lang.String)
	 */
	@Override
	public BannersCarouselDTO getBannerProductCarouselDTO(final HomepageComponentRequestDTO bannerCarouselRequestDTO,
			final String bannerProductcarouselJsonString) throws EtailNonBusinessExceptions
	{
		// YTODO Auto-generated method stub
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<HomeProductsDTO> finalProductsDTO = new ArrayList<HomeProductsDTO>();
		final BannersCarouselDTO finalBannersCarouselDTO = new BannersCarouselDTO();
		BannersCarouselJSONDTO bannersCarouselJSONDTO = null;

		try
		{
			final List<String> productCodes = new ArrayList<String>();
			List<BuyBoxModel> buyBoxModelList = new ArrayList<BuyBoxModel>();
			bannersCarouselJSONDTO = mapper.readValue(bannerProductcarouselJsonString, BannersCarouselJSONDTO.class);
			final List<HomeProductsDTO> productsdto = bannersCarouselJSONDTO.getItems();
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
				finalProductsDTO = this.compareDataWithBuyBox(bannersCarouselJSONDTO.getItems(), buyBoxModelList);

			}
			finalBannersCarouselDTO.setTitle(bannersCarouselJSONDTO.getTitle());
			finalBannersCarouselDTO.setDescription(bannersCarouselJSONDTO.getDescription());
			finalBannersCarouselDTO.setImageURL(bannersCarouselJSONDTO.getImageURL());
			finalBannersCarouselDTO.setItems(finalProductsDTO);
		}
		catch (final IOException | ClassCastException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			LOG.error("HomePageAppFacadeImpl error occured:: " + e.getMessage());
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}

		return finalBannersCarouselDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.homeapi.HomePageAppFacade#getVideoProductCarouselDTO(com.tisl.mpl.facade.homeapi.
	 * HomepageComponentRequestDTO, java.lang.String)
	 */
	@Override
	public VideoProductCaraouselDTO getVideoProductCarouselDTO(final HomepageComponentRequestDTO videoProductCaraouselRequestDTO,
			final String videoProductcarouselJsonString) throws EtailNonBusinessExceptions
	{
		// YTODO Auto-generated method stub
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<HomeProductsDTO> finalProductsDTO = new ArrayList<HomeProductsDTO>();
		final VideoProductCaraouselDTO finalVideoProductCaraouselDTO = new VideoProductCaraouselDTO();
		VideoProductJSONDTO videoProductJSONDTO = null;

		try
		{
			final List<String> productCodes = new ArrayList<String>();
			List<BuyBoxModel> buyBoxModelList = new ArrayList<BuyBoxModel>();
			videoProductJSONDTO = mapper.readValue(videoProductcarouselJsonString, VideoProductJSONDTO.class);
			final List<HomeProductsDTO> productsdto = videoProductJSONDTO.getItems();
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
				finalProductsDTO = this.compareDataWithBuyBox(videoProductJSONDTO.getItems(), buyBoxModelList);

			}
			finalVideoProductCaraouselDTO.setTitle(videoProductJSONDTO.getTitle());
			finalVideoProductCaraouselDTO.setDescription(videoProductJSONDTO.getDescription());
			finalVideoProductCaraouselDTO.setImageURL(videoProductJSONDTO.getImageURL());
			finalVideoProductCaraouselDTO.setVideoURL(videoProductJSONDTO.getVideoURL());
			finalVideoProductCaraouselDTO.setItems(finalProductsDTO);
		}
		catch (final IOException | ClassCastException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			LOG.error("HomePageAppFacadeImpl error occured:: " + e.getMessage());
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}

		return finalVideoProductCaraouselDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.homeapi.HomePageAppFacade#getautomatedBrandCarouselDTO(com.tisl.mpl.facade.homeapi.
	 * HomepageComponentRequestDTO, java.lang.String)
	 */
	@Override
	public AutomatedBrandProductCarouselDTO getautomatedBrandCarouselDTO(
			final HomepageComponentRequestDTO automatedBrandRequestDTO, final String automatedBrandCaraouselJsonString)
			throws EtailNonBusinessExceptions
	{
		// YTODO Auto-generated method stub
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<HomeProductsDTO> finalProductsDTO = new ArrayList<HomeProductsDTO>();
		final AutomatedBrandProductCarouselDTO finalAutomatedBrandProductCarouselDTO = new AutomatedBrandProductCarouselDTO();
		AutomatedBrandProductCarouselJSONDTO automatedBrandProductCarouselJSONDTO = null;

		try
		{
			final List<String> productCodes = new ArrayList<String>();
			List<BuyBoxModel> buyBoxModelList = new ArrayList<BuyBoxModel>();
			automatedBrandProductCarouselJSONDTO = mapper.readValue(automatedBrandCaraouselJsonString,
					AutomatedBrandProductCarouselJSONDTO.class);
			final List<HomeProductsDTO> productsdto = automatedBrandProductCarouselJSONDTO.getItems();
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
				finalProductsDTO = this.compareDataWithBuyBox(automatedBrandProductCarouselJSONDTO.getItems(), buyBoxModelList);

			}
			finalAutomatedBrandProductCarouselDTO.setBrandLogo(automatedBrandProductCarouselJSONDTO.getBrandLogo());
			finalAutomatedBrandProductCarouselDTO.setDescription(automatedBrandProductCarouselJSONDTO.getDescription());
			finalAutomatedBrandProductCarouselDTO.setImageURL(automatedBrandProductCarouselJSONDTO.getImageURL());
			finalAutomatedBrandProductCarouselDTO.setItems(finalProductsDTO);
		}
		catch (final IOException | ClassCastException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			LOG.error("HomePageAppFacadeImpl error occured:: " + e.getMessage());
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}

		return finalAutomatedBrandProductCarouselDTO;
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
		final CurrencyModel currency = commonI18NService.getCurrency(MarketplaceFacadesConstants.INR);
		final String currencySymbol = currency.getSymbol();
		final String currencyIso = currency.getIsocode();
		final DecimalFormat df = new DecimalFormat("###,###.##");
		for (final BuyBoxModel buyBoxModel : buyBoxModelList)
		{
			//final String buyBoxListingIdLwrCase = buyBoxModel.getProduct().toLowerCase();
			final String buyBoxListingId = buyBoxModel.getProduct();
			if (listingIds.contains(buyBoxListingId))
			{
				productdto = (HomeProductsDTO) CollectionUtils.find(productsdto, new Predicate()
				{
					@Override
					public boolean evaluate(final Object obj)
					{
						final HomeProductsDTO finder = (HomeProductsDTO) obj;
						return finder.getPrdId().equals(buyBoxListingId);
					}
				});

				if (buyBoxModel.getSpecialPrice() != null && Double.compare(buyBoxModel.getSpecialPrice().doubleValue(), 0.0) >= 0)
				{
					final PriceData discountedPrice = new PriceData();
					discountedPrice.setDoubleValue(buyBoxModel.getSpecialPrice());
					discountedPrice.setFormattedValue(df.format(buyBoxModel.getSpecialPrice()));
					discountedPrice.setCurrencyIso(currencyIso);
					discountedPrice.setCurrencySymbol(currencySymbol);

					productdto.setDiscountedPrice(discountedPrice);
				}
				if (buyBoxModel.getMrp() != null && Double.compare(buyBoxModel.getMrp().doubleValue(), 0.0) > 0)
				{
					final PriceData mrpPrice = new PriceData();
					mrpPrice.setDoubleValue(buyBoxModel.getMrp());
					mrpPrice.setFormattedValue(df.format(buyBoxModel.getMrp()));
					mrpPrice.setCurrencyIso(currencyIso);
					mrpPrice.setCurrencySymbol(currencySymbol);

					productdto.setMrpPrice(mrpPrice);
				}

				finalProductsDTO.add(productdto);
			}

		}
		return finalProductsDTO;
	}
}
