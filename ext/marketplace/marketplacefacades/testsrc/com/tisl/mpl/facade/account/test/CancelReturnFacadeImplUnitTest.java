/**
 *
 */
package com.tisl.mpl.facade.account.test;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.data.SendTicketLineItemData;
import com.tisl.mpl.data.SendTicketRequestData;
import com.tisl.mpl.service.MplOrderCancelClientService;
import com.tisl.mpl.xml.pojo.MplCancelOrderRequest;
import com.tisl.mpl.xml.pojo.MplOrderIsCancellableResponse;


/**
 * @author TCS
 *
 */
@UnitTest
public class CancelReturnFacadeImplUnitTest
{
	//@Autowired
	//private MplOrderService mplOrderService;
	@Autowired
	private MplOrderCancelClientService mplOrderCancelClientService;
	//@Autowired
	//private TicketCreationCRMservice ticketCreate;
	//@Autowired
	//private MplJusPayRefundService mplJusPayRefundService;
	@Autowired
	private BaseStoreService baseStoreService;
	@Autowired
	private UserService userService;
	@Autowired
	private CustomerAccountService customerAccountService;
	//@Autowired
	//private ReturnLogisticsService returnLogistics;
	//@Autowired
	//private ProductService productService;
	@Autowired
	private FlexibleSearchService flexibleSearchService;
	@Autowired
	private ModelService modelService;
	//@Autowired
	//private MplSendSMSService sendSMSService;

