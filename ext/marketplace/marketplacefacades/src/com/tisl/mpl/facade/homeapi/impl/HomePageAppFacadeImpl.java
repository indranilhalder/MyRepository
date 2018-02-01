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
import com.tisl.mpl.wsdto.HomeProductsDTO;
import com.tisl.mpl.wsdto.HomepageComponenetJSONDTO;
import com.tisl.mpl.wsdto.HomepageComponentRequestDTO;
import com.tisl.mpl.wsdto.HomepageComponetsDTO;


/**
 * @author TCS
 *
 */
public class HomePageAppFacadeImpl implements HomePageAppFacade
{
	private static final Logger LOG = Logger.getLogger(HomePageAppFacadeImpl.class);

	private BuyBoxService buyBoxService;

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;

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


	@Override
	public HomepageComponetsDTO gethomepageComponentsDTO(final HomepageComponentRequestDTO homepageComponentRequestDTO)
			throws EtailNonBusinessExceptions
	{
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<HomeProductsDTO> finalProductsDTO = new ArrayList<HomeProductsDTO>();
		final HomepageComponetsDTO finalHomepageComponetsDTO = new HomepageComponetsDTO();
		HomepageComponenetJSONDTO homepageComponenetJSONDTO = null;
		String homepageComponenetJSONString = MarketplaceFacadesConstants.EMPTY;

		try
		{
			List<String> productCodes = new ArrayList<String>();
			List<BuyBoxModel> buyBoxModelList = new ArrayList<BuyBoxModel>();
			if (StringUtils.isNotEmpty(homepageComponentRequestDTO.getContent()))
			{
				homepageComponenetJSONString = homepageComponentRequestDTO.getContent();
			}
			homepageComponenetJSONDTO = mapper.readValue(homepageComponenetJSONString, HomepageComponenetJSONDTO.class);
			if (CollectionUtils.isNotEmpty(homepageComponenetJSONDTO.getItemIds()))
			{
				productCodes = homepageComponenetJSONDTO.getItemIds();
			}

			if (CollectionUtils.isNotEmpty(productCodes))
			{
				final String commaSepartedProductCodes = StringUtils.join(productCodes, ",");
				buyBoxModelList = buyBoxService.buyboxPrice(commaSepartedProductCodes);
			}

			if (CollectionUtils.isNotEmpty(buyBoxModelList))
			{
				finalProductsDTO = this.compareDataWithBuyBox(productCodes, buyBoxModelList);

			}
			finalHomepageComponetsDTO.setItems(finalProductsDTO);
		}

		catch (final IOException | ClassCastException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			LOG.error("HomePageAppFacadeImpl error occured:: " + e.getMessage());
			throw new EtailNonBusinessExceptions(e, MarketplaceFacadesConstants.H9001);
		}
		return finalHomepageComponetsDTO;
		//
	}


	private List<HomeProductsDTO> compareDataWithBuyBox(final List<String> listingIds, final List<BuyBoxModel> buyBoxModelList)
			throws ArrayIndexOutOfBoundsException, ClassCastException, IllegalAccessException, InvocationTargetException,
			NoSuchMethodException
	{
		// YTODO Auto-generated method stub
		final List<HomeProductsDTO> finalProductsDTO = new ArrayList<HomeProductsDTO>();
		final HomeProductsDTO productdto = new HomeProductsDTO();
		List<ProductData> productDataList = new ArrayList<ProductData>();
		final CurrencyModel currency = commonI18NService.getCurrency(MarketplaceFacadesConstants.INR);
		final String currencySymbol = currency.getSymbol();
		final String currencyIso = currency.getIsocode();
		final DecimalFormat df = new DecimalFormat("###,###.##");
		final String imageFormat = getMediaFormat();
		final List<ProductModel> productModelList = mplProductService.getProductListForCodeList(
				catalogVersionService.getCatalogVersion("mplProductCatalog", "Online"), listingIds);

		LOG.info("*************Home Page app components controller" + productModelList.size());

		productDataList = Converters.convertAll(productModelList, getProductConverter());


		for (final BuyBoxModel buyBoxModel : buyBoxModelList)
		{
			ProductData productdata = new ProductData();
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
