/**
 *
 */
package com.tisl.mpl.dao;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.promotions.model.OrderPromotionModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.core.model.SavedCardModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.model.BankModel;
import com.tisl.mpl.model.PaymentTypeModel;
import com.tisl.mpl.service.MplPaymentWebService;


/**
 * @author TCS
 *
 */
public class MplPaymentWebDAOImpl implements MplPaymentWebDAO
{
	private static final Logger LOG = Logger.getLogger(MplPaymentWebService.class);
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/**
	 * This method is used to get Cart Values from Db according to cartID success
	 *
	 * @param cartID
	 * @return CartModel
	 */
	@Override
	public CartModel findCartValues(final String cartID) throws EtailNonBusinessExceptions
	{
		try
		{
			CartModel cartModel = null;
			final String queryString = MarketplacewebservicesConstants.CARTQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery cartQuery = new FlexibleSearchQuery(queryString);
			cartQuery.addQueryParameter(MarketplacewebservicesConstants.CARTID, cartID);

			LOG.info("**************** findCartValues Query ******************** : cartQuery : " + cartQuery);

			final List<CartModel> cartList = flexibleSearchService.<CartModel> search(cartQuery).getResult();

			LOG.info("**************** findCartValues ******************** : cartList : " + cartList);

			if (!cartList.isEmpty())
			{
				//fetching Cart data from DB using flexible search query
				cartModel = cartList.get(0);//flexibleSearchService.<CartModel> search(cartQuery).getResult().get(0);

				//returning bank against the bin
				return cartModel;
			}
			else
			{
				return cartModel;
			}

			//forming the flexible search query
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
	}


	/**
	 * This method is used to get Cart Values from Db according to guid success
	 *
	 * @param guid
	 * @return CartModel
	 */
	@Override
	public CartModel findCartValuesAnonymous(final String guid) throws EtailNonBusinessExceptions
	{
		try
		{
			CartModel cartModel = null;
			final String queryString = MarketplacewebservicesConstants.CARTGUIDQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery cartQuery = new FlexibleSearchQuery(queryString);
			cartQuery.addQueryParameter(MarketplacewebservicesConstants.GUID, guid);

			LOG.info("**************** findCartValues Query ******************** : cartQuery : " + cartQuery);

			final List<CartModel> cartList = flexibleSearchService.<CartModel> search(cartQuery).getResult();

			LOG.info("**************** findCartValues ******************** : cartList : " + cartList);

			if (!cartList.isEmpty())
			{
				//fetching Cart data from DB using flexible search query
				cartModel = cartList.get(0);

				return cartModel;
			}
			else
			{
				return cartModel;
			}

			//forming the flexible search query
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
	}


	/**
	 * This method is used to check whether a Juspay order Id is present in PaymentTransactionModel in cart with status
	 * success
	 *
	 * @param paymentMode
	 * @return PaymentTypeModel
	 */
	@Override
	public PaymentTypeModel getPaymentMode(final String paymentMode) throws EtailNonBusinessExceptions
	{
		try
		{
			PaymentTypeModel paymentTypeModel = null;

			final String queryString = MarketplacewebservicesConstants.PAYMENTMODETYPE;

			//forming the flexible search query
			final FlexibleSearchQuery paymentModeTypeQuery = new FlexibleSearchQuery(queryString);
			paymentModeTypeQuery.addQueryParameter(MarketplacecommerceservicesConstants.MODE, paymentMode);

			LOG.info("**************** getPaymentMode Query ******************** : paymentModeTypeQuery : " + paymentModeTypeQuery);

			final List<PaymentTypeModel> paymentModeTypeList = flexibleSearchService.<PaymentTypeModel> search(paymentModeTypeQuery)
					.getResult();

			LOG.info("**************** findCartValues ******************** : paymentModeTypeList : " + paymentModeTypeList);

			if (null != paymentModeTypeList && !paymentModeTypeList.isEmpty())
			{
				//fetching Payment Transaction from DB using flexible search query
				paymentTypeModel = flexibleSearchService.<PaymentTypeModel> search(paymentModeTypeQuery).getResult().get(0);

				return paymentTypeModel;
			}
			else
			{
				return paymentTypeModel;
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
	}


	/**
	 * This method is used to get Billing Address from DB according to cardRefNo
	 *
	 * @param originalUid
	 * @param cardRefNo
	 * @return country.getIsocode()
	 */
	@Override
	public SavedCardModel getBillingAddress(final String originalUid, final String cardRefNo)
	{
		try
		{
			SavedCardModel saveCardDetails = new SavedCardModel();

			final String queryString = MarketplacewebservicesConstants.SAVEDCARDFORCUSTOMERQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery billingAddressQuery = new FlexibleSearchQuery(queryString);
			billingAddressQuery.addQueryParameter(MarketplacewebservicesConstants.ORIGINALUID, originalUid);
			billingAddressQuery.addQueryParameter(MarketplacewebservicesConstants.CARDREFNUMBER, cardRefNo);

			LOG.info("**************** getBillingAddress Query ******************** : billingAddressQuery : " + billingAddressQuery);

			final List<SavedCardModel> cardModel = flexibleSearchService.<SavedCardModel> search(billingAddressQuery).getResult();

			LOG.info("**************** getBillingAddress ******************** : cardModel : " + cardModel);

			if (null != cardModel && !cardModel.isEmpty())
			{
				//fetching the saved card against the card reference no.
				saveCardDetails = flexibleSearchService.<SavedCardModel> search(billingAddressQuery).getResult().get(0);

				return saveCardDetails;
			}
			else
			{
				return saveCardDetails;
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
	}


	/**
	 * This method fetches the details from Bank Name
	 *
	 * @param bankName
	 * @return BankModel
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public BankModel savedCardBankFromBin(final String bankName) throws EtailNonBusinessExceptions
	{
		try
		{
			BankModel bankModel = null;
			final String queryString = MarketplacewebservicesConstants.BANKFROMSAVEDCARD;

			//forming the flexible search query
			final FlexibleSearchQuery bankQuery = new FlexibleSearchQuery(queryString);
			bankQuery.addQueryParameter(MarketplacewebservicesConstants.BANKNAME, bankName);

			LOG.info("**************** savedCardBankFromBin Query ******************** : bankQuery : " + bankQuery);

			final List<BankModel> binList = flexibleSearchService.<BankModel> search(bankQuery).getResult();

			LOG.info("**************** savedCardBankFromBin ******************** : binList : " + binList);

			if (null != binList && !binList.isEmpty())
			{
				//fetching BIN data from DB using flexible search query
				bankModel = binList.get(0);

				//returning bank against the bin
				return bankModel;
			}
			else
			{
				return null;
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
	}

	@Override
	/**
	 * This method returns the customer model based on the CustomerUid
	 *
	 * @param userId
	 * @return CustomerModel
	 *
	 */
	public CustomerModel getCustomer(final String userId)
	{
		try
		{
			CustomerModel customer = new CustomerModel();
			final String queryString = MarketplacewebservicesConstants.CUSTOMERQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery customerQuery = new FlexibleSearchQuery(queryString);
			customerQuery.addQueryParameter(MarketplacewebservicesConstants.ORIGINALUID, userId);

			LOG.info("**************** getCustomer Query ******************** : customerQuery : " + customerQuery);

			//fetching customer list from DB using flexible search query
			final List<CustomerModel> customerList = flexibleSearchService.<CustomerModel> search(customerQuery).getResult();

			LOG.info("**************** getCustomer ******************** : customerList : " + customerList);

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
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.dao.MplPaymentWebDAO#orderPromotions()
	 */
	@Override
	public List<OrderPromotionModel> orderPromotions()
	{
		try
		{
			final String queryString = MarketplacewebservicesConstants.ORDERPROMOQRY;

			//forming the flexible search query
			final FlexibleSearchQuery orderPromoQuery = new FlexibleSearchQuery(queryString);

			LOG.info("**************** orderPromotions Query ******************** : orderPromoQuery : " + orderPromoQuery);

			final List<OrderPromotionModel> orderPromotionList = flexibleSearchService.<OrderPromotionModel> search(orderPromoQuery)
					.getResult();

			LOG.info("**************** orderPromotions ******************** : orderPromoList : " + orderPromotionList);

			return orderPromotionList;


			//forming the flexible search query
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
	}
}