	//private CancelReturnFacadeImpl cancelReturnFacadeImpl;
	protected static final Logger LOG = Logger.getLogger(CancelReturnFacadeImplUnitTest.class);

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		//this.cancelReturnFacadeImpl = new CancelReturnFacadeImpl();
		//this.mplOrderService = Mockito.mock(MplOrderService.class);
		this.modelService = Mockito.mock(ModelService.class);
		this.mplOrderCancelClientService = Mockito.mock(MplOrderCancelClientService.class);
		//this.ticketCreate = Mockito.mock(TicketCreationCRMservice.class);
		//this.mplJusPayRefundService = Mockito.mock(MplJusPayRefundService.class);
		this.baseStoreService = Mockito.mock(BaseStoreService.class);
		this.userService = Mockito.mock(UserService.class);
		this.customerAccountService = Mockito.mock(CustomerAccountService.class);
		//this.returnLogistics = Mockito.mock(ReturnLogisticsService.class);
		//this.productService = Mockito.mock(ProductService.class);
		this.flexibleSearchService = Mockito.mock(FlexibleSearchService.class);
		//this.sendSMSService = Mockito.mock(MplSendSMSService.class);
	}


	@Test
	public void testImplementCancelRefund()
	{
		boolean cancellable = true;
		boolean consignmentPresent = true;
		final List<OrderEntryData> allEntries = new ArrayList<OrderEntryData>();
		final OrderData subOrderDetails = new OrderData();
		final OrderModel subOrderModel = customerAccountService.getOrderForCode((CustomerModel) userService.getCurrentUser(),
				subOrderDetails.getCode(), baseStoreService.getCurrentBaseStore());
		final OrderEntryData subOrderEntry = new OrderEntryData();
		if (subOrderEntry.getConsignment() == null)
		{
			consignmentPresent = false;
		}

		//final List<CancellationReasonModel> cancellationReasonList = mplOrderService.getCancellationReason();
		final MplCancelOrderRequest orderLineRequest = new MplCancelOrderRequest();

		final List<MplCancelOrderRequest.OrderLine> orderLineList = orderLineRequest.getOrderLine();
		final MplCancelOrderRequest.OrderLine orderLineData = new MplCancelOrderRequest.OrderLine();

		orderLineData.setOrderId(subOrderModel.getParentReference().getCode());
		orderLineData.setReasonCode("R");
		orderLineData.setRequestID("123456789" + "" + System.currentTimeMillis());//TODO: Change with a valid request ID
		orderLineData.setReturnCancelFlag("C");

		orderLineData.setReturnCancelRemarks("RETURN");
		orderLineData.setTransactionId("321-321-879");
		orderLineList.add(orderLineData);

		if (!orderLineList.isEmpty() && consignmentPresent)
		{
			List<MplOrderIsCancellableResponse.OrderLine> responseList = new ArrayList<MplOrderIsCancellableResponse.OrderLine>();
			final MplOrderIsCancellableResponse response = mplOrderCancelClientService.orderCancelDataToOMS(orderLineRequest);
			if (null != response)
			{
				responseList = response.getOrderLine();
			}
			//TODO: For Testing..remove later
			else
			{
				final MplOrderIsCancellableResponse.OrderLine orderResponse = new MplOrderIsCancellableResponse.OrderLine();
				orderResponse.setOrderId(subOrderModel.getParentReference().getCode());
				orderResponse.setIsCancellable("N");
				orderResponse.setRemarks("TEST REMARKS");
				orderResponse.setTransactionId("11111");
				responseList.add(orderResponse);
			}
			for (final MplOrderIsCancellableResponse.OrderLine orderLine : responseList)
			{
				if (orderLine.isIsCancellable().equalsIgnoreCase("N"))
				{
					cancellable = false;
					break;
				}
			}
		}

		if (cancellable)
		{
			//Method for CRM Ticket Create
			final List<SendTicketLineItemData> lineItemDataList = new ArrayList<SendTicketLineItemData>();
			final SendTicketRequestData sendTicketRequestData = new SendTicketRequestData();
			final List<OrderEntryData> assosiatedEntryList = new ArrayList<OrderEntryData>();
			for (final OrderEntryData eachEntry : allEntries)
			{
				if (!eachEntry.getTransactionId().equalsIgnoreCase(subOrderEntry.getTransactionId()))
				{
					assosiatedEntryList.add(eachEntry);
				}
			}
			if (subOrderEntry.getMplDeliveryMode().getSellerArticleSKU().equalsIgnoreCase("123456789"))
			{
				final SendTicketLineItemData sendTicketLineItemData = new SendTicketLineItemData();
				sendTicketLineItemData.setLineItemId(subOrderEntry.getOrderLineId());
				sendTicketLineItemData.setCancelReasonCode("R");

				lineItemDataList.add(sendTicketLineItemData);
			}


			final CustomerData customerData = new CustomerData();
			sendTicketRequestData.setCustomerID(customerData.getUid());
			sendTicketRequestData.setLineItemDataList(lineItemDataList);
			sendTicketRequestData.setOrderId(subOrderModel.getParentReference().getCode());
			sendTicketRequestData.setSubOrderId(subOrderDetails.getCode());
			sendTicketRequestData.setTicketType("C");

			if (consignmentPresent)
			{
				for (final SendTicketLineItemData data : lineItemDataList)
				{
					final ConsignmentModel consignment = modelService.create(ConsignmentModel.class);
					consignment.setCode(data.getLineItemId());
					Mockito.when(flexibleSearchService.getModelByExample(consignment)).thenReturn(consignment);

					if (sendTicketRequestData.getTicketType().equalsIgnoreCase("C"))
					{
						consignment.setStatus(ConsignmentStatus.ORDER_CANCELLED);
					}
					else
					{
						consignment.setStatus(ConsignmentStatus.RETURN_INITIATED);
					}
					Mockito.doNothing().when(modelService).save(consignment);
				}
			}
			else
			{
				for (final AbstractOrderEntryModel orderEntryData : subOrderModel.getEntries())
				{
					if (subOrderEntry.getEntryNumber().equals(orderEntryData.getEntryNumber()))
					{
						orderEntryData.setQuantity(new Long(0));
						Mockito.doNothing().when(modelService).save(orderEntryData);
					}
				}
			}
		}
	}


}
