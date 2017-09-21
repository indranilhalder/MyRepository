/**
 *
 */
package com.tisl.mpl.helper;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ClassificationData;
import de.hybris.platform.commercefacades.product.data.FeatureData;
import de.hybris.platform.commercefacades.product.data.FeatureValueData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.PincodeServiceData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.core.Constants.USER;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MplGlobalCodeConstants;
import com.tisl.mpl.core.enums.ClickAndCollectEnum;
import com.tisl.mpl.core.enums.ExpressDeliveryEnum;
import com.tisl.mpl.core.enums.HomeDeliveryEnum;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.core.model.ConfigureImagesCountComponentModel;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.data.WishlistData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.product.MplProductFacade;
import com.tisl.mpl.facade.wishlist.WishlistFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryInformationService;
import com.tisl.mpl.marketplacecommerceservices.service.impl.ExtendedUserServiceImpl;
import com.tisl.mpl.service.GigyaService;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.wsdto.GalleryImageData;


/**
 * @author TCS
 *
 */
public class ProductDetailsHelper
{
	/**
	 *
	 */
	private static final String N = "N";
	public static final String EMPTY = "";
	private final static String COMMACONSTANT = ",";
	/**
	 *
	 */
	private static final String CLASSIFICATION_ATTRIBUTES_ELECTRONICS_GROUPNAME = "classification.attributes.electronics.groupname";

	/*
	 * Added by I313024 for TATAUNISTORE-15 START :::
	 */

	private static final String CLASSIFICATION_ATTRIBUTES_WATCHES_GROUPNAME = "classification.attributes.watches.groupname";
	/*
	 * Added by I313024 for TATAUNISTORE-15 END :::
	 */
	private static final String CLASSIFICATION_ATTRIBUTES_TRAVELANDLUGGAGE_GROUPNAME = "classification.attributes.travelandluggage.groupname";
	/*
	 * Added for travel and Luggage
	 */
	private static final String CLASSIFICATION_ATTRIBUTES_FINEJEWELLERY_GROUPNAME = "classification.attributes.finejewellery.groupname";
	/*
	 * Added for Fine Jewellery
	 */
	private static final String CLASSIFICATION_ATTRIBUTES_FASHIONJEWELLERY_GROUPNAME = "classification.attributes.fashionjewellery.groupname";
	/*
	 * Added for Fashion Jewellery
	 */
	//	added for fine jewellery
	private static final String CLASSIFICATION_ATTRIBUTES_DETAILS_ORDER = "classification.attributes.details.order";
	/**
	 *
	 */
	private static final String N_A = "n/a";
	@Resource
	private ClassificationService classificationService;

	@Autowired
	private MplDeliveryCostService deliveryCostService;

	@Resource(name = "GigyaService")
	private GigyaService gigyaservice;

	@Autowired
	private ExtendedUserServiceImpl userexService;

	@Resource(name = "buyBoxService")
	private BuyBoxService buyBoxService;

	//SOnar fixes
	//@Autowired
	//private SiteConfigService siteConfigService;

	public GigyaService getGigyaservice()
	{
		return gigyaservice;
	}

	public void setGigyaservice(final GigyaService gigyaservice)
	{
		this.gigyaservice = gigyaservice;
	}

	/**
	 * @return the deliveryCostService
	 */
	public MplDeliveryCostService getDeliveryCostService()
	{
		return deliveryCostService;
	}

	/**
	 * @param deliveryCostService
	 *           the deliveryCostService to set
	 */
	public void setDeliveryCostService(final MplDeliveryCostService deliveryCostService)
	{
		this.deliveryCostService = deliveryCostService;
	}

	@Resource(name = "mplProductFacade")
	private MplProductFacade mplProductFacade;

	/**
	 *
	 */
	private static final String MPL_CONTENT_CATALOG = "mplContentCatalog";
	private static final String MEDIA_PRIORITY = "mediaPriority";
	private static final String COLOUR = "Colour";
	private static final String SETTING = "Setting";
	private static final String SHAPE = "Shape";
	private static final String COUNT = "Count";
	private static final String SIZE = "Size";
	private static final String STONE_FINE_JEWEL = "stonefinejwlry";
	private static final String STONE_FINE_JEWEL_COLOR = "stonecolorfinejwlry";
	private static final String STONE_FINE_JEWEL_COUNT = "stonecountfinejwlry";
	private static final String STONE_FINE_JEWEL_SIZE = "stonesizefinejwlry";
	private static final String STONE_FINE_JEWEL_SETTING = "stonesettingfinejwlry";
	private static final String STONE_FINE_JEWEL_SHAPE = "stoneshapefinejwlry";
	private static final String STONE_FINE_JEWEL_DETAILS = " Details";

	@Resource(name = "mplDeliveryCostService")
	private MplDeliveryCostService mplDeliveryCostService;
	@Autowired
	private WishlistFacade wishlistFacade;
	@Autowired
	private UserService userService;
	@Autowired
	private MplDeliveryInformationService mplDeliveryInformationService;
	@Autowired
	private FlexibleSearchService flexibleSearchService;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private CatalogVersionService catalogVersionService;
	@Resource(name = "productService")
	private ProductService productService;
	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;

	private PriceDataFactory priceDataFactory;

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(ProductDetailsHelper.class);

	/**
	 * Populating different delivery modes
	 *
	 * @param rich
	 * @param skuid
	 * @return-MarketplaceDeliveryModeData
	 */
	public List<MarketplaceDeliveryModeData> getDeliveryModeLlist(final RichAttributeModel rich, final String skuid)
	{
		final List<MarketplaceDeliveryModeData> deliveryModeDataList = new ArrayList<MarketplaceDeliveryModeData>();
		final StringBuilder deliveryMode = new StringBuilder();

		if (null != rich.getHomeDelivery() && rich.getHomeDelivery().equals(HomeDeliveryEnum.YES))
		{
			deliveryMode.append(MarketplaceFacadesConstants.HD);
			deliveryMode.append(COMMACONSTANT);//sonar fix
		}

		if (null != rich.getClickAndCollect() && rich.getClickAndCollect().equals(ClickAndCollectEnum.YES))
		{
			deliveryMode.append(MarketplaceFacadesConstants.C_C);
			deliveryMode.append(COMMACONSTANT);//sonar fix
		}

		if (null != rich.getExpressDelivery() && rich.getExpressDelivery().equals(ExpressDeliveryEnum.YES))
		{
			deliveryMode.append(MarketplaceFacadesConstants.EXPRESS);
		}


		if (deliveryMode.length() > 0)
		{
			final List<MplZoneDeliveryModeValueModel> mplZoneDeliveryModeValueModelList = getMplDeliveryCostService()
					.getDeliveryCost(deliveryMode, MarketplaceFacadesConstants.INR, skuid);

			if (CollectionUtils.isNotEmpty(mplZoneDeliveryModeValueModelList))
			{
				final List<MarketplaceDeliveryModeData> dataList = getDeliveryData(mplZoneDeliveryModeValueModelList, rich);
				if (CollectionUtils.isNotEmpty(dataList))
				{
					deliveryModeDataList.addAll(dataList);
				}
			}

		}

		return deliveryModeDataList;
	}

