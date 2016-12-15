/**
 *
 */
package com.tisl.mpl.facade.account.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.wishlist2.Wishlist2Service;
import de.hybris.platform.wishlist2.enums.Wishlist2EntryPriority;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.data.EditWishlistNameData;
import com.tisl.mpl.facade.wishlist.impl.DefaultWishlistFacade;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;
import com.tisl.mpl.marketplacecommerceservices.service.MplWishlistService;


/**
 * @author 594165
 *
 */

@UnitTest
public class DefaultWishlistFacadeUnitTest
{
	@Mock
	private CatalogVersionService catalogVersionService;
	@Mock
	private Wishlist2Service wishlistService;
	@Mock
	private Wishlist2Model wishlistModel;
	@Mock
	private UserService userService;
	@Mock
	private CustomerModel customerModel;
	@Mock
	private MplWishlistService mplWishlistService;
	@Mock
	private Wishlist2EntryModel wishlistEntryModel;
	@Mock
	private ProductService productService;
	@Mock
	private EditWishlistNameData editWishlistNameData;
	@Mock
	private ModelService modelService;
	@Mock
	private BuyBoxService buyBoxService;
	@Mock
	private BuyBoxModel buyBoxModel;
	@Mock
	private CartService cartService;
	@Mock
	private CartModel cartModel;
	@Mock
	private PriceData priceData;
	@Mock
	private ProductData productData;
	@Mock
	private DefaultWishlistFacade defaultWishlistFacade;
	@Mock
	private OrderFacade orderFacade;
	@Mock
	private OrderData orderData;
	@Mock
	private OrderEntryData orderEntryData;

	private ProductModel product;
	private final Wishlist2EntryPriority priority = Wishlist2EntryPriority.HIGH;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		this.modelService = Mockito.mock(ModelService.class);
		given(wishlistService.getDefaultWishlist()).willReturn(wishlistModel);
		given(userService.getCurrentUser()).willReturn(customerModel);
		this.wishlistEntryModel = Mockito.mock(Wishlist2EntryModel.class);
		this.editWishlistNameData = Mockito.mock(EditWishlistNameData.class);
		this.buyBoxModel = Mockito.mock(BuyBoxModel.class);
		this.buyBoxService = Mockito.mock(BuyBoxService.class);
		this.cartService = Mockito.mock(CartService.class);
		this.cartModel = Mockito.mock(CartModel.class);
		this.priceData = Mockito.mock(PriceData.class);
		this.productData = Mockito.mock(ProductData.class);
		this.defaultWishlistFacade = Mockito.mock(DefaultWishlistFacade.class);
		this.orderEntryData = Mockito.mock(OrderEntryData.class);
		this.orderData = Mockito.mock(OrderData.class);
		product = productService.getProductForCode("HW2300-2356");
		//		orderData = orderFacade.getOrderDetailsForCode("100000001");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetSize()
	{
		final List<Wishlist2EntryModel> list = new ArrayList<Wishlist2EntryModel>();
		Mockito.when(wishlistModel.getEntries()).thenReturn(list);
		assertEquals(0, list.size());
	}

