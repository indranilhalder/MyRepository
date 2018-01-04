/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.VoucherCardPerOfferInvalidationModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;
import de.hybris.platform.voucher.model.CouponUserRestrictionModel;
import de.hybris.platform.voucher.model.UserRestrictionModel;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.JuspayCardStatusModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplVoucherDao;


/**
 * @author TCS
 *
 */
public class MplVoucherDaoImpl implements MplVoucherDao
{
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;


	private static final Logger LOG = Logger.getLogger(MplVoucherDaoImpl.class);



	/**
	 * This method returns list of Voucher Invalidation model for the voucher applied against the cart or order
	 *
	 * @param voucherIdentifier
	 * @param customerUid
	 * @param orderCode
	 * @return List<VoucherInvalidationModel>
	 *
	 *
	 */
	//	@Override
	//	public List<VoucherInvalidationModel> findVoucherInvalidation(final String voucherIdentifier, final String customerUid,
	//			final String orderCode)
	//	{
	//		try
	//		{
	//			String queryString = MarketplacecommerceservicesConstants.VOUCHERINVALIDATIONQUERY;
	//			queryString += " and {vo.code}='" + voucherIdentifier + "' ";
	//			LOG.debug("Invalidation query is " + queryString);
	//			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
	//			//query.addQueryParameter(MarketplacecommerceservicesConstants.VOUCHERIDENTIFIER, voucherIdentifier);
	//			query.addQueryParameter(MarketplacecommerceservicesConstants.CUSTOMERUID, customerUid);
	//			query.addQueryParameter(MarketplacecommerceservicesConstants.ORDERCODE, orderCode);
	//			return getFlexibleSearchService().<VoucherInvalidationModel> search(query).getResult();
	//		}
	//		catch (final FlexibleSearchException e)
	//		{
	//			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
	//		}
	//		catch (final UnknownIdentifierException e)
	//		{
	//			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
	//		}
	//		catch (final Exception e)
	//		{
	//			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
	//		}
	//	}

	/**
	 * This method returns Invalidation model for a particular voucher-user-order
	 *
	 * @param voucher
	 * @param user
	 * @param order
	 * @return VoucherInvalidationModel
	 */
	@Override
	public VoucherInvalidationModel findVoucherInvalidation(final VoucherModel voucher, final UserModel user,
			final OrderModel order)
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.VOUCHERINVALIDATIONQUERY;
			LOG.debug("Invalidation query is " + queryString);
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter(MarketplacecommerceservicesConstants.VOUCHER, voucher);
			query.addQueryParameter(MarketplacecommerceservicesConstants.USER, user);
			query.addQueryParameter(MarketplacecommerceservicesConstants.ORDER, order);

			return getFlexibleSearchService().<VoucherInvalidationModel> search(query).getResult().get(0);
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
	 * TPR-7448 This method returns Invalidation model for maxAvailCount
	 *
	 * @param voucher
	 * @param cardFingerprint
	 * @return VoucherInvalidationModel
	 */
	@Override
	public List<VoucherCardPerOfferInvalidationModel> findCardPerOfferInvalidation(final String guid, final VoucherModel voucher,
			final String cardFingerprint)
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.VOUCHERCARDPEROFFERQUERY;
			LOG.debug("Invalidation query is " + queryString);
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter(MarketplacecommerceservicesConstants.VOUCHER, voucher);
			query.addQueryParameter(MarketplacecommerceservicesConstants.CARDFINGERPRINT1, cardFingerprint);
			query.addQueryParameter(MarketplacecommerceservicesConstants.GUID, guid);

			return getFlexibleSearchService().<VoucherCardPerOfferInvalidationModel> search(query).getResult();
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
	 * TPR-7448 This method returns Invalidation model for maxAountPerMonth
	 *
	 * @param voucher
	 * @param cardFingerprint
	 * @return VoucherInvalidationModel
	 */
	@Override
	public List<VoucherCardPerOfferInvalidationModel> findInvalidationMaxAmtPMnth(final String guid, final VoucherModel voucher,
			final String cardFingerprint)
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.VOUCHERCARDPEROFRMXAMTQUERYMNTH;
			LOG.debug("Invalidation query is " + queryString);
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter(MarketplacecommerceservicesConstants.VOUCHER, voucher);
			query.addQueryParameter(MarketplacecommerceservicesConstants.CARDFINGERPRINT1, cardFingerprint);
			query.addQueryParameter(MarketplacecommerceservicesConstants.GUID, guid);

