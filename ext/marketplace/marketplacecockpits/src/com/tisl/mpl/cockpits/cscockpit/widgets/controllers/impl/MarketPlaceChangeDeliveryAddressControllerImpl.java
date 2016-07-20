package com.tisl.mpl.cockpits.cscockpit.widgets.controllers.impl;

/**
 * @author Techouts
 * 
 */

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tis.mpl.facade.changedelivery.MplChangeDeliveryAddressFacade;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceChangeDeliveryAddressController;
import com.tisl.mpl.core.model.TemproryAddressModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.facades.data.PincodeData;
import com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress.MplChangeDeliveryAddressDao;
import com.tisl.mpl.pincode.facade.PincodeServiceFacade;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultOrderManagementActionsWidgetController;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

public class MarketPlaceChangeDeliveryAddressControllerImpl extends
		DefaultOrderManagementActionsWidgetController implements
		MarketPlaceChangeDeliveryAddressController {
	private static final Logger LOG = Logger
			.getLogger(MarketPlaceChangeDeliveryAddressControllerImpl.class);
	@Autowired
	private MplChangeDeliveryAddressFacade mplChangeDeliveryAddressFacade;
	@Autowired
	private MplOrderFacade mplOrderFacade;
	@Autowired
	private MplChangeDeliveryAddressDao changeDeliveryAddressDao;
	@Autowired
	private ModelService modelService;
	@Autowired
    private MplCheckoutFacade mplCheckoutFacade;
	@Resource
    private PincodeServiceFacade pincodeServiceFacade;
	
	
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
				changable = mplChangeDeliveryAddressFacade
						.isDeliveryAddressChangable(orderModel
								.getParentReference().getCode());
			}
		} catch (Exception e) {
			LOG.error("Exception occurred " + e.getCause());
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
		mplChangeDeliveryAddressFacade.createcrmTicketForChangeDeliveryAddress(
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
	public boolean changeDeliveryAddressCallToOMS(String orderId,
			AddressModel newDeliveryAddress) throws EtailNonBusinessExceptions {
		boolean omsResponce = false;
		try {
			omsResponce = mplChangeDeliveryAddressFacade
					.changeDeliveryRequestCallToOMS(orderId, newDeliveryAddress);
		} catch (EtailNonBusinessExceptions e) {
			throw new EtailNonBusinessExceptions(e.getRootCause(),
					e.getErrorCode());
		} catch (Exception e) {
			LOG.error("Exception occurred " + e.getCause());
		}
		return omsResponce;

	}
	
	/**
	 * This Method is used to Get the temprory Address
	 * 
	 * @author Techouts
	 * @param orderId
	 * @return TemproryAddressModel
	 */
	@Override
	public TemproryAddressModel getTempororyAddress(String orderId) throws ModelNotFoundException{
		TemproryAddressModel tempAddress = modelService
				.create(TemproryAddressModel.class);
		try {
			tempAddress = changeDeliveryAddressDao.geTemproryAddressModel(orderId);
		} catch (ModelNotFoundException e) {
			LOG.error("Model Not Found Exception " + e.getMessage());
			throw new ModelNotFoundException(e);
		} catch (Exception e) {
			LOG.error("Exception occurred " + e.getMessage());
		}
		return tempAddress;
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
	public void saveDeliveryAddress(OrderModel order, AddressModel address)
			throws ModelSavingException {
		try {
			changeDeliveryAddressDao.saveDeliveryAddress(order, address);
		} catch (ModelSavingException e) {
			LOG.error("ModelSavingException" + e.getMessage());
			throw new ModelSavingException(e.getMessage());
		} catch (Exception e) {
			LOG.error("Exception occurred " + e.getMessage());
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

}
