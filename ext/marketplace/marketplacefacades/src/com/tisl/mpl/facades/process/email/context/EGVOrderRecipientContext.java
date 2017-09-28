/**
 * 
 */
package com.tisl.mpl.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author PankajS
 *
 */
public class EGVOrderRecipientContext extends AbstractEmailContext<OrderProcessModel>
{

	private static final String CUSTOMER_CARE_NUMBER = "customerCareNumber";
	private static final String CUSTOMER_CARE_EMAIL = "customerCareEmail";

	@Autowired
	private ConfigurationService configurationService;
	private static final Logger LOG = Logger.getLogger(EGVOrderRecipientContext.class);



	@Override
	public void init(final OrderProcessModel orderProcessModel, final EmailPageModel emailPageModel)
	{
		
		LOG.info("Prepare Email template befor..... ");
		
	   super.init(orderProcessModel, emailPageModel);
		LOG.info("Prepare Email template before ");
	   String email =null;
	   if(orderProcessModel.getOrder().getRecipientId()!=null){
	   	email=orderProcessModel.getOrder().getRecipientId();
	   }
	   else{
	   	if((orderProcessModel.getOrder() !=null && orderProcessModel.getOrder().getUser()!=null)){
	   		email=orderProcessModel.getOrder().getUser().getUid();
	   	}
	   }
	  if(orderProcessModel.getOrder()!=null && orderProcessModel.getOrder().getEntries()!=null){
		  put("abstractOrderEntryList",orderProcessModel.getOrder().getEntries());  
		  put("cardAmount",orderProcessModel.getOrder().getEntries().get(0).getWalletApportionPaymentInfo().getWalletCardList().get(0).getCardAmount());
		  put("cardNumber", orderProcessModel.getOrder().getEntries().get(0).getWalletApportionPaymentInfo().getWalletCardList().get(0).getCardNumber());
		  put("cardExp",orderProcessModel.getOrder().getEntries().get(0).getWalletApportionPaymentInfo().getWalletCardList().get(0).getCardExpiry());  
		  put("cardPin",orderProcessModel.getOrder().getEntries().get(0).getWalletApportionPaymentInfo().getWalletCardList().get(0).getCardPinNumber());
        
		 
		  
	  }
	   put("senderMSG",orderProcessModel.getOrder().getRecipientMessage());
	   put("senderName",orderProcessModel.getOrder().getGiftFromId());
		put(EMAIL,email);
		put("orderNumber",orderProcessModel.getOrder().getCode());
		final String customerCareNumber = configurationService.getConfiguration().getString("marketplace.sms.service.contactno",
				"1800-208-8282");
		put(CUSTOMER_CARE_NUMBER, customerCareNumber);


		final String customerCareEmail = configurationService.getConfiguration().getString("cliq.care.mail", "hello@tatacliq.com");
		put(CUSTOMER_CARE_EMAIL, customerCareEmail);


	}

	@Override
	protected BaseSiteModel getSite(final OrderProcessModel orderProcessModel)
	{
		return orderProcessModel.getOrder().getSite();
	}

	@Override
	protected CustomerModel getCustomer(final OrderProcessModel orderProcessModel)
	{
		return (CustomerModel) orderProcessModel.getOrder().getUser();
	}

	
	@Override
	protected LanguageModel getEmailLanguage(final OrderProcessModel orderProcessModel)
	{
		return orderProcessModel.getOrder().getLanguage();
	}


}
