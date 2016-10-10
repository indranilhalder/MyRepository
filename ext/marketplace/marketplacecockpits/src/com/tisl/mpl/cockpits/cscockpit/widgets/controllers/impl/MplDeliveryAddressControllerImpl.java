package com.tisl.mpl.cockpits.cscockpit.widgets.controllers.impl;

/**
 * @author Techouts
 * 
 */

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.oms.domain.changedeliveryaddress.TransactionSDDto;
import com.tis.mpl.facade.changedelivery.MplDeliveryAddressFacade;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MplDeliveryAddressController;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.facades.data.PincodeData;
import com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress.MplDeliveryAddressDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryAddressService;
import com.tisl.mpl.pincode.facade.PincodeServiceFacade;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultOrderManagementActionsWidgetController;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

public class MplDeliveryAddressControllerImpl extends
		DefaultOrderManagementActionsWidgetController implements
		MplDeliveryAddressController {
	private static final Logger LOG = Logger
			.getLogger(MplDeliveryAddressControllerImpl.class);
	@Autowired
	private MplDeliveryAddressFacade mplDeliveryAddressFacade;
	@Autowired
	private MplOrderFacade mplOrderFacade;
	@Autowired
	private MplDeliveryAddressDao mplDeliveryAddressDao;
	@Autowired
	private MplDeliveryAddressService mplDeliveryAddressService;
	@Autowired
	private ModelService modelService;
	@Autowired
    private MplCheckoutFacade mplCheckoutFacade;
	@Resource
    private PincodeServiceFacade pincodeServiceFacade;
	
	@Autowired
	private SessionService sessionService;
	
	@Resource(name = "tempAddressReverseConverter")
	private Converter<AddressData, AddressModel> tempAddressReverseConverter;
	
	
	/**
	 * this method is used to check whether delivery Address is changable or not
	 * if this method returns false ,then change delivery Address button is
	 * disabled
	 * 
	 * @author Techouts
	 * @param orderObject
	 * @return boolean
	 */
	@Override
	public boolean isDeliveryAddressChangable(TypedObject orderObject) {
		OrderModel orderModel = (OrderModel) orderObject.getObject();
		boolean changable = false;
		try {
			if (null != orderModel.getParentReference()
					&& null != orderModel.getParentReference().getCode()) {
				changable = mplDeliveryAddressFacade
						.isDeliveryAddressChangable(orderModel
								.getParentReference().getCode());
			}
		} catch (Exception e) {
			LOG.error("Exception occurred while checking whether change delivery Address for order id  " + orderModel.getParentReference().getCode());
		}

		return changable;
	}


	/**
	 * This method is used to create CRM Ticket for Change Delivery Address
	 * @author Techouts
	 * @param Order
	 * @param customerId
	 * @param source
	 * @return void
	 */
	@Override
	public void ticketCreateToCrm(OrderModel Order, String customerId,
			String source) {
		mplDeliveryAddressFacade.createcrmTicketForChangeDeliveryAddress(
				Order, customerId, source);

	}

	/**
	 * This method is used to Call OMS for changeDeliveryAddress Request
	 * 
	 * @author Techouts
	 * @param code
	 * @param newDeliveryAddress
	 * @return boolean
	 */
	@Override
	public String changeDeliveryAddressCallToOMS(String orderId,
			AddressModel newDeliveryAddress,String interfaceType,List<TransactionSDDto> transactionSDDtos) throws EtailNonBusinessExceptions {
		String omsResponce = null;
		try {
			//String interfaceType="CA";
			//List<TransactionSDDto> transactionSDDtos=null;
			omsResponce = mplDeliveryAddressFacade
					.changeDeliveryRequestCallToOMS(orderId, newDeliveryAddress,interfaceType,transactionSDDtos);
		} catch (EtailNonBusinessExceptions e) {
			throw new EtailNonBusinessExceptions(e.getRootCause(),
					e.getErrorCode());
		} catch (Exception e) {
			LOG.error("Exception occurred in  changeDeliveryRequestCallToOMS " + e.getCause());
		}
		return omsResponce;

	}
	
	

	/**
	 * This method is used to save the delivery address and customer Addresses
	 * and delivery Addresses for an order
	 * 
	 * @author Techouts
	 * @param order
	 * @param address
	 * @return void
	 * 
	 */
	@Override
	public void saveDeliveryAddress(OrderModel orderModel,AddressModel address)
			throws ModelSavingException {
		try {
			mplDeliveryAddressService.saveDeliveryAddress(address,orderModel);
		} catch (ModelSavingException e) {
			LOG.error("ModelSavingException  while saving the Changed delivery Address " + e.getMessage());
			throw new ModelSavingException(e.getMessage());
		} catch (Exception e) {
			LOG.error("Exception occurred while saving the Changed delivery Address" + e.getMessage());
		}

	}

	/**
	 * This method is used to get The PincodeData for a particular pincode
	 * 
	 * @author Techouts
	 * @param pincode
	 * @return PincodeData
	 */
	@Override
	public PincodeData getPincodeData(String pincode) {
		 PincodeData pincodeData = null;
		try {
			   pincodeData = pincodeServiceFacade.getAutoPopulatePincodeData(pincode);
		}catch ( FlexibleSearchException e) {
			LOG.error(" FlexibleSearchException while getting pincode data "+e.getMessage());
		}catch(Exception e) {
			LOG.error(" Exception occurred while getting pincode data "+e.getMessage());
		}
		return pincodeData;
	}


	@Override
	public boolean checkScheduledDeliveryForOrder(OrderModel orderModel) {
		return mplDeliveryAddressFacade.checkScheduledDeliveryForOrder(orderModel);
	}


	@Override
	public void saveChangeDeliveryRequests(OrderModel orderModel) {
		mplDeliveryAddressFacade.saveChangeDeliveryRequests(orderModel);
		
	}

}
