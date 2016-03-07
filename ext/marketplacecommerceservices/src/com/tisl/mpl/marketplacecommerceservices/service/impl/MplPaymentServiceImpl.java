/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCartCalculationStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CODPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.DebitCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.EMIPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.NetbankingPaymentInfoModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
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
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.EBSResponseStatus;
import com.tisl.mpl.core.enums.EBSRiskLevelEnum;
import com.tisl.mpl.core.enums.MplPaymentAuditStatusEnum;
import com.tisl.mpl.core.model.BankforNetbankingModel;
import com.tisl.mpl.core.model.EMIBankModel;
import com.tisl.mpl.core.model.EMITermRowModel;
import com.tisl.mpl.core.model.JuspayEBSResponseModel;
import com.tisl.mpl.core.model.MplPaymentAuditEntryModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.core.model.PaymentModeApportionModel;
import com.tisl.mpl.core.model.SavedCardModel;
import com.tisl.mpl.data.EMITermRateData;
import com.tisl.mpl.data.MplPromoPriceData;
import com.tisl.mpl.data.MplPromotionData;
import com.tisl.mpl.data.VoucherDiscountData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.juspay.response.CardResponse;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.marketplacecommerceservices.daos.MplPaymentDao;
import com.tisl.mpl.marketplacecommerceservices.order.MplCommerceCartCalculationStrategy;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentTransactionService;
import com.tisl.mpl.marketplacecommerceservices.service.MplVoucherService;
import com.tisl.mpl.model.BankModel;
import com.tisl.mpl.model.PaymentModeSpecificPromotionRestrictionModel;
import com.tisl.mpl.model.PaymentTypeModel;
import com.tisl.mpl.util.DiscountUtility;
import com.tisl.mpl.util.MplEMICalculator;


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
	//@Autowired
	//private CommerceCartService commerceCartService;

	@Autowired
	private PersistentKeyGenerator juspayOrderIdGenerator;
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

	//@Autowired
	//private ExtendedUserService extendedUserService;


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
		final List<PaymentTypeModel> paymentTypes = getMplPaymentDao().getPaymentTypes(store);

		//returning the payment types
		return paymentTypes;
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
			final CartModel cart)
	{
		if (null != orderStatusResponse)
		{
			LOG.info(MarketplacecommerceservicesConstants.JUSPAY_ORDER_STAT_RESP + orderStatusResponse);

			//Logic if the order status response is not null
			//if (null != orderStatusResponse.getCardResponse())
			//{
			for (final Map.Entry<String, Double> entry : paymentMode.entrySet())
			{
				if (null != orderStatusResponse.getCardResponse()
						&& MarketplacecommerceservicesConstants.DEBIT.equalsIgnoreCase(entry.getKey()))
				{
					try
					{
						//saving the cartmodel for Debit Card
						getModelService().save(setValueInDebitCardPaymentInfo(cart, orderStatusResponse));
						break;
					}
					catch (final ModelSavingException e)
					{
						LOG.error(MarketplacecommerceservicesConstants.PAYMENT_EXC_LOG + e);
						throw new ModelSavingException(e + MarketplacecommerceservicesConstants.PAYMENT_EXC_LOG_END);
					}
				}
				else if (null != orderStatusResponse.getCardResponse()
						&& MarketplacecommerceservicesConstants.CREDIT.equalsIgnoreCase(entry.getKey()))
				{
					try
					{
						//saving the cartmodel for Credit Card
						getModelService().save(setValueInCreditCardPaymentInfo(cart, orderStatusResponse));
						break;
					}
					catch (final ModelSavingException e)
					{
						LOG.error(MarketplacecommerceservicesConstants.PAYMENT_EXC_LOG + e);
						throw new ModelSavingException(e + MarketplacecommerceservicesConstants.PAYMENT_EXC_LOG_END);
					}
				}
				else if (null != orderStatusResponse.getCardResponse()
						&& MarketplacecommerceservicesConstants.EMI.equalsIgnoreCase(entry.getKey()))
				{
					try
					{
						//saving the cartmodel for EMI
						getModelService().save(setValueInEMIPaymentInfo(cart, orderStatusResponse));
						break;
					}
					catch (final ModelSavingException e)
					{
						LOG.error(MarketplacecommerceservicesConstants.PAYMENT_EXC_LOG + e);
						throw new ModelSavingException(e + MarketplacecommerceservicesConstants.PAYMENT_EXC_LOG_END);
					}
				}
				else if (MarketplacecommerceservicesConstants.NETBANKING.equalsIgnoreCase(entry.getKey().trim()))
				{
					try
					{
						//saving the cartmodel for Netbanking
						getModelService().save(setValueInNetbankingPaymentInfo(cart, orderStatusResponse));
						break;
					}
					catch (final ModelSavingException e)
					{
						LOG.error(MarketplacecommerceservicesConstants.PAYMENT_EXC_LOG + e);
						throw new ModelSavingException(e + MarketplacecommerceservicesConstants.PAYMENT_EXC_LOG_END);
					}
				}
			}
			//}
		}
	}


	/**
	 * This method is setting paymentTransactionModel and the paymentTransactionEntryModel against the cart for non-COD
	 * payment modes including wallet
	 *
	 * @param orderStatusResponse
	 * @param paymentMode
	 * @param cart
	 *
	 */
	@Override
	public void setPaymentTransaction(final GetOrderStatusResponse orderStatusResponse, final Map<String, Double> paymentMode,
			final CartModel cart)
	{
		Collection<PaymentTransactionModel> collection = cart.getPaymentTransactions();
		List<PaymentTransactionModel> paymentTransactionList = new ArrayList<PaymentTransactionModel>();
		if (null == collection || collection.isEmpty())
		{
			collection = new ArrayList<PaymentTransactionModel>();
		}

		paymentTransactionList.addAll(collection);
		List<PaymentTransactionEntryModel> paymentTransactionEntryList = new ArrayList<PaymentTransactionEntryModel>();

		//final PaymentTransactionModel paymentTransactionModel = getModelService().create(PaymentTransactionModel.class);
		final Date date = new Date();

		String checkValues = "".intern();
		String[] parts = null;
		String saveCard = "".intern();
		String sameAsShipping = "".intern();
		if (null != orderStatusResponse)
		{
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
					paymentTransactionEntry.setCurrency(cart.getCurrency());
					//	paymentTransactionEntry.setPaymentMode(MarketplacecommerceservicesConstants.WALLET);//TODO::Wallet not in scope of Release 1
					paymentTransactionEntry.setTransactionStatus(MarketplacecommerceservicesConstants.SUCCESS);

					try
					{
						getModelService().save(paymentTransactionEntry);
						paymentTransactionEntryList.add(paymentTransactionEntry);
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
					paymentTransactionEntryList = getMplPaymentTransactionService().createPaymentTranEntry(orderStatusResponse, cart,
							entry, paymentTransactionEntryList);
				}
			}

			paymentTransactionList = getMplPaymentTransactionService().createPaymentTransaction(cart, orderStatusResponse,
					paymentTransactionEntryList, paymentTransactionList);
		}
		cart.setPaymentTransactions(paymentTransactionList);

		try
		{
			getModelService().save(cart);
			if (saveCard.equalsIgnoreCase(MarketplacecommerceservicesConstants.TRUE)
					&& null != orderStatusResponse.getCardResponse()
					&& StringUtils.isNotEmpty(orderStatusResponse.getCardResponse().getCardReference()))
			{
				//setting as saved card
				saveCards(orderStatusResponse, paymentMode, cart, sameAsShipping);
			}
		}
		catch (final ModelSavingException e)
		{
			LOG.error("Exception while saving cart with ", e);
		}
	}



	/**
	 * This method is setting paymentTransactionModel and the paymentTransactionEntryModel against the cart for COD and
	 * wallet
	 *
	 * @param paymentMode
	 * @param cart
	 */
	@Override
	public void setPaymentTransactionForCOD(final Map<String, Double> paymentMode, final CartModel cart)
			throws EtailNonBusinessExceptions
	{
		try
		{
			// TISPRD-361
			Collection<PaymentTransactionModel> collection = cart.getPaymentTransactions();
			final List<PaymentTransactionModel> paymentTransactionList = new ArrayList<PaymentTransactionModel>();
			if (null == collection || collection.isEmpty())
			{
				collection = new ArrayList<PaymentTransactionModel>();
			}

			paymentTransactionList.addAll(collection);

			final List<PaymentTransactionEntryModel> paymentTransactionEntryList = new ArrayList<PaymentTransactionEntryModel>();
			//final List<PaymentTransactionModel> paymentTransactionList = new ArrayList<PaymentTransactionModel>();
			final PaymentTransactionModel paymentTransactionModel = getModelService().create(PaymentTransactionModel.class);
			final Date date = new Date();
			final String codCode = getCodCodeGenerator().generate().toString();
			final PaymentTransactionEntryModel paymentTransactionEntry = getModelService()
					.create(PaymentTransactionEntryModel.class);
			paymentTransactionEntry.setCode(MarketplacecommerceservicesConstants.COD + codCode + "-" + System.currentTimeMillis());
			paymentTransactionEntry.setAmount(BigDecimal.valueOf(cart.getTotalPriceWithConv().doubleValue()));
			paymentTransactionEntry.setTime(date);
			paymentTransactionEntry.setCurrency(cart.getCurrency());
			paymentTransactionEntry.setType(PaymentTransactionType.COD_PAYMENT);
			paymentTransactionEntry.setTransactionStatus(MarketplacecommerceservicesConstants.SUCCESS);

			PaymentTypeModel paymentTypeModelCOD = modelService.create(PaymentTypeModel.class);
			paymentTypeModelCOD.setMode(MarketplacecommerceservicesConstants.COD);
			paymentTypeModelCOD = flexibleSearchService.getModelByExample(paymentTypeModelCOD);
			paymentTransactionEntry.setPaymentMode(paymentTypeModelCOD);

			getModelService().save(paymentTransactionEntry);
			paymentTransactionEntryList.add(paymentTransactionEntry);

			if (null != cart.getPaymentInfo())
			{
				paymentTransactionModel.setInfo(cart.getPaymentInfo());
			}
			paymentTransactionModel.setCode(MarketplacecommerceservicesConstants.COD + codCode + "-" + System.currentTimeMillis());
			paymentTransactionModel.setCreationtime(date);
			paymentTransactionModel.setCurrency(cart.getCurrency());
			paymentTransactionModel.setEntries(paymentTransactionEntryList);
			paymentTransactionModel.setOrder(cart);
			paymentTransactionModel.setPlannedAmount(BigDecimal.valueOf(cart.getTotalPriceWithConv().doubleValue()));
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
			getModelService().save(paymentTransactionModel);
			paymentTransactionList.add(paymentTransactionModel);

			cart.setPaymentTransactions(paymentTransactionList);
			getModelService().save(cart);
		}
		catch (final ModelSavingException e)
		{
			LOG.error("Exception while saving cart with ", e);
			throw new EtailNonBusinessExceptions(e, ": Exception while setPaymentTransactionForCOD");
		}
		catch (final Exception ex)
		{
			LOG.error("Exception while setPaymentTransactionForCOD ", ex);
			throw new EtailNonBusinessExceptions(ex);
		}
	}

	/**
	 * This private method is used to set the values in DebitCardPaymentInfoModel after getting successful response from
	 * Juspay
	 *
	 * @param cart
	 * @param orderStatusResponse
	 * @return CartModel
	 */
	private CartModel setValueInDebitCardPaymentInfo(final CartModel cart, final GetOrderStatusResponse orderStatusResponse)
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
		debitCardPaymentInfoModel.setUser(getUserService().getCurrentUser());
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

		if (StringUtils.isNotEmpty(response.getCardType()))
		{
			if (MarketplacecommerceservicesConstants.MASTERCARD.equalsIgnoreCase(response.getCardBrand()))
			{
				debitCardPaymentInfoModel.setType(CreditCardType.MASTER);
			}
			else if (MarketplacecommerceservicesConstants.MAESTRO.equalsIgnoreCase(response.getCardBrand()))
			{
				debitCardPaymentInfoModel.setType(CreditCardType.MAESTRO);
			}
			else if (MarketplacecommerceservicesConstants.AMEX.equalsIgnoreCase(response.getCardBrand())
					|| MarketplacecommerceservicesConstants.AMERICAN_EXPRESS.equalsIgnoreCase(orderStatusResponse.getCardResponse()
							.getCardBrand()))
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
		try
		{
			getModelService().save(debitCardPaymentInfoModel);

		}
		catch (final ModelSavingException e)
		{
			LOG.error("Exception while saving debit card payment info with " + e);
		}

		//setting paymentinfo in cart
		cart.setPaymentInfo(debitCardPaymentInfoModel);
		cart.setPaymentAddress(cart.getDeliveryAddress());
		//returning the cart
		return cart;
	}

	/**
	 * This private method is used to set the values in CreditCardPaymentInfoModel after getting successful response from
	 * Juspay
	 *
	 * @param cart
	 * @param orderStatusResponse
	 * @return CartModel
	 */
	private CartModel setValueInCreditCardPaymentInfo(final CartModel cart, final GetOrderStatusResponse orderStatusResponse)
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
				address = getAddress(orderStatusResponse);
			}
			else
			{
				address = billingAddressForSavedCard(orderStatusResponse, cart, sameAsShipping);
			}
		}
		else
		{
			address = getAddress(orderStatusResponse);
		}

		//Creating Dummy Address when Address is null for Credit Card
		if (null == address)
		{
			address = createDummyAddress(cart);
		}

		//for saving Billing Address against credit card

		//final AddressModel address = saveBillingAddress(orderStatusResponse, cart, sameAsShipping);
		if (StringUtils.isNotEmpty(orderStatusResponse.getCardResponse().getCardReference()))
		{
			creditCardPaymentInfoModel.setCode(orderStatusResponse.getCardResponse().getCardReference());
		}
		else
		{
			creditCardPaymentInfoModel.setCode("DUMMY_CC_" + cart.getCode());//To Be Removed
		}
		creditCardPaymentInfoModel.setUser(getUserService().getCurrentUser());
		if (StringUtils.isNotEmpty(orderStatusResponse.getCardResponse().getNameOnCard()))
		{
			creditCardPaymentInfoModel.setCcOwner(orderStatusResponse.getCardResponse().getNameOnCard());
		}
		else
		{
			creditCardPaymentInfoModel.setCcOwner(MarketplacecommerceservicesConstants.DUMMYCCOWNER);//To Be Removed
		}
		if (StringUtils.isNotEmpty(orderStatusResponse.getCardResponse().getCardNumber()))
		{
			creditCardPaymentInfoModel.setNumber(orderStatusResponse.getCardResponse().getCardNumber());
		}
		else
		{
			creditCardPaymentInfoModel.setNumber(MarketplacecommerceservicesConstants.DUMMYNUMBER);//To Be Removed
		}
		if (StringUtils.isNotEmpty(orderStatusResponse.getCardResponse().getExpiryMonth()))
		{
			creditCardPaymentInfoModel.setValidToMonth(orderStatusResponse.getCardResponse().getExpiryMonth());
		}
		else
		{
			creditCardPaymentInfoModel.setValidToMonth(MarketplacecommerceservicesConstants.DUMMYMM);//To Be Removed
		}
		if (StringUtils.isNotEmpty(orderStatusResponse.getCardResponse().getExpiryYear()))
		{
			creditCardPaymentInfoModel.setValidToYear(orderStatusResponse.getCardResponse().getExpiryYear());
		}
		else
		{
			creditCardPaymentInfoModel.setValidToYear(MarketplacecommerceservicesConstants.DUMMYYY);
		}
		if (StringUtils.isNotEmpty(orderStatusResponse.getCardResponse().getCardReference()))
		{
			creditCardPaymentInfoModel.setSubscriptionId(orderStatusResponse.getCardResponse().getCardReference());//Card Reference
		}
		else
		{
			creditCardPaymentInfoModel.setSubscriptionId(MarketplacecommerceservicesConstants.DUMMYCARDREF);
		}

		if (StringUtils.isNotEmpty(orderStatusResponse.getCardResponse().getCardBrand()))
		{
			if (MarketplacecommerceservicesConstants.MASTERCARD.equalsIgnoreCase(orderStatusResponse.getCardResponse()
					.getCardBrand()))
			{
				creditCardPaymentInfoModel.setType(CreditCardType.MASTER);
			}
			else if (MarketplacecommerceservicesConstants.MAESTRO.equalsIgnoreCase(orderStatusResponse.getCardResponse()
					.getCardBrand()))
			{
				creditCardPaymentInfoModel.setType(CreditCardType.MAESTRO);
			}
			else if (MarketplacecommerceservicesConstants.AMEX
					.equalsIgnoreCase(orderStatusResponse.getCardResponse().getCardBrand())
					|| MarketplacecommerceservicesConstants.AMERICAN_EXPRESS.equalsIgnoreCase(orderStatusResponse.getCardResponse()
							.getCardBrand()))
			{
				creditCardPaymentInfoModel.setType(CreditCardType.AMEX);
			}
			else if (MarketplacecommerceservicesConstants.DINERSCARD.equalsIgnoreCase(orderStatusResponse.getCardResponse()
					.getCardBrand()))
			{
				creditCardPaymentInfoModel.setType(CreditCardType.DINERS);
			}
			else if (MarketplacecommerceservicesConstants.VISA
					.equalsIgnoreCase(orderStatusResponse.getCardResponse().getCardBrand()))
			{
				creditCardPaymentInfoModel.setType(CreditCardType.VISA);
			}
			else if (MarketplacecommerceservicesConstants.EUROCARD.equalsIgnoreCase(orderStatusResponse.getCardResponse()
					.getCardBrand()))
			{
				creditCardPaymentInfoModel.setType(CreditCardType.MASTERCARD_EUROCARD);
			}
			else if (MarketplacecommerceservicesConstants.SWITCHCARD.equalsIgnoreCase(orderStatusResponse.getCardResponse()
					.getCardBrand()))
			{
				creditCardPaymentInfoModel.setType(CreditCardType.SWITCH);
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
		try
		{
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
			getModelService().save(creditCardPaymentInfoModel);

		}
		catch (final ModelSavingException e)
		{
			LOG.error("Exception while saving credit card payment info with " + e);
		}
		//setting paymentinfo in cart
		cart.setPaymentInfo(creditCardPaymentInfoModel);
		cart.setPaymentAddress(address);
		//returning the cart
		return cart;

	}

	/**
	 * This private method is used to set the values in EMIPaymentInfoModel after getting successful response from Juspay
	 *
	 * @param cart
	 * @param response
	 * @return CartModel
	 */
	private CartModel setValueInEMIPaymentInfo(final CartModel cart, final GetOrderStatusResponse response)
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
				address = getAddress(response);
			}
			else
			{
				address = billingAddressForSavedCard(response, cart, sameAsShipping);
			}
		}
		else
		{
			address = getAddress(response);
		}

		//Creating Dummy Address when Address is null for Credit Card
		if (null == address)
		{
			address = createDummyAddress(cart);
		}

		if (StringUtils.isNotEmpty(response.getCardResponse().getCardReference()))
		{
			emiPaymentInfoModel.setCode(response.getCardResponse().getCardReference());
		}
		else
		{
			emiPaymentInfoModel.setCode("DUMMY_EMI_" + cart.getCode());//To Be Removed
		}
		emiPaymentInfoModel.setUser(getUserService().getCurrentUser());
		if (StringUtils.isNotEmpty(response.getCardResponse().getNameOnCard()))
		{
			emiPaymentInfoModel.setCcOwner(response.getCardResponse().getNameOnCard());
		}
		else
		{
			emiPaymentInfoModel.setCcOwner(MarketplacecommerceservicesConstants.DUMMYCCOWNER);//To Be Removed
		}
		if (StringUtils.isNotEmpty(response.getCardResponse().getCardNumber()))
		{
			emiPaymentInfoModel.setNumber(response.getCardResponse().getCardNumber());
		}
		else
		{
			emiPaymentInfoModel.setNumber(MarketplacecommerceservicesConstants.DUMMYNUMBER);//To Be Removed
		}
		if (StringUtils.isNotEmpty(response.getCardResponse().getExpiryMonth()))
		{
			emiPaymentInfoModel.setValidToMonth(response.getCardResponse().getExpiryMonth());
		}
		else
		{
			emiPaymentInfoModel.setValidToMonth(MarketplacecommerceservicesConstants.DUMMYMM);//To Be Removed
		}
		if (StringUtils.isNotEmpty(response.getCardResponse().getExpiryYear()))
		{
			emiPaymentInfoModel.setValidToYear(response.getCardResponse().getExpiryYear());
		}
		else
		{
			emiPaymentInfoModel.setValidToYear(MarketplacecommerceservicesConstants.DUMMYYY);
		}
		if (StringUtils.isNotEmpty(response.getCardResponse().getCardReference()))
		{
			emiPaymentInfoModel.setSubscriptionId(response.getCardResponse().getCardReference());
		}
		else
		{
			emiPaymentInfoModel.setSubscriptionId(MarketplacecommerceservicesConstants.DUMMYCARDREF);
		}


		//TODO:Add EMI related values from response to emiPaymentInfoModel
		if (StringUtils.isNotEmpty(response.getCardResponse().getCardBrand()))
		{
			if (MarketplacecommerceservicesConstants.MASTERCARD.equalsIgnoreCase(response.getCardResponse().getCardBrand()))
			{
				emiPaymentInfoModel.setType(CreditCardType.MASTER);
			}
			else if (MarketplacecommerceservicesConstants.MAESTRO.equalsIgnoreCase(response.getCardResponse().getCardBrand()))
			{
				emiPaymentInfoModel.setType(CreditCardType.MAESTRO);
			}
			else if (MarketplacecommerceservicesConstants.AMEX.equalsIgnoreCase(response.getCardResponse().getCardBrand())
					|| MarketplacecommerceservicesConstants.AMERICAN_EXPRESS.equalsIgnoreCase(response.getCardResponse()
							.getCardBrand()))
			{
				emiPaymentInfoModel.setType(CreditCardType.AMEX);
			}
			else if (MarketplacecommerceservicesConstants.DINERSCARD.equalsIgnoreCase(response.getCardResponse().getCardBrand()))
			{
				emiPaymentInfoModel.setType(CreditCardType.DINERS);
			}
			else if (MarketplacecommerceservicesConstants.VISA.equalsIgnoreCase(response.getCardResponse().getCardBrand()))
			{
				emiPaymentInfoModel.setType(CreditCardType.VISA);
			}
			else if (MarketplacecommerceservicesConstants.EUROCARD.equalsIgnoreCase(response.getCardResponse().getCardBrand()))
			{
				emiPaymentInfoModel.setType(CreditCardType.MASTERCARD_EUROCARD);
			}
			else if (MarketplacecommerceservicesConstants.SWITCHCARD.equalsIgnoreCase(response.getCardResponse().getCardBrand()))
			{
				emiPaymentInfoModel.setType(CreditCardType.SWITCH);
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
		try
		{
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
			//saving the creditCardPaymentInfoModel
			getModelService().save(emiPaymentInfoModel);
		}
		catch (final ModelSavingException e)
		{
			LOG.error("Exception while saving emi payment info with " + e);
		}
		//setting paymentinfo in cart
		cart.setPaymentInfo(emiPaymentInfoModel);
		cart.setPaymentAddress(address);
		//returning the cart
		return cart;

	}

	/**
	 * This private method is used to set the values in NetbankingPaymentInfoModel after getting successful response from
	 * Juspay
	 *
	 * @param cart
	 * @param response
	 * @return CartModel
	 */
	private CartModel setValueInNetbankingPaymentInfo(final CartModel cart, final GetOrderStatusResponse response)
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
				nbPaymentInfoModel.setCode("DUMMY_NB_" + cart.getGuid());
			}

			if (null != getUserService().getCurrentUser())
			{
				nbPaymentInfoModel.setUser(getUserService().getCurrentUser());
				if (StringUtils.isNotEmpty(getUserService().getCurrentUser().getName()))
				{
					nbPaymentInfoModel.setBankOwner(getUserService().getCurrentUser().getName());
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

			//saving the nbPaymentInfoModel
			getModelService().save(nbPaymentInfoModel);

			//setting paymentinfo in cart
			cart.setPaymentInfo(nbPaymentInfoModel);
			cart.setPaymentAddress(cart.getDeliveryAddress());
		}
		catch (final ModelSavingException e)
		{
			LOG.error("Exception while saving netbanking payment info with " + e);
		}
		//returning the cart
		return cart;
	}

	/**
	 * This method helps to save COD Payment info into database
	 *
	 * @param custName
	 * @param cartValue
	 * @param totalCODCharge
	 * @param entries
	 *
	 */
	@Override
	public void saveCODPaymentInfo(final String custName, final Double cartValue, final Double totalCODCharge,
			final List<AbstractOrderEntryModel> entries, final CartModel cartModel)
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
					entryTotals -= (null != entry.getFreeCount() ? entry.getFreeCount().intValue() : 0);
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
						throw new ModelSavingException(e + " :Exception while saving abstract order entry model with");
					}
				}

			}
		}
		//creating CODPaymentInfoModel
		final CODPaymentInfoModel cODPaymentInfoModel = getModelService().create(CODPaymentInfoModel.class);

		//setting values in CODPaymentInfoModel
		cODPaymentInfoModel.setCashOwner(custName);
		cODPaymentInfoModel.setCode(MarketplacecommerceservicesConstants.COD + "_" + entries.get(0).getOrder().getCode());
		cODPaymentInfoModel.setUser(getUserService().getCurrentUser());
		try
		{
			//saving CODPaymentInfoModel
			getModelService().save(cODPaymentInfoModel);
		}
		catch (final ModelSavingException e)
		{
			LOG.error("Exception while saving cod payment info with " + e);
			throw new ModelSavingException("Exception while saving cod payment info with", e);
		}

		//setting CODPaymentInfoModel in cartmodel
		cartModel.setPaymentInfo(cODPaymentInfoModel);
		cartModel.setPaymentAddress(cartModel.getDeliveryAddress());
		try
		{
			//saving the cartmodel
			getModelService().save(cartModel);
		}
		catch (final ModelSavingException e)
		{
			LOG.error("Exception while saving cart with ", e);
			throw new ModelSavingException("Exception while saving cart with", e);
		}
	}


	/**
	 * This method is used set the saved card details in SavedCardModel
	 *
	 * @param response
	 * @param address
	 */
	private void setInSavedCard(final GetOrderStatusResponse response, final AddressModel address)
	{
		//getting the current customer to fetch customer Id and customer email
		final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();
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
			saveNewCard.setBillingAddress(address);
			getModelService().save(saveNewCard);
			saveNewCardList.add(saveNewCard);
			customer.setSavedCard(saveNewCardList);
		}
		getModelService().save(customer);

	}



	/**
	 * This method is used set the saved card details in SavedCardModel
	 *
	 * @param response
	 */
	private void setInSavedDebitCard(final GetOrderStatusResponse response)
	{
		//getting the current customer to fetch customer Id and customer email
		final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();
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
			getModelService().save(saveNewCard);
			saveNewCardList.add(saveNewCard);
			customer.setSavedCard(saveNewCardList);
		}
		getModelService().save(customer);

	}


	/**
	 * This method helps to save apportion for the PaymentModes which were successful
	 *
	 * @param cart
	 *
	 */
	@Override
	public void paymentModeApportion(final CartModel cart)
	{
		final List<AbstractOrderEntryModel> entries = cart.getEntries();
		final List<PaymentTransactionModel> paymentTransactionList = cart.getPaymentTransactions();
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
								final Double totalAmount = cart.getTotalPriceWithConv();
								final Double modeAmount = Double.valueOf(tranasctionEntry.getAmount().doubleValue());
								final Double percentApportion = Double.valueOf((modeAmount.doubleValue() / totalAmount.doubleValue())
										* MarketplacecommerceservicesConstants.PERCENTVALUE);
								final Double formattedPercentApportion = Double.valueOf(String.format(
										MarketplacecommerceservicesConstants.FORMAT, percentApportion));
								paymentModeApportionModel.setPaymentMode(paymentType);
								paymentModeApportionModel.setApportionPercent(formattedPercentApportion);
								percentTobeDeducted += formattedPercentApportion.doubleValue();
								try
								{
									getModelService().save(paymentModeApportionModel);
								}
								catch (final ModelSavingException e)
								{
									LOG.error("Exception while saving payment mode apportion model with " + e);
								}
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
								try
								{
									getModelService().save(paymentModeApportionModel);
								}
								catch (final ModelSavingException e)
								{
									LOG.error("Exception while saving payment mode apportion model with " + e);
								}
							}
							paymentModeApportion.add(paymentModeApportionModel);
						}
					}
				}

				//Setting the List<PaymentModeApportionModel> against the cart entries
				for (final AbstractOrderEntryModel entry : entries)
				{
					entry.setPaymentModeApportion(paymentModeApportion);
					try
					{
						getModelService().save(entry);
					}
					catch (final ModelSavingException e)
					{
						LOG.error("Exception while saving abstract order entry model with " + e);
					}
				}
			}
		}
	}


	/**
	 * This method is used to check whether a Juspay order Id is present in PaymentTransactionModel in cart with status
	 * success
	 *
	 * @param juspayOrderId
	 * @param mplCustomerID
	 * @return PaymentTransactionModel
	 */
	@Override
	public PaymentTransactionModel getOrderStatusFromCart(final String juspayOrderId, final String mplCustomerID)
	{
		PaymentTransactionModel paymentTransaction = null;
		if (null != getMplPaymentDao().getOrderStatusFromCart(juspayOrderId, mplCustomerID))
		{
			paymentTransaction = getMplPaymentDao().getOrderStatusFromCart(juspayOrderId, mplCustomerID);
			//return paymentTransaction;	SONAR Fix
		}
		//		else
		//		{
		//			return null;	SONAR Fix
		//		}

		return paymentTransaction;
	}


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
	 * @param cartModel
	 * @return MplPromoPriceData
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
	public MplPromoPriceData applyPromotions(final CartData cartData, final CartModel cartModel) throws ModelSavingException,
			NumberFormatException, JaloInvalidParameterException, VoucherOperationException, CalculationException,
			JaloSecurityException, JaloPriceFactoryException, EtailNonBusinessExceptions
	{
		final long startTime = System.currentTimeMillis();
		//Reset Voucher Apportion
		if (CollectionUtils.isNotEmpty(cartModel.getDiscounts()))
		{
			LOG.debug(">> 1 : Checking voucher related promotion >> ");
			for (final AbstractOrderEntryModel entry : getMplVoucherService().getOrderEntryModelFromVouEntries(
					(VoucherModel) cartModel.getDiscounts().get(0), cartModel))
			{
				entry.setCouponCode(MarketplacecommerceservicesConstants.EMPTY);
				entry.setCouponValue(Double.valueOf(0.00D));
				getModelService().save(entry);
			}
		}


		final MplPromoPriceData promoPriceData = new MplPromoPriceData();
		VoucherDiscountData discData = new VoucherDiscountData();
		calculatePromotion(cartModel, cartData);

		final String bankName = getSessionService().getAttribute(MarketplacecommerceservicesConstants.BANKFROMBIN);
		final String paymentMode = getSessionService().getAttribute(MarketplacecommerceservicesConstants.PAYMENTMODEFORPROMOTION);
		getSessionService().removeAttribute(MarketplacecommerceservicesConstants.BANKFROMBIN);

		if (StringUtils.isNotEmpty(paymentMode) && paymentMode.equalsIgnoreCase("EMI") && StringUtils.isNotEmpty(bankName))
		{

			LOG.debug(">> Apply promotion >> Inside EMI Bank Name : " + bankName);
			final List<EMIBankModel> emiBankList = getMplPaymentDao().getEMIBanks(cartModel.getTotalPriceWithConv(), bankName);
			if (!(CollectionUtils.isNotEmpty(emiBankList) && emiBankList.size() == 1))
			{
				calculatePromotion(cartModel, cartData);
				promoPriceData.setErrorMsgForEMI(getConfigurationService().getConfiguration().getString(
						MarketplacecommerceservicesConstants.PAYMENT_EMI_PROMOERROR));
			}
		}

		if (CollectionUtils.isNotEmpty(cartModel.getDiscounts()))
		{
			LOG.debug(">> 2 : Checking voucher related promotion >> ");
			final PromotionVoucherModel voucher = (PromotionVoucherModel) cartModel.getDiscounts().get(0);
			final List<AbstractOrderEntryModel> applicableOrderEntryList = getMplVoucherService().getOrderEntryModelFromVouEntries(
					voucher, cartModel);
			discData = getMplVoucherService().checkCartAfterApply(voucher, cartModel, applicableOrderEntryList);
			getMplVoucherService().setApportionedValueForVoucher(voucher, cartModel, voucher.getVoucherCode(),
					applicableOrderEntryList);
			getMplCommerceCartService().setTotalWithConvCharge(cartModel, cartData);

		}
		getSessionService().removeAttribute(MarketplacecommerceservicesConstants.PAYMENTMODEFORPROMOTION);

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
			final PriceData totalvalExcConv = discountUtility
					.createPrice(cartModel, Double.valueOf(((cartData.getTotalPriceWithConvCharge().getValue()).subtract(cartData
							.getConvenienceChargeForCOD().getValue())).toString()));

			if (null != totalvalExcConv && null != totalvalExcConv.getFormattedValue())
			{
				promoPriceData.setTotalExcConv(totalvalExcConv);
			}
			promoPriceData.setDeliveryCost(cartData.getDeliveryCost());
		}
		promoPriceData.setVoucherDiscount(discData);

		final long endTime = System.currentTimeMillis();
		LOG.debug("Exiting service applyPromotions()======" + (endTime - startTime));
		return promoPriceData;
	}

	/**
	 * @Description : To get details of applied promotion
	 * @param cartModel
	 * @param cartData
	 * @return List<MplPromotionData>
	 * @throws EtailNonBusinessExceptions
	 */
	private List<MplPromotionData> getAppliedPromotionDetails(final CartModel cartModel, final CartData cartData)
			throws EtailNonBusinessExceptions
	{
		MplPromotionData responseData = new MplPromotionData();

		final List<MplPromotionData> responseDataList = new ArrayList<MplPromotionData>();

		final Set<PromotionResultModel> promotion = cartModel.getAllPromotionResults();
		if (CollectionUtils.isNotEmpty(promotion))
		{
			for (final PromotionResultModel promo : promotion)
			{
				if (checkPromoForPaymntRestrictn(promo))
				{
					if (promo.getPromotion() instanceof ProductPromotionModel && promo.getCertainty().floatValue() < 1.0F)
					{
						final ProductPromotionModel productPromotion = (ProductPromotionModel) promo.getPromotion();
						responseData = getDiscountUtility().populatePotentialPromoData(productPromotion, cartModel);
						responseDataList.add(responseData);
					}
					else if (promo.getPromotion() instanceof OrderPromotionModel && promo.getCertainty().floatValue() < 1.0F)
					{
						final OrderPromotionModel orderPromotion = (OrderPromotionModel) promo.getPromotion();
						responseData = getDiscountUtility().populatePotentialOrderPromoData(orderPromotion, cartModel);
						responseDataList.add(responseData);
					}
					else if (promo.getPromotion() instanceof ProductPromotionModel && promo.getCertainty().floatValue() == 1.0F)
					{
						final ProductPromotionModel productPromotion = (ProductPromotionModel) promo.getPromotion();
						responseData = getDiscountUtility().populateData(productPromotion, cartModel);
						responseDataList.add(responseData);
					}
					else if (promo.getPromotion() instanceof OrderPromotionModel && promo.getCertainty().floatValue() == 1.0F)
					{
						final OrderPromotionModel orderPromotion = (OrderPromotionModel) promo.getPromotion();
						responseData = getDiscountUtility().populateCartPromoData(orderPromotion, cartModel);
						responseDataList.add(responseData);
					}
				}
				else
				{
					responseData = getDiscountUtility().populateNonPromoData(cartData);
					responseDataList.add(responseData);
				}
			}
		}
		else
		{
			responseData = getDiscountUtility().populateNonPromoData(cartData);
			responseDataList.add(responseData);
		}

		return responseDataList;
	}

	/**
	 * This method calculates the promotional values
	 *
	 * @param cart
	 */
	private void calculatePromotion(final CartModel cart, final CartData cartData)
	{
		final long startTime = System.currentTimeMillis();
		final Double deliveryCost = cart.getDeliveryCost();
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cart);
		getCalculationStrategy().recalculateCart(parameter);

		final Double subTotal = cart.getSubtotal();
		final Double cartDiscount = populateCartDiscountPrice(cart);
		final Double totalPriceAfterDeliveryCost = Double.valueOf(subTotal.doubleValue() + deliveryCost.doubleValue()
				- cartDiscount.doubleValue());

		cart.setDeliveryCost(deliveryCost);

		//TISEE-5354
		final Double totalPrice = Double.valueOf(String.format("%.2f", totalPriceAfterDeliveryCost));
		cart.setTotalPrice(totalPrice);

		getModelService().save(cart);
		getMplCommerceCartService().setTotalWithConvCharge(cart, cartData);

		final long endTime = System.currentTimeMillis();
		LOG.debug("Exiting calculatePromotion()========" + (endTime - startTime));
	}


	/**
	 * @param cart
	 * @Description : Calculate Total Discount Value for Showing on Payment Screen
	 * @return dicountData
	 */
	private PriceData calculateTotalDiscount(final CartModel cart)
	{
		BigDecimal discount = null;
		double totalPrice = 0.0D;
		if (null != cart && CollectionUtils.isNotEmpty(cart.getEntries()))
		{
			final List<DiscountModel> discountList = cart.getDiscounts();
			final List<DiscountValue> discountValueList = cart.getGlobalDiscountValues();
			double voucherDiscount = 0.0d;

			for (final AbstractOrderEntryModel entry : cart.getEntries())
			{
				totalPrice = totalPrice + (entry.getBasePrice().doubleValue() * entry.getQuantity().doubleValue());
			}

			for (final DiscountValue discountValue : discountValueList)
			{
				if (CollectionUtils.isNotEmpty(discountList) && discountValue.getCode().equals(discountList.get(0).getCode()))
				{
					voucherDiscount = discountValue.getAppliedValue();
					break;
				}
			}

			discount = BigDecimal.valueOf(
					(totalPrice + cart.getDeliveryCost().doubleValue() + cart.getConvenienceCharges().doubleValue())).subtract(
					BigDecimal.valueOf((cart.getTotalPriceWithConv().doubleValue() + voucherDiscount)));
		}

		return getDiscountUtility().createPrice(cart, Double.valueOf(discount != null ? discount.doubleValue() : 0.0));
	}

	/**
	 * @Description : Calculating Cart Promotion Discount Value
	 * @param cart
	 * @return Double
	 */
	private Double populateCartDiscountPrice(final CartModel cart)
	{
		final long startTime = System.currentTimeMillis();
		LOG.debug("Entering Service populateCartDiscountPrice()=====" + System.currentTimeMillis());
		Double value = Double.valueOf(0);
		final CartData cartData = getMplExtendedCartConverter().convert(cart);

		if (cartData.getTotalDiscounts() != null && cartData.getTotalDiscounts().getValue() != null)

		{
			value = Double.valueOf(cartData.getTotalDiscounts().getValue().doubleValue());

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
	 * @Description : Return Channel Data
	 * @param channel
	 * @return salesApplication
	 */
	private List<SalesApplication> returnChannelData(final String channel)
	{
		final List<SalesApplication> salesApplication = new ArrayList<SalesApplication>();
		if (channel.equalsIgnoreCase(MarketplacecommerceservicesConstants.CHANNEL_WEB))
		{
			salesApplication.add(SalesApplication.WEB);
		}
		else if (channel.equalsIgnoreCase(MarketplacecommerceservicesConstants.CHANNEL_WEBMOBILE))
		{
			salesApplication.add(SalesApplication.WEBMOBILE);
		}
		else if (channel.equalsIgnoreCase(MarketplacecommerceservicesConstants.CHANNEL_MOBILE))
		{
			salesApplication.add(SalesApplication.MOBILE);
		}
		else if (channel.equalsIgnoreCase(MarketplacecommerceservicesConstants.CHANNEL_CALLCENTER))
		{
			salesApplication.add(SalesApplication.CALLCENTER);
		}
		return salesApplication;
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
	 *
	 */
	@Override
	public boolean createEntryInAudit(final String juspayOrderId, final String channel, final String cartGuId)
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
				newAuditModel.setChannel(returnChannelData(channel));
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
		catch (final NullPointerException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0001);
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
	}



	/**
	 * This method updates already created entry in the Audit Table with response from Juspay
	 *
	 * @param orderStatusResponse
	 *
	 */
	@Override
	public boolean updateAuditEntry(final GetOrderStatusResponse orderStatusResponse)
	{
		boolean flag = false;
		try
		{
			//Make entry in Audit Table
			final MplPaymentAuditModel auditModel = getMplPaymentDao().getAuditEntries(orderStatusResponse.getOrderId());

			final ArrayList<JuspayEBSResponseModel> juspayEBSResponseList = new ArrayList<JuspayEBSResponseModel>();
			final JuspayEBSResponseModel juspayEBSResponseModel = getModelService().create(JuspayEBSResponseModel.class);
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

			if (null != auditModel)
			{
				List<MplPaymentAuditEntryModel> collection = auditModel.getAuditEntries();
				final List<MplPaymentAuditEntryModel> auditEntryList = new ArrayList<MplPaymentAuditEntryModel>();
				if (null == collection || collection.isEmpty())
				{
					collection = new ArrayList<MplPaymentAuditEntryModel>();
				}
				auditEntryList.addAll(collection);

				final MplPaymentAuditEntryModel auditEntry = getModelService().create(MplPaymentAuditEntryModel.class);
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
						juspayEBSResponseModel.setEbsRiskPercentage("-1.0");
					}
					if (StringUtils.isNotEmpty(orderStatusResponse.getRiskResponse().getEbsPaymentStatus())
							&& !orderStatusResponse.getRiskResponse().getEbsPaymentStatus()
									.equalsIgnoreCase(MarketplacecommerceservicesConstants.PAID))
					{
						setEBSRiskStatus(orderStatusResponse.getRiskResponse().getEbsPaymentStatus(), juspayEBSResponseModel);
					}
					else if (StringUtils.isEmpty(orderStatusResponse.getRiskResponse().getEbsPaymentStatus())
							&& !juspayEBSResponseModel.getEbsRiskPercentage().equalsIgnoreCase("-1.0"))
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
								juspayEBSResponseModel.setEbsRiskPercentage("-1.0");
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
				auditModel.setRisk(juspayEBSResponseList);
				getModelService().save(auditModel);
			}
		}
		catch (final NullPointerException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0001);
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		return flag;
	}

	/**
	 * This method sets the EbsRiskStatus in JuspayEBSResponseModel
	 *
	 * @param riskStatus
	 * @param juspayEBSResponseModel
	 */
	@Override
	public void setEBSRiskStatus(final String riskStatus, final JuspayEBSResponseModel juspayEBSResponseModel)
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
		catch (final NullPointerException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0001);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
	}

	/**
	 * This method sets the EbsRiskLevel in JuspayEBSResponseModel
	 *
	 * @param riskLevel
	 * @param juspayEBSResponseModel
	 */
	@Override
	public void setEBSRiskLevel(final String riskLevel, final JuspayEBSResponseModel juspayEBSResponseModel)
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
		catch (final NullPointerException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0001);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
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
	public void saveCreditCard(final GetOrderStatusResponse orderStatusResponse, final CartModel cart, final String sameAsShipping)
	{
		//for saving Billing Address against credit card
		final AddressModel address = billingAddressForSavedCard(orderStatusResponse, cart, sameAsShipping);
		getModelService().save(address);

		//setting as saved card
		setInSavedCard(orderStatusResponse, address);
	}



	/**
	 * This method saves debit cards
	 *
	 * @param orderStatusResponse
	 * @param cart
	 */
	@Override
	public void saveDebitCard(final GetOrderStatusResponse orderStatusResponse, final CartModel cart)
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
			setInSavedDebitCard(orderStatusResponse);
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
	private AddressModel billingAddressForSavedCard(final GetOrderStatusResponse orderStatusResponse, final CartModel cart,
			final String sameAsShipping)
	{
		AddressModel address = null;
		if (StringUtils.isNotEmpty(orderStatusResponse.getUdf1()))
		{
			if (sameAsShipping.equalsIgnoreCase(MarketplacecommerceservicesConstants.TRUE))
			{
				address = cart.getDeliveryAddress();
				address.setBillingAddress(Boolean.TRUE);
			}
			else
			{
				address = getModelService().create(AddressModel.class);
				try
				{
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
				catch (final NullPointerException e)
				{
					LOG.error("Cannot set value in Address Model with exception ", e);
				}
			}
		}
		modelService.save(address);
		return address;

	}


	/**
	 * This method gets the addressModel mapped to any saved card
	 *
	 * @param orderStatusResponse
	 * @return AddressModel
	 */
	private AddressModel getAddress(final GetOrderStatusResponse orderStatusResponse)
	{
		AddressModel address = null;
		//getting the current customer to fetch customer Id and customer email
		final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();
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
		return address;
	}



	/**
	 * This method saves cards against the customer
	 *
	 * @param orderStatusResponse
	 * @param paymentMode
	 * @param cart
	 * @param sameAsShipping
	 */
	private void saveCards(final GetOrderStatusResponse orderStatusResponse, final Map<String, Double> paymentMode,
			final CartModel cart, final String sameAsShipping)
	{
		if (null != orderStatusResponse && null != orderStatusResponse.getCardResponse())
		{
			//Logic if the order status response is not null
			for (final Map.Entry<String, Double> entry : paymentMode.entrySet())
			{
				if (MarketplacecommerceservicesConstants.DEBIT.equalsIgnoreCase(entry.getKey()))
				{
					try
					{
						saveDebitCard(orderStatusResponse, cart);
						break;
					}
					catch (final ModelSavingException e)
					{
						LOG.error(MarketplacecommerceservicesConstants.PAYMENT_EXC_LOG, e);
					}
				}
				else if (MarketplacecommerceservicesConstants.CREDIT.equalsIgnoreCase(entry.getKey())
						|| MarketplacecommerceservicesConstants.EMI.equalsIgnoreCase(entry.getKey()))
				{
					try
					{
						saveCreditCard(orderStatusResponse, cart, sameAsShipping);
						break;
					}
					catch (final ModelSavingException e)
					{
						LOG.error(MarketplacecommerceservicesConstants.PAYMENT_EXC_LOG, e);
					}
				}
			}

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
		final CustomerModel customer = getMplPaymentDao().getCustomer(uid);
		//		if (null != customer)
		//		{
		//			return customer;
		//		}
		//		else
		//		{
		//			return null;
		//		}						SONAR Fix

		return customer;
	}


	/**
	 * This method is used to create a dummy address when there is no address present in Credit Card and EMI Payment Info
	 *
	 * @param cart
	 * @return AddressModel
	 */
	private AddressModel createDummyAddress(final CartModel cart)
	{
		final AddressModel billingAddress = getModelService().create(AddressModel.class);

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

		return billingAddress;
	}

	@Override
	public JuspayEBSResponseModel getEntryInAuditByOrder(final String auditId)
	{
		JuspayEBSResponseModel jusModelLast = null;
		final MplPaymentAuditModel auditModel = getMplPaymentDao().getAuditEntries(auditId);
		for (final JuspayEBSResponseModel jusModel : auditModel.getRisk())
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

}