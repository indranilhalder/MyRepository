/**
 *
 */
package com.tisl.mpl.promotion.dao;

import de.hybris.platform.core.model.order.OrderModel;

import java.util.List;

import com.tisl.mpl.core.model.WalletApportionReturnInfoModel;


/**
 * @author techouts
 *
 */
public interface MplQcPaymentFailDao
{
	List<WalletApportionReturnInfoModel> getPendingQcPayments();

	OrderModel getOrder(String orderCode);
}
