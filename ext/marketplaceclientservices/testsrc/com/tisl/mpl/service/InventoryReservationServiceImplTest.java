/**
 *
 */
package com.tisl.mpl.service;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderModel;

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

		final AbstractOrderModel cart = new AbstractOrderModel();

		//TISSEC-50
		//final String cartId = MarketplacecclientservicesConstants.EMPTY;//TODO : Please enter cart id
		final String pincode = MarketplacecclientservicesConstants.EMPTY;//TODO : Please enter pincode
		final String requestType = MarketplacecclientservicesConstants.EMPTY;//TODO : Please enter req type

		InventoryReservListResponse response = new InventoryReservListResponse();
		final InventoryReservListRequest reqdata = new InventoryReservListRequest();
		final List<InventoryReservRequest> reqlist = new ArrayList<InventoryReservRequest>();
		final List<InventoryReservRequest> freebieItemslist = new ArrayList<InventoryReservRequest>();
		final InventoryReservRequest reqObj = new InventoryReservRequest();
		try
		{


			//TISSEC-50
			cartObj.setUSSID("");//TODO : Please enter ussid
			cartObj.setIsAFreebie("");//TODO : Please enter freebie flag
			cartObj.setStoreId("");//TODO : Please enter store id
			cartObj.setFulfillmentType("");//TODO : Please enter fulfillment type
			cartObj.setDeliveryMode("");//TODO : Please enter delivery mode
			cartObj.setQuantity(Integer.valueOf(4));
			reqObj.setUSSID("");//TODO : Please enter ussid
			reqObj.setParentUSSID("");//TODO : Please enter parent ussid
			reqObj.setIsAFreebie("");//TODO : Please enter freebie flag
			reqObj.setStoreId("");//TODO : Please enter store id
			reqObj.setFulfillmentType("");//TODO : Please enter fulfillment type
			reqObj.setDeliveryMode("");//TODO : Please enter delivery mode
			reqObj.setQuantity("");//TODO : Please enter quantity
			freebieItemslist.add(reqObj);
			reqlist.addAll(freebieItemslist);
			reqdata.setCartId(cart.getGuid());
			reqdata.setPinCode(pincode);

			reqdata.setDuration("");//TODO : Please enter duration
			reqdata.setItem(reqlist);
			response = inventoryReservationServiceImpl.reserveInventoryAtCheckout(reqdata);
		}
		catch (final JAXBException e)
		{
			LOG.error(MarketplacecclientservicesConstants.JAXB_EXCEPTION);

		}

		//added for jewellery
		final InventoryReservListRequest req = inventoryReservationServiceImpl.convertDatatoWsdto(cartdatalist, cart.getGuid(),
				pincode, requestType);
		try
		{
			given(inventoryReservationServiceImpl.reserveInventoryAtCheckout(req)).willReturn(response);
		}
		catch (final JAXBException e)
		{
			LOG.error(MarketplacecclientservicesConstants.JAXB_EXCEPTION);
		}
		//end
		//given(inventoryReservationServiceImpl.convertDatatoWsdto(cartdatalist, cart, pincode, requestType)).willReturn(response);
	}

	@Test
	public void reserveInventoryAtCheckout() throws JAXBException
	{
		final InventoryReservListRequest request = new InventoryReservListRequest();

		//TISSEC-50
		request.setCartId("");//TODO : Please enter cart id
		request.setPinCode("");//TODO : Please enter pincode
		request.setDuration("");//TODO : Please enter duration

		final List<InventoryReservRequest> itemList = new ArrayList<InventoryReservRequest>();
		final InventoryReservRequest item = new InventoryReservRequest();


		item.setDeliveryMode("");//TODO : Please enter delivery mode
		item.setFulfillmentType("");//TODO : Please enter fulfillment type
		item.setParentUSSID("");//TODO : Please enter parent ussid
		item.setQuantity("");//TODO : Please enter quantity
		item.setUSSID("");//TODO : Please enter ussid
		item.setStoreId("");//TODO : Please enter store id
		item.setIsAFreebie("");//TODO : Please enter freebie flag
		itemList.add(item);
		request.setItem(itemList);
		final InventoryReservListResponse responsefromOMS = new InventoryReservListResponse();
		given(inventoryReservationServiceImpl.reserveInventoryAtCheckout(request)).willReturn(responsefromOMS);
	}
}
