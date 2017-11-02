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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author PankajS
 *
 */
public class EGVOrderRecipientContext extends AbstractEmailContext<OrderProcessModel>
{

	/**
	 * 
	 */
	private static final String MARKETPLACE_EGV_CARD_ADD_URL = "marketplace.egv.card.add.url";
	private static final String CUSTOMER_CARE_NUMBER = "customerCareNumber";
	private static final String CUSTOMER_CARE_EMAIL = "customerCareEmail";
	private static final String ERROR_GETTING_EXCEPTION_WHILE_CHANING_DATE_FORMAT = "Error Getting Exception while  Chaning date Format";

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
		  String date= orderProcessModel.getOrder().getEntries().get(0).getWalletApportionPaymentInfo().getWalletCardList().get(0).getCardExpiry();
		  put("cardExp",getCardExpDate(date));  
		  put("cardPin",orderProcessModel.getOrder().getEntries().get(0).getWalletApportionPaymentInfo().getWalletCardList().get(0).getCardPinNumber());
		  
	  }
	   put("senderMSG",orderProcessModel.getOrder().getRecipientMessage());
	   put("senderName",orderProcessModel.getOrder().getGiftFromId());
	   
	   put("email",email);
		put(EMAIL,email);
		
		 try
		{
			String addMoney = getConfigurationService().getConfiguration()
					.getString(MARKETPLACE_EGV_CARD_ADD_URL) + orderProcessModel.getOrder().getCode();
			put("addMoney",addMoney);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		put("orderNumber",orderProcessModel.getOrder().getCode());
		final String customerCareNumber = configurationService.getConfiguration().getString("marketplace.sms.service.contactno",
				"1800-208-8282");
		put(CUSTOMER_CARE_NUMBER, customerCareNumber);
		final String customerCareEmail = configurationService.getConfiguration().getString("cliq.care.mail", "hello@tatacliq.com");
		put(CUSTOMER_CARE_EMAIL, customerCareEmail);
	}
	
	
	@SuppressWarnings("javadoc")
	private String getCardExpDate(String cardExpDate) 
	{
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(StringUtils.isNotEmpty(cardExpDate)){
		Date date = null;
		try {
			date = format1.parse(cardExpDate);
			LOG.info("Card Exp converting");
			return format2.format(date);
		} catch (ParseException e) {
			LOG.error(ERROR_GETTING_EXCEPTION_WHILE_CHANING_DATE_FORMAT);
		}
		}
		return null;
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
