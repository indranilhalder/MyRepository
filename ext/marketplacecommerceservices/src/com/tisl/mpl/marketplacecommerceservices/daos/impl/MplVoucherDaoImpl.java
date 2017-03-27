/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
import de.hybris.platform.voucher.model.VoucherModel;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
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





}
