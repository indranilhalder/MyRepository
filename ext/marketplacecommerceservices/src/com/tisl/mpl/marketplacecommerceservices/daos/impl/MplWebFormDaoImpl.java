/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.core.model.MplWebCrmModel;
import com.tisl.mpl.core.model.MplWebCrmTicketModel;
import com.tisl.mpl.facades.cms.data.WebFormData;
import com.tisl.mpl.marketplacecommerceservices.daos.MplWebFormDao;


/**
 * @author TCS
 *
 */
public class MplWebFormDaoImpl implements MplWebFormDao
{

	private static final Logger LOG = Logger.getLogger(MplWebFormDaoImpl.class);

	@Resource
	private FlexibleSearchService flexibleSearchService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplWebFormDao#getWebCRMParentNodes()
	 */
	@Override
	public List<MplWebCrmModel> getWebCRMParentNodes()
	{
		final String queryString = "SELECT {form: " + MplWebCrmModel.PK + " } " + " FROM { " + MplWebCrmModel._TYPECODE
				+ " AS form}" + "where " + " { form." + MplWebCrmModel.NODETYPE + " }  = ?nodeType order by " + "{"
				+ MplWebCrmModel.SERIALNUM + "}";
		LOG.debug("Fetching MplWebCrmModel " + queryString);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("nodeType", "L1");
		return flexibleSearchService.<MplWebCrmModel> search(query).getResult();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplWebFormDao#getWebCRMByNodes(java.lang.String)
	 */
	@Override
	public List<MplWebCrmModel> getWebCRMByNodes(final String nodeParent)
	{
		final String queryString = "SELECT {form: " + MplWebCrmModel.PK + " } " + " FROM { " + MplWebCrmModel._TYPECODE
				+ " AS form} " + "where " + " { form." + MplWebCrmModel.NODEPARENT + " }  = ?nodeParent order by {"
				+ MplWebCrmModel.SERIALNUM + "}";
		LOG.debug("Fetching MplWebCrmModel " + queryString);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("nodeParent", nodeParent);
		return flexibleSearchService.<MplWebCrmModel> search(query).getResult();
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplWebFormDao#getWebCRMTicket(java.lang.String)
	 */
	@Override
	public MplWebCrmTicketModel getWebCRMTicket(final String commerceTicketId)
	{
		final String queryString = "SELECT {form: " + MplWebCrmTicketModel.PK + " } " + " FROM { " + MplWebCrmTicketModel._TYPECODE
				+ " AS form} " + "where " + " { form." + MplWebCrmTicketModel.COMMERCETICKETID + " }  = ?commerceTicketId ";
		LOG.debug("Fetching MplWebCrmModel " + queryString);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("commerceTicketId", commerceTicketId);
		return flexibleSearchService.<MplWebCrmTicketModel> search(query).getResult().get(0);
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplWebFormDao#checkDuplicateWebCRMTickets(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkDuplicateWebCRMTickets(final WebFormData formData)
	{
		boolean returnResult = false;
		String queryString = "SELECT {form: " + MplWebCrmTicketModel.PK + " } " + " FROM { " + MplWebCrmTicketModel._TYPECODE
				+ " AS form } " + " where { form." + MplWebCrmTicketModel.L0CODE + " }  = ?L0code " + " AND { form."
				+ MplWebCrmTicketModel.L1CODE + " }  = ?L1code " + " AND { form." + MplWebCrmTicketModel.L2CODE + " }  = ?L2code "
				+ " AND { form." + MplWebCrmTicketModel.L3CODE + " }  = ?L3code " + " AND { form." + MplWebCrmTicketModel.L4CODE
				+ " }  = ?L4code " + " AND { form." + MplWebCrmTicketModel.CUSTOMEREMAIL + " }  = ?customerEmail ";
		//TISHS-172
		if (StringUtils.isNotEmpty(formData.getOrderCode()))
		{
			queryString += " AND  { form." + MplWebCrmTicketModel.ORDERCODE + " }  = ?orderCode ";
		}
		if (StringUtils.isNotEmpty(formData.getSubOrderCode()))
		{
			queryString += " AND { form." + MplWebCrmTicketModel.SUBORDERCODE + " }  = ?subOrderCode ";
		}
		if (StringUtils.isNotEmpty(formData.getTransactionId()))
		{
			queryString += " AND { form." + MplWebCrmTicketModel.TRANSACTIONID + " }  = ?transactionId ";
		}

		LOG.debug("Fetching MplWebCrmModel " + queryString);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("L0code", formData.getL0code());
		query.addQueryParameter("L1code", formData.getL1code());
		query.addQueryParameter("L2code", formData.getL2code());
		query.addQueryParameter("L3code", formData.getL3code());
		query.addQueryParameter("L4code", formData.getL4code());
		query.addQueryParameter("customerName", formData.getCustomerName());
		query.addQueryParameter("customerEmail", formData.getCustomerEmail());

		if (StringUtils.isNotEmpty(formData.getOrderCode()))
		{
			query.addQueryParameter("orderCode", formData.getOrderCode());
		}
		if (StringUtils.isNotEmpty(formData.getSubOrderCode()))
		{
			query.addQueryParameter("subOrderCode", formData.getSubOrderCode());
		}
		if (StringUtils.isNotEmpty(formData.getTransactionId()))
		{
			query.addQueryParameter("transactionId", formData.getTransactionId());
		}


		final List<MplWebCrmTicketModel> result = flexibleSearchService.<MplWebCrmTicketModel> search(query).getResult();
		if (CollectionUtils.isNotEmpty(result) && result.size() > 0)
		{
			returnResult = true;
		}

		return returnResult;
	}
}