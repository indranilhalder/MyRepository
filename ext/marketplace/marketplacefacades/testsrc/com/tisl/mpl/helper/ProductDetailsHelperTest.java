/**
 *
 */
package com.tisl.mpl.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.Constants.USER;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.core.enums.ClickAndCollectEnum;
import com.tisl.mpl.core.enums.ExpressDeliveryEnum;
import com.tisl.mpl.core.enums.HomeDeliveryEnum;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.data.WishlistData;
import com.tisl.mpl.facade.wishlist.WishlistFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryInformationService;


/**
 * @author TCS
 *
 */
public class ProductDetailsHelperTest
{
	@Resource
	private final RichAttributeModel rich = new RichAttributeModel();

	@Resource(name = "mplDeliveryCostService")
	private MplDeliveryCostService mplDeliveryCostService;

	@Resource
	private MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel;
	@Resource
	private WishlistFacade wishlistFacade;

	@Resource
	private UserService userService;

	private final ProductDetailsHelper productDetailsHelper = new ProductDetailsHelper();

	@Resource
	private ProductData productData;

	@Resource
	private MplDeliveryInformationService mplDeliveryInformationService;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

	}


	@Test
	public void testDeliveryModeLlist()
	{
		final String skuid = "123654098765485130011712";
		final List<MarketplaceDeliveryModeData> deliveryModeDataList = new ArrayList<MarketplaceDeliveryModeData>();
		assertNotNull(rich.getHomeDelivery());
		assertEquals(rich.getHomeDelivery(), HomeDeliveryEnum.YES);
		assertNotNull(mplDeliveryCostService
				.getDeliveryCost(MarketplaceFacadesConstants.HD, MarketplaceFacadesConstants.INR, skuid));
		assertNotNull(mplZoneDeliveryModeValueModel);
		assertNotNull(mplZoneDeliveryModeValueModel.getValue());
		assertNotNull(mplZoneDeliveryModeValueModel.getDeliveryMode().getCode());
		assertNotNull(mplZoneDeliveryModeValueModel.getDeliveryMode().getDescription());
		assertNotNull(mplZoneDeliveryModeValueModel.getDeliveryMode().getName());
		assertNotNull(rich.getClickAndCollect());
		assertNotNull(rich.getClickAndCollect());
		assertEquals(rich.getClickAndCollect(), ClickAndCollectEnum.YES);
		assertNotNull(mplDeliveryCostService.getDeliveryCost(MarketplaceFacadesConstants.C_C, MarketplaceFacadesConstants.INR,
				skuid));
		assertNotNull(mplZoneDeliveryModeValueModel);
		assertNotNull(mplZoneDeliveryModeValueModel.getValue());
		assertNotNull(mplZoneDeliveryModeValueModel.getDeliveryMode().getCode());
		assertNotNull(mplZoneDeliveryModeValueModel.getDeliveryMode().getDescription());
		assertNotNull(mplZoneDeliveryModeValueModel.getDeliveryMode().getName());
		assertNotNull(mplZoneDeliveryModeValueModel.getSellerArticleSKU());
		assertNotNull(rich.getExpressDelivery());
		assertEquals(rich.getExpressDelivery(), ExpressDeliveryEnum.YES);
		assertNotNull(mplDeliveryCostService.getDeliveryCost(MarketplaceFacadesConstants.EXPRESS, MarketplaceFacadesConstants.INR,
				skuid));
		assertNotNull(mplZoneDeliveryModeValueModel);
		assertNotNull(mplZoneDeliveryModeValueModel.getValue());
		assertNotNull(mplZoneDeliveryModeValueModel.getDeliveryMode().getCode());
		assertNotNull(mplZoneDeliveryModeValueModel.getDeliveryMode().getDescription());
		assertNotNull(mplZoneDeliveryModeValueModel.getDeliveryMode().getName());

		assertEquals(deliveryModeDataList, productDetailsHelper.getDeliveryModeLlist(rich, skuid));
	}

	@Test
	public void testFormPriceData()
	{
		final Double price = new Double("100.564");
		final PriceData priceData = new PriceData();
		assertNotNull(priceData.getCurrencyIso());
		assertNotNull(priceData.getCurrencyIso());
		assertNotNull(priceData.getValue());

		assertEquals(priceData, productDetailsHelper.formPriceData(price));
	}

	@Test
	public void testAddToWishListInPopup()
	{
		final String wishName = "My Wishlist 1";
		assertNotNull(wishlistFacade.getWishlistForName(wishName));
		final Wishlist2Model existingWishlist = new Wishlist2Model();
		assertNotNull(existingWishlist);

		assertNotNull(userService.getCurrentUser());
		assertNotNull(wishlistFacade.getWishlistForName(wishName));
	}

	@Test
	public void testShowWishListsInPopUp()
	{
		final List<WishlistData> wishList = new ArrayList<WishlistData>();
		final String productCode = "987654321";
		assertNotNull(userService.getCurrentUser());
		final UserModel user = new UserModel();
		assertNotNull(user.getName());
		assertEquals(user.getName(), USER.ANONYMOUS_CUSTOMER);
		assertNotNull(wishlistFacade.getAllWishlists());
		final Wishlist2Model wish = new Wishlist2Model();
		assertNotNull(wish.getName());
		assertNotNull(wish.getEntries());
		final Wishlist2EntryModel wlEntry = new Wishlist2EntryModel();
		assertNotNull(wlEntry.getProduct().getCode());
		assertEquals(wlEntry.getProduct().getCode(), productCode);
		assertNotNull(wlEntry.getUssid());
		assertEquals(wishList, productDetailsHelper.showWishListsInPopUp(productCode));
	}

	public void testGalleryImages()
	{
		final List<Map<String, ImageData>> galleryImages = new ArrayList<>();
		assertNotNull(productData.getImages());
		final List<ImageData> images = new ArrayList<>();
		assertNotNull(productData.getImages());
		final ImageData image = new ImageData();
		assertNotNull(image.getImageType());
		assertEquals(ImageDataType.GALLERY, image.getImageType());
		assertNotNull(images.get(0).getGalleryIndex());
		assertNotNull(image.getGalleryIndex());
		assertNotNull(image.getGalleryIndex());
		assertNotNull(image.getFormat());
		assertEquals(galleryImages, productDetailsHelper.getGalleryImages(productData));
	}

	public void testTDeliveryModeATMap()
	{
		final Map<String, String> deliveryModeATMap = new HashMap<String, String>();
		final List<String> deliveryInfoList = new ArrayList<String>();
		assertNotNull(mplDeliveryInformationService.getDeliveryInformation(deliveryInfoList));
		final List<DeliveryModeModel> deliveryInformationList = new ArrayList<DeliveryModeModel>();
		assertFalse(deliveryInformationList.isEmpty());
		final DeliveryModeModel deliveryMode = new DeliveryModeModel();
		assertNotNull(deliveryMode.getCode());
		assertEquals(deliveryMode.getCode(), MarketplaceFacadesConstants.HOME_DELIVERY);
		assertEquals(deliveryMode.getCode(), deliveryMode.getDescription());
		assertEquals(deliveryMode.getCode(), MarketplaceFacadesConstants.EXPRESS_DELIVERY);
		assertNotNull(deliveryMode.getCode());
		assertNotNull(deliveryMode.getDescription());

		assertEquals(deliveryModeATMap, productDetailsHelper.getDeliveryModeATMap(deliveryInfoList));
	}
}
