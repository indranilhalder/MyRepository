/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.order.CommerceCartCalculationStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CODPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.DebitCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.EMIPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.JusPayPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.NetbankingPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.order.payment.ThirdPartyWalletInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.model.AbstractPromotionRestrictionModel;
import de.hybris.platform.promotions.model.OrderPromotionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.EBSResponseStatus;
import com.tisl.mpl.core.enums.EBSRiskLevelEnum;
import com.tisl.mpl.core.enums.JuspayRefundType;
import com.tisl.mpl.core.enums.MplPaymentAuditStatusEnum;
import com.tisl.mpl.core.enums.WalletEnum;
import com.tisl.mpl.core.model.BankforNetbankingModel;
import com.tisl.mpl.core.model.EMIBankModel;
import com.tisl.mpl.core.model.EMITermRowModel;
import com.tisl.mpl.core.model.JuspayCardStatusModel;
import com.tisl.mpl.core.model.JuspayEBSResponseDataModel;
import com.tisl.mpl.core.model.JuspayOrderStatusModel;
import com.tisl.mpl.core.model.MplPaymentAuditEntryModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.core.model.PaymentModeApportionModel;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;
import com.tisl.mpl.core.model.SavedCardModel;
import com.tisl.mpl.data.EMITermRateData;
import com.tisl.mpl.data.MplPromoPriceData;
import com.tisl.mpl.data.MplPromotionData;
import com.tisl.mpl.data.VoucherDiscountData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.juspay.PaymentService;
import com.tisl.mpl.juspay.request.AddCardRequest;
import com.tisl.mpl.juspay.request.GetOrderStatusRequest;
import com.tisl.mpl.juspay.response.AddCardResponse;
import com.tisl.mpl.juspay.response.CardResponse;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.marketplacecommerceservices.daos.MplOrderDao;
import com.tisl.mpl.marketplacecommerceservices.daos.MplPaymentDao;
import com.tisl.mpl.marketplacecommerceservices.daos.MplProcessOrderDao;
import com.tisl.mpl.marketplacecommerceservices.order.MplCommerceCartCalculationStrategy;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplFraudModelService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;
import com.tisl.mpl.marketplacecommerceservices.service.MplMWalletRefundService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentTransactionService;
import com.tisl.mpl.marketplacecommerceservices.service.MplVoucherService;
import com.tisl.mpl.model.BankModel;
import com.tisl.mpl.model.PaymentModeSpecificPromotionRestrictionModel;
import com.tisl.mpl.model.PaymentTypeModel;
import com.tisl.mpl.util.DiscountUtility;
import com.tisl.mpl.util.GenericUtilityMethods;
import com.tisl.mpl.util.MplEMICalculator;
import com.tisl.mpl.util.OrderStatusSpecifier;


/**
 * @author TCS
 *
 */
