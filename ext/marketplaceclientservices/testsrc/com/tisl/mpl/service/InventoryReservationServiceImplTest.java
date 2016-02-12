/**
 *
 */
package com.tisl.mpl.service;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.mplcommerceservices.service.data.CartSoftReservationData;
import com.tisl.mpl.wsdto.InventoryReservListRequest;
import com.tisl.mpl.wsdto.InventoryReservListResponse;
import com.tisl.mpl.wsdto.InventoryReservRequest;


/**
 * @author TCS
 *
 */
@SuppressWarnings("deprecation")
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class InventoryReservationServiceImplTest
{
	private static final Logger LOG = Logger.getLogger(InventoryReservationServiceImpl.class);
	@Mock
	private InventoryReservationServiceImpl inventoryReservationServiceImpl = Mockito.mock(InventoryReservationServiceImpl.class);
	@Mock
	private final CartSoftReservationData cartObj = Mockito.mock(CartSoftReservationData.class);

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		this.inventoryReservationServiceImpl = Mockito.mock(InventoryReservationServiceImpl.class);
	}

	@Test
	public void convertDatatoWsdto()
	{
		final List<CartSoftReservationData> cartdatalist = new ArrayList<CartSoftReservationData>();
		final String cartId = "cart09876";
		final String pincode = "700156";
		final String requestType = "cart";
		InventoryReservListResponse response = new InventoryReservListResponse();
		final InventoryReservListRequest reqdata = new InventoryReservListRequest();
		final List<InventoryReservRequest> reqlist = new ArrayList<InventoryReservRequest>();
		final List<InventoryReservRequest> freebieItemslist = new ArrayList<InventoryReservRequest>();
		final InventoryReservRequest reqObj = new InventoryReservRequest();
		try
		{
			cartObj.setUSSID("78945612312345");
			cartObj.setIsAFreebie("Y");
			cartObj.setStoreId("STORE1234");
			cartObj.setFulfillmentType("TSHIP");
			cartObj.setDeliveryMode("HD");
			cartObj.setQuantity(Integer.valueOf(4));
			reqObj.setUSSID("789456123789456");
			reqObj.setParentUSSID("789456123789456");
			reqObj.setIsAFreebie("Y");
			reqObj.setStoreId("STORE1234");
			reqObj.setFulfillmentType("TSHIP");
			reqObj.setDeliveryMode("HD");
			reqObj.setQuantity("Integer.valueOf(4)");
			freebieItemslist.add(reqObj);
			reqlist.addAll(freebieItemslist);
			reqdata.setCartId(cartId);
			reqdata.setPinCode(pincode);
			reqdata.setDuration("oms.inventory.reservation.cart.duration");
			reqdata.setItem(reqlist);
			response = inventoryReservationServiceImpl.reserveInventoryAtCheckout(reqdata);
		}
		catch (final JAXBException e)
		{
			LOG.error(MarketplacecclientservicesConstants.JAXB_EXCEPTION);

		}

		given(inventoryReservationServiceImpl.convertDatatoWsdto(cartdatalist, cartId, pincode, requestType)).willReturn(response);
	}

	@Test
	public void reserveInventoryAtCheckout() throws JAXBException
	{
		final InventoryReservListRequest request = new InventoryReservListRequest();
		request.setCartId("cart1234");
		request.setPinCode("700156");
		request.setDuration("0");
		final List<InventoryReservRequest> itemList = new ArrayList<InventoryReservRequest>();
		final InventoryReservRequest item = new InventoryReservRequest();
		item.setDeliveryMode("HD");
		item.setFulfillmentType("TSHIP");
		item.setParentUSSID("12345678");
		item.setQuantity("5");
		item.setUSSID("s00000000001");
		item.setStoreId("store1234");
		item.setIsAFreebie("Y");
		itemList.add(item);
		request.setItem(itemList);
		final InventoryReservListResponse responsefromOMS = new InventoryReservListResponse();
		given(inventoryReservationServiceImpl.reserveInventoryAtCheckout(request)).willReturn(responsefromOMS);
	}
}
