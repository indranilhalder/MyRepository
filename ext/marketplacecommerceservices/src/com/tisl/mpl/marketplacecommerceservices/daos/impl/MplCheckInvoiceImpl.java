/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.InvoiceDetailModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MplCheckInvoice;


/**
 * @author TCS
 *
 */
public class MplCheckInvoiceImpl implements MplCheckInvoice
{
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<InvoiceDetailModel> checkSameInvoice(final String invoice)
	{
		final String queryString = "SELECT {id.PK} FROM {" + InvoiceDetailModel._TYPECODE + " AS id}" + " WHERE {id:"
				+ InvoiceDetailModel.INVOICENO + "}=?invoice";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("invoice", invoice);
		return flexibleSearchService.<InvoiceDetailModel> search(query).getResult();
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
	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}
}
