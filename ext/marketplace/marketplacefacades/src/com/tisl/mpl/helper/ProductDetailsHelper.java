/**
 *
 */
package com.tisl.mpl.helper;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ClassificationData;
import de.hybris.platform.commercefacades.product.data.FeatureData;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MplGlobalCodeConstants;
import com.tisl.mpl.core.enums.ClickAndCollectEnum;
import com.tisl.mpl.core.enums.ExpressDeliveryEnum;
import com.tisl.mpl.core.enums.HomeDeliveryEnum;
import com.tisl.mpl.core.model.ConfigureImagesCountComponentModel;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.data.WishlistData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.wishlist.WishlistFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
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
	/**
	 *
	 */
	private static final String CLASSIFICATION_ATTRIBUTES_ELECTRONICS_GROUPNAME = "classification.attributes.electronics.groupname";
	/**
	 *
	 */
	private static final String N_A = "n/a";

	/*
	 * private MplCheckoutFacade mplCheckoutFacade;
	 *//**
	 * @return the mplCheckoutFacade
	 */
	/*
	 * public MplCheckoutFacade getMplCheckoutFacade() { return mplCheckoutFacade; }
	 *//**
	 * @param mplCheckoutFacade
	 *           the mplCheckoutFacade to set
	 */
	/*
	 * public void setMplCheckoutFacade(final MplCheckoutFacade mplCheckoutFacade) { this.mplCheckoutFacade =
	 * mplCheckoutFacade; }
	 */

	@Autowired
	private MplDeliveryCostService deliveryCostService;


	@Resource(name = "GigyaService")
	private GigyaService gigyaservice;


	@Autowired
	private ExtendedUserServiceImpl userexService;

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


	/**
	 *
	 */

	private static final String MPL_CONTENT_CATALOG = "mplContentCatalog";
	private static final String MEDIA_PRIORITY = "mediaPriority";
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

	/*
	 * @Resource(name = "GigyaService") private GigyaService gigyaservice;
	 * 
	 * 
	 * @Autowired private ExtendedUserServiceImpl userexService;
	 *//**
	 * @return the gigyaservice
	 */

	/*
	 * public GigyaService getGigyaservice() { return gigyaservice; }
	 *//**
	 * @param gigyaservice
	 *           the gigyaservice to set
	 */
	/*
	 * public void setGigyaservice(final GigyaService gigyaservice) { this.gigyaservice = gigyaservice; }
	 */

	public MplDeliveryCostService getMplDeliveryCostService()
	{
		return mplDeliveryCostService;
	}


	private PriceDataFactory priceDataFactory;

	protected PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}

	@Required
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}

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
		if (null != rich.getHomeDelivery() && rich.getHomeDelivery().equals(HomeDeliveryEnum.YES))
		{
			final MarketplaceDeliveryModeData deliveryModeData = new MarketplaceDeliveryModeData();
			final MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = getMplDeliveryCostService().getDeliveryCost(
					MarketplaceFacadesConstants.HD, MarketplaceFacadesConstants.INR, skuid);
			//Populating Delivery Modes for each USSID
			if (mplZoneDeliveryModeValueModel != null)
			{
				final PriceData priceData = formPriceData(mplZoneDeliveryModeValueModel.getValue());
				deliveryModeData.setCode(mplZoneDeliveryModeValueModel.getDeliveryMode().getCode());
				deliveryModeData.setDescription(mplZoneDeliveryModeValueModel.getDeliveryMode().getDescription());
				deliveryModeData.setName(mplZoneDeliveryModeValueModel.getDeliveryMode().getName());
				deliveryModeData.setSellerArticleSKU(skuid);
				if (rich.getDeliveryFulfillModes().getCode().equalsIgnoreCase("tship"))
				{
					final PriceData priceForTshipItem = formPriceData(Double.valueOf(0.0));
					deliveryModeData.setDeliveryCost(priceForTshipItem);
				}
				else
				{
					deliveryModeData.setDeliveryCost(priceData);
				}
				deliveryModeDataList.add(deliveryModeData);
			}

		}
		if (null != rich.getClickAndCollect() && rich.getClickAndCollect().equals(ClickAndCollectEnum.YES))
		{
			final MarketplaceDeliveryModeData deliveryModeData = new MarketplaceDeliveryModeData();
			final MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = getMplDeliveryCostService().getDeliveryCost(
					MarketplaceFacadesConstants.C_C, MarketplaceFacadesConstants.INR, skuid);
			if (mplZoneDeliveryModeValueModel != null)
			{
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
				deliveryModeDataList.add(deliveryModeData);
			}
		}
		if (null != rich.getExpressDelivery() && rich.getExpressDelivery().equals(ExpressDeliveryEnum.YES))
		{
			final MarketplaceDeliveryModeData deliveryModeData = new MarketplaceDeliveryModeData();
			final MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = getMplDeliveryCostService().getDeliveryCost(
					MarketplaceFacadesConstants.EXPRESS, MarketplaceFacadesConstants.INR, skuid);
			if (mplZoneDeliveryModeValueModel != null)
			{
				final PriceData priceData = formPriceData(mplZoneDeliveryModeValueModel.getValue());
				deliveryModeData.setCode(mplZoneDeliveryModeValueModel.getDeliveryMode().getCode());
				deliveryModeData.setDescription(mplZoneDeliveryModeValueModel.getDeliveryMode().getDescription());
				deliveryModeData.setName(mplZoneDeliveryModeValueModel.getDeliveryMode().getName());
				deliveryModeData.setSellerArticleSKU(skuid);
				if (rich.getDeliveryFulfillModes().getCode().equalsIgnoreCase("tship"))
				{
					final PriceData priceForTshipItem = formPriceData(Double.valueOf(0.0));
					deliveryModeData.setDeliveryCost(priceForTshipItem);
				}
				else
				{
					deliveryModeData.setDeliveryCost(priceData);
				}
				deliveryModeDataList.add(deliveryModeData);
			}
		}
		return deliveryModeDataList;
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
						&& configurationService.getConfiguration().getString(CLASSIFICATION_ATTRIBUTES_ELECTRONICS_GROUPNAME)
								.contains(classData.getName()))
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
								&& configurationService.getConfiguration().getString(CLASSIFICATION_ATTRIBUTES_ELECTRONICS_GROUPNAME)
										.contains(classData.getName()))
						{
							classicationDataList.add(classData);
						}
					}
				}
			}
		}
		productData.setClassifications(classicationDataList);
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
			Wishlist2Model existingWishlist = wishlistFacade.getWishlistForName(wishName);
			//  boolean add=
			//checking whether the wishlist with given name exists or not
			if (null != existingWishlist)
			{
				add = wishlistFacade.addProductToWishlist(existingWishlist, productCode, ussid, sizeSelected.booleanValue());
			}
			else
			{
				final UserModel user = userService.getCurrentUser();
				final Wishlist2Model createdWishlist = wishlistFacade.createNewWishlist(user, wishName, productCode);
				add = wishlistFacade.addProductToWishlist(createdWishlist, productCode, ussid, sizeSelected.booleanValue());
				final WishlistData wishData = new WishlistData();
				wishData.setParticularWishlistName(createdWishlist.getName());
				existingWishlist = wishlistFacade.getWishlistForName(wishName);
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
								&& wlEntry.getProduct().getCode().equals(productCode))
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
							galleryImageData.setStaticImage("store/_ui/responsive/common/images/video-play.png");
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


}
