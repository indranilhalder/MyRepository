/*package com.tisl.mpl.service;


import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.tisl.mpl.facades.data.CustomerDataForOrderXML;
import com.tisl.mpl.facades.data.OrderDataForXML;
import com.tisl.mpl.facades.data.OrderDataSubOrderForXML;
import com.tisl.mpl.facades.data.OrderDataTransactionForXML;
import com.tisl.mpl.facades.data.PaymentDetails;
import com.tisl.mpl.facades.data.ProductDataForOrderXML;
import com.tisl.mpl.wsdto.CustomerAddressWsDTO;
import com.tisl.mpl.wsdto.OrderTrialWSDTO;
import com.tisl.mpl.wsdto.PaymentDetWsDTO;
import com.tisl.mpl.wsdto.ProductDetailsWsDTO;
import com.tisl.mpl.wsdto.SubOrderWsDTO;
import com.tisl.mpl.wsdto.TransactionWsDTO;



 *
 *
 

public class OrderWebServiceImpl implements OrderWebService
{
	private static final Logger LOG = Logger.getLogger(OrderWebServiceImpl.class);

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;


	
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.service.OrderWebService#orderModeltoWsDTO(de.hybris.platform.core.model.order.AbstractOrderModel)
	 
	@Override
	public void orderModeltoWsDTO(final OrderDataForXML orderDataForXML)
	{
		final OrderTrialWSDTO order = new OrderTrialWSDTO();
		if (orderDataForXML != null)
		{
			if (orderDataForXML.getOrderRefNo() != null)
			{
				order.setOrderRefNo(orderDataForXML.getOrderRefNo());
			}
			if (orderDataForXML.getOrderType() != null)
			{
				order.setOrderType(orderDataForXML.getOrderType());
			}
			
			 * if (orderDataForXML.getSubmissionDateTime() != null) {
			 * order.setSubmissionDateTime(orderDataForXML.getSubmissionDateTime()); }
			 
			if (orderDataForXML.getOrderDate() != null)
			{
				order.setOrderDate(orderDataForXML.getOrderDate());
			}
			if (orderDataForXML.getChannel() != null)
			{
				order.setChannel(orderDataForXML.getChannel());
			}

			final List<OrderDataSubOrderForXML> l = orderDataForXML.getSubOrder();
			final List<SubOrderWsDTO> sublist = new ArrayList<SubOrderWsDTO>();
			for (final OrderDataSubOrderForXML orderDataSubOrderForXML : l)
			{
				final SubOrderWsDTO suborderobj = new SubOrderWsDTO();
				suborderobj.setOrderNo(orderDataSubOrderForXML.getOrderNo());
				final List<OrderDataTransactionForXML> t = orderDataSubOrderForXML.getTransaction();

				final List<TransactionWsDTO> transactionobjList = new ArrayList<TransactionWsDTO>();
				for (final OrderDataTransactionForXML orderDataTransactionForXML : t)
				{
					final TransactionWsDTO transactionobj = new TransactionWsDTO();
					if (orderDataTransactionForXML.getTransactionId() != null)
					{
						transactionobj.setTransactionId(orderDataTransactionForXML.getTransactionId());
					}
					if (orderDataTransactionForXML.getSellerID() != null)
					{
						transactionobj.setSellerId(orderDataTransactionForXML.getSellerID());
					}
					if (orderDataTransactionForXML.getListingID() != null)
					{
						transactionobj.setListingId(orderDataTransactionForXML.getListingID());
					}
					if (orderDataTransactionForXML.getUSSID() != null)
					{
						transactionobj.setUssid(orderDataTransactionForXML.getUSSID());
					}
					if (orderDataTransactionForXML.getPrice() != 0)
					{
						transactionobj.setPrice(orderDataTransactionForXML.getPrice());
					}
					if (orderDataTransactionForXML.getApportionedCODPrice() != 0)
					{
						transactionobj.setApportionedCodPrice(orderDataTransactionForXML.getApportionedCODPrice());
					}
					if (orderDataTransactionForXML.getPromotionCode() != null)
					{
						transactionobj.setPromotionCode(orderDataTransactionForXML.getPromotionCode());
					}
					if (orderDataTransactionForXML.getIsaGift().toString() != null)
					{
						transactionobj.setIsaGift(Boolean.getBoolean(orderDataTransactionForXML.getIsaGift().toString()));
					}
					
					 * if (orderDataTransactionForXML.getDeliveryMode() != null) {
					 * transactionobj.setDeliveryMode(orderDataTransactionForXML.getDeliveryMode()); }
					 

					transactionobj.setShippingCharge(100.00);
					if (orderDataTransactionForXML.getSlaveID() != null)
					{
						transactionobj.setSlaveId(orderDataTransactionForXML.getSlaveID());
					}
					transactionobj.setCod(false);
					transactionobj.setReturnable(false);
					if (orderDataTransactionForXML.getProductSize() != null)
					{
						transactionobj.setProductSize(orderDataTransactionForXML.getProductSize());
					}
					if (orderDataTransactionForXML.getTransportMode() != null)
					{
						transactionobj.setTransportMode(orderDataTransactionForXML.getTransportMode());
					}
					if (orderDataTransactionForXML.getFulfillmentType() != null)
					{
						transactionobj.setFulfilmentType(orderDataTransactionForXML.getFulfillmentType());
					}



					final CustomerAddressWsDTO custAddrObj = new CustomerAddressWsDTO();
					final CustomerDataForOrderXML customerDataForOrderXML = orderDataTransactionForXML.getCustomerAddress().get(0);
					if (customerDataForOrderXML.getCustomerID() != null)
					{
						custAddrObj.setCustomerId(customerDataForOrderXML.getCustomerID());
					}
					if (customerDataForOrderXML.getEmailID() != null)
					{
						custAddrObj.setEmailId(customerDataForOrderXML.getEmailID());
					}
					if (customerDataForOrderXML.getFirstName() != null)
					{
						custAddrObj.setFirstName(customerDataForOrderXML.getFirstName());
					}
					if (customerDataForOrderXML.getLastName() != null)
					{
						custAddrObj.setLastName(customerDataForOrderXML.getLastName());
					}
					if (customerDataForOrderXML.getPhoneNumber() != null)
					{
						custAddrObj.setPhoneNo(customerDataForOrderXML.getPhoneNumber());
					}
					if (customerDataForOrderXML.getAddress1() != null)
					{
						custAddrObj.setAddress1(customerDataForOrderXML.getAddress1());
					}
					if (customerDataForOrderXML.getAddress2() != null)
					{
						custAddrObj.setAddress2(customerDataForOrderXML.getAddress2());
					}
					if (customerDataForOrderXML.getCountry() != null)
					{
						custAddrObj.setCountry(customerDataForOrderXML.getCountry());
					}
					if (customerDataForOrderXML.getCity() != null)
					{
						custAddrObj.setCity(customerDataForOrderXML.getCity());
					}
					if (customerDataForOrderXML.getState() != null)
					{
						custAddrObj.setState(customerDataForOrderXML.getState());
					}
					if (customerDataForOrderXML.getPincode() != null)
					{
						custAddrObj.setPincode(customerDataForOrderXML.getPincode());
					}
					if (customerDataForOrderXML.getDefaultFlag() != null)
					{
						custAddrObj.setDefaultFlag(customerDataForOrderXML.getDefaultFlag());
					}
					
					 * if (orderDataTransactionForXML.getCustomerAddress().get("AddressTag") != null) {
					 * custAddrObj.setAddresstag(orderDataTransactionForXML.getCustomerAddress().get("AddressTag")); }
					 
					final ProductDetailsWsDTO prodDet = new ProductDetailsWsDTO();
					//final OrderDataTransactionForXML orderDataTransactionForXML;
					final ProductDataForOrderXML productDataForOrderXML = orderDataTransactionForXML.getProductDetails().get(0);
					if (productDataForOrderXML.getCancellationAllowed() != null)
					{
						prodDet.setCancellationAllowed(Integer.parseInt(productDataForOrderXML.getCancellationAllowed()));
					}
					if (null != productDataForOrderXML.getReturnsAllowed())
					{
						prodDet.setReturnsAllowed(Integer.parseInt(productDataForOrderXML.getReturnsAllowed()));
					}
					if (null != productDataForOrderXML.getReplacementAllowed())
					{
						prodDet.setReplacementAllowed(Integer.parseInt(productDataForOrderXML.getReplacementAllowed()));
					}
					if (null != productDataForOrderXML.getExchangeAllowed())
					{
						prodDet.setExchangeAllowed(Integer.parseInt(productDataForOrderXML.getExchangeAllowed()));
					}
					if (null != productDataForOrderXML.getProductName())
					{
						prodDet.setProductName(productDataForOrderXML.getProductName());
					}

					transactionobj.setCad(custAddrObj);
					transactionobj.setPd(prodDet);
					transactionobjList.add(transactionobj);

				}
				suborderobj.setTransaction(transactionobjList);
				sublist.add(suborderobj);
			}
			order.setSuborder(sublist);
			// YTODO Auto-generated method stub

			if (orderDataForXML.getCustomerAddress() != null && !orderDataForXML.getCustomerAddress().isEmpty())
			{
				final CustomerDataForOrderXML customerDataForOrderXML = orderDataForXML.getCustomerAddress().get(0);
				final CustomerAddressWsDTO custAddrObj1 = new CustomerAddressWsDTO();
				if (customerDataForOrderXML.getCustomerID() != null)
				{
					custAddrObj1.setCustomerId(customerDataForOrderXML.getCustomerID());
				}
				if (customerDataForOrderXML.getEmailID() != null)
				{
					custAddrObj1.setEmailId(customerDataForOrderXML.getEmailID());
				}
				if (customerDataForOrderXML.getFirstName() != null)
				{
					custAddrObj1.setFirstName(customerDataForOrderXML.getFirstName());
				}
				if (customerDataForOrderXML.getLastName() != null)
				{
					custAddrObj1.setLastName(customerDataForOrderXML.getLastName());
				}
				if (customerDataForOrderXML.getPhoneNumber() != null)
				{
					custAddrObj1.setPhoneNo(customerDataForOrderXML.getPhoneNumber());
				}
				if (customerDataForOrderXML.getAddress1() != null)
				{
					custAddrObj1.setAddress1(customerDataForOrderXML.getAddress1());
				}
				if (null != customerDataForOrderXML.getAddress2())
				{
					custAddrObj1.setAddress2(customerDataForOrderXML.getAddress2());
				}
				if (null != customerDataForOrderXML.getAddress3())
				{
					custAddrObj1.setAddress3(customerDataForOrderXML.getAddress3());
				}
				if (null != customerDataForOrderXML.getCountry())
				{
					custAddrObj1.setCountry(customerDataForOrderXML.getCountry());
				}
				if (null != customerDataForOrderXML.getCity())
				{
					custAddrObj1.setCity(customerDataForOrderXML.getCity());
				}
				if (null != customerDataForOrderXML.getState())
				{
					custAddrObj1.setState(customerDataForOrderXML.getState());
				}
				if (null != customerDataForOrderXML.getPincode())
				{
					custAddrObj1.setPincode(customerDataForOrderXML.getPincode());
				}
				if (null != customerDataForOrderXML.getDefaultFlag())
				{
					custAddrObj1.setDefaultFlag(customerDataForOrderXML.getDefaultFlag());
				}
				if (null != customerDataForOrderXML.getAddressTag())
				{
					custAddrObj1.setAddresstag(customerDataForOrderXML.getAddressTag());
				}

				order.setCa(custAddrObj1);
			}

			final PaymentDetWsDTO paymentObj = new PaymentDetWsDTO();
			if (orderDataForXML.getPaymentDetails() != null && orderDataForXML.getPaymentDetails().get(0) != null)
			{
				final PaymentDetails paymentDetails = orderDataForXML.getPaymentDetails().get(0);


				if (paymentDetails.getPaymentCost() > 0)
				{
					paymentObj.setPaymentCost(paymentDetails.getPaymentCost());
				}
				if (null != paymentDetails.getPaymentMode())
				{
					paymentObj.setPaymentMode(paymentDetails.getPaymentMode());
				}
				if (null != paymentDetails.getPaymentStatus())
				{
					paymentObj.setPaymentStatus(paymentDetails.getPaymentStatus());
				}
				if (null != paymentDetails.getPaymentInfo())
				{
					paymentObj.setPaymentInfo(paymentDetails.getPaymentInfo());
				}
				//	paymentObj.setPaymentDate(orderDataForXML.getPaymentDetails().get("paymentDate"));


				order.setPd(paymentObj);

			}


			orderCreate(order);
		}
	}

	
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.service.OrderWebService#orderCreate(com.tisl.mpl.wsDTO.OrderTrialWSDTO)
	 
	@Override
	public void orderCreate(final OrderTrialWSDTO orderTrialWSDTO)
	{
		// YTODO Auto-generated method stub
		final Client client = Client.create();
		final WebResource webResource = client.resource(UriBuilder.fromUri(
				configurationService.getConfiguration().getString("ordercreate_url")).build());
		try
		{
			final JAXBContext context = JAXBContext.newInstance(OrderTrialWSDTO.class);
			final Marshaller m = context.createMarshaller(); //for pretty-print XML in JAXB
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			System.out.println("Marshalling to file!!!!");
			final File f = new File("D:\\orderCreate.xml");
			m.marshal(orderTrialWSDTO, f);
			//final StringWriter sw = new StringWriter();
			//m.marshal(orderTrialWSDTO, sw);
			//	final String xmlString = sw.toString();

			final ClientResponse response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml").entity(f)
					.post(ClientResponse.class);
			final String output = response.getEntity(String.class);

			System.out.println("output " + output);
		}

		catch (final Exception ex)
		{
			LOG.info("EXCEPTION IS" + ex);
			ex.printStackTrace();

		}



	}
}*/