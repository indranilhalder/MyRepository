/**
 *
 */
package com.tisl.mpl.facade.checkout.impl;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.CartService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;



/**
 * @author TCS
 *
 */
@SuppressWarnings("deprecation")
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class MplCartFacadeImplTest
{

	@Mock
	private MplCartFacadeImpl mplCartFacadeImpl;

	//@Mock
	//private CategoryModel categoryModel;
	@Mock
	private CartService cartServiceMock;

	@Before
	public void setUp()
	{

		this.mplCartFacadeImpl = new MplCartFacadeImpl();
		//this.mplCommerceCartService = new MplCommerceCartServiceImpl();
		//mplCartFacadeTest.setMplCommerceCartService(mplCommerceCartService);
	}

	/**
	 * JUnit Testing for mplCartFacadeTest
	 *
	 * @throws CommerceCartModificationException
	 */

	@Test
	public void testGetSessionCartWithEntryOrdering()
	{
		final CartData cartDataMock = Mockito.mock(CartData.class);
		final boolean recentlyAddedFirstTest = true;

		final List<OrderEntryData> listEntriesMock = new ArrayList<OrderEntryData>();

		given(cartDataMock.getEntries()).willReturn(listEntriesMock);
		final List<OrderEntryData> recentlyAddedListEntriesMock = new ArrayList<OrderEntryData>();
		//recentlyAddedListEntriesMock.add(listEntriesMock.get(index - 1));
		cartDataMock.setEntries(Collections.unmodifiableList(recentlyAddedListEntriesMock));
		mplCartFacadeImpl.getSessionCartWithEntryOrdering(recentlyAddedFirstTest);
	}

	@Test
	public void testGetSessionCart()
	{
		//TISSEC-50
		final CartData cartDataMock = Mockito.mock(CartData.class);
		final CartModel cartModelMock = Mockito.mock(CartModel.class);
		given(cartServiceMock.getSessionCart()).willReturn(cartModelMock);
		//given(mplCartFacadeImpl.getSessionCart()).willReturn(cartModelMock);
		final List<AbstractOrderEntryModel> cartEntryModelMock = new ArrayList<AbstractOrderEntryModel>();
		given(cartModelMock.getEntries()).willReturn(cartEntryModelMock);
		final Collection<OrderEntryData> orderEntryDatasMock = new ArrayList<OrderEntryData>();
		given(cartDataMock.getEntries()).willReturn((List<OrderEntryData>) orderEntryDatasMock);
		final String ussid = MarketplacecommerceservicesConstants.EMPTY;//TODO : Please enter ussid
		final AbstractOrderEntryModel abstractOrderEntryModelMock = Mockito.mock(AbstractOrderEntryModel.class);
		given(abstractOrderEntryModelMock.getSelectedUSSID()).willReturn(ussid);
		final SellerInformationData sellerInformationDataMock = Mockito.mock(SellerInformationData.class);
		sellerInformationDataMock.setSellerID("");//TODO : Please enter seller id
		sellerInformationDataMock.setUssid("");//TODO : Please enter ussid
		sellerInformationDataMock.setSellername("");//TODO : Please enter seller name
		final OrderEntryData orderEntryDataMock = Mockito.mock(OrderEntryData.class);
		orderEntryDataMock.setSelectedSellerInformation(sellerInformationDataMock);
		final ProductData productDataMock = Mockito.mock(ProductData.class);
		given(orderEntryDataMock.getProduct()).willReturn(productDataMock);
		//given(productDataMock.getSeller()).willReturn(sellerInformationDataMock);
		orderEntryDataMock.setSelectedSellerInformation(sellerInformationDataMock);
	}

	@Test
	public void testAddToCart() throws CommerceCartModificationException
	{
		//final CartData cartdata = Mockito.mock(CartData.class);
		final ProductModel product = Mockito.mock(ProductModel.class);
		final CartModel cartModelMock = Mockito.mock(CartModel.class);
		final CommerceCartParameter parameterMock = new CommerceCartParameter();
		final long quantity = 1;
		parameterMock.setCart(cartModelMock);
		parameterMock.setQuantity(quantity);
		final String ussid = MarketplacecommerceservicesConstants.EMPTY;//TODO : Please enter ussid
		parameterMock.setUssid(ussid);
		final String code = MarketplacecommerceservicesConstants.EMPTY;//TODO : Please enter code
		parameterMock.setProduct(product);

		mplCartFacadeImpl.addToCart(code, quantity, ussid);

	}


	@Test
	public void testGetSellerInfo() throws CMSItemNotFoundException
	{
		final CartData cartDataMock = Mockito.mock(CartData.class);
		final String ussid = MarketplacecommerceservicesConstants.EMPTY;//TODO : Please enter ussid

		mplCartFacadeImpl.getSellerInfo(cartDataMock, ussid);
	}

	@Test
	public void testGetAddress() throws CMSItemNotFoundException
	{
		final List<AddressData> addressDataMock = new ArrayList<AddressData>();
		mplCartFacadeImpl.getAddress(addressDataMock);
	}

	@Test
	public void testGetFullfillmentMode() throws Exception
	{
		final CartData cartDataMock = Mockito.mock(CartData.class);
		mplCartFacadeImpl.getFullfillmentMode(cartDataMock);
	}

	@Test
	public void testGetDeliveryMode() throws Exception
	{
		final CartData cartDataMock = Mockito.mock(CartData.class);
		final List<PinCodeResponseData> omsDeliveryResponseMock = new ArrayList<PinCodeResponseData>();
		mplCartFacadeImpl.getDeliveryMode(cartDataMock, omsDeliveryResponseMock);
	}

	@Test
	public void isInventoryReservedMobile()
	{

		final String requestType = "payment";
		final AbstractOrderModel abstractOrderModel = Mockito.mock(CartModel.class);
		final String defaultPinCodeId = "500030";

		Assert.assertEquals(true, mplCartFacadeImpl.isInventoryReservedMobile(requestType, abstractOrderModel, defaultPinCodeId));
	}

}
