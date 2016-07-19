package com.tisl.mpl.cockpits.cscockpit.widgets.controllers.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tis.mpl.facade.changedelivery.MplChangeDeliveryAddressFacade;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceChangeDeliveryAddressController;
import com.tisl.mpl.core.model.TemproryAddressModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress.MplChangeDeliveryAddressDao;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultOrderManagementActionsWidgetController;
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
	@Override
	public boolean isDeliveryAddressChangable(TypedObject orderObject) {
		OrderModel orderModel = (OrderModel) orderObject.getObject();
		boolean changable = false;
		final OrderData orderData = mplCheckoutFacade.getOrderDetailsForCode(orderModel.getCode());
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

	
	@Override
	public void ticketCreateToCrm(OrderModel Order, String customerId,
			String source) {
		mplChangeDeliveryAddressFacade.createcrmTicketForChangeDeliveryAddress(
				Order, customerId, source);

	}

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

}
