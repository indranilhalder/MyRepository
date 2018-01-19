/**
 *
 */
package com.tisl.mpl.service.impl;

import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSImageComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSContentSlotService;
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
import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.JewellerySellerDetailsModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.enums.KeywordRedirectMatchType;
import de.hybris.platform.solrfacetsearch.handler.KeywordRedirectHandler;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrFacetSearchKeywordRedirectModel;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrURIRedirectModel;
import de.hybris.platform.solrfacetsearch.search.KeywordRedirectSorter;
import de.hybris.platform.solrfacetsearch.search.SolrFacetSearchKeywordDao;
import de.hybris.platform.util.localization.Localization;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.constants.MplConstants;
import com.tisl.mpl.core.constants.MarketplaceCoreConstants;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.core.model.VideoComponentModel;
import com.tisl.mpl.data.PriceBreakupData;
import com.tisl.mpl.enums.OnlineExclusiveEnum;
import com.tisl.mpl.enums.SellerAssociationStatusEnum;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.product.MplProductFacade;
import com.tisl.mpl.facade.product.PriceBreakupFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.helper.ProductDetailsHelper;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.marketplacecommerceservices.daos.MplKeywordRedirectDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplCmsPageService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.seller.product.facades.ProductOfferDetailFacade;
import com.tisl.mpl.service.MplProductWebService;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.wsdto.CapacityLinkData;
import com.tisl.mpl.wsdto.ClassificationDTO;
import com.tisl.mpl.wsdto.ClassificationDTOLister;
import com.tisl.mpl.wsdto.ClassificationMobileWsData;
import com.tisl.mpl.wsdto.Classifications;
import com.tisl.mpl.wsdto.ColorLinkData;
import com.tisl.mpl.wsdto.DeliveryModeData;
import com.tisl.mpl.wsdto.ExchangeLinkUrl;
import com.tisl.mpl.wsdto.FineJwlryClassificationListDTO;
import com.tisl.mpl.wsdto.FineJwlryClassificationListValueDTO;
import com.tisl.mpl.wsdto.GalleryImageData;
import com.tisl.mpl.wsdto.GiftProductMobileData;
import com.tisl.mpl.wsdto.KnowMoreDTO;
import com.tisl.mpl.wsdto.PriceBreakUpDto;
import com.tisl.mpl.wsdto.ProductAPlusWsData;
import com.tisl.mpl.wsdto.ProductContentWsData;
import com.tisl.mpl.wsdto.ProductDetailMobileWsData;
import com.tisl.mpl.wsdto.ProductOfferMsgDTO;
import com.tisl.mpl.wsdto.PromotionMobileData;
import com.tisl.mpl.wsdto.RefundReturnDTO;
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
	@Resource
	private SolrFacetSearchKeywordDao solrFacetSearchKeywordDao;
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	private KeywordRedirectSorter keywordRedirectSorter;
	@Resource
	private ModelService modelService;
	@Resource
	private ProductDetailsHelper productDetailsHelper;
	@Resource
	private MplKeywordRedirectDao mplKeywordRedirectDao;
	@Resource
	private MplProductFacade mplProductFacade;

	private Map<KeywordRedirectMatchType, KeywordRedirectHandler> redirectHandlers;




	private static final String Y = "Y";
	private static final String N = "N";

	private static final String HTTP = "http";
	private static final String HTTPS = "https";


	private static final Logger LOG = Logger.getLogger(MplProductWebServiceImpl.class);

	@Resource(name = "prodOfferDetFacade")
	private ProductOfferDetailFacade prodOfferDetFacade;

	@Resource(name = "cmsPageService")
	private MplCmsPageService mplCmsPageService;
	@Resource
	private SiteConfigService siteConfigService;
	private static final String MAXIMUM_CONFIGURED_QUANTIY = "mpl.cart.maximumConfiguredQuantity.lineItem";

	//sonar fix
	/*
	 * @Resource(name = "mplProductWebService") private MplProductWebServiceImpl mplProductWebServiceImpl;
	 */
	@Resource(name = "mplJewelleryService")
	private MplJewelleryService jewelleryService;

	// Jewellery Changes
	@Resource(name = "priceBreakupFacade")
	private PriceBreakupFacade priceBreakupFacade;
	@Autowired
	private DefaultCMSContentSlotService contentSlotService;


	/**
	 * @throws CMSItemNotFoundException
	 * @desc This service fetches all the details of A+ content based on product code
	 */
	@Override
	public ProductAPlusWsData getAPluscontentForProductCode(String productCode) throws EtailNonBusinessExceptions,
			CMSItemNotFoundException
	{

		if (null != productCode)
		{
			productCode = productCode.toUpperCase();
		}
		ContentPageModel contentPage = null;
		List<String> contentList = null;
		List<String> imageList = null;
		List<String> videoList = null;
		//		final ProductAPlusWsData productAPlus = new ProductAPlusWsData();
		ProductAPlusWsData productAPlus = null;
		//		final HashMap<String, ProductContentWsData> productContentDataMap = new HashMap<String, ProductContentWsData>();
		HashMap<String, ProductContentWsData> productContentDataMap = null;
		final ProductModel productModel = productService.getProductForCode(productCode);
		try
		{

			contentPage = getContentPageForProduct(productModel);
			if (null != contentPage)
			{
				productAPlus = new ProductAPlusWsData();
				productContentDataMap = new HashMap<String, ProductContentWsData>();
				for (final ContentSlotForPageModel contentSlotForPageModel : contentPage.getContentSlots())
				{
					final ProductContentWsData productContentData = new ProductContentWsData();
					contentList = new ArrayList<String>();
					imageList = new ArrayList<String>();
					videoList = new ArrayList<String>();


					for (final AbstractCMSComponentModel abstractCMSComponentModel : contentSlotForPageModel.getContentSlot()
							.getCmsComponents())
					{

						if (abstractCMSComponentModel instanceof CMSParagraphComponentModel)
						{
							final CMSParagraphComponentModel paragraphComponent = (CMSParagraphComponentModel) abstractCMSComponentModel;
							contentList.add(paragraphComponent.getContent());
						}

						if (abstractCMSComponentModel instanceof CMSImageComponentModel)
						{
							final CMSImageComponentModel cmsImageComponent = (CMSImageComponentModel) abstractCMSComponentModel;
							imageList.add(cmsImageComponent.getMedia().getUrl2());
						}

						if (abstractCMSComponentModel instanceof SimpleBannerComponentModel)
						{
							final SimpleBannerComponentModel bannerComponent = (SimpleBannerComponentModel) abstractCMSComponentModel;
							if (bannerComponent.getMedia() != null && StringUtils.isNotEmpty(bannerComponent.getMedia().getUrl2()))
							{

								imageList.add(bannerComponent.getMedia().getUrl2());


							}
							else if (StringUtils.isNotEmpty(bannerComponent.getUrlLink()))
							{

								imageList.add(bannerComponent.getUrlLink());


							}
						}

						if (abstractCMSComponentModel instanceof VideoComponentModel)
						{
							final VideoComponentModel bannerComponent = (VideoComponentModel) abstractCMSComponentModel;
							videoList.add(bannerComponent.getVideoUrl());
						}

					}

					productContentData.setTextList(contentList);
					productContentData.setImageList(imageList);
					productContentData.setVideoList(videoList);
					productContentDataMap.put(contentSlotForPageModel.getPosition(), productContentData);

				}
			} //final end of if
			if (null != contentPage && StringUtils.isNotEmpty(contentPage.getLabelOrId()))
			{
				productAPlus.setTemlateName(contentPage.getLabelOrId());
			}
			if (MapUtils.isNotEmpty(productContentDataMap))
			{
				productAPlus.setProductContent(productContentDataMap);
			}
		}
		catch (final CMSItemNotFoundException e)
		{
			throw e;
		}
		return productAPlus;
	}

	/**
	 * @desc get the content page for the provded product code
	 * @param product
	 * @return ContentPageModel
	 * @throws CMSItemNotFoundException
	 */
	private ContentPageModel getContentPageForProduct(final ProductModel product) throws CMSItemNotFoundException
	{
		final ContentPageModel productContentPage = mplCmsPageService.getContentPageForProduct(product);
		//		if (productContentPage == null)
		//		{
		//			throw new CMSItemNotFoundException("Could not find a product content for the product" + product.getName());
		//		}

		if (null != productContentPage)
		{

			return productContentPage;
		}
		else
		{
			return null;
		}
	}

	/*
	 * To get product details for a product code
	 *
	 * @see com.tisl.mpl.service.MplProductWebService#getProductdetailsForProductCode(java.lang.String)
	 */
	@Override
	public ProductDetailMobileWsData getProductdetailsForProductCode(final String productCode, final String baseUrl,
			final String channel) throws EtailNonBusinessExceptions
	{
		final ProductDetailMobileWsData productDetailMobile = new ProductDetailMobileWsData();
		ProductDetailMobileWsData isNewOrOnlineExclusive = new ProductDetailMobileWsData();
		String isEMIeligible = null;
		BuyBoxData buyBoxData = null;
		String ussid = null;
		String isProductCOD = null;
		ProductData productData = null;
		ProductModel productModel = null;
		List<SellerInformationMobileData> framedOtherSellerDataList = null;
		List<SellerInformationData> otherSellerDataList = null;
		List<KnowMoreDTO> knowMoreList = null;
		PromotionMobileData potenitalPromo = null;
		List<VariantOptionMobileData> variantDataList = new ArrayList<VariantOptionMobileData>();
		final StringBuilder allVariants = new StringBuilder();
		String variantCodes = "";
		String variantsString = "";
		final Map<String, Integer> stockAvailibilty = new TreeMap<String, Integer>();
		List<ClassificationMobileWsData> specificationsList = null;
		//	PromotionMobileData potenitalPromo = null;
		final boolean specialMobileFlag = configurationService.getConfiguration().getBoolean(
				MarketplacewebservicesConstants.SPECIAL_MOBILE_FLAG, false);
		List<PriceBreakupData> priceMap = new ArrayList<PriceBreakupData>();
		final List<PriceBreakUpDto> priceBreakUpList = new ArrayList<PriceBreakUpDto>();
		final Map<String, FineJwlryClassificationListDTO> fineJewelleryClassificationList = new LinkedHashMap<String, FineJwlryClassificationListDTO>();
		String ussidJwlry = "";

		//Added for TPR-6869
		String sellerID = MarketplacecommerceservicesConstants.EMPTY;
		String sellerMonogramMessage = MarketplacecommerceservicesConstants.EMPTY;
		String buyingGuideURL = MarketplacecommerceservicesConstants.EMPTY;

		//for new tab return refund for fine and fashion jewellery
		List<RefundReturnDTO> refundReturnList = null;
		try
		{

			String sharedText = Localization.getLocalizedString(MarketplacewebservicesConstants.PDP_SHARED_PRE);
			//CAR-86
			//productModel = productService.getProductForCode(defaultPromotionManager.catalogData(), productCode);
			productModel = productService.getProductForCode(productCode);
			if (null != productModel)
			{
				productData = productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC,
						ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.GALLERY, ProductOption.CATEGORIES,
						ProductOption.PROMOTIONS, ProductOption.CLASSIFICATION, ProductOption.VARIANT_FULL, ProductOption.SELLER));

				//Added for TPR-6869

				buyingGuideURL = populateBuyingGuide(productModel);

				//TISPT-396
				/*
				 * productData = productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC,
				 * ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.GALLERY,
				 * ProductOption.CATEGORIES, ProductOption.REVIEW, ProductOption.PROMOTIONS, ProductOption.CLASSIFICATION,
				 * ProductOption.VARIANT_FULL, ProductOption.VOLUME_PRICES, ProductOption.DELIVERY_MODE_AVAILABILITY,
				 * ProductOption.SELLER));
				 */
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9037);
			}

			if (null != productCode)
			{
				try
				{
					//TPR-797---TISSTRT-1403 TISPRM-56
					if (CollectionUtils.isNotEmpty(productData.getAllVariantsId()))
					{
						//get left over variants
						if (productData.getAllVariantsId().size() > 1)
						{
							productData.getAllVariantsId().remove(productData.getCode());
							for (final String variants : productData.getAllVariantsId())
							{
								allVariants.append(variants).append(',');
							}
							final int length = allVariants.length();
							variantCodes = allVariants.substring(0, length - 1);
						}
					}
					if (StringUtils.isNotEmpty(productData.getCode()))
					{
						variantsString = productData.getCode() + "," + variantCodes;
					}


					//CKD:TPR-250:Start
					//final Map<String, Object> buydata = buyBoxFacade.buyboxPricePDP(variantsString);
					//final Map<String, Object> buydata = buyBoxFacade.buyboxPricePDP(variantsString, null);
					final Map<String, Object> buydata = buyBoxFacade.buyboxPricePDP(variantsString, null, channel);
					//CKD:TPR-250:End
					if (MapUtils.isNotEmpty(buydata))
					{
						final List<String> noStockPCodes = (List<String>) buydata.get("no_stock_p_codes");
						for (final String pCode : noStockPCodes)
						{
							stockAvailibilty.put(pCode, Integer.valueOf(0));
						}
						buyBoxData = (BuyBoxData) buydata.get("pdp_buy_box");

						//Added for TPR-6869
						sellerID = getSellerIDData(buydata);

						if (StringUtils.isNotEmpty(sellerID))
						{
							sellerMonogramMessage = buyBoxFacade.getSellerMonogrammingMsg(productCode, sellerID);
						}
					}
					//Commented for TPR-797
					//buyBoxData = buyBoxFacade.buyboxPrice(productCode);
					if (null == buyBoxData)
					{
						productDetailMobile.setSellerAssociationstatus(MarketplacecommerceservicesConstants.N);
					}
					// TPR-522
					if (null != buyBoxData.getMrp())
					{
						// Below codes are Channel specific promotion TISPRD-8944
						if (specialMobileFlag && buyBoxData.getSpecialPriceMobile() != null
								&& buyBoxData.getSpecialPriceMobile().getValue().doubleValue() > 0)
						{
							final double savingPriceCal = buyBoxData.getMrp().getDoubleValue().doubleValue()
									- buyBoxData.getSpecialPriceMobile().getDoubleValue().doubleValue();
							final double savingPriceCalPer = (savingPriceCal / buyBoxData.getMrp().getDoubleValue().doubleValue()) * 100;
							final double roundedOffValuebefore = Math.round(savingPriceCalPer * 100.0) / 100.0;
							final BigDecimal roundedOffValue = new BigDecimal((int) roundedOffValuebefore);
							//changed as per Ashish mail
							productDetailMobile.setDiscount(roundedOffValue.toString());

						}
						else if (!specialMobileFlag && buyBoxData.getSpecialPrice() != null
								&& buyBoxData.getSpecialPrice().getValue().doubleValue() > 0) //backward compatible
						{
							final double savingPriceCal = buyBoxData.getMrp().getDoubleValue().doubleValue()
									- buyBoxData.getSpecialPrice().getDoubleValue().doubleValue();
							final double savingPriceCalPer = (savingPriceCal / buyBoxData.getMrp().getDoubleValue().doubleValue()) * 100;
							final double roundedOffValuebefore = Math.round(savingPriceCalPer * 100.0) / 100.0;
							final BigDecimal roundedOffValue = new BigDecimal((int) roundedOffValuebefore); //changed as per Ashish mail
							productDetailMobile.setDiscount(roundedOffValue.toString());

						}
						else if (buyBoxData.getPrice() != null && buyBoxData.getPrice().getValue().doubleValue() > 0)
						{
							final double savingPriceCal = buyBoxData.getMrp().getDoubleValue().doubleValue()
									- buyBoxData.getPrice().getDoubleValue().doubleValue();
							final double savingPriceCalPer = (savingPriceCal / buyBoxData.getMrp().getDoubleValue().doubleValue()) * 100;
							final double roundedOffValuebefore = Math.round(savingPriceCalPer * 100.0) / 100.0;
							final BigDecimal roundedOffValue = new BigDecimal((int) roundedOffValuebefore);
							//changed as per Ashish mail
							productDetailMobile.setDiscount(roundedOffValue.toString());
						}
					}
				}
				catch (final Exception e)
				{
					LOG.debug("*************** Exception at PDP web service buybox fetching ******************* " + e);
				}
			}
			if (null != buyBoxData && null != buyBoxData.getSellerAssociationstatus()
					&& buyBoxData.getSellerAssociationstatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
			{
				productDetailMobile.setSellerAssociationstatus(MarketplacecommerceservicesConstants.Y);
			}
			else
			{
				productDetailMobile.setSellerAssociationstatus(MarketplacecommerceservicesConstants.N);
			}
			//TPR-6117 STARTS
			final int maximum_configured_quantiy = siteConfigService.getInt(MAXIMUM_CONFIGURED_QUANTIY, 0);


			if (null != productModel.getMaxOrderQuantity() && productModel.getMaxOrderQuantity().intValue() > 0
					&& productModel.getMaxOrderQuantity().intValue() < maximum_configured_quantiy)
			{
				productDetailMobile.setMaxQuantityAllowed(productModel.getMaxOrderQuantity().toString());
			}
			else
			{
				//final int maximum_configured_quantiy = siteConfigService.getInt(MAXIMUM_CONFIGURED_QUANTIY, 0);
				productDetailMobile.setMaxQuantityAllowed(String.valueOf(maximum_configured_quantiy));
			}
			//TPR-6117 END

			//Added for jewellery
			if (null != buyBoxData)
			{
				if (productModel.getProductCategoryType().equalsIgnoreCase(MarketplacewebservicesConstants.FINEJEWELLERY))
				{
					final List<JewelleryInformationModel> jewelleryInfo = jewelleryService.getJewelleryInfoByUssid(buyBoxData
							.getSellerArticleSKU());
					if (CollectionUtils.isNotEmpty(jewelleryInfo) && StringUtils.isNotEmpty(jewelleryInfo.get(0).getPCMUSSID()))
					{
						ussidJwlry = jewelleryInfo.get(0).getPCMUSSID();
					}
				}
			}
			//			Added for OfferDetail of  a product TPR-1299
			if (null != productCode)
			{
				final Map<String, Map<String, String>> offerMessageMap = prodOfferDetFacade.showOfferMessage(productCode);
				if (MapUtils.isNotEmpty(offerMessageMap) && null != buyBoxData && null != buyBoxData.getSellerId()
						&& offerMessageMap.containsKey(buyBoxData.getSellerId()))
				{
					for (final Entry<String, Map<String, String>> entry : offerMessageMap.entrySet())
					{
						if (null != entry && null != entry.getKey() && null != entry.getValue()
								&& entry.getKey().equals(buyBoxData.getSellerId()))
						{
							final Map<String, String> offerMessage = entry.getValue();
							final ProductOfferMsgDTO ProductOfferMsgDTO = new ProductOfferMsgDTO();
							for (final Entry<String, String> entry1 : offerMessage.entrySet())
							{
								if (null != entry1 && null != entry1.getValue()
										&& entry1.getKey().equalsIgnoreCase(MarketplacecommerceservicesConstants.MESSAGEDET))
								{
									ProductOfferMsgDTO.setMessageDetails(entry1.getValue());
								}
								if (null != entry1 && null != entry1.getValue()
										&& entry1.getKey().equalsIgnoreCase(MarketplacecommerceservicesConstants.MESSAGE))
								{
									ProductOfferMsgDTO.setMessageID(entry1.getValue());
								}
								if (null != entry1 && null != entry1.getValue()
										&& entry1.getKey().equalsIgnoreCase(MarketplacecommerceservicesConstants.MESSAGESTARTDATE))
								{

									ProductOfferMsgDTO.setStartDate(entry1.getValue());
								}
								if (null != entry1 && null != entry1.getValue()
										&& entry1.getKey().equalsIgnoreCase(MarketplacecommerceservicesConstants.MESSAGEENDDATE))
								{
									ProductOfferMsgDTO.setEndDate(entry1.getValue());
								}
							}
							productDetailMobile.setProductOfferMsg(ProductOfferMsgDTO);
						}
					}

				}
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
			if (null != productData)
			{
				buyboxdataCheck = buyboxdata(productData, ussid, productModel);
				if (null != buyboxdataCheck && null != buyboxdataCheck.getIsCod())
				{
					isProductCOD = buyboxdataCheck.getIsCod();
				}
				if (null != isProductCOD)
				{
					productDetailMobile.setIsCOD(isProductCOD);
				}
				if (null != buyboxdataCheck)
				{
					productDetailMobile.setEligibleDeliveryModes(getEligibleDeliveryModes(buyboxdataCheck));
				}

				otherSellerDataList = getOtherSellerDetails(productData, ussid);
				if (CollectionUtils.isNotEmpty(otherSellerDataList))
				{
					framedOtherSellerDataList = frameOtherSellerDetails(otherSellerDataList, productModel);
				}
				if (null != productData.getListingId())
				{
					productDetailMobile.setProductListingId(productData.getListingId());
				}
				if (null != productData.getProductTitle())
				{
					productDetailMobile.setProductName(productData.getProductTitle());
				}
				if (null != productData.getArticleDescription())
				{
					productDetailMobile.setProductDescription(productData.getArticleDescription());
				}
				if (null != productData.getRootCategory())
				{
					productDetailMobile.setRootCategory(productData.getRootCategory());
				}
				if (CollectionUtils.isNotEmpty(productData.getImages()))
				{
					productDetailMobile.setGalleryImagesList(getGalleryImages(productData));
				}

				if (null != framedOtherSellerDataList && !framedOtherSellerDataList.isEmpty())
				{
					productDetailMobile.setOtherSellers(framedOtherSellerDataList);
				}
				if (null != buyBoxData && null != buyBoxData.getSellerName())
				{
					productDetailMobile.setWinningSellerName(buyBoxData.getSellerName());
				}
				//TISPRD-8944 Changes Start
				if (specialMobileFlag && null != buyBoxData && null != buyBoxData.getSpecialPriceMobile()
						&& null != buyBoxData.getSpecialPriceMobile().getFormattedValue()
						&& null != buyBoxData.getSpecialPriceMobile().getValue()
						&& buyBoxData.getSpecialPriceMobile().getValue().compareTo(BigDecimal.ZERO) > 0)
				{
					productDetailMobile.setWinningSellerSpecialPrice(buyBoxData.getSpecialPriceMobile().getFormattedValue());
				} //backward compatible
				else if (!specialMobileFlag && null != buyBoxData && null != buyBoxData.getSpecialPrice()
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

				if (specialMobileFlag && null != buyBoxData && null != buyBoxData.getSpecialPriceMobile()
						&& null != buyBoxData.getSpecialPriceMobile().getValue()
						&& buyBoxData.getSpecialPriceMobile().getValue().compareTo(BigDecimal.ZERO) > 0)
				{
					isEMIeligible = getEMIforProduct(buyBoxData.getSpecialPriceMobile().getValue());
				} //backward compatible
				else if (!specialMobileFlag && null != buyBoxData && null != buyBoxData.getSpecialPrice()
						&& null != buyBoxData.getSpecialPrice().getValue() && null != buyBoxData.getSpecialPrice().getValue()
						&& buyBoxData.getSpecialPrice().getValue().compareTo(BigDecimal.ZERO) > 0)
				{
					isEMIeligible = getEMIforProduct(buyBoxData.getSpecialPrice().getValue());
				}
				//TISPRD-8944 Changes End
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

				if (StringUtils.isNotEmpty(sellerMonogramMessage))
				{
					productDetailMobile.setCustomizationInfoText(sellerMonogramMessage);
				}

				if (StringUtils.isNotEmpty(buyingGuideURL))
				{
					productDetailMobile.setBuyingGuideUrl(buyingGuideURL);
				}

				if (null != buyboxdataCheck && null != buyboxdataCheck.getFullfillment())
				{
					productDetailMobile.setFulfillmentType(buyboxdataCheck.getFullfillment());
				}
				//	Promotion Dto changed
				potenitalPromo = getPromotionsForProduct(productData, buyBoxData, framedOtherSellerDataList, channel);
				if (null != potenitalPromo)
				{
					productDetailMobile.setPotentialPromotions(potenitalPromo);
				}
				//TISPT-396 Rating reviews are part of Gigya
				/*
				 * if (null != productData.getRatingCount()) {
				 * productDetailMobile.setTotalreviewComments(productData.getRatingCount().toString()); } else {
				 * productDetailMobile.setTotalreviewComments(productData.getNumberOfReviews().toString()); }
				 */
				if (CollectionUtils.isNotEmpty(productData.getClassifications()))
				{
					if (MarketplacewebservicesConstants.HOME_FURNISHING.equalsIgnoreCase(productModel.getProductCategoryType()))
					{
						final String[] overviewtabSectEntry = configurationService.getConfiguration()
								.getString("classification.attributes.HomeFurnishing.sectionSeq").split(",");

						final Map<String, List<String>> mapConfigurableAttributes = productDetailsHelper
								.displayConfigurableAttributeForHF(productData);
						final List<ClassificationDTOLister> classificationDTOListerList = new ArrayList<ClassificationDTOLister>();
						for (final Iterator<Map.Entry<String, List<String>>> it = mapConfigurableAttributes.entrySet().iterator(); it
								.hasNext();)
						{
							final Entry<String, List<String>> entry = it.next();

							//Product Details,Care Instructions,Set Information,Key Product Points

							if (entry.getKey().equals(overviewtabSectEntry[0]))
							{
								//For Product Details
								final List<String> productDetList = entry.getValue();
								final List<ClassificationDTO> classificationList = new ArrayList<ClassificationDTO>();
								final ClassificationDTOLister lister = new ClassificationDTOLister();
								final Classifications classifications = new Classifications();
								for (final String prodDetail : productDetList)
								{
									final ClassificationDTO classDTO = new ClassificationDTO();
									String prodAttrVal = null;
									final String[] prodAttr = prodDetail.split(":", 2);
									final String prodAttrName = prodAttr[0];
									if (prodAttr.length >= 2)
									{
										prodAttrVal = prodAttr[1];
									}
									classDTO.setKey(prodAttrName);
									classDTO.setValue(prodAttrVal);
									classificationList.add(classDTO);
									classifications.setClassificationList(classificationList);

								}
								lister.setKey(overviewtabSectEntry[0]);
								lister.setValue(classifications);
								classificationDTOListerList.add(lister);
							}
							else if (entry.getKey().equals(overviewtabSectEntry[2]))
							{
								//for Set Information
								final List<String> setInfoList = entry.getValue();
								final List<ClassificationDTO> classificationList = new ArrayList<ClassificationDTO>();
								final ClassificationDTOLister lister = new ClassificationDTOLister();
								final Classifications classifications = new Classifications();
								for (final String setInfo : setInfoList)
								{
									final ClassificationDTO classDTO = new ClassificationDTO();
									String setAttrVal = null;
									final String[] setAttr = setInfo.split(":", 2);
									final String setAttrName = setAttr[0];
									if (setAttr.length >= 2)
									{
										setAttrVal = setAttr[1];
									}
									classDTO.setKey(setAttrName);
									classDTO.setValue(setAttrVal);
									classificationList.add(classDTO);
									classifications.setClassificationList(classificationList);
								}
								lister.setKey(overviewtabSectEntry[2]);
								lister.setValue(classifications);
								classificationDTOListerList.add(lister);
							}
							else if (entry.getKey().equals(overviewtabSectEntry[1]))
							{
								//For Care Instructions
								final List<String> careInsList = entry.getValue();
								final ClassificationDTOLister lister = new ClassificationDTOLister();
								final Classifications classifications = new Classifications();

								classifications.setClassificationValues(careInsList);
								lister.setKey(overviewtabSectEntry[1]);
								lister.setValue(classifications);
								classificationDTOListerList.add(lister);
							}
							else if (entry.getKey().equals(overviewtabSectEntry[3]))
							{
								//For Key Product Points
								final List<String> keyProdPtsList = entry.getValue();
								final ClassificationDTOLister lister = new ClassificationDTOLister();
								final Classifications classifications = new Classifications();

								classifications.setClassificationValues(keyProdPtsList);
								lister.setKey(overviewtabSectEntry[3]);
								lister.setValue(classifications);
								classificationDTOListerList.add(lister);
							}
						}
						productDetailMobile.setClassificationList(classificationDTOListerList);
					}
					else
					{
						displayConfigurableAttribute(productData, productDetailMobile);
					}

					/* Specifications of a product */
					if (MarketplacecommerceservicesConstants.FINEJEWELLERY.equalsIgnoreCase(productModel.getProductCategoryType())
							|| MarketplacecommerceservicesConstants.FASHIONJEWELLERY.equalsIgnoreCase(productModel
									.getProductCategoryType()))
					{
						productDetailsHelper.groupGlassificationDataForJewelDetails(productData);
						if (MapUtils.isNotEmpty(productData.getFineJewelleryDeatils()))
						{
							LinkedHashMap<String, Map<String, List<String>>> featureDetails = new LinkedHashMap<String, Map<String, List<String>>>();
							featureDetails = (LinkedHashMap<String, Map<String, List<String>>>) productData.getFineJewelleryDeatils();
							for (final Entry<String, Map<String, List<String>>> entry : featureDetails.entrySet())
							{
								final FineJwlryClassificationListDTO jwlryClass = new FineJwlryClassificationListDTO();
								final Map<String, List<String>> innerEntry = entry.getValue();
								final Map<String, FineJwlryClassificationListValueDTO> classificationListJwlry = new LinkedHashMap<String, FineJwlryClassificationListValueDTO>();
								if (entry.getKey().equalsIgnoreCase("Product Details"))
								{
									final FineJwlryClassificationListValueDTO classUssid = new FineJwlryClassificationListValueDTO();
									classUssid.setClassificationListValueJwlry(Arrays
											.asList(buyBoxData.getSellerArticleSKU().substring(6)));
									classificationListJwlry.put("PRODUCT CODE", classUssid);
								}
								for (final Entry<String, List<String>> innerLoopEntry : innerEntry.entrySet())
								{
									final FineJwlryClassificationListValueDTO listValue = new FineJwlryClassificationListValueDTO();
									listValue.setClassificationListValueJwlry(innerLoopEntry.getValue());
									classificationListJwlry.put(innerLoopEntry.getKey(), listValue);
								}
								jwlryClass.setClassificationListJwlry(classificationListJwlry);
								fineJewelleryClassificationList.put(entry.getKey(), jwlryClass);
							}
						}
						productDetailMobile.setFineJewelleryClassificationList(fineJewelleryClassificationList);
					}
					else
					{
						specificationsList = getSpecificationsOfProductByGroup(productData);
					}
					if (CollectionUtils.isNotEmpty(specificationsList))
					{
						productDetailMobile.setClassifications(specificationsList);
					}
				}

				if (null != productData.getArticleDescription())
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

				if (null != buyBoxData)
				{
					knowMoreList = getknowMoreDetails(productModel, buyBoxData, ussidJwlry);
				}
				if (CollectionUtils.isNotEmpty(knowMoreList))
				{
					productDetailMobile.setKnowMore(knowMoreList);
				}

				if (MarketplacecommerceservicesConstants.FINEJEWELLERY.equalsIgnoreCase(productModel.getProductCategoryType())
						|| MarketplacecommerceservicesConstants.FASHIONJEWELLERY
								.equalsIgnoreCase(productModel.getProductCategoryType()))
				{
					if (null != buyBoxData)
					{
						refundReturnList = getRefundReturnDetails(productModel, buyBoxData, ussidJwlry);
					}
				}
				if (CollectionUtils.isNotEmpty(refundReturnList))
				{
					//
					//{
					productDetailMobile.setReturnAndRefund(refundReturnList);
					//}
					//					if (MarketplacecommerceservicesConstants.FASHIONJEWELLERY.equalsIgnoreCase(productModel.getProductCategoryType()))
					//					{
					//						productDetailMobile.setReturns(refundReturnList);
					//					}
				}

				if (null != productData.getBrand() && null != productData.getBrand().getBrandname())
				{
					productDetailMobile.setBrandName(productData.getBrand().getBrandname());
				}
				//TPR-6228/SDI-2805
				if (StringUtils.isNotEmpty(productData.getBrand().getBrandDescription()))
				{
					productDetailMobile
							.setBrandInfo(productData.getBrand().getBrandDescription().length() <= MplConstants.BRANDINFO_CHAR_LIMIT ? productData
									.getBrand().getBrandDescription() : StringUtils.substring(
									productData.getBrand().getBrandDescription(), 0, MplConstants.BRANDINFO_CHAR_LIMIT));
				}

				// changed for TPR-796
				//first set false to make all available
				productDetailMobile.setAllOOStock(Boolean.FALSE);
				//check if all products are Out of Stock
				if (buyBoxData != null && StringUtils.isNotEmpty(buyBoxData.getAllOOStock()))
				{
					//buyboxdata.getAllOOStock() is Y/N
					if (buyBoxData.getAllOOStock().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
					{
						productDetailMobile.setAllOOStock(Boolean.TRUE);
					}
				}
				//TISSTRT-1411

				variantDataList = getVariantDetailsForProduct(productData, stockAvailibilty);

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
					deliveryModesForProduct = getDeliveryModes(productModel, deliveryModesATPForProduct,
							buyBoxData.getSellerArticleSKU());
				}
				if (null != deliveryModesForProduct && !deliveryModesForProduct.isEmpty())
				{
					productDetailMobile.setDeliveryModesATP(deliveryModesForProduct);
				}
				if (CollectionUtils.isNotEmpty(productData.getCategories()))
				{
					productDetailMobile.setProductCategory(getCategoryOfProduct(productData));
				}
				if (CollectionUtils.isNotEmpty(productData.getCategories()))
				{
					productDetailMobile.setProductCategoryId(getCategoryCodeOfProduct(productData));
				}



				if (null != productData.getUrl()
						&& (!(productData.getUrl().toLowerCase().contains(HTTP) || productData.getUrl().toLowerCase().contains(HTTPS))))
				{
					//sharedText += MarketplacecommerceservicesConstants.SPACE + baseUrl + "" + productData.getUrl(); Do not add empty strings
					sharedText += MarketplacecommerceservicesConstants.SPACE + baseUrl + productData.getUrl();
				}
				else if (null != productData.getUrl())
				{
					sharedText += productData.getUrl();
				}

				if (productData.getLuxIndicator() != null
						&& productData.getLuxIndicator().equalsIgnoreCase(MarketplaceCoreConstants.LUXURY))
				{
					productDetailMobile.setLuxIndicator(MarketplaceCoreConstants.LUXURY);
					//TISPRD-9238
					productDetailMobile.setKnowMoreEmail(configurationService.getConfiguration().getString("luxury.care.mail"));
				}

				//Added for TPR-1083 Exchange
				if (StringUtils.isNotEmpty(productData.getLevel3CategoryCode())
						&& StringUtils.isNotEmpty(productData.getLevel3CategoryName()))
				{
					productDetailMobile.setL3code(productData.getLevel3CategoryCode());
					productDetailMobile.setL3name(productData.getLevel3CategoryName());

					final ContentSlotModel contentSlotModel = contentSlotService
							.getContentSlotForId(MarketplacecommerceservicesConstants.Exchange_Slot);
					final List<ExchangeLinkUrl> linkUrlList = new ArrayList<>();

					if (contentSlotModel != null)
					{

						final List<AbstractCMSComponentModel> componentLists = contentSlotModel.getCmsComponents();
						if (CollectionUtils.isNotEmpty(componentLists))
						{
							for (final AbstractCMSComponentModel model : componentLists)
							{
								final ExchangeLinkUrl linkUrl = new ExchangeLinkUrl();
								if (model instanceof SimpleCMSComponentModel)

								{
									final SimpleCMSComponentModel simpleComponent = (SimpleCMSComponentModel) model;

									if (simpleComponent instanceof CMSLinkComponentModel)
									{
										final CMSLinkComponentModel model2 = (CMSLinkComponentModel) simpleComponent;
										if (StringUtils.isNotEmpty(model2.getName()))
										{
											linkUrl.setName(model2.getName());
										}
										if (StringUtils.isNotEmpty(model2.getUrl()))
										{

											linkUrl.setUrl(model2.getUrl() + MarketplacecommerceservicesConstants.MOBILE_SOURCE2);
										}
										if (StringUtils.isNotEmpty(model2.getUid()))
										{
											linkUrl.setId(model2.getUid());
										}
									}
									linkUrlList.add(linkUrl);
								}
							}
							productDetailMobile.setLinkUrls(linkUrlList);


						}
					}
				}

				//Added for jewellery
				if ((MarketplacewebservicesConstants.FINEJEWELLERY).equalsIgnoreCase(productData.getRootCategory()))
				{
					//final LinkedHashMap<String, Map<String, List<String>>> featureDetails = new LinkedHashMap<String, Map<String, List<String>>>();
					productDetailMobile.setPriceDisclaimerTextPDP(MarketplacewebservicesConstants.PRICE_DISCLAIMER_JEWELLERY);
					if (StringUtils.isNotEmpty(buyBoxData.getSellerArticleSKU()))
					{
						final String priceBrkUpPDP = displayConfigurableAttributeForPriceBreakup(buyBoxData.getSellerArticleSKU());
						productDetailMobile.setShowPriceBrkUpPDP(priceBrkUpPDP);
						priceMap = priceBreakupFacade.getPricebreakup(buyBoxData.getSellerArticleSKU(), buyBoxData.getSellerId());
					}

					if (CollectionUtils.isNotEmpty(priceMap))
					{
						for (final PriceBreakupData priceBrkUp : priceMap)
						{
							final PriceBreakUpDto priceBreakUp = new PriceBreakUpDto();
							priceBreakUp.setName(priceBrkUp.getName());
							priceBreakUp.setPrice(priceBrkUp.getPrice());
							priceBreakUp.setWeightRateList(priceBrkUp.getWeightRateList());
							priceBreakUpList.add(priceBreakUp);
						}

						productDetailMobile.setPriceBreakUpDetailsMap(priceBreakUpList);
					}
				}
				if (((MarketplacewebservicesConstants.FINEJEWELLERY).equalsIgnoreCase(productData.getRootCategory()))
						|| ((MarketplacewebservicesConstants.FASHIONJEWELLERY).equalsIgnoreCase(productData.getRootCategory())))
				{
					final String jwlryCat = configurationService.getConfiguration().getString("mpl.jewellery.category");
					boolean showSizeOrLength = false;
					if (StringUtils.isNotEmpty(jwlryCat))
					{
						final List<String> catCodeList = Arrays.asList(jwlryCat.split(","));

						for (final CategoryData cat : productData.getCategories())
						{
							final String catCode = cat.getCode();
							if (catCodeList.contains(catCode))
							{
								showSizeOrLength = true;
								break;
							}
						}
					}
					if (showSizeOrLength)
					{
						productDetailMobile.setIsSizeOrLength("Length");
					}
					else
					{
						productDetailMobile.setIsSizeOrLength("Size");
					}
				}
			}
			sharedText += MarketplacecommerceservicesConstants.SPACE
					+ Localization.getLocalizedString(MarketplacewebservicesConstants.PDP_SHARED_POST);
			productDetailMobile.setSharedText(sharedText);
			LOG.debug("******************** PDP mobile web service  fetching done *****************");

			//TPR-978


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
	//	{
	//		final List<String> warrantyList = new ArrayList<String>();
	//		try
	//		{
	//			if (null != mapConfigurableAttribute && null != productData && null != productData.getRootCategory()
	//					&& productData.getRootCategory().equalsIgnoreCase(MarketplacewebservicesConstants.ELECTRONICS))
	//			{
	//				for (final Map.Entry<String, String> entry : mapConfigurableAttribute.entrySet())
	//				{
	//					if (null != entry.getKey() && entry.getKey().contains(WARRANTY) && null != entry.getValue())
	//					{
	//						warrantyList.add(entry.getValue());
	//					}
	//				}
	//			}
	//		}
	//		catch (final Exception e)
	//		{
	//			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
	//		}
	//		return warrantyList;
	//	}
	/**
	 * This Method deals with population of Buying Guide details on PDP
	 *
	 * @param productModel
	 */
	private String populateBuyingGuide(final ProductModel productModel)
	{
		String buyingGuideCode = MarketplacecommerceservicesConstants.EMPTY;
		try
		{
			final List<CategoryModel> superCategoryDetails = new ArrayList<>(productModel.getSupercategories());

			if (CollectionUtils.isNotEmpty(superCategoryDetails))
			{
				for (final CategoryModel category : superCategoryDetails)
				{
					buyingGuideCode = category.getBuyingGuide();

					if (StringUtils.isNotEmpty(buyingGuideCode))
					{
						LOG.error("Buying Guide redirect URL" + buyingGuideCode);
						break;
					}
				}
			}
		}
		catch (final Exception exception)
		{
			LOG.error("Error during population of Buying Guide Details >> for Product >>" + productModel.getCode() + "Error>>"
					+ exception);
		}

		return buyingGuideCode;
	}

	/**
	 * Gets Seller ID Data
	 *
	 * @param buydata
	 * @return String
	 */
	private String getSellerIDData(final Map<String, Object> buydata)
	{
		final BuyBoxData buyboxdata = (BuyBoxData) buydata.get("pdp_buy_box");

		if (null != buyboxdata && StringUtils.isNotEmpty(buyboxdata.getSellerId()))
		{
			return buyboxdata.getSellerId();
		}
		return null;
	}

	/**
	 * @param buyBoxData
	 * @param ussidJwlry
	 * @return
	 */
	private List<RefundReturnDTO> getRefundReturnDetails(final ProductModel productModel, final BuyBoxData buyBoxData,
			final String ussidJwlry)
	{
		// YTODO Auto-generated method stub
		final List<RefundReturnDTO> refundReturnList = new ArrayList<RefundReturnDTO>();
		List<JewellerySellerDetailsModel> jSellerMsgList = new ArrayList<JewellerySellerDetailsModel>();
		RefundReturnDTO refundReturn = null;

		String retRefFirst = null;
		String retRefSec = null;

		String returnWindow = "0";

		if (StringUtils.isNotEmpty(Localization.getLocalizedString(MarketplacewebservicesConstants.KNOW_MORE_SECOND)))
		{
			retRefFirst = Localization.getLocalizedString(MarketplacewebservicesConstants.KNOW_MORE_SECOND);

			if (StringUtils.isNotEmpty(Localization.getLocalizedString(MarketplacewebservicesConstants.KNOW_MORE_THIRD)))
			{
				retRefSec = Localization.getLocalizedString(MarketplacewebservicesConstants.KNOW_MORE_THIRD);
			}
		}

		returnWindow = getReturnWindow(productModel, buyBoxData.getSellerArticleSKU(), ussidJwlry);

		if (StringUtils.isNotEmpty(retRefFirst) && StringUtils.isNotEmpty(returnWindow) && StringUtils.isNotEmpty(retRefSec))
		{
			refundReturn = new RefundReturnDTO();
			refundReturn.setRefundReturnItem(retRefFirst + MarketplacecommerceservicesConstants.SPACE + returnWindow
					+ MarketplacecommerceservicesConstants.SPACE + retRefSec);
			refundReturnList.add(refundReturn);
		}

		if (MarketplacecommerceservicesConstants.FINEJEWELLERY.equalsIgnoreCase(productModel.getProductCategoryType()))
		{
			if (StringUtils.isNotEmpty(buyBoxData.getSellerId()))
			{
				jSellerMsgList = jewelleryService.getSellerMsgForRetRefTab(buyBoxData.getSellerId());
			}

			if (CollectionUtils.isNotEmpty(jSellerMsgList))
			{
				for (final JewellerySellerDetailsModel jSellerMsg : jSellerMsgList)
				{
					refundReturn = new RefundReturnDTO();
					if (StringUtils.isNotEmpty(jSellerMsg.getDescription()))
					{
						refundReturn.setRefundReturnItem(jSellerMsg.getDescription());
						refundReturnList.add(refundReturn);
					}
				}
			}
		}

		return refundReturnList;
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
	 * @param ussidJwlry
	 * @return List<String>
	 */
	private List<KnowMoreDTO> getknowMoreDetails(final ProductModel productModel, final BuyBoxData buyBoxData,
			final String ussidJwlry)
	{

		String knowMoreSec = null;
		String knowMoreTh = null;
		String returnWindow = "0";
		String knowMoreFourth = null;
		String knowMoreFifth = null;
		// Added for INC_11931
		String knowMoreSecLux = null;
		String knowMoreSecLuxLessThanZero = null; //changes for INC144314533
		//final String knowMoreThLux = null;
		final List<KnowMoreDTO> knowMoreList = new ArrayList<KnowMoreDTO>();
		KnowMoreDTO knowMoreItem = null;
		final String cliqCareNumber = configurationService.getConfiguration().getString("cliq.care.number");
		final String cliqCareMail = configurationService.getConfiguration().getString("cliq.care.mail");
		final String luxuryCareMail = configurationService.getConfiguration().getString("luxury.care.mail");
		String lingerieReturnMsg = null;
		boolean isProductLingerie = false;
		if (null != productModel && StringUtils.isNotEmpty(buyBoxData.getSellerArticleSKU()))
		{

			lingerieReturnMsg = getReturnWindowLingerie(productModel, buyBoxData.getSellerArticleSKU());
			if (null != lingerieReturnMsg)
			{
				isProductLingerie = true;
			}
			if (!isProductLingerie
					&& !MarketplacewebservicesConstants.FINEJEWELLERY.equalsIgnoreCase(productModel.getProductCategoryType())
					&& !MarketplacewebservicesConstants.FASHIONJEWELLERY.equalsIgnoreCase(productModel.getProductCategoryType()))
			{
				returnWindow = getReturnWindow(productModel, buyBoxData.getSellerArticleSKU(), ussidJwlry);
			}
		}

		if (!MarketplacewebservicesConstants.FINEJEWELLERY.equalsIgnoreCase(productModel.getProductCategoryType())
				&& !MarketplacewebservicesConstants.FASHIONJEWELLERY.equalsIgnoreCase(productModel.getProductCategoryType()))
		{
			if (StringUtils.isNotEmpty(Localization.getLocalizedString(MarketplacewebservicesConstants.KNOW_MORE_SECOND)))
			{
				knowMoreSec = Localization.getLocalizedString(MarketplacewebservicesConstants.KNOW_MORE_SECOND);

				if (StringUtils.isNotEmpty(Localization.getLocalizedString(MarketplacewebservicesConstants.KNOW_MORE_THIRD)))
				{
					knowMoreTh = Localization.getLocalizedString(MarketplacewebservicesConstants.KNOW_MORE_THIRD);
				}
			}
		}

		//Start Code change for INC_11931
		if (StringUtils.isNotEmpty(configurationService.getConfiguration().getString(
				MarketplacewebservicesConstants.KNOW_MORE_SECOND_FOR_LUX)))
		{
			knowMoreSecLux = configurationService.getConfiguration().getString(
					MarketplacewebservicesConstants.KNOW_MORE_SECOND_FOR_LUX);
		}
		//changes for INC144314533
		if (StringUtils.isNotEmpty(configurationService.getConfiguration().getString(
				MarketplacewebservicesConstants.KNOW_MORE_SECOND_LUX_LESS_THAN_ZERO)))
		{
			knowMoreSecLuxLessThanZero = configurationService.getConfiguration().getString(
					MarketplacewebservicesConstants.KNOW_MORE_SECOND_LUX_LESS_THAN_ZERO);
		}
		//End Code change for INC_11931
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

		if (isProductLingerie && StringUtils.isNotEmpty(lingerieReturnMsg))
		{
			knowMoreItem = new KnowMoreDTO();
			knowMoreItem.setKnowMoreItem(lingerieReturnMsg);
			knowMoreList.add(knowMoreItem);
		}

		else
		{

			//Start Code change for INC_11931

			if (StringUtils.isNotEmpty(configurationService.getConfiguration().getString(
					MarketplacewebservicesConstants.KNOW_MORE_SECOND_FOR_LUX)))
			{
				knowMoreSecLux = configurationService.getConfiguration().getString(
						MarketplacewebservicesConstants.KNOW_MORE_SECOND_FOR_LUX);
			}
			//changes for INC144314533
			if (StringUtils.isNotEmpty(configurationService.getConfiguration().getString(
					MarketplacewebservicesConstants.KNOW_MORE_SECOND_LUX_LESS_THAN_ZERO)))
			{
				knowMoreSecLuxLessThanZero = configurationService.getConfiguration().getString(
						MarketplacewebservicesConstants.KNOW_MORE_SECOND_LUX_LESS_THAN_ZERO);
			}

			//End Code change for INC_11931



			//changes for INC144314533

			if (productModel.getLuxIndicator() != null
					&& productModel.getLuxIndicator().getCode().equalsIgnoreCase(MarketplaceCoreConstants.LUXURY))
			{
				if (StringUtils.isNotEmpty(knowMoreSecLux) && StringUtils.isNotEmpty(returnWindow)
						&& Integer.parseInt(returnWindow) > 0)
				{
					knowMoreItem = new KnowMoreDTO();
					knowMoreItem.setKnowMoreItem(knowMoreSecLux);
					knowMoreList.add(knowMoreItem);
				}
				else if (StringUtils.isNotEmpty(knowMoreSecLuxLessThanZero)
						&& (StringUtils.isEmpty(returnWindow) || Integer.parseInt(returnWindow) <= 0))
				{
					knowMoreItem = new KnowMoreDTO();
					knowMoreItem.setKnowMoreItem(knowMoreSecLuxLessThanZero);
					knowMoreList.add(knowMoreItem);
				}
			}
			else if (StringUtils.isNotEmpty(knowMoreSec) && StringUtils.isNotEmpty(returnWindow)
					&& StringUtils.isNotEmpty(knowMoreTh))
			{
				knowMoreItem = new KnowMoreDTO();
				knowMoreItem.setKnowMoreItem(knowMoreSec + MarketplacecommerceservicesConstants.SPACE + returnWindow
						+ MarketplacecommerceservicesConstants.SPACE + knowMoreTh);
				knowMoreList.add(knowMoreItem);
			}
		}
		if (StringUtils.isNotEmpty(knowMoreFourth) && StringUtils.isNotEmpty(cliqCareNumber)
				&& StringUtils.isNotEmpty(knowMoreFifth) && StringUtils.isNotEmpty(cliqCareMail))
		{
			knowMoreItem = new KnowMoreDTO();
			//TISPRD-9238
			if (productModel.getLuxIndicator() != null
					&& productModel.getLuxIndicator().getCode().equalsIgnoreCase(MarketplaceCoreConstants.LUXURY))
			{
				knowMoreItem.setKnowMoreItem(knowMoreFourth + MarketplacecommerceservicesConstants.SPACE + cliqCareNumber
						+ MarketplacecommerceservicesConstants.SPACE + knowMoreFifth + MarketplacecommerceservicesConstants.SPACE
						+ luxuryCareMail);
			}
			else
			{
				knowMoreItem.setKnowMoreItem(knowMoreFourth + MarketplacecommerceservicesConstants.SPACE + cliqCareNumber
						+ MarketplacecommerceservicesConstants.SPACE + knowMoreFifth + MarketplacecommerceservicesConstants.SPACE
						+ cliqCareMail);
			}

			knowMoreList.add(knowMoreItem);
		}
		return knowMoreList;
	}

	/**
	 * return window calculation
	 *
	 * @param productModel
	 * @param winningUssid
	 * @param ussidJwlry
	 * @return String
	 */
	private String getReturnWindow(final ProductModel productModel, final String winningUssid, final String ussidJwlry)
	{

		String returnWindow = "0";
		if (null != productModel.getSellerInformationRelator() && !productModel.getSellerInformationRelator().isEmpty())
		{
			for (final SellerInformationModel sellerInfo : productModel.getSellerInformationRelator())
			{
				/*
				 * if (StringUtils.isNotEmpty(winningUssid) && StringUtils.isNotEmpty(sellerInfo.getSellerArticleSKU()) &&
				 * sellerInfo.getSellerArticleSKU().equalsIgnoreCase(winningUssid) && null != sellerInfo.getRichAttribute()
				 * && !sellerInfo.getRichAttribute().isEmpty())
				 */
				if (StringUtils.isNotEmpty(winningUssid)
						&& StringUtils.isNotEmpty(sellerInfo.getSellerArticleSKU())
						&& (sellerInfo.getSellerArticleSKU().equalsIgnoreCase(winningUssid) || sellerInfo.getSellerArticleSKU()
								.equalsIgnoreCase(ussidJwlry)) && null != sellerInfo.getRichAttribute()
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
	 * return window calculation for lingerie products
	 *
	 * @param productModel
	 * @param winningUssid
	 * @return String
	 */
	private String getReturnWindowLingerie(final ProductModel productModel, final String winningUssid)
	{
		//TISCR-414 - Chairmans demo feedback 10thMay CR starts
		String returnWindowMsg = null;
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>(productModel.getSupercategories());
		final String configLingerieCategoris1 = configurationService.getConfiguration().getString("pdp.lingerie1.code");
		final String configLingerieCategoris2 = configurationService.getConfiguration().getString("pdp.lingerie2.code");

		if (StringUtils.isNotEmpty(configLingerieCategoris1) && buyBoxFacade.isCatLingerie(categoryList, configLingerieCategoris1))
		{
			returnWindowMsg = Localization.getLocalizedString(MarketplacewebservicesConstants.KNOW_MORE_LINGERIE1);

		}
		else if (StringUtils.isNotEmpty(configLingerieCategoris2)
				&& buyBoxFacade.isCatLingerie(categoryList, configLingerieCategoris2))
		{
			returnWindowMsg = Localization.getLocalizedString(MarketplacewebservicesConstants.KNOW_MORE_LINGERIE2);
		}

		return returnWindowMsg;

		//TISCR-414 - Chairmans demo feedback 10thMay CR ends
	}

	/**
	 * getting delivery modes ATP
	 *
	 * @param productModel
	 * @param deliveryModesATPForProduct
	 * @param winningUssid
	 * @param ussidJwlry
	 * @return Map<String, String>
	 */
	private Map<String, String> getDeliveryModes(final ProductModel productModel,
			final Map<String, Map<String, Integer>> deliveryModesATPForProduct, String winningUssid)
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

		//for fine jewellery
		if (null != productModel && null != productModel.getProductCategoryType()
				&& StringUtils.equalsIgnoreCase(productModel.getProductCategoryType(), MarketplacewebservicesConstants.FINEJEWELLERY))
		{
			final String variantUssid = winningUssid;
			LOG.debug("variant ussid : " + variantUssid);
			final List<JewelleryInformationModel> jewelleryInfo = jewelleryService.getJewelleryInfoByUssid(winningUssid);
			winningUssid = jewelleryInfo.get(0).getPCMUSSID();
			LOG.debug("pcm ussid : " + winningUssid);
		}

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
	private SellerInformationData buyboxdata(final ProductData productData, String ussid, final ProductModel productModel)
	{

		final SellerInformationData buyBoxData = new SellerInformationData();
		try
		{
			//for fine jewellery
			if (null != productData && null != productData.getRootCategory()
					&& StringUtils.equalsIgnoreCase(productData.getRootCategory(), MarketplacewebservicesConstants.FINEJEWELLERY))
			{
				final String variantUssid = ussid;
				LOG.debug("variant ussid : " + variantUssid);
				final List<JewelleryInformationModel> jewelleryInfo = jewelleryService.getJewelleryInfoByUssid(ussid);
				ussid = jewelleryInfo.get(0).getPCMUSSID();
				LOG.debug("pcm ussid : " + ussid);

				for (final SellerInformationModel sellerInfo : productModel.getSellerInformationRelator())
				{
					if ((sellerInfo.getSellerAssociationStatus() == null || sellerInfo.getSellerAssociationStatus().equals(
							SellerAssociationStatusEnum.YES))
							&& (null != sellerInfo.getStartDate() && new Date().after(sellerInfo.getStartDate())
									&& null != sellerInfo.getEndDate() && new Date().before(sellerInfo.getEndDate())))
					{
						for (final RichAttributeModel richattr : sellerInfo.getRichAttribute())
						{
							buyBoxData.setDeliveryModes(productDetailsHelper.getDeliveryModeLlist(richattr, variantUssid));
						}
					}
				}
			}

			if (null != productData && null != productData.getSeller() && !productData.getSeller().isEmpty())
			{
				for (final SellerInformationData seller : productData.getSeller())
				{
					if (null != seller.getUssid() && null != ussid && seller.getUssid().equalsIgnoreCase(ussid)) //for buy box seller
					{
						// Commented for channel specific promotion TISPRD-8944
						if (null != seller.getSpPriceMobile())
						{
							buyBoxData.setSpPriceMobile(seller.getSpPriceMobile());
						}
						else if (null == seller.getSpPriceMobile() && null != seller.getSpPrice()) //backward compatible
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

						if (null != seller.getDeliveryModes() && !(seller.getDeliveryModes().isEmpty()))
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
	 * @param productData
	 * @param ussid
	 * @return List<SellerInformationData>
	 */
	private List<SellerInformationData> getOtherSellerDetails(final ProductData productData, final String ussid)
	{
		List<SellerInformationData> allSellerList = null;
		final List<SellerInformationData> otherSellerList = new ArrayList<SellerInformationData>();
		try
		{
			if (null != productData)
			{
				//TPR-3809
				//allSellerList = buyBoxFacade.getsellersDetails(productCode);
				allSellerList = buyBoxFacade.getsellersDetails(productData.getListingId(), productData.getRootCategory());
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
			ExceptionUtil.getCustomizedExceptionTrace(e);
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
				if (null != seller.getSpPriceMobile() && null != seller.getSpPriceMobile().getFormattedValue()
						&& null != seller.getSpPriceMobile().getValue()
						&& seller.getSpPriceMobile().getValue().compareTo(BigDecimal.ZERO) > 0)
				{
					sellerMobileData.setSellerSpecialPrice(seller.getSpPriceMobile().getFormattedValue().toString());
				}
				else if (null == seller.getSpPriceMobile() && null != seller.getSpPrice()
						&& null != seller.getSpPrice().getFormattedValue() && null != seller.getSpPrice().getValue()
						&& seller.getSpPrice().getValue().compareTo(BigDecimal.ZERO) > 0) //backward compatible
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
				if (null != seller.getSpPriceMobile() && null != seller.getSpPriceMobile().getValue()
						&& seller.getSpPriceMobile().getValue().compareTo(BigDecimal.ZERO) > 0)
				{
					isEMIeligible = getEMIforProduct(seller.getSpPriceMobile().getValue());
				}
				else if (null != seller.getMopPrice() && null != seller.getMopPrice().getValue()
						&& seller.getMopPrice().getValue().compareTo(BigDecimal.ZERO) > 0) //backward compatible
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
				//LOG.debug("****************** Other sellers added PDP mobile web service *********************");

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
	@Override
	public List<GalleryImageData> getGalleryImages(final ProductData productData)
	{
		List<GalleryImageData> galleryImageList = new ArrayList<GalleryImageData>();
		if (null != productData.getCode())
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
	private PromotionData getHighestPromotion(final ProductData productData, final String channel, final BuyBoxData buyBoxData)
	{
		PromotionData highestPromotion = null;
		try
		{
			Collection<PromotionData> promotioncollection = null;
			if (null != productData.getPotentialPromotions() && !productData.getPotentialPromotions().isEmpty())
			{
				promotioncollection = productData.getPotentialPromotions();
			}
			//INC144317611
			//final List<PromotionData> enabledPromotionList = new ArrayList<PromotionData>();
			//final Date todays_Date = new Date();
			if (CollectionUtils.isNotEmpty(promotioncollection))
			{
				for (final PromotionData promodata : promotioncollection)
				{

					final List<String> restrictedSellerList = promodata.getAllowedSellers();
					final List<String> restrictedChannel = promodata.getChannels();

					if (CollectionUtils.isEmpty(restrictedSellerList)
							|| (CollectionUtils.isNotEmpty(restrictedSellerList) && restrictedSellerList.contains(buyBoxData
									.getSellerId())))
					{
						if (channel == null
								|| CollectionUtils.isEmpty(restrictedChannel)
								|| (channel.equalsIgnoreCase(SalesApplication.WEB.getCode()) && restrictedChannel
										.contains(SalesApplication.WEB.getCode()))
								|| (channel.equalsIgnoreCase(SalesApplication.MOBILE.getCode()) && restrictedChannel
										.contains(SalesApplication.MOBILE.getCode())))
						{
							highestPromotion = promodata;
							break;
						}

					}

					//INC144317611

					/*
					 * if (null != promodata.getEnabled() && promodata.getEnabled().booleanValue() && null !=
					 * promodata.getStartDate() && null != promodata.getEndDate() &&
					 * todays_Date.after(promodata.getStartDate()) && todays_Date.before(promodata.getEndDate())) { if
					 * (CollectionUtils.isNotEmpty(promodata.getChannels())) { for (final String promoChannel :
					 * promodata.getChannels()) { //TISLUX-1823 -For LuxuryWeb if (channel != null &&
					 * channel.equalsIgnoreCase(SalesApplication.WEB.getCode())) { if
					 * (promoChannel.equalsIgnoreCase(SalesApplication.WEB.getCode())) { enabledPromotionList.add(promodata);
					 * } } else //For Mobile APP { if (promoChannel.equalsIgnoreCase(SalesApplication.MOBILE.getCode())) {
					 * enabledPromotionList.add(promodata); } } } } else { enabledPromotionList.add(promodata); } }
					 */

				}
				//INC144317611
				/*
				 * if (CollectionUtils.isNotEmpty(enabledPromotionList)) { highestPromotion =
				 * checkHighestPriority(enabledPromotionList); }
				 */
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
	/* sonar fix */
	/*
	 * private PromotionData checkHighestPriority(final List<PromotionData> enabledPromotionList) {
	 * Collections.sort(enabledPromotionList, new Comparator<PromotionData>() {
	 *
	 * @Override public int compare(final PromotionData promo1, final PromotionData promo2) { int priority = 0; if (null
	 * != promo1.getPriority() && null != promo2.getPriority()) { priority =
	 * promo1.getPriority().compareTo(promo2.getPriority()); } return priority; }
	 *
	 *
	 * }); Collections.reverse(enabledPromotionList); return enabledPromotionList.get(0); }
	 */

	/**
	 * All Potential promotions for a product
	 *
	 * @param productData
	 * @param otherSellers
	 * @param buyBoxData
	 * @return List<PromotionMobileData>
	 */
	private PromotionMobileData getPromotionsForProduct(final ProductData productData, final BuyBoxData buyBoxData,
			final List<SellerInformationMobileData> otherSellers, final String channel)
	{
		final PromotionData highestPrmotion = getHighestPromotion(productData, channel, buyBoxData);
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
			if (null != highestPrmotion && null != highestPrmotion.getBundlepromolinktext())
			{
				promoMobiledata.setBundlepromolinkname(highestPrmotion.getBundlepromolinktext());
			}
			if (null != highestPrmotion && null != highestPrmotion.getPromourl())
			{
				promoMobiledata.setBundlepromolink(highestPrmotion.getPromourl());
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
								//SDI-2041
								final ProductFeatureModel productFeature = mplProductFacade.getProductFeatureModelByProductAndQualifier(
										productData, featureData.getCode());
								if (null != featureData.getName() && null != featureValueData.getValue())
								{
									String fValue = featureValueData.getValue();
									if (null != productFeature && null != productFeature.getUnit()
											&& StringUtils.isNotEmpty(productFeature.getUnit().getSymbol()))
									{
										fValue = fValue.concat(" ").concat(productFeature.getUnit().getSymbol());
									}
									mapConfigurableAttribute.put(featureData.getName(), fValue);
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

	//	{
	//		final Map<String, String> mapConfigurableAttribute = new HashMap<String, String>();
	//		try
	//		{
	//			/* Checking the presence of classification attributes */
	//			if (null != productData.getClassifications())
	//			{
	//				final List<ClassificationData> ConfigurableAttributeList = new ArrayList<ClassificationData>(
	//						productData.getClassifications());
	//				for (final ClassificationData configurableAttributData : ConfigurableAttributeList)
	//				{
	//					final List<FeatureData> featureDataList = new ArrayList<FeatureData>(configurableAttributData.getFeatures());
	//					for (final FeatureData featureData : featureDataList)
	//					{
	//
	//						final List<FeatureValueData> featureValueList = new ArrayList<FeatureValueData>(featureData.getFeatureValues());
	//						if (null != productData.getRootCategory())
	//						{
	//							final String properitsValue = configurationService.getConfiguration().getString(
	//									MarketplacewebservicesConstants.CONFIGURABLE_ATTRIBUTE + productData.getRootCategory());
	//							//apparel
	//							final FeatureValueData featureValueData = featureValueList.get(0);
	//							if (productData.getRootCategory().equalsIgnoreCase(MarketplacewebservicesConstants.CLOTHING))
	//							{
	//
	//								if (null != properitsValue && featureValueData.getValue() != null
	//										&& properitsValue.contains(featureData.getName()))
	//								{
	//									mapConfigurableAttribute.put(featureData.getName(), featureValueData.getValue());
	//								}
	//
	//							} //end apparel
	//							  //electronics
	//							else
	//							{
	//								if (null != featureData.getName() && null != featureValueData.getValue())
	//
	//								{
	//									mapConfigurableAttribute.put(featureData.getName(), featureValueData.getValue());
	//								}
	//
	//							}
	//						}
	//					}
	//				}
	//			}
	//
	//		}
	//		catch (final EtailNonBusinessExceptions e)
	//		{
	//			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
	//		}
	//		catch (final Exception e)
	//		{
	//			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
	//		}
	//		return mapConfigurableAttribute;
	//	}

	/**
	 * sales category of product
	 *
	 * @param productData
	 * @return String
	 */
	private String getCategoryOfProduct(final ProductData productData)
	{
		String productCategory = null;
		final String salesCategory = configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.SALESCATEGORYTYPE);
		final String luxSalesCategory = configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.LUX_SALESCATEGORYTYPE);
		try
		{
			if (CollectionUtils.isNotEmpty(productData.getCategories()))
			{
				for (final CategoryData category : productData.getCategories())
				{
					if (null != category && null != category.getName() && null != category.getCode()
							&& (category.getCode().startsWith(salesCategory) || category.getCode().startsWith(luxSalesCategory)))
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
		final String salesCategory = configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.SALESCATEGORYTYPE);

		final String luxSalesCategory = configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.LUX_SALESCATEGORYTYPE);
		try
		{
			if (CollectionUtils.isNotEmpty(productData.getCategories()))
			{
				for (final CategoryData category : productData.getCategories())
				{
					if (null != category && null != category.getCode() && null != category.getCode()
							&& (category.getCode().startsWith(salesCategory) || category.getCode().startsWith(luxSalesCategory)))
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
	private List<VariantOptionMobileData> getVariantDetailsForProduct(final ProductData productData,
			final Map<String, Integer> stockAvailibilty)
	{
		final List<VariantOptionMobileData> variantDataList = new ArrayList<VariantOptionMobileData>();
		SizeLinkData sizeLinkData = null;
		CapacityLinkData capacityLinkData = null;
		ColorLinkData colorLinkData = null;
		String variantSizePCode = "";

		try
		{

			if (CollectionUtils.isNotEmpty(productData.getVariantOptions()))
			{
				for (final VariantOptionData variantData : productData.getVariantOptions())
				{
					final VariantOptionMobileData variantMobileData = new VariantOptionMobileData();
					colorLinkData = new ColorLinkData();
					sizeLinkData = new SizeLinkData();
					capacityLinkData = new CapacityLinkData();
					// For Color
					if (StringUtils.isNotEmpty(variantData.getColour()))
					{//INC144317643
						colorLinkData.setColor(variantData.getColour());
						final String hexCode = (variantData.getColour()).replaceAll("\\s", "");
						colorLinkData.setColorHexCode(configurationService.getConfiguration().getString(
								"colorhexcode." + hexCode.toLowerCase(), ""));//INC144317643
					}
					//checking for colour hex code
					/* start comment of INC144317643 */
					/*
					 * if (StringUtils.isNotEmpty(variantData.getColourCode())) {
					 * colorLinkData.setColorHexCode(variantData.getColourCode()); }
					 */
					/* end comment of INC144317643 */
					if (StringUtils.isNotEmpty(variantData.getUrl()))
					{
						colorLinkData.setColorurl(variantData.getUrl());
					}
					variantMobileData.setColorlink(colorLinkData);
					//For Size
					if (MapUtils.isNotEmpty(variantData.getSizeLink()))
					{
						for (final Map.Entry<String, String> sizeEntry : variantData.getSizeLink().entrySet())
						{
							variantSizePCode = "";
							//INC144318807
							if (StringUtils.isNotEmpty(sizeEntry.getValue()))
							{
								sizeLinkData.setSize(sizeEntry.getValue());
								sizeLinkData.setIsAvailable(true);
							}
							else
							{
								sizeLinkData.setIsAvailable(false);
							} //INC144318807
							if (StringUtils.isNotEmpty(sizeEntry.getKey()))
							{
								sizeLinkData.setUrl(sizeEntry.getKey());
								//setting Variant codes TISSTRT-1411
								final String[] url = sizeEntry.getKey().split("-");
								variantSizePCode = url[url.length - 1];
								variantSizePCode = variantSizePCode.toUpperCase();
								LOG.debug("variant_Size" + variantSizePCode);
							}
							//TISSTRT-1411
							//by default its available
							//INC144318807	/*sizeLinkData.setIsAvailable(true);*/ //INC144318807
							//check teh stock availability
							if (MapUtils.isNotEmpty(stockAvailibilty) && stockAvailibilty.containsKey(variantSizePCode))
							{
								if (stockAvailibilty.get(variantSizePCode).intValue() == 0)
								{
									sizeLinkData.setIsAvailable(false);
								}
							}

						}
					}

					if (StringUtils.isNotEmpty(sizeLinkData.getUrl()))
					{
						variantMobileData.setSizelink(sizeLinkData);
					}

					//For Electronics Capacity
					if (StringUtils.isNotEmpty(variantData.getCapacity()))
					{
						capacityLinkData.setCapacity(variantData.getCapacity());
					}
					if (StringUtils.isNotEmpty(variantData.getUrl()))
					{
						capacityLinkData.setUrl(variantData.getUrl());
					}
					if (StringUtils.isNotEmpty(capacityLinkData.getCapacity()))
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
	 * check if Keyword is configured
	 *
	 * @param searchText
	 * @return keywordRedirect
	 */
	@Override
	public String getKeywordSearch(final String searchText)
	{
		//suggestion to remove new Arraylist
		SolrFacetSearchKeywordRedirectModel result = null;
		String url = null;
		try
		{
			//searching the keyword in solr search config
			if (StringUtils.isNotBlank(searchText))
			{
				result = findKeywordRedirect(searchText);
			}
			//FOR Direct URL redirection only
			if (null != result && null != result.getRedirectMobile())
			{
				url = ((SolrURIRedirectModel) result.getRedirectMobile()).getUrl();
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
		return url;
	}

	private SolrFacetSearchKeywordRedirectModel findKeywordRedirect(final String searchQuery)
	{
		SolrFacetSearchKeywordRedirectModel keywordRedirect = null;
		final String langIso = commonI18NService.getCurrentLanguage().getIsocode();
		List<SolrFacetSearchKeywordRedirectModel> result = mplKeywordRedirectDao.findKeywords(searchQuery,
				KeywordRedirectMatchType.EXACT,
				configurationService.getConfiguration().getString(MarketplacewebservicesConstants.SEARCH_FACET_CONFIG), langIso);
		if (CollectionUtils.isNotEmpty(result))
		{
			result = keywordRedirectSorter.sort(result);
			keywordRedirect = result.get(0);
		}
		return keywordRedirect;
	}


	/**
	 * Displaying classification attributes in the Details tab of the PDP page
	 *
	 * @param productData
	 * @param productDetailMobile
	 */
	private void displayConfigurableAttribute(final ProductData productData, final ProductDetailMobileWsData productDetailMobile)
	{
		final Map<String, String> mapConfigurableAttribute = new HashMap<String, String>();
		final List<String> warrentyList = new ArrayList<String>();
		final List<String> certificationVal = new ArrayList<String>();
		try
		{
			/* Checking the presence of classification attributes */
			if (CollectionUtils.isNotEmpty(productData.getClassifications()))
			{
				final List<ClassificationData> ConfigurableAttributeList = new ArrayList<ClassificationData>(
						productData.getClassifications());
				for (final ClassificationData configurableAttributData : ConfigurableAttributeList)
				{
					final List<FeatureData> featureDataList = new ArrayList<FeatureData>(configurableAttributData.getFeatures());

					for (final FeatureData featureData : featureDataList)
					{
						final Map<String, String> productFeatureMap = new HashMap<String, String>();
						final List<FeatureValueData> featureValueList = new ArrayList<FeatureValueData>(featureData.getFeatureValues());
						final ProductFeatureModel productFeature = mplProductFacade.getProductFeatureModelByProductAndQualifier(
								productData, featureData.getCode());
						if (null != productData.getRootCategory())
						{
							final String properitsValue = configurationService.getConfiguration().getString(
									MarketplacewebservicesConstants.CONFIGURABLE_ATTRIBUTE + productData.getRootCategory());
							final String descValues = configurationService.getConfiguration().getString(
									MarketplacewebservicesConstants.PDP_DESC_TAB + productData.getRootCategory());
							//for jwl certification
							final String certificationValue = configurationService.getConfiguration().getString(
									MarketplacewebservicesConstants.CONFIGURABLE_ATTRIBUTE + productData.getRootCategory()
											+ ".certification");
							//apparel
							final FeatureValueData featureValueData = featureValueList.get(0);
							if ((MarketplacewebservicesConstants.CLOTHING.equalsIgnoreCase(productData.getRootCategory()))
									|| (MarketplacewebservicesConstants.FOOTWEAR.equalsIgnoreCase(productData.getRootCategory()) || MarketplacewebservicesConstants.FASHIONJEWELLERY
											.equalsIgnoreCase(productData.getRootCategory())))
							{

								//								mapConfigurableAttribute.put(featureValueData.getValue(),
								//											productFeature != null && productFeature.getUnit() != null
								//													&& !productFeature.getUnit().getSymbol().isEmpty() ? productFeature.getUnit().getSymbol()
								//													: "");
								if (productFeatureMap.size() > 0)
								{
									productFeatureMap.clear();
								}
								if (null != properitsValue && featureValueData.getValue() != null
										&& properitsValue.toLowerCase().contains(featureData.getName().toLowerCase()))
								{
									String featureValues = featureValueData.getValue();
									featureValues += productFeature != null && productFeature.getUnit() != null
											&& !productFeature.getUnit().getSymbol().isEmpty() ? productFeature.getUnit().getSymbol() : "";
									mapConfigurableAttribute.put(featureData.getName(), featureValues);
								}
							} //end apparel
							  //electronics

							else if (MarketplacewebservicesConstants.FASHION_ACCESSORIES.equalsIgnoreCase(productData.getRootCategory())
									|| MarketplacewebservicesConstants.WATCHES.equalsIgnoreCase(productData.getRootCategory())
									|| MarketplacewebservicesConstants.TRAVELANDLUGGAGE.equalsIgnoreCase(productData.getRootCategory())
									|| MarketplacewebservicesConstants.FINEJEWELLERY.equalsIgnoreCase(productData.getRootCategory()))
							{
								final String[] propertiesValues = properitsValue.split(",");
								//for jwl certification
								final String[] certificationValues = certificationValue.split(",");
								String featureValues = "";
								if (propertiesValues != null && propertiesValues.length > 0)
								{
									for (final String value : propertiesValues)
									{
										if (value.equalsIgnoreCase(featureData.getName()))
										{
											if (featureValues.length() > 0)
											{
												featureValues = "";
											}
											featureValues = featureValueData.getValue();
											featureValues += productFeature != null && productFeature.getUnit() != null
													&& !productFeature.getUnit().getSymbol().isEmpty() ? productFeature.getUnit().getSymbol()
													: "";
											mapConfigurableAttribute.put(featureData.getName(), featureValues);
										}
									}
								}
								if (descValues != null && StringUtils.isNotBlank(descValues))
								{
									final String[] descValue = descValues.split(",");
									if (descValue != null && descValue.length > 0)
									{
										for (final String value : descValue)
										{
											if (value.equalsIgnoreCase(featureData.getName()))
											{
												mapConfigurableAttribute.put(featureData.getName(), featureValueData.getValue());
											}
										}
									}

								}

								if (featureData.getName().toLowerCase().contains(MarketplacewebservicesConstants.WARRANTY.toLowerCase()))
								{
									warrentyList.add(featureValueData.getValue());
								}
								if (featureData.getName().equalsIgnoreCase("certification"))
								{
									final List<FeatureValueData> featureValueDataList = new ArrayList<FeatureValueData>(
											featureData.getFeatureValues());

									if (CollectionUtils.isNotEmpty(featureValueDataList) && certificationValues != null
											&& certificationValues.length > 0)
									{
										for (final FeatureValueData featurevalueData : featureValueDataList)
										{
											for (final String certification : certificationValues)
											{
												if (certification.equalsIgnoreCase(featurevalueData.getValue()))
												{
													certificationVal.add(featurevalueData.getValue());
													break;
												}
											}
										}
									}
								}
							}
							else
							{
								if (properitsValue.toLowerCase().contains(configurableAttributData.getCode().toLowerCase()))

								{

									mapConfigurableAttribute.put(featureData.getName(), featureValueData.getValue());
								}

								if (featureData.getName().toLowerCase().contains(MarketplacewebservicesConstants.WARRANTY.toLowerCase()))
								{
									warrentyList.add(featureValueData.getValue());
								}
							}

						}
					}
				}
			}
			//model.addAttribute(MarketplacewebservicesConstants.MAP_CONFIGURABLE_ATTRIBUTE, mapConfigurableAttribute);
			if (MarketplacewebservicesConstants.CLOTHING.equalsIgnoreCase(productData.getRootCategory())
					|| MarketplacewebservicesConstants.FOOTWEAR.equalsIgnoreCase(productData.getRootCategory())
					|| MarketplacewebservicesConstants.TRAVELANDLUGGAGE.equalsIgnoreCase(productData.getRootCategory())
					|| MarketplacewebservicesConstants.FINEJEWELLERY.equalsIgnoreCase(productData.getRootCategory())
					|| MarketplacewebservicesConstants.FASHIONJEWELLERY.equalsIgnoreCase(productData.getRootCategory()))
			{
				productDetailMobile.setDetails(mapConfigurableAttribute);
				productDetailMobile.setCertificationMapFrJwlry(certificationVal);
			}
			else if (MarketplacewebservicesConstants.FASHION_ACCESSORIES.equalsIgnoreCase(productData.getRootCategory())
					|| MarketplacewebservicesConstants.WATCHES.equalsIgnoreCase(productData.getRootCategory()))
			{
				final Map<String, String> treeMapConfigurableAttribute = new TreeMap<String, String>(mapConfigurableAttribute);
				productDetailMobile.setDetails(treeMapConfigurableAttribute);
			}
			else
			{
				final Map<String, String> treeMapConfigurableAttribute = new TreeMap<String, String>(mapConfigurableAttribute);
				productDetailMobile.setDetails(treeMapConfigurableAttribute);
			}

			productDetailMobile.setWarranty(warrentyList);
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
		}

	}

	@Override
	public ProductDetailMobileWsData getProductInfoForProductCode(final String productCode, final String baseUrl,
			final String channel)
	{
		final ProductDetailMobileWsData productDetailMobile = new ProductDetailMobileWsData();
		//final MplProductWebServiceImpl mplProductWebServiceImpl = new MplProductWebServiceImpl();
		BuyBoxData buyBoxData = null;
		ProductData productData = null;
		ProductModel productModel = null;
		final StringBuilder allVariants = new StringBuilder();
		String variantCodes = null;
		String variantsString = "";
		final Map<String, Integer> stockAvailibilty = new TreeMap<String, Integer>();
		try
		{
			String sharedText = Localization.getLocalizedString(MarketplacewebservicesConstants.PDP_SHARED_PRE);
			final boolean specialMobileFlag = configurationService.getConfiguration().getBoolean(
					MarketplacewebservicesConstants.SPECIAL_MOBILE_FLAG, false);
			productModel = productService.getProductForCode(productCode);
			if (null != productModel)
			{
				productData = productFacade.getProductForOptions(productModel,
						Arrays.asList(ProductOption.BASIC, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.GALLERY));
				//ProductOption.CATEGORIES,ProductOption.PROMOTIONS, ProductOption.CLASSIFICATION,
				//ProductOption.VARIANT_FULL, ProductOption.SELLER));
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9037);
			}
			if (null != productData && StringUtils.isNotEmpty(productData.getCode()))
			{
				try
				{
					//get left over variants
					if (productData.getAllVariantsId() != null && productData.getAllVariantsId().size() > 1)
					{
						productData.getAllVariantsId().remove(productData.getCode());
						for (final String variants : productData.getAllVariantsId())
						{
							allVariants.append(variants).append(',');
						}
						final int length = allVariants.length();
						variantCodes = allVariants.substring(0, length - 1);
					}
					if (StringUtils.isNotEmpty(variantCodes))
					{
						variantsString = productData.getCode() + "," + variantCodes;
					}
					//CKD:TPR-250:Start
					//final Map<String, Object> buydata = buyBoxFacade.buyboxPricePDP(variantsString);
					Map<String, Object> buydata = null;
					if (StringUtils.isNotEmpty(variantsString))
					{
						buydata = buyBoxFacade.buyboxPricePDP(variantsString, null, null);
					}
					else
					{
						buydata = buyBoxFacade.buyboxPricePDP(productData.getCode(), null, null);
					}

					//CKD:TPR-250:End
					if (MapUtils.isNotEmpty(buydata))
					{
						final List<String> noStockPCodes = (List<String>) buydata.get("no_stock_p_codes");
						for (final String pCode : noStockPCodes)
						{
							stockAvailibilty.put(pCode, Integer.valueOf(0));
						}
						buyBoxData = (BuyBoxData) buydata.get("pdp_buy_box");
					}
				}
				catch (final Exception e)
				{
					LOG.error("*************** Exception at PDP web service buybox fetching ******************* " + e);
				}
			}

			//TPR-1727 Removal of Out of stock
			if (null != buyBoxData && null != buyBoxData.getAvailable() && buyBoxData.getAvailable().intValue() <= 0)
			{
				return null;
			}
			if (null != productData)
			{
				if (null != productData.getListingId())
				{
					productDetailMobile.setProductListingId(productData.getListingId());
				}
				if (null != productData.getProductTitle())
				{
					productDetailMobile.setProductName(productData.getProductTitle());
				}
				if (specialMobileFlag && buyBoxData.getSpecialPriceMobile() != null
						&& buyBoxData.getSpecialPriceMobile().getValue().doubleValue() > 0)
				{
					productDetailMobile.setWinningSellerSpecialPrice(buyBoxData.getSpecialPriceMobile().getFormattedValue());
				}
				else if (!specialMobileFlag && buyBoxData.getSpecialPrice() != null
						&& buyBoxData.getSpecialPrice().getValue().doubleValue() > 0) //backward compatible
				{
					productDetailMobile.setWinningSellerSpecialPrice(buyBoxData.getSpecialPrice().getFormattedValue());
				}
				if (null != buyBoxData && null != buyBoxData.getPrice() && null != buyBoxData.getPrice().getValue().toString()
						&& null != buyBoxData.getPrice().getValue() && buyBoxData.getPrice().getValue().compareTo(BigDecimal.ZERO) > 0)
				{
					productDetailMobile.setWinningSellerMOP(buyBoxData.getPrice().getFormattedValue());
				}

				if (null != buyBoxData && null != buyBoxData.getMrp() && null != buyBoxData.getMrp().getValue().toString())
				{
					productDetailMobile.setMrp(buyBoxData.getMrp().getFormattedValue());
				}

				if (null != buyBoxData && null != buyBoxData.getSellerArticleSKU())
				{
					productDetailMobile.setWinningUssID(buyBoxData.getSellerArticleSKU());
					LOG.debug("*************** Mobile web service buyBox USSID ****************" + buyBoxData.getSellerArticleSKU());
				}

				if (null != productData.getUrl()
						&& (!(productData.getUrl().toLowerCase().contains(HTTP) || productData.getUrl().toLowerCase().contains(HTTPS))))
				{
					sharedText += MarketplacecommerceservicesConstants.SPACE + baseUrl + productData.getUrl();
				}
				else if (null != productData.getUrl())
				{
					sharedText += productData.getUrl();
				}
			}
			sharedText += MarketplacecommerceservicesConstants.SPACE
					+ Localization.getLocalizedString(MarketplacewebservicesConstants.PDP_SHARED_POST);
			productDetailMobile.setSharedText(sharedText);
			LOG.debug("******************** PDP mobile web service  fetching done *****************");
			//TPR-1727 PRODUCT IMAGES
			if (CollectionUtils.isNotEmpty(productData.getImages()))
			{
				//Set product image(thumbnail) url
				for (final ImageData img : productData.getImages())
				{
					if (null != img && null != img.getUrl() && StringUtils.isNotEmpty(img.getFormat())
							//&& img.getFormat().toLowerCase().equals(MarketplacecommerceservicesConstants.THUMBNAIL) Sonar fix
							&& img.getFormat().equalsIgnoreCase(MarketplacecommerceservicesConstants.PRODUCT_IMAGE)
							&& img.getMediaPriority().intValue() == 1)//INC144317283 Fix
					{
						productDetailMobile.setImageUrl(img.getUrl());
						break;
					}

				}
			}
			else
			{
				LOG.debug("*************** productInfo images not found ********************");
			}

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


	private String displayConfigurableAttributeForPriceBreakup(final String ussid)
	{
		// YTODO Auto-generated method stub
		String displayConfigurableAttributeForPriceBreakup = "";
		String productCode = buyBoxFacade.getpriceForUssid(ussid).getProduct();
		if (null != productCode)
		{
			productCode = productCode.toUpperCase();
		}

		final ProductModel productModel = productService.getProductForCode(productCode);
		final ProductData productData = productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC,
				ProductOption.SELLER, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.CATEGORIES,
				//ProductOption.GALLERY, ProductOption.PROMOTIONS, ProductOption.VARIANT_FULL, ProductOption.CLASSIFICATION));
				ProductOption.GALLERY, ProductOption.CLASSIFICATION, ProductOption.VARIANT_FULL));

		if (null != productData.getClassifications())
		{
			final List<ClassificationData> ConfigurableAttributeList = new ArrayList<ClassificationData>(
					productData.getClassifications());
			for (final ClassificationData configurableAttributData : ConfigurableAttributeList)
			{
				if (configurableAttributData.getCode().equals("19na"))
				{
					final List<FeatureData> featureDataList = new ArrayList<FeatureData>(configurableAttributData.getFeatures());

					for (final FeatureData featureData : featureDataList)
					{
						if (featureData.getCode().equals("pcmClassification/1/19na.pricebreakuponpdpfinejwlry"))
						{
							final List<FeatureValueData> featureValueList = new ArrayList<FeatureValueData>(
									featureData.getFeatureValues());

							for (final FeatureValueData featureValueData : featureValueList)
							{
								displayConfigurableAttributeForPriceBreakup = featureValueData.getValue();
							}
							LOG.debug("display price breakup on pdp :" + displayConfigurableAttributeForPriceBreakup);
						}

					}
				}

			}
		}
		return displayConfigurableAttributeForPriceBreakup;
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

	/**
	 * @return the productService
	 */
	public ProductService getProductService()
	{
		return productService;
	}

	/**
	 * @param productService
	 *           the productService to set
	 */
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	/**
	 * @return the productFacade
	 */
	public ProductFacade getProductFacade()
	{
		return productFacade;
	}

	/**
	 * @param productFacade
	 *           the productFacade to set
	 */
	public void setProductFacade(final ProductFacade productFacade)
	{
		this.productFacade = productFacade;
	}

	/**
	 * @return the defaultPromotionManager
	 */
	public DefaultPromotionManager getDefaultPromotionManager()
	{
		return defaultPromotionManager;
	}

	/**
	 * @param defaultPromotionManager
	 *           the defaultPromotionManager to set
	 */
	public void setDefaultPromotionManager(final DefaultPromotionManager defaultPromotionManager)
	{
		this.defaultPromotionManager = defaultPromotionManager;
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * @return the solrFacetSearchKeywordDao
	 */
	public SolrFacetSearchKeywordDao getSolrFacetSearchKeywordDao()
	{
		return solrFacetSearchKeywordDao;
	}

	/**
	 * @param solrFacetSearchKeywordDao
	 *           the solrFacetSearchKeywordDao to set
	 */
	public void setSolrFacetSearchKeywordDao(final SolrFacetSearchKeywordDao solrFacetSearchKeywordDao)
	{
		this.solrFacetSearchKeywordDao = solrFacetSearchKeywordDao;
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
	 * @return the keywordRedirectSorter
	 */
	public KeywordRedirectSorter getKeywordRedirectSorter()
	{
		return keywordRedirectSorter;
	}

	/**
	 * @param keywordRedirectSorter
	 *           the keywordRedirectSorter to set
	 */
	public void setKeywordRedirectSorter(final KeywordRedirectSorter keywordRedirectSorter)
	{
		this.keywordRedirectSorter = keywordRedirectSorter;
	}

	/**
	 * @return the productDetailsHelper
	 */
	public ProductDetailsHelper getProductDetailsHelper()
	{
		return productDetailsHelper;
	}

	/**
	 * @param productDetailsHelper
	 *           the productDetailsHelper to set
	 */
	public void setProductDetailsHelper(final ProductDetailsHelper productDetailsHelper)
	{
		this.productDetailsHelper = productDetailsHelper;
	}

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

	/**
	 * @return the redirectHandlers
	 */
	public Map<KeywordRedirectMatchType, KeywordRedirectHandler> getRedirectHandlers()
	{
		return redirectHandlers;
	}

	/**
	 * @param redirectHandlers
	 *           the redirectHandlers to set
	 */
	public void setRedirectHandlers(final Map<KeywordRedirectMatchType, KeywordRedirectHandler> redirectHandlers)
	{
		this.redirectHandlers = redirectHandlers;
	}


}
