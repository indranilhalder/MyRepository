/**
 *
 */
package com.tisl.mpl.service.impl;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ClassificationData;
import de.hybris.platform.commercefacades.product.data.FeatureData;
import de.hybris.platform.commercefacades.product.data.FeatureValueData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.PromotionData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.localization.Localization;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.enums.OnlineExclusiveEnum;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.helper.ProductDetailsHelper;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.service.MplProductWebService;
import com.tisl.mpl.wsdto.CapacityLinkData;
import com.tisl.mpl.wsdto.ClassificationMobileWsData;
import com.tisl.mpl.wsdto.ColorLinkData;
import com.tisl.mpl.wsdto.DeliveryModeData;
import com.tisl.mpl.wsdto.GalleryImageData;
import com.tisl.mpl.wsdto.GiftProductMobileData;
import com.tisl.mpl.wsdto.KnowMoreDTO;
import com.tisl.mpl.wsdto.ProductDetailMobileWsData;
import com.tisl.mpl.wsdto.PromotionMobileData;
import com.tisl.mpl.wsdto.SellerInformationMobileData;
import com.tisl.mpl.wsdto.SizeLinkData;
import com.tisl.mpl.wsdto.VariantOptionMobileData;


/**
 * @author TCS
 *
 */
public class MplProductWebServiceImpl implements MplProductWebService
{
	@Resource(name = "productService")
	private ProductService productService;
	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;
	@Resource(name = "buyBoxFacade")
	private BuyBoxFacade buyBoxFacade;
	@Resource(name = "defaultPromotionManager")
	private DefaultPromotionManager defaultPromotionManager;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	/*
	 * @Autowired private MplDeliveryInformationService mplDeliveryInformationService;
	 */
	@Autowired
	private ModelService modelService;
	@Autowired
	private ProductDetailsHelper productDetailsHelper;

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	private static final String Y = "Y";
	private static final String N = "N";
	private static final String WARRANTY = "Warranty";
	private static final String HTTP = "http";
	private static final String HTTPS = "https";
	private static final Logger LOG = Logger.getLogger(MplProductWebServiceImpl.class);

