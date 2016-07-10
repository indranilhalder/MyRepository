/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import java.util.List;

import com.tisl.mpl.core.model.InvoiceDetailModel;


/**
 * @author 397968
 *
 */
public interface MplCheckInvoice
{
	List<InvoiceDetailModel> checkSameInvoice(final String invoice);

}
