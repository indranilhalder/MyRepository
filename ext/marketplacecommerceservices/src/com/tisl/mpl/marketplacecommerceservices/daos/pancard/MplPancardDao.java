/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.pancard;

import de.hybris.platform.core.model.PancardInformationModel;
import de.hybris.platform.core.model.order.OrderModel;

import java.util.List;


/**
 * @author TCS
 *
 */
public interface MplPancardDao
{
	public List<PancardInformationModel> getPanCardOrderId(String orderreferancenumber);

	public List<OrderModel> getOrderForCode(String orderreferancenumber);
}
