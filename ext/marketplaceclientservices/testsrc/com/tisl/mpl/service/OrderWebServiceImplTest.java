///**
// *
// */
//package com.tisl.mpl.service;
//
//import static org.junit.Assert.assertNotNull;
//
//import de.hybris.platform.commerceservices.service.data.OrderDataForXML;
//import de.hybris.platform.commerceservices.service.data.OrderDataSubOrderForXML;
//import de.hybris.platform.commerceservices.service.data.OrderDataTransactionForXML;
//import de.hybris.platform.servicelayer.config.ConfigurationService;
//
//import java.io.StringWriter;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.annotation.Resource;
//import javax.ws.rs.core.MediaType;
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.Marshaller;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//
//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.ClientResponse;
//import com.sun.jersey.api.client.WebResource;
//import com.sun.jersey.api.uri.UriBuilderImpl;
//import com.tisl.mpl.wsdto.CustomerAddressWsDTO;
//import com.tisl.mpl.wsdto.OrderTrialWSDTO;
//import com.tisl.mpl.wsdto.PaymentDetWsDTO;
//import com.tisl.mpl.wsdto.ProductDetailsWsDTO;
//import com.tisl.mpl.wsdto.SubOrderWsDTO;
//import com.tisl.mpl.wsdto.TransactionWsDTO;
//
//
//public class OrderWebServiceImplTest
//{
//
//	private OrderDataForXML orderDataForXML;
//	private OrderTrialWSDTO order;
//	private OrderDataSubOrderForXML orderDataSubOrderForXML;
//	private OrderDataTransactionForXML orderDataTransactionForXML;
//	@Resource(name = "configurationService")
//	private ConfigurationService configurationService;
//	private OrderTrialWSDTO orderTrialWSDTO;
//
//	@Before
//	public void setUp()
//	{
//		MockitoAnnotations.initMocks(this);
//		this.order = Mockito.mock(OrderTrialWSDTO.class);
//		this.orderDataForXML = Mockito.mock(OrderDataForXML.class);
//		this.orderDataSubOrderForXML = Mockito.mock(OrderDataSubOrderForXML.class);
//		this.orderDataTransactionForXML = Mockito.mock(OrderDataTransactionForXML.class);
//		this.configurationService = Mockito.mock(ConfigurationService.class);
//		this.orderTrialWSDTO = Mockito.mock(OrderTrialWSDTO.class);
//
//
//
//	}
//
//	@Test
//	public void testOrderModeltoWsDTO()
//	{
//		//assertNotNull(orderDataForXML);
//		assertNotNull(orderDataForXML.getOrderRefNo());
//		order.setOrderRefNo(orderDataForXML.getOrderRefNo());
//		assertNotNull(orderDataForXML.getOrderType());
//		order.setOrderType(orderDataForXML.getOrderType());
//		assertNotNull(orderDataForXML.getSubmissionDateTime());
//		order.setSubmissionDateTime(orderDataForXML.getSubmissionDateTime());
//		assertNotNull(orderDataForXML.getOrderDate());
//		order.setOrderDate(orderDataForXML.getOrderDate());
//		assertNotNull(orderDataForXML.getChannel());
//		order.setChannel(orderDataForXML.getChannel());
//
//		//final List<OrderDataSubOrderForXML> l = orderDataForXML.getSubOrder();
//		final List<SubOrderWsDTO> sublist = new ArrayList<SubOrderWsDTO>();
//
//		final SubOrderWsDTO suborderobj = new SubOrderWsDTO();
//		suborderobj.setOrderNo(orderDataSubOrderForXML.getOrderNo());
//		//final List<OrderDataTransactionForXML> t = orderDataSubOrderForXML.getTransaction();
//		final List<TransactionWsDTO> transactionobjList = new ArrayList<TransactionWsDTO>();
//
//
//		final TransactionWsDTO transactionobj = new TransactionWsDTO();
//
//		assertNotNull(orderDataTransactionForXML.getTransactionId());
//		transactionobj.setTransactionId(orderDataTransactionForXML.getTransactionId());
//
//		assertNotNull(orderDataTransactionForXML.getSellerID());
//		transactionobj.setSellerId(orderDataTransactionForXML.getSellerID());
//
//		assertNotNull(orderDataTransactionForXML.getListingID());
//		transactionobj.setListingId(orderDataTransactionForXML.getListingID());
//
//		assertNotNull(orderDataTransactionForXML.getUSSID());
//		transactionobj.setUssid(orderDataTransactionForXML.getUSSID());
//
//		assertNotNull(orderDataTransactionForXML.getPrice().toString());
//		transactionobj.setPrice(Double.parseDouble(orderDataTransactionForXML.getPrice().toString()));
//
//		assertNotNull(orderDataTransactionForXML.getApportionedCODPrice().toString());
//		transactionobj.setApportionedCodPrice(Double.parseDouble(orderDataTransactionForXML.getApportionedCODPrice().toString()));
//
//		assertNotNull(orderDataTransactionForXML.getPromotionCode());
//		transactionobj.setPromotionCode(orderDataTransactionForXML.getPromotionCode());
//
//
//		assertNotNull(orderDataTransactionForXML.getIsaGift().toString());
//		transactionobj.setIsaGift(Boolean.getBoolean(orderDataTransactionForXML.getIsaGift().toString()));
//
//
//		assertNotNull(orderDataTransactionForXML.getDeliveryMode());
//		transactionobj.setDeliveryMode(orderDataTransactionForXML.getDeliveryMode());
//
//		transactionobj.setShippingCharge(100.00);
//
//		assertNotNull(orderDataTransactionForXML.getSlaveID());
//		transactionobj.setSlaveId(orderDataTransactionForXML.getSlaveID());
//
//		transactionobj.setCod(false);
//		transactionobj.setReturnable(false);
//
//		assertNotNull(orderDataTransactionForXML.getProductSize());
//		transactionobj.setProductSize(orderDataTransactionForXML.getProductSize());
//
//		assertNotNull(orderDataTransactionForXML.getTransportMode());
//		transactionobj.setTransportMode(orderDataTransactionForXML.getTransportMode());
//
//		assertNotNull(orderDataTransactionForXML.getFulfillmentType());
//		transactionobj.setFulfilmentType(orderDataTransactionForXML.getFulfillmentType());
//
//
//		final CustomerAddressWsDTO custAddrObj = new CustomerAddressWsDTO();
//		assertNotNull(orderDataTransactionForXML.getCustomerAddress().get("CustomerID"));
//		custAddrObj.setCustomerId(orderDataTransactionForXML.getCustomerAddress().get("CustomerID"));
//
//
//		assertNotNull(orderDataTransactionForXML.getCustomerAddress().get("EmailID"));
//		custAddrObj.setEmailId(orderDataTransactionForXML.getCustomerAddress().get("EmailID"));
//
//		assertNotNull(orderDataTransactionForXML.getCustomerAddress().get("FirstName"));
//		custAddrObj.setFirstName(orderDataTransactionForXML.getCustomerAddress().get("FirstName"));
//
//		assertNotNull(orderDataTransactionForXML.getCustomerAddress().get("LastName"));
//		custAddrObj.setLastName(orderDataTransactionForXML.getCustomerAddress().get("LastName"));
//
//		assertNotNull(orderDataTransactionForXML.getCustomerAddress().get("PhoneNumber"));
//		custAddrObj.setPhoneNo(orderDataTransactionForXML.getCustomerAddress().get("PhoneNumber"));
//
//		assertNotNull(orderDataTransactionForXML.getCustomerAddress().get("Address1"));
//		custAddrObj.setAddress1(orderDataTransactionForXML.getCustomerAddress().get("Address1"));
//
//		assertNotNull(orderDataTransactionForXML.getCustomerAddress().get("Address2"));
//		custAddrObj.setAddress2(orderDataTransactionForXML.getCustomerAddress().get("Address2"));
//
//		assertNotNull(orderDataTransactionForXML.getCustomerAddress().get("Address3"));
//		custAddrObj.setAddress3(orderDataTransactionForXML.getCustomerAddress().get("Address3"));
//
//		assertNotNull(orderDataTransactionForXML.getCustomerAddress().get("Country"));
//		custAddrObj.setCountry(orderDataTransactionForXML.getCustomerAddress().get("Country"));
//
//		assertNotNull(orderDataTransactionForXML.getCustomerAddress().get("City"));
//		custAddrObj.setCity(orderDataTransactionForXML.getCustomerAddress().get("City"));
//
//		assertNotNull(orderDataTransactionForXML.getCustomerAddress().get("State"));
//		custAddrObj.setState(orderDataTransactionForXML.getCustomerAddress().get("State"));
//
//		assertNotNull(orderDataTransactionForXML.getCustomerAddress().get("Pincode"));
//		custAddrObj.setPincode(orderDataTransactionForXML.getCustomerAddress().get("Pincode"));
//
//		assertNotNull(orderDataTransactionForXML.getCustomerAddress().get("defaultFlag"));
//		custAddrObj.setDefaultFlag(orderDataTransactionForXML.getCustomerAddress().get("defaultFlag"));
//
//		assertNotNull(orderDataTransactionForXML.getCustomerAddress().get("AddressTag"));
//		custAddrObj.setAddresstag(orderDataTransactionForXML.getCustomerAddress().get("AddressTag"));
//
//		final ProductDetailsWsDTO prodDet = new ProductDetailsWsDTO();
//
//		assertNotNull(orderDataTransactionForXML.getProductDetails().get("cancellationAllowed"));
//		prodDet.setCancellationAllowed(Integer.parseInt(orderDataTransactionForXML.getProductDetails().get("cancellationAllowed")));
//
//		assertNotNull(orderDataTransactionForXML.getProductDetails().get("returnsAllowed"));
//		prodDet.setReturnsAllowed(Integer.parseInt(orderDataTransactionForXML.getProductDetails().get("returnsAllowed")));
//
//		assertNotNull(orderDataTransactionForXML.getProductDetails().get("replacementAllowed"));
//		prodDet.setReplacementAllowed(Integer.parseInt(orderDataTransactionForXML.getProductDetails().get("replacementAllowed")));
//
//
//		assertNotNull(orderDataTransactionForXML.getProductDetails().get("exchangeAllowed"));
//		prodDet.setExchangeAllowed(Integer.parseInt(orderDataTransactionForXML.getProductDetails().get("exchangeAllowed")));
//
//		assertNotNull(orderDataTransactionForXML.getProductDetails().get("productName"));
//		prodDet.setProductName(orderDataTransactionForXML.getProductDetails().get("productName"));
//
//		transactionobj.setCad(custAddrObj);
//		transactionobj.setPd(prodDet);
//		transactionobjList.add(transactionobj);
//
//		suborderobj.setTransaction(transactionobjList);
//		sublist.add(suborderobj);
//
//		order.setSuborder(sublist);
//
//		final CustomerAddressWsDTO custAddrObj1 = new CustomerAddressWsDTO();
//
//		assertNotNull(orderDataForXML.getCustomerAddress().get("CustomerID"));
//		custAddrObj1.setCustomerId(orderDataForXML.getCustomerAddress().get("CustomerID"));
//
//		assertNotNull(orderDataForXML.getCustomerAddress().get("EmailID"));
//		custAddrObj1.setEmailId(orderDataForXML.getCustomerAddress().get("EmailID"));
//
//		assertNotNull(orderDataForXML.getCustomerAddress().get("FirstName"));
//		custAddrObj1.setFirstName(orderDataForXML.getCustomerAddress().get("FirstName"));
//
//		assertNotNull(orderDataForXML.getCustomerAddress().get("LastName"));
//		custAddrObj1.setLastName(orderDataForXML.getCustomerAddress().get("LastName"));
//
//		assertNotNull(orderDataForXML.getCustomerAddress().get("PhoneNumber"));
//		custAddrObj1.setPhoneNo(orderDataForXML.getCustomerAddress().get("PhoneNumber"));
//
//		assertNotNull(orderDataForXML.getCustomerAddress().get("Address1"));
//		custAddrObj1.setAddress1(orderDataForXML.getCustomerAddress().get("Address1"));
//
//		assertNotNull(orderDataForXML.getCustomerAddress().get("Address2"));
//		custAddrObj1.setAddress2(orderDataForXML.getCustomerAddress().get("Address2"));
//
//		assertNotNull(orderDataForXML.getCustomerAddress().get("Address3"));
//		custAddrObj1.setAddress3(orderDataForXML.getCustomerAddress().get("Address3"));
//
//		assertNotNull(orderDataForXML.getCustomerAddress().get("Country"));
//		custAddrObj1.setCountry(orderDataForXML.getCustomerAddress().get("Country"));
//
//		assertNotNull(orderDataForXML.getCustomerAddress().get("City"));
//		custAddrObj1.setCity(orderDataForXML.getCustomerAddress().get("City"));
//
//		assertNotNull(orderDataForXML.getCustomerAddress().get("State"));
//		custAddrObj1.setState(orderDataForXML.getCustomerAddress().get("State"));
//
//		assertNotNull(orderDataForXML.getCustomerAddress().get("Pincode"));
//		custAddrObj1.setPincode(orderDataForXML.getCustomerAddress().get("Pincode"));
//
//		assertNotNull(orderDataForXML.getCustomerAddress().get("defaultFlag"));
//		custAddrObj1.setDefaultFlag(orderDataForXML.getCustomerAddress().get("defaultFlag"));
//
//		assertNotNull(orderDataForXML.getCustomerAddress().get("AddressTag"));
//		custAddrObj1.setAddresstag(orderDataForXML.getCustomerAddress().get("AddressTag"));
//
//		order.setCa(custAddrObj1);
//
//		final PaymentDetWsDTO paymentObj = new PaymentDetWsDTO();
//
//		assertNotNull(orderDataForXML.getPaymentDetails().get("paymentCost"));
//		paymentObj.setPaymentCost(Double.parseDouble(orderDataForXML.getPaymentDetails().get("paymentCost")));
//
//		assertNotNull(orderDataForXML.getPaymentDetails().get("paymentMode"));
//		paymentObj.setPaymentMode(orderDataForXML.getPaymentDetails().get("paymentMode"));
//
//
//		assertNotNull(orderDataForXML.getPaymentDetails().get("paymentStatus"));
//		paymentObj.setPaymentStatus(orderDataForXML.getPaymentDetails().get("paymentStatus"));
//
//		assertNotNull(orderDataForXML.getPaymentDetails().get("paymentInfo"));
//		paymentObj.setPaymentInfo(orderDataForXML.getPaymentDetails().get("paymentInfo"));
//
//		order.setPd(paymentObj);
//		orderCreate();
//	}
//
//	@Test
//	public void orderCreate()
//	{
//
//		final Client client = Client.create();
//		final WebResource webResource = client.resource(UriBuilderImpl.fromUri(
//				configurationService.getConfiguration().getString("ordercreate_url")).build());
//		try
//		{
//			final JAXBContext context = JAXBContext.newInstance(OrderTrialWSDTO.class);
//			final Marshaller m = context.createMarshaller(); //for pretty-print XML in JAXB
//			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//
//			final StringWriter sw = new StringWriter();
//			m.marshal(orderTrialWSDTO, sw);
//			final String xmlString = sw.toString();
//
//			final ClientResponse response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml").entity(xmlString)
//					.post(ClientResponse.class);
//
//		}
//
//		catch (final Exception ex)
//		{
//
//			ex.printStackTrace();
//
//		}
//
//
//
//	}
//
//
//
//
//
//
//
//
//
//
//}
