/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;
import de.hybris.platform.store.BaseStoreModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.BankforNetbankingModel;
import com.tisl.mpl.core.model.EMIBankModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplPaymentDao;
import com.tisl.mpl.model.BankModel;
import com.tisl.mpl.model.PaymentTypeModel;


/**
 * @author TCS
 *
 */
@Component
@Qualifier(MarketplacecommerceservicesConstants.MPLPAYMENTDAO)
public class MplPaymentDaoImpl implements MplPaymentDao
{
	private static final Logger LOG = Logger.getLogger(MplPaymentDaoImpl.class);
	private FlexibleSearchService flexibleSearchService;

	/**
	 * This method searches all those banks for Netbanking which have Priority field set as false and returns them in a
	 * list of type BankForNetbanking
	 *
	 * @return List<BankforNetbankingModel>
	 *
	 */
	@Override
	public List<BankforNetbankingModel> getOtherBanks()
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.NBNORMALBANKSQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery bankListQuery = new FlexibleSearchQuery(queryString);

			//fetching bank list from DB using flexible search query
			final List<BankforNetbankingModel> bankList = flexibleSearchService.<BankforNetbankingModel> search(bankListQuery)
					.getResult();

			return bankList;
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final NullPointerException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0008);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}



	/**
	 * This method searches all those banks for Netbanking which have Priority field set as true and returns them in a
	 * list of type BankForNetbanking
	 *
	 * @return List<BankforNetbankingModel>
	 *
	 */
	@Override
	public List<BankforNetbankingModel> getBanksByPriority()
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.NBPRIORITYBANKSQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery bankListQuery = new FlexibleSearchQuery(queryString);

			//fetching bank list from DB using flexible search query
			final List<BankforNetbankingModel> bankList = flexibleSearchService.<BankforNetbankingModel> search(bankListQuery)
					.getResult();

			return bankList;
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final NullPointerException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0008);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}



	/**
	 * This method returns the list of all active Payment modes(eg. Credit Card, Debit Card, COD, etc.) for the specific
	 * store and displays them on the payment page of the store
	 *
	 * @param store
	 * @return List<PaymentTypeModel>
	 *
	 */
	@Override
	public List<PaymentTypeModel> getPaymentTypes(final String store)
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.PAYMENTTYPESQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery paymentTypeQuery = new FlexibleSearchQuery(queryString);
			paymentTypeQuery.addQueryParameter(MarketplacecommerceservicesConstants.MPLSTORE, store);

			//fetching payment types from DB using flexible search query
			final List<PaymentTypeModel> paymentTypes = flexibleSearchService.<PaymentTypeModel> search(paymentTypeQuery)
					.getResult();

			return paymentTypes;
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final NullPointerException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0008);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * This method returns the list of all Banks available for EMI. The cartValue argument is used to check whether the
	 * total amount in the cart lies within the bank's upper and lower limits for EMI. Only those banks are returned
	 * whose upper limit is greater than/equal to cartValue and also whose lower limit is less than/equal to cartValue
	 *
	 * @param cartValue
	 * @param emiBankName
	 * @return List<EMIBankModel>
	 */
	@Override
	public List<EMIBankModel> getEMIBanks(final Double cartValue, final String emiBankName)
	{
		try
		{
			final long startTime = System.currentTimeMillis();
			LOG.debug("Entering Dao getEMIBanks()=======" + System.currentTimeMillis());

			FlexibleSearchQuery bankListQuery = new FlexibleSearchQuery(MarketplacecommerceservicesConstants.EMPTY);
			if (emiBankName == null)
			{
				final String queryString = MarketplacecommerceservicesConstants.EMIBANKSQUERY;
				bankListQuery = new FlexibleSearchQuery(queryString);
				bankListQuery.addQueryParameter(MarketplacecommerceservicesConstants.MPLCARTVALUE, cartValue);
			}
			else
			{
				final String queryString = MarketplacecommerceservicesConstants.EMIBANK_FOR_BANKNAMES_QUERY;
				bankListQuery = new FlexibleSearchQuery(queryString);
				bankListQuery.addQueryParameter(MarketplacecommerceservicesConstants.MPLCARTVALUE, cartValue);
				bankListQuery.addQueryParameter(MarketplacecommerceservicesConstants.BANKNAME, emiBankName.toUpperCase());
			}

			//fetching EMI bank list from DB using flexible search query
			final List<EMIBankModel> emiBankList = flexibleSearchService.<EMIBankModel> search(bankListQuery).getResult();

			final long endTime = System.currentTimeMillis();
			LOG.debug("Exiting Dao getEMIBanks()=======" + (endTime - startTime));
			//returning the EMI bank list
			return emiBankList;
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final NullPointerException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0008);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	//NU-61 getbankDetailsforEMI START


	@Override
	public List<EMIBankModel> getBankDetailsforEMI(final Double productValue, final String emiBankName)
	{
		try
		{
			long startTime = 0;
			long endTime = 0;
			if (LOG.isDebugEnabled())
			{
				startTime = System.currentTimeMillis();
				LOG.debug("Entering Dao getBankDetailsforEMI()=======startTime::" + startTime);
			}

			//final long startTime = System.currentTimeMillis();
			//LOG.debug("Entering Dao getBankDetailsforEMI()=======" + System.currentTimeMillis());

			FlexibleSearchQuery bankListQuery = new FlexibleSearchQuery(MarketplacecommerceservicesConstants.EMPTY);
			if (emiBankName == null)
			{

				final String queryString = MarketplacecommerceservicesConstants.EMIBANKSQUERYPRODUCTVALUE;
				bankListQuery = new FlexibleSearchQuery(queryString);
				bankListQuery.addQueryParameter(MarketplacecommerceservicesConstants.MPLPRODUCTVALUE, productValue);

			}
			else
			{
				final String queryString = MarketplacecommerceservicesConstants.EMIBANK_FOR_BANKNAMES_QUERY_PRODUCTVALUE;
				bankListQuery = new FlexibleSearchQuery(queryString);
				bankListQuery.addQueryParameter(MarketplacecommerceservicesConstants.MPLPRODUCTVALUE, productValue);
				bankListQuery.addQueryParameter(MarketplacecommerceservicesConstants.BANKNAME, emiBankName.toUpperCase());

			}

			final List<EMIBankModel> emiBankList = flexibleSearchService.<EMIBankModel> search(bankListQuery).getResult();
			if (LOG.isDebugEnabled())
			{
				endTime = System.currentTimeMillis();
				LOG.debug("Exiting Dao getBankDetailsforEMI()=======endTime:::" + endTime);
				LOG.debug("Time taken=======" + (endTime - startTime));
			}
			//final long endTime = System.currentTimeMillis();
			//LOG.debug("Exiting Dao getBankDetailsforEMI()=======" + (endTime - startTime));
			return emiBankList;
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final NullPointerException e)
		{

			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0008);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	//19/02/2018 NU-61 getbankDetailsforEMI END

	/**
	 * This method returns the EMI terms available for the selected bank.
	 *
	 * @param bank
	 * @return List<EMIBankModel>
	 */
	@Override
	public List<EMIBankModel> getEMIBankTerms(final String bank)
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.EMIBANTERMSSQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery emiTermQuery = new FlexibleSearchQuery(queryString);
			emiTermQuery.addQueryParameter(MarketplacecommerceservicesConstants.MPLBANK, bank);

			//fetching EMI terms against banks from DB using flexible search query
			final List<EMIBankModel> emiTermList = flexibleSearchService.<EMIBankModel> search(emiTermQuery).getResult();
			if (null != emiTermList)
			{
				return emiTermList;
			}
			else
			{
				return null;
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex);
		}
	}


	/**
	 * This method returns the list of the PaymentTypeModel whose mode is same as that present in PaymentTransactionEntry
	 *
	 * @param paymentType
	 * @return PaymentTypeModel
	 *
	 */
	@Override
	public PaymentTypeModel getPaymentTypeForApportion(final String paymentType, final BaseStoreModel baseStore)
	{
		try
		{
			PaymentTypeModel paymentTypeModel = new PaymentTypeModel();
			final String queryString = MarketplacecommerceservicesConstants.PAYMENTTYPEFORAPPORTIONQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery paymentTypeQuery = new FlexibleSearchQuery(queryString);
			paymentTypeQuery.addQueryParameter(MarketplacecommerceservicesConstants.PAYMENTMODE, paymentType);
			paymentTypeQuery.addQueryParameter(MarketplacecommerceservicesConstants.BASESTORE, baseStore);

			//fetching payment types from DB using flexible search query
			final List<PaymentTypeModel> paymentTypeList = flexibleSearchService.<PaymentTypeModel> search(paymentTypeQuery)
					.getResult();
			if (null != paymentTypeList && !paymentTypeList.isEmpty())
			{
				paymentTypeModel = paymentTypeList.get(0);
				return paymentTypeModel;
			}
			else
			{
				return null;
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex);
		}
	}

	/*
	 * @description : fetching bank model for a bank name TISPRO-179
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
		BankModel bankModel = null;
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.BANKMODELQUERY;
			final FlexibleSearchQuery bankQuery = new FlexibleSearchQuery(queryString);
			bankQuery.addQueryParameter(MarketplacecommerceservicesConstants.BANKNAME, bankName.toUpperCase());
			final List<BankModel> bankModelList = flexibleSearchService.<BankModel> search(bankQuery).getResult();
			if (CollectionUtils.isNotEmpty(bankModelList))
			{
				bankModel = bankModelList.get(0);
			}
		}
		catch (final Exception ex)
		{
			LOG.error("Exception while fetching bank model :", ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		return bankModel;
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
		try
		{
			PaymentTransactionModel paymentTransactionModel = new PaymentTransactionModel();
			final String queryString = MarketplacecommerceservicesConstants.GETPAYMENTTRANSACTIONFROMCARTQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery paymentTransactionQuery = new FlexibleSearchQuery(queryString);
			paymentTransactionQuery.addQueryParameter(MarketplacecommerceservicesConstants.CUSTOMERID, mplCustomerID);
			paymentTransactionQuery.addQueryParameter(MarketplacecommerceservicesConstants.JUSPAYORDERID, juspayOrderId);
			final List<PaymentTransactionModel> paymentTransactionList = flexibleSearchService.<PaymentTransactionModel> search(
					paymentTransactionQuery).getResult();
			if (null != paymentTransactionList && !paymentTransactionList.isEmpty())
			{
				//fetching Payment Transaction from DB using flexible search query
				paymentTransactionModel = paymentTransactionList.get(0);
				return paymentTransactionModel;
			}
			else
			{
				return null;
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex);
		}
	}



	/**
	 * This method is used to check whether a Juspay order Id is present in PaymentTransactionModel in cart with status
	 * success
	 *
	 * @param paymentMode
	 * @return PaymentTypeModel
	 */
	@Override
	public PaymentTypeModel getPaymentMode(final String paymentMode, final BaseStoreModel baseStore)
	{
		try
		{
			PaymentTypeModel paymentTypeModel = new PaymentTypeModel();

			final String queryString = MarketplacecommerceservicesConstants.PAYMENTMODETYPE;

			//forming the flexible search query
			final FlexibleSearchQuery paymentModeTypeQuery = new FlexibleSearchQuery(queryString);
			paymentModeTypeQuery.addQueryParameter(MarketplacecommerceservicesConstants.MODE, paymentMode);
			paymentModeTypeQuery.addQueryParameter(MarketplacecommerceservicesConstants.BASESTORE, baseStore);

			final List<PaymentTypeModel> paymentModeTypeList = flexibleSearchService.<PaymentTypeModel> search(paymentModeTypeQuery)
					.getResult();
			if (null != paymentModeTypeList && !paymentModeTypeList.isEmpty())
			{
				//fetching Payment Transaction from DB using flexible search query
				paymentTypeModel = paymentModeTypeList.get(0);
				return paymentTypeModel;
			}
			else
			{
				return null;
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex);
		}
	}


	/**
	 * This method is used to get the Country ISO
	 *
	 * @param countryName
	 * @return String
	 */
	@Override
	public String getCountryISO(final String countryName)
	{
		try
		{
			CountryModel country = new CountryModel();

			final String queryString = MarketplacecommerceservicesConstants.COUNTRYISOQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery countryISOQuery = new FlexibleSearchQuery(queryString);
			countryISOQuery.addQueryParameter(MarketplacecommerceservicesConstants.COUNTRYNAME, countryName);

			final List<CountryModel> countryList = flexibleSearchService.<CountryModel> search(countryISOQuery).getResult();
			if (null != countryList && !countryList.isEmpty())
			{
				//fetching the saved card against the card reference no.
				country = countryList.get(0);
				return country.getIsocode();
			}
			else
			{
				return null;
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex);
		}
	}


	/**
	 * This method is used to get the List of Countries
	 *
	 * @return List<CountryModel>
	 */
	@Override
	public List<CountryModel> getCountries()
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.COUNTRYLISTQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery countryListQuery = new FlexibleSearchQuery(queryString);

			final List<CountryModel> countryList = flexibleSearchService.<CountryModel> search(countryListQuery).getResult();
			if (null != countryList && !countryList.isEmpty())
			{
				return countryList;
			}
			else
			{
				return null;
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex);
		}
	}


	/**
	 * This method takes the juspayOrderId as parameter and fetches any entry present in Audit Model
	 *
	 * @param auditId
	 * @return MplPaymentAuditModel
	 */
	@Override
	public MplPaymentAuditModel getAuditEntries(final String auditId)
	{
		try
		{
			MplPaymentAuditModel audit = new MplPaymentAuditModel();
			final String queryString = MarketplacecommerceservicesConstants.AUDITQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery auditQuery = new FlexibleSearchQuery(queryString);
			auditQuery.addQueryParameter(MarketplacecommerceservicesConstants.AUDITID, auditId);

			//fetching audit entries from DB using flexible search query
			final List<MplPaymentAuditModel> auditList = flexibleSearchService.<MplPaymentAuditModel> search(auditQuery).getResult();
			if (null != auditList && !auditList.isEmpty())
			{
				//fetching the audit entries
				audit = auditList.get(0);
				return audit;
			}
			else
			{
				return null;
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex);
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
		try
		{
			CustomerModel customer = new CustomerModel();
			final String queryString = MarketplacecommerceservicesConstants.CUSTOMERQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery customerQuery = new FlexibleSearchQuery(queryString);
			customerQuery.addQueryParameter(MarketplacecommerceservicesConstants.UID, uid);

			//fetching customer list from DB using flexible search query
			final List<CustomerModel> customerList = flexibleSearchService.<CustomerModel> search(customerQuery).getResult();
			if (null != customerList)
			{
				customer = customerList.get(0);
				return customer;
			}
			else
			{
				return null;
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex);
		}
	}


	/**
	 * This method returns the CartModel based on the CartGuid
	 *
	 * @param cartGuid
	 * @return CartModel
	 *
	 */
	@Override
	public CartModel getCart(final String cartGuid)
	{
		try
		{
			CartModel cart = new CartModel();
			final String queryString = MarketplacecommerceservicesConstants.CARTQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery cartQuery = new FlexibleSearchQuery(queryString);
			cartQuery.addQueryParameter(MarketplacecommerceservicesConstants.GUID, cartGuid);
			final List<CartModel> cartList = flexibleSearchService.<CartModel> search(cartQuery).getResult();
			if (null != cartList && !cartList.isEmpty())
			{
				cart = cartList.get(0);
				return cart;
			}
			else
			{
				return null;
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex);
		}
	}

	/*
	 * @Description : Fetching bank name for net banking-- TISPT-169 --- Exception fixed for PMD and TPR-629
	 * 
	 * @return List<BankforNetbankingModel>
	 */
	@Override
	public List<BankforNetbankingModel> getNetBankingBanks() /* throws EtailNonBusinessExceptions , Exception */
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.NBBANKSQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery bankListQuery = new FlexibleSearchQuery(queryString);

			//fetching bank list from DB using flexible search query
			final List<BankforNetbankingModel> bankList = flexibleSearchService.<BankforNetbankingModel> search(bankListQuery)
					.getResult();

			return bankList;
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		//		catch (final NullPointerException e)
		//		{
		//			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0008);
		//		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

	}



	/**
	 * This method picks up the latest audit id against the cart guid which is in submitted status TISPT-200 ---
	 * Exception fixed for PMD and TPR-629
	 *
	 * @param cartGuid
	 * @return List<MplPaymentAuditModel>
	 */
	@Override
	public List<MplPaymentAuditModel> getAuditId(final String cartGuid) /* throws EtailNonBusinessExceptions , Exception */
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.GETAUDITID;

			//forming the flexible search query
			final FlexibleSearchQuery auditIdQuery = new FlexibleSearchQuery(queryString);
			auditIdQuery.addQueryParameter(MarketplacecommerceservicesConstants.CARTGUID, cartGuid);

			//fetching bank list from DB using flexible search query
			final List<MplPaymentAuditModel> auditList = flexibleSearchService.<MplPaymentAuditModel> search(auditIdQuery)
					.getResult();

			return auditList;
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		//		catch (final NullPointerException e)
		//		{
		//			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0008);
		//		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

	}





	/**
	 * @Decsription : Fetch Order Details Based on GUID for new Payment Soln - Order before payment TPR-629
	 * @param: guid
	 */
	@Override
	public OrderModel fetchOrderOnGUID(final String guid)
	{
		OrderModel orderModel = null;
		try
		{
			final String queryString = //
			"SELECT {om:" + OrderModel.PK
					+ "} "//
					+ MarketplacecommerceservicesConstants.QUERYFROM + OrderModel._TYPECODE + " AS om } where" + "{om."
					+ OrderModel.GUID + "} = ?code and " + "{om." + OrderModel.TYPE + "} = ?type";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter(MarketplacecommerceservicesConstants.CODE, guid);
			query.addQueryParameter("type", "Parent");
			final List<OrderModel> orderModelList = getFlexibleSearchService().<OrderModel> search(query).getResult();
			if (!CollectionUtils.isEmpty(orderModelList))
			{
				orderModel = orderModelList.get(0);
			}
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return orderModel;
	}






	//getters and setters
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
	 * This method fetches audit model corresponding to a reference number
	 *
	 * @param auditId
	 * @return MplPaymentAuditModel
	 *
	 */
	@Override
	public MplPaymentAuditModel getWalletAuditEntries(final String auditId)
	{

		try
		{
			final String queryString = MarketplacecommerceservicesConstants.TPWALLETAUDITQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery auditQuery = new FlexibleSearchQuery(queryString);
			auditQuery.addQueryParameter("auditId", auditId);

			//fetching audit entries from DB using flexible search query
			final List<MplPaymentAuditModel> auditList = flexibleSearchService.<MplPaymentAuditModel> search(auditQuery).getResult();
			if (null != auditList && !auditList.isEmpty())
			{
				//fetching the audit entries
				final MplPaymentAuditModel audit = auditList.get(0);
				return audit;
			}
			else
			{
				return null;
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex);
		}

	}
	@Override
	public boolean isNoCostEmiAvailable(String productId, String sellerId)
	{
		boolean isNoCostEmiAvailable = false;
		try
		{
			
		  final String queryString = MarketplacecommerceservicesConstants.NOCOSTEMIQUERY;
			LOG.debug("queryString: " + queryString);
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

			query.addQueryParameter(MarketplacecommerceservicesConstants.OFFERPRODUCTID, productId);
			query.addQueryParameter(MarketplacecommerceservicesConstants.OFFERSELLERID, sellerId);
			query.addQueryParameter(MarketplacecommerceservicesConstants.SYSDATE, new Date());		

			List resultClassList = new ArrayList();
			resultClassList.add(Integer.class);
			query.setResultClassList(resultClassList);
			//flexExchangeL3Query.setResultClassList(Arrays.asList(String.class));

			//LOG.debug(logQuery + flexExchangeL3Query.getQuery() + logQueryParam + flexExchangeL3Query.getQueryParameters());
			final List<Integer> emiListcount = flexibleSearchService.<Integer> search(query).getResult();

			if (CollectionUtils.isNotEmpty(emiListcount))
			{
				if (emiListcount.get(0).intValue() > 0)
				{
					isNoCostEmiAvailable = true;
				}
			}
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return isNoCostEmiAvailable;
	}
}