			return getFlexibleSearchService().<VoucherCardPerOfferInvalidationModel> search(query).getResult();
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
	 * TPR-7448 This method returns Invalidation model for maxAountPerDay
	 *
	 * @param voucher
	 * @param cardFingerprint
	 * @return VoucherInvalidationModel
	 */
	@Override
	public List<VoucherCardPerOfferInvalidationModel> findInvalidationMaxAmtPDay(final String guid, final VoucherModel voucher,
			final String cardFingerprint)
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.VOUCHERCARDPEROFRMXAMTQUERYDAY;
			LOG.debug("Invalidation query is " + queryString);
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter(MarketplacecommerceservicesConstants.VOUCHER, voucher);
			query.addQueryParameter(MarketplacecommerceservicesConstants.CARDFINGERPRINT1, cardFingerprint);
			query.addQueryParameter(MarketplacecommerceservicesConstants.GUID, guid);

			return getFlexibleSearchService().<VoucherCardPerOfferInvalidationModel> search(query).getResult();
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
	 * TPR-7448 This method returns Invalidation model for maxAountPerWeek
	 *
	 * @param voucher
	 * @param cardFingerprint
	 * @return VoucherInvalidationModel
	 */
	@Override
	public List<VoucherCardPerOfferInvalidationModel> findInvalidationMaxAmtPWeek(final String guid, final VoucherModel voucher,
			final String cardFingerprint)
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.VOUCHERCARDPEROFRMXAMTQUERYWEEK;
			LOG.debug("Invalidation query is " + queryString);
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter(MarketplacecommerceservicesConstants.VOUCHER, voucher);
			query.addQueryParameter(MarketplacecommerceservicesConstants.CARDFINGERPRINT1, cardFingerprint);
			query.addQueryParameter(MarketplacecommerceservicesConstants.GUID, guid);

			return getFlexibleSearchService().<VoucherCardPerOfferInvalidationModel> search(query).getResult();
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
	 * TPR-7448 This method returns Invalidation model for maxAountPerYear
	 *
	 * @param voucher
	 * @param cardFingerprint
	 * @return VoucherInvalidationModel
	 */
	@Override
	public List<VoucherCardPerOfferInvalidationModel> findInvalidationMaxAmtPYear(final String guid, final VoucherModel voucher,
			final String cardFingerprint)
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.VOUCHERCARDPEROFRMXAMTQUERYYEAR;
			LOG.debug("Invalidation query is " + queryString);
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter(MarketplacecommerceservicesConstants.VOUCHER, voucher);
			query.addQueryParameter(MarketplacecommerceservicesConstants.CARDFINGERPRINT1, cardFingerprint);
			query.addQueryParameter(MarketplacecommerceservicesConstants.GUID, guid);

			return getFlexibleSearchService().<VoucherCardPerOfferInvalidationModel> search(query).getResult();
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
	 * TPR-7448 This method returns JuspayCardStatus model
	 *
	 * @param guid
	 * @param customerId
	 * @return VoucherInvalidationModel
	 */
	@Override
	public List<JuspayCardStatusModel> findJuspayCardStatus(final String guid, final String customerId)
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.JUSPAYCARDSTATUSQRY;
			LOG.debug(" query is " + queryString);
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter(MarketplacecommerceservicesConstants.CUSTOMERID, customerId);
			query.addQueryParameter(MarketplacecommerceservicesConstants.GUID, guid);
			//query.addQueryParameter(MarketplacecommerceservicesConstants.CARDREFERENCENO, cardReferenceNo);

			return getFlexibleSearchService().<JuspayCardStatusModel> search(query).getResult();
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



	/* CAR-330 starts here */

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplVoucherDao#fetchUserRestrictionDetails(java.util.Date)
	 */
	@Override
	public List<UserRestrictionModel> fetchUserRestrictionDetails(final Date mplConfigDate)
	{
		List<UserRestrictionModel> userRestriction = new ArrayList<UserRestrictionModel>();
		final String queryString = "select {pk} from {UserRestriction AS pr} WHERE {pr.modifiedtime} >= ?earlierDate";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("earlierDate", mplConfigDate);
		LOG.debug("The queryString is " + queryString);
		userRestriction = getFlexibleSearchService().<UserRestrictionModel> search(query).getResult();
		return userRestriction;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.daos.MplVoucherDao#fetchExistingVoucherData(de.hybris.platform.voucher
	 * .model.VoucherModel)
	 */
	@Override
	public List<CouponUserRestrictionModel> fetchExistingVoucherData(final VoucherModel voucher)
	{
		List<CouponUserRestrictionModel> couponUserRestrs = new ArrayList<CouponUserRestrictionModel>();
		final String queryStr = "SELECT {pk} FROM {CouponUserRestriction} where {vouchers} = ?voucher";
		LOG.debug("The queryStr is " + queryStr);
		final FlexibleSearchQuery userRestrQuery = new FlexibleSearchQuery(queryStr);
		userRestrQuery.addQueryParameter("voucher", voucher);
		couponUserRestrs = getFlexibleSearchService().<CouponUserRestrictionModel> search(userRestrQuery).getResult();
		return couponUserRestrs;
	}

	/* CAR-330 ends here */

}
