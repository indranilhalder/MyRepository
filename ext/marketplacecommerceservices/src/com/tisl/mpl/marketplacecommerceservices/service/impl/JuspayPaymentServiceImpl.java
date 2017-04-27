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

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.enums.OTPTypeEnum;
import com.tisl.mpl.marketplacecommerceservices.daos.impl.MplPaymentDaoImpl;
import com.tisl.mpl.marketplacecommerceservices.service.JuspayPaymentService;


/**
 * @author 1047493
 *
 */
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
		final String jusPayCreatedOrderId = (String) JaloSession.getCurrentSession().getAttribute("jusPayEndOrderId");
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

		final String codCode = getCodCodeGenerator().generate().toString();
		paymentTransactionModel.setCode(MarketplacecommerceservicesConstants.JUSPAY + codCode + "-" + System.currentTimeMillis());

		paymentTransactionModel.setCurrency(cart.getCurrency());
		paymentTransactionModel.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
		paymentTransactionModel.setPlannedAmount(BigDecimal.valueOf(cart.getTotalPriceWithConv().doubleValue()));
		paymentTransactionModel.setPaymentProvider(getConfigurationService().getConfiguration().getString("payment.juspay"));
		paymentTransactionModel.setRequestId(juspayPrefix_reqId[1]);
		paymentTransactionModelList.add(paymentTransactionModel);
		cartModel.setPaymentTransactions(paymentTransactionModelList);
		getModelService().save(paymentTransactionModel);
		getPaymentTransactionEntryModel(paymentTransactionModel, cart, amount);
		getModelService().save(cartModel);
	}

	@Override
	public void getPaymentTransactionEntryModel(final PaymentTransactionModel paymentTransactionModel, final CartModel cart,
			final Double amount)
	{
		final PaymentTransactionEntryModel paymentTransactionEntryModel = getModelService().create(
				PaymentTransactionEntryModel.class);
		//Reverting BACK TISPRO-192 as Order was not getting placed from CSCOCKPIT
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
		paymentTransactionEntryModel.setPaymentMode(mplPaymentDaoImpl.getPaymentMode(OTPTypeEnum.JUSPAY.toString()));
		getModelService().save(paymentTransactionEntryModel);

	}

	@Override
	public void createJusPayPaymentInfo(final CartModel cart)
	{
		final JusPayPaymentInfoModel jusPayPaymentInfoModel = getModelService().create(JusPayPaymentInfoModel.class);
		jusPayPaymentInfoModel.setCode(UUID.randomUUID().toString());
		jusPayPaymentInfoModel.setUser(cart.getUser());
		jusPayPaymentInfoModel.setCashOwner(StringUtils.isNotEmpty(cart.getUser().getName()) ? cart.getUser().getName()
				: ((CustomerModel) cart.getUser()).getOriginalUid());
		cart.setPaymentInfo(jusPayPaymentInfoModel);
		cart.setConvenienceCharges(Double
				.valueOf(null != baseStoreService.getCurrentBaseStore().getConvenienceChargeForCOD() ? baseStoreService
						.getCurrentBaseStore().getConvenienceChargeForCOD().longValue() : 0.0)); //NullPointerException handled
		//setting the payment modes and the amount against it in session to be used later
		final Map<String, Double> paymentInfo = new HashMap<String, Double>();
		paymentInfo.put("JusPay", cart.getConvenienceCharges());
		sessionService.setAttribute("paymentModes", paymentInfo);
		sessionService.setAttribute("paymentModeForPromotion", "JusPay");
		getModelService().save(jusPayPaymentInfoModel);
		getModelService().save(cart);
		getModelService().refresh(cart);

	}

}
