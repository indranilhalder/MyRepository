/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.pancard.impl;

import de.hybris.platform.core.model.PancardInformationModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.pancard.MplPancardDao;


/**
 * @author TCS
 *
 */
public class MplPancardDaoImpl implements MplPancardDao
{



	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;
	//SONAR FIX JEWELLERY
	//	@Autowired
	//	private ModelService modelService;

	private static final String SELECT_CLASS = "SELECT {p:";
	private static final String FROM_CLASS = "FROM {";
	private static final String P_CLASS = "{p.";
	private static final String WHERE = " AS p } where";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.pancard.MplPancardDao#getPanCardOrderId(java.lang.String)
	 */
	@Override
	public List<PancardInformationModel> getPanCardOrderId(final String orderreferancenumber)
	{
		final String queryString = //
		SELECT_CLASS + PancardInformationModel.PK + "} "//
				+ FROM_CLASS + PancardInformationModel._TYPECODE + WHERE + P_CLASS + PancardInformationModel.ORDERID + "} = ?orderid";

		//final String pancardquery = "select {pk} from {PancardInformation} where {OrderId}=?orderid";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("orderid", orderreferancenumber);

		//OrderId


		final List<PancardInformationModel> pancardInfo = flexibleSearchService.<PancardInformationModel> search(query).getResult();
		if (CollectionUtils.isNotEmpty(pancardInfo))
		{
			return pancardInfo;
		}

		return null;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.pancard.MplPancardDao#getOrderForCode(java.lang.String)
	 */
	@Override
	public List<OrderModel> getOrderForCode(final String orderreferancenumber)
	{
		// YTODO Auto-generated method stub
		List<OrderModel> orderModelList = null;
		try
		{
			final String queryString = //
			"SELECT {om:" + OrderModel.PK
					+ "} "//
					+ MarketplacecommerceservicesConstants.QUERYFROM + OrderModel._TYPECODE + " AS om } where" + "{om."
					+ OrderModel.CODE + "} = ?code and " + "{om." + OrderModel.TYPE + "} = ?type";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter(MarketplacecommerceservicesConstants.CODE, orderreferancenumber);
			query.addQueryParameter("type", "Parent");
			orderModelList = getFlexibleSearchService().<OrderModel> search(query).getResult();
			if (!CollectionUtils.isEmpty(orderModelList))
			{
				return orderModelList;
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

		return orderModelList;
	}
}
