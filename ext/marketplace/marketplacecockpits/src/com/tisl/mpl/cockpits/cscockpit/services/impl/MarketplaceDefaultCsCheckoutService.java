package com.tisl.mpl.cockpits.cscockpit.services.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.services.MarketplaceCsCheckoutService;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OTPModel;
import com.tisl.mpl.enums.OTPTypeEnum;
import com.tisl.mpl.marketplacecommerceservices.service.BlacklistService;
import com.tisl.mpl.marketplacecommerceservices.service.MplDelistingService;
import com.tisl.mpl.marketplacecommerceservices.service.OTPGenericService;
import com.tisl.mpl.model.SellerInformationModel;

import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cscockpit.exceptions.ResourceMessage;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.services.checkout.impl.DefaultCsCheckoutService;
import de.hybris.platform.cscockpit.utils.SafeUnbox;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.config.ConfigurationService;

public class MarketplaceDefaultCsCheckoutService extends
		DefaultCsCheckoutService implements MarketplaceCsCheckoutService{
	
	
	/** The Constant _15. */
	private static final int _15 = 15;
	
	@Autowired
	OTPGenericService otpGenericService;
	
	/** The blacklist service. */
	@Autowired
	private BlacklistService blacklistService;
	
	private boolean lineItemdeliveryModeRequired;
	
	private boolean otpValidationRequired;
	
	/** The configuration service. */
	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private MplDelistingService mplDelistingService;
	

	public boolean isLineItemDeliveryModeRequired() {
		return lineItemdeliveryModeRequired;
	}

	@Required
	public void setLineItemDeliveryModeRequired(boolean lineItemdeliveryModeRequired) {
		this.lineItemdeliveryModeRequired = lineItemdeliveryModeRequired;
	}


	public boolean isOtpValidationRequired() {
		return otpValidationRequired;
	}
	@Required
	public void setOtpValidationRequired(boolean otpValidationRequired) {
		this.otpValidationRequired = otpValidationRequired;
	}


	private static final Logger LOG = Logger.getLogger(MarketplaceDefaultOrderSearchQueryBuilder.class);
	
	private static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
	private static final String MOBILENUMBER_REGEX = "^[0-9]{10}";

	
	@Override
	public void checkCustomerStatus(CartModel cart) throws ValidationException{
		List<ResourceMessage> errorMessages = new ArrayList<ResourceMessage>();
		
		if (cart == null)
		{
			errorMessages.add(new ResourceMessage("placeOrder.validation.invalidCart"));
		}
		else
		{
			String mobile ="0000000000";
			CustomerModel customer =  (CustomerModel) cart.getUser();
			AddressModel address = cart.getDeliveryAddress() ;
			if(address!=null){
				mobile = address.getPhone1();
			}
			
			boolean black = blacklistService.getBlacklistedCustomerforCOD(
					customer.getPk().getLongValueAsString() , customer.getOriginalUid(), mobile, StringUtils.EMPTY);
			
			if(black){
				errorMessages.add(new ResourceMessage("placeOrder.validation.customerisblacklisted"));
			}
			
			if (!errorMessages.isEmpty()){
			       throw new ValidationException(errorMessages);
		}
			
		}	
	}
	@Override
	protected void validateCartForCheckout(CartModel cart) throws ValidationException{
		List<ResourceMessage> errorMessages = new ArrayList<ResourceMessage>();
		if (cart == null)
		{
			errorMessages.add(new ResourceMessage("placeOrder.validation.invalidCart"));
		}
		else
		{
			
			checkCustomerStatus(cart);//
			
			CustomerModel customer =  (CustomerModel) cart.getUser();
		
		 OTPModel opt = otpGenericService.getLatestOTPModel(cart.getUser().getUid(), OTPTypeEnum.COD);
		boolean validateOTP =!( opt!=null && opt.getIsValidated() !=null
				&& opt.getIsValidated().booleanValue());
		
		if(! customer.getOriginalUid().matches(EMAIL_REGEX)  ) {
			errorMessages.add(new ResourceMessage("placeOrder.validation.invalidEmail"));
		}
		
	      if (cart.getEntries().isEmpty())
	      {
	        errorMessages.add(new ResourceMessage("placeOrder.validation.noItems"));
	      }
	      else if (SafeUnbox.toDouble(cart.getTotalPrice()) <= 0.0D)
	      {
	        errorMessages.add(new ResourceMessage("placeOrder.validation.invalidTotal"));
	      }

	      if ((isDeliveryAddressRequired()) && (cart.getDeliveryAddress() == null))
	      {
	        errorMessages.add(new ResourceMessage("placeOrder.validation.noShippingAddress"));
	      }
	     
	       if(!(cart.getDeliveryAddress() != null && cart.getDeliveryAddress().getPhone1()!=null  && 
	    		  cart.getDeliveryAddress().getPhone1().matches(MOBILENUMBER_REGEX) ) ) { 
	    	  errorMessages.add(new ResourceMessage("placeOrder.validation.invalidPhone"));
	      }

	      if ((isPaymentAddressRequired()) && (cart.getPaymentAddress() == null))
	      {
	        errorMessages.add(new ResourceMessage("placeOrder.validation.noPaymentAddress"));
	      }

	      if (getUnauthorizedTotal(cart) > 0.0D)
	      {
	        errorMessages.add(new ResourceMessage("placeOrder.validation.needPaymentOption"));
	      }
		
		if(isLineItemDeliveryModeRequired()){
			 List<SellerInformationModel> sellerInformationModelList =null;
			final Date sysDate = new Date();
			for(AbstractOrderEntryModel entry : cart.getEntries()){
				if(entry.getMplDeliveryMode()==null){
					errorMessages.add(new ResourceMessage("placeOrder.validation.noDeliveryMode",Arrays.asList(entry.getInfo())));
				} 
				sellerInformationModelList = mplDelistingService.getModelforUSSID(entry.getSelectedUSSID());
				if (CollectionUtils.isNotEmpty(sellerInformationModelList)
						&& sellerInformationModelList.get(0) != null
						&& sellerInformationModelList.get(0).getSellerAssociationStatus() != null
						&& (sellerInformationModelList.get(0).getSellerAssociationStatus().getCode()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.NO) || (sellerInformationModelList.get(0)
								.getEndDate() != null && sysDate.after(sellerInformationModelList.get(0).getEndDate())))){
					LOG.debug(">> Cart entry for delisted ussid for " + entry.getSelectedUSSID());
					errorMessages.add(new ResourceMessage("placeOrder.validation.entryDelisted",Arrays.asList(entry.getInfo())));
				}
			}
		}
		
		if(isOtpValidationRequired() && validateOTP)
		
		{
			errorMessages.add(new ResourceMessage("placeOrder.validation.otpFieldBlank"));
		
		}
		if(!checkCartReservationStatus(cart)) {
			errorMessages.add(new ResourceMessage("placeOrder.validation.cartNotReserved"));
		}
		
		}
		
		if (!errorMessages.isEmpty()){
			       throw new ValidationException(errorMessages);
		}
		
	}
	
	/**
	 * Check cart reservation status.
	 *
	 * @param cart the cart
	 * @param confirmAddress the confirm address
	 * @return true, if successful
	 */
	@Override
	public boolean checkCartReservationStatus(final CartModel cart) {
		boolean isCartReserved=false;
		if(null!=cart.getCartReservationDate()) {
			int reservationTime=configurationService.getConfiguration().getInt(MarketplaceCockpitsConstants.CSCOCKPIT_WIDGET_CHECKOUT_PAYMENT_RESERVATION_TIME,_15);
			LOG.info("cart reservation date time:"+cart.getCartReservationDate());
			LOG.info("cart reservation duration from configuration:"+reservationTime);
			Date validCartDate=DateUtils.addMinutes(cart.getCartReservationDate(),reservationTime);
			LOG.info("Time till which the cart should not be reserved again:"+validCartDate);
			int compare = validCartDate.compareTo(Calendar.getInstance().getTime());
			LOG.info("Date comparison result:"+compare);
			if(compare<0) {
				LOG.info("The cart is not reserved, hence the confirm address button would be enabled and COD should appear");
			} else {
				LOG.info("The cart is still reserved, hence the confirm address button would be disabled and COD wont appear");
				isCartReserved = true;
			}
		}
		return isCartReserved;
	}
	
	   protected void validateCartForCreatePayments(CartModel cart) throws ValidationException
	   {
		   List<ResourceMessage> errorMessages = new ArrayList<ResourceMessage>();
	 
	     if (cart == null)
	     {
	       errorMessages.add(new ResourceMessage("placeOrder.validation.invalidCart"));
	     }
	     else
	     {
	    	 checkCustomerStatus(cart);//
	    	 
	       if (cart.getEntries().isEmpty())
	       {
	         errorMessages.add(new ResourceMessage("placeOrder.validation.noItems"));
	       }
	       else if (SafeUnbox.toDouble(cart.getTotalPrice()) <= 0.0D)
	       {
	         errorMessages.add(new ResourceMessage("placeOrder.validation.invalidTotal"));

	       }
	 
	       if ((isDeliveryAddressRequired()) && (getPaymentDeliveryAddress(cart) == null))
	       {
	         errorMessages.add(new ResourceMessage("placeOrder.validation.noShippingAddress"));
	       }
	       
	       if(!(cart.getDeliveryAddress() != null && cart.getDeliveryAddress().getPhone1()!=null  && 
		    		  cart.getDeliveryAddress().getPhone1().matches(MOBILENUMBER_REGEX) ) ) { 
		    	  errorMessages.add(new ResourceMessage("placeOrder.validation.invalidPhone"));
		     }
	       
	 
			if(isLineItemDeliveryModeRequired()){
				for(AbstractOrderEntryModel entry : cart.getEntries()){
					if(entry.getMplDeliveryMode()==null){
						errorMessages.add(new ResourceMessage("placeOrder.validation.noDeliveryMode",Arrays.asList(entry.getInfo())));
					}
				}
			}
			

	     }
	 
	     if (errorMessages.isEmpty())
	       return;
	     throw new ValidationException(errorMessages);
	   }
	
	   public OrderModel doCheckout(CartModel cart)
			    throws ValidationException
			  {
			    validateCartForCheckout(cart);
			
			    OrderModel order = null;
			
			    try
			    {
				CommerceCheckoutParameter parm = new CommerceCheckoutParameter();
				parm.setEnableHooks(true);
				parm.setCart(cart);
				parm.setSalesApplication(SalesApplication.CALLCENTER);
				CommerceOrderResult result = getCommerceCheckoutService().placeOrder(parm);
				order = result.getOrder();
			      //order = getCommerceCheckoutService().placeOrder(cart, SalesApplication.CALLCENTER);
			    }
			    catch (InvalidCartException localInvalidCartException)
			    {
			      LOG.warn("Cart [" + cart + "] is invalid and cannot be used to place an order");
			    }
			
			    if (order != null)
			
			    {
			      getModelService().remove(cart);
			    }
			    return order;
			  }
	   
	   

	
}