	@Test
	public void testRemoveProductFromWl()
	{
		final String ussid = "1001161796787004";
		final String wishlistName = "My Wishlist";
		final String productCode = "987654321";
		final List<Wishlist2EntryModel> list = new ArrayList<Wishlist2EntryModel>();
		Mockito.when(wishlistModel.getEntries()).thenReturn(list);
		Mockito.doNothing().when(wishlistService).removeWishlistEntry(wishlistModel, wishlistEntryModel);
		defaultWishlistFacade.removeProductFromWl(productCode, wishlistName, ussid);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetAllWishlists()
	{
		final List<Wishlist2Model> list = new ArrayList<Wishlist2Model>();
		Mockito.when(mplWishlistService.getWishlists(customerModel)).thenReturn(list);
		final List<Wishlist2Model> actual = defaultWishlistFacade.getAllWishlists();
		Assert.assertEquals(list, actual);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetAllWishlistByUssid()
	{
		final String ussid = "1001161796787004";
		final List<Wishlist2EntryModel> list = new ArrayList<Wishlist2EntryModel>();
		Mockito.when(mplWishlistService.getWishlistByUserAndUssid(customerModel, ussid)).thenReturn(list);
		final List<Wishlist2EntryModel> actual = defaultWishlistFacade.getAllWishlistByUssid(ussid);
		Assert.assertEquals(list, actual);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testCreateNewWishlist()
	{
		final String wishlistName = "My Wishlist";
		final String description = "Wishlist Description";
		given(Boolean.valueOf(wishlistService.hasDefaultWishlist(customerModel))).willReturn(Boolean.TRUE);
		Assert.assertTrue(wishlistService.hasDefaultWishlist(customerModel));
		Mockito.when(wishlistService.createWishlist(wishlistName, description)).thenReturn(wishlistModel);
		given(Boolean.valueOf(wishlistService.hasDefaultWishlist(customerModel))).willReturn(Boolean.FALSE);
		Assert.assertFalse(wishlistService.hasDefaultWishlist(customerModel));
		Mockito.when(wishlistService.createWishlist(customerModel, wishlistName, description)).thenReturn(wishlistModel);
		defaultWishlistFacade.createNewWishlist(customerModel, wishlistName, description);
	}

	@Test
	public void testAddProductToWishlist()
	{
		final String comment = "New Wishlist";
		final String ussid = "1001161796787004";
		final boolean selectedSize = true;
		final String productCode = "987654321";
		Mockito.doNothing().when(mplWishlistService)
				.addWishlistEntry(wishlistModel, product, Integer.valueOf(1), priority, comment, ussid, selectedSize);
		defaultWishlistFacade.addProductToWishlist(wishlistModel, productCode, ussid, selectedSize);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetWishlistForName()
	{
		final String wishlistName = "My Wishlist";
		final List<Wishlist2Model> list = Arrays.asList(wishlistModel);
		Mockito.when(mplWishlistService.getWishlists(customerModel)).thenReturn(list);
		given(wishlistModel.getName()).willReturn(wishlistName);
		defaultWishlistFacade.getWishlistForName(wishlistName);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testEditWishlistName()
	{
		final String newWishlistName = "New Wishlist";
		final List<Wishlist2Model> list = Arrays.asList(wishlistModel);
		Mockito.when(mplWishlistService.getWishlists(customerModel)).thenReturn(list);
		given(editWishlistNameData.getNewWishlistName()).willReturn(newWishlistName);
		Mockito.doNothing().when(modelService).save(wishlistModel);
		defaultWishlistFacade.editWishlistName(editWishlistNameData);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetBuyBoxPrice()
	{
		final String ussid = "1001161796787004";
		Mockito.when(buyBoxService.getpriceForUssid(ussid)).thenReturn(buyBoxModel);
		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);
		defaultWishlistFacade.getBuyBoxPrice(ussid, productData);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRemoveProductFromWL()
	{
		final String wishlistName = "New Wishlist";
		final String ussid = "1001161796787004";
		final String productCode = "987654321";
		final String OrderCode = "100000001";
		final List<OrderEntryData> orderEntryData = new ArrayList<OrderEntryData>();
		Mockito.when(orderData.getEntries()).thenReturn(orderEntryData);
		final List<Wishlist2Model> list = new ArrayList<Wishlist2Model>();
		Mockito.when(mplWishlistService.getWishlists(customerModel)).thenReturn(list);
		given(wishlistEntryModel.getAddToCartFromWl()).willReturn(Boolean.TRUE);
		Mockito.when(defaultWishlistFacade.removeProductFromWl(productCode, wishlistName, ussid)).thenReturn(wishlistModel);
		defaultWishlistFacade.removeProductFromWL(OrderCode);
	}

	@Test
	public void testRemProdFromWLForConf()
	{
		final List<OrderEntryData> orderEntryData = new ArrayList<OrderEntryData>();
		Mockito.when(orderData.getEntries()).thenReturn(orderEntryData);
		final List<Wishlist2Model> list = new ArrayList<Wishlist2Model>();
		Mockito.when(mplWishlistService.getWishlists(customerModel)).thenReturn(list);
		given(wishlistEntryModel.getAddToCartFromWl()).willReturn(Boolean.TRUE);
		Mockito.doNothing().when(wishlistService).removeWishlistEntry(wishlistModel, wishlistEntryModel);
		defaultWishlistFacade.remProdFromWLForConf(orderData, customerModel);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetAllWishlistsForCustomer()
	{
		final List<Wishlist2Model> list = new ArrayList<Wishlist2Model>();
		Mockito.when(mplWishlistService.getWishlists(customerModel)).thenReturn(list);
		final List<Wishlist2Model> actual = defaultWishlistFacade.getAllWishlistsForCustomer(customerModel);
		Assert.assertEquals(list, actual);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetSingleWishlist()
	{
		final List<Wishlist2Model> list = new ArrayList<Wishlist2Model>();
		Mockito.when(mplWishlistService.getWishListAgainstUser(customerModel)).thenReturn(list);
		defaultWishlistFacade.getSingleWishlist(customerModel);
	}
}