	/**
	 * Populate Data Class for Delivery Information
	 *
	 * CAR-266
	 *
	 * @param mplZoneDeliveryModeValueModelList
	 * @param rich
	 * @return dataList
	 */
	private List<MarketplaceDeliveryModeData> getDeliveryData(
			final List<MplZoneDeliveryModeValueModel> mplZoneDeliveryModeValueModelList, final RichAttributeModel rich)
	{
		final List<MarketplaceDeliveryModeData> dataList = new ArrayList<MarketplaceDeliveryModeData>();

		for (final MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel : mplZoneDeliveryModeValueModelList)
		{
			final MarketplaceDeliveryModeData deliveryModeData = new MarketplaceDeliveryModeData();
			final PriceData priceData = formPriceData(mplZoneDeliveryModeValueModel.getValue());

			deliveryModeData.setCode(mplZoneDeliveryModeValueModel.getDeliveryMode().getCode());
			deliveryModeData.setDescription(mplZoneDeliveryModeValueModel.getDeliveryMode().getDescription());
			deliveryModeData.setName(mplZoneDeliveryModeValueModel.getDeliveryMode().getName());
			deliveryModeData.setSellerArticleSKU(mplZoneDeliveryModeValueModel.getSellerArticleSKU());

			if (rich.getDeliveryFulfillModes().getCode().equalsIgnoreCase("tship"))
			{
				final PriceData priceForTshipItem = formPriceData(Double.valueOf(0.0));
				deliveryModeData.setDeliveryCost(priceForTshipItem);
			}
			else
			{
				deliveryModeData.setDeliveryCost(priceData);
			}

			dataList.add(deliveryModeData);
		}

		return dataList;
	}

	public void groupGlassificationData(final ProductData productData)
	{

		//	final List<FeatureData> featureDataList = new ArrayList<FeatureData>();
		final List<ClassificationData> classicationDataList = new ArrayList<ClassificationData>();
		if (null != productData.getClassifications())
		{
			for (final ClassificationData classData : productData.getClassifications())
			{
				if (classicationDataList.isEmpty()
						&& !(classData.getName().equalsIgnoreCase(N_A))
						&& (configurationService.getConfiguration().getString(CLASSIFICATION_ATTRIBUTES_ELECTRONICS_GROUPNAME)
								.contains(classData.getName())
								|| configurationService.getConfiguration().getString(CLASSIFICATION_ATTRIBUTES_WATCHES_GROUPNAME)
										.contains(classData.getName()) || configurationService.getConfiguration()
								.getString(CLASSIFICATION_ATTRIBUTES_TRAVELANDLUGGAGE_GROUPNAME).contains(classData.getName())))
				{
					classicationDataList.add(classData);
				}
				else
				{
					final ClassificationData cData = getExistingClasificationData(classData.getName(), classicationDataList);
					if (cData != null)
					{
						final List<FeatureData> featureList = new ArrayList<FeatureData>(cData.getFeatures());
						featureList.addAll(classData.getFeatures());
						cData.getFeatures().clear();
						cData.setFeatures(featureList);
					}
					else
					{
						if (!(classData.getName().equalsIgnoreCase(N_A))
								&& (configurationService.getConfiguration().getString(CLASSIFICATION_ATTRIBUTES_ELECTRONICS_GROUPNAME)
										.contains(classData.getName())
										|| configurationService.getConfiguration().getString(CLASSIFICATION_ATTRIBUTES_WATCHES_GROUPNAME)
												.contains(classData.getName()) || configurationService.getConfiguration()
										.getString(CLASSIFICATION_ATTRIBUTES_TRAVELANDLUGGAGE_GROUPNAME).contains(classData.getName())))
						{
							classicationDataList.add(classData);
						}
					}
				}
			}
			productData.setClassifications(classicationDataList);
		}
	}