@Component
@Qualifier(MarketplacecommerceservicesConstants.MPLPAYMENTSERVICE)
public class MplPaymentServiceImpl implements MplPaymentService
{
	private static final Logger LOG = Logger.getLogger(MplPaymentServiceImpl.class);
	private MplPaymentDao mplPaymentDao;
	@Resource
	private ModelService modelService;
	@Resource
	private UserService userService;
	@Resource
	private CartService cartService;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	@Resource(name = "i18NService")
	private I18NService i18NService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "commerceCartCalculationStrategy")
	private CommerceCartCalculationStrategy commerceCartCalculationStrategy;
	@Resource(name = "promotionsService")
	private PromotionsService promotionsService;
	@Resource(name = "discountUtility")
	private DiscountUtility discountUtility;
	@Resource(name = "mplCommerceCartService")
	private MplCommerceCartService mplCommerceCartService;

	@Autowired
	private BaseStoreService baseStoreService;

	//@Autowired
	//private CommerceCartService commerceCartService;

	@Autowired
	private PersistentKeyGenerator juspayOrderIdGenerator;

	@Autowired
	private PersistentKeyGenerator walletOrderIdGenerator;

	@Autowired
	private MplCommerceCartCalculationStrategy calculationStrategy;
	@Autowired
	private Converter<CartModel, CartData> mplExtendedCartConverter;
	@Autowired
	private MplPaymentTransactionService mplPaymentTransactionService;
	@Autowired
	private PersistentKeyGenerator codCodeGenerator;
	@Autowired
	private FlexibleSearchService flexibleSearchService;
	@Autowired
	private MplVoucherService mplVoucherService;
	@Autowired
	private OrderStatusSpecifier orderStatusSpecifier;
	@Autowired
	private MplFraudModelService mplFraudModelService;
	@Autowired
	private Converter<OrderModel, OrderData> orderConverter;
	@Resource(name = "mplOrderDao")
	private MplOrderDao mplOrderDao;
	@Resource(name = "mplProcessOrderDao")
	private MplProcessOrderDao mplProcessOrderDao;
	private Converter<JuspayOrderStatusModel, GetOrderStatusResponse> juspayOrderResponseConverter;

	//@Autowired
	//private ExtendedUserService extendedUserService;
	private static final String ERROR_PAYMENT = "Payment_Timeout order status for orderCode>>>";

	//Sonar Fix
	private static final String FAILURE_KEY = "FAILURE";

	@Autowired
	private MplJusPayRefundService mplJusPayRefundService; //Added for TPR-1348

	/**
	 * @return the mplJusPayRefundService
	 */
	public MplJusPayRefundService getMplJusPayRefundService()
	{
		return mplJusPayRefundService;
	}


	/**
	 * @param mplJusPayRefundService
	 *           the mplJusPayRefundService to set
	 */
	public void setMplJusPayRefundService(final MplJusPayRefundService mplJusPayRefundService)
	{
		this.mplJusPayRefundService = mplJusPayRefundService;
	}


	/**
	 * @return the mplMWalletRefundService
	 */
	public MplMWalletRefundService getMplMWalletRefundService()
	{
		return mplMWalletRefundService;
	}


	/**
	 * @param mplMWalletRefundService
	 *           the mplMWalletRefundService to set
	 */
	public void setMplMWalletRefundService(final MplMWalletRefundService mplMWalletRefundService)
	{
		this.mplMWalletRefundService = mplMWalletRefundService;
	}


	@Autowired
	private MplMWalletRefundService mplMWalletRefundService; //Added for TPR-1348


	/**
	 * This method returns the list of all active Payment modes(eg. Credit Card, Debit Card, COD, etc.) for the specific
	 * store and displays them on the payment page of the store
	 *
	 * @param store
	 * @return List<PaymentTypeModel>
	 * @throws EtailNonBusinessExceptions
	 *
	 */
	@Override
	public List<PaymentTypeModel> getPaymentModes(final String store) throws EtailNonBusinessExceptions
	{
		//getting the Payment types
		//final List<PaymentTypeModel> paymentTypes = getMplPaymentDao().getPaymentTypes(store);

		//returning the payment types
		return getMplPaymentDao().getPaymentTypes(store);
	}


	/**
	 * This method searches all those banks for Netbanking which have Priority field set as true and returns them in a
	 * list of type BankForNetbanking
	 *
	 * @return List<BankforNetbankingModel>
	 *
	 */
	@Override
	public List<BankforNetbankingModel> getBanksByPriority() throws EtailNonBusinessExceptions
	{
		//getting the priority banks
		final List<BankforNetbankingModel> bankList = getMplPaymentDao().getBanksByPriority();

		//returning the priority banklist
		return bankList;
	}


	/**
	 * This method searches all those banks for Netbanking which have Priority field set as false and returns them in a
	 * list of type BankForNetbanking
	 *
	 * @return List<BankforNetbankingModel>
	 *
	 */
	@Override
	public List<BankforNetbankingModel> getOtherBanks() throws EtailNonBusinessExceptions
	{
		//getting the non priority banks
		final List<BankforNetbankingModel> bankList = getMplPaymentDao().getOtherBanks();

		//returning the non priority banklist
		return bankList;
	}


	/**
	 * This method returns the list of all Banks available for EMI. The cartValue argument is used to check whether the
	 * total amount in the cart lies within the bank's upper and lower limits for EMI. Only those banks are returned
	 * whose upper limit is greater than/equal to cartValue and also whose lower limit is less than/equal to cartValue
	 *
	 * @param cartValue
	 * @return List<EMIBankModel>
	 */
	@Override
	public List<EMIBankModel> getEMIBanks(final Double cartValue) throws EtailNonBusinessExceptions
	{
		//getting the banks related to EMI
		List<EMIBankModel> emiBankList = new ArrayList<EMIBankModel>();
		final String emiCuttOffAmount = getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.EMI_CUTOFF);
		Double emiThreshold = new Double(0.0);
		if (null != emiCuttOffAmount)
		{
			emiThreshold = Double.valueOf(emiCuttOffAmount);
		}

		if (null != cartValue && cartValue.doubleValue() >= emiThreshold.doubleValue())
		{
			//TISPRO-179
			emiBankList = getMplPaymentDao().getEMIBanks(cartValue, null);
		}

		return emiBankList;
	}


	/**
	 * This method takes the totalAmount as parameter and then calculates the emi using the emi calculation logic. It the
	 * returns an ArrayList of EMITermrateData for the selected bank to be displayed on the payment page.
	 *
	 * @param bank
	 * @param totalAmount
	 * @return ArrayList<EMITermRateData>
	 */
	@Override
	public List<EMITermRateData> getBankTerms(final String bank, final Double totalAmount)
	{
		//getting the bank terms for EMI
		final List<EMIBankModel> termList = getMplPaymentDao().getEMIBankTerms(bank);
		ArrayList<EMITermRateData> emiTermRate = new ArrayList<EMITermRateData>();
		final CartModel cartModel = getCartService().getSessionCart();

		//When the termlist is not null
		if (null != termList)
		{
			//Looping through the termlist
			for (final EMIBankModel term : termList)
			{
				//When the term rates are not null
				if (null != term.getEMITermRates())
				{
					//Looping through the term rates
					for (final EMITermRowModel termRate : term.getEMITermRates())
					{
						final EMITermRateData termDatas = new EMITermRateData();
						termDatas.setTerm(termRate.getTermInMonths().toString());
						termDatas.setInterestRate(String.format(MarketplacecommerceservicesConstants.FORMATONE,
								termRate.getInterestRate()));
						final Double termInMonths = Double.valueOf(termRate.getTermInMonths().doubleValue());

						//For Future: The below piece of code is written which may be used in the future
						//termRate.getInterestRate().doubleValue() gives the interest rate percent. To get the interest rate per month, we divide by 1200
						final Double interestRatePerMonth = Double.valueOf((termRate.getInterestRate().doubleValue())
								/ MarketplacecommerceservicesConstants.MONTHDENO);
						final Double emi = MplEMICalculator.emiCalculator(termInMonths, interestRatePerMonth, totalAmount);

						final PriceData formattedEmi = getDiscountUtility().createPrice(cartModel, emi);
						//termDatas.setMonthlyInstallment(String.format(MarketplacecommerceservicesConstants.FORMAT, emi));
						termDatas.setMonthlyInstallment(formattedEmi.getFormattedValue());

						final Double interestPayable = MplEMICalculator.interestPayable(emi, termInMonths, totalAmount);

						final PriceData formattedInterest = getDiscountUtility().createPrice(cartModel, interestPayable);
						//termDatas.setInterestPayable(String.format(MarketplacecommerceservicesConstants.FORMAT, interestPayable));
						termDatas.setInterestPayable(formattedInterest.getFormattedValue());
						emiTermRate.add(termDatas);
					}
				}
				else
				{
					emiTermRate.add(null);
				}
			}
			//returning the term rates
			//return emiTermRate;	SONAR Fix
		}
		else
		{
			//return null;	SONAR Fix
			emiTermRate = null;
		}
		//returning the term rates
		return emiTermRate;
	}


	/**
	 * This method saves the card details once successful response is received from Juspay
	 *
	 * @param orderStatusResponse
	 * @param paymentMode
	 * @param cart
	 */
	@Override
	public void saveCardDetailsFromJuspay(final GetOrderStatusResponse orderStatusResponse, final Map<String, Double> paymentMode,
			final AbstractOrderModel cart) throws EtailNonBusinessExceptions //Changed to abstractOrderModel for TPR-629
	{
		if (null != orderStatusResponse)
		{
			LOG.info(MarketplacecommerceservicesConstants.JUSPAY_ORDER_STAT_RESP + orderStatusResponse);

			//Logic if the order status response is not null

			//TISPRO-540
			//			for (final Map.Entry<String, Double> entry : paymentMode.entrySet())
			//			{
			try
			{
				//				if (null != orderStatusResponse.getCardResponse() && StringUtils.isNotEmpty(cart.getModeOfPayment())
				//						&& MarketplacecommerceservicesConstants.DEBIT.equalsIgnoreCase(cart.getModeOfPayment()))
				if (null != orderStatusResponse.getCardResponse()
						&& StringUtils.isNotEmpty(orderStatusResponse.getCardResponse().getCardType())
						&& orderStatusResponse.getCardResponse().getCardType().equalsIgnoreCase("DEBIT"))
				{
					//saving the cartmodel for Debit Card
					getModelService().save(setValueInDebitCardPaymentInfo(cart, orderStatusResponse));
					//						break;
				}
				//				else if (null != orderStatusResponse.getCardResponse() && StringUtils.isNotEmpty(cart.getModeOfPayment())
				//						&& MarketplacecommerceservicesConstants.CREDIT.equalsIgnoreCase(cart.getModeOfPayment()))
				else if (null != orderStatusResponse.getCardResponse()
						&& StringUtils.isNotEmpty(orderStatusResponse.getCardResponse().getCardType())
						&& orderStatusResponse.getCardResponse().getCardType().equalsIgnoreCase("CREDIT"))
				{
					//saving the cartmodel for Credit Card
					getModelService().save(setValueInCreditCardPaymentInfo(cart, orderStatusResponse));
					//						break;
				}
				//				else if (StringUtils.isNotEmpty(cart.getModeOfPayment())
				//						&& MarketplacecommerceservicesConstants.NETBANKING.equalsIgnoreCase(cart.getModeOfPayment()))
				else if (StringUtils.isNotEmpty(orderStatusResponse.getPaymentMethodType())
						&& orderStatusResponse.getPaymentMethodType().equalsIgnoreCase("NB"))
				{
					//saving the cartmodel for Netbanking
					getModelService().save(setValueInNetbankingPaymentInfo(cart, orderStatusResponse));
					//						break;
				}

				//Added for paytm integration
				else if (StringUtils.isNotEmpty(orderStatusResponse.getPaymentMethodType())
						&& orderStatusResponse.getPaymentMethodType().equalsIgnoreCase("WALLET"))
				{
					//saving the cartmodel for paytm
					getModelService().save(setValueInPaytmPaymentInfo(cart, orderStatusResponse));
					//						break;
				}
				else if (null != orderStatusResponse.getCardResponse() && StringUtils.isNotEmpty(orderStatusResponse.getBankEmi()))
				{
					//saving the cartmodel for EMI
					getModelService().save(setValueInEMIPaymentInfo(cart, orderStatusResponse));
					//						break;
				}

			}
			catch (final ModelSavingException e)
			{
				LOG.error(MarketplacecommerceservicesConstants.PAYMENT_EXC_LOG + e);
				//throw new ModelSavingException(e + MarketplacecommerceservicesConstants.PAYMENT_EXC_LOG_END);
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
			}
			catch (final Exception e)
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
			}
			//			}
			//}
		}
	}

	/**
	 * This method is setting paymentTransactionModel and the paymentTransactionEntryModel against the order for non-COD
	 * payment modes including wallet. After TPR-629, payment details will be attached to order
	 *
	 * @param orderStatusResponse
	 * @param paymentMode
	 * @param order
	 *
	 */
	@Override
	public void setPaymentTransaction(final GetOrderStatusResponse orderStatusResponse, final Map<String, Double> paymentMode,
			final AbstractOrderModel order) throws EtailNonBusinessExceptions //Changed to abstractOrderModel for TPR-629
	{
		Collection<PaymentTransactionModel> collection = order.getPaymentTransactions();
		final List<PaymentTransactionModel> paymentTransactionList = new ArrayList<PaymentTransactionModel>();
		//Soln Changes
		PaymentTransactionModel payTranModel = null;
		if (null == collection || collection.isEmpty())
		{
			collection = new ArrayList<PaymentTransactionModel>();
		}

		paymentTransactionList.addAll(collection);
		//List<PaymentTransactionEntryModel> paymentTransactionEntryList = new ArrayList<PaymentTransactionEntryModel>();

		//final PaymentTransactionModel paymentTransactionModel = getModelService().create(PaymentTransactionModel.class);
		final Date date = new Date();

		String checkValues = "".intern();
		String[] parts = null;
		String saveCard = "".intern();
		String sameAsShipping = "".intern();
		if (null != orderStatusResponse)
		{
			List<PaymentTransactionEntryModel> paymentTransactionEntryList = new ArrayList<PaymentTransactionEntryModel>();
			LOG.info(MarketplacecommerceservicesConstants.JUSPAY_ORDER_STAT_RESP + orderStatusResponse);
			if (StringUtils.isNotEmpty(orderStatusResponse.getUdf10()))
			{
				checkValues = orderStatusResponse.getUdf10();
			}
			if (checkValues.contains(MarketplacecommerceservicesConstants.CONCTASTRING))
			{
				parts = checkValues.split(MarketplacecommerceservicesConstants.SPLITSTRING);
				saveCard = parts[0];
				sameAsShipping = parts[1];
			}
			for (final Map.Entry<String, Double> entry : paymentMode.entrySet())
			{
				//Setting fields of paymentTransactionEntry with Payment Gateway Responses for Wallet
				if (MarketplacecommerceservicesConstants.WALLET.equalsIgnoreCase(entry.getKey()))
				{
					final PaymentTransactionEntryModel paymentTransactionEntry = getModelService().create(
							PaymentTransactionEntryModel.class);
					//TODO:Change required when Order Ref No. is ready
					if (StringUtils.isNotEmpty(orderStatusResponse.getOrderId()))
					{
						paymentTransactionEntry.setCode(orderStatusResponse.getOrderId() + "-" + System.currentTimeMillis());
					}
					paymentTransactionEntry.setAmount(BigDecimal.valueOf(entry.getValue().doubleValue()));
					paymentTransactionEntry.setTime(date);
					paymentTransactionEntry.setCurrency(order.getCurrency());
					//	paymentTransactionEntry.setPaymentMode(MarketplacecommerceservicesConstants.WALLET);//TODO::Wallet not in scope of Release 1
					paymentTransactionEntry.setTransactionStatus(MarketplacecommerceservicesConstants.SUCCESS);

					try
					{
						//Check handled to remove concurrent scenario - TPR-629
						if (null == order.getPaymentInfo() && !OrderStatus.PAYMENT_TIMEOUT.equals(order.getStatus()))
						{
							getModelService().save(paymentTransactionEntry);
							paymentTransactionEntryList.add(paymentTransactionEntry);
						}
						else if (null != order.getPaymentInfo())
						{
							LOG.error("Order already has payment info -- not saving paymentTransactionEntry>>>"
									+ order.getPaymentInfo().getCode());
						}
						else
						{
							LOG.error(ERROR_PAYMENT + order.getCode());
						}

					}
					catch (final ModelSavingException e)
					{
						LOG.error(MarketplacecommerceservicesConstants.PAYMENT_TRAN_EXC_LOG + e);
						throw new ModelSavingException(e + ": Exception while saving payment transaction entry with");
					}
				}

				//Setting fields of paymentTransactionEntry with Payment Gateway Responses for other payment modes
				else
				{
					paymentTransactionEntryList = getMplPaymentTransactionService().createPaymentTranEntry(orderStatusResponse, order,
							entry, paymentTransactionEntryList);
				}
			}

			//Soln Changes
			//OrderIssues:- Null or Empty check added
			if (CollectionUtils.isNotEmpty(paymentTransactionEntryList))
			{
				payTranModel = getMplPaymentTransactionService().createPaymentTransaction(order, orderStatusResponse,
						paymentTransactionEntryList);
				paymentTransactionList.add(payTranModel);
			}
			else
			{
				LOG.error("paymentTransactionEntryList is Empty");
			}
		}

		if (null == order.getPaymentInfo() && !OrderStatus.PAYMENT_TIMEOUT.equals(order.getStatus()))
		{
			order.setPaymentTransactions(paymentTransactionList);

			//Check handled to remove concurrent scenario - TPR-629
			getModelService().save(order);
			if (saveCard.equalsIgnoreCase(MarketplacecommerceservicesConstants.TRUE)
					&& null != orderStatusResponse.getCardResponse()
					&& StringUtils.isNotEmpty(orderStatusResponse.getCardResponse().getCardReference()))
			{
				//setting as saved card
				saveCards(orderStatusResponse, paymentMode, order, sameAsShipping);
			}
		}
		else if (null != order.getPaymentInfo())
		{
			LOG.error("Order already has payment info -- not saving order or card models>>>" + order.getPaymentInfo().getCode());
		}
		else
		{
			LOG.error(ERROR_PAYMENT + order.getCode());
		}
	}



	/**
	 * This method is setting paymentTransactionModel and the paymentTransactionEntryModel against the cart for COD and
	 * wallet
	 *
	 * @param abstractOrderModel
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public void setPaymentTransactionForCOD(final AbstractOrderModel abstractOrderModel) throws EtailNonBusinessExceptions //Changed to abstractOrderModel for TPR-629
	{
		try
		{
			//Sonar Issue Fixed For Kidswear: null check added for abstractOrderModel
			if (null != abstractOrderModel)
			{
				// TISPRD-361
				Collection<PaymentTransactionModel> collection = abstractOrderModel.getPaymentTransactions();
				final List<PaymentTransactionModel> paymentTransactionList = new ArrayList<PaymentTransactionModel>();
				//if (null == collection || collection.isEmpty())
				if (CollectionUtils.isEmpty(collection))

				{
					collection = new ArrayList<PaymentTransactionModel>();
				}

				paymentTransactionList.addAll(collection);

				final List<PaymentTransactionEntryModel> paymentTransactionEntryList = new ArrayList<PaymentTransactionEntryModel>();

				final PaymentTransactionModel paymentTransactionModel = getModelService().create(PaymentTransactionModel.class);
				final Date date = new Date();
				final String codCode = getCodCodeGenerator().generate().toString();

				final PaymentTransactionEntryModel paymentTransactionEntry = getModelService().create(
						PaymentTransactionEntryModel.class);

				// SprintPaymentFixes Multiple Payment Transaction with success status one with 0.0 and another with proper amount
				paymentTransactionEntry
						.setCode(MarketplacecommerceservicesConstants.COD + codCode + "-" + System.currentTimeMillis());
				//SONAR FIX updated
				if (null != abstractOrderModel.getTotalPriceWithConv()
						&& abstractOrderModel.getTotalPriceWithConv().doubleValue() > 0.0)
				{
					paymentTransactionEntry.setAmount(BigDecimal.valueOf(abstractOrderModel.getTotalPriceWithConv().doubleValue()));
				}

				paymentTransactionEntry.setTime(date);
				paymentTransactionEntry.setCurrency(abstractOrderModel.getCurrency());
				paymentTransactionEntry.setType(PaymentTransactionType.COD_PAYMENT);
				paymentTransactionEntry.setTransactionStatus(MarketplacecommerceservicesConstants.SUCCESS);

				PaymentTypeModel paymentTypeModelCOD = modelService.create(PaymentTypeModel.class);
				paymentTypeModelCOD.setMode(MarketplacecommerceservicesConstants.COD);
				paymentTypeModelCOD.setBaseStore(baseStoreService.getCurrentBaseStore());
				paymentTypeModelCOD = flexibleSearchService.getModelByExample(paymentTypeModelCOD);
				paymentTransactionEntry.setPaymentMode(paymentTypeModelCOD);

				if (null == abstractOrderModel.getPaymentInfo()
						&& !OrderStatus.PAYMENT_TIMEOUT.equals(abstractOrderModel.getStatus()))
				{
					getModelService().save(paymentTransactionEntry);
					paymentTransactionEntryList.add(paymentTransactionEntry);
				}
				else
				{
					LOG.error("PaymentInfo already available or order is Payment_Timeout.....not saving any more paymentTransactionEntry model");
				}

				if (null != abstractOrderModel.getPaymentInfo())
				{
					paymentTransactionModel.setInfo(abstractOrderModel.getPaymentInfo());
				}

				paymentTransactionModel
						.setCode(MarketplacecommerceservicesConstants.COD + codCode + "-" + System.currentTimeMillis());

				paymentTransactionModel.setCreationtime(date);
				paymentTransactionModel.setCurrency(abstractOrderModel.getCurrency());
				paymentTransactionModel.setEntries(paymentTransactionEntryList);
				paymentTransactionModel.setPaymentProvider(getConfigurationService().getConfiguration().getString("payment.cod"));
				paymentTransactionModel.setOrder(abstractOrderModel);

				// SprintPaymentFixes Multiple Payment Transaction with success status one with 0.0 and another with proper amount
				//SONAR FIX updated

				if (null != abstractOrderModel.getTotalPriceWithConv()
						&& abstractOrderModel.getTotalPriceWithConv().doubleValue() > 0.0)
				{
					paymentTransactionModel.setPlannedAmount(BigDecimal.valueOf(abstractOrderModel.getTotalPriceWithConv()
							.doubleValue()));
					//the flag is used to identify whether all the entries in the PaymentTransactionModel are successful or not. If all are successful then flag is set as true and status against paymentTransactionModel is set as success
				}
				//COD Payment transaction check
				if (CollectionUtils.isNotEmpty(paymentTransactionEntryList)
						&& StringUtils.isNotEmpty(paymentTransactionEntryList.get(0).getTransactionStatus())
						&& paymentTransactionEntryList.get(0).getTransactionStatus()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
				{
					paymentTransactionModel.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
				}
				else
				{
					paymentTransactionModel.setStatus(MarketplacecommerceservicesConstants.FAILURE);
				}


				if (null == abstractOrderModel.getPaymentInfo()
						&& !OrderStatus.PAYMENT_TIMEOUT.equals(abstractOrderModel.getStatus()))
				{
					getModelService().save(paymentTransactionModel);
					paymentTransactionList.add(paymentTransactionModel);
					abstractOrderModel.setPaymentTransactions(paymentTransactionList);
					getModelService().save(abstractOrderModel);
				}
				else if (null != abstractOrderModel.getPaymentInfo())
				{
					LOG.error("PaymentInfo already available.....not saving any more paymentTransactionModel and not setting against the abstractOrderModel>>>"
							+ abstractOrderModel.getPaymentInfo().getCode());
				}
				else
				{
					LOG.error(ERROR_PAYMENT + abstractOrderModel.getCode());
				}

			}
		}
		catch (final ModelSavingException e)
		{
			LOG.error("Inside setPaymentTransactionForCOD::Exception while saving cart with ", e); //Sonar fix
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9214);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception while setPaymentTransactionForCOD ", ex);
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B9214);
		}

	}

	/**
	 * This private method is used to set the values in DebitCardPaymentInfoModel after getting successful response from
	 * Juspay. After TPR-629, payment details will be attached to order
	 *
	 * @param cart
	 * @param orderStatusResponse
	 * @return CartModel
	 */
	private AbstractOrderModel setValueInDebitCardPaymentInfo(final AbstractOrderModel cart,
			final GetOrderStatusResponse orderStatusResponse) //Changed to abstractOrderModel for TPR-629
	{
		try
		{
			//creating debitCardPaymentInfoModel
			final DebitCardPaymentInfoModel debitCardPaymentInfoModel = getModelService().create(DebitCardPaymentInfoModel.class);
			final CardResponse response = orderStatusResponse.getCardResponse();

			if (StringUtils.isNotEmpty(response.getCardReference()))
			{
				debitCardPaymentInfoModel.setCode(response.getCardReference());
			}
			else
			{
				debitCardPaymentInfoModel.setCode("DUMMY_DC_" + cart.getCode());//To Be Removed
			}
			//debitCardPaymentInfoModel.setUser(getUserService().getCurrentUser());			//Commented for TPR-629
			debitCardPaymentInfoModel.setUser(cart.getUser());
			if (StringUtils.isNotEmpty(response.getNameOnCard()))
			{
				debitCardPaymentInfoModel.setCcOwner(response.getNameOnCard());
			}
			else
			{
				debitCardPaymentInfoModel.setCcOwner(MarketplacecommerceservicesConstants.DUMMYCCOWNER);//To Be Removed
			}
			if (StringUtils.isNotEmpty(response.getCardNumber()))
			{
				debitCardPaymentInfoModel.setNumber(response.getCardNumber());
			}
			else
			{
				debitCardPaymentInfoModel.setNumber(MarketplacecommerceservicesConstants.DUMMYNUMBER);//To Be Removed
			}
			if (StringUtils.isNotEmpty(response.getExpiryMonth()))
			{
				debitCardPaymentInfoModel.setValidToMonth(response.getExpiryMonth());
			}
			else
			{
				debitCardPaymentInfoModel.setValidToMonth(MarketplacecommerceservicesConstants.DUMMYMM);//To Be Removed
			}
			if (StringUtils.isNotEmpty(response.getExpiryYear()))
			{
				debitCardPaymentInfoModel.setValidToYear(response.getExpiryYear());
			}
			else
			{
				debitCardPaymentInfoModel.setValidToYear(MarketplacecommerceservicesConstants.DUMMYYY);
			}

			if (StringUtils.isNotEmpty(response.getCardBrand()))
			{
				//OrderIssues:- CardBrand Null Check added
				if (MarketplacecommerceservicesConstants.MASTERCARD.equalsIgnoreCase(response.getCardBrand()))
				{
					debitCardPaymentInfoModel.setType(CreditCardType.MASTER);
				}
				else if (MarketplacecommerceservicesConstants.MAESTRO.equalsIgnoreCase(response.getCardBrand()))
				{
					debitCardPaymentInfoModel.setType(CreditCardType.MAESTRO);
				}
				else if ((MarketplacecommerceservicesConstants.AMEX.equalsIgnoreCase(response.getCardBrand()) || MarketplacecommerceservicesConstants.AMERICAN_EXPRESS
						.equalsIgnoreCase(orderStatusResponse.getCardResponse().getCardBrand())))
				{
					debitCardPaymentInfoModel.setType(CreditCardType.AMEX);
				}
				else if (MarketplacecommerceservicesConstants.DINERSCARD.equalsIgnoreCase(response.getCardBrand()))
				{
					debitCardPaymentInfoModel.setType(CreditCardType.DINERS);
				}
				else if (MarketplacecommerceservicesConstants.VISA.equalsIgnoreCase(response.getCardBrand()))
				{
					debitCardPaymentInfoModel.setType(CreditCardType.VISA);
				}
				else if (MarketplacecommerceservicesConstants.EUROCARD.equalsIgnoreCase(response.getCardBrand()))
				{
					debitCardPaymentInfoModel.setType(CreditCardType.MASTERCARD_EUROCARD);
				}
				else if (MarketplacecommerceservicesConstants.SWITCHCARD.equalsIgnoreCase(response.getCardBrand()))
				{
					debitCardPaymentInfoModel.setType(CreditCardType.SWITCH);
				}
				//SDI-1561
				else if (MarketplacecommerceservicesConstants.DINERS.equalsIgnoreCase(response.getCardBrand()))
				{
					debitCardPaymentInfoModel.setType(CreditCardType.DINERS);
				}
				else if (MarketplacecommerceservicesConstants.JCB.equalsIgnoreCase(response.getCardBrand()))
				{
					debitCardPaymentInfoModel.setType(CreditCardType.JCB);
				}
				else if (MarketplacecommerceservicesConstants.DISCOVER.equalsIgnoreCase(response.getCardBrand()))
				{
					debitCardPaymentInfoModel.setType(CreditCardType.DISCOVER);
				}
				else
				{
					debitCardPaymentInfoModel.setType(CreditCardType.MASTER);
				}
			}
			else
			{
				debitCardPaymentInfoModel.setType(CreditCardType.MASTER);//To Be Removed
			}
			//saving the debitcardpaymentinfomodel
			//try
			//{
			if (null == cart.getPaymentInfo() && !OrderStatus.PAYMENT_TIMEOUT.equals(cart.getStatus()))
			{
				getModelService().save(debitCardPaymentInfoModel);
				//setting paymentinfo in cart
				cart.setPaymentInfo(debitCardPaymentInfoModel);
				cart.setPaymentAddress(cart.getDeliveryAddress());
			}
			else if (null != cart.getPaymentInfo())
			{
				LOG.error("Order already has payment info -- not saving debitCardPaymentInfoModel>>>"
						+ cart.getPaymentInfo().getCode());
			}
			else
			{
				LOG.error(ERROR_PAYMENT + cart.getCode());
			}

			//}
			//catch (final ModelSavingException e)
			//{
			//	LOG.error("Exception while saving debit card payment info with " + e);
			//}



		}
		catch (final ModelSavingException e)
		{
			LOG.error(MarketplacecommerceservicesConstants.PAYMENT_EXC_LOG + e);
			//throw new ModelSavingException(e + MarketplacecommerceservicesConstants.PAYMENT_EXC_LOG_END);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		//returning the cart
		return cart;
	}

	/**
	 * This private method is used to set the values in CreditCardPaymentInfoModel after getting successful response from
	 * Juspay. After TPR-629, payment details will be attached to order
	 *
	 * @param cart
	 * @param orderStatusResponse
	 * @return CartModel
	 */
	private AbstractOrderModel setValueInCreditCardPaymentInfo(final AbstractOrderModel cart,
			final GetOrderStatusResponse orderStatusResponse) throws EtailNonBusinessExceptions //Changed to abstractOrderModel for TPR-629
	{
		try
		{
			//creating creditCardPaymentInfoModel
			final CreditCardPaymentInfoModel creditCardPaymentInfoModel = getModelService().create(CreditCardPaymentInfoModel.class);
			String checkValues = "".intern();
			String[] parts = null;
			String saveCard = "".intern();
			String sameAsShipping = "".intern();
			AddressModel address = getModelService().create(AddressModel.class);
			if (StringUtils.isNotEmpty(orderStatusResponse.getUdf10())
					&& !orderStatusResponse.getUdf10().equalsIgnoreCase(MarketplacecommerceservicesConstants.NULL))
			{
				checkValues = orderStatusResponse.getUdf10();
			}
			if (checkValues.contains(MarketplacecommerceservicesConstants.CONCTASTRING))
			{
				parts = checkValues.split(MarketplacecommerceservicesConstants.SPLITSTRING);
				saveCard = parts[0];
				sameAsShipping = parts[1];
			}

			if (StringUtils.isNotEmpty(orderStatusResponse.getUdf1())
					&& !orderStatusResponse.getUdf1().equalsIgnoreCase(MarketplacecommerceservicesConstants.NULL))
			{
				if (saveCard.equalsIgnoreCase(MarketplacecommerceservicesConstants.TRUE))
				{
					address = getAddress(orderStatusResponse, cart.getUser());
				}
				else
				{
					address = billingAddressForSavedCard(orderStatusResponse, cart, sameAsShipping);
				}
			}
			else
			{
				address = getAddress(orderStatusResponse, cart.getUser());
			}

			//Creating Dummy Address when Address is null for Credit Card
			if (null == address)
			{
				address = createDummyAddress(cart);
			}

			//for saving Billing Address against credit card

			//final AddressModel address = saveBillingAddress(orderStatusResponse, cart, sameAsShipping);

			//OrderIssues:-
			final CardResponse response = orderStatusResponse.getCardResponse();
			if (StringUtils.isNotEmpty(response.getCardReference()))
			{
				creditCardPaymentInfoModel.setCode(response.getCardReference());
			}
			else
			{
				creditCardPaymentInfoModel.setCode("DUMMY_CC_" + cart.getCode());//To Be Removed
			}
			creditCardPaymentInfoModel.setUser(getUserService().getCurrentUser());
			if (StringUtils.isNotEmpty(response.getNameOnCard()))
			{
				creditCardPaymentInfoModel.setCcOwner(response.getNameOnCard());
			}
			else
			{
				creditCardPaymentInfoModel.setCcOwner(MarketplacecommerceservicesConstants.DUMMYCCOWNER);//To Be Removed
			}
			if (StringUtils.isNotEmpty(response.getCardNumber()))
			{
				creditCardPaymentInfoModel.setNumber(response.getCardNumber());
			}
			else
			{
				creditCardPaymentInfoModel.setNumber(MarketplacecommerceservicesConstants.DUMMYNUMBER);//To Be Removed
			}
			if (StringUtils.isNotEmpty(response.getExpiryMonth()))
			{
				creditCardPaymentInfoModel.setValidToMonth(response.getExpiryMonth());
			}
			else
			{
				creditCardPaymentInfoModel.setValidToMonth(MarketplacecommerceservicesConstants.DUMMYMM);//To Be Removed
			}
			if (StringUtils.isNotEmpty(response.getExpiryYear()))
			{
				creditCardPaymentInfoModel.setValidToYear(response.getExpiryYear());
			}
			else
			{
				creditCardPaymentInfoModel.setValidToYear(MarketplacecommerceservicesConstants.DUMMYYY);
			}
			if (StringUtils.isNotEmpty(response.getCardReference()))
			{
				creditCardPaymentInfoModel.setSubscriptionId(response.getCardReference());//Card Reference
			}
			else
			{
				creditCardPaymentInfoModel.setSubscriptionId(MarketplacecommerceservicesConstants.DUMMYCARDREF);
			}

			if (StringUtils.isNotEmpty(response.getCardBrand()))
			{
				if (MarketplacecommerceservicesConstants.MASTERCARD.equalsIgnoreCase(response.getCardBrand()))
				{
					creditCardPaymentInfoModel.setType(CreditCardType.MASTER);
				}
				else if (MarketplacecommerceservicesConstants.MAESTRO.equalsIgnoreCase(response.getCardBrand()))
				{
					creditCardPaymentInfoModel.setType(CreditCardType.MAESTRO);
				}
				else if (MarketplacecommerceservicesConstants.AMEX.equalsIgnoreCase(response.getCardBrand())
						|| MarketplacecommerceservicesConstants.AMERICAN_EXPRESS.equalsIgnoreCase(response.getCardBrand()))
				{
					creditCardPaymentInfoModel.setType(CreditCardType.AMEX);
				}
				else if (MarketplacecommerceservicesConstants.DINERSCARD.equalsIgnoreCase(response.getCardBrand()))
				{
					creditCardPaymentInfoModel.setType(CreditCardType.DINERS);
				}
				else if (MarketplacecommerceservicesConstants.VISA.equalsIgnoreCase(response.getCardBrand()))
				{
					creditCardPaymentInfoModel.setType(CreditCardType.VISA);
				}
				else if (MarketplacecommerceservicesConstants.EUROCARD.equalsIgnoreCase(response.getCardBrand()))
				{
					creditCardPaymentInfoModel.setType(CreditCardType.MASTERCARD_EUROCARD);
				}
				else if (MarketplacecommerceservicesConstants.SWITCHCARD.equalsIgnoreCase(response.getCardBrand()))
				{
					creditCardPaymentInfoModel.setType(CreditCardType.SWITCH);
				}
				//SDI-1561
				else if (MarketplacecommerceservicesConstants.DINERS.equalsIgnoreCase(response.getCardBrand()))
				{
					creditCardPaymentInfoModel.setType(CreditCardType.DINERS);
				}
				else if (MarketplacecommerceservicesConstants.JCB.equalsIgnoreCase(response.getCardBrand()))
				{
					creditCardPaymentInfoModel.setType(CreditCardType.JCB);
				}
				else if (MarketplacecommerceservicesConstants.DISCOVER.equalsIgnoreCase(response.getCardBrand()))
				{
					creditCardPaymentInfoModel.setType(CreditCardType.DISCOVER);
				}
				else
				{
					creditCardPaymentInfoModel.setType(CreditCardType.MASTER);
				}
			}
			else
			{
				creditCardPaymentInfoModel.setType(CreditCardType.MASTER);//To Be Removed
			}
			//try
			//{
			if (null != address)
			{
				if (StringUtils.isEmpty(address.getPhone1()) && StringUtils.isEmpty(address.getCellphone())
						&& null != cart.getDeliveryAddress())
				{
					final AddressModel deliveryAddress = cart.getDeliveryAddress();
					if (StringUtils.isNotEmpty(deliveryAddress.getPhone1()) && StringUtils.isEmpty(address.getPhone1()))
					{
						address.setPhone1(deliveryAddress.getPhone1());
					}
					if (StringUtils.isNotEmpty(deliveryAddress.getCellphone()) && StringUtils.isEmpty(address.getCellphone()))
					{
						address.setCellphone(deliveryAddress.getCellphone());
					}
					//saving the billing address
					getModelService().save(address);
				}

				//setting the billing address against creditcardpaymentinfo
				creditCardPaymentInfoModel.setBillingAddress(address);
			}

			//saving the creditCardPaymentInfoModel
			//}
			//catch (final ModelSavingException e)
			//{
			//	LOG.error("Exception while saving credit card payment info with " + e);
			//}

			if (null == cart.getPaymentInfo() && !OrderStatus.PAYMENT_TIMEOUT.equals(cart.getStatus()))
			{
				getModelService().save(creditCardPaymentInfoModel);
				//setting paymentinfo in cart
				cart.setPaymentInfo(creditCardPaymentInfoModel);
				cart.setPaymentAddress(address);
			}
			else if (null != cart.getPaymentInfo())
			{
				LOG.error("Order already has payment info -- not saving creditCardPaymentInfoModel>>>"
						+ cart.getPaymentInfo().getCode());
			}
			else
			{
				LOG.error(ERROR_PAYMENT + cart.getCode());
			}
		}
		catch (final ModelSavingException e)
		{
			LOG.error(MarketplacecommerceservicesConstants.PAYMENT_EXC_LOG + e);
			//throw new ModelSavingException(e + MarketplacecommerceservicesConstants.PAYMENT_EXC_LOG_END);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		//returning the cart
		return cart;

	}

	/**
	 * This private method is used to set the values in EMIPaymentInfoModel after getting successful response from
	 * Juspay. After TPR-629, payment details will be attached to order
	 *
	 * @param cart
	 * @param response
	 * @return CartModel
	 */
	//Exception handled and unwanted code commented TPR-629
	private AbstractOrderModel setValueInEMIPaymentInfo(final AbstractOrderModel cart, final GetOrderStatusResponse response) //Changed to abstractOrderModel for TPR-629
	{
		try
		{
			//creating EMIPaymentInfoModel
			final EMIPaymentInfoModel emiPaymentInfoModel = getModelService().create(EMIPaymentInfoModel.class);
			AddressModel address = getModelService().create(AddressModel.class);
			String checkValues = "".intern();
			String[] parts = null;
			String saveCard = "".intern();
			String sameAsShipping = "".intern();
			if (StringUtils.isNotEmpty(response.getUdf10())
					&& !response.getUdf10().equalsIgnoreCase(MarketplacecommerceservicesConstants.NULL))
			{
				checkValues = response.getUdf10();
			}
			if (checkValues.contains(MarketplacecommerceservicesConstants.CONCTASTRING))
			{
				parts = checkValues.split(MarketplacecommerceservicesConstants.SPLITSTRING);
				saveCard = parts[0];
				sameAsShipping = parts[1];
			}

			if (StringUtils.isNotEmpty(response.getUdf1())
					&& !response.getUdf1().equalsIgnoreCase(MarketplacecommerceservicesConstants.NULL))
			{
				if (saveCard.equalsIgnoreCase(MarketplacecommerceservicesConstants.TRUE))
				{
					address = getAddress(response, cart.getUser());
				}
				else
				{
					address = billingAddressForSavedCard(response, cart, sameAsShipping);
				}
			}
			else
			{
				address = getAddress(response, cart.getUser());
			}

			//Creating Dummy Address when Address is null for Credit Card
			if (null == address)
			{
				address = createDummyAddress(cart);
			}
			//OrderIssues:-
			final CardResponse cardRes = response.getCardResponse();
			if (StringUtils.isNotEmpty(cardRes.getCardReference()))
			{
				emiPaymentInfoModel.setCode(cardRes.getCardReference());
			}
			else
			{
				emiPaymentInfoModel.setCode("DUMMY_EMI_" + cart.getCode());//To Be Removed
			}
			//emiPaymentInfoModel.setUser(getUserService().getCurrentUser());		//Commented for TPR-629
			emiPaymentInfoModel.setUser(cart.getUser());
			if (StringUtils.isNotEmpty(cardRes.getNameOnCard()))
			{
				emiPaymentInfoModel.setCcOwner(cardRes.getNameOnCard());
			}
			else
			{
				emiPaymentInfoModel.setCcOwner(MarketplacecommerceservicesConstants.DUMMYCCOWNER);//To Be Removed
			}
			if (StringUtils.isNotEmpty(cardRes.getCardNumber()))
			{
				emiPaymentInfoModel.setNumber(cardRes.getCardNumber());
			}
			else
			{
				emiPaymentInfoModel.setNumber(MarketplacecommerceservicesConstants.DUMMYNUMBER);//To Be Removed
			}
			if (StringUtils.isNotEmpty(cardRes.getExpiryMonth()))
			{
				emiPaymentInfoModel.setValidToMonth(cardRes.getExpiryMonth());
			}
			else
			{
				emiPaymentInfoModel.setValidToMonth(MarketplacecommerceservicesConstants.DUMMYMM);//To Be Removed
			}
			if (StringUtils.isNotEmpty(cardRes.getExpiryYear()))
			{
				emiPaymentInfoModel.setValidToYear(cardRes.getExpiryYear());
			}
			else
			{
				emiPaymentInfoModel.setValidToYear(MarketplacecommerceservicesConstants.DUMMYYY);
			}
			if (StringUtils.isNotEmpty(cardRes.getCardReference()))
			{
				emiPaymentInfoModel.setSubscriptionId(cardRes.getCardReference());
			}
			else
			{
				emiPaymentInfoModel.setSubscriptionId(MarketplacecommerceservicesConstants.DUMMYCARDREF);
			}

			//EMI bank and tenure setup
			//try
			//{
			if (StringUtils.isNotEmpty(response.getBankEmi()))
			{
				EMIBankModel emiBankModel = modelService.create(EMIBankModel.class);
				emiBankModel.setCode(response.getBankEmi());
				emiBankModel = flexibleSearchService.getModelByExample(emiBankModel);
				emiPaymentInfoModel.setBankSelected(emiBankModel);
			}
			//}
			//catch (final ModelNotFoundException e)
			//{
			//	LOG.error("EMIBank Model not found", e);
			//	throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0008);
			//}
			if (StringUtils.isNotEmpty(response.getBankTenure()))
			{
				//emiPaymentInfoModel.setTermSelected(response.getBankEmi());
				emiPaymentInfoModel.setTermSelected(response.getBankTenure());
			}

			//TODO:Add EMI related values from response to emiPaymentInfoModel
			if (StringUtils.isNotEmpty(cardRes.getCardBrand()))
			{
				if (MarketplacecommerceservicesConstants.MASTERCARD.equalsIgnoreCase(cardRes.getCardBrand()))
				{
					emiPaymentInfoModel.setType(CreditCardType.MASTER);
				}
				else if (MarketplacecommerceservicesConstants.MAESTRO.equalsIgnoreCase(cardRes.getCardBrand()))
				{
					emiPaymentInfoModel.setType(CreditCardType.MAESTRO);
				}
				else if (MarketplacecommerceservicesConstants.AMEX.equalsIgnoreCase(cardRes.getCardBrand())
						|| MarketplacecommerceservicesConstants.AMERICAN_EXPRESS.equalsIgnoreCase(cardRes.getCardBrand()))
				{
					emiPaymentInfoModel.setType(CreditCardType.AMEX);
				}
				else if (MarketplacecommerceservicesConstants.DINERSCARD.equalsIgnoreCase(cardRes.getCardBrand()))
				{
					emiPaymentInfoModel.setType(CreditCardType.DINERS);
				}
				else if (MarketplacecommerceservicesConstants.VISA.equalsIgnoreCase(cardRes.getCardBrand()))
				{
					emiPaymentInfoModel.setType(CreditCardType.VISA);
				}
				else if (MarketplacecommerceservicesConstants.EUROCARD.equalsIgnoreCase(cardRes.getCardBrand()))
				{
					emiPaymentInfoModel.setType(CreditCardType.MASTERCARD_EUROCARD);
				}
				else if (MarketplacecommerceservicesConstants.SWITCHCARD.equalsIgnoreCase(cardRes.getCardBrand()))
				{
					emiPaymentInfoModel.setType(CreditCardType.SWITCH);
				}
				//SDI-1561
				else if (MarketplacecommerceservicesConstants.DINERS.equalsIgnoreCase(cardRes.getCardBrand()))
				{
					emiPaymentInfoModel.setType(CreditCardType.DINERS);
				}
				else if (MarketplacecommerceservicesConstants.JCB.equalsIgnoreCase(cardRes.getCardBrand()))
				{
					emiPaymentInfoModel.setType(CreditCardType.JCB);
				}
				else if (MarketplacecommerceservicesConstants.DISCOVER.equalsIgnoreCase(cardRes.getCardBrand()))
				{
					emiPaymentInfoModel.setType(CreditCardType.DISCOVER);
				}
				else
				{
					emiPaymentInfoModel.setType(CreditCardType.MASTER);
				}
			}
			else
			{
				emiPaymentInfoModel.setType(CreditCardType.MASTER);//To Be Removed
			}
			//try
			//{
			if (null != address)
			{
				if (StringUtils.isEmpty(address.getPhone1()) && StringUtils.isEmpty(address.getCellphone())
						&& null != cart.getDeliveryAddress())
				{
					final AddressModel deliveryAddress = cart.getDeliveryAddress();
					if (StringUtils.isNotEmpty(deliveryAddress.getPhone1()) && StringUtils.isEmpty(address.getPhone1()))
					{
						address.setPhone1(deliveryAddress.getPhone1());
					}
					if (StringUtils.isNotEmpty(deliveryAddress.getCellphone()) && StringUtils.isEmpty(address.getCellphone()))
					{
						address.setCellphone(deliveryAddress.getCellphone());
					}
					//saving the billing address
					getModelService().save(address);
				}

				//setting the billing address against emiPaymentInfoModel
				emiPaymentInfoModel.setBillingAddress(address);
			}

			if (null == cart.getPaymentInfo() && !OrderStatus.PAYMENT_TIMEOUT.equals(cart.getStatus()))
			{
				//saving the emiPaymentInfoModel
				getModelService().save(emiPaymentInfoModel);
				//}
				//catch (final ModelSavingException e)
				//{
				//	LOG.error("Exception while saving emi payment info with " + e);
				//}
				//setting paymentinfo in cart
				cart.setPaymentInfo(emiPaymentInfoModel);
				cart.setPaymentAddress(address);
			}
			else if (null != cart.getPaymentInfo())
			{
				LOG.error("Order already has payment info -- not saving emiPaymentInfoModel>>>" + cart.getPaymentInfo().getCode());
			}
			else
			{
				LOG.error(ERROR_PAYMENT + cart.getCode());
			}

		}
		catch (final ModelNotFoundException e)
		{
			LOG.error("EMIBank Model not found", e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0008);
		}
		catch (final ModelSavingException e)
		{
			LOG.error("Exception while saving emi payment info with " + e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		//returning the cart
		return cart;

	}

	/**
	 * This private method is used to set the values in NetbankingPaymentInfoModel after getting successful response from
	 * Juspay. After TPR-629, payment details will be attached to order
	 *
	 * @param cart
	 * @param response
	 * @return CartModel
	 */
	private AbstractOrderModel setValueInNetbankingPaymentInfo(final AbstractOrderModel cart, final GetOrderStatusResponse response) //Changed to abstractOrderModel for TPR-629
	{
		try
		{
			//creating NetbankingPaymentInfoModel
			final NetbankingPaymentInfoModel nbPaymentInfoModel = getModelService().create(NetbankingPaymentInfoModel.class);

			if (StringUtils.isNotEmpty(cart.getGuid()))
			{
				nbPaymentInfoModel.setCode("NB_" + cart.getGuid());//TODO::Setting like this until it is finalized
			}
			else
			{
				//nbPaymentInfoModel.setCode("DUMMY_NB_" + cart.getGuid());		//Erroneous code fixed
				nbPaymentInfoModel.setCode("DUMMY_NB_" + System.currentTimeMillis());
			}

			//if (null != getUserService().getCurrentUser())		//Commented for TPR-629
			//			{
			//				nbPaymentInfoModel.setUser(getUserService().getCurrentUser());
			//				if (StringUtils.isNotEmpty(getUserService().getCurrentUser().getName()))
			//				{
			//					nbPaymentInfoModel.setBankOwner(getUserService().getCurrentUser().getName());
			//				}
			//				else
			//				{
			//					nbPaymentInfoModel.setBankOwner(MarketplacecommerceservicesConstants.DUMMYCCOWNER);
			//				}
			//			}

			final UserModel user = cart.getUser();
			if (null != user)
			{
				nbPaymentInfoModel.setUser(user);
				if (StringUtils.isNotEmpty(user.getName()))
				{
					nbPaymentInfoModel.setBankOwner(user.getName());
				}
				else
				{
					nbPaymentInfoModel.setBankOwner(MarketplacecommerceservicesConstants.DUMMYCCOWNER);
				}
			}

			if (StringUtils.isNotEmpty(response.getUdf1()))
			{
				BankforNetbankingModel nbBankModel = getModelService().create(BankforNetbankingModel.class);
				nbBankModel.setNbCode(response.getUdf1());
				nbBankModel = getFlexibleSearchService().getModelByExample(nbBankModel);
				nbPaymentInfoModel.setBank(nbBankModel);
			}

			if (null == cart.getPaymentInfo() && !OrderStatus.PAYMENT_TIMEOUT.equals(cart.getStatus()))
			{
				//saving the nbPaymentInfoModel
				getModelService().save(nbPaymentInfoModel);

				//setting paymentinfo in cart
				cart.setPaymentInfo(nbPaymentInfoModel);
				cart.setPaymentAddress(cart.getDeliveryAddress());
			}
			else if (null != cart.getPaymentInfo())
			{
				LOG.error("Order already has payment info -- not saving nbPaymentInfoModel>>>" + cart.getPaymentInfo().getCode());
			}
			else
			{
				LOG.error(ERROR_PAYMENT + cart.getCode());
			}

		}
		//		catch (final ModelSavingException e)
		//		{
		//			LOG.error("Exception while saving netbanking payment info with " + e);
		//		}
		catch (final ModelSavingException e)
		{
			LOG.error("Exception while saving netbanking payment info with " + e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		//returning the cart
		return cart;
	}

	/**
	 * This method helps to save juspay Payment info into database
	 *
	 * @param custName
	 * @param cartValue
	 * @param totalCharge
	 * @param entries
	 * @throws EtailNonBusinessExceptions
	 *
	 */

	@Override
	public void saveJusPayPaymentInfo(final String custName, final Double cartValue, final Double totalCharge,
			final List<AbstractOrderEntryModel> entries, final AbstractOrderModel abstractOrderModel)
			throws EtailNonBusinessExceptions
	{

		if (null != entries)
		{
			double totalPrice = 0;
			for (final AbstractOrderEntryModel entry : entries)
			{
				if (!getDiscountUtility().isFreebieOrBOGOApplied(entry))
				{
					if (entry.getNetAmountAfterAllDisc().doubleValue() > 0)
					{
						totalPrice += entry.getNetAmountAfterAllDisc().doubleValue();
					}
					else
					{
						totalPrice += entry.getTotalPrice().doubleValue();
					}

					totalPrice -= 0.01 * (null != entry.getFreeCount() ? entry.getFreeCount().intValue() : 0);
				}
			}
			LOG.debug("Total cart price is>>>>>>>>>" + totalPrice);

			//amtTobeDeductedAtlineItemLevel is a variable to check total apportioned COD charge is equal to total convenience charge

			for (final AbstractOrderEntryModel entry : entries)
			{
				if (!getDiscountUtility().isFreebieOrBOGOApplied(entry))
				{
					double entryTotals = 0;
					if (entry.getNetAmountAfterAllDisc().doubleValue() > 0)
					{
						entryTotals = entry.getNetAmountAfterAllDisc().doubleValue();
					}

					else
					{

						entryTotals = entry.getTotalPrice().doubleValue();
					}
					entryTotals -= (null == entry.getFreeCount() ? 0 : 0.01 * entry.getFreeCount().intValue());
					final double quantity = (entry.getQualifyingCount().intValue() > 0) ? entry.getQualifyingCount().doubleValue()
							: entry.getQuantity().doubleValue();

					LOG.debug("Entry totals is>>>>>" + entryTotals + "<<<<<<&& quantity is>>>>>" + quantity);

					//calculating ratio of convenience charge for cart entry
					final Double jusPayChargePercent = Double.valueOf(entryTotals / totalPrice);
					final Double jusPayChargePerEntry = Double.valueOf(totalCharge.doubleValue() * jusPayChargePercent.doubleValue());
					final Double formattedJusPayCharge = Double.valueOf(String.format(MarketplacecommerceservicesConstants.FORMAT,
							jusPayChargePerEntry));
					double appJusPayChargeForEachItem = 0.00D;
					if (quantity > 0)
					{
						appJusPayChargeForEachItem = formattedJusPayCharge.doubleValue() / quantity;
					}
					LOG.debug("Entry level Conv charge is>>>>>>>" + appJusPayChargeForEachItem);
					entry.setConvenienceChargeApportion(Double.valueOf(appJusPayChargeForEachItem));

					try
					{
						getModelService().save(entry);
					}
					catch (final ModelSavingException e)
					{
						LOG.error("Exception while saving abstract order entry model with " + e);
						throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E9051);
					}
				}

			}
		}


		final JusPayPaymentInfoModel jusPayPaymentInfoModel = getModelService().create(JusPayPaymentInfoModel.class);


		jusPayPaymentInfoModel.setCashOwner(custName);
		jusPayPaymentInfoModel.setCode(MarketplacecommerceservicesConstants.JUSPAY + "_" + abstractOrderModel.getCode());
		jusPayPaymentInfoModel.setUser(getUserService().getCurrentUser());
		if (null == abstractOrderModel.getPaymentInfo() && !OrderStatus.PAYMENT_TIMEOUT.equals(abstractOrderModel.getStatus()))
		{
			try
			{
				getModelService().save(jusPayPaymentInfoModel);
			}
			catch (final ModelSavingException e)
			{
				//LOG.error("Exception while saving cod payment info with " + e);
				LOG.error("juspay Payment juspayPaymentInfoModel set for cart with GUID" + abstractOrderModel.getGuid());
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E9051);

			}

			//setting juspayPaymentInfoModel in cartmodel
			abstractOrderModel.setPaymentInfo(jusPayPaymentInfoModel);
			abstractOrderModel.setPaymentAddress(abstractOrderModel.getDeliveryAddress());
			try
			{
				//saving the cartmodel
				getModelService().save(abstractOrderModel);
				if (null != abstractOrderModel.getGuid())
				{
					LOG.error("juspay Payment abstractOrderModel set for cart with GUID" + abstractOrderModel.getGuid());
				}

			}
			catch (final ModelSavingException e)
			{
				if (null != abstractOrderModel.getGuid())
				{
					LOG.error("Exception while saving cart for" + abstractOrderModel.getGuid());
				}

				LOG.error("Exception while saving cart with ", e);
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E9051);
			}
		}
		else if (null != abstractOrderModel.getPaymentInfo())
		{
			LOG.error("Order already has payment info -- not saving juspayPaymentInfoModel and not attaching to abstractOrderModel>>>"
					+ abstractOrderModel.getPaymentInfo().getCode());
		}
		else
		{
			LOG.error(ERROR_PAYMENT + abstractOrderModel.getCode());
		}

	}

	/**
	 * This method helps to save COD Payment info into database
	 *
	 * @param custName
	 * @param cartValue
	 * @param totalCODCharge
	 * @param entries
	 * @throws EtailNonBusinessExceptions
	 *
	 */
	@Override
	public void saveCODPaymentInfo(final String custName, final Double cartValue, final Double totalCODCharge,
			final List<AbstractOrderEntryModel> entries, final AbstractOrderModel abstractOrderModel) //Parameter AbstractOrderModel added extra for TPR-629
			throws EtailNonBusinessExceptions
	{
		if (null != entries)
		{
			double totalPrice = 0;
			for (final AbstractOrderEntryModel entry : entries)
			{
				if (!getDiscountUtility().isFreebieOrBOGOApplied(entry))
				{
					if (entry.getNetAmountAfterAllDisc().doubleValue() > 0)
					{
						totalPrice += entry.getNetAmountAfterAllDisc().doubleValue();
					}
					else
					{
						totalPrice += entry.getTotalPrice().doubleValue();
					}

					totalPrice -= 0.01 * (null != entry.getFreeCount() ? entry.getFreeCount().intValue() : 0);
				}
			}
			LOG.debug("Total cart price is>>>>>>>>>" + totalPrice);

			//amtTobeDeductedAtlineItemLevel is a variable to check total apportioned COD charge is equal to total convenience charge

			for (final AbstractOrderEntryModel entry : entries)
			{
				if (!getDiscountUtility().isFreebieOrBOGOApplied(entry))
				{
					double entryTotals = 0;
					if (entry.getNetAmountAfterAllDisc().doubleValue() > 0)
					{
						entryTotals = entry.getNetAmountAfterAllDisc().doubleValue();
					}

					else
					{

						entryTotals = entry.getTotalPrice().doubleValue();
					}
					entryTotals -= (null == entry.getFreeCount() ? 0 : 0.01 * entry.getFreeCount().intValue());
					final double quantity = (entry.getQualifyingCount().intValue() > 0) ? entry.getQualifyingCount().doubleValue()
							: entry.getQuantity().doubleValue();

					LOG.debug("Entry totals is>>>>>" + entryTotals + "<<<<<<&& quantity is>>>>>" + quantity);

					//calculating ratio of convenience charge for cart entry
					final Double codChargePercent = Double.valueOf(entryTotals / totalPrice);
					final Double codChargePerEntry = Double.valueOf(totalCODCharge.doubleValue() * codChargePercent.doubleValue());
					final Double formattedCODCharge = Double.valueOf(String.format(MarketplacecommerceservicesConstants.FORMAT,
							codChargePerEntry));
					double appCODChargeForEachItem = 0.00D;
					if (quantity > 0)
					{
						appCODChargeForEachItem = formattedCODCharge.doubleValue() / quantity;
					}
					LOG.debug("Entry level Conv charge is>>>>>>>" + appCODChargeForEachItem);
					entry.setConvenienceChargeApportion(Double.valueOf(appCODChargeForEachItem));

					try
					{
						getModelService().save(entry);
					}
					catch (final ModelSavingException e)
					{
						LOG.error("Exception while saving abstract order entry model with " + e);
						throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E9051);
					}
				}

			}
		}

		//creating CODPaymentInfoModel
		final CODPaymentInfoModel cODPaymentInfoModel = getModelService().create(CODPaymentInfoModel.class);

		//setting values in CODPaymentInfoModel
		cODPaymentInfoModel.setCashOwner(custName);
		cODPaymentInfoModel.setCode(MarketplacecommerceservicesConstants.COD + "_" + abstractOrderModel.getCode());
		cODPaymentInfoModel.setUser(getUserService().getCurrentUser());
		if (null == abstractOrderModel.getPaymentInfo() && !OrderStatus.PAYMENT_TIMEOUT.equals(abstractOrderModel.getStatus()))
		{
			try
			{

				//saving CODPaymentInfoModel
				getModelService().save(cODPaymentInfoModel);
			}
			catch (final ModelSavingException e)
			{
				//LOG.error("Exception while saving cod payment info with " + e);
				LOG.error("COD Payment cODPaymentInfoModel set for cart with GUID" + abstractOrderModel.getGuid());
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E9051);

			}

			//setting CODPaymentInfoModel in cartmodel
			abstractOrderModel.setPaymentInfo(cODPaymentInfoModel);
			abstractOrderModel.setPaymentAddress(abstractOrderModel.getDeliveryAddress());
			try
			{
				//saving the cartmodel
				getModelService().save(abstractOrderModel);
				//TIS-3168
				if (null != abstractOrderModel.getGuid())
				{
					LOG.error("COD Payment abstractOrderModel set for cart with GUID" + abstractOrderModel.getGuid());
				}

			}
			catch (final ModelSavingException e)
			{
				//TIS-3168
				if (null != abstractOrderModel.getGuid())
				{
					LOG.error("Exception while saving cart for" + abstractOrderModel.getGuid());
				}

				LOG.error("Inside saveCODPaymentInfo::Exception while saving cart with ", e); //Sonar fix
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E9051);
			}
		}
		else if (null != abstractOrderModel.getPaymentInfo())
		{
			LOG.error("Order already has payment info -- not saving cODPaymentInfoModel and not attaching to abstractOrderModel>>>"
					+ abstractOrderModel.getPaymentInfo().getCode());
		}
		else
		{
			LOG.error(ERROR_PAYMENT + abstractOrderModel.getCode());
		}
	}

	/**
	 * This method is used set the saved card details in SavedCardModel
	 *
	 * @param user
	 * @param response
	 * @param address
	 */
	private void setInSavedCard(final GetOrderStatusResponse response, final AddressModel address, final UserModel user)
	{
		try
		{
			//getting the current customer to fetch customer Id and customer email
			//final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();			//Commented after adding parameter for TPR-629
			final CustomerModel customer = (CustomerModel) user;
			final Collection<SavedCardModel> savedCardForCustomer = customer.getSavedCard();
			final ArrayList<SavedCardModel> savedCardList = new ArrayList<SavedCardModel>();

			if (null != savedCardForCustomer && !(savedCardForCustomer.isEmpty()))
			{
				boolean flag = false;
				savedCardList.addAll(savedCardForCustomer);
				for (final SavedCardModel savedCard : savedCardList)
				{
					if (null != response.getCardResponse() && StringUtils.isNotEmpty(response.getCardResponse().getCardReference())
							&& response.getCardResponse().getCardReference().equalsIgnoreCase(savedCard.getCardReferenceNumber()))
					{
						savedCard.setBillingAddress(address);
						getModelService().save(savedCard);
						flag = true;
						break;
					}
				}
				if (!flag)
				{
					final SavedCardModel saveNewCard = new SavedCardModel();
					saveNewCard.setCardReferenceNumber(response.getCardResponse().getCardReference());
					saveNewCard.setCardBinNumber(response.getCardResponse().getCardISIN());//TPR-7486
					saveNewCard.setBillingAddress(address);
					getModelService().save(saveNewCard);
					savedCardList.add(saveNewCard);
					customer.setSavedCard(savedCardList);
				}
			}
			else
			{
				final Collection<SavedCardModel> saveNewCardList = new ArrayList<SavedCardModel>();
				final SavedCardModel saveNewCard = getModelService().create(SavedCardModel.class);
				saveNewCard.setCardReferenceNumber(response.getCardResponse().getCardReference());
				saveNewCard.setCardBinNumber(response.getCardResponse().getCardISIN()); //TPR-7486
				saveNewCard.setBillingAddress(address);
				getModelService().save(saveNewCard);
				saveNewCardList.add(saveNewCard);
				customer.setSavedCard(saveNewCardList);
			}
			getModelService().save(customer);
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

	}



	/**
	 * This method is used set the saved card details in SavedCardModel
	 *
	 * @param user
	 * @param response
	 */
	private void setInSavedDebitCard(final GetOrderStatusResponse response, final UserModel user)
	{
		try
		{
			//getting the current customer to fetch customer Id and customer email
			//final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();		//Commeted as parameter added for TPR-629
			final CustomerModel customer = (CustomerModel) user;
			final Collection<SavedCardModel> savedCardForCustomer = customer.getSavedCard();
			final ArrayList<SavedCardModel> savedCardList = new ArrayList<SavedCardModel>();

			if (null != savedCardForCustomer && !(savedCardForCustomer.isEmpty()))
			{
				boolean flag = false;
				savedCardList.addAll(savedCardForCustomer);
				for (final SavedCardModel savedCard : savedCardList)
				{
					if (null != response.getCardResponse() && StringUtils.isNotEmpty(response.getCardResponse().getCardReference())
							&& response.getCardResponse().getCardReference().equalsIgnoreCase(savedCard.getCardReferenceNumber()))
					{
						flag = true;
						break;
					}
				}
				if (!flag)
				{
					final SavedCardModel saveNewCard = new SavedCardModel();
					saveNewCard.setCardReferenceNumber(response.getCardResponse().getCardReference());
					saveNewCard.setCardBinNumber(response.getCardResponse().getCardISIN()); //TPR-7486
					getModelService().save(saveNewCard);
					savedCardList.add(saveNewCard);
					customer.setSavedCard(savedCardList);
				}
			}
			else
			{
				final Collection<SavedCardModel> saveNewCardList = new ArrayList<SavedCardModel>();
				final SavedCardModel saveNewCard = getModelService().create(SavedCardModel.class);
				saveNewCard.setCardReferenceNumber(response.getCardResponse().getCardReference());
				saveNewCard.setCardBinNumber(response.getCardResponse().getCardISIN()); //TPR-7486
				getModelService().save(saveNewCard);
				saveNewCardList.add(saveNewCard);
				customer.setSavedCard(saveNewCardList);
			}
			getModelService().save(customer);
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * This method helps to save apportion for the PaymentModes which were successful
	 *
	 * @param abstractOrderModel
	 *
	 */
	//Try catch fixed and save all implemented instead of save
	@Override
	public void paymentModeApportion(final AbstractOrderModel abstractOrderModel) //Changed to abstractOrderModel for TPR-629
	{
		try
		{
			final List<AbstractOrderEntryModel> entries = abstractOrderModel.getEntries();
			final List<PaymentTransactionModel> paymentTransactionList = abstractOrderModel.getPaymentTransactions();
			final List<PaymentModeApportionModel> paymentModeApportion = new ArrayList<PaymentModeApportionModel>();
			final PaymentModeApportionModel paymentModeApportionModel = getModelService().create(PaymentModeApportionModel.class);
			if (null != entries)
			{
				double percentTobeDeducted = MarketplacecommerceservicesConstants.AMOUNTTOBEDEDUCTED;

				//Looping through the entries of paymentTransactionList
				if (null != paymentTransactionList)
				{
					for (final PaymentTransactionModel transaction : paymentTransactionList)
					{
						//Only when the status of PaymentTransactionModel is "success"
						if (transaction.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
						{
							final List<PaymentTransactionEntryModel> paymentTransactionEntryList = transaction.getEntries();
							int entrysize = paymentTransactionEntryList.size();

							//Looping through the entries of paymentTransactionEntryList to fetch the mode and amount paid against each mode
							for (final PaymentTransactionEntryModel tranasctionEntry : paymentTransactionEntryList)
							{
								//Calling DAO method to fetch the PaymentTypeModel where mode is same as the mode in PaymentTransactionEntryModel
								final PaymentTypeModel paymentType = tranasctionEntry.getPaymentMode();

								//Checking if entrysize is greater than 1. If so, then normal percentage process will be applied. Else the remaining percent will be calculated by subtracting from 100
								if (entrysize > 1)
								{
									final Double totalAmount = abstractOrderModel.getTotalPriceWithConv();
									final Double modeAmount = Double.valueOf(tranasctionEntry.getAmount().doubleValue());
									final Double percentApportion = Double.valueOf((modeAmount.doubleValue() / totalAmount.doubleValue())
											* MarketplacecommerceservicesConstants.PERCENTVALUE);
									final Double formattedPercentApportion = Double.valueOf(String.format(
											MarketplacecommerceservicesConstants.FORMAT, percentApportion));
									paymentModeApportionModel.setPaymentMode(paymentType);
									paymentModeApportionModel.setApportionPercent(formattedPercentApportion);
									percentTobeDeducted += formattedPercentApportion.doubleValue();
									//try
									//{
									getModelService().save(paymentModeApportionModel);
									//}
									//catch (final ModelSavingException e)
									//{
									//	LOG.error("Exception while saving payment mode apportion model with " + e);
									//}
									entrysize--;
								}
								else
								{
									final Double percentApportion = Double.valueOf(MarketplacecommerceservicesConstants.PERCENTVALUE
											- percentTobeDeducted);
									final Double formattedPercentApportion = Double.valueOf(String.format(
											MarketplacecommerceservicesConstants.FORMAT, percentApportion));
									paymentModeApportionModel.setPaymentMode(paymentType);
									paymentModeApportionModel.setApportionPercent(formattedPercentApportion);
									//try
									//{
									getModelService().save(paymentModeApportionModel);
									//}
									//catch (final ModelSavingException e)
									//{
									//	LOG.error("Exception while saving payment mode apportion model with " + e);
									//}
								}
								paymentModeApportion.add(paymentModeApportionModel);
							}
						}
					}

					//Setting the List<PaymentModeApportionModel> against the cart entries
					for (final AbstractOrderEntryModel entry : entries)
					{
						entry.setPaymentModeApportion(paymentModeApportion);
						//try
						//{
						//getModelService().save(entry);
						//}
						//catch (final ModelSavingException e)
						//{
						//	LOG.error("Exception while saving abstract order entry model with " + e);
						//}
					}
					getModelService().saveAll(entries);
				}
			}
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	//Commented as not used after TPR-629
	//	/**
	//	 * This method is used to check whether a Juspay order Id is present in PaymentTransactionModel in cart with status
	//	 * success
	//	 *
	//	 * @param juspayOrderId
	//	 * @param mplCustomerID
	//	 * @return PaymentTransactionModel
	//	 */
	//	@Override
	//	public PaymentTransactionModel getOrderStatusFromCart(final String juspayOrderId, final String mplCustomerID)
	//	{
	//		PaymentTransactionModel paymentTransaction = null;
	//		if (null != getMplPaymentDao().getOrderStatusFromCart(juspayOrderId, mplCustomerID))
	//		{
	//			paymentTransaction = getMplPaymentDao().getOrderStatusFromCart(juspayOrderId, mplCustomerID);
	//			//return paymentTransaction;	SONAR Fix
	//		}
	//		//		else
	//		//		{
	//		//			return null;	SONAR Fix
	//		//		}
	//
	//		return paymentTransaction;
	//	}


	/**
	 * This method is used to get the List of Countries
	 *
	 * @return List<String>
	 */
	@Override
	public List<String> getCountries()
	{
		final List<CountryModel> countryList = getMplPaymentDao().getCountries();
		List<String> countryNameList = new ArrayList<String>();
		if (null != countryList)
		{
			for (final CountryModel country : countryList)
			{
				countryNameList.add(country.getName());
			}
			Collections.sort(countryNameList);
			//return countryNameList;	SONAR Fix
		}
		else
		{
			//return null;	SONAR Fix
			countryNameList = null;
		}

		return countryNameList;
	}



	/**
	 * This method applies promotion in the payment page
	 *
	 * @param cartData
	 * @param orderData
	 * @param cartModel
	 * @param orderModel
	 * @return MplPromoPriceData
	 *
	 * @throws JaloPriceFactoryException
	 * @throws JaloSecurityException
	 * @throws CalculationException
	 * @throws VoucherOperationException
	 * @throws JaloInvalidParameterException
	 * @throws NumberFormatException
	 * @throws ModelSavingException
	 * @throws EtailNonBusinessExceptions
	 *
	 */
	@Override
	public MplPromoPriceData applyPromotions(final CartData cartData, final OrderData orderData, final CartModel cartModel,
			OrderModel orderModel, final MplPromoPriceData promoPriceData)
			throws ModelSavingException, NumberFormatException, JaloInvalidParameterException, VoucherOperationException,
			CalculationException, JaloSecurityException, JaloPriceFactoryException, EtailNonBusinessExceptions //Additional parameters added for TPR-629
	{
		final long startTime = System.currentTimeMillis();
		//final MplPromoPriceData promoPriceData = new MplPromoPriceData();
		VoucherDiscountData discData = new VoucherDiscountData();
		if (null != cartModel)
		{
			//Reset Voucher Apportion
			if (CollectionUtils.isNotEmpty(cartModel.getDiscounts()) && null != cartModel.getDiscounts().get(0)) //IQA for TPR-629
			{
				final List<AbstractOrderEntryModel> entryList = getMplVoucherService().getOrderEntryModelFromVouEntries(
						(VoucherModel) cartModel.getDiscounts().get(0), cartModel); //Since only 1 voucher is applied to the cart and
				//before promotion calculation only 1 discount will be present

				if (CollectionUtils.isNotEmpty(entryList)) //IQA for TPR-629
				{
					for (final AbstractOrderEntryModel entry : entryList)

					{
						entry.setCouponCode("");
						entry.setCouponValue(Double.valueOf(0.00D));

						//TPR-7408 starts here
						entry.setCouponCostCentreOnePercentage(Double.valueOf(0.00D));
						entry.setCouponCostCentreTwoPercentage(Double.valueOf(0.00D));
						entry.setCouponCostCentreThreePercentage(Double.valueOf(0.00D));
						//TPR-7408 ends here

						//getModelService().save(entry);
					}
					getModelService().saveAll(entryList);
				}
			}


			calculatePromotion(cartModel, null, cartData, null);

			final String bankName = getSessionService().getAttribute(MarketplacecommerceservicesConstants.BANKFROMBIN);
			final String paymentMode = getSessionService()
					.getAttribute(MarketplacecommerceservicesConstants.PAYMENTMODEFORPROMOTION);
			//getSessionService().removeAttribute(MarketplacecommerceservicesConstants.BANKFROMBIN); commented for SDI-2154/SDI-2155/SDI-2157

			if (StringUtils.isNotEmpty(paymentMode) && paymentMode.equalsIgnoreCase("EMI") && StringUtils.isNotEmpty(bankName))

			{

				LOG.debug(">> Apply promotion >> Inside EMI Bank Name : " + bankName);
				final List<EMIBankModel> emiBankList = getMplPaymentDao().getEMIBanks(cartModel.getTotalPriceWithConv(), bankName);

				if (CollectionUtils.isEmpty(emiBankList))
				{
					calculatePromotion(cartModel, null, cartData, null);
					promoPriceData.setErrorMsgForEMI(getConfigurationService().getConfiguration().getString(
							MarketplacecommerceservicesConstants.PAYMENT_EMI_PROMOERROR));
				}
			}

			//Checking if the cart has coupon already applied
			if (CollectionUtils.isNotEmpty(cartModel.getDiscounts())
					&& cartModel.getDiscounts().get(0) instanceof PromotionVoucherModel)
			{
				LOG.debug(">> 2 : Checking voucher related promotion >> ");
				final PromotionVoucherModel voucher = (PromotionVoucherModel) cartModel.getDiscounts().get(0);
				final List<AbstractOrderEntryModel> applicableOrderEntryList = getMplVoucherService()
						.getOrderEntryModelFromVouEntries(voucher, cartModel);
				discData = getMplVoucherService().checkCartAfterApply(voucher, cartModel, null, applicableOrderEntryList);
				getMplVoucherService().setApportionedValueForVoucher(voucher, cartModel, voucher.getVoucherCode(),
						applicableOrderEntryList);
				getMplCommerceCartService().setTotalWithConvCharge(cartModel, cartData);

			}
			//Removing the session if the session is not empty
			if (null != getSessionService().getAttribute(MarketplacecommerceservicesConstants.PAYMENTMODEFORPROMOTION))
			{
				getSessionService().removeAttribute(MarketplacecommerceservicesConstants.PAYMENTMODEFORPROMOTION);
			}
			final List<MplPromotionData> responseDataList = getAppliedPromotionDetails(cartModel, cartData);
			if (CollectionUtils.isNotEmpty(responseDataList))
			{
				promoPriceData.setMplPromo(responseDataList);
			}
			promoPriceData.setTotalDiscount(calculateTotalDiscount(cartModel));
			//Populating Currency and Total Price Details
			if (null != cartModel.getCurrency() && StringUtils.isNotEmpty(cartModel.getCurrency().getSymbol()) && null != cartData)
			{
				promoPriceData.setCurrency(cartModel.getCurrency().getSymbol());
				promoPriceData.setTotalPrice(cartData.getTotalPriceWithConvCharge());
				promoPriceData.setConvCharge(cartData.getConvenienceChargeForCOD());
				final PriceData totalvalExcConv = discountUtility.createPrice(cartModel, Double.valueOf(((cartData
						.getTotalPriceWithConvCharge().getValue()).subtract(cartData.getConvenienceChargeForCOD().getValue()))
						.toString()));

				if (null != totalvalExcConv && null != totalvalExcConv.getFormattedValue())
				{
					promoPriceData.setTotalExcConv(totalvalExcConv);
				}
				promoPriceData.setDeliveryCost(cartData.getDeliveryCost());
			}
			promoPriceData.setVoucherDiscount(discData);
		}

		//Logic when order is there for customers
		else if (null != orderModel)
		{
		   calculatePromotion(null, orderModel, null, orderData);

			final String bankName = getSessionService().getAttribute(MarketplacecommerceservicesConstants.BANKFROMBIN);
			final String paymentMode = getSessionService()
					.getAttribute(MarketplacecommerceservicesConstants.PAYMENTMODEFORPROMOTION);
			//getSessionService().removeAttribute(MarketplacecommerceservicesConstants.BANKFROMBIN);commented for SDI-2154/SDI-2155/SDI-2157

			if (StringUtils.isNotEmpty(paymentMode) && paymentMode.equalsIgnoreCase("EMI") && StringUtils.isNotEmpty(bankName))
			{
				LOG.debug(">> Apply promotion >> Inside EMI Bank Name : " + bankName);
				final List<EMIBankModel> emiBankList = getMplPaymentDao().getEMIBanks(orderModel.getTotalPriceWithConv(), bankName);

				if (CollectionUtils.isEmpty(emiBankList))
				{
					calculatePromotion(null, orderModel, null, orderData);
					promoPriceData.setErrorMsgForEMI(getConfigurationService().getConfiguration().getString(
							MarketplacecommerceservicesConstants.PAYMENT_EMI_PROMOERROR));
				}
			}

			//Checking if the cart has coupon already applied
			if (CollectionUtils.isNotEmpty(orderModel.getDiscounts()))
			{
				orderModel = (OrderModel) getMplVoucherService().modifyDiscountValues(orderModel);
				final Double totalPrice = getMplVoucherService().setTotalPrice(orderModel);
				if (null != totalPrice && totalPrice.doubleValue() > 0)
				{
					orderModel.setTotalPrice(totalPrice);
					getModelService().save(orderModel);
					getModelService().refresh(orderModel);
				}

				discData = getMplVoucherService().getVoucherData(orderModel);
				//				LOG.debug(">> 2 : Checking voucher related promotion >> ");
				//				final PromotionVoucherModel voucher = (PromotionVoucherModel) orderModel.getDiscounts().get(0);
				//				final List<AbstractOrderEntryModel> applicableOrderEntryList = getMplVoucherService()
				//						.getOrderEntryModelFromVouEntries(voucher, orderModel);
				//				discData = getMplVoucherService().checkCartAfterApply(voucher, null, orderModel, applicableOrderEntryList);
				//				getMplVoucherService().setApportionedValueForVoucher(voucher, orderModel, voucher.getVoucherCode(),
				//						applicableOrderEntryList);
				getMplCommerceCartService().setTotalWithConvCharge(orderModel, orderData);

			}
			//Removing the session if the session is not empty
			if (null != getSessionService().getAttribute(MarketplacecommerceservicesConstants.PAYMENTMODEFORPROMOTION))
			{
				getSessionService().removeAttribute(MarketplacecommerceservicesConstants.PAYMENTMODEFORPROMOTION);
			}
			final List<MplPromotionData> responseDataList = getAppliedPromotionDetails(orderModel, orderData);
			if (CollectionUtils.isNotEmpty(responseDataList))
			{
				promoPriceData.setMplPromo(responseDataList);
			}
			promoPriceData.setTotalDiscount(calculateTotalDiscount(orderModel));
			//Populating Currency and Total Price Details
			if (null != orderModel.getCurrency() && StringUtils.isNotEmpty(orderModel.getCurrency().getSymbol())
					&& null != orderData)
			{
				promoPriceData.setCurrency(orderModel.getCurrency().getSymbol());
				promoPriceData.setTotalPrice(orderData.getTotalPriceWithConvCharge());
				promoPriceData.setConvCharge(orderData.getConvenienceChargeForCOD());
				final PriceData totalvalExcConv = discountUtility.createPrice(orderModel, Double.valueOf(((orderData
						.getTotalPriceWithConvCharge().getValue()).subtract(orderData.getConvenienceChargeForCOD().getValue()))
						.toString()));

				if (null != totalvalExcConv && null != totalvalExcConv.getFormattedValue())
				{
					promoPriceData.setTotalExcConv(totalvalExcConv);
				}
				promoPriceData.setDeliveryCost(orderData.getDeliveryCost());
			}
			promoPriceData.setVoucherDiscount(discData);
		}


		final long endTime = System.currentTimeMillis();
		LOG.debug("Exiting service applyPromotions()======" + (endTime - startTime));
		return promoPriceData;
	}

	/**
	 * @Description : To get details of applied promotion
	 * @param abstractOrderModel
	 * @param abstractOrderData
	 * @return List<MplPromotionData>
	 * @throws EtailNonBusinessExceptions
	 */
	private List<MplPromotionData> getAppliedPromotionDetails(final AbstractOrderModel abstractOrderModel,
			final AbstractOrderData abstractOrderData) throws EtailNonBusinessExceptions //Changed to abstractOrderModel for TPR-629
	{
		MplPromotionData responseData = new MplPromotionData();

		final List<MplPromotionData> responseDataList = new ArrayList<MplPromotionData>();

		final Set<PromotionResultModel> promotion = abstractOrderModel.getAllPromotionResults();
		if (CollectionUtils.isNotEmpty(promotion))
		{
			for (final PromotionResultModel promo : promotion)
			{
				if (checkPromoForPaymntRestrictn(promo))
				{
					if (promo.getPromotion() instanceof ProductPromotionModel && promo.getCertainty().floatValue() < 1.0F)
					{
						final ProductPromotionModel productPromotion = (ProductPromotionModel) promo.getPromotion();
						responseData = getDiscountUtility().populatePotentialPromoData(productPromotion);
						responseDataList.add(responseData);
					}
					else if (promo.getPromotion() instanceof OrderPromotionModel && promo.getCertainty().floatValue() < 1.0F)
					{
						final OrderPromotionModel orderPromotion = (OrderPromotionModel) promo.getPromotion();
						responseData = getDiscountUtility().populatePotentialOrderPromoData(orderPromotion);
						responseDataList.add(responseData);
					}
					else if (promo.getPromotion() instanceof ProductPromotionModel && promo.getCertainty().floatValue() == 1.0F)
					{
						final ProductPromotionModel productPromotion = (ProductPromotionModel) promo.getPromotion();
						responseData = getDiscountUtility().populateData(productPromotion, abstractOrderModel);
						responseDataList.add(responseData);
					}
					else if (promo.getPromotion() instanceof OrderPromotionModel && promo.getCertainty().floatValue() == 1.0F)
					{
						final OrderPromotionModel orderPromotion = (OrderPromotionModel) promo.getPromotion();
						responseData = getDiscountUtility().populateCartPromoData(orderPromotion, abstractOrderModel);
						responseDataList.add(responseData);
					}
				}
				else
				{
					responseData = getDiscountUtility().populateNonPromoData(abstractOrderData);
					responseDataList.add(responseData);
				}
			}
		}
		else
		{
			responseData = getDiscountUtility().populateNonPromoData(abstractOrderData);
			responseDataList.add(responseData);
		}
		return responseDataList;
	}



	/**
	 * This method calculates the promotional values
	 *
	 * @param cartData
	 * @param cartModel
	 * @param orderModel
	 * @param orderData
	 *
	 */
	private void calculatePromotion(final CartModel cartModel, final OrderModel orderModel, final CartData cartData,
			final OrderData orderData) //Added  orderModel and orderData to handle TPR-629---order before payment
	{
		final long startTime = System.currentTimeMillis();
		//Try/catch handled for IQA TPR-629
		try
		{
			if (null != cartModel)
			{
				//When customer has cartModel

				//	Double deliveryCost = cartModel.getDeliveryCost();
				final CommerceCartParameter parameter = new CommerceCartParameter();
				parameter.setEnableHooks(true);
				parameter.setCart(cartModel);
				getCalculationStrategy().recalculateCart(parameter);

				final Double subTotal = cartModel.getSubtotal();
				final Double cartDiscount = populateCartDiscountPrice(cartModel, null);

				final double deliveryCost = calculateDeliveryChargeForShipping(cartModel);

				cartModel.setDeliveryCost(Double.valueOf(deliveryCost));

				final Double totalPriceAfterDeliveryCost = Double.valueOf(subTotal.doubleValue() + deliveryCost
						- cartDiscount.doubleValue());

				//TISEE-5354
				final Double totalPrice = Double.valueOf(String.format("%.2f", totalPriceAfterDeliveryCost));
				cartModel.setTotalPrice(totalPrice);

				getModelService().save(cartModel);
				getMplCommerceCartService().setTotalWithConvCharge(cartModel, cartData);
			}
			else if (null != orderModel)
			{
				//When customer has orderModel
				final Double deliveryCost = orderModel.getDeliveryCost();
				final CommerceCartParameter parameter = new CommerceCartParameter();
				parameter.setEnableHooks(true);
				parameter.setOrder(orderModel);
				getCalculationStrategy().recalculateCart(parameter);

				final Double subTotal = orderModel.getSubtotal();
				final Double orderDiscount = populateCartDiscountPrice(null, orderModel);
				final Double totalPriceAfterDeliveryCost = Double.valueOf(subTotal.doubleValue() + deliveryCost.doubleValue()
						- orderDiscount.doubleValue());

				orderModel.setDeliveryCost(deliveryCost);

				//TISEE-5354
				final Double totalPrice = Double.valueOf(String.format("%.2f", totalPriceAfterDeliveryCost));
				orderModel.setTotalPrice(totalPrice);

				getModelService().save(orderModel);
				getMplCommerceCartService().setTotalWithConvCharge(orderModel, orderData);
			}
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}


		final long endTime = System.currentTimeMillis();
		LOG.debug("Exiting calculatePromotion()========" + (endTime - startTime));
	}

	/**
	 * @param cartModel
	 */
	private double calculateDeliveryChargeForShipping(final AbstractOrderModel cartModel)
	{
		// YTODO Auto-generated method stub
		/**********
		 * This is required for calculating tship and ship del charge separately
		 *
		 ***********/
		double deliveryCost = 0.0d;

		for (final AbstractOrderEntryModel cartentrymodel : cartModel.getEntries())
		{
			if (null != cartentrymodel.getCurrDelCharge() && cartentrymodel.getCurrDelCharge().doubleValue() > 0.0d)
			{
				deliveryCost += cartentrymodel.getCurrDelCharge().doubleValue();
			}
		}
		return deliveryCost;

	}


	/**
	 * @param abstractOrderModel
	 * @Description : Calculate Total Discount Value for Showing on Payment Screen
	 * @return dicountData
	 */
	private PriceData calculateTotalDiscount(final AbstractOrderModel abstractOrderModel) //Changed to abstractOrderModel for TPR-629
	{
		BigDecimal discount = null;
		//final double totalPrice = 0.0D;
		if (null != abstractOrderModel && CollectionUtils.isNotEmpty(abstractOrderModel.getEntries()))
		{
			//final List<DiscountModel> discountList = abstractOrderModel.getDiscounts();
			//final List<DiscountValue> discountValueList = abstractOrderModel.getGlobalDiscountValues();
			//double voucherDiscount = 0.0d;
			double discountVal = 0.0d;

			for (final AbstractOrderEntryModel entry : abstractOrderModel.getEntries())
			{
				if (null != entry.getGiveAway() && !entry.getGiveAway().booleanValue())
				{
					final double productDiscount = (null != entry.getTotalProductLevelDisc()
							&& entry.getTotalProductLevelDisc().doubleValue() > 0) ? entry.getTotalProductLevelDisc().doubleValue() : 0;
					final double cartDiscount = (null != entry.getCartLevelDisc() && entry.getCartLevelDisc().doubleValue() > 0)
							? entry.getCartLevelDisc().doubleValue() : 0;
					final double cartCouponDiscount = (null != entry.getCartCouponValue()
							&& entry.getCartCouponValue().doubleValue() > 0) ? entry.getCartCouponValue().doubleValue() : 0;

					discountVal += productDiscount + cartDiscount + cartCouponDiscount;
					//totalPrice = totalPrice + price;
					//(entry.getBasePrice().doubleValue() * entry.getQuantity().doubleValue());
				}
			}

			discount = BigDecimal.valueOf(discountVal);
			//			for (final DiscountValue discountValue : discountValueList)
			//			{
			//				discountVal = discountValue.getAppliedValue();
			//			}

			//discount = BigDecimal.valueOf(totalPrice).subtract(BigDecimal.valueOf(discountVal));
			//			discount = (BigDecimal.valueOf(abstractOrderModel.getDeliveryCost().doubleValue())).add(BigDecimal.valueOf(totalPrice))
			//					.add(BigDecimal.valueOf(abstractOrderModel.getConvenienceCharges().doubleValue()))
			//					.subtract(BigDecimal.valueOf(abstractOrderModel.getTotalPriceWithConv().doubleValue()))
			//					.subtract(BigDecimal.valueOf(voucherDiscount));

			//			discount = BigDecimal.valueOf(
			//					(totalPrice + cart.getDeliveryCost().doubleValue() + cart.getConvenienceCharges().doubleValue())).subtract(
			//					BigDecimal.valueOf((cart.getTotalPriceWithConv().doubleValue() + voucherDiscount)));					BigDecimal.valueOf((cart.getTotalPriceWithConv().doubleValue() + voucherDiscount)));
		}

		return getDiscountUtility()
				.createPrice(abstractOrderModel, Double.valueOf(discount != null ? discount.doubleValue() : 0.0));
	}

	/**
	 * @Description : Calculating Cart Promotion Discount Value
	 * @param cart
	 * @return Double
	 */
	private Double populateCartDiscountPrice(final CartModel cart, final OrderModel order) //Added orderModel to handle TPR-629
	{
		final long startTime = System.currentTimeMillis();
		LOG.debug("Entering Service populateCartDiscountPrice()=====" + System.currentTimeMillis());
		Double value = Double.valueOf(0);
		if (null != cart)
		{
			//When customer has cart-->first time payment
			final CartData cartData = getMplExtendedCartConverter().convert(cart);

			if (cartData.getTotalDiscounts() != null && cartData.getTotalDiscounts().getValue() != null)
			{
				value = Double.valueOf(cartData.getTotalDiscounts().getValue().doubleValue());

			}
		}
		else if (null != order)
		{
			//When customer has order-->2nd or later time payment after one failed attempt
			final OrderData orderData = orderConverter.convert(order);

			if (orderData.getTotalDiscounts() != null && orderData.getTotalDiscounts().getValue() != null)
			{
				value = Double.valueOf(value.doubleValue() + orderData.getTotalDiscounts().getValue().doubleValue());

			}

			if (null != orderData.getCouponDiscount() && null != orderData.getCouponDiscount().getValue())
			{
				value = Double.valueOf(value.doubleValue() + orderData.getCouponDiscount().getValue().doubleValue());
			}

			if (null != orderData.getCartCouponDiscount() && null != orderData.getCartCouponDiscount().getValue())
			{
				value = Double.valueOf(value.doubleValue() + orderData.getCartCouponDiscount().getValue().doubleValue());
			}
		}

		final long endTime = System.currentTimeMillis();
		LOG.debug("Time taken within Controller populateCartDiscountPrice()=====" + (endTime - startTime));
		return value;
	}


	/**
	 * @Description : Check Promotion for Payment Restriction
	 * @param promo
	 * @return boolean
	 */
	private boolean checkPromoForPaymntRestrictn(final PromotionResultModel promo)
	{
		boolean flag = false;
		if (null != promo && null != promo.getPromotion() && CollectionUtils.isNotEmpty(promo.getPromotion().getRestrictions()))

		{
			for (final AbstractPromotionRestrictionModel restriction : promo.getPromotion().getRestrictions())
			{
				if (restriction instanceof PaymentModeSpecificPromotionRestrictionModel)
				{
					flag = true;
					break;
				}
			}
		}

		return flag;
	}



	/**
	 *
	 * @return String
	 */
	@Override
	public String createPaymentId()
	{
		return getJuspayOrderIdGenerator().generate().toString();
	}



	/**
	 * This method creates an entry in the audit table after an Id has been generated for Juspay
	 *
	 * @param juspayOrderId
	 * @param channel
	 * @param cartGuId
	 * @return boolean
	 * @throws EtailNonBusinessExceptions
	 *
	 */
	@Override
	public boolean createEntryInAudit(final String juspayOrderId, final String channel, final String cartGuId)
			throws EtailNonBusinessExceptions
	{
		try
		{
			Assert.notNull(juspayOrderId, "Parameter juspayOrderId cannot be null.");
			//Make entry in Audit Table
			boolean flag = false;
			final MplPaymentAuditModel auditModel = getMplPaymentDao().getAuditEntries(juspayOrderId);

			if (null != auditModel)
			{
				LOG.debug("************** Saving auditModel *************" + juspayOrderId);
				List<MplPaymentAuditEntryModel> collection = auditModel.getAuditEntries();
				final List<MplPaymentAuditEntryModel> auditEntryList = new ArrayList<MplPaymentAuditEntryModel>();
				if (null == collection || collection.isEmpty())
				{
					collection = new ArrayList<MplPaymentAuditEntryModel>();
				}

				auditEntryList.addAll(collection);
				final MplPaymentAuditEntryModel auditEntry = getModelService().create(MplPaymentAuditEntryModel.class);
				auditEntry.setAuditId(juspayOrderId);
				auditEntry.setStatus(MplPaymentAuditStatusEnum.SUBMITTED);
				getModelService().save(auditEntry);

				auditEntryList.add(auditEntry);

				auditModel.setAuditEntries(auditEntryList);
				getModelService().save(auditModel);
				flag = true;
			}
			else
			{
				LOG.debug("************** Creating and  Saving auditModel *************" + juspayOrderId);
				final CartModel cartModel = getMplPaymentDao().getCart(cartGuId);
				final List<MplPaymentAuditEntryModel> auditEntryList = new ArrayList<MplPaymentAuditEntryModel>();
				final MplPaymentAuditEntryModel auditEntry = getModelService().create(MplPaymentAuditEntryModel.class);
				auditEntry.setAuditId(juspayOrderId);
				auditEntry.setStatus(MplPaymentAuditStatusEnum.CREATED);
				getModelService().save(auditEntry);

				auditEntryList.add(auditEntry);

				final MplPaymentAuditModel newAuditModel = getModelService().create(MplPaymentAuditModel.class);
				newAuditModel.setChannel(GenericUtilityMethods.returnChannelData(channel));
				newAuditModel.setAuditId(juspayOrderId);
				newAuditModel.setCartGUID(cartGuId);
				newAuditModel.setRequestDate(new Date());
				newAuditModel.setAuditEntries(auditEntryList);
				if (cartModel != null && cartModel.getTotalPrice() != null)
				{
					newAuditModel.setPaymentAmount(cartModel.getTotalPrice());
				}

				getModelService().save(newAuditModel);
				flag = true;
			}
			LOG.debug("************** Saved auditModel *************" + juspayOrderId + ":::" + flag);
			return flag;

		}
		//Commented for TPR-629
		//		catch (final NullPointerException e)
		//		{
		//			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0001);
		//		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		//		catch (final EtailNonBusinessExceptions e)
		//		{
		//			throw e;
		//		}
		//Catch added for IQA TPR-629
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}



	/**
	 * This method updates already created entry in the Audit Table with response from Juspay
	 *
	 * @param orderStatusResponse
	 *
	 */
	@Override
	public boolean updateAuditEntry(final GetOrderStatusResponse orderStatusResponse) throws EtailNonBusinessExceptions
	{
		boolean flag = false;
		try
		{
			//Make entry in Audit Table
			final MplPaymentAuditModel auditModel = getMplPaymentDao().getAuditEntries(orderStatusResponse.getOrderId());
			final MplPaymentAuditEntryModel auditEntry = getModelService().create(MplPaymentAuditEntryModel.class);
			//changes for JuspayEBSResponseFIX
			final ArrayList<JuspayEBSResponseDataModel> juspayEBSResponseList = new ArrayList<JuspayEBSResponseDataModel>();
			final JuspayEBSResponseDataModel juspayEBSResponseModel = getModelService().create(JuspayEBSResponseDataModel.class);
			final String ebsDowntime = getConfigurationService().getConfiguration().getString("payment.ebs.downtime");
			final Map<String, Double> paymentMode = getSessionService().getAttribute(
					MarketplacecommerceservicesConstants.PAYMENTMODE);
			if (null != paymentMode)
			{
				LOG.debug("updateAuditEntry method" + paymentMode);
			}
			else
			{

				LOG.error("payment mode is null    ------->" + orderStatusResponse.getOrderId());
			}

			if (!MarketplacecommerceservicesConstants.AUTHORIZATION_FAILED.equalsIgnoreCase(orderStatusResponse.getStatus())
					&& !MarketplacecommerceservicesConstants.AUTHENTICATION_FAILED.equalsIgnoreCase(orderStatusResponse.getStatus()))
			{

				if (null != auditModel)
				{
					List<MplPaymentAuditEntryModel> collection = auditModel.getAuditEntries();
					final List<MplPaymentAuditEntryModel> auditEntryList = new ArrayList<MplPaymentAuditEntryModel>();
					if (null == collection || collection.isEmpty())
					{
						collection = new ArrayList<MplPaymentAuditEntryModel>();
					}
					auditEntryList.addAll(collection);


					if (StringUtils.isNotEmpty(orderStatusResponse.getOrderId()))
					{
						auditEntry.setAuditId(orderStatusResponse.getOrderId());
					}

					//Condition when RiskResponse is available in OrderStatusResponse
					if (null != orderStatusResponse.getRiskResponse())
					{
						LOG.debug("orderStatusResponse status ------> " + orderStatusResponse.getStatus());
						//Condition when PG Response status is available and charged
						if (StringUtils.isNotEmpty(orderStatusResponse.getStatus())
								&& orderStatusResponse.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.CHARGED))
						{
							if (StringUtils.isNotEmpty(orderStatusResponse.getRiskResponse().getEbsRiskLevel()))
							{
								LOG.debug("orderStatusResponse getRiskResponse ------> "
										+ orderStatusResponse.getRiskResponse().getEbsRiskLevel());

								//Condition when RiskLevel is GREEN
								if (orderStatusResponse.getRiskResponse().getEbsRiskLevel()
										.equalsIgnoreCase(MarketplacecommerceservicesConstants.GREEN))
								{
									auditEntry.setStatus(MplPaymentAuditStatusEnum.COMPLETED);
									auditModel.setIsExpired(Boolean.TRUE);
								}

								//Condition when RiskLevel is NOT GREEN
								else
								{
									//Condition for Domestic Card //TODO::Change once this is finalized
									if (StringUtils.isNotEmpty(orderStatusResponse.getRiskResponse().getEbsBinCountry())
											&& orderStatusResponse.getRiskResponse().getEbsBinCountry()
													.equalsIgnoreCase(MarketplacecommerceservicesConstants.INDIA))
									{
										if (orderStatusResponse.getRiskResponse().getEbsRiskLevel()
												.equalsIgnoreCase(MarketplacecommerceservicesConstants.YELLOW)
												|| orderStatusResponse.getRiskResponse().getEbsRiskLevel()
														.equalsIgnoreCase(MarketplacecommerceservicesConstants.RED))
										{
											auditEntry.setStatus(MplPaymentAuditStatusEnum.PENDING);
										}
									}
									//Condition for International Card
									else
									{
										if (orderStatusResponse.getRiskResponse().getEbsRiskLevel()
												.equalsIgnoreCase(MarketplacecommerceservicesConstants.YELLOW)
												|| orderStatusResponse.getRiskResponse().getEbsRiskLevel()
														.equalsIgnoreCase(MarketplacecommerceservicesConstants.RED))
										{
											auditEntry.setStatus(MplPaymentAuditStatusEnum.PENDING);
										}
									}
								}
							}
							else
							{
								auditEntry.setStatus(MplPaymentAuditStatusEnum.PENDING);
								juspayEBSResponseModel.setEbsRiskPercentage(MarketplacecommerceservicesConstants.DEFAULT_RISK);
							}
						}
						//Condition when PG Response status is NOT available or it is NOT charged
						else
						{
							auditEntry.setStatus(MplPaymentAuditStatusEnum.DECLINED);
							auditModel.setIsExpired(Boolean.TRUE);
						}

						LOG.debug("auditEntry status risk ne null------> " + auditEntry.getStatus());

						if (StringUtils.isNotEmpty(orderStatusResponse.getRiskResponse().getEbsBinCountry()))
						{
							juspayEBSResponseModel.setEbs_bin_country(orderStatusResponse.getRiskResponse().getEbsBinCountry());
						}
						if (StringUtils.isNotEmpty(orderStatusResponse.getRiskResponse().getEbsRiskLevel()))
						{
							setEBSRiskLevel(orderStatusResponse.getRiskResponse().getEbsRiskLevel(), juspayEBSResponseModel);
						}
						if (null != orderStatusResponse.getRiskResponse().getEbsRiskPercentage())
						{
							final Double scoreDouble = Double.valueOf(orderStatusResponse.getRiskResponse().getEbsRiskPercentage()
									.doubleValue());
							juspayEBSResponseModel.setEbsRiskPercentage(scoreDouble.toString());
						}
						else
						{
							juspayEBSResponseModel.setEbsRiskPercentage(MarketplacecommerceservicesConstants.DEFAULT_RISK);
						}
						if (StringUtils.isNotEmpty(orderStatusResponse.getRiskResponse().getEbsPaymentStatus())
								&& !orderStatusResponse.getRiskResponse().getEbsPaymentStatus()
										.equalsIgnoreCase(MarketplacecommerceservicesConstants.PAID))
						{
							setEBSRiskStatus(orderStatusResponse.getRiskResponse().getEbsPaymentStatus(), juspayEBSResponseModel);
						}
						else if (StringUtils.isEmpty(orderStatusResponse.getRiskResponse().getEbsPaymentStatus())
								&& !juspayEBSResponseModel.getEbsRiskPercentage().equalsIgnoreCase(
										MarketplacecommerceservicesConstants.DEFAULT_RISK))
						{
							if (MplPaymentAuditStatusEnum.PENDING.equals(auditEntry.getStatus()))
							{
								setEBSRiskStatus(MarketplacecommerceservicesConstants.REVIEW, juspayEBSResponseModel);
							}
							else if (MplPaymentAuditStatusEnum.COMPLETED.equals(auditEntry.getStatus()))
							{
								setEBSRiskStatus(MarketplacecommerceservicesConstants.APPROVED, juspayEBSResponseModel);
							}
							else if (MplPaymentAuditStatusEnum.DECLINED.equals(auditEntry.getStatus())
									|| MplPaymentAuditStatusEnum.EBS_DECLINED.equals(auditEntry.getStatus()))
							{
								setEBSRiskStatus(MarketplacecommerceservicesConstants.REJECTED, juspayEBSResponseModel);
							}
						}
						flag = true;
					}
					//Condition when RiskResponse is NOT available in OrderStatusResponse
					//For NetBanking , we will not get any RISK structure
					else
					{
						if (StringUtils.isNotEmpty(orderStatusResponse.getStatus())
								&& orderStatusResponse.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.CHARGED))
						{
							if (StringUtils.isNotEmpty(ebsDowntime) && ebsDowntime.equalsIgnoreCase("Y"))
							{
								auditEntry.setStatus(MplPaymentAuditStatusEnum.COMPLETED);
								auditModel.setIsExpired(Boolean.TRUE);
							}
							else
							{

								boolean netBanking = false;
								for (final Map.Entry<String, Double> entry : paymentMode.entrySet())
								{
									//if (entry.getKey() != null
									//		&& MarketplacecommerceservicesConstants.NETBANKING.equalsIgnoreCase(entry.getKey()))
									if (entry.getKey() != null
											&& MarketplacecommerceservicesConstants.NETBANKING.equalsIgnoreCase(entry.getKey().trim()))
									{
										LOG.debug("Payment mode netbanking ------> " + orderStatusResponse.getOrderId());

										auditEntry.setStatus(MplPaymentAuditStatusEnum.COMPLETED);
										auditModel.setIsExpired(Boolean.TRUE);
										netBanking = true;
										break;
									}
								}
								// For credit card/debit card and emi , if risk block is not available
								if (!netBanking)
								{
									LOG.debug("Payment mode not netbanking and no risk block present ------> "
											+ orderStatusResponse.getOrderId());

									auditEntry.setStatus(MplPaymentAuditStatusEnum.PENDING);
									juspayEBSResponseModel.setEbsRiskPercentage(MarketplacecommerceservicesConstants.DEFAULT_RISK);
								}
							}
						}
						else
						{
							auditEntry.setStatus(MplPaymentAuditStatusEnum.DECLINED);
							auditModel.setIsExpired(Boolean.TRUE);
						}
						flag = true;
					}

					auditEntry.setResponseDate(new Date());

					LOG.debug("auditEntry status risk null------> " + auditEntry.getStatus());

					getModelService().save(auditEntry);
					auditEntryList.add(auditEntry);

					getModelService().save(juspayEBSResponseModel);
					juspayEBSResponseList.add(juspayEBSResponseModel);

					auditModel.setAuditEntries(auditEntryList);
					//changes for JuspayEBSResponseFIX
					auditModel.setRiskData(juspayEBSResponseList);
					getModelService().save(auditModel);
				}
			}
			else
			{
				auditEntry.setStatus(MplPaymentAuditStatusEnum.DECLINED);
				auditModel.setIsExpired(Boolean.TRUE);
			}


		}
		//PMD Fixes
		//		catch (final NullPointerException e)
		//		{
		//			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0001);
		//		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		//		catch (final EtailNonBusinessExceptions e)
		//		{
		//			throw e;
		//		}
		return flag;
	}

	/**
	 * This method sets the EbsRiskStatus in JuspayEBSResponseModel
	 *
	 * @param riskStatus
	 * @param juspayEBSResponseModel
	 */
	@Override
	public void setEBSRiskStatus(final String riskStatus, final JuspayEBSResponseDataModel juspayEBSResponseModel)
			throws EtailNonBusinessExceptions
	{
		try
		{
			if (riskStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.REVIEW))
			{
				juspayEBSResponseModel.setEbsRiskStatus(EBSResponseStatus.REVIEW);
			}
			else if (riskStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.APPROVED))
			{
				juspayEBSResponseModel.setEbsRiskStatus(EBSResponseStatus.APPROVED);
			}
			else if (riskStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.REJECTED))
			{
				juspayEBSResponseModel.setEbsRiskStatus(EBSResponseStatus.REJECTED);
			}
		}
		//PMD Fix
		//		catch (final NullPointerException e)
		//		{
		//			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0001);
		//		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * This method sets the EbsRiskLevel in JuspayEBSResponseModel
	 *
	 * @param riskLevel
	 * @param juspayEBSResponseModel
	 */
	@Override
	public void setEBSRiskLevel(final String riskLevel, final JuspayEBSResponseDataModel juspayEBSResponseModel)
			throws EtailNonBusinessExceptions
	{
		try
		{
			if (riskLevel.equalsIgnoreCase(MarketplacecommerceservicesConstants.RED))
			{
				juspayEBSResponseModel.setEbsRiskLevel(EBSRiskLevelEnum.RED);
			}
			else if (riskLevel.equalsIgnoreCase(MarketplacecommerceservicesConstants.YELLOW))
			{
				juspayEBSResponseModel.setEbsRiskLevel(EBSRiskLevelEnum.YELLOW);
			}
			else if (riskLevel.equalsIgnoreCase(MarketplacecommerceservicesConstants.GREEN))
			{
				juspayEBSResponseModel.setEbsRiskLevel(EBSRiskLevelEnum.GREEN);
			}
		}
		//PMD Fix
		//		catch (final NullPointerException e)
		//		{
		//			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0001);
		//		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * This method saves credit cards
	 *
	 * @param orderStatusResponse
	 * @param cart
	 * @param sameAsShipping
	 *
	 */
	@Override
	public void saveCreditCard(final GetOrderStatusResponse orderStatusResponse, final AbstractOrderModel cart,
			final String sameAsShipping) throws EtailNonBusinessExceptions //Changed to abstractOrderModel for TPR-629
	{
		try
		{
			//for saving Billing Address against credit card
			final AddressModel address = billingAddressForSavedCard(orderStatusResponse, cart, sameAsShipping);
			getModelService().save(address);

			//setting as saved card
			if (null != address)
			{
				setInSavedCard(orderStatusResponse, address, cart.getUser());
			}
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}



	/**
	 * This method saves debit cards
	 *
	 * @param orderStatusResponse
	 * @param cart
	 */
	@Override
	public void saveDebitCard(final GetOrderStatusResponse orderStatusResponse, final AbstractOrderModel cart)
			throws EtailNonBusinessExceptions //Changed to abstractOrderModel for TPR-629
	{
		String checkValues = "".intern();
		String[] parts = null;
		String saveCard = "".intern();
		if (StringUtils.isNotEmpty(orderStatusResponse.getUdf10()))
		{
			checkValues = orderStatusResponse.getUdf10();
		}
		if (checkValues.contains(MarketplacecommerceservicesConstants.CONCTASTRING))
		{
			parts = checkValues.split(MarketplacecommerceservicesConstants.SPLITSTRING);
			saveCard = parts[0];
		}

		if (saveCard.equalsIgnoreCase(MarketplacecommerceservicesConstants.TRUE))
		{
			//setting as saved card
			setInSavedDebitCard(orderStatusResponse, cart.getUser());
		}
	}



	/**
	 * This private method is used to set the billing address in CreditCardPaymentInfoModel and EMIPaymentInfoModel after
	 * getting successful response from Juspay
	 *
	 * @param orderStatusResponse
	 * @param cart
	 * @param sameAsShipping
	 * @return AddressModel
	 */
	private AddressModel billingAddressForSavedCard(final GetOrderStatusResponse orderStatusResponse,
			final AbstractOrderModel cart, final String sameAsShipping) //Changed to abstractOrderModel for TPR-629
	{
		AddressModel address = null;
		try
		{
			if (StringUtils.isNotEmpty(orderStatusResponse.getUdf1()))
			{
				if (sameAsShipping.equalsIgnoreCase(MarketplacecommerceservicesConstants.TRUE) && null != cart.getDeliveryAddress())
				{
					address = cart.getDeliveryAddress();
					address.setBillingAddress(Boolean.TRUE);
				}
				else
				{
					address = getModelService().create(AddressModel.class);
					//try	//Try-Catch fixed
					//{
					if (StringUtils.isNotEmpty(orderStatusResponse.getUdf1())
							&& !orderStatusResponse.getUdf1().equalsIgnoreCase(MarketplacecommerceservicesConstants.NULL))
					{
						address.setFirstname(orderStatusResponse.getUdf1());
					}
					if (StringUtils.isNotEmpty(orderStatusResponse.getUdf2())
							&& !orderStatusResponse.getUdf2().equalsIgnoreCase(MarketplacecommerceservicesConstants.NULL))
					{
						address.setLastname(orderStatusResponse.getUdf2());
					}
					if (StringUtils.isNotEmpty(orderStatusResponse.getUdf3())
							&& !orderStatusResponse.getUdf3().equalsIgnoreCase(MarketplacecommerceservicesConstants.NULL))
					{
						address.setLine1(orderStatusResponse.getUdf3());
					}
					if (StringUtils.isNotEmpty(orderStatusResponse.getUdf4())
							&& !orderStatusResponse.getUdf4().equalsIgnoreCase(MarketplacecommerceservicesConstants.NULL))
					{
						address.setLine2(orderStatusResponse.getUdf4());
					}
					if (StringUtils.isNotEmpty(orderStatusResponse.getUdf8())
							&& !orderStatusResponse.getUdf8().equalsIgnoreCase(MarketplacecommerceservicesConstants.NULL))
					{
						address.setTown(orderStatusResponse.getUdf8());//City
					}
					if (StringUtils.isNotEmpty(orderStatusResponse.getUdf9())
							&& !orderStatusResponse.getUdf9().equalsIgnoreCase(MarketplacecommerceservicesConstants.NULL))
					{
						address.setPostalcode(orderStatusResponse.getUdf9());//Pincode
					}
					if (StringUtils.isNotEmpty(orderStatusResponse.getUdf7())
							&& !orderStatusResponse.getUdf7().equalsIgnoreCase(MarketplacecommerceservicesConstants.NULL))
					{
						address.setDistrict(orderStatusResponse.getUdf7());//State
					}
					if (StringUtils.isNotEmpty(orderStatusResponse.getUdf5())
							&& !orderStatusResponse.getUdf5().equalsIgnoreCase(MarketplacecommerceservicesConstants.NULL))
					{
						address.setAddressLine3(orderStatusResponse.getUdf5());
					}
					if (StringUtils.isNotEmpty(orderStatusResponse.getUdf6())
							&& !orderStatusResponse.getUdf6().equalsIgnoreCase(MarketplacecommerceservicesConstants.NULL))
					{
						final String countryISO = getMplPaymentDao().getCountryISO(orderStatusResponse.getUdf6());
						final CountryModel country = getI18NService().getCountry(countryISO);
						address.setCountry(country);//country
					}
					address.setOwner(cart.getUser());
					address.setShippingAddress(Boolean.FALSE);
					address.setUnloadingAddress(Boolean.FALSE);
					address.setBillingAddress(Boolean.TRUE);
					address.setContactAddress(Boolean.FALSE);
					address.setVisibleInAddressBook(Boolean.FALSE);
					address.setDuplicate(Boolean.FALSE);

				}
				//				catch (final NullPointerException e)
				//				{
				//					LOG.error("Cannot set value in Address Model with exception ", e);
				//				}
			}
			//}
			modelService.save(address);
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		//TISPRD-3025
		LOG.error("Address for new card is :::" + (address != null ? address : null));

		return address;

	}


	/**
	 * This method gets the addressModel mapped to any saved card
	 *
	 * @param orderStatusResponse
	 * @return AddressModel
	 */
	private AddressModel getAddress(final GetOrderStatusResponse orderStatusResponse, final UserModel user) //parameter added for TPR-629
	{
		AddressModel address = null;
		//getting the current customer to fetch customer Id and customer email
		//final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();		//Commented for TPR-629
		final CustomerModel customer = (CustomerModel) user;
		final Collection<SavedCardModel> savedCardColl = customer.getSavedCard();
		final List<SavedCardModel> savedCardList = new ArrayList<SavedCardModel>();

		if (!savedCardColl.isEmpty())
		{
			savedCardList.addAll(savedCardColl);
		}
		if (!savedCardList.isEmpty() && null != orderStatusResponse.getCardResponse()
				&& StringUtils.isNotEmpty(orderStatusResponse.getCardResponse().getCardReference()))
		{
			for (final SavedCardModel savedCard : savedCardList)
			{
				if (orderStatusResponse.getCardResponse().getCardReference().equalsIgnoreCase(savedCard.getCardReferenceNumber()))
				{
					address = savedCard.getBillingAddress();
					break;
				}
			}
		}
		//TISPRD-3025
		LOG.error("Address for saved card is :::" + (address != null ? address : null));
		return address;
	}



	/**
	 * This method saves cards against the customer. Try catch fixed and unwanted codes commented accordingly TPR-629
	 *
	 * @param orderStatusResponse
	 * @param paymentMode
	 * @param cart
	 * @param sameAsShipping
	 */
	private void saveCards(final GetOrderStatusResponse orderStatusResponse, final Map<String, Double> paymentMode,
			final AbstractOrderModel cart, final String sameAsShipping) throws EtailNonBusinessExceptions //Changed to abstractOrderModel for TPR-629
	{
		//		if (null != orderStatusResponse && null != orderStatusResponse.getCardResponse()
		//				&& StringUtils.isNotEmpty(cart.getModeOfPayment()))
		if (null != orderStatusResponse && null != orderStatusResponse.getCardResponse()
				&& StringUtils.isNotEmpty(orderStatusResponse.getCardResponse().getCardType()))
		//			&& (StringUtils.isNotEmpty(orderStatusResponse.getCardResponse().getCardType()) || StringUtils
		//					.isNotEmpty(orderStatusResponse.getPaymentMethodType()))
		{
			//Logic if the order status response is not null
			//			for (final Map.Entry<String, Double> entry : paymentMode.entrySet())
			//			{
			//				if (MarketplacecommerceservicesConstants.DEBIT.equalsIgnoreCase(entry.getKey()))


			//			if (MarketplacecommerceservicesConstants.DEBIT.equalsIgnoreCase(cart.getModeOfPayment()))

			//OrderIssues:- getting the CardType Or PaymentMethodType in a variable

			final String cardType = orderStatusResponse.getCardResponse().getCardType();

			if (cardType.equalsIgnoreCase("DEBIT"))
			{
				//try
				//{
				saveDebitCard(orderStatusResponse, cart);
				//					break;
				//}
				//catch (final ModelSavingException e)
				//{
				//	LOG.error(MarketplacecommerceservicesConstants.PAYMENT_EXC_LOG, e);
				//}
			}
			//			else if (MarketplacecommerceservicesConstants.CREDIT.equalsIgnoreCase(cart.getModeOfPayment())
			//					|| MarketplacecommerceservicesConstants.EMI.equalsIgnoreCase(cart.getModeOfPayment()))
			if (cardType.equalsIgnoreCase("CREDIT"))
			{
				//try
				//{
				saveCreditCard(orderStatusResponse, cart, sameAsShipping);
				//					break;
				//}
				//catch (final ModelSavingException e)
				//{
				//	LOG.error(MarketplacecommerceservicesConstants.PAYMENT_EXC_LOG, e);
				//}
			}
			//			}

		}
	}

	/**
	 * This method returns the customer model based on the CustomerUid
	 *
	 * @param uid
	 * @return CustomerModel
	 *
	 */
	@Override
	public CustomerModel getCustomer(final String uid)
	{
		return getMplPaymentDao().getCustomer(uid);
	}


	/**
	 * This method is used to create a dummy address when there is no address present in Credit Card and EMI Payment Info
	 *
	 * @param cart
	 * @return AddressModel
	 */
	private AddressModel createDummyAddress(final AbstractOrderModel cart)
	{
		final AddressModel billingAddress = getModelService().create(AddressModel.class);

		//INC144315579: cloning deliveryAddress to billingAddress
		if (null != cart.getDeliveryAddress())
		{
			final AddressModel deliveryAddress = cart.getDeliveryAddress();
			billingAddress.setOwner(cart.getUser());
			billingAddress.setShippingAddress(Boolean.FALSE);
			billingAddress.setUnloadingAddress(Boolean.FALSE);
			billingAddress.setBillingAddress(Boolean.TRUE);
			billingAddress.setContactAddress(Boolean.FALSE);
			billingAddress.setVisibleInAddressBook(Boolean.FALSE);
			billingAddress.setDuplicate(Boolean.FALSE);
			billingAddress.setFirstname(deliveryAddress.getFirstname());
			billingAddress.setLastname(deliveryAddress.getLastname());
			billingAddress.setLine1(deliveryAddress.getLine1());
			billingAddress.setLine2(deliveryAddress.getLine2());
			billingAddress.setAddressLine3(deliveryAddress.getAddressLine3());
			billingAddress.setTown(deliveryAddress.getTown());
			billingAddress.setPostalcode(deliveryAddress.getPostalcode());
			billingAddress.setDistrict(deliveryAddress.getDistrict());
			billingAddress.setCountry(deliveryAddress.getCountry());
		}
		else
		{
			//setting mandatory fields
			billingAddress.setOwner(cart.getUser());
			billingAddress.setShippingAddress(Boolean.FALSE);
			billingAddress.setUnloadingAddress(Boolean.FALSE);
			billingAddress.setBillingAddress(Boolean.TRUE);
			billingAddress.setContactAddress(Boolean.FALSE);
			billingAddress.setVisibleInAddressBook(Boolean.FALSE);
			billingAddress.setDuplicate(Boolean.FALSE);
			billingAddress.setStreetname("DUMMY_STREET_NAME");
			billingAddress.setStreetnumber("DUMMY_STREET_NO");
		}

		return billingAddress;
	}

	@Override
	public JuspayEBSResponseDataModel getEntryInAuditByOrder(final String auditId)
	{
		//changes for JuspayEBSResponseFIX
		JuspayEBSResponseDataModel jusModelLast = null;
		final MplPaymentAuditModel auditModel = getMplPaymentDao().getAuditEntries(auditId);
		for (final JuspayEBSResponseDataModel jusModel : auditModel.getRiskData())
		{
			jusModelLast = jusModel;
		}
		return jusModelLast;
	}

	/*
	 * @description : fetching bank model for a bank name TISPRO-179\
	 * 
	 * @param : bankName
	 * 
	 * @return : BankModel
	 * 
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public BankModel getBankDetailsForBank(final String bankName) throws EtailNonBusinessExceptions
	{
		return getMplPaymentDao().getBankDetailsForBank(bankName);

	}

	/*
	 * @Description : Fetching bank name for net banking-- TISPT-169
	 * 
	 * @return List<BankforNetbankingModel>
	 * 
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public List<BankforNetbankingModel> getNetBankingBanks() throws EtailNonBusinessExceptions
	{
		return getMplPaymentDao().getNetBankingBanks();
	}



	/**
	 *
	 * This method returns the latest audit id with 2 audit entries TISPT-200
	 *
	 * @param cartGuid
	 * @return String
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public String getAuditId(final String cartGuid) throws EtailNonBusinessExceptions
	{
		final List<MplPaymentAuditModel> auditList = getMplPaymentDao().getAuditId(cartGuid);
		String auditId = null;
		for (final MplPaymentAuditModel audit : auditList)
		{
			if (CollectionUtils.isNotEmpty(audit.getAuditEntries()) && audit.getAuditEntries().size() == 2)
			{
				auditId = audit.getAuditId();
				break;
			}
		}
		return auditId;
	}






	/**
	 * This method updates already created entry in the Audit Table with response from Juspay --- TIS-3168 and TPR-629
	 *
	 * @param orderStatusResponse
	 *
	 */
	@Override
	public boolean updateAuditEntry(final GetOrderStatusResponse orderStatusResponse,
			final GetOrderStatusRequest orderStatusRequest, final OrderModel orderModel, final Map<String, Double> paymentMode)
			throws EtailNonBusinessExceptions
	{
		boolean flag = false;
		try
		{
			//Make entry in Audit Table
			final MplPaymentAuditModel auditModel = getMplPaymentDao().getAuditEntries(orderStatusResponse.getOrderId());
			final MplPaymentAuditEntryModel auditEntry = getModelService().create(MplPaymentAuditEntryModel.class);
			//changes for JuspayEBSResponseFIX
			final ArrayList<JuspayEBSResponseDataModel> juspayEBSResponseList = new ArrayList<JuspayEBSResponseDataModel>();
			final JuspayEBSResponseDataModel juspayEBSResponseModel = getModelService().create(JuspayEBSResponseDataModel.class);
			final String ebsDowntime = getConfigurationService().getConfiguration().getString("payment.ebs.downtime");
			//final Map<String, Double> paymentMode = getSessionService().getAttribute(
			//		MarketplacecommerceservicesConstants.PAYMENTMODE);		//Added as parameter for TPR-629
			if (null != paymentMode)
			{
				LOG.debug("updateAuditEntry method" + paymentMode);
			}
			else
			{

				LOG.error("payment mode is null    ------->" + orderStatusResponse.getOrderId());
			}
			if (!MarketplacecommerceservicesConstants.AUTHORIZATION_FAILED.equalsIgnoreCase(orderStatusResponse.getStatus())
					&& !MarketplacecommerceservicesConstants.AUTHENTICATION_FAILED.equalsIgnoreCase(orderStatusResponse.getStatus()))
			{

				if (null != auditModel)
				{
					List<MplPaymentAuditEntryModel> collection = auditModel.getAuditEntries();
					final List<MplPaymentAuditEntryModel> auditEntryList = new ArrayList<MplPaymentAuditEntryModel>();
					if (null == collection || collection.isEmpty())
					{
						collection = new ArrayList<MplPaymentAuditEntryModel>();
					}
					auditEntryList.addAll(collection);

					if (StringUtils.isNotEmpty(orderStatusResponse.getOrderId()))
					{
						auditEntry.setAuditId(orderStatusResponse.getOrderId());
					}

					//Condition when RiskResponse is available in OrderStatusResponse
					if (null != orderStatusResponse.getRiskResponse())
					{
						LOG.debug("orderStatusResponse status ------> " + orderStatusResponse.getStatus());
						//Condition when PG Response status is available and charged
						if (StringUtils.isNotEmpty(orderStatusResponse.getStatus())
								&& orderStatusResponse.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.CHARGED))
						{
							if (StringUtils.isNotEmpty(orderStatusResponse.getRiskResponse().getEbsRiskLevel()))
							{
								LOG.debug("orderStatusResponse getRiskResponse ------> "
										+ orderStatusResponse.getRiskResponse().getEbsRiskLevel());

								//Condition when RiskLevel is GREEN
								if (orderStatusResponse.getRiskResponse().getEbsRiskLevel()
										.equalsIgnoreCase(MarketplacecommerceservicesConstants.GREEN))
								{
									auditEntry.setStatus(MplPaymentAuditStatusEnum.COMPLETED);
									auditModel.setIsExpired(Boolean.TRUE);
								}

								//Condition when RiskLevel is NOT GREEN
								else
								{
									//Condition for Domestic Card //TODO::Change once this is finalized
									if (StringUtils.isNotEmpty(orderStatusResponse.getRiskResponse().getEbsBinCountry())
											&& orderStatusResponse.getRiskResponse().getEbsBinCountry()
													.equalsIgnoreCase(MarketplacecommerceservicesConstants.INDIA))
									{
										if (orderStatusResponse.getRiskResponse().getEbsRiskLevel()
												.equalsIgnoreCase(MarketplacecommerceservicesConstants.YELLOW)
												|| orderStatusResponse.getRiskResponse().getEbsRiskLevel()
														.equalsIgnoreCase(MarketplacecommerceservicesConstants.RED))
										{
											auditEntry.setStatus(MplPaymentAuditStatusEnum.PENDING);
										}
									}
									//Condition for International Card
									else
									{
										if (orderStatusResponse.getRiskResponse().getEbsRiskLevel()
												.equalsIgnoreCase(MarketplacecommerceservicesConstants.YELLOW)
												|| orderStatusResponse.getRiskResponse().getEbsRiskLevel()
														.equalsIgnoreCase(MarketplacecommerceservicesConstants.RED))
										{
											auditEntry.setStatus(MplPaymentAuditStatusEnum.PENDING);
										}
									}
								}
							}
							else
							{
								auditEntry.setStatus(MplPaymentAuditStatusEnum.PENDING);
								juspayEBSResponseModel.setEbsRiskPercentage(MarketplacecommerceservicesConstants.DEFAULT_RISK);
							}
						}
						//Condition when PG Response status is NOT available or it is NOT charged
						else
						{
							auditEntry.setStatus(MplPaymentAuditStatusEnum.DECLINED);
							auditModel.setIsExpired(Boolean.TRUE);
						}

						LOG.debug("auditEntry status risk ne null------> " + auditEntry.getStatus());

						if (StringUtils.isNotEmpty(orderStatusResponse.getRiskResponse().getEbsBinCountry()))
						{
							juspayEBSResponseModel.setEbs_bin_country(orderStatusResponse.getRiskResponse().getEbsBinCountry());
						}
						if (StringUtils.isNotEmpty(orderStatusResponse.getRiskResponse().getEbsRiskLevel()))
						{
							setEBSRiskLevel(orderStatusResponse.getRiskResponse().getEbsRiskLevel(), juspayEBSResponseModel);
						}
						if (null != orderStatusResponse.getRiskResponse().getEbsRiskPercentage())
						{
							final Double scoreDouble = Double.valueOf(orderStatusResponse.getRiskResponse().getEbsRiskPercentage()
									.doubleValue());
							juspayEBSResponseModel.setEbsRiskPercentage(scoreDouble.toString());
						}
						else
						{
							juspayEBSResponseModel.setEbsRiskPercentage(MarketplacecommerceservicesConstants.DEFAULT_RISK);
						}
						if (StringUtils.isNotEmpty(orderStatusResponse.getRiskResponse().getEbsPaymentStatus())
								&& !orderStatusResponse.getRiskResponse().getEbsPaymentStatus()
										.equalsIgnoreCase(MarketplacecommerceservicesConstants.PAID))
						{
							setEBSRiskStatus(orderStatusResponse.getRiskResponse().getEbsPaymentStatus(), juspayEBSResponseModel);
						}
						else if (StringUtils.isEmpty(orderStatusResponse.getRiskResponse().getEbsPaymentStatus())
								&& !juspayEBSResponseModel.getEbsRiskPercentage().equalsIgnoreCase(
										MarketplacecommerceservicesConstants.DEFAULT_RISK))
						{
							if (MplPaymentAuditStatusEnum.PENDING.equals(auditEntry.getStatus()))
							{
								setEBSRiskStatus(MarketplacecommerceservicesConstants.REVIEW, juspayEBSResponseModel);
							}
							else if (MplPaymentAuditStatusEnum.COMPLETED.equals(auditEntry.getStatus()))
							{
								setEBSRiskStatus(MarketplacecommerceservicesConstants.APPROVED, juspayEBSResponseModel);
							}
							else if (MplPaymentAuditStatusEnum.DECLINED.equals(auditEntry.getStatus())
									|| MplPaymentAuditStatusEnum.EBS_DECLINED.equals(auditEntry.getStatus()))
							{
								setEBSRiskStatus(MarketplacecommerceservicesConstants.REJECTED, juspayEBSResponseModel);
							}
						}
						flag = true;
					}
					//Condition when RiskResponse is NOT available in OrderStatusResponse
					//For NetBanking , we will not get any RISK structure
					else
					{
						if (StringUtils.isNotEmpty(orderStatusResponse.getStatus())
								&& orderStatusResponse.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.CHARGED))
						{
							if (StringUtils.isNotEmpty(ebsDowntime) && ebsDowntime.equalsIgnoreCase("Y"))
							{
								auditEntry.setStatus(MplPaymentAuditStatusEnum.COMPLETED);
								auditModel.setIsExpired(Boolean.TRUE);
							}
							else
							{

								boolean netBanking = false;
								for (final Map.Entry<String, Double> entry : paymentMode.entrySet())
								{
									//if (entry.getKey() != null
									//		&& MarketplacecommerceservicesConstants.NETBANKING.equalsIgnoreCase(entry.getKey()))
									if (entry.getKey() != null
											&& MarketplacecommerceservicesConstants.NETBANKING.equalsIgnoreCase(entry.getKey().trim()))
									{
										LOG.debug("Payment mode netbanking ------> " + orderStatusResponse.getOrderId());

										auditEntry.setStatus(MplPaymentAuditStatusEnum.COMPLETED);
										auditModel.setIsExpired(Boolean.TRUE);
										netBanking = true;
										break;
									}
								}
								// For credit card/debit card and emi , if risk block is not available
								if (!netBanking)
								{
									LOG.debug("Payment mode not netbanking and no risk block present ------> "
											+ orderStatusResponse.getOrderId());

									auditEntry.setStatus(MplPaymentAuditStatusEnum.PENDING);
									juspayEBSResponseModel.setEbsRiskPercentage(MarketplacecommerceservicesConstants.DEFAULT_RISK);
								}
							}
						}
						else
						{
							auditEntry.setStatus(MplPaymentAuditStatusEnum.DECLINED);
							auditModel.setIsExpired(Boolean.TRUE);
						}
						flag = true;
					}

					auditEntry.setResponseDate(new Date());

					final ObjectMapper objectMapper = new ObjectMapper();
					final String jsonResponse = objectMapper.writeValueAsString(orderStatusResponse);
					final String jsonRequest = objectMapper.writeValueAsString(orderStatusRequest);


					if (null != jsonRequest)
					{
						if (jsonRequest.length() >= 255)
						{
							auditEntry.setRequestStructure(jsonRequest.substring(0, 254));
						}
						else
						{
							auditEntry.setRequestStructure(jsonRequest);
						}
					}
					if (null != jsonResponse)
					{
						if (jsonResponse.length() >= 255)
						{
							auditEntry.setResponseStructure(jsonResponse.substring(0, 254));
						}
						else
						{
							auditEntry.setResponseStructure(jsonResponse);
						}
					}


					LOG.debug("auditEntry status risk null------> " + auditEntry.getStatus());

					getModelService().save(auditEntry);

					auditEntryList.add(auditEntry);

					getModelService().save(juspayEBSResponseModel);
					juspayEBSResponseList.add(juspayEBSResponseModel);

					auditModel.setAuditEntries(auditEntryList);
					//changes for JuspayEBSResponseFIX
					auditModel.setRiskData(juspayEBSResponseList);
					getModelService().save(auditModel);

					updateFraudModel(orderModel, juspayEBSResponseModel, auditModel);
				}
			}
			else
			{
				auditEntry.setStatus(MplPaymentAuditStatusEnum.DECLINED);
				auditModel.setIsExpired(Boolean.TRUE);
			}
		}
		//catch (final NullPointerException e)
		//{
		//	throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0001);
		//}		//Commented as it was NullPointerException and not advisable to catch
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E9042);
		}
		catch (final JsonGenerationException e)
		{
			LOG.error("Exception in parsing into json ", e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E9042);
		}
		catch (final JsonMappingException e)
		{
			LOG.error("Exception in parsing into json ", e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E9042);
		}
		catch (final IOException e)
		{
			LOG.error("Exception in parsing into json ", e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E9042);
		}
		//PMD Fix
		//		catch (final EtailNonBusinessExceptions e)
		//		{
		//			throw e;
		//		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return flag;
	}


	/**
	 * @Decsription : Fetch Order Details Based on GUID for new Payment Soln - Order before payment
	 * @param: guid
	 */
	@Override
	public OrderModel fetchOrderOnGUID(final String guid)
	{
		return getMplPaymentDao().fetchOrderOnGUID(guid);
	}





	/**
	 * This method updates the fraud model wrt order model
	 *
	 * @param orderModel
	 * @param juspayEBSResponseModel
	 * @param mplAudit
	 * @throws EtailNonBusinessExceptions
	 */
	private void updateFraudModel(final OrderModel orderModel, final JuspayEBSResponseDataModel juspayEBSResponseModel,
			final MplPaymentAuditModel mplAudit) throws EtailNonBusinessExceptions
	{
		if (null != juspayEBSResponseModel && StringUtils.isNotEmpty(juspayEBSResponseModel.getEbsRiskPercentage())
				&& !juspayEBSResponseModel.getEbsRiskPercentage().equalsIgnoreCase("-1.0"))
		{
			getMplFraudModelService().updateFraudModel(orderModel, mplAudit, juspayEBSResponseModel);
		}
	}




	/*
	 * (non-Javadoc)
	 * 
	 * @see * SprintPaymentFixes:- This method is setting paymentTransactionModel and the paymentTransactionEntryModel
	 * against the cart for non-COD from OMS Submit Order Job de.hybris.platform.core.model.order.OrderModel)
	 */
	@Override
	public boolean createPaymentTransactionFromSubmitOrderJob(final OrderModel orderModel)
	{
		boolean returnFlag = false;
		try
		{
			PaymentInfoModel payInfo = null;
			String paymentModeFromInfo = null;
			if (null != orderModel.getPaymentInfo())
			{
				payInfo = orderModel.getPaymentInfo();
				paymentModeFromInfo = getPaymentModeFrompayInfo(payInfo);
			}
			else
			{
				paymentModeFromInfo = orderModel.getModeOfOrderPayment();
			}

			LOG.info("Creating Payment transaction from Submit Order Job:- paymentModeFromInfo :- " + paymentModeFromInfo
					+ " For Order ID:- " + orderModel.getCode());

			final List<OrderModel> orderList = new ArrayList<OrderModel>();
			orderList.add(orderModel);
			orderList.addAll(orderModel.getChildOrders());

			final Map<String, Double> paymentMode = new HashMap<String, Double>();
			paymentMode.put(paymentModeFromInfo, orderModel.getTotalPriceWithConv());

			LOG.info("Creating Payment transaction from Submit Order Job:- ModeOfPayment :- " + paymentMode + " For Order ID:- "
					+ orderModel.getCode());

			if (!paymentModeFromInfo.equalsIgnoreCase(MarketplacecommerceservicesConstants.COD))
			{
				LOG.info("Creating Payment transaction from Submit Order Job:- ModeOfPayment Prepaid" + " For Order ID:- "
						+ orderModel.getCode());

				final String cartGuid = orderModel.getGuid();
				LOG.info("Order  GUID:- " + cartGuid);

				MplPaymentAuditModel auditModel = null;
				if (StringUtils.isNotEmpty(cartGuid))
				{
					auditModel = getMplOrderDao().getAuditList(cartGuid);
				}

				if (null != auditModel && StringUtils.isNotEmpty(auditModel.getAuditId()))
				{
					LOG.info("Audit ID:- :- " + auditModel.getAuditId() + "for Order ID:- " + orderModel.getCode());
					final PaymentService juspayService = new PaymentService();

					juspayService.setBaseUrl(getConfigurationService().getConfiguration().getString(
							MarketplacecommerceservicesConstants.JUSPAYBASEURL));
					juspayService.withKey(
							getConfigurationService().getConfiguration().getString(
									MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY)).withMerchantId(
							getConfigurationService().getConfiguration()
									.getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTID));

					final GetOrderStatusRequest orderStatusRequest = new GetOrderStatusRequest();
					orderStatusRequest.withOrderId(auditModel.getAuditId());

					//getting the response by calling get Order Status service
					final GetOrderStatusResponse orderStatusResponse = juspayService.getOrderStatus(orderStatusRequest);

					LOG.info("orderStatusResponse for Order Id:- " + orderModel.getCode() + "  and Audit ID:- "
							+ auditModel.getAuditId() + "***********");
					LOG.info("orderStatusResponse " + orderStatusResponse);

					if (orderStatusResponse.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.CHARGED))
					{

						for (final OrderModel so : orderList)
						{
							LOG.info("Order ID creating Payment Transaction " + so);
							so.setModeOfOrderPayment(paymentModeFromInfo);
							setPaymentTransactionFromJob(orderStatusResponse, paymentMode, so);
						}
						//modelService.saveAll(orderList);
						returnFlag = true;
						LOG.info("Payment transaction Created:- order ID:- " + orderModel.getCode());
					}
					//					final List<JuspayWebhookModel> hooks = getMplProcessOrderDao().getEventsForPendingOrders(auditModel.getAuditId());
					//					//}
					//					for (final JuspayWebhookModel juspayWebhook : hooks)
					//					{
					//						if (null != juspayWebhook.getOrderStatus()
					//								&& juspayWebhook.getOrderStatus().getStatus().equalsIgnoreCase("charged"))
					//						{
					//							final JuspayOrderStatusModel juspayOrderStatusModel = juspayWebhook.getOrderStatus();
					//
					//
					//							final GetOrderStatusResponse orderStatusResponse = getJuspayOrderResponseConverter().convert(
					//									juspayOrderStatusModel);
					//
					//							for (final OrderModel so : orderList)
					//							{
					//								setPaymentTransactionFromJob(orderStatusResponse, paymentMode, so);
					//								so.setModeOfOrderPayment(paymentModeFromInfo);
					//							}
					//							modelService.saveAll(orderList);
					//							returnFlag = true;
					//							break;
					//						}
					//					}
				}
				else
				{
					LOG.info("audit Model doesnot exist for Order Id:- " + orderModel.getCode());
					returnFlag = false;
				}
			}
			else
			{
				LOG.info("Creating Payment transaction from Submit Order Job:- ModeOfPayment COD");
				for (final OrderModel so : orderList)
				{
					LOG.info("Order ID creating Payment Transaction " + so);
					setPaymentTransactionForCODFromSubmitProcess(paymentMode, so);
					so.setModeOfOrderPayment(paymentModeFromInfo);
				}
				//modelService.saveAll(orderList);
				returnFlag = true;
				LOG.info("Payment transaction Created:- order ID:- " + orderModel.getCode());
			}
		}
		catch (final ModelSavingException e)
		{
			LOG.error("Creating Payment transaction from Submit Order Job-  ModelSavingException:- ", e);
			returnFlag = false;
		}
		catch (final AdapterException e)
		{
			LOG.error("Creating Payment transaction from Submit Order Job - Error with connection", e);
			//throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0023);
		}
		catch (final Exception e)
		{
			LOG.error("Creating Payment transaction from Submit Order Job Exception:- ", e);
			returnFlag = false;
		}

		return returnFlag;
	}

	/*
	 * @desc getPaymentModeFrompayInfo
	 * 
	 * @see SprintPaymentFixes:- ModeOfpayment set same as in Payment Info
	 */
	@Override
	public String getPaymentModeFrompayInfo(final PaymentInfoModel payInfo)
	{
		if (null != payInfo)
		{
			if (payInfo instanceof CODPaymentInfoModel)
			{
				return MarketplacecommerceservicesConstants.COD;
			}
			else if (payInfo instanceof CreditCardPaymentInfoModel)
			{
				return "Credit Card";
			}
			else if (payInfo instanceof DebitCardPaymentInfoModel)
			{
				return "Debit Card";
			}
			else if (payInfo instanceof NetbankingPaymentInfoModel)
			{
				return "Netbanking";
			}
			else if (payInfo instanceof EMIPaymentInfoModel)
			{
				return "EMI";
			}
			//Paytm Changes
			else if (payInfo instanceof ThirdPartyWalletInfoModel)
			{
				if (StringUtils.isNotEmpty(((ThirdPartyWalletInfoModel) payInfo).getProviderName())
						&& "PAYTM".equalsIgnoreCase(((ThirdPartyWalletInfoModel) payInfo).getProviderName()))
				{
					return "PAYTM";
				}
				else
				{
					return "CC";
				}
			}
			else
			{
				return "CC";
			}
		}
		else
		{
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see SprintPaymentFixes:- This method is setting paymentTransactionModel and the paymentTransactionEntryModel
	 * against the cart for pre paid from OMS Submit Order Job
	 */
	@Override
	public void setPaymentTransactionFromJob(final GetOrderStatusResponse orderStatusResponse,
			final Map<String, Double> paymentMode, final OrderModel order)
	{
		try
		{
			final List<PaymentTransactionModel> paymentTransactionList = new ArrayList<PaymentTransactionModel>();
			PaymentTransactionModel payTranModel = null;

			String checkValues = "".intern();
			String[] parts = null;
			String saveCard = "".intern();
			String sameAsShipping = "".intern();
			if (null != orderStatusResponse)
			{
				List<PaymentTransactionEntryModel> paymentTransactionEntryList = new ArrayList<PaymentTransactionEntryModel>();
				LOG.info(MarketplacecommerceservicesConstants.JUSPAY_ORDER_STAT_RESP + orderStatusResponse);
				if (StringUtils.isNotEmpty(orderStatusResponse.getUdf10()))
				{
					checkValues = orderStatusResponse.getUdf10();
				}
				if (checkValues.contains(MarketplacecommerceservicesConstants.CONCTASTRING))
				{
					parts = checkValues.split(MarketplacecommerceservicesConstants.SPLITSTRING);
					saveCard = parts[0];
					sameAsShipping = parts[1];
				}
				for (final Map.Entry<String, Double> entry : paymentMode.entrySet())
				{
					paymentTransactionEntryList = getMplPaymentTransactionService().createPaymentTranEntryFromSubmitOrderJob(
							orderStatusResponse, order, entry, paymentTransactionEntryList);
				}
				payTranModel = getMplPaymentTransactionService().createPaymentTranFromSubmitOrderJob(order, orderStatusResponse,
						paymentTransactionEntryList);
				paymentTransactionList.add(payTranModel);
			}

			order.setPaymentTransactions(paymentTransactionList);
			getModelService().save(order);
			if (saveCard.equalsIgnoreCase(MarketplacecommerceservicesConstants.TRUE)
					&& null != orderStatusResponse.getCardResponse()
					&& StringUtils.isNotEmpty(orderStatusResponse.getCardResponse().getCardReference()))
			{
				//setting as saved card
				saveCards(orderStatusResponse, paymentMode, order, sameAsShipping);
			}
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @desc SprintPaymentFixes:- This method is setting paymentTransactionModel and the paymentTransactionEntryModel
	 * against the cart for COD from OMS Submit Order Job
	 */
	@Override
	public void setPaymentTransactionForCODFromSubmitProcess(final Map<String, Double> paymentMode, final OrderModel orderModel)
			throws EtailNonBusinessExceptions
	{
		try
		{
			if (null != orderModel)
			{
				final List<PaymentTransactionModel> paymentTransactionList = new ArrayList<PaymentTransactionModel>();

				final List<PaymentTransactionEntryModel> paymentTransactionEntryList = new ArrayList<PaymentTransactionEntryModel>();

				final PaymentTransactionModel paymentTransactionModel = getModelService().create(PaymentTransactionModel.class);
				final Date date = new Date();
				final String codCode = getCodCodeGenerator().generate().toString();

				final PaymentTransactionEntryModel paymentTransactionEntry = getModelService().create(
						PaymentTransactionEntryModel.class);

				// SprintPaymentFixes Multiple Payment Transaction with success status one with 0.0 and another with proper amount
				paymentTransactionEntry
						.setCode(MarketplacecommerceservicesConstants.COD + codCode + "-" + System.currentTimeMillis());
				//SONAR FIX updated
				if (null != orderModel.getTotalPriceWithConv() && orderModel.getTotalPriceWithConv().doubleValue() > 0.0)
				{
					paymentTransactionEntry.setAmount(BigDecimal.valueOf(orderModel.getTotalPriceWithConv().doubleValue()));
				}

				paymentTransactionEntry.setTime(date);
				paymentTransactionEntry.setCurrency(orderModel.getCurrency());
				paymentTransactionEntry.setType(PaymentTransactionType.COD_PAYMENT);
				paymentTransactionEntry.setTransactionStatus(MarketplacecommerceservicesConstants.SUCCESS);

				PaymentTypeModel paymentTypeModelCOD = modelService.create(PaymentTypeModel.class);
				paymentTypeModelCOD.setMode(MarketplacecommerceservicesConstants.COD);
				paymentTypeModelCOD.setBaseStore(baseStoreService.getCurrentBaseStore());
				paymentTypeModelCOD = flexibleSearchService.getModelByExample(paymentTypeModelCOD);
				paymentTransactionEntry.setPaymentMode(paymentTypeModelCOD);

				getModelService().save(paymentTransactionEntry);
				paymentTransactionEntryList.add(paymentTransactionEntry);

				if (null != orderModel.getPaymentInfo())
				{
					paymentTransactionModel.setInfo(orderModel.getPaymentInfo());
				}

				paymentTransactionModel
						.setCode(MarketplacecommerceservicesConstants.COD + codCode + "-" + System.currentTimeMillis());

				paymentTransactionModel.setCreationtime(date);
				paymentTransactionModel.setCurrency(orderModel.getCurrency());
				paymentTransactionModel.setEntries(paymentTransactionEntryList);
				paymentTransactionModel.setPaymentProvider(getConfigurationService().getConfiguration().getString("payment.cod"));
				paymentTransactionModel.setOrder(orderModel);

				// SprintPaymentFixes Multiple Payment Transaction with success status one with 0.0 and another with proper amount
				//SONAR FIX updated
				if (null != orderModel.getTotalPriceWithConv() && orderModel.getTotalPriceWithConv().doubleValue() > 0.0)
				{
					paymentTransactionModel.setPlannedAmount(BigDecimal.valueOf(orderModel.getTotalPriceWithConv().doubleValue()));
				}

				if (StringUtils.isNotEmpty(paymentTransactionEntryList.get(0).getTransactionStatus())
						&& paymentTransactionEntryList.get(0).getTransactionStatus()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
				{
					paymentTransactionModel.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
				}
				else
				{
					paymentTransactionModel.setStatus(MarketplacecommerceservicesConstants.FAILURE);
				}


				getModelService().save(paymentTransactionModel);
				paymentTransactionList.add(paymentTransactionModel);
				orderModel.setPaymentTransactions(paymentTransactionList);
				getModelService().save(orderModel);
			}
		}
		catch (final ModelSavingException e)
		{
			LOG.error("Inside setPaymentTransactionForCODFromSubmitProcess::Exception while saving cart with ", e); //Sonar fix
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9214);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception while setPaymentTransactionForCOD ", ex);
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B9214);
		}
	}


	//Getters and Setters

	/**
	 * @return the mplpaymentDao
	 */
	public MplPaymentDao getMplPaymentDao()
	{
		return mplPaymentDao;
	}


	/**
	 * @param mplPaymentDao
	 *           the mplpaymentDao to set
	 */
	public void setMplPaymentDao(final MplPaymentDao mplPaymentDao)
	{
		this.mplPaymentDao = mplPaymentDao;
	}


	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}


	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}


	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}


	/**
	 * @return the cartService
	 */
	public CartService getCartService()
	{
		return cartService;
	}


	/**
	 * @param cartService
	 *           the cartService to set
	 */
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}


	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}


	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}


	/**
	 * @return the i18NService
	 */
	public I18NService getI18NService()
	{
		return i18NService;
	}


	/**
	 * @param i18nService
	 *           the i18NService to set
	 */
	public void setI18NService(final I18NService i18nService)
	{
		i18NService = i18nService;
	}


	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}


	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * @return the mplCommerceCartService
	 */
	public MplCommerceCartService getMplCommerceCartService()
	{
		return mplCommerceCartService;
	}


	/**
	 * @param mplCommerceCartService
	 *           the mplCommerceCartService to set
	 */
	public void setMplCommerceCartService(final MplCommerceCartService mplCommerceCartService)
	{
		this.mplCommerceCartService = mplCommerceCartService;
	}


	/**
	 * @return the discountUtility
	 */
	public DiscountUtility getDiscountUtility()
	{
		return discountUtility;
	}


	/**
	 * @param discountUtility
	 *           the discountUtility to set
	 */
	public void setDiscountUtility(final DiscountUtility discountUtility)
	{
		this.discountUtility = discountUtility;
	}


	/**
	 * @return the promotionsService
	 */
	public PromotionsService getPromotionsService()
	{
		return promotionsService;
	}


	/**
	 * @param promotionsService
	 *           the promotionsService to set
	 */
	public void setPromotionsService(final PromotionsService promotionsService)
	{
		this.promotionsService = promotionsService;
	}


	/**
	 * @return the commerceCartCalculationStrategy
	 */
	public CommerceCartCalculationStrategy getCommerceCartCalculationStrategy()
	{
		return commerceCartCalculationStrategy;
	}


	/**
	 * @param commerceCartCalculationStrategy
	 *           the commerceCartCalculationStrategy to set
	 */
	public void setCommerceCartCalculationStrategy(final CommerceCartCalculationStrategy commerceCartCalculationStrategy)
	{
		this.commerceCartCalculationStrategy = commerceCartCalculationStrategy;
	}


	/**
	 * @return the juspayOrderIdGenerator
	 */
	public PersistentKeyGenerator getJuspayOrderIdGenerator()
	{
		return juspayOrderIdGenerator;
	}


	/**
	 * @param juspayOrderIdGenerator
	 *           the juspayOrderIdGenerator to set
	 */
	public void setJuspayOrderIdGenerator(final PersistentKeyGenerator juspayOrderIdGenerator)
	{
		this.juspayOrderIdGenerator = juspayOrderIdGenerator;
	}


	/**
	 * @return the calculationStrategy
	 */
	public MplCommerceCartCalculationStrategy getCalculationStrategy()
	{
		return calculationStrategy;
	}


	/**
	 * @param calculationStrategy
	 *           the calculationStrategy to set
	 */
	public void setCalculationStrategy(final MplCommerceCartCalculationStrategy calculationStrategy)
	{
		this.calculationStrategy = calculationStrategy;
	}


	/**
	 * @return the mplExtendedCartConverter
	 */
	public Converter<CartModel, CartData> getMplExtendedCartConverter()
	{
		return mplExtendedCartConverter;
	}


	/**
	 * @param mplExtendedCartConverter
	 *           the mplExtendedCartConverter to set
	 */
	public void setMplExtendedCartConverter(final Converter<CartModel, CartData> mplExtendedCartConverter)
	{
		this.mplExtendedCartConverter = mplExtendedCartConverter;
	}


	/**
	 * @return the mplPaymentTransactionService
	 */
	public MplPaymentTransactionService getMplPaymentTransactionService()
	{
		return mplPaymentTransactionService;
	}


	/**
	 * @param mplPaymentTransactionService
	 *           the mplPaymentTransactionService to set
	 */
	public void setMplPaymentTransactionService(final MplPaymentTransactionService mplPaymentTransactionService)
	{
		this.mplPaymentTransactionService = mplPaymentTransactionService;
	}


	/**
	 * @return the codCodeGenerator
	 */
	public PersistentKeyGenerator getCodCodeGenerator()
	{
		return codCodeGenerator;
	}


	/**
	 * @param codCodeGenerator
	 *           the codCodeGenerator to set
	 */
	public void setCodCodeGenerator(final PersistentKeyGenerator codCodeGenerator)
	{
		this.codCodeGenerator = codCodeGenerator;
	}


	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}


	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}


	/**
	 * @return the mplVoucherService
	 */
	public MplVoucherService getMplVoucherService()
	{
		return mplVoucherService;
	}


	/**
	 * @param mplVoucherService
	 *           the mplVoucherService to set
	 */
	public void setMplVoucherService(final MplVoucherService mplVoucherService)
	{
		this.mplVoucherService = mplVoucherService;
	}


	/**
	 * @return the orderStatusSpecifier
	 */
	public OrderStatusSpecifier getOrderStatusSpecifier()
	{
		return orderStatusSpecifier;
	}


	/**
	 * @param orderStatusSpecifier
	 *           the orderStatusSpecifier to set
	 */
	public void setOrderStatusSpecifier(final OrderStatusSpecifier orderStatusSpecifier)
	{
		this.orderStatusSpecifier = orderStatusSpecifier;
	}


	/**
	 * @return the mplFraudModelService
	 */
	public MplFraudModelService getMplFraudModelService()
	{
		return mplFraudModelService;
	}


	/**
	 * @param mplFraudModelService
	 *           the mplFraudModelService to set
	 */
	public void setMplFraudModelService(final MplFraudModelService mplFraudModelService)
	{
		this.mplFraudModelService = mplFraudModelService;
	}

	/**
	 * @return the walletOrderIdGenerator
	 */
	public PersistentKeyGenerator getWalletOrderIdGenerator()
	{
		return walletOrderIdGenerator;
	}


	/**
	 * @param walletOrderIdGenerator
	 *           the walletOrderIdGenerator to set
	 */
	public void setWalletOrderIdGenerator(final PersistentKeyGenerator walletOrderIdGenerator)
	{
		this.walletOrderIdGenerator = walletOrderIdGenerator;
	}


	/**
	 *
	 * @return String
	 */
	@Override
	public String createWalletPaymentId()
	{
		return getWalletOrderIdGenerator().generate().toString();
	}


	/**
	 * This method makes entry for mRupee orders in AUdit table
	 *
	 * @param status
	 * @param channelWeb
	 * @param guid
	 * @param refNo
	 *
	 */
	@Override
	public void entryInTPWaltAudit(final String status, final String channelWeb, final String guid, final String refNo)

	{
		try
		{
			Assert.notNull(refNo, "Parameter refNo cannot be null.");

			final MplPaymentAuditModel auditModel = getMplPaymentDao().getWalletAuditEntries(refNo);

			if (null != auditModel)
			{
				LOG.info("Audit Model with ref no>>>" + refNo);
				List<MplPaymentAuditEntryModel> collection = auditModel.getAuditEntries();
				final List<MplPaymentAuditEntryModel> auditEntryList = new ArrayList<MplPaymentAuditEntryModel>();
				if (null == collection || collection.isEmpty())
				{
					collection = new ArrayList<MplPaymentAuditEntryModel>();
				}
				auditEntryList.addAll(collection);
				final MplPaymentAuditEntryModel auditEntry = getModelService().create(MplPaymentAuditEntryModel.class);
				auditEntry.setAuditId(refNo);

				//Commented for Mobile use

				//				if (null != request.getParameter("STATUS") && !("S".equalsIgnoreCase(request.getParameter("STATUS"))))
				//				{
				//					auditEntry.setStatus(MplPaymentAuditStatusEnum.DECLINED);
				//				}
				//				else
				//				{
				//					auditEntry.setStatus(MplPaymentAuditStatusEnum.COMPLETED);
				//				}
				//				if (null != request.getParameter("MWREFNO"))
				//				{
				//					auditEntry.setMWRefNo(request.getParameter("MWREFNO"));
				//				}

				if (null != status && !(MarketplacecommerceservicesConstants.SUCCESS.equalsIgnoreCase(status)))
				{
					auditEntry.setStatus(MplPaymentAuditStatusEnum.DECLINED);
				}
				else
				{
					auditEntry.setStatus(MplPaymentAuditStatusEnum.COMPLETED);
				}
				getModelService().save(auditEntry);
				auditEntryList.add(auditEntry);
				auditModel.setAuditEntries(auditEntryList);
				auditModel.setIsExpired(Boolean.TRUE); //Sonar fix
				getModelService().save(auditModel);
				LOG.info("Saved existing Audit Model with ref no>>>" + refNo);
			}
			else
			{
				final CartModel cartModel = getMplPaymentDao().getCart(guid);
				//	final List<ThirdPartyAuditEntryModel> auditEntryList = new ArrayList<ThirdPartyAuditEntryModel>();
				final List<MplPaymentAuditEntryModel> auditEntryList = new ArrayList<MplPaymentAuditEntryModel>();
				final MplPaymentAuditEntryModel auditEntry = getModelService().create(MplPaymentAuditEntryModel.class);
				auditEntry.setAuditId(refNo);
				//auditEntry.setTwAuditId(refNo);
				auditEntry.setStatus(MplPaymentAuditStatusEnum.CREATED);
				getModelService().save(auditEntry);
				auditEntryList.add(auditEntry);
				final MplPaymentAuditModel newAuditModel = getModelService().create(MplPaymentAuditModel.class);
				newAuditModel.setChannel(GenericUtilityMethods.returnChannelData(channelWeb));
				newAuditModel.setAuditId(refNo);
				newAuditModel.setCartGUID(guid);
				newAuditModel.setRequestDate(new Date());
				newAuditModel.setAuditEntries(auditEntryList);
				if (cartModel != null && cartModel.getTotalPrice() != null)
				{
					newAuditModel.setPaymentAmount(cartModel.getTotalPrice());
				}

				getModelService().save(newAuditModel);

				LOG.info("Creating new Audit Model with ref no>>>" + refNo);
			}

		}

		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}

		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * This method saves payment info model for mRupee orders and the returning the order
	 *
	 * @param custName
	 * @param entries
	 * @param cart
	 * @param refernceCode
	 * @return AbstractOrderModel
	 *
	 */
	@Override
	public AbstractOrderModel saveTPWalletPaymentInfo(final String custName, final List<AbstractOrderEntryModel> entries,
			final AbstractOrderModel cart, final String refernceCode)
	{
		try
		{
			LOG.info("Inside saveTPWalletPaymentInfo with ref no>>>" + refernceCode);
			final ThirdPartyWalletInfoModel tpWalletInfoModel = getModelService().create(ThirdPartyWalletInfoModel.class);

			//Commented for Mobile use
			//final String walletOwner = StringUtils.trim(cart.getUser().getName());
			//	tpWalletInfoModel.setCode(MarketplacecommerceservicesConstants.MRUPEE + "-" + request.getParameter("REFNO"));
			tpWalletInfoModel.setCode(MarketplacecommerceservicesConstants.MRUPEE + "-" + refernceCode);

			tpWalletInfoModel.setWalletOwner(custName);
			tpWalletInfoModel.setProviderName(MarketplacecommerceservicesConstants.MRUPEE_OPTION);
			tpWalletInfoModel.setUser(getUserService().getCurrentUser());

			//saving the tpWalletInfoModel
			//try
			//{
			if (null == cart.getPaymentInfo() && !OrderStatus.PAYMENT_TIMEOUT.equals(cart.getStatus()))
			{
				getModelService().save(tpWalletInfoModel);
				//setting paymentinfo in cart
				cart.setPaymentInfo(tpWalletInfoModel);
				cart.setPaymentAddress(cart.getDeliveryAddress());
			}
			else if (null != cart.getPaymentInfo())
			{
				LOG.error("Order already has payment info -- not saving tpWalletInfoModel>>>" + cart.getPaymentInfo().getCode());
			}
			else
			{
				LOG.error("Order does not have payment info tpWalletInfoModel -- " + ERROR_PAYMENT + cart.getCode());
			}

		}
		catch (final ModelSavingException e)
		{
			LOG.error(MarketplacecommerceservicesConstants.PAYMENT_EXC_LOG + e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		//returning the cart
		return cart;
	}


	/**
	 * This method set payment transaction for mRupee orders
	 *
	 * @param paymentMode
	 * @param abstractOrderModel
	 * @param refernceCode
	 *
	 */
	@Override
	public void setTPWalletPaymentTransaction(final Map<String, Double> paymentMode, final AbstractOrderModel abstractOrderModel,
			final String refernceCode, final Double transactionAmount)
	{
		LOG.info("Inside setTPWalletPaymentTransaction with ref no>>>" + refernceCode);
		try
		{
			Collection<PaymentTransactionModel> collection = abstractOrderModel.getPaymentTransactions();
			final List<PaymentTransactionModel> paymentTransactionList = new ArrayList<PaymentTransactionModel>();
			if (CollectionUtils.isEmpty(collection))

			{
				collection = new ArrayList<PaymentTransactionModel>();
			}

			paymentTransactionList.addAll(collection);

			final List<PaymentTransactionEntryModel> paymentTransactionEntryList = new ArrayList<PaymentTransactionEntryModel>();

			final PaymentTransactionModel paymentTransactionModel = getModelService().create(PaymentTransactionModel.class);
			final Date date = new Date();

			//Commented for Mobile use
			//	final String refernceCode = request.getParameter("REFNO");

			final PaymentTransactionEntryModel paymentTransactionEntry = getModelService()
					.create(PaymentTransactionEntryModel.class);
			paymentTransactionEntry.setCode(MarketplacecommerceservicesConstants.MRUPEE + "-" + refernceCode + "-"
					+ System.currentTimeMillis());
			paymentTransactionEntry.setAmount(BigDecimal.valueOf(transactionAmount.doubleValue()));
			paymentTransactionEntry.setTime(date);
			paymentTransactionEntry.setCurrency(abstractOrderModel.getCurrency());
			//To Change this
			paymentTransactionEntry.setType(PaymentTransactionType.CAPTURE);
			paymentTransactionEntry.setTransactionStatus(MarketplacecommerceservicesConstants.SUCCESS);

			PaymentTypeModel paymentTypeForTPWallet = modelService.create(PaymentTypeModel.class);
			paymentTypeForTPWallet.setMode(MarketplacecommerceservicesConstants.MRUPEE_CODE);
			paymentTypeForTPWallet = flexibleSearchService.getModelByExample(paymentTypeForTPWallet);
			paymentTransactionEntry.setPaymentMode(paymentTypeForTPWallet);

			paymentTransactionEntry.setRequestToken(refernceCode);

			if (null == abstractOrderModel.getPaymentInfo())
			{
				getModelService().save(paymentTransactionEntry);
				paymentTransactionEntryList.add(paymentTransactionEntry);
			}
			else
			{
				LOG.error("PaymentInfo already available.....not saving any more paymentTransactionEntry model");
			}

			if (null != abstractOrderModel.getPaymentInfo())
			{
				paymentTransactionModel.setInfo(abstractOrderModel.getPaymentInfo());
			}

			paymentTransactionModel.setCode(MarketplacecommerceservicesConstants.MRUPEE + "-" + refernceCode + "-"
					+ System.currentTimeMillis());

			paymentTransactionModel.setRequestToken(refernceCode);

			paymentTransactionModel.setCreationtime(date);
			paymentTransactionModel.setCurrency(abstractOrderModel.getCurrency());
			paymentTransactionModel.setEntries(paymentTransactionEntryList);
			paymentTransactionModel.setPaymentProvider(MarketplacecommerceservicesConstants.MRUPEE);
			paymentTransactionModel.setOrder(abstractOrderModel);
			paymentTransactionModel.setPlannedAmount(BigDecimal.valueOf(transactionAmount.doubleValue()));
			//the flag is used to identify whether all the entries in the PaymentTransactionModel are successful or not. If all are successful then flag is set as true and status against paymentTransactionModel is set as success

			if (StringUtils.isNotEmpty(paymentTransactionEntryList.get(0).getTransactionStatus())
					&& paymentTransactionEntryList.get(0).getTransactionStatus()
							.equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
			{
				paymentTransactionModel.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
			}
			else
			{
				paymentTransactionModel.setStatus(MarketplacecommerceservicesConstants.FAILURE);
			}


			if (null == abstractOrderModel.getPaymentInfo())
			{
				getModelService().save(paymentTransactionModel);
				paymentTransactionList.add(paymentTransactionModel);
				abstractOrderModel.setPaymentTransactions(paymentTransactionList);
				getModelService().save(abstractOrderModel);
			}
			else if (null != abstractOrderModel.getPaymentInfo())
			{
				LOG.error("PaymentInfo already available.....not saving any more paymentTransactionModel and not setting against the abstractOrderModel>>>"
						+ abstractOrderModel.getPaymentInfo().getCode());
			}
			else
			{
				LOG.error(ERROR_PAYMENT + abstractOrderModel.getCode());
			}

		}
		catch (final ModelSavingException e)
		{
			LOG.error("Inside setTPWalletPaymentTransaction::Exception while saving cart with ", e); //Sonar fix
			throw new EtailNonBusinessExceptions(e, ": Exception while setPaymentTransactionFor Third Party Wallet");
		}
		catch (final Exception ex)
		{
			LOG.error("Exception while setPaymentTransactionFor Third Party Wallet", ex);
			throw new EtailNonBusinessExceptions(ex);
		}


	}


	/**
	 * This method fetches audit model corresponding to a reference number
	 *
	 * @param refNo
	 * @return MplPaymentAuditModel
	 *
	 */
	@Override
	public MplPaymentAuditModel getWalletAuditEntries(final String refNo)
	{
		MplPaymentAuditModel auditModel = new MplPaymentAuditModel();
		auditModel = getMplPaymentDao().getWalletAuditEntries(refNo);
		return auditModel;
	}


	/**
	 * @return the mplOrderDao
	 */
	public MplOrderDao getMplOrderDao()
	{
		return mplOrderDao;
	}


	/**
	 * @param mplOrderDao
	 *           the mplOrderDao to set
	 */
	public void setMplOrderDao(final MplOrderDao mplOrderDao)
	{
		this.mplOrderDao = mplOrderDao;
	}


	/**
	 * @return the mplProcessOrderDao
	 */
	public MplProcessOrderDao getMplProcessOrderDao()
	{
		return mplProcessOrderDao;
	}


	/**
	 * @param mplProcessOrderDao
	 *           the mplProcessOrderDao to set
	 */
	public void setMplProcessOrderDao(final MplProcessOrderDao mplProcessOrderDao)
	{
		this.mplProcessOrderDao = mplProcessOrderDao;
	}


	/**
	 * @return the juspayOrderResponseConverter
	 */
	public Converter<JuspayOrderStatusModel, GetOrderStatusResponse> getJuspayOrderResponseConverter()
	{
		return juspayOrderResponseConverter;
	}


	/**
	 * @param juspayOrderResponseConverter
	 *           the juspayOrderResponseConverter to set
	 */
	public void setJuspayOrderResponseConverter(
			final Converter<JuspayOrderStatusModel, GetOrderStatusResponse> juspayOrderResponseConverter)
	{
		this.juspayOrderResponseConverter = juspayOrderResponseConverter;
	}


	//Added for TPR-1348
	@Override
	public String doRefundPayment(final List<OrderEntryModel> orderEntryModel)
	{
		Double totalRefundAmount = Double.valueOf(0.0);
		PaymentTransactionModel paymentTransactionModel = null;
		for (final OrderEntryModel orderEntry : orderEntryModel)
		{
			//H2 Priority Sprint1
			final Double chargeBack = orderEntry.getChargeback() != null ? orderEntry.getChargeback() : NumberUtils.DOUBLE_ZERO;
			totalRefundAmount = Double.valueOf((orderEntry.getNetAmountAfterAllDisc().doubleValue() - chargeBack.doubleValue()));
		}


		if (CollectionUtils.isNotEmpty(orderEntryModel))
		{
			//		Mrupee implementation
			final OrderModel order = orderEntryModel.get(0).getOrder();
			final String uniqueRequestId = mplJusPayRefundService.getRefundUniqueRequestId();
			if ((null != order.getIsWallet() && WalletEnum.NONWALLET.toString().equalsIgnoreCase(order.getIsWallet().getCode()))
					|| null == order.getIsWallet())
			{
				try
				{
					if (totalRefundAmount.doubleValue() > 0)
					{
						paymentTransactionModel = mplJusPayRefundService.doRefund(orderEntryModel.get(0).getOrder(), totalRefundAmount,
								PaymentTransactionType.RETURN, uniqueRequestId);
						if (null != paymentTransactionModel)
						{
							mplJusPayRefundService.attachPaymentTransactionModel(orderEntryModel.get(0).getOrder(),
									paymentTransactionModel);
							for (final OrderEntryModel orderEntry : orderEntryModel)
							{
								//H2 Priority Sprint1
								/*
								 * final Double chargeBack = orderEntry.getChargeback() != null ? orderEntry.getChargeback() :
								 * NumberUtils.DOUBLE_ZERO;
								 */

								// If CosignmentEnteries are present then update OMS with
								// the state.
								ConsignmentStatus newStatus = null;
								if (orderEntry != null && CollectionUtils.isNotEmpty(orderEntry.getConsignmentEntries()))
								{
									// ConsignmentModel consignmentModel = orderEntry
									// .getConsignmentEntries().iterator().next()
									// .getConsignment();
									if (StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(), "SUCCESS"))
									{
										newStatus = ConsignmentStatus.RETURN_COMPLETED;
									}
									else if (StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(), "PENDING"))
									{
										newStatus = ConsignmentStatus.REFUND_INITIATED;
										final RefundTransactionMappingModel refundTransactionMappingModel = getModelService().create(
												RefundTransactionMappingModel.class);
										refundTransactionMappingModel.setRefundedOrderEntry(orderEntry);
										refundTransactionMappingModel.setJuspayRefundId(paymentTransactionModel.getCode());
										refundTransactionMappingModel.setCreationtime(new Date());
										refundTransactionMappingModel.setRefundType(JuspayRefundType.RETURN);
										refundTransactionMappingModel.setRefundAmount(orderEntry.getNetAmountAfterAllDisc());//TISPRO-216 : Refund amount Set in RTM
										getModelService().save(refundTransactionMappingModel);
									}
									else
									{
										newStatus = ConsignmentStatus.REFUND_IN_PROGRESS;
									}
									// getModelService().save(consignmentModel);
									mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel,
											orderEntry.getNetAmountAfterAllDisc(), newStatus, null);

									//Start TISPRD-871
									if (newStatus.equals(ConsignmentStatus.RETURN_COMPLETED))
									{
										orderEntry.setJuspayRequestId(uniqueRequestId);
										getModelService().save(orderEntry);
									}
								}
							}
						}
						else
						{

							//TISSIT-1801
							LOG.error("Manual Refund Failed");
							for (final OrderEntryModel orderEntry : orderEntryModel)
							{
								mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel,
										orderEntry.getNetAmountAfterAllDisc(), ConsignmentStatus.REFUND_IN_PROGRESS, null);
							}

							paymentTransactionModel = mplJusPayRefundService.createPaymentTransactionModel(orderEntryModel.get(0)
									.getOrder(), FAILURE_KEY, totalRefundAmount, PaymentTransactionType.RETURN, "NO Response FROM PG",
									uniqueRequestId);
							mplJusPayRefundService.attachPaymentTransactionModel(orderEntryModel.get(0).getOrder(),
									paymentTransactionModel);
							//TISSIT-1801

						}
					}
					else
					{
						paymentTransactionModel = mplJusPayRefundService.doRefund(orderEntryModel.get(0).getOrder(), totalRefundAmount,
								PaymentTransactionType.RETURN, uniqueRequestId);
						for (final OrderEntryModel orderEntry : orderEntryModel)
						{
							final ConsignmentStatus conStatus = orderEntry.getConsignmentEntries().iterator().next().getConsignment()
									.getStatus();
							if (conStatus.equals(ConsignmentStatus.CANCELLATION_INITIATED))
							{
								mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel,
										orderEntry.getNetAmountAfterAllDisc(), ConsignmentStatus.ORDER_CANCELLED, null);
							}
							else
							{
								mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel,
										orderEntry.getNetAmountAfterAllDisc(), ConsignmentStatus.RETURN_COMPLETED, null);
							}

						}
					}
				}
				catch (final Exception e)
				{
					LOG.error(e.getMessage(), e);

					// TISSIT-1784 Code addition started
					for (final OrderEntryModel orderEntry : orderEntryModel)
					{
						mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel,
								orderEntry.getNetAmountAfterAllDisc(), ConsignmentStatus.REFUND_INITIATED, null);

						// Making RTM entry to be picked up by webhook job
						final RefundTransactionMappingModel refundTransactionMappingModel = getModelService().create(
								RefundTransactionMappingModel.class);
						refundTransactionMappingModel.setRefundedOrderEntry(orderEntry);
						refundTransactionMappingModel.setJuspayRefundId(uniqueRequestId);
						refundTransactionMappingModel.setCreationtime(new Date());
						refundTransactionMappingModel.setRefundType(JuspayRefundType.RETURN);
						refundTransactionMappingModel.setRefundAmount(orderEntry.getNetAmountAfterAllDisc());//TISPRO-216 : Refund amount Set in RTM
						getModelService().save(refundTransactionMappingModel);
					}
					// TISSIT-1784 Code addition ended

					paymentTransactionModel = mplJusPayRefundService.createPaymentTransactionModel(orderEntryModel.get(0).getOrder(),
							FAILURE_KEY, totalRefundAmount, PaymentTransactionType.RETURN, FAILURE_KEY, uniqueRequestId);
					mplJusPayRefundService.attachPaymentTransactionModel(orderEntryModel.get(0).getOrder(), paymentTransactionModel);

				}
			}
			else if (null != order.getIsWallet() && WalletEnum.MRUPEE.toString().equalsIgnoreCase(order.getIsWallet().getCode()))
			{
				final String uniqueRequestId1 = mplMWalletRefundService.getRefundUniqueRequestId();
				try
				{
					paymentTransactionModel = mplMWalletRefundService.doRefund(orderEntryModel.get(0).getOrder(), totalRefundAmount,
							PaymentTransactionType.RETURN, uniqueRequestId1);
					if (null != paymentTransactionModel)
					{
						mplJusPayRefundService
								.attachPaymentTransactionModel(orderEntryModel.get(0).getOrder(), paymentTransactionModel);
						for (final OrderEntryModel orderEntry : orderEntryModel)
						{
							// If CosignmentEnteries are present then update OMS with
							// the state.
							ConsignmentStatus newStatus = null;
							if (orderEntry != null && CollectionUtils.isNotEmpty(orderEntry.getConsignmentEntries()))
							{

								if (StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(),
										MarketplacecommerceservicesConstants.SUCCESS))
								{
									newStatus = ConsignmentStatus.RETURN_COMPLETED;
								}
								else if (StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(),
										MarketplacecommerceservicesConstants.FAILURE))
								{
									newStatus = ConsignmentStatus.REFUND_IN_PROGRESS;
								}
								else
								{
									newStatus = ConsignmentStatus.REFUND_INITIATED;
								}
								// getModelService().save(consignmentModel);
								//							mplJusPayRefundService.makeRefundOMSCall(orderEntry,
								//									paymentTransactionModel,
								//									orderEntry.getNetAmountAfterAllDisc(),
								//									newStatus);
								//R2.3
								mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel,
										orderEntry.getNetAmountAfterAllDisc(), newStatus, null);
								if (newStatus.equals(ConsignmentStatus.RETURN_COMPLETED))
								{
									//orderEntry.setJuspayRequestId(uniqueRequestId);
									getModelService().save(orderEntry);
								}
							}
						}
					}
					else
					{
						LOG.error("Manual Refund Failed");
						for (final OrderEntryModel orderEntry : orderEntryModel)
						{
							//mplJusPayRefundService.makeRefundOMSCall(orderEntry,paymentTransactionModel,orderEntry.getNetAmountAfterAllDisc(),ConsignmentStatus.REFUND_IN_PROGRESS);
							//R2.3
							mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel,
									orderEntry.getNetAmountAfterAllDisc(), ConsignmentStatus.REFUND_IN_PROGRESS, null);
						}
						paymentTransactionModel = mplJusPayRefundService.createPaymentTransactionModel(orderEntryModel.get(0)
								.getOrder(), FAILURE_KEY, totalRefundAmount, PaymentTransactionType.RETURN, "NO Response FROM PG",
								uniqueRequestId);
						mplJusPayRefundService
								.attachPaymentTransactionModel(orderEntryModel.get(0).getOrder(), paymentTransactionModel);
					}
				}
				catch (final Exception e)
				{
					LOG.error(e.getMessage(), e);
					for (final OrderEntryModel orderEntry : orderEntryModel)
					{
						//mplJusPayRefundService.makeRefundOMSCall(orderEntry,paymentTransactionModel,orderEntry.getNetAmountAfterAllDisc(),ConsignmentStatus.REFUND_INITIATED);
						//R2.3
						mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel,
								orderEntry.getNetAmountAfterAllDisc(), ConsignmentStatus.REFUND_INITIATED, null);
					}
					paymentTransactionModel = mplJusPayRefundService.createPaymentTransactionModel(orderEntryModel.get(0).getOrder(),
							FAILURE_KEY, totalRefundAmount, PaymentTransactionType.RETURN, FAILURE_KEY, uniqueRequestId);
					mplJusPayRefundService.attachPaymentTransactionModel(orderEntryModel.get(0).getOrder(), paymentTransactionModel);
				}
			}


		}
		final String result = paymentTransactionModel.getStatus() + "," + paymentTransactionModel.getCode() + ","
				+ totalRefundAmount;
		return result;
	}



	//CheckedInvalid PaymentInfo missing handled call
	@Override
	public boolean createPaymentInfo(final OrderModel orderModel)
	{
		boolean returnFlag = false;
		String modeOrderPayment = null;

		modeOrderPayment = orderModel.getModeOfOrderPayment();
		if (StringUtils.isNotEmpty(modeOrderPayment)
				&& !modeOrderPayment.equalsIgnoreCase(MarketplacecommerceservicesConstants.COD))
		{
			try
			{
				createPaymentInfoforJuspay(orderModel);
				returnFlag = true;
			}
			catch (final Exception e)
			{
				LOG.error("Creating Payment info  createPaymentInfoforJuspay :- ", e);
				returnFlag = false;
			}

		}
		else if (StringUtils.isNotEmpty(modeOrderPayment)
				&& modeOrderPayment.equalsIgnoreCase(MarketplacecommerceservicesConstants.COD))
		{
			try
			{
				createPaymentInfoforCOD(orderModel);
				returnFlag = true;
			}
			catch (final Exception e)
			{
				LOG.error("Creating Payment info  createPaymentInfoforCOD :- ", e);
				returnFlag = false;
			}

		}
		else
		{
			returnFlag = false;
		}
		return returnFlag;

	}


	/**
	 * @param orderModel
	 */
	private void createPaymentInfoforJuspay(final OrderModel orderModel)
	{

		if (orderModel.getPaymentInfo() != null)
		{
			removeExistingPaymentInfo(orderModel);
		}
		final JusPayPaymentInfoModel jusPayPaymentInfoModel = getModelService().create(JusPayPaymentInfoModel.class);
		jusPayPaymentInfoModel.setCode(UUID.randomUUID().toString());
		jusPayPaymentInfoModel.setUser(orderModel.getUser());
		jusPayPaymentInfoModel.setCashOwner(StringUtils.isNotEmpty(orderModel.getUser().getName()) ? orderModel.getUser().getName()
				: ((CustomerModel) orderModel.getUser()).getOriginalUid());

		/*
		 * cart.setConvenienceCharges(Double .valueOf(null !=
		 * baseStoreService.getCurrentBaseStore().getConvenienceChargeForCOD() ? baseStoreService
		 * .getCurrentBaseStore().getConvenienceChargeForCOD().longValue() : 0.0));
		 */
		//setting the payment modes and the amount against it in session to be used later
		/*
		 * final Map<String, Double> paymentInfo = new HashMap<String, Double>();
		 * paymentInfo.put(MarketplaceJuspayServicesConstants.JUSPAY_KEY, orderModel.getConvenienceCharges());
		 * sessionService.setAttribute("paymentModes", paymentInfo);
		 * sessionService.setAttribute("paymentModeForPromotion", MarketplaceJuspayServicesConstants.JUSPAY_KEY);
		 */
		getModelService().save(jusPayPaymentInfoModel);
		orderModel.setPaymentInfo(jusPayPaymentInfoModel);
		getModelService().save(orderModel);
		getModelService().refresh(orderModel);


	}

	private void createPaymentInfoforCOD(final OrderModel orderModel)
	{
		final CODPaymentInfoModel codPaymentInfoModel = getModelService().create(CODPaymentInfoModel.class);
		codPaymentInfoModel.setCode(UUID.randomUUID().toString());
		codPaymentInfoModel.setUser(orderModel.getUser());
		codPaymentInfoModel.setCashOwner(StringUtils.isNotEmpty(orderModel.getUser().getName()) ? orderModel.getUser().getName()
				: ((CustomerModel) orderModel.getUser()).getOriginalUid());
		orderModel.setPaymentInfo(codPaymentInfoModel);

		if (null != baseStoreService.getCurrentBaseStore())
		{
			orderModel.setConvenienceCharges(Double.valueOf(null != baseStoreService.getCurrentBaseStore()
					.getConvenienceChargeForCOD() ? baseStoreService.getCurrentBaseStore().getConvenienceChargeForCOD().longValue()
					: 0.0));
		}
		else
		{
			orderModel.setConvenienceCharges(Double.valueOf(0.0));
		}
		//setting the payment modes and the amount against it in session to be used later
		final Map<String, Double> paymentInfo = new HashMap<String, Double>();
		paymentInfo.put(MarketplacecommerceservicesConstants.COD, orderModel.getConvenienceCharges());
		sessionService.setAttribute("paymentModes", paymentInfo);
		sessionService.setAttribute("paymentModeForPromotion", MarketplacecommerceservicesConstants.COD);
		getModelService().save(codPaymentInfoModel);
		getModelService().save(orderModel);
		getModelService().refresh(orderModel);
	}

	protected void removeExistingPaymentInfo(final OrderModel orderModel)
	{
		getModelService().remove(orderModel.getPaymentInfo());

		orderModel.setPaymentInfo(null);
		orderModel.setConvenienceCharges(null);
		sessionService.removeAttribute("paymentModes");
		sessionService.removeAttribute("paymentModeForPromotion");
		getModelService().save(orderModel);
		getModelService().refresh(orderModel);

	}

	//TPR-7448
	@Override
	public AddCardResponse saveAndGetCardReferenceNo(final AddCardRequest addCardRequest) throws Exception
	{
		final PaymentService juspayService = new PaymentService();

		juspayService.setBaseUrl(getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.JUSPAYBASEURL));
		juspayService.withKey(
				getConfigurationService().getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY))
				.withMerchantId(
						getConfigurationService().getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTID));
		return juspayService.saveAndGetCardReferenceNo(addCardRequest);
	}

	//TPR-7448
	@Override
	public AddCardResponse getCurrentCardReferenceNo(final String cardToken, final String email, final String customerId)
			throws Exception
	{
		final AddCardRequest addCardRequest = new AddCardRequest();
		addCardRequest.setCustomerEmail(email);
		addCardRequest.setCustomerId(customerId);
		addCardRequest.setToken(cardToken);
		final AddCardResponse addCardResponse = saveAndGetCardReferenceNo(addCardRequest);
		return addCardResponse;
	}

	//TPR-7448
	@Override
	public void rmvJuspayCardStatusForCustomer(final String customerId)
	{
		try
		{
			//Removing card reference on completion
			final JuspayCardStatusModel juspayCardStatusModel = new JuspayCardStatusModel();
			juspayCardStatusModel.setCustomerId(customerId);
			final List<JuspayCardStatusModel> juspayCardStatusModelList = flexibleSearchService
					.getModelsByExample(juspayCardStatusModel);
			modelService.removeAll(juspayCardStatusModelList);
		}
		catch (final ModelNotFoundException e)
		{
			LOG.error("In rmvJuspayCardStatusForCustomer nothing to remove for the customer:" + customerId);
		}
		catch (final AmbiguousIdentifierException e)
		{
			LOG.error("In rmvJuspayCardStatusForCustomer some issue occured customer:" + customerId);
		}
	}

	//TPR-7448
	@Override
	public JuspayCardStatusModel getJuspayCardStatusForCustomer(final String customerId, final String guid)
	{
		JuspayCardStatusModel juspayCardStatusModel = null;
		try
		{
			//Removing card reference on completion
			final JuspayCardStatusModel juspayCardStatusModelNew = new JuspayCardStatusModel();
			juspayCardStatusModelNew.setCustomerId(customerId);
			juspayCardStatusModelNew.setGuid(guid);
			juspayCardStatusModel = flexibleSearchService.getModelByExample(juspayCardStatusModelNew);
		}
		catch (final ModelNotFoundException e)
		{
			juspayCardStatusModel = new JuspayCardStatusModel();
			LOG.error("In rmvJuspayCardStatusForCustomer nothing to remove for the customer:" + customerId);
		}
		catch (final AmbiguousIdentifierException e)
		{
			rmvJuspayCardStatusForCustomer(customerId);
			juspayCardStatusModel = new JuspayCardStatusModel();
			LOG.error("In rmvJuspayCardStatusForCustomer some issue occured customer:" + customerId);
		}
		return juspayCardStatusModel;

	}

	/**
	 * Added for paytm integration
	 *
	 * @param cart
	 * @param response
	 * @return CartModel
	 */
	private AbstractOrderModel setValueInPaytmPaymentInfo(final AbstractOrderModel cart, final GetOrderStatusResponse response)
	{
		try
		{
			//creating NetbankingPaymentInfoModel
			final ThirdPartyWalletInfoModel nbPaymentInfoModel = getModelService().create(ThirdPartyWalletInfoModel.class);

			if (StringUtils.isNotEmpty(cart.getGuid()))
			{
				nbPaymentInfoModel.setCode("PAYTM_" + cart.getGuid());//TODO::Setting like this until it is finalized
			}
			else
			{
				//nbPaymentInfoModel.setCode("DUMMY_NB_" + cart.getGuid());		//Erroneous code fixed
				nbPaymentInfoModel.setCode("DUMMY_PAYTM_" + System.currentTimeMillis());
			}
			final UserModel user = cart.getUser();
			if (null != user)
			{
				nbPaymentInfoModel.setUser(user);
				if (StringUtils.isNotEmpty(user.getName()))
				{
					nbPaymentInfoModel.setWalletOwner(user.getName());
				}
				else
				{
					nbPaymentInfoModel.setWalletOwner(MarketplacecommerceservicesConstants.DUMMYCCOWNER);
				}
			}
			nbPaymentInfoModel.setProviderName("PAYTM");
			if (null == cart.getPaymentInfo() && !OrderStatus.PAYMENT_TIMEOUT.equals(cart.getStatus()))
			{
				//saving the nbPaymentInfoModel
				getModelService().save(nbPaymentInfoModel);

				//setting paymentinfo in cart
				cart.setPaymentInfo(nbPaymentInfoModel);
				cart.setPaymentAddress(cart.getDeliveryAddress());
			}
			else if (null != cart.getPaymentInfo())
			{
				LOG.error("Order already has payment info -- not saving nbPaymentInfoModel>>>" + cart.getPaymentInfo().getCode());
			}
			else
			{
				LOG.error(ERROR_PAYMENT + cart.getCode());
			}

		}
		catch (final ModelSavingException e)
		{
			LOG.error("Exception while saving netbanking payment info with " + e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		//returning the cart
		return cart;
	}
}
