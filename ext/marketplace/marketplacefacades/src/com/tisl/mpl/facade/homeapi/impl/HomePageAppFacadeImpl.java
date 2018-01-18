/**
 *
 */
package com.tisl.mpl.facade.homeapi.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.homeapi.HomePageAppFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;
import com.tisl.mpl.marketplacecommerceservices.services.product.MplProductService;
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

	//	private Converter<ProductModel, ProductData> customProductConverter;
	//
	//	private ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator;

	//private ProductFacade productFacade;

	@Resource(name = "productService")
	private MplProductService mplProductService;

	@Autowired
	private CatalogVersionService catalogVersionService;

	private String mediaFormat;

	private Converter<ProductModel, ProductData> productConverter;

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
	 * @return the commonI18NService
	 */
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	/**
	 * @param commonI18NService
	 *           the commonI18NService to set
	 */
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	/**
	 * @return the customProductConverter
	 */
	//	public Converter<ProductModel, ProductData> getCustomProductConverter()
	//	{
	//		return customProductConverter;
	//	}
	//
	//	/**
	//	 * @param customProductConverter
	//	 *           the customProductConverter to set
	//	 */
	//	public void setCustomProductConverter(final Converter<ProductModel, ProductData> customProductConverter)
	//	{
	//		this.customProductConverter = customProductConverter;
	//	}
	//
	//	/**
	//	 * @return the productConfiguredPopulator
	//	 */
	//	public ConfigurablePopulator<ProductModel, ProductData, ProductOption> getProductConfiguredPopulator()
	//	{
	//		return productConfiguredPopulator;
	//	}
	//
	//	/**
	//	 * @param productConfiguredPopulator
	//	 *           the productConfiguredPopulator to set
	//	 */
	//	public void setProductConfiguredPopulator(
	//			final ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator)
	//	{
	//		this.productConfiguredPopulator = productConfiguredPopulator;
	//	}

	/**
	 * @return the mediaFormat
	 */
	public String getMediaFormat()
	{
		return mediaFormat;
	}

	/**
	 * @param mediaFormat
	 *           the mediaFormat to set
	 */
	public void setMediaFormat(final String mediaFormat)
	{
		this.mediaFormat = mediaFormat;
	}

	/**
	 * @return the productConverter
	 */
	public Converter<ProductModel, ProductData> getProductConverter()
	{
		return productConverter;
	}

	/**
	 * @param productConverter
	 *           the productConverter to set
	 */
	public void setProductConverter(final Converter<ProductModel, ProductData> productConverter)
	{
		this.productConverter = productConverter;
	}

	//	/**
	//	 * @return the productFacade
	//	 */
	//	public ProductFacade getProductFacade()
	//	{
	//		return productFacade;
	//	}
	//
	//	/**
	//	 * @param productFacade
	//	 *           the productFacade to set
	//	 */
	//	public void setProductFacade(final ProductFacade productFacade)
	//	{
	//		this.productFacade = productFacade;
	//	}

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
	public ThemeOffersDTO getThemeOffersComponentDTO(final HomepageComponentRequestDTO themeOffersRequestDTO)
			throws EtailNonBusinessExceptions
	{
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<HomeProductsDTO> finalProductsDTO = new ArrayList<HomeProductsDTO>();
		final ThemeOffersDTO finalThemeOffersDTO = new ThemeOffersDTO();
		ThemeOffersJSONDTO themeOffersJSONDTO = null;
		String themeOffersJSONString = MarketplaceFacadesConstants.EMPTY;
		try
		{
			List<String> productCodes = new ArrayList<String>();
			List<BuyBoxModel> buyBoxModelList = new ArrayList<BuyBoxModel>();
			if (StringUtils.isNotEmpty(themeOffersRequestDTO.getContent()))
			{
				themeOffersJSONString = themeOffersRequestDTO.getContent();
			}

			if (StringUtils.isNotEmpty(themeOffersJSONString))
			{
				themeOffersJSONDTO = mapper.readValue(themeOffersJSONString, ThemeOffersJSONDTO.class);
			}

			productCodes = themeOffersJSONDTO.getItemIds();

			//			final List<HomeProductsDTO> productsdto = themeOffersJSONDTO.getItemIds();
			//
			//			for (final HomeProductsDTO productdto : productsdto)
			//			{
			//				if (StringUtils.isNotEmpty(productdto.getPrdId()))
			//				{
			//					productCodes.add(productdto.getPrdId().toUpperCase());
			//				}
			//			}

			if (CollectionUtils.isNotEmpty(productCodes))
			{
				final String commaSepartedProductCodes = StringUtils.join(productCodes, ",");
				buyBoxModelList = buyBoxService.buyboxPrice(commaSepartedProductCodes);
			}

			if (CollectionUtils.isNotEmpty(buyBoxModelList))
			{
				finalProductsDTO = this.compareDataWithBuyBox(productCodes, buyBoxModelList);

			}
			finalThemeOffersDTO.setOffers(themeOffersJSONDTO.getItems());
			finalThemeOffersDTO.setItems(finalProductsDTO);
		}
		catch (final IOException | ClassCastException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			LOG.error("HomePageAppFacadeImpl error occured:: " + e.getMessage());
			throw new EtailNonBusinessExceptions(e, MarketplaceFacadesConstants.H9001);
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
	public BannersCarouselDTO getBannerProductCarouselDTO(final HomepageComponentRequestDTO bannerCarouselRequestDTO)
			throws EtailNonBusinessExceptions
	{
		// YTODO Auto-generated method stub
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<HomeProductsDTO> finalProductsDTO = new ArrayList<HomeProductsDTO>();
		final BannersCarouselDTO finalBannersCarouselDTO = new BannersCarouselDTO();
		BannersCarouselJSONDTO bannersCarouselJSONDTO = null;
		String bannerProductcarouselJsonString = MarketplaceFacadesConstants.EMPTY;
		try
		{
			List<String> productCodes = new ArrayList<String>();
			List<BuyBoxModel> buyBoxModelList = new ArrayList<BuyBoxModel>();
			if (StringUtils.isNotEmpty(bannerCarouselRequestDTO.getContent()))
			{
				bannerProductcarouselJsonString = bannerCarouselRequestDTO.getContent();
			}

			bannersCarouselJSONDTO = mapper.readValue(bannerProductcarouselJsonString, BannersCarouselJSONDTO.class);

			if (CollectionUtils.isNotEmpty(bannersCarouselJSONDTO.getItemIds()))
			{
				productCodes = bannersCarouselJSONDTO.getItemIds();
			}
			//			for (final HomeProductsDTO productdto : productsdto)
			//			{
			//				if (StringUtils.isNotEmpty(productdto.getPrdId()))
			//				{
			//					productCodes.add(productdto.getPrdId().toUpperCase());
			//				}
			//			}

			if (CollectionUtils.isNotEmpty(productCodes))
			{
				final String commaSepartedProductCodes = StringUtils.join(productCodes, ",");
				buyBoxModelList = buyBoxService.buyboxPrice(commaSepartedProductCodes);
			}

			if (CollectionUtils.isNotEmpty(buyBoxModelList))
			{
				finalProductsDTO = this.compareDataWithBuyBox(productCodes, buyBoxModelList);

			}
			finalBannersCarouselDTO.setTitle(bannersCarouselJSONDTO.getTitle());
			finalBannersCarouselDTO.setDescription(bannersCarouselJSONDTO.getDescription());
			finalBannersCarouselDTO.setImageURL(bannersCarouselJSONDTO.getImageURL());
			finalBannersCarouselDTO.setItems(finalProductsDTO);
		}
		catch (final IOException | ClassCastException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			LOG.error("HomePageAppFacadeImpl error occured:: " + e.getMessage());
			throw new EtailNonBusinessExceptions(e, MarketplaceFacadesConstants.H9001);
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
	public VideoProductCaraouselDTO getVideoProductCarouselDTO(final HomepageComponentRequestDTO videoProductCaraouselRequestDTO)
			throws EtailNonBusinessExceptions
	{
		// YTODO Auto-generated method stub
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<HomeProductsDTO> finalProductsDTO = new ArrayList<HomeProductsDTO>();
		final VideoProductCaraouselDTO finalVideoProductCaraouselDTO = new VideoProductCaraouselDTO();
		VideoProductJSONDTO videoProductJSONDTO = null;
		String videoProductcarouselJsonString = MarketplaceFacadesConstants.EMPTY;
		try
		{
			List<String> productCodes = new ArrayList<String>();
			List<BuyBoxModel> buyBoxModelList = new ArrayList<BuyBoxModel>();
			if (StringUtils.isNotEmpty(videoProductCaraouselRequestDTO.getContent()))
			{
				videoProductcarouselJsonString = videoProductCaraouselRequestDTO.getContent();
			}
			videoProductJSONDTO = mapper.readValue(videoProductcarouselJsonString, VideoProductJSONDTO.class);
			if (CollectionUtils.isNotEmpty(videoProductJSONDTO.getItemIds()))
			{
				productCodes = videoProductJSONDTO.getItemIds();
			}
			//			for (final HomeProductsDTO productdto : productsdto)
			//			{
			//				if (StringUtils.isNotEmpty(productdto.getPrdId()))
			//				{
			//					productCodes.add(productdto.getPrdId().toUpperCase());
			//				}
			//			}

			if (CollectionUtils.isNotEmpty(productCodes))
			{
				final String commaSepartedProductCodes = StringUtils.join(productCodes, ",");
				buyBoxModelList = buyBoxService.buyboxPrice(commaSepartedProductCodes);
			}

			if (CollectionUtils.isNotEmpty(buyBoxModelList))
			{
				finalProductsDTO = this.compareDataWithBuyBox(productCodes, buyBoxModelList);

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
			throw new EtailNonBusinessExceptions(e, MarketplaceFacadesConstants.H9001);
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
	public AutomatedBrandProductCarouselDTO getautomatedBrandCarouselDTO(final HomepageComponentRequestDTO automatedBrandRequestDTO)
			throws EtailNonBusinessExceptions
	{
		// YTODO Auto-generated method stub
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<HomeProductsDTO> finalProductsDTO = new ArrayList<HomeProductsDTO>();
		final AutomatedBrandProductCarouselDTO finalAutomatedBrandProductCarouselDTO = new AutomatedBrandProductCarouselDTO();
		AutomatedBrandProductCarouselJSONDTO automatedBrandProductCarouselJSONDTO = null;
		String automatedBrandCaraouselJsonString = MarketplaceFacadesConstants.EMPTY;
		try
		{
			List<String> productCodes = new ArrayList<String>();
			List<BuyBoxModel> buyBoxModelList = new ArrayList<BuyBoxModel>();
			if (StringUtils.isNotEmpty(automatedBrandRequestDTO.getContent()))
			{
				automatedBrandCaraouselJsonString = automatedBrandRequestDTO.getContent();
			}
			automatedBrandProductCarouselJSONDTO = mapper.readValue(automatedBrandCaraouselJsonString,
					AutomatedBrandProductCarouselJSONDTO.class);
			if (CollectionUtils.isNotEmpty(automatedBrandProductCarouselJSONDTO.getItemIds()))
			{
				productCodes = automatedBrandProductCarouselJSONDTO.getItemIds();
			}
			//			for (final HomeProductsDTO productdto : productsdto)
			//			{
			//				if (StringUtils.isNotEmpty(productdto.getPrdId()))
			//				{
			//					productCodes.add(productdto.getPrdId().toUpperCase());
			//				}
			//			}

			if (CollectionUtils.isNotEmpty(productCodes))
			{
				final String commaSepartedProductCodes = StringUtils.join(productCodes, ",");
				buyBoxModelList = buyBoxService.buyboxPrice(commaSepartedProductCodes);
			}

			if (CollectionUtils.isNotEmpty(buyBoxModelList))
			{
				finalProductsDTO = this.compareDataWithBuyBox(productCodes, buyBoxModelList);

			}
			finalAutomatedBrandProductCarouselDTO.setBrandLogo(automatedBrandProductCarouselJSONDTO.getBrandLogo());
			finalAutomatedBrandProductCarouselDTO.setDescription(automatedBrandProductCarouselJSONDTO.getDescription());
			finalAutomatedBrandProductCarouselDTO.setImageURL(automatedBrandProductCarouselJSONDTO.getImageURL());
			finalAutomatedBrandProductCarouselDTO.setItems(finalProductsDTO);
		}
		catch (final IOException | ClassCastException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			LOG.error("HomePageAppFacadeImpl error occured:: " + e.getMessage());
			throw new EtailNonBusinessExceptions(e, MarketplaceFacadesConstants.H9001);
		}

		return finalAutomatedBrandProductCarouselDTO;
	}


	private List<HomeProductsDTO> compareDataWithBuyBox(final List<String> listingIds, final List<BuyBoxModel> buyBoxModelList)
			throws ArrayIndexOutOfBoundsException, ClassCastException, IllegalAccessException, InvocationTargetException,
			NoSuchMethodException
	{
		// YTODO Auto-generated method stub
		final List<HomeProductsDTO> finalProductsDTO = new ArrayList<HomeProductsDTO>();
		final HomeProductsDTO productdto = new HomeProductsDTO();
		List<ProductData> productDataList = new ArrayList<ProductData>();
		//		final List<String> listingIds = (List<String>) CollectionUtils.collect(productsdto, new BeanToPropertyValueTransformer(
		//				"prdId"));
		final CurrencyModel currency = commonI18NService.getCurrency(MarketplaceFacadesConstants.INR);
		final String currencySymbol = currency.getSymbol();
		final String currencyIso = currency.getIsocode();
		final DecimalFormat df = new DecimalFormat("###,###.##");
		final String imageFormat = getMediaFormat();
		final List<ProductModel> productModelList = mplProductService.getProductListForCodeList(
				catalogVersionService.getCatalogVersion("mplProductCatalog", "Online"), listingIds);

		LOG.info("*************" + productModelList.size());

		//final Collection<ProductOption> options = Arrays.asList(ProductOption.BASIC, ProductOption.IMAGES);
		productDataList = Converters.convertAll(productModelList, getProductConverter());
		//final ProductData productData = getProductConverter()..convert(productModel);

		//		for (final ProductModel productModel : productModelList)
		//		{
		//			final ProductData productData = productFacade.getProductForOptions(productModel,
		//					Arrays.asList(ProductOption.BASIC, ProductOption.GALLERY, ProductOption.IMAGES));
		//			//			ProductData productData = new ProductData();
		//			//			productData = getCustomProductConverter().convert(productModel);
		//			//			if (options != null)
		//			//			{
		//			//				getProductConfiguredPopulator().populate(productModel, productData, options);
		//			//			}
		//
		//			productDataList.add(productData);
		//		}


		for (final BuyBoxModel buyBoxModel : buyBoxModelList)
		{
			ProductData productdata = new ProductData();
			//final String buyBoxListingIdLwrCase = buyBoxModel.getProduct().toLowerCase();
			final String buyBoxListingId = buyBoxModel.getProduct();
			if (listingIds.contains(buyBoxListingId))
			{
				productdata = (ProductData) CollectionUtils.find(productDataList, new Predicate()
				{
					@Override
					public boolean evaluate(final Object obj)
					{
						final ProductData finder = (ProductData) obj;
						return finder.getCode().equals(buyBoxListingId);
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

				ImageData imageData = new ImageData();

				if (CollectionUtils.isNotEmpty(productdata.getImages()))
				{
					imageData = (ImageData) CollectionUtils.find(productdata.getImages(), new Predicate()
					{
						@Override
						public boolean evaluate(final Object obj)
						{
							final ImageData finder = (ImageData) obj;
							return finder.getFormat().equals(imageFormat);
						}
					});
					//				for (final ImageData imageData : productdata.getImages())
					//				{
					//					if (imageData.getFormat().equalsIgnoreCase(imageFormat))
					//					{
					//						productdto.setImageURL(imageData.getUrl());
					//					}
					//				}
					if (StringUtils.isNotEmpty(imageData.getUrl()))
					{
						productdto.setImageURL(imageData.getUrl());
					}
				}
				productdto.setTitle(productdata.getProductTitle());
				productdto.setPrdId(productdata.getCode());

				productdto.setWebURL(productdata.getUrl());
				productdto.setAppURL(productdata.getUrl());
				finalProductsDTO.add(productdto);
			}

		}
		return finalProductsDTO;
	}
}