	public void groupGlassificationDataForJewelDetails(final ProductData productData)
	{
		final LinkedHashMap<String, Map<String, List<String>>> featureDetailsMap = new LinkedHashMap<String, Map<String, List<String>>>();
		final LinkedHashMap<String, Map<String, List<String>>> featureDetails = new LinkedHashMap<String, Map<String, List<String>>>();
		String classificationName = null;
		if (null != productData.getClassifications())
		{
			for (final ClassificationData classData : productData.getClassifications())
			{
				if (!(classData.getName().equalsIgnoreCase(N_A))
						&& (configurationService.getConfiguration().getString(CLASSIFICATION_ATTRIBUTES_FINEJEWELLERY_GROUPNAME)
								.contains(classData.getName()) || configurationService.getConfiguration()
								.getString(CLASSIFICATION_ATTRIBUTES_FASHIONJEWELLERY_GROUPNAME).contains(classData.getName())))
				{
					final List<FeatureData> classDataName = new ArrayList<FeatureData>(classData.getFeatures());

					if (classData.getName().equalsIgnoreCase("Stone Details"))
					{
						int countStone = 0;
						final Map<String, Collection<FeatureValueData>> stoneFeatureDataMap = new HashMap<>();
						for (final FeatureData feature : classDataName)
						{

							stoneFeatureDataMap.put(
									feature.getCode().substring(feature.getCode().indexOf('.') + 1, feature.getCode().length()),
									feature.getFeatureValues());

							if (feature.getCode().substring(feature.getCode().indexOf('.') + 1, feature.getCode().length())
									.startsWith(STONE_FINE_JEWEL))
							{
								countStone++;
							}
						}
						for (int i = 1; i <= countStone; i++)
						{
							final Collection<FeatureValueData> name = stoneFeatureDataMap.get(STONE_FINE_JEWEL + i);
							if (stoneFeatureDataMap.containsKey(STONE_FINE_JEWEL + i)
									&& featureDetails.containsKey(name.stream().findFirst().get().getValue() + STONE_FINE_JEWEL_DETAILS))
							{
								final LinkedHashMap<String, List<String>> featureMap = new LinkedHashMap<>();
								final Map<String, List<String>> featureMapUpdate = featureDetails.get(name.stream().findFirst().get()
										.getValue()
										+ STONE_FINE_JEWEL_DETAILS);
								final Collection<FeatureValueData> color = stoneFeatureDataMap.get(STONE_FINE_JEWEL_COLOR + i);

								{
									final List valueList = new ArrayList<>();
									if (featureMapUpdate.get(COLOUR) != null)
									{
										valueList.addAll(featureMapUpdate.get(COLOUR));
									}
									if (color != null)
									{
										valueList.add(color.stream().findFirst().get().getValue());
									}
									featureMap.put(COLOUR, valueList);
								}

								final Collection<FeatureValueData> setting = stoneFeatureDataMap.get(STONE_FINE_JEWEL_SETTING + i);

								{
									final List valueList = new ArrayList<>();
									if (featureMapUpdate.get(SETTING) != null)
									{
										valueList.addAll(featureMapUpdate.get(SETTING));
									}
									if (setting != null)
									{
										valueList.add(setting.stream().findFirst().get().getValue());
									}
									featureMap.put(SETTING, valueList);
								}
								final Collection<FeatureValueData> shape = stoneFeatureDataMap.get(STONE_FINE_JEWEL_SHAPE + i);

								{
									final List valueList = new ArrayList<>();
									if (featureMapUpdate.get(SHAPE) != null)
									{
										valueList.addAll(featureMapUpdate.get(SHAPE));
									}
									if (shape != null)
									{
										valueList.add(shape.stream().findFirst().get().getValue());
									}
									featureMap.put(SHAPE, valueList);
								}

								final Collection<FeatureValueData> count = stoneFeatureDataMap.get(STONE_FINE_JEWEL_COUNT + i);

								{
									final List valueList = new ArrayList<>();
									if (featureMapUpdate.get(COUNT) != null)
									{
										valueList.addAll(featureMapUpdate.get(COUNT));
									}
									if (count != null)
									{
										valueList.add(count.stream().findFirst().get().getValue());
									}
									featureMap.put(COUNT, valueList);
								}

								final Collection<FeatureValueData> size = stoneFeatureDataMap.get(STONE_FINE_JEWEL_SIZE + i);

								{
									final List valueList = new ArrayList<>();
									if (featureMapUpdate.get(SIZE) != null)
									{
										valueList.addAll(featureMapUpdate.get(SIZE));
									}
									if (size != null)
									{
										valueList.add(size.stream().findFirst().get().getValue());
									}
									featureMap.put(SIZE, valueList);
								}
								featureDetails.put(name.stream().findFirst().get().getValue() + STONE_FINE_JEWEL_DETAILS, featureMap);
							}

							else if (stoneFeatureDataMap.containsKey(STONE_FINE_JEWEL + i))
							{
								final LinkedHashMap<String, List<String>> featureMap = new LinkedHashMap<>();
								final Collection<FeatureValueData> color = stoneFeatureDataMap.get(STONE_FINE_JEWEL_COLOR + i);
								if (color != null)
								{
									final List valueList = new ArrayList<>();
									valueList.add(color.stream().findFirst().get().getValue());
									featureMap.put(COLOUR, valueList);
								}

								final Collection<FeatureValueData> setting = stoneFeatureDataMap.get(STONE_FINE_JEWEL_SETTING + i);
								if (setting != null)
								{
									final List valueList = new ArrayList<>();
									valueList.add(setting.stream().findFirst().get().getValue());
									featureMap.put(SETTING, valueList);
								}

								final Collection<FeatureValueData> shape = stoneFeatureDataMap.get(STONE_FINE_JEWEL_SHAPE + i);
								if (shape != null)
								{
									final List valueList = new ArrayList<>();
									valueList.add(shape.stream().findFirst().get().getValue());
									featureMap.put(SHAPE, valueList);
								}

								final Collection<FeatureValueData> count = stoneFeatureDataMap.get(STONE_FINE_JEWEL_COUNT + i);
								if (count != null)
								{
									final List valueList = new ArrayList<>();
									valueList.add(count.stream().findFirst().get().getValue());
									featureMap.put(COUNT, valueList);
								}

								final Collection<FeatureValueData> size = stoneFeatureDataMap.get(STONE_FINE_JEWEL_SIZE + i);
								if (size != null)
								{
									final List valueList = new ArrayList<>();
									valueList.add(size.stream().findFirst().get().getValue());
									featureMap.put(SIZE, valueList);
								}
								featureDetails.put(name.stream().findFirst().get().getValue() + STONE_FINE_JEWEL_DETAILS, featureMap);
							}


						}

					}
					else
					{
						final LinkedHashMap<String, List<String>> featureMap = new LinkedHashMap<String, List<String>>();
						classificationName = classData.getName();
						for (final FeatureData feature : classDataName)
						{
							final ProductFeatureModel productFeature = mplProductFacade.getProductFeatureModelByProductAndQualifier(
									productData, feature.getCode());
							final String unit = productFeature != null && productFeature.getUnit() != null
									&& !productFeature.getUnit().getSymbol().isEmpty() ? productFeature.getUnit().getSymbol() : "";
							final String featurename = feature.getName();
							final List<FeatureValueData> featuredvalue = new ArrayList<FeatureValueData>(feature.getFeatureValues());
							final List<String> featureValueList = new ArrayList<String>();
							for (final FeatureValueData featurevalue : featuredvalue)
							{
								final String featureV = featurevalue.getValue();
								final List<String> featureMapKeys = new ArrayList<String>();
								for (final Entry<String, List<String>> featureMapKey : featureMap.entrySet())//null check
								{
									featureMapKeys.add(featureMapKey.getKey());
								}
								if (featureMapKeys.contains(featurename))
								{
									for (final String key : featureMapKeys)
									{
										if (key != null && key.equalsIgnoreCase(featurename))
										{
											featureMap.get(featurename).add(featureV + MarketplaceFacadesConstants.SPACE + unit);
										}
									}
								}
								else
								{
									featureValueList.add(featureV + MarketplaceFacadesConstants.SPACE + unit);
									featureMap.put(featurename, featureValueList);
								}
							}
						}
						final List<String> keyList = new ArrayList<String>();
						if (!featureDetails.isEmpty())
						{
							for (final Entry<String, Map<String, List<String>>> keys : featureDetails.entrySet())
							{
								keyList.add(keys.getKey());
							}
						}
						if (keyList.contains(classificationName))
						{
							for (final String key : keyList)
							{
								if (key != null && key.equalsIgnoreCase(classificationName))
								{
									featureDetails.get(classificationName).putAll(featureMap);
								}
							}
						}
						else
						{
							featureDetails.put(classificationName, featureMap);
						}
					}
				}
			}
			//JWLSPCUAT-101:Product details section should be top in PDP page
			final String featureDetailsOrder = configurationService.getConfiguration().getString(
					CLASSIFICATION_ATTRIBUTES_DETAILS_ORDER);
			if (StringUtils.isNotEmpty(featureDetailsOrder))
			{
				final String[] featureDetailsOrderArray = featureDetailsOrder.split(",");

				for (final String keyArray : featureDetailsOrderArray)
				{
					if (featureDetails.containsKey(keyArray))
					{
						featureDetailsMap.put(keyArray, featureDetails.get(keyArray));
					}
				}
				for (final String keyArray : featureDetails.keySet())
				{
					if (!featureDetailsMap.containsKey(keyArray))
					{
						featureDetailsMap.put(keyArray, featureDetails.get(keyArray));
					}
				}
			}
			else
			{
				featureDetailsMap.putAll(featureDetails);
			}
			//productData.setFineJewelleryDeatils(featureDetails);
			productData.setFineJewelleryDeatils(featureDetailsMap);
		}
	}

