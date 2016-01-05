/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CODPaymentInfoModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.store.services.BaseStoreService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.enums.OTPTypeEnum;
import com.tisl.mpl.marketplacecommerceservices.daos.impl.MplPaymentDaoImpl;
import com.tisl.mpl.marketplacecommerceservices.service.CODPaymentService;


/**
 * @author TCS
 * 
 */
public class CODPaymentServiceImpl implements CODPaymentService
{

	private ModelService modelService;
	private ConfigurationService configurationService;
	@Autowired
	private MplPaymentDaoImpl mplPaymentDaoImpl;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private BaseStoreService baseStoreService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.CODPaymentService#getTransactionModel(de.hybris.platform.core
	 * .model.order.CartModel)
	 */

	@Override
	public void getTransactionModel(final CartModel cart, final Double amount)
	{
		// YTODO Auto-generated method stub
		List<PaymentTransactionModel> paymentTransactionModelList = null;
		final CartModel cartModel;
		cartModel = cart;
		final PaymentTransactionModel paymentTransactionModel = getModelService().create(PaymentTransactionModel.class);
		paymentTransactionModel.setOrder(cart);

		if (!cartModel.getPaymentTransactions().isEmpty())
		{
			paymentTransactionModelList = new ArrayList<PaymentTransactionModel>(cartModel.getPaymentTransactions());
		}
		else
		{
			paymentTransactionModelList = new ArrayList<PaymentTransactionModel>();
		}

		final String merchantTransactionCode = (new StringBuilder(String.valueOf(cart.getUser().getUid()))).append("-")
				.append(UUID.randomUUID()).toString();
		paymentTransactionModel.setCode(merchantTransactionCode);
		paymentTransactionModel.setCurrency(cart.getCurrency());
		paymentTransactionModel.setStatus("SUCCESS");
		paymentTransactionModel.setPaymentProvider(getConfigurationService().getConfiguration().getString("payment.cod"));
		paymentTransactionModelList.add(paymentTransactionModel);
		cartModel.setPaymentTransactions(paymentTransactionModelList);
		getModelService().save(paymentTransactionModel);
		getPaymentTransactionEntryModel(paymentTransactionModel, cart, amount);
		getModelService().save(cartModel);

	}

	@Override
	public void getTransactionModel(final CartModel cart)
	{
		getTransactionModel(cart, cart.getTotalPrice());

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.CODPaymentService#getPaymentTransactionEntryModel(de.hybris.platform
	 * .payment.model.PaymentTransactionModel)
	 */
	@Override
	public void getPaymentTransactionEntryModel(final PaymentTransactionModel paymentTransactionModel, final CartModel cart,
			final Double amount)
	{
		// YTODO Auto-generated method stub
		final PaymentTransactionEntryModel paymentTransactionEntryModel = getModelService().create(
				PaymentTransactionEntryModel.class);

		//paymentTransactionEntryModel.setType(value);
		paymentTransactionEntryModel.setPaymentTransaction(paymentTransactionModel);
		paymentTransactionEntryModel.setCurrency(cart.getCurrency());
		paymentTransactionEntryModel.setTransactionStatus(getConfigurationService().getConfiguration().getString(
				"payment.transactionStatus"));
		paymentTransactionEntryModel.setCode(paymentTransactionModel.getCode());
		paymentTransactionEntryModel.setAmount(new BigDecimal(amount));
		paymentTransactionEntryModel.setTransactionStatusDetails(getConfigurationService().getConfiguration().getString(
				"payment.transactionStatusDetails"));
		paymentTransactionEntryModel.setTime(new Date());
		paymentTransactionEntryModel.setType(PaymentTransactionType.AUTHORIZATION);
		//paymentTransactionEntryModel.setPaymentMode(OTPTypeEnum.COD.toString());
		paymentTransactionEntryModel.setPaymentMode(mplPaymentDaoImpl.getPaymentMode(OTPTypeEnum.COD.toString()));
		getModelService().save(paymentTransactionEntryModel);

	}


	@Override
	public void createCODPaymentInfo(final CartModel cart)
	{
		final CODPaymentInfoModel codPaymentInfoModel = getModelService().create(CODPaymentInfoModel.class);
		codPaymentInfoModel.setCode(UUID.randomUUID().toString());
		codPaymentInfoModel.setUser(cart.getUser());
		codPaymentInfoModel.setCashOwner(StringUtils.isNotEmpty(cart.getUser().getName()) ? cart.getUser().getName()
				: ((CustomerModel) cart.getUser()).getOriginalUid());
		cart.setPaymentInfo(codPaymentInfoModel);
		cart.setConvenienceCharges(Double.valueOf(baseStoreService.getCurrentBaseStore().getConvenienceChargeForCOD().longValue()));
		//setting the payment modes and the amount against it in session to be used later
		final Map<String, Double> paymentInfo = new HashMap<String, Double>();
		paymentInfo.put("COD", cart.getConvenienceCharges());
		sessionService.setAttribute("paymentModes", paymentInfo);
		sessionService.setAttribute("paymentModeForPromotion", "COD");
		getModelService().save(codPaymentInfoModel);
		getModelService().save(cart);
		getModelService().refresh(cart);

	}


	@Override
	public void removeCODPaymentInfo(final CartModel cart)
	{
		if (cart.getPaymentInfo() != null)
		{
			getModelService().remove(cart.getPaymentInfo());
		}
		cart.setPaymentInfo(null);
		cart.setConvenienceCharges(null);
		sessionService.removeAttribute("paymentModes");
		sessionService.removeAttribute("paymentModeForPromotion");
		getModelService().save(cart);
		getModelService().refresh(cart);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.CODPaymentService#getPaymentTransactionEntryModel(de.hybris.platform
	 * .payment.model.PaymentTransactionModel)
	 */
	@Override
	public void getPaymentTransactionEntryModel(final PaymentTransactionModel paymentTransactionModel, final CartModel cart)
	{

		getPaymentTransactionEntryModel(paymentTransactionModel, cart, cart.getTotalPrice());

	}

	public ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

}
