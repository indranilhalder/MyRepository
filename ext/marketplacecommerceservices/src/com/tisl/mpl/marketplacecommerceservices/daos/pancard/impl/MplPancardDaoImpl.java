/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.pancard.impl;

import de.hybris.platform.core.model.PancardInformationModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.daos.pancard.MplPancardDao;


/**
 * @author TCS
 *
 */
public class MplPancardDaoImpl implements MplPancardDao
{



	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private ModelService modelService;

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
	public PancardInformationModel getPanCardOrderId(final String orderreferancenumber)
	{
		PancardInformationModel returnModel = null;
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
			returnModel = pancardInfo.get(0);
		}

		return returnModel;
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
