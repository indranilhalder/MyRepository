/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.JusPayPaymentInfoModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
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

import com.tisl.mpl.constants.GeneratedMarketplacecommerceservicesConstants.Enumerations.OISPaymentTypeEnum;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.juspay.constants.MarketplaceJuspayServicesConstants;
import com.tisl.mpl.marketplacecommerceservices.daos.impl.MplPaymentDaoImpl;
import com.tisl.mpl.marketplacecommerceservices.service.JuspayPaymentService;


public class JuspayPaymentServiceImpl implements JuspayPaymentService
{

	private ModelService modelService;
	private ConfigurationService configurationService;


	public ModelService getModelService()
	{
		return modelService;
	}


	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}


	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}


	@Autowired
	private MplPaymentDaoImpl mplPaymentDaoImpl;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private BaseStoreService baseStoreService;

	//	@Autowired
	//	private BaseStoreService baseStoreService;

	@Autowired
	private PersistentKeyGenerator codCodeGenerator;

	public PersistentKeyGenerator getCodCodeGenerator()
	{
		return codCodeGenerator;
	}


	public void setCodCodeGenerator(final PersistentKeyGenerator codCodeGenerator)
	{
		this.codCodeGenerator = codCodeGenerator;
	}


	@Override
	public void getTransactionModel(final CartModel cart, final Double amount)
	{
		// YTODO Auto-generated method stub
		List<PaymentTransactionModel> paymentTransactionModelList = null;
		final CartModel cartModel;
		cartModel = cart;
		String jusPayCreatedOrderId = StringUtils.EMPTY;
		//Long paymentGatewayId = new Long(0);
		long paymentGatewayId = 0L;
		String bankErrorCode = StringUtils.EMPTY;
		String commerEndOrderId = StringUtils.EMPTY;
		final JaloSession jSession = JaloSession.getCurrentSession();
		if (jSession != null)
		{
			jusPayCreatedOrderId = (String) jSession.getAttribute("jusPayEndOrderId");
			paymentGatewayId = ((Long) jSession.getAttribute("paymentGatewayId")).longValue();
			bankErrorCode = (String) jSession.getAttribute("bankErrorCode");
			commerEndOrderId = (String) jSession.getAttribute("commerceEndOrderId");
		}

		if (StringUtils.isNotEmpty(jusPayCreatedOrderId))
		{
			final String[] juspayPrefix_reqId = jusPayCreatedOrderId.split("_");
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

			//final String juspayCode = getCodCodeGenerator().generate().toString();
			if (StringUtils.isNotEmpty(commerEndOrderId))
			{
				paymentTransactionModel.setCode(commerEndOrderId + "-" + System.currentTimeMillis());
			}
			else
			{
				paymentTransactionModel.setCode(cart.getCode() + "-" + System.currentTimeMillis());
			}

			paymentTransactionModel.setCurrency(cart.getCurrency());
			paymentTransactionModel.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
			paymentTransactionModel.setPlannedAmount(BigDecimal.valueOf(cart.getTotalPriceWithConv().doubleValue()));
			paymentTransactionModel.setPaymentProvider(String.valueOf(paymentGatewayId));
			paymentTransactionModel.setRequestId(juspayPrefix_reqId[1]);
			paymentTransactionModel.setRequestToken(bankErrorCode);
			paymentTransactionModelList.add(paymentTransactionModel);
			cartModel.setPaymentTransactions(paymentTransactionModelList);
			getModelService().save(paymentTransactionModel);
			getPaymentTransactionEntryModel(paymentTransactionModel, cart, amount);
			getModelService().save(cartModel);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void getPaymentTransactionEntryModel(final PaymentTransactionModel paymentTransactionModel, final CartModel cart,
			final Double amount)
	{
		final PaymentTransactionEntryModel paymentTransactionEntryModel = getModelService().create(
				PaymentTransactionEntryModel.class);
		paymentTransactionEntryModel.setPaymentTransaction(paymentTransactionModel);
		paymentTransactionEntryModel.setCurrency(cart.getCurrency());
		paymentTransactionEntryModel.setTransactionStatus(getConfigurationService().getConfiguration().getString(
				"payment.transactionStatus"));

		paymentTransactionEntryModel.setCode(paymentTransactionModel.getCode());
		paymentTransactionEntryModel.setAmount(new BigDecimal(amount.doubleValue()));
		paymentTransactionEntryModel.setTransactionStatusDetails(getConfigurationService().getConfiguration().getString(
				"payment.transactionStatusDetails"));
		paymentTransactionEntryModel.setTime(new Date());
		paymentTransactionEntryModel.setType(PaymentTransactionType.AUTHORIZATION);
		String paymentMode = StringUtils.EMPTY;
		String txnId = StringUtils.EMPTY;
		final JaloSession jSession = JaloSession.getCurrentSession();
		if (jSession != null)
		{
			paymentMode = (String) jSession.getAttribute("oisPaymentType");
			txnId = (String) jSession.getAttribute("txnId");
		}
		paymentTransactionEntryModel.setRequestToken(txnId);
		if (paymentMode.equalsIgnoreCase("credit"))
		{
			final String actualOISPaymentMode = OISPaymentTypeEnum.CREDIT.toString() + " "
					+ MarketplaceJuspayServicesConstants.JUSPAY_PAYMENTMODE_CARD_STRING;
			paymentTransactionEntryModel.setPaymentMode(mplPaymentDaoImpl.getPaymentMode(actualOISPaymentMode,
					baseStoreService.getCurrentBaseStore()));
		}
		else if (paymentMode.equalsIgnoreCase("debit"))
		{
			final String actualOISPaymentMode = OISPaymentTypeEnum.DEBIT.toString() + " "
					+ MarketplaceJuspayServicesConstants.JUSPAY_PAYMENTMODE_CARD_STRING;
			paymentTransactionEntryModel.setPaymentMode(mplPaymentDaoImpl.getPaymentMode(actualOISPaymentMode,
					baseStoreService.getCurrentBaseStore()));
		}
		else
		{
			paymentTransactionEntryModel.setPaymentMode(mplPaymentDaoImpl.getPaymentMode(paymentMode,
					baseStoreService.getCurrentBaseStore()));
		}
		getModelService().save(paymentTransactionEntryModel);
	}

	@Override
	public void createJusPayPaymentInfo(final CartModel cart)
	{
		if (cart.getPaymentInfo() != null)
		{
			removeExistingPaymentInfo(cart);
		}
		final JusPayPaymentInfoModel jusPayPaymentInfoModel = getModelService().create(JusPayPaymentInfoModel.class);
		jusPayPaymentInfoModel.setCode(UUID.randomUUID().toString());
		jusPayPaymentInfoModel.setUser(cart.getUser());
		jusPayPaymentInfoModel.setCashOwner(StringUtils.isNotEmpty(cart.getUser().getName()) ? cart.getUser().getName()
				: ((CustomerModel) cart.getUser()).getOriginalUid());
		cart.setPaymentInfo(jusPayPaymentInfoModel);
		/*
		 * cart.setConvenienceCharges(Double .valueOf(null !=
		 * baseStoreService.getCurrentBaseStore().getConvenienceChargeForCOD() ? baseStoreService
		 * .getCurrentBaseStore().getConvenienceChargeForCOD().longValue() : 0.0));
		 */
		//setting the payment modes and the amount against it in session to be used later
		final Map<String, Double> paymentInfo = new HashMap<String, Double>();
		paymentInfo.put(MarketplaceJuspayServicesConstants.JUSPAY_KEY, cart.getConvenienceCharges());
		sessionService.setAttribute("paymentModes", paymentInfo);
		sessionService.setAttribute("paymentModeForPromotion", MarketplaceJuspayServicesConstants.JUSPAY_KEY);
		getModelService().save(jusPayPaymentInfoModel);
		getModelService().save(cart);
		getModelService().refresh(cart);

	}

	protected void removeExistingPaymentInfo(final CartModel cart)
	{
		getModelService().remove(cart.getPaymentInfo());

		cart.setPaymentInfo(null);
		cart.setConvenienceCharges(null);
		sessionService.removeAttribute("paymentModes");
		sessionService.removeAttribute("paymentModeForPromotion");
		getModelService().save(cart);
		getModelService().refresh(cart);

	}

}