	public ClassificationData getExistingClasificationData(final String name, final List<ClassificationData> classificationList)
	{
		ClassificationData classificationData = null;
		for (final ClassificationData cData : classificationList)
		{
			if (cData.getName().equals(name))
			{
				classificationData = cData;
			}
		}
		return classificationData;
	}



	public PriceData formPriceData(final Double price)
	{

		return priceDataFactory.create(PriceDataType.BUY, new BigDecimal(price.doubleValue()), MarketplaceFacadesConstants.INR);
	}

	/**
	 * @param productCode
	 * @param ussid
	 * @param wishName
	 * @param sizeSelected
	 */
	public boolean addToWishListInPopup(final String productCode, final String ussid, final String wishName,
			final Boolean sizeSelected)
	{

		boolean add = false;
		try
		{
			final Wishlist2Model existingWishlist = wishlistFacade.getWishlistForName(wishName);
			//  boolean add=
			//checking whether the wishlist with given name exists or not
			LOG.debug("addToWishListInPopup: *****productCode: " + productCode + " **** ussid: " + ussid + " *** wishName: "
					+ wishName);
			if (null != existingWishlist)
			{
				add = wishlistFacade.addProductToWishlist(existingWishlist, productCode, ussid, sizeSelected.booleanValue());

				LOG.debug("addToWishListInPopup: ***** existingWishlist: add" + add);
			}
			else
			{
				LOG.debug("addToWishListInPopup: ***** New Create");
				final UserModel user = userService.getCurrentUser();
				final Wishlist2Model createdWishlist = wishlistFacade.createNewWishlist(user, wishName, productCode);
				add = wishlistFacade.addProductToWishlist(createdWishlist, productCode, ussid, sizeSelected.booleanValue());
				final WishlistData wishData = new WishlistData();
				wishData.setParticularWishlistName(createdWishlist.getName());
				//existingWishlist = wishlistFacade.getWishlistForName(wishName);
				wishData.setProductCode(productCode);
			}
			if (!add) //add == false
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B3002);
			}
		}
		catch (final EtailBusinessExceptions e)
		{

			throw e;
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return add;
	}

	/**
	 * @param productCode
	 */
	public List<WishlistData> showWishListsInPopUp(final String productCode)
	{
		final UserModel user = userService.getCurrentUser();

		//If the user is not logged in then ask customer to login.
		if (null != user.getName() && user.getName().equalsIgnoreCase(USER.ANONYMOUS_CUSTOMER))
		{
			return null;
		}

		//loading all the existing wishlists
		final List<WishlistData> wishListData = new ArrayList<WishlistData>();
		try
		{
			final List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists();
			final int wishListSize = allWishlists.size();

			if (wishListSize != 0)
			{
				for (final Wishlist2Model wish : allWishlists)
				{
					final WishlistData wishList = new WishlistData();
					wishList.setParticularWishlistName(wish.getName());
					final List<String> ussidEntries = new ArrayList<String>();
					for (final Wishlist2EntryModel wlEntry : wish.getEntries())
					{
						//TISEE-6376
						if (wlEntry.getProduct() != null && wlEntry.getProduct().getCode() != null
								&& wlEntry.getProduct().getCode().equals(productCode)
								&& (!wlEntry.getIsDeleted().booleanValue() || wlEntry.getIsDeleted() == null))//TPR-5787 TISSPTEN-9 check added
						{
							ussidEntries.add(wlEntry.getUssid());
						}
					}
					wishList.setUssidEntries(ussidEntries);
					wishListData.add(wishList);
				}
			}
		}
		catch (final IllegalArgumentException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return wishListData;
	}

	public List<Map<String, ImageData>> getGalleryImages(final ProductData productData)
	{

		final List<Map<String, ImageData>> galleryImages = new ArrayList<>();
		try
		{
			if (CollectionUtils.isNotEmpty(productData.getImages()))
			{
				final List<ImageData> images = new ArrayList<>();
				for (final ImageData image : productData.getImages())
				{
					if (ImageDataType.GALLERY.equals(image.getImageType()))
					{
						images.add(image);
					}
				}
				final Comparator<ImageData> comp = new BeanComparator(MEDIA_PRIORITY);
				Collections.sort(images, comp);
				if (CollectionUtils.isNotEmpty(images))
				{
					int currentIndex = images.get(0).getGalleryIndex().intValue();
					Map<String, ImageData> formats = new HashMap<String, ImageData>();
					for (final ImageData image : images)
					{
						if (currentIndex != image.getGalleryIndex().intValue())
						{
							galleryImages.add(formats);
							formats = new HashMap<>();
							currentIndex = image.getGalleryIndex().intValue();
						}
						formats.put(image.getFormat(), image);
					}
					if (!formats.isEmpty())
					{
						galleryImages.add(formats);
					}
				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return galleryImages;
	}

	/**
	 * frame gallery images for mobile web service
	 *
	 * @param productData
	 * @return List<Map<String, String>>
	 */
	public List<GalleryImageData> getGalleryImagesMobile(final ProductData productData)
	{

		final List<Map<String, String>> galleryImages = new ArrayList<>();
		final List<GalleryImageData> galleryImageList = new ArrayList<GalleryImageData>();
		GalleryImageData galleryImageData = null;
		try
		{
			if (CollectionUtils.isNotEmpty(productData.getImages()))
			{
				final List<ImageData> images = new ArrayList<>();
				for (final ImageData image : productData.getImages())
				{
					if (ImageDataType.GALLERY.equals(image.getImageType()))
					{
						images.add(image);
					}
				}
				/*
				 * final BeanComparator reverseOrderBeanComparator = new BeanComparator(MEDIA_PRIORITY, new
				 * ReverseComparator( new ComparableComparator())); Collections.sort(images, reverseOrderBeanComparator);
				 */
				final Comparator<ImageData> comp = new BeanComparator(MEDIA_PRIORITY);
				Collections.sort(images, comp);
				if (CollectionUtils.isNotEmpty(images))
				{
					int currentIndex = images.get(0).getGalleryIndex().intValue();
					Map<String, String> formats = new HashMap<String, String>();
					galleryImageData = new GalleryImageData();
					for (final ImageData image : images)
					{
						if (currentIndex != image.getGalleryIndex().intValue())
						{
							galleryImages.add(formats);
							galleryImageData.setGalleryImages(formats);
							galleryImageList.add(galleryImageData);
							formats = new HashMap<>();
							galleryImageData = new GalleryImageData();
							currentIndex = image.getGalleryIndex().intValue();
						}
						if (null != image.getFormat() && !image.getFormat().isEmpty() && null != image.getUrl()
								&& !image.getUrl().isEmpty())
						{
							formats.put(image.getFormat(), image.getUrl());
							galleryImageData.setGalleryImages(formats);
						}
						if (null != image.getMediaType() && null != image.getMediaType().getCode())
						{
							galleryImageData.setMediaType(image.getMediaType().getCode());
						}
						if (null != image.getMediaType() && null != image.getMediaType().getCode()
								&& image.getMediaType().getCode().equalsIgnoreCase("Video"))
						{
							galleryImageData.setStaticImage(MarketplaceFacadesConstants.STATIC_VIDEO);
						}

					}

					if (!formats.isEmpty())
					{
						galleryImageList.add(galleryImageData);
						galleryImages.add(formats);

					}
				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return galleryImageList;
	}

	/**
	 * frame gallery images for mobile web service TPR-796
	 *
	 * @param productData
	 * @return List<Map<String, String>>
	 */
	public List<GalleryImageData> getPrimaryGalleryImagesMobile(final ProductData productData)
	{
		int currentIndex = 0;
		final List<Map<String, String>> galleryImages = new ArrayList<>();
		final List<GalleryImageData> galleryImageList = new ArrayList<GalleryImageData>();
		GalleryImageData galleryImageData = null;
		try
		{
			if (CollectionUtils.isNotEmpty(productData.getImages()))
			{
				final List<ImageData> images = new ArrayList<>();
				for (final ImageData image : productData.getImages())
				{
					if (ImageDataType.PRIMARY.equals(image.getImageType()))
					{
						images.add(image);
					}
				}
				/*
				 * final BeanComparator reverseOrderBeanComparator = new BeanComparator(MEDIA_PRIORITY, new
				 * ReverseComparator( new ComparableComparator())); Collections.sort(images, reverseOrderBeanComparator);
				 */
				//final Comparator<ImageData> comp = new BeanComparator(MEDIA_PRIORITY);
				//Collections.sort(images, comp);
				if (CollectionUtils.isNotEmpty(images))
				{
					Map<String, String> formats = new HashMap<String, String>();
					galleryImageData = new GalleryImageData();
					if (images.get(0).getGalleryIndex() != null)
					{
						currentIndex = images.get(0).getGalleryIndex().intValue();
					}
					for (final ImageData image : images)
					{
						if (null != image.getGalleryIndex() && currentIndex != image.getGalleryIndex().intValue())
						{
							galleryImages.add(formats);
							galleryImageData.setGalleryImages(formats);
							galleryImageList.add(galleryImageData);
							formats = new HashMap<>();
							galleryImageData = new GalleryImageData();
							currentIndex = image.getGalleryIndex().intValue();
						}
						if (null != image.getFormat() && !image.getFormat().isEmpty() && null != image.getUrl()
								&& !image.getUrl().isEmpty())
						{
							formats.put(image.getFormat(), image.getUrl());
							galleryImageData.setGalleryImages(formats);
						}
						if (null != image.getMediaType() && null != image.getMediaType().getCode())
						{
							galleryImageData.setMediaType(image.getMediaType().getCode());
						}
						if (null != image.getMediaType() && null != image.getMediaType().getCode()
								&& image.getMediaType().getCode().equalsIgnoreCase("Video"))
						{
							//IQA comment
							galleryImageData.setStaticImage(MarketplaceFacadesConstants.STATIC_VIDEO);
						}

					}
					//TODO mediatype can be made dynamic
					galleryImageData.setMediaType(MarketplacecommerceservicesConstants.IMAGE_MEDIA_TYPE);

					galleryImageList.add(galleryImageData);

					if (MapUtils.isNotEmpty(formats))
					{
						galleryImages.add(formats);
					}

				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return galleryImageList;
	}

	public Map<String, Map<String, Integer>> getDeliveryModeATMap(final List<String> deliveryInfoList)
	{
		//		final Map<String, String> deliveryModeATMap = new HashMap<String, String>();
		final Map<String, Map<String, Integer>> deliveryModeATMap = new HashMap<String, Map<String, Integer>>();
		try
		{
			final List<DeliveryModeModel> deliveryInformationList = mplDeliveryInformationService
					.getDeliveryInformation(deliveryInfoList);
			if (deliveryInformationList.isEmpty())
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B3003);
			}
			else
			{
				for (final DeliveryModeModel deliveryMode : deliveryInformationList)
				{
					if (null != deliveryMode.getCode())
					{
						if (deliveryMode.getCode().equals(MarketplaceFacadesConstants.HOME_DELIVERY))
						{
							//deliveryModeATMap.put(deliveryMode.getCode(), deliveryMode.getDescription());
							final Map<String, Integer> deliveryModeMap = new HashMap<String, Integer>();
							deliveryModeMap.put("startForHome", deliveryMode.getStart());
							deliveryModeMap.put("endForHome", deliveryMode.getEnd());
							deliveryModeATMap.put(deliveryMode.getCode(), deliveryModeMap);
						}
						if (deliveryMode.getCode().equals(MarketplaceFacadesConstants.EXPRESS_DELIVERY))
						{
							//deliveryModeATMap.put(deliveryMode.getCode(), deliveryMode.getDescription());
							final Map<String, Integer> deliveryModeMap = new HashMap<String, Integer>();
							deliveryModeMap.put("startForExpress", deliveryMode.getStart());
							deliveryModeMap.put("endForExpress", deliveryMode.getEnd());
							deliveryModeATMap.put(deliveryMode.getCode(), deliveryModeMap);
						}

						if (deliveryMode.getCode().equals(MarketplaceFacadesConstants.CLICK_AND_COLLECT))
						{
							//deliveryModeATMap.put(deliveryMode.getCode(), deliveryMode.getDescription());
							final Map<String, Integer> deliveryModeMap = new HashMap<String, Integer>();
							deliveryModeMap.put("startForClick", deliveryMode.getStart());
							deliveryModeMap.put("endForClick", deliveryMode.getEnd());
							deliveryModeATMap.put(deliveryMode.getCode(), deliveryModeMap);
						}
					}

				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return deliveryModeATMap;
	}


	public List<String> getBrandsForProduct(final ProductModel productModel)
	{
		List<String> brandList = null;
		try
		{

			final List<CategoryModel> categories = getImmediateSuperCategory(productModel);//(List<CategoryModel>) productModel.getSupercategories();
			if (categories != null && !categories.isEmpty())
			{
				brandList = new ArrayList<String>();
				for (final CategoryModel categoryModel : categories)
				{
					if (categoryModel.getCode().startsWith("MBH"))
					{
						brandList.add(categoryModel.getCode());
					}

				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return brandList;
	}


	private List<CategoryModel> getImmediateSuperCategory(final ProductModel product)
	{

		List<CategoryModel> superCategories = new ArrayList<CategoryModel>();
		try
		{
			if (product != null)
			{

				superCategories = (List<CategoryModel>) product.getSupercategories();

			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return superCategories;


	}

	public int getCountForGalleryImages()
	{
		ConfigureImagesCountComponentModel countComp = null;
		try
		{
			final CatalogVersionModel catalogVersion = getCatalogVersion();
			final String queryString = "SELECT {c.PK} FROM {" + ConfigureImagesCountComponentModel._TYPECODE
					+ " AS c} where {c.catalogVersion}=?catalogVersion";
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("catalogVersion", catalogVersion);
			countComp = flexibleSearchService.<ConfigureImagesCountComponentModel> search(query).getResult().get(0);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return countComp.getCount();

	}

	private CatalogVersionModel getCatalogVersion()
	{
		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(MPL_CONTENT_CATALOG,
				MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_VERSION);
		return catalogVersionModel;
	}

	/**
	 * populating the request data to be send to oms
	 *
	 * @param productCode
	 * @return requestData
	 */
	public List<PincodeServiceData> populatePinCodeServiceData(final String productCode)
	{

		final List<PincodeServiceData> requestData = new ArrayList<>();
		PincodeServiceData data = null;
		MarketplaceDeliveryModeData deliveryModeData = null;
		try
		{
			final ProductModel productModel = productService.getProductForCode(productCode);
			final ProductData productData = productFacade.getProductForOptions(productModel,
					Arrays.asList(ProductOption.BASIC, ProductOption.SELLER, ProductOption.PRICE));

			for (final SellerInformationData seller : productData.getSeller())
			{
				final List<MarketplaceDeliveryModeData> deliveryModeList = new ArrayList<MarketplaceDeliveryModeData>();
				data = new PincodeServiceData();
				if ((null != seller.getDeliveryModes()) && !(seller.getDeliveryModes().isEmpty()))
				{
					for (final MarketplaceDeliveryModeData deliveryMode : seller.getDeliveryModes())
					{
						deliveryModeData = fetchDeliveryModeDataForUSSID(deliveryMode.getCode(), seller.getUssid());
						deliveryModeList.add(deliveryModeData);
					}
					data.setDeliveryModes(deliveryModeList);
				}
				if (null != seller.getFullfillment() && StringUtils.isNotEmpty(seller.getFullfillment()))
				{
					data.setFullFillmentType(MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(seller.getFullfillment().toUpperCase()));
				}
				if (null != seller.getShippingMode() && (StringUtils.isNotEmpty(seller.getShippingMode())))
				{
					data.setTransportMode(MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(seller.getShippingMode().toUpperCase()));
				}
				if (null != seller.getSpPrice() && !(seller.getSpPrice().equals(EMPTY)))
				{
					data.setPrice(new Double(seller.getSpPrice().getValue().doubleValue()));
				}
				else if (null != seller.getMopPrice() && !(seller.getMopPrice().equals(EMPTY)))
				{
					data.setPrice(new Double(seller.getMopPrice().getValue().doubleValue()));
				}
				else if (null != seller.getMrpPrice() && !(seller.getMrpPrice().equals(EMPTY)))
				{
					data.setPrice(new Double(seller.getMrpPrice().getValue().doubleValue()));
				}
				else
				{
					LOG.info("*************** No price avaiable for seller :" + seller.getSellerID());
					continue;
				}
				if (null != seller.getIsCod() && StringUtils.isNotEmpty(seller.getIsCod()))
				{
					data.setIsCOD(seller.getIsCod());
				}
				data.setSellerId(seller.getSellerID());
				data.setUssid(seller.getUssid());
				data.setIsDeliveryDateRequired(N);
				requestData.add(data);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}

		catch (final Exception e)
		{

			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return requestData;
	}


	/**
	 * @param deliveryMode
	 * @param ussid
	 * @return deliveryModeData
	 */
	private MarketplaceDeliveryModeData fetchDeliveryModeDataForUSSID(final String deliveryMode, final String ussid)
	{
		final MarketplaceDeliveryModeData deliveryModeData = new MarketplaceDeliveryModeData();
		final MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = populateDeliveryCostForUSSIDAndDeliveryMode(
				deliveryMode, MarketplaceFacadesConstants.INR, ussid);

		final PriceData priceData = formPriceData(mplZoneDeliveryModeValueModel.getValue());
		deliveryModeData.setCode(mplZoneDeliveryModeValueModel.getDeliveryMode().getCode());
		deliveryModeData.setDescription(mplZoneDeliveryModeValueModel.getDeliveryMode().getDescription());
		deliveryModeData.setName(mplZoneDeliveryModeValueModel.getDeliveryMode().getName());
		deliveryModeData.setSellerArticleSKU(ussid);
		deliveryModeData.setDeliveryCost(priceData);
		return deliveryModeData;
	}


	/*
	 * @description: It is used for populating delivery code and cost for sellerartickeSKU
	 * 
	 * @param deliveryCode
	 * 
	 * @param currencyIsoCode
	 * 
	 * @param sellerArticleSKU
	 * 
	 * @return MplZoneDeliveryModeValueModel
	 * 
	 * @throws EtailNonBusinessExceptions
	 */
	private MplZoneDeliveryModeValueModel populateDeliveryCostForUSSIDAndDeliveryMode(final String deliveryCode,
			final String currencyIsoCode, final String sellerArticleSKU) throws EtailNonBusinessExceptions
	{
		ServicesUtil.validateParameterNotNull(deliveryCode, "deliveryCode cannot be null");
		ServicesUtil.validateParameterNotNull(currencyIsoCode, "currencyIsoCode cannot be null");
		ServicesUtil.validateParameterNotNull(sellerArticleSKU, "sellerArticleSKU cannot be null");
		return getDeliveryCostService().getDeliveryCost(deliveryCode, currencyIsoCode, sellerArticleSKU);
	}



	public Cookie ratingReviewHelper(final CustomerModel customerModel, final boolean isNewUser)
	{
		Cookie ck = null;
		try
		{
			if (!customerModel.getUid().equalsIgnoreCase(MarketplaceFacadesConstants.ANONYMOUS_CUSTOMER))
			{
				final List<String> cookiedat = gigyaservice.gigyaLoginHelper(customerModel, isNewUser);

				if (cookiedat != null && !cookiedat.isEmpty())

				{
					ck = new Cookie(URLEncoder.encode(cookiedat.get(0), MarketplaceFacadesConstants.UTF), URLEncoder.encode(
							cookiedat.get(1), MarketplaceFacadesConstants.UTF));
					ck.setPath("/");
					//ck.setMaxAge(0);
					//if (!cookieSwitch.equalsIgnoreCase("N"))
					//{
					// ck.setDomain(cookiedat.get(2));
					// ck.setMaxAge(0);//To end the final session when the final browser closes
					// ck.setPath(cookiedat.get(3)); //
					//}

					return ck;

				}
			}

			else
			{

				LOG.debug(MarketplaceFacadesConstants.CUSTOMER_ERROR);

			}

		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return ck;
	}

	/**
	 *
	 * @param emailID
	 */

	public void ratingReviewLogout(final String emailID)
	{
		try
		{
			final CustomerModel user = (CustomerModel) userexService.getUserForUID(emailID);
			LOG.debug("*******************" + user.getUid());
			gigyaservice.ratingLogoutHelper(user);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

	}

	/**
	 * get
	 *
	 * @param product
	 * @return String
	 */
	public String getSizeType(final ProductModel product)
	{
		String sizeGuideCode = null;
		//get size chart feature
		final FeatureList featureList = classificationService.getFeatures(product);
		try
		{

			for (final Feature feature : featureList)
			{
				final String featureName = feature.getName().replaceAll("\\s+", "");

				final String sizeChart = configurationService.getConfiguration().getString("product.sizetype.value")
						.replaceAll("\\s+", "");
				if (featureName.equalsIgnoreCase(sizeChart))
				{
					if (null != feature.getValue())
					{
						final FeatureValue sizeGuidefeatureVal = feature.getValue();
						if (sizeGuidefeatureVal != null)
						{
							//						sizeGuideCode = String.valueOf(((ClassificationAttributeValueModel) sizeGuidefeatureVal.getValue()).getCode()
							//								.replaceAll("sizetype", ""));

							if (StringUtils.isNotEmpty(String.valueOf(((ClassificationAttributeValueModel) sizeGuidefeatureVal
									.getValue()).getCode())))
							{
								sizeGuideCode = String.valueOf(
										((ClassificationAttributeValueModel) sizeGuidefeatureVal.getValue()).getCode().replaceAll(
												"sizetype", "")).toUpperCase();
							}
						}
						break;
					}
				}
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return sizeGuideCode;
	}


	/**
	 * @param productCode
	 * @param ussid
	 * @param user
	 * @param valueOf
	 * @return
	 */
	public boolean addSingleToWishList(final String productCode, final String ussid, final Boolean sizeSelected)
	{
		boolean add = false;
		final String wishName = MarketplaceFacadesConstants.DEFAULT_WISHLIST_NAME;
		Wishlist2Model lastCreatedWishlist = null;
		final UserModel user = userService.getCurrentUser();
		try
		{
			lastCreatedWishlist = wishlistFacade.getSingleWishlist(user);
			if (null != lastCreatedWishlist)
			{
				add = wishlistFacade.addProductToWishlist(lastCreatedWishlist, productCode, ussid, sizeSelected.booleanValue());

				LOG.debug("addToWishListInPopup: ***** getLastCreatedWishlist: add" + add);
			}
			else
			{
				LOG.debug("addToWishListInPopup: ***** New Create");
				final Wishlist2Model createdWishlist = wishlistFacade.createNewWishlist(user, wishName, productCode);
				add = wishlistFacade.addProductToWishlist(createdWishlist, productCode, ussid, sizeSelected.booleanValue());
				final WishlistData wishData = new WishlistData();
				wishData.setParticularWishlistName(createdWishlist.getName());
				//existingWishlist = wishlistFacade.getWishlistForName(wishName);
				wishData.setProductCode(productCode);
			}
			if (!add) //add == false
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B3002);
			}
		}
		catch (final EtailBusinessExceptions e)
		{

			throw e;
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return add;
	}

	/**
	 * @param ussid
	 * @return
	 */
	public boolean getLastModifiedWishlistByUssid(final String ussid)
	{
		Wishlist2Model lastCreatedWishlist = null;
		boolean existUssid = false;
		try
		{
			final UserModel user = userService.getCurrentUser();
			lastCreatedWishlist = wishlistFacade.getSingleWishlist(user);
			if (null != lastCreatedWishlist)
			{
				for (final Wishlist2EntryModel entry : lastCreatedWishlist.getEntries())
				{
					if (null != (entry) && null != entry.getUssid() && (entry.getUssid()).equalsIgnoreCase(ussid)
							&& (!entry.getIsDeleted().booleanValue() || entry.getIsDeleted() == null))//TPR-5787 condition added
					{
						existUssid = true;
						break;
					}
				}
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			throw e;
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return existUssid;
	}



	/**
	 * @param productCode
	 * @param valueOf
	 * @return
	 */
	public boolean addSingleToWishListForPLP(final String productCode, final String ussid, final Boolean sizeSelected)
	{
		boolean add = false;
		final String wishName = MarketplaceFacadesConstants.DEFAULT_WISHLIST_NAME;
		Wishlist2Model lastCreatedWishlist = null;
		final UserModel user = userService.getCurrentUser();
		//String ussid = null;
		//		if (getBuyBoxService().getBuyboxPricesForSearch(productCode) != null)
		//		{
		//			ussid = getBuyBoxService().getBuyboxPricesForSearch(productCode).get(0).getSellerArticleSKU();
		//		}
		try
		{
			lastCreatedWishlist = wishlistFacade.getSingleWishlist(user);
			if (null != lastCreatedWishlist)
			{
				add = wishlistFacade.addProductToWishlist(lastCreatedWishlist, productCode, ussid, sizeSelected.booleanValue());

				LOG.debug("addToWishListInPopup: ***** getLastCreatedWishlist: add" + add);
			}
			else
			{
				LOG.debug("addToWishListInPopup: ***** New Create");
				final Wishlist2Model createdWishlist = wishlistFacade.createNewWishlist(user, wishName, productCode);
				add = wishlistFacade.addProductToWishlist(createdWishlist, productCode, ussid, sizeSelected.booleanValue());
				final WishlistData wishData = new WishlistData();
				wishData.setParticularWishlistName(createdWishlist.getName());
				//existingWishlist = wishlistFacade.getWishlistForName(wishName);
				wishData.setProductCode(productCode);
			}
			if (!add) //add == false
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B3002);
			}
		}
		catch (final EtailBusinessExceptions e)
		{

			throw e;
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return add;
	}

	/* Changes for INC144313867 */

	/**
	 * @param productCode
	 * @param ussid
	 * @return
	 */
	public boolean removeFromWishListForPLP(final String productCode)
	{

		Wishlist2Model lastCreatedWishlist = null;
		Wishlist2Model removedWishlist = null;
		boolean removeFromWl = false;
		//final String ussid = null;
		try
		{
			final UserModel user = userService.getCurrentUser();
			lastCreatedWishlist = wishlistFacade.getSingleWishlist(user);
			if (null != lastCreatedWishlist)
			{

				if (getBuyBoxService().getBuyboxPricesForSearch(productCode) != null)
				{
					//ussid = getBuyBoxService().getBuyboxPricesForSearch(productCode).get(0).getSellerArticleSKU();
					//if (null != ussid)
					//{
					//removedWishlist = wishlistFacade.removeProductFromWl(productCode, lastCreatedWishlist.getName(), ussid);

					//}
					removedWishlist = wishlistFacade.removeProductFromWl(productCode, lastCreatedWishlist.getName());
				}
			}
			if (null != removedWishlist)
			{
				removeFromWl = true;
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			throw e;
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return removeFromWl;
	}

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
	 * @param productCode
	 * @param ussid
	 * @return
	 */
	public boolean removeFromWishList(final String productCode, final String ussid)
	{

		Wishlist2Model lastCreatedWishlist = null;
		Wishlist2Model removedWishlist = null;
		boolean removeFromWl = false;
		try
		{
			final UserModel user = userService.getCurrentUser();
			lastCreatedWishlist = wishlistFacade.getSingleWishlist(user);
			if (null != lastCreatedWishlist)
			{
				removedWishlist = wishlistFacade.removeProductFromWl(productCode, lastCreatedWishlist.getName(), ussid);
			}
			if (null != removedWishlist)
			{
				removeFromWl = true;
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			throw e;
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return removeFromWl;
	}

	/**
	 * @param ussID
	 * @return
	 */
	public BuyBoxModel buyboxPriceForJewelleryWithVariant(final String ussID)
	{
		BuyBoxModel buyBox = null;
		final List<BuyBoxModel> buyBoxList = getBuyBoxService().buyboxPriceForJewelleryWithVariant(ussID);
		if (CollectionUtils.isNotEmpty(buyBoxList))
		{
			buyBox = buyBoxList.get(0);
		}
		return buyBox;
	}



	@Required
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}

	/**
	 * @return the classificationService
	 */
	public ClassificationService getClassificationService()
	{
		return classificationService;
	}

	/**
	 * @param classificationService
	 *           the classificationService to set
	 */
	public void setClassificationService(final ClassificationService classificationService)
	{
		this.classificationService = classificationService;
	}

	/**
	 * @return the deliveryCostService
	 */
	public MplDeliveryCostService getDeliveryCostService()
	{
		return deliveryCostService;
	}

	/**
	 * @param deliveryCostService
	 *           the deliveryCostService to set
	 */
	public void setDeliveryCostService(final MplDeliveryCostService deliveryCostService)
	{
		this.deliveryCostService = deliveryCostService;
	}

	/**
	 * @return the gigyaservice
	 */
	public GigyaService getGigyaservice()
	{
		return gigyaservice;
	}

	/**
	 * @param gigyaservice
	 *           the gigyaservice to set
	 */
	public void setGigyaservice(final GigyaService gigyaservice)
	{
		this.gigyaservice = gigyaservice;
	}

	/**
	 * @return the userexService
	 */
	public ExtendedUserServiceImpl getUserexService()
	{
		return userexService;
	}

	/**
	 * @param userexService
	 *           the userexService to set
	 */
	public void setUserexService(final ExtendedUserServiceImpl userexService)
	{
		this.userexService = userexService;
	}

	/**
	 * @return the mplDeliveryCostService
	 */
	public MplDeliveryCostService getMplDeliveryCostService()
	{
		return mplDeliveryCostService;
	}

	/**
	 * @param mplDeliveryCostService
	 *           the mplDeliveryCostService to set
	 */
	public void setMplDeliveryCostService(final MplDeliveryCostService mplDeliveryCostService)
	{
		this.mplDeliveryCostService = mplDeliveryCostService;
	}

	/**
	 * @return the wishlistFacade
	 */
	public WishlistFacade getWishlistFacade()
	{
		return wishlistFacade;
	}

	/**
	 * @param wishlistFacade
	 *           the wishlistFacade to set
	 */
	public void setWishlistFacade(final WishlistFacade wishlistFacade)
	{
		this.wishlistFacade = wishlistFacade;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the mplDeliveryInformationService
	 */
	public MplDeliveryInformationService getMplDeliveryInformationService()
	{
		return mplDeliveryInformationService;
	}

	/**
	 * @param mplDeliveryInformationService
	 *           the mplDeliveryInformationService to set
	 */
	public void setMplDeliveryInformationService(final MplDeliveryInformationService mplDeliveryInformationService)
	{
		this.mplDeliveryInformationService = mplDeliveryInformationService;
	}

	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
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
	 * @return the catalogVersionService
	 */
	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	/**
	 * @param catalogVersionService
	 *           the catalogVersionService to set
	 */
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
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
	 * @return the priceDataFactory
	 */
	public PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}


}