	/*
	 * To get product details for a product code
	 * 
	 * @see com.tisl.mpl.service.MplProductWebService#getProductdetailsForProductCode(java.lang.String)
	 */
	@Override
	public ProductDetailMobileWsData getProductdetailsForProductCode(final String productCode, final String baseUrl)
			throws EtailNonBusinessExceptions
	{
		final ProductDetailMobileWsData productDetailMobile = new ProductDetailMobileWsData();
		ProductDetailMobileWsData isNewOrOnlineExclusive = new ProductDetailMobileWsData();
		try
		{
			String isEMIeligible = null;
			final ProductModel productModel = productService.getProductForCode(defaultPromotionManager.catalogData(), productCode);
			BuyBoxData buyBoxData = null;
			String ussid = null;
			String isProductCOD = null;
			ProductData productData = null;
			if (null != productModel)
			{
				productData = productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC,
						ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.GALLERY,
						ProductOption.CATEGORIES, ProductOption.REVIEW, ProductOption.PROMOTIONS, ProductOption.CLASSIFICATION,
						ProductOption.VARIANT_FULL, ProductOption.VOLUME_PRICES, ProductOption.DELIVERY_MODE_AVAILABILITY,
						ProductOption.SELLER));
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9037);
			}

			if (null != productCode)
			{
				try
				{
					buyBoxData = buyBoxFacade.buyboxPrice(productCode);
					if (null == buyBoxData)
					{
						productDetailMobile.setSellerAssociationstatus(MarketplacecommerceservicesConstants.N);
					}
				}
				catch (final Exception e)
				{
					LOG.error("*************** Exception at PDP web service buybox fetching ******************* " + e);
				}
			}
			if (null != buyBoxData && null != buyBoxData.getSellerAssociationstatus())
			{
				productDetailMobile.setSellerAssociationstatus(MarketplacecommerceservicesConstants.Y);
			}
			else
			{
				productDetailMobile.setSellerAssociationstatus(MarketplacecommerceservicesConstants.N);
			}
			if (null != buyBoxData && null != buyBoxData.getSellerArticleSKU())
			{
				ussid = buyBoxData.getSellerArticleSKU();
				LOG.debug("*************** Mobile web service buyBox USSID ****************" + ussid);
			}
			isNewOrOnlineExclusive = getRichAttributes(productModel);

			if (null != isNewOrOnlineExclusive && null != isNewOrOnlineExclusive.getIsOnlineExclusive())
			{
				productDetailMobile.setIsOnlineExclusive(isNewOrOnlineExclusive.getIsOnlineExclusive());
			}
			if (null != isNewOrOnlineExclusive && null != isNewOrOnlineExclusive.getIsProductNew())
			{
				productDetailMobile.setIsProductNew(isNewOrOnlineExclusive.getIsProductNew());
			}
			SellerInformationData buyboxdataCheck = null;
			if (null != productData && null != ussid)
			{
				buyboxdataCheck = buyboxdata(productData, ussid);
			}
			if (null != buyboxdataCheck && null != buyboxdataCheck.getIsCod())
			{
				isProductCOD = buyboxdataCheck.getIsCod();
			}
			if (null != buyboxdataCheck && null != getEligibleDeliveryModes(buyboxdataCheck))
			{
				productDetailMobile.setEligibleDeliveryModes(getEligibleDeliveryModes(buyboxdataCheck));
			}
			List<SellerInformationMobileData> framedOtherSellerDataList = null;
			List<SellerInformationData> otherSellerDataList = null;
			if (null != productData && null != ussid)
			{
				otherSellerDataList = getOtherSellerDetails(productCode, ussid);
			}
			if (null != otherSellerDataList && !otherSellerDataList.isEmpty()
					&& null != frameOtherSellerDetails(otherSellerDataList, productModel))
			{
				framedOtherSellerDataList = frameOtherSellerDetails(otherSellerDataList, productModel);
			}
			if (null != productData && null != productData.getListingId())
			{
				productDetailMobile.setProductListingId(productData.getListingId());
			}
			if (null != productData && null != productData.getProductTitle())
			{
				productDetailMobile.setProductName(productData.getProductTitle());
			}
			if (null != productData && null != productData.getArticleDescription())
			{
				productDetailMobile.setProductDescription(productData.getArticleDescription());
			}
			if (null != productData && null != productData.getRootCategory())
			{
				productDetailMobile.setRootCategory(productData.getRootCategory());

				LOG.debug("*************** Mobile web service product root category ****************" + productData.getRootCategory());
			}
			if (null != productData && null != getGalleryImages(productData))
			{
				productDetailMobile.setGalleryImagesList(getGalleryImages(productData));
			}

			if (null != isProductCOD)
			{
				productDetailMobile.setIsCOD(isProductCOD);

			}

			if (null != framedOtherSellerDataList && !framedOtherSellerDataList.isEmpty())
			{
				productDetailMobile.setOtherSellers(framedOtherSellerDataList);
			}
			if (null != buyBoxData && null != buyBoxData.getSellerName())
			{
				productDetailMobile.setWinningSellerName(buyBoxData.getSellerName());
			}

			if (null != buyBoxData && null != buyBoxData.getSpecialPrice()
					&& null != buyBoxData.getSpecialPrice().getFormattedValue() && null != buyBoxData.getSpecialPrice().getValue()
					&& buyBoxData.getSpecialPrice().getValue().compareTo(BigDecimal.ZERO) > 0)
			{
				productDetailMobile.setWinningSellerSpecialPrice(buyBoxData.getSpecialPrice().getFormattedValue());
			}

			if (null != buyBoxData && null != buyBoxData.getPrice() && null != buyBoxData.getPrice().getFormattedValue()
					&& null != buyBoxData.getPrice().getValue() && buyBoxData.getPrice().getValue().compareTo(BigDecimal.ZERO) > 0)
			{
				productDetailMobile.setWinningSellerMOP(buyBoxData.getPrice().getFormattedValue().toString());
			}
			if (null != buyBoxData && null != buyBoxData.getSpecialPrice() && null != buyBoxData.getSpecialPrice().getValue()
					&& null != buyBoxData.getSpecialPrice().getValue()
					&& buyBoxData.getSpecialPrice().getValue().compareTo(BigDecimal.ZERO) > 0)
			{
				isEMIeligible = getEMIforProduct(buyBoxData.getSpecialPrice().getValue());
			}
			else if (null != buyBoxData && null != buyBoxData.getPrice() && null != buyBoxData.getPrice().getValue()
					&& null != buyBoxData.getPrice().getValue() && buyBoxData.getPrice().getValue().compareTo(BigDecimal.ZERO) > 0)
			{
				isEMIeligible = getEMIforProduct(buyBoxData.getPrice().getValue());
			}
			else if (null != buyBoxData && null != buyBoxData.getMrp() && null != buyBoxData.getMrp().getValue()
					&& null != buyBoxData.getMrp().getValue() && buyBoxData.getMrp().getValue().compareTo(BigDecimal.ZERO) > 0)
			{
				isEMIeligible = getEMIforProduct(buyBoxData.getMrp().getValue());
			}
			if (null != isEMIeligible)
			{
				productDetailMobile.setIsEMIEligible(isEMIeligible);
			}

			if (null != buyBoxData && null != buyBoxData.getAvailable())
			{
				productDetailMobile.setWinningSellerAvailableStock(buyBoxData.getAvailable().toString());
			}

			if (null != buyBoxData && null != buyBoxData.getMrp() && null != buyBoxData.getMrp().getFormattedValue())
			{
				productDetailMobile.setMrp(buyBoxData.getMrp().getFormattedValue());
			}
			if (null != buyboxdataCheck && null != buyboxdataCheck.getFullfillment())
			{
				productDetailMobile.setFulfillmentType(buyboxdataCheck.getFullfillment());
			}
			//	Promotion Dto changed

			PromotionMobileData potenitalPromo = null;
			if (null != productData)
			{
				potenitalPromo = getPromotionsForProduct(productData, buyBoxData, framedOtherSellerDataList);
			}
			if (null != potenitalPromo)
			{
				productDetailMobile.setPotentialPromotions(potenitalPromo);
			}

			if (null != productData && null != productData.getRatingCount())
			{
				productDetailMobile.setTotalreviewComments(productData.getRatingCount().toString());
			}
			else
			{
				productDetailMobile.setTotalreviewComments(productData.getNumberOfReviews().toString());
			}
			/* Details section of a product */
			if (null != productData.getClassifications())
			{
				final Map<String, String> mapConfigurableAttribute = new HashMap<String, String>();
				final List<ClassificationData> ConfigurableAttributeList = new ArrayList<ClassificationData>(
						productData.getClassifications());
				for (final ClassificationData configurableAttributData : ConfigurableAttributeList)
				{
					final List<FeatureData> featureDataList = new ArrayList<FeatureData>(configurableAttributData.getFeatures());

					for (final FeatureData featureData : featureDataList)
					{

						final List<FeatureValueData> featureValueList = new ArrayList<FeatureValueData>(featureData.getFeatureValues());
						if (null != productData.getRootCategory())
						{
							final String properitsValue = configurationService.getConfiguration().getString(
									MarketplacewebservicesConstants.CONFIGURABLE_ATTRIBUTE + productData.getRootCategory());
							//apparel
							final FeatureValueData featureValueData = featureValueList.get(0);

							if (productData.getRootCategory().equalsIgnoreCase(MarketplacewebservicesConstants.CLOTHING))
							{

								if (null != properitsValue && featureValueData.getValue() != null
										&& properitsValue.toLowerCase().contains(featureData.getName().toLowerCase()))
								{
									mapConfigurableAttribute.put(featureData.getName(), featureValueData.getValue());
								}

							} //end apparel
							  //electronics
							else
							{
								if (properitsValue.contains(configurableAttributData.getName()))
								{
									mapConfigurableAttribute.put(featureData.getName(), featureValueData.getValue());
								}
							}
						}
					}
				}
				if (!mapConfigurableAttribute.isEmpty())
				{
					if (productData.getRootCategory().equalsIgnoreCase(MarketplacewebservicesConstants.CLOTHING))
					{
						productDetailMobile.setDetails(mapConfigurableAttribute);
					}
					else
					{
						final Map<String, String> treeMapConfigurableAttribute = new TreeMap<String, String>(mapConfigurableAttribute);
						productDetailMobile.setDetails(treeMapConfigurableAttribute);
					}
				}
			}
			/* Specifications of a product */
			if (null != productData && null != productData.getClassifications())

			{
				List<ClassificationMobileWsData> specificationsList = null;
				specificationsList = getSpecificationsOfProductByGroup(productData);

				if (null != specificationsList && !specificationsList.isEmpty())
				{
					productDetailMobile.setClassifications(specificationsList);
				}
				//Warranty
				Map<String, String> mapConfigurableAttribute = new HashMap<String, String>();
				mapConfigurableAttribute = getSpecificationsOfProduct(productData);
				if (null != mapConfigurableAttribute && null != productData
						&& null != getWarrantyOfProduct(mapConfigurableAttribute, productData)
						&& !getWarrantyOfProduct(mapConfigurableAttribute, productData).isEmpty())
				{
					productDetailMobile.setWarranty(getWarrantyOfProduct(mapConfigurableAttribute, productData));
				}

			}

			if (null != productData && null != productData.getArticleDescription())
			{
				productDetailMobile.setStyleNote(productData.getArticleDescription());
			}
			//	productDetailMobile.setTataPromise("Tata Promise");
			//Know more section changes
			if (StringUtils.isNotEmpty(configurationService.getConfiguration().getString("cliq.care.number")))
			{
				productDetailMobile.setKnowMorePhoneNo(configurationService.getConfiguration().getString("cliq.care.number"));
			}

			if (StringUtils.isNotEmpty(configurationService.getConfiguration().getString("cliq.care.mail")))
			{
				productDetailMobile.setKnowMoreEmail(configurationService.getConfiguration().getString("cliq.care.mail"));
			}


			List<KnowMoreDTO> knowMoreList = null;
			if (null != productModel && null != buyBoxData)
			{
				knowMoreList = getknowMoreDetails(productModel, buyBoxData);
			}
			if (CollectionUtils.isNotEmpty(knowMoreList))
			{
				productDetailMobile.setKnowMore(knowMoreList);
			}


			if (null != productData && null != productData.getBrand() && null != productData.getBrand().getBrandname())
			{
				productDetailMobile.setBrandName(productData.getBrand().getBrandname());
			}
			List<VariantOptionMobileData> variantDataList = new ArrayList<VariantOptionMobileData>();
			if (null != productData)
			{
				variantDataList = getVariantDetailsForProduct(productData);
			}
			if (null != variantDataList && !variantDataList.isEmpty())
			{
				productDetailMobile.setVariantOptions(variantDataList);
			}
			if (null != buyBoxData && null != buyBoxData.getSellerArticleSKU())
			{
				productDetailMobile.setWinningUssID(buyBoxData.getSellerArticleSKU());
			}

			Map<String, String> deliveryModesForProduct = new HashMap<String, String>();
			Map<String, Map<String, Integer>> deliveryModesATPForProduct = new HashMap<String, Map<String, Integer>>();

			final List<String> deliveryInfoList = new ArrayList<String>();
			deliveryInfoList.add(MarketplacewebservicesConstants.EXPRESS_DELIVERY);
			deliveryInfoList.add(MarketplacewebservicesConstants.HOME_DELIVERY);


			//deliveryModesForProduct = getDeliveryModesAtPPrep(productData);
			deliveryModesATPForProduct = productDetailsHelper.getDeliveryModeATMap(deliveryInfoList);

			if (null != deliveryModesATPForProduct && !deliveryModesATPForProduct.isEmpty() && null != buyBoxData
					&& StringUtils.isNotEmpty(buyBoxData.getSellerArticleSKU()))
			{
				deliveryModesForProduct = getDeliveryModes(productModel, deliveryModesATPForProduct, buyBoxData.getSellerArticleSKU());
			}
			if (null != deliveryModesForProduct && !deliveryModesForProduct.isEmpty())
			{
				productDetailMobile.setDeliveryModesATP(deliveryModesForProduct);
			}
			if (null != productData && null != getCategoryOfProduct(productData))
			{
				productDetailMobile.setProductCategory(getCategoryOfProduct(productData));
			}
			if (null != productData && null != getCategoryCodeOfProduct(productData))
			{
				productDetailMobile.setProductCategoryId(getCategoryCodeOfProduct(productData));
			}

			String sharedText = Localization.getLocalizedString(MarketplacewebservicesConstants.PDP_SHARED_PRE);

			if (null != productData && null != productData.getUrl()
					&& (!(productData.getUrl().toLowerCase().contains(HTTP) || productData.getUrl().toLowerCase().contains(HTTPS))))
			{
				//sharedText += MarketplacecommerceservicesConstants.SPACE + baseUrl + "" + productData.getUrl(); Do not add empty strings
				sharedText += MarketplacecommerceservicesConstants.SPACE + baseUrl + productData.getUrl();
			}
			else if (null != productData && null != productData.getUrl())
			{
				sharedText += productData.getUrl();
			}
			sharedText += MarketplacecommerceservicesConstants.SPACE
					+ Localization.getLocalizedString(MarketplacewebservicesConstants.PDP_SHARED_POST);
			productDetailMobile.setSharedText(sharedText);
			LOG.debug("******************** PDP mobile web service  fetching done *****************");

		}

		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9037);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final EtailBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		return productDetailMobile;
	}

	/**
	 * warranty list
	 *
	 * @param mapConfigurableAttribute
	 * @param productData
	 * @return List<String>
	 */
	private List<String> getWarrantyOfProduct(final Map<String, String> mapConfigurableAttribute, final ProductData productData)
	{
		final List<String> warrantyList = new ArrayList<String>();
		try
		{
			if (null != mapConfigurableAttribute && null != productData && null != productData.getRootCategory()
					&& productData.getRootCategory().equalsIgnoreCase(MarketplacewebservicesConstants.ELECTRONICS))
			{
				for (final Map.Entry<String, String> entry : mapConfigurableAttribute.entrySet())
				{
					if (null != entry.getKey() && entry.getKey().contains(WARRANTY) && null != entry.getValue())
					{
						warrantyList.add(entry.getValue());
					}
				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		return warrantyList;
	}

	/**
	 * get eligible delivery modes
	 *
	 * @param seller
	 * @return List<DeliveryModeData>
	 */
	private List<DeliveryModeData> getEligibleDeliveryModes(final SellerInformationData seller)
	{
		DeliveryModeData deliveryMode = null;
		final List<DeliveryModeData> deliveryModesList = new ArrayList<DeliveryModeData>();
		try
		{
			if (null != seller && null != seller.getDeliveryModes())
			{
				if (StringUtils.isNotEmpty(seller.getSellerID()))
				{
					LOG.debug("*************** Mobile web service eligible delivery modes ****************" + seller.getSellerID());
				}

				for (final MarketplaceDeliveryModeData delivery : seller.getDeliveryModes())
				{
					deliveryMode = new DeliveryModeData();
					if (null != delivery.getCode())
					{
						LOG.debug("*************** Mobile web service eligible delivery modes code ****************"
								+ delivery.getCode());

						deliveryMode.setCode(delivery.getCode());
					}
					if (null != delivery.getName())
					{
						deliveryMode.setName(delivery.getName());
					}
					if (null != delivery.getDeliveryCost() && null != delivery.getDeliveryCost().getFormattedValue())
					{
						deliveryMode.setDisplayCost(delivery.getDeliveryCost().getFormattedValue());
					}
					deliveryModesList.add(deliveryMode);
				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		return deliveryModesList;
	}

	/**
	 * know more details
	 *
	 * @param productModel
	 * @param buyBoxData
	 * @return List<String>
	 */
	private List<KnowMoreDTO> getknowMoreDetails(final ProductModel productModel, final BuyBoxData buyBoxData)
	{

		String knowMoreSec = null;
		String knowMoreTh = null;
		String returnWindow = "0";
		String knowMoreFourth = null;
		String knowMoreFifth = null;
		final List<KnowMoreDTO> knowMoreList = new ArrayList<KnowMoreDTO>();
		KnowMoreDTO knowMoreItem = null;
		final String cliqCareNumber = configurationService.getConfiguration().getString("cliq.care.number");
		final String cliqCareMail = configurationService.getConfiguration().getString("cliq.care.mail");
		if (StringUtils.isNotEmpty(Localization.getLocalizedString(MarketplacewebservicesConstants.KNOW_MORE_SECOND)))
		{
			knowMoreSec = Localization.getLocalizedString(MarketplacewebservicesConstants.KNOW_MORE_SECOND);

			if (null != productModel && StringUtils.isNotEmpty(buyBoxData.getSellerArticleSKU()))
			{
				returnWindow = getReturnWindow(productModel, buyBoxData.getSellerArticleSKU());
			}
			if (StringUtils.isNotEmpty(Localization.getLocalizedString(MarketplacewebservicesConstants.KNOW_MORE_THIRD)))
			{
				knowMoreTh = Localization.getLocalizedString(MarketplacewebservicesConstants.KNOW_MORE_THIRD);
			}
		}
		if (StringUtils.isNotEmpty(Localization.getLocalizedString(MarketplacewebservicesConstants.KNOW_MORE_FOURTH)))
		{
			knowMoreFourth = Localization.getLocalizedString(MarketplacewebservicesConstants.KNOW_MORE_FOURTH);
		}
		if (StringUtils.isNotEmpty(Localization.getLocalizedString(MarketplacewebservicesConstants.KNOW_MORE_FIFTH)))
		{
			knowMoreFifth = Localization.getLocalizedString(MarketplacewebservicesConstants.KNOW_MORE_FIFTH);
		}

		if (StringUtils.isNotEmpty(Localization.getLocalizedString(MarketplacewebservicesConstants.KNOW_MORE_FIRST)))
		{
			knowMoreItem = new KnowMoreDTO();
			knowMoreItem.setKnowMoreItem(Localization.getLocalizedString(MarketplacewebservicesConstants.KNOW_MORE_FIRST));
			knowMoreList.add(knowMoreItem);
		}
		if (StringUtils.isNotEmpty(knowMoreSec) && StringUtils.isNotEmpty(returnWindow) && StringUtils.isNotEmpty(knowMoreTh))
		{
			knowMoreItem = new KnowMoreDTO();
			knowMoreItem.setKnowMoreItem(knowMoreSec + MarketplacecommerceservicesConstants.SPACE + returnWindow
					+ MarketplacecommerceservicesConstants.SPACE + knowMoreTh);
			knowMoreList.add(knowMoreItem);
		}
		if (StringUtils.isNotEmpty(knowMoreFourth) && StringUtils.isNotEmpty(cliqCareNumber)
				&& StringUtils.isNotEmpty(knowMoreFifth) && StringUtils.isNotEmpty(cliqCareMail))
		{
			knowMoreItem = new KnowMoreDTO();
			knowMoreItem.setKnowMoreItem(knowMoreFourth + MarketplacecommerceservicesConstants.SPACE + cliqCareNumber
					+ MarketplacecommerceservicesConstants.SPACE + knowMoreFifth + MarketplacecommerceservicesConstants.SPACE
					+ cliqCareMail);
			knowMoreList.add(knowMoreItem);
		}
		return knowMoreList;
	}

	/**
	 * return window calculation
	 *
	 * @param productModel
	 * @param winningUssid
	 * @return String
	 */
	private String getReturnWindow(final ProductModel productModel, final String winningUssid)
	{

		String returnWindow = "0";
		if (null != productModel.getSellerInformationRelator() && !productModel.getSellerInformationRelator().isEmpty())
		{
			for (final SellerInformationModel sellerInfo : productModel.getSellerInformationRelator())
			{
				if (StringUtils.isNotEmpty(winningUssid) && StringUtils.isNotEmpty(sellerInfo.getSellerArticleSKU())
						&& sellerInfo.getSellerArticleSKU().equalsIgnoreCase(winningUssid) && null != sellerInfo.getRichAttribute()
						&& !sellerInfo.getRichAttribute().isEmpty())
				{
					for (final RichAttributeModel rich : sellerInfo.getRichAttribute())
					{
						if (null != rich.getReturnWindow())
						{
							returnWindow = rich.getReturnWindow().toString();
						}
					}

				}
			}
		}
		return returnWindow;
	}

	/**
	 * getting delivery modes ATP
	 *
	 * @param productModel
	 * @param deliveryModesATPForProduct
	 * @param winningUssid
	 * @return Map<String, String>
	 */
	private Map<String, String> getDeliveryModes(final ProductModel productModel,
			final Map<String, Map<String, Integer>> deliveryModesATPForProduct, final String winningUssid)
	{
		final Map<String, String> finalDeliveryMode = new HashMap<String, String>();
		String homeDeliveryText = null;
		String expressDeliveryText = null;
		String homeDeliveryStart = null;
		String homeDeliveryEnd = null;
		String expressDeliveryStart = null;
		String expressDeliveryend = null;
		final String deliveryPreText = Localization.getLocalizedString(MarketplacewebservicesConstants.DELIVERY_PRE_TEXT);
		final String deliveryPostText = Localization.getLocalizedString(MarketplacewebservicesConstants.DELIVERY_POST_TEXT);
		int leadTimeForUssid = 0;
		if (null != productModel.getSellerInformationRelator() && !productModel.getSellerInformationRelator().isEmpty())
		{
			for (final SellerInformationModel sellerInfo : productModel.getSellerInformationRelator())
			{
				if (StringUtils.isNotEmpty(winningUssid) && StringUtils.isNotEmpty(sellerInfo.getSellerArticleSKU())
						&& sellerInfo.getSellerArticleSKU().equalsIgnoreCase(winningUssid) && null != sellerInfo.getRichAttribute()
						&& !sellerInfo.getRichAttribute().isEmpty())
				{
					for (final RichAttributeModel rich : sellerInfo.getRichAttribute())
					{
						if (null != rich.getLeadTimeForHomeDelivery())
						{
							leadTimeForUssid = rich.getLeadTimeForHomeDelivery().intValue();
						}
					}

				}
			}
		}
		for (final Map.Entry<String, Map<String, Integer>> entry : deliveryModesATPForProduct.entrySet())
		{
			if (null != entry.getKey() && entry.getKey().equalsIgnoreCase(MarketplaceFacadesConstants.HOME_DELIVERY)
					&& null != entry.getValue())
			{
				for (final Map.Entry<String, Integer> deliveryModeMap : entry.getValue().entrySet())
				{
					if (null != deliveryModeMap.getKey() && null != deliveryModeMap.getValue()
							&& deliveryModeMap.getKey().equalsIgnoreCase(MarketplacewebservicesConstants.STARTFORHOME))
					{
						homeDeliveryStart = String.valueOf(leadTimeForUssid + deliveryModeMap.getValue().intValue());
					}
					if (null != deliveryModeMap.getKey() && null != deliveryModeMap.getValue()
							&& deliveryModeMap.getKey().equalsIgnoreCase(MarketplacewebservicesConstants.ENDFORHOME))
					{
						homeDeliveryEnd = String.valueOf(leadTimeForUssid + deliveryModeMap.getValue().intValue());
					}
				}
			}
			else
			{
				for (final Map.Entry<String, Integer> deliveryModeMap : entry.getValue().entrySet())
				{
					if (null != deliveryModeMap.getKey() && null != deliveryModeMap.getValue()
							&& deliveryModeMap.getKey().equalsIgnoreCase(MarketplacewebservicesConstants.STARTFOREXPRESS))
					{
						expressDeliveryStart = String.valueOf(deliveryModeMap.getValue().intValue());
					}
					if (null != deliveryModeMap.getKey() && null != deliveryModeMap.getValue()
							&& deliveryModeMap.getKey().equalsIgnoreCase(MarketplacewebservicesConstants.ENDFOREXPRESS))
					{
						expressDeliveryend = String.valueOf(deliveryModeMap.getValue().intValue());
					}
				}
			}

		}
		if (StringUtils.isNotEmpty(deliveryPreText) && StringUtils.isNotEmpty(homeDeliveryStart)
				&& StringUtils.isNotEmpty(homeDeliveryEnd) && StringUtils.isNotEmpty(deliveryPostText))
		{
			homeDeliveryText = deliveryPreText + MarketplacecommerceservicesConstants.SPACE + homeDeliveryStart
					+ MarketplacecommerceservicesConstants.HYPHEN + homeDeliveryEnd + MarketplacecommerceservicesConstants.SPACE
					+ deliveryPostText;
		}
		if (StringUtils.isNotEmpty(deliveryPreText) && StringUtils.isNotEmpty(expressDeliveryStart)
				&& StringUtils.isNotEmpty(expressDeliveryend) && StringUtils.isNotEmpty(deliveryPostText))
		{
			expressDeliveryText = deliveryPreText + MarketplacecommerceservicesConstants.SPACE + expressDeliveryStart
					+ MarketplacecommerceservicesConstants.HYPHEN + expressDeliveryend + MarketplacecommerceservicesConstants.SPACE
					+ deliveryPostText;
		}
		if (StringUtils.isNotEmpty(homeDeliveryText))
		{
			finalDeliveryMode.put(MarketplaceFacadesConstants.HOME_DELIVERY, homeDeliveryText);
		}
		if (StringUtils.isNotEmpty(expressDeliveryText))
		{
			finalDeliveryMode.put(MarketplaceFacadesConstants.EXPRESS_DELIVERY, expressDeliveryText);
		}

		return finalDeliveryMode;
	}

	/**
	 * To get BuyBox data
	 *
	 * @param productData
	 * @param ussid
	 * @return SellerInformationData
	 */
	private SellerInformationData buyboxdata(final ProductData productData, final String ussid)
	{

		final SellerInformationData buyBoxData = new SellerInformationData();
		try
		{

			if (null != productData && null != productData.getSeller() && !productData.getSeller().isEmpty())
			{
				for (final SellerInformationData seller : productData.getSeller())
				{
					if (null != seller.getUssid() && null != ussid && seller.getUssid().equalsIgnoreCase(ussid)) //for buy box seller
					{
						if (null != seller.getSpPrice())
						{
							buyBoxData.setSpPrice(seller.getSpPrice());
						}
						if (null != seller.getMopPrice())
						{
							buyBoxData.setMopPrice(seller.getMopPrice());
						}
						if (null != seller.getMopPrice())
						{
							buyBoxData.setMrpPrice(seller.getMrpPrice());
						}
						if (null != seller.getUssid())
						{
							buyBoxData.setUssid(seller.getUssid());
						}
						if (null != seller.getSellername())
						{
							buyBoxData.setSellername(seller.getSellername());
						}
						if (null != seller.getSellerID())
						{
							buyBoxData.setSellerID(seller.getSellerID());
						}
						if (null != seller.getIsCod())
						{
							buyBoxData.setIsCod(seller.getIsCod());
						}
						if (null != seller.getReturnPolicy())
						{
							buyBoxData.setReturnPolicy(seller.getReturnPolicy());
						}
						if (null != seller.getReplacement())
						{
							buyBoxData.setReplacement(seller.getReplacement());
						}
						if (null != seller.getShippingMode())
						{
							buyBoxData.setShippingMode(seller.getShippingMode());
						}
						if (null != seller.getFullfillment())
						{
							buyBoxData.setFullfillment(seller.getFullfillment());
						}

						if (null != seller.getAvailableStock())
						{
							buyBoxData.setAvailableStock(seller.getAvailableStock());
						}

						if (null != seller.getDeliveryModes())
						{
							buyBoxData.setDeliveryModes(seller.getDeliveryModes());
						}
						if (null != seller.getBuyBoxWeightage())
						{
							buyBoxData.setBuyBoxWeightage(seller.getBuyBoxWeightage());
						}
					}
				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		return buyBoxData;
	}


	/**
	 * get other seller details for the product code
	 *
	 * @param productCode
	 * @param ussid
	 * @return List<SellerInformationData>
	 */
	private List<SellerInformationData> getOtherSellerDetails(final String productCode, final String ussid)
	{
		List<SellerInformationData> allSellerList = null;
		final List<SellerInformationData> otherSellerList = new ArrayList<SellerInformationData>();
		try
		{
			if (null != productCode)
			{
				allSellerList = buyBoxFacade.getsellersDetails(productCode);
			}
			if (null != allSellerList && !allSellerList.isEmpty())
			{
				for (final SellerInformationData seller : allSellerList)
				{
					if (null != seller.getUssid() && null != ussid && !(seller.getUssid().equalsIgnoreCase(ussid)))
					{
						otherSellerList.add(seller);
					}
				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		return otherSellerList;
	}


	/**
	 * framing the seller details as per mobile
	 *
	 * @param otherSellerList
	 * @return List<SellerInformationMobileData>
	 */
	private List<SellerInformationMobileData> frameOtherSellerDetails(final List<SellerInformationData> otherSellerList,
			final ProductModel productModel)
	{

		final List<SellerInformationMobileData> othersellerDataList = new ArrayList<SellerInformationMobileData>();
		try
		{
			for (final SellerInformationData seller : otherSellerList)
			{
				String isEMIeligible = null;
				final SellerInformationMobileData sellerMobileData = new SellerInformationMobileData();
				if (null != seller.getSpPrice() && null != seller.getSpPrice().getFormattedValue()
						&& null != seller.getSpPrice().getValue() && seller.getSpPrice().getValue().compareTo(BigDecimal.ZERO) > 0)
				{
					sellerMobileData.setSellerSpecialPrice(seller.getSpPrice().getFormattedValue().toString());
				}
				if (null != seller.getMopPrice() && null != seller.getMopPrice().getFormattedValue()
						&& null != seller.getMopPrice().getValue() && seller.getMopPrice().getValue().compareTo(BigDecimal.ZERO) > 0)
				{
					sellerMobileData.setSellerMOP(seller.getMopPrice().getFormattedValue().toString());
				}
				if (null != seller.getMrpPrice() && null != seller.getMrpPrice().getFormattedValue()
						&& null != seller.getMrpPrice().getValue() && seller.getMrpPrice().getValue().compareTo(BigDecimal.ZERO) > 0)
				{
					sellerMobileData.setSellerMRP(seller.getMrpPrice().getFormattedValue().toString());
				}
				if (null != seller.getAvailableStock())
				{
					sellerMobileData.setAvailableStock(seller.getAvailableStock().toString());
				}

				if (null != seller.getSellerID())
				{
					sellerMobileData.setSellerId(seller.getSellerID());
				}
				if (null != seller.getSellername())
				{
					sellerMobileData.setSellerName(seller.getSellername());
				}
				if (null != seller.getIsCod())
				{
					sellerMobileData.setIsCOD(seller.getIsCod());
				}
				if (null != seller.getFullfillment())
				{
					sellerMobileData.setFullfillmentType(seller.getFullfillment());
				}
				if (null != seller.getReturnPolicy())
				{
					sellerMobileData.setReturnPolicy(seller.getReturnPolicy());
				}
				if (null != seller.getReplacement())
				{
					sellerMobileData.setReplacement(seller.getReplacement());
				}
				if (null != seller.getSellerAssociationstatus())
				{
					sellerMobileData.setSellerAssociationstatus(MarketplacecommerceservicesConstants.Y);
				}
				else
				{
					sellerMobileData.setSellerAssociationstatus(MarketplacecommerceservicesConstants.N);
				}
				if (null != seller.getSpPrice() && null != seller.getSpPrice().getValue()
						&& seller.getSpPrice().getValue().compareTo(BigDecimal.ZERO) > 0)
				{
					isEMIeligible = getEMIforProduct(seller.getSpPrice().getValue());
				}
				else if (null != seller.getMopPrice() && null != seller.getMopPrice().getValue()
						&& seller.getMopPrice().getValue().compareTo(BigDecimal.ZERO) > 0)
				{
					isEMIeligible = getEMIforProduct(seller.getMopPrice().getValue());
				}
				if (null != isEMIeligible)
				{
					sellerMobileData.setIsEMIEligible(isEMIeligible);
				}
				if (null != seller.getUssid())
				{
					sellerMobileData.setUSSID(seller.getUssid());
				}
				if (null != seller.getBuyBoxWeightage())
				{
					sellerMobileData.setBuyBoxWeightage(seller.getBuyBoxWeightage().toString());
				}
				if (null != getEligibleDeliveryModes(seller))
				{
					sellerMobileData.setEligibleDeliveryModes(getEligibleDeliveryModes(seller));
				}

				final List<String> deliveryInfoList = new ArrayList<String>();
				deliveryInfoList.add(MarketplacewebservicesConstants.EXPRESS_DELIVERY);
				deliveryInfoList.add(MarketplacewebservicesConstants.HOME_DELIVERY);
				Map<String, String> deliveryModesForProduct = new HashMap<String, String>();
				Map<String, Map<String, Integer>> deliveryModesATPForProduct = new HashMap<String, Map<String, Integer>>();
				deliveryModesATPForProduct = productDetailsHelper.getDeliveryModeATMap(deliveryInfoList);
				if (null != deliveryModesATPForProduct && !deliveryModesATPForProduct.isEmpty()
						&& StringUtils.isNotEmpty(seller.getUssid()))
				{
					deliveryModesForProduct = getDeliveryModes(productModel, deliveryModesATPForProduct, seller.getUssid());
					if (null != deliveryModesForProduct && !deliveryModesForProduct.isEmpty())
					{
						sellerMobileData.setDeliveryModesATP(deliveryModesForProduct);
					}
				}
				othersellerDataList.add(sellerMobileData);
				LOG.debug("****************** Other sellers added PDP mobile web service *********************");

			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		return othersellerDataList;
	}

	/**
	 * get Gallery Images such as thumbnail,zoom etc for a product code
	 *
	 * @param productData
	 * @return List<Map<String, ImageData>>
	 */
	private List<GalleryImageData> getGalleryImages(final ProductData productData)
	{
		List<GalleryImageData> galleryImageList = new ArrayList<GalleryImageData>();
		if (null != productDetailsHelper.getGalleryImagesMobile(productData)
				&& !productDetailsHelper.getGalleryImagesMobile(productData).isEmpty())
		{
			galleryImageList = productDetailsHelper.getGalleryImagesMobile(productData);
		}
		return galleryImageList;
	}

	/**
	 * get the highest promotion
	 *
	 * @param productData
	 * @return PromotionData
	 */
	private PromotionData getHighestPromotion(final ProductData productData)
	{
		PromotionData highestPromotion = null;
		try
		{
			Collection<PromotionData> promotioncollection = null;
			if (null != productData.getPotentialPromotions() && !productData.getPotentialPromotions().isEmpty())
			{
				promotioncollection = productData.getPotentialPromotions();
			}
			final List<PromotionData> enabledPromotionList = new ArrayList<PromotionData>();
			final Date todays_Date = new Date();
			if (null != promotioncollection && !promotioncollection.isEmpty())
			{
				for (final PromotionData promodata : promotioncollection)
				{
					if (null != promodata.getEnabled() && promodata.getEnabled().booleanValue() && null != promodata.getStartDate()
							&& null != promodata.getEndDate() && todays_Date.after(promodata.getStartDate())
							&& todays_Date.before(promodata.getEndDate()))
					{
						if (null != promodata.getChannels() && !promodata.getChannels().isEmpty())
						{
							for (final String promoChannel : promodata.getChannels())
							{
								if (promoChannel.equalsIgnoreCase(SalesApplication.MOBILE.getCode()))
								{
									enabledPromotionList.add(promodata);
								}
							}
						}
						else
						{
							enabledPromotionList.add(promodata);
						}
					}

				}
				if (!enabledPromotionList.isEmpty())
				{
					highestPromotion = checkHighestPriority(enabledPromotionList);
				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		return highestPromotion;
	}

	/**
	 * check highest promotion priority
	 *
	 * @param enabledPromotionList
	 * @return PromotionData
	 */
	private PromotionData checkHighestPriority(final List<PromotionData> enabledPromotionList)
	{
		Collections.sort(enabledPromotionList, new Comparator<PromotionData>()
		{
			@Override
			public int compare(final PromotionData promo1, final PromotionData promo2)
			{
				int priority = 0;
				if (null != promo1.getPriority() && null != promo2.getPriority())
				{
					priority = promo1.getPriority().compareTo(promo2.getPriority());
				}
				return priority;
			}


		});
		Collections.reverse(enabledPromotionList);
		return enabledPromotionList.get(0);
	}

	/**
	 * All Potential promotions for a product
	 *
	 * @param productData
	 * @param otherSellers
	 * @param buyBoxData
	 * @return List<PromotionMobileData>
	 */
	private PromotionMobileData getPromotionsForProduct(final ProductData productData, final BuyBoxData buyBoxData,
			final List<SellerInformationMobileData> otherSellers)
	{
		final PromotionData highestPrmotion = getHighestPromotion(productData);
		PromotionMobileData promoMobiledata = null;
		try
		{
			if (null != highestPrmotion)
			{
				promoMobiledata = new PromotionMobileData();
			}
			if (null != highestPrmotion && null != highestPrmotion.getTitle())
			{
				promoMobiledata.setTitle(highestPrmotion.getTitle());
			}
			if (null != highestPrmotion && null != highestPrmotion.getStartDate())
			{
				promoMobiledata.setStartDate(highestPrmotion.getStartDate().toString());
			}
			if (null != highestPrmotion && null != highestPrmotion.getEndDate())
			{
				promoMobiledata.setEndDate(highestPrmotion.getEndDate().toString());
			}
			if (null != highestPrmotion && null != highestPrmotion.getDescription())
			{
				promoMobiledata.setDescription(highestPrmotion.getDescription());
			}
			List<String> allowedSellers = null;
			final List<String> totalUssidListOfProduct = new ArrayList<String>();

			if (null != highestPrmotion && null != highestPrmotion.getAllowedSellers()
					&& !highestPrmotion.getAllowedSellers().isEmpty())
			{
				allowedSellers = highestPrmotion.getAllowedSellers();
			}
			if (null != buyBoxData && null != buyBoxData.getSellerId() && !buyBoxData.getSellerId().isEmpty()
					&& null != buyBoxData.getSellerArticleSKU() && !buyBoxData.getSellerArticleSKU().isEmpty()
					&& null != allowedSellers && !allowedSellers.isEmpty() && allowedSellers.contains(buyBoxData.getSellerId()))
			{
				totalUssidListOfProduct.add(buyBoxData.getSellerArticleSKU());
			}
			if (null != otherSellers && !otherSellers.isEmpty())
			{
				for (final SellerInformationMobileData otherSeller : otherSellers)
				{
					if (null != otherSeller.getSellerId() && !otherSeller.getSellerId().isEmpty() && null != otherSeller.getUSSID()
							&& !otherSeller.getUSSID().isEmpty() && null != allowedSellers && !allowedSellers.isEmpty()
							&& allowedSellers.contains(otherSeller.getSellerId()))
					{
						totalUssidListOfProduct.add(otherSeller.getUSSID());
					}
				}
			}
			if (CollectionUtils.isNotEmpty(totalUssidListOfProduct))
			{
				promoMobiledata.setEligibleUSSIDList(totalUssidListOfProduct);
			}

			if (null != highestPrmotion && null != highestPrmotion.getGiftProduct())
			{
				final GiftProductMobileData giftProduct = new GiftProductMobileData();
				if (null != highestPrmotion.getGiftProduct() && null != highestPrmotion.getGiftProduct().get(0)
						&& null != highestPrmotion.getGiftProduct().get(0).getDescription())
				{
					giftProduct.setDescription(highestPrmotion.getGiftProduct().get(0).getDescription());
				}

				else if (null != highestPrmotion.getGiftProduct() && null != highestPrmotion.getGiftProduct().get(0)
						&& null != highestPrmotion.getGiftProduct().get(0).getName())
				{
					giftProduct.setDescription(highestPrmotion.getGiftProduct().get(0).getName());
				}
				if (null != highestPrmotion.getGiftProduct() && null != highestPrmotion.getGiftProduct().get(0)
						&& null != highestPrmotion.getGiftProduct().get(0).getListingId())
				{
					giftProduct.setProductCode(highestPrmotion.getGiftProduct().get(0).getListingId());
				}
				if (null != highestPrmotion.getGiftProduct() && null != highestPrmotion.getGiftProduct().get(0))
				{
					giftProduct.setProductImageUrl(getPrimaryImageForProductAndFormat(highestPrmotion.getGiftProduct().get(0),
							MarketplacecommerceservicesConstants.THUMBNAIL));
				}
				promoMobiledata.setGiftProduct(giftProduct);

			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		return promoMobiledata;
	}

	/**
	 * Get specifications of a product
	 *
	 * @param productData
	 * @return Map<String, String>
	 */
	private List<ClassificationMobileWsData> getSpecificationsOfProductByGroup(final ProductData productData)
	{
		ClassificationMobileWsData classification = null;
		final List<ClassificationMobileWsData> specificationsList = new ArrayList<ClassificationMobileWsData>();
		try
		{

			productDetailsHelper.groupGlassificationData(productData);
			/* Checking the presence of classification attributes */
			if (null != productData.getClassifications())
			{
				final List<ClassificationData> ConfigurableAttributeList = new ArrayList<ClassificationData>(
						productData.getClassifications());
				for (final ClassificationData configurableAttributData : ConfigurableAttributeList)
				{
					classification = new ClassificationMobileWsData();
					final Map<String, String> mapConfigurableAttribute = new HashMap<String, String>();
					final List<FeatureData> featureDataList = new ArrayList<FeatureData>(configurableAttributData.getFeatures());
					for (final FeatureData featureData : featureDataList)
					{

						final List<FeatureValueData> featureValueList = new ArrayList<FeatureValueData>(featureData.getFeatureValues());
						if (null != productData.getRootCategory())
						{
							final String properitsValue = configurationService.getConfiguration().getString(
									MarketplacewebservicesConstants.CONFIGURABLE_ATTRIBUTE + productData.getRootCategory());
							//apparel
							final FeatureValueData featureValueData = featureValueList.get(0);
							if (productData.getRootCategory().equalsIgnoreCase(MarketplacewebservicesConstants.CLOTHING))
							{

								if (null != properitsValue && featureValueData.getValue() != null
										&& properitsValue.contains(featureData.getName()))
								{
									mapConfigurableAttribute.put(featureData.getName(), featureValueData.getValue());
								}

							} //end apparel
							  //electronics
							else
							{
								if (null != featureData.getName() && null != featureValueData.getValue())

								{
									mapConfigurableAttribute.put(featureData.getName(), featureValueData.getValue());
								}

							}
						}
					}
					if (null != configurableAttributData.getName() && !configurableAttributData.getName().isEmpty())
					{
						classification.setGroupName(configurableAttributData.getName());
					}
					classification.setSpecifications(mapConfigurableAttribute);
					specificationsList.add(classification);
				}
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		return specificationsList;
	}


	/**
	 * Get specifications of a product
	 *
	 * @param productData
	 * @return Map<String, String>
	 */
	private Map<String, String> getSpecificationsOfProduct(final ProductData productData)
	{
		final Map<String, String> mapConfigurableAttribute = new HashMap<String, String>();
		try
		{
			/* Checking the presence of classification attributes */
			if (null != productData.getClassifications())
			{
				final List<ClassificationData> ConfigurableAttributeList = new ArrayList<ClassificationData>(
						productData.getClassifications());
				for (final ClassificationData configurableAttributData : ConfigurableAttributeList)
				{
					final List<FeatureData> featureDataList = new ArrayList<FeatureData>(configurableAttributData.getFeatures());
					for (final FeatureData featureData : featureDataList)
					{

						final List<FeatureValueData> featureValueList = new ArrayList<FeatureValueData>(featureData.getFeatureValues());
						if (null != productData.getRootCategory())
						{
							final String properitsValue = configurationService.getConfiguration().getString(
									MarketplacewebservicesConstants.CONFIGURABLE_ATTRIBUTE + productData.getRootCategory());
							//apparel
							final FeatureValueData featureValueData = featureValueList.get(0);
							if (productData.getRootCategory().equalsIgnoreCase(MarketplacewebservicesConstants.CLOTHING))
							{

								if (null != properitsValue && featureValueData.getValue() != null
										&& properitsValue.contains(featureData.getName()))
								{
									mapConfigurableAttribute.put(featureData.getName(), featureValueData.getValue());
								}

							} //end apparel
							  //electronics
							else
							{
								if (null != featureData.getName() && null != featureValueData.getValue())

								{
									mapConfigurableAttribute.put(featureData.getName(), featureValueData.getValue());
								}

							}
						}
					}
				}
			}

		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		return mapConfigurableAttribute;
	}

	/**
	 * sales category of product
	 *
	 * @param productData
	 * @return String
	 */
	private String getCategoryOfProduct(final ProductData productData)
	{
		String productCategory = null;
		try
		{
			if (null != productData.getCategories() && !productData.getCategories().isEmpty())
			{
				for (final CategoryData category : productData.getCategories())
				{
					if (null != category
							&& null != category.getCode()
							&& null != configurationService.getConfiguration().getString(
									MarketplacecommerceservicesConstants.SALESCATEGORYTYPE)
							&& category.getCode().startsWith(
									configurationService.getConfiguration().getString(
											MarketplacecommerceservicesConstants.SALESCATEGORYTYPE)) && null != category.getName())
					{
						productCategory = category.getName();
						return productCategory;
					}
				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		return productCategory;
	}

	/**
	 * sales category of product
	 *
	 * @param productData
	 * @return String
	 */
	@Override
	public String getCategoryCodeOfProduct(final ProductData productData)
	{
		String productCategory = null;
		try
		{
			if (null != productData.getCategories() && !productData.getCategories().isEmpty())
			{
				for (final CategoryData category : productData.getCategories())
				{
					if (null != category
							&& null != category.getCode()
							&& null != configurationService.getConfiguration().getString(
									MarketplacecommerceservicesConstants.SALESCATEGORYTYPE)
							&& category.getCode().startsWith(
									configurationService.getConfiguration().getString(
											MarketplacecommerceservicesConstants.SALESCATEGORYTYPE)) && null != category.getName())
					{
						productCategory = category.getCode();
						return productCategory;
					}
				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		return productCategory;
	}

	/**
	 * variant details such as color and size link for a product
	 *
	 * @param productData
	 * @return List<VariantOptionMobileData>
	 */
	private List<VariantOptionMobileData> getVariantDetailsForProduct(final ProductData productData)
	{
		final List<VariantOptionMobileData> variantDataList = new ArrayList<VariantOptionMobileData>();
		try
		{
			if (null != productData.getVariantOptions())
			{
				for (final VariantOptionData variantData : productData.getVariantOptions())
				{
					final VariantOptionMobileData variantMobileData = new VariantOptionMobileData();
					final ColorLinkData colorLinkData = new ColorLinkData();
					SizeLinkData sizeLinkData = null;
					CapacityLinkData capacityLinkData = null;
					if (null != variantData.getColour())
					{
						colorLinkData.setColor(variantData.getColour());
					}
					//checking for colour hex code
					if (null != variantData.getColourCode())
					{
						colorLinkData.setColorHexCode(variantData.getColourCode());
					}
					if (null != variantData.getUrl())
					{
						colorLinkData.setColorurl(variantData.getUrl());
					}
					if (null != variantData.getSizeLink() && !variantData.getSizeLink().isEmpty())
					{
						for (final Map.Entry<String, String> sizeEntry : variantData.getSizeLink().entrySet())
						{
							sizeLinkData = new SizeLinkData();
							if (null != sizeEntry.getValue())
							{
								sizeLinkData.setSize(sizeEntry.getValue());
							}
							if (null != sizeEntry.getKey())
							{
								sizeLinkData.setUrl(sizeEntry.getKey());
							}
						}
					}
					if (null != variantData.getCapacity())
					{
						capacityLinkData = new CapacityLinkData();
						capacityLinkData.setCapacity(variantData.getCapacity());
						if (null != variantData.getUrl())
						{
							capacityLinkData.setUrl(variantData.getUrl());
						}

					}
					variantMobileData.setColorlink(colorLinkData);
					if (null != sizeLinkData)
					{
						variantMobileData.setSizelink(sizeLinkData);
					}
					if (null != capacityLinkData)
					{
						variantMobileData.setCapacityLink(capacityLinkData);
					}
					variantDataList.add(variantMobileData);
				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		return variantDataList;
	}

	private String getEMIforProduct(final BigDecimal price)
	{
		String isEMIEligible = null;
		try
		{
			final String emiCuttOffAmount = configurationService.getConfiguration().getString(
					MarketplacewebservicesConstants.EMI_CUT_OFF_LIMIT);
			BigDecimal emiLimit = null;
			if (null != emiCuttOffAmount)
			{
				emiLimit = new BigDecimal(emiCuttOffAmount);
			}
			if (price.compareTo(BigDecimal.ZERO) > 0 && price.compareTo(emiLimit) > 0)
			{
				isEMIEligible = Y;
			}
			else
			{
				isEMIEligible = N;
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		return isEMIEligible;
	}

	/**
	 * This method is responsible for getting the RICH ATTRIBUTE DETAILS
	 *
	 * @param productModel
	 * @return productMobileData
	 */
	private ProductDetailMobileWsData getRichAttributes(final ProductModel productModel)
	{
		boolean onlineExclusive = false;
		Date existDate = null;
		final ProductDetailMobileWsData productMobileData = new ProductDetailMobileWsData();
		final String allowNew = configurationService.getConfiguration().getString("attribute.new.display");
		for (final SellerInformationModel seller : productModel.getSellerInformationRelator())
		{
			if (null != seller.getOnlineExclusive()
					&& (OnlineExclusiveEnum.YES).toString().equalsIgnoreCase(seller.getOnlineExclusive().getCode()))
			{
				onlineExclusive = true;
			}
			if (null != allowNew && allowNew.equalsIgnoreCase("Y"))
			{
				//Find the oldest startDate of the seller
				if (null == existDate)
				{
					existDate = seller.getStartDate();
				}
				else if (existDate.after(seller.getStartDate()))
				{
					existDate = seller.getStartDate();
				}
			}

		}
		//New Attribute
		if (null != existDate && isNew(existDate))
		{
			productMobileData.setIsProductNew(Y);
		}
		else if (null != existDate && !isNew(existDate))
		{
			productMobileData.setIsProductNew(N);
		}

		if (onlineExclusive)
		{
			productMobileData.setIsOnlineExclusive(Y);
		}
		else
		{
			productMobileData.setIsOnlineExclusive(N);
		}

		return productMobileData;
	}

	/**
	 * check if the product is new
	 *
	 * @param existDate
	 * @return boolean
	 */
	private boolean isNew(final Date existDate)
	{
		boolean newAttr = false;
		if (null != existDate)
		{
			final Date sysDate = new Date();
			final long dayDiff = calculateDays(existDate, sysDate);

			LOG.info("******" + existDate + "  --- dayDiff: " + dayDiff);
			final String validDaysSt = configurationService.getConfiguration().getString("attribute.new.validDays");
			final int validDays = validDaysSt == null ? 0 : Integer.parseInt(validDaysSt);

			if (validDays > dayDiff)
			{
				newAttr = true;
			}
		}

		return newAttr;
	}

	private long calculateDays(final Date dateEarly, final Date dateLater)
	{
		return (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000);
	}


	public String getPrimaryImageForProductAndFormat(final ProductData product, final String format)
	{
		if (product != null && format != null)
		{
			final Collection<ImageData> images = product.getImages();
			if (images != null && !images.isEmpty())
			{
				for (final ImageData image : images)
				{
					if (ImageDataType.PRIMARY.equals(image.getImageType()) && format.equals(image.getFormat())
							&& StringUtils.isNotEmpty(image.getUrl()))
					{
						return image.getUrl();
					}
				}
			}
		}
		return null;
	}

	/**
	 * @return the buyBoxFacade
	 */
	public BuyBoxFacade getBuyBoxFacade()
	{
		return buyBoxFacade;
	}

	/**
	 * @param buyBoxFacade
	 *           the buyBoxFacade to set
	 */
	public void setBuyBoxFacade(final BuyBoxFacade buyBoxFacade)
	{
		this.buyBoxFacade = buyBoxFacade;
	}
}
