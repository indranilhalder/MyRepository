/**
 * 
 */
package com.tisl.mpl.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.velocity.tools.generic.MathTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OrderUpdateProcessModel;
import com.tisl.mpl.facades.account.address.AccountAddressFacade;

/**
 * @author pankajk
 *
 */
public class OrderSecondaryStatusEmailContext extends AbstractEmailContext<OrderUpdateProcessModel>
{
	
	private static final String ORDER_CODE = "orderReferenceNumber";
	private static final String CHILDORDERS = "childOrders";
	private static final String CUSTOMER_NAME = "customerName";



	private static final String MOBILENUMBER = "mobilenumber";
	public static final String TRACK_ORDER_URL = "trackOrderUrl";
	private static final String CUSTOMER = "Customer";
	private static final String SPACE = " ";
	private static final String NUMBERTOOL = "numberTool";
	@Autowired
	private AccountAddressFacade accountAddressFacade;
	private static final Logger LOG = Logger.getLogger(OrderNotificationEmailContext.class);


	@Override
	public void init(final OrderUpdateProcessModel orderUpdateProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(orderUpdateProcessModel, emailPageModel);
		final String orderCode = orderUpdateProcessModel.getOrder().getCode();
		final List<OrderModel> childOrders = orderUpdateProcessModel.getOrder().getChildOrders();

		
     if(LOG.isDebugEnabled()){
   	  
   	  LOG.debug("OrderSecondaryStatusEmailContext: Trigger Email for OrderNumber:"+orderCode);
     }
		final String trackOrderUrl = getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL)
				+ orderUpdateProcessModel.getOrder().getCode();
		put(TRACK_ORDER_URL, trackOrderUrl);
		put(ORDER_CODE, orderCode);
		put(CHILDORDERS, childOrders);
	
		//Setting first name and last name 
		final StringBuilder name = new StringBuilder(150);	
		final AddressModel deliveryAddress = orderUpdateProcessModel.getOrder().getDeliveryAddress();
		if (deliveryAddress != null)

		{
			if (null != deliveryAddress.getFirstname())
			{
				put(CUSTOMER_NAME, deliveryAddress.getFirstname());
			}
			if (null != deliveryAddress.getFirstname())
			{
				name.append(deliveryAddress.getFirstname());
			}
			if (null != deliveryAddress.getLastname())
			{
				name.append(SPACE).append(deliveryAddress.getLastname());
			}
			put(MOBILENUMBER, (null != deliveryAddress.getPhone1() ? deliveryAddress.getPhone1() : deliveryAddress.getCellphone()));
			put(DISPLAY_NAME, (null != deliveryAddress.getFirstname() ? deliveryAddress.getFirstname() : CUSTOMER));

		}


		final CustomerModel customer = (CustomerModel) orderUpdateProcessModel.getOrder().getUser();
		put(EMAIL, customer.getOriginalUid());
		put("math", new MathTool());
		put(NUMBERTOOL, new NumberTool());

	}

	@Override
	protected BaseSiteModel getSite(final OrderUpdateProcessModel orderProcessModel)
	{
		return orderProcessModel.getOrder().getSite();
	}

	@Override
	protected CustomerModel getCustomer(final OrderUpdateProcessModel orderProcessModel)
	{
		return (CustomerModel) orderProcessModel.getOrder().getUser();
	}

	
	@Override
	protected LanguageModel getEmailLanguage(final OrderUpdateProcessModel orderProcessModel)
	{
		return orderProcessModel.getOrder().getLanguage();
	}

	/**
	 * @return the accountAddressFacade
	 */
	public AccountAddressFacade getAccountAddressFacade()
	{
		return accountAddressFacade;
	}

	/**
	 * @param accountAddressFacade
	 *           the accountAddressFacade to set
	 */
	public void setAccountAddressFacade(final AccountAddressFacade accountAddressFacade)
	{
		this.accountAddressFacade = accountAddressFacade;
	}


}
