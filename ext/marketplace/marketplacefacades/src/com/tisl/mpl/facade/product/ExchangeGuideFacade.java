/**
 *
 */
package com.tisl.mpl.facade.product;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;

import java.util.List;

import com.tisl.mpl.core.model.ExchangeTransactionModel;
import com.tisl.mpl.facades.product.data.ExchangeGuideData;


/**
 * @author TCS
 */

public interface ExchangeGuideFacade
{
	/*
	 * @Javadoc
	 * 
	 * @returns All L4 for which Exchange is Applicable
	 * 
	 * @see com.tisl.mpl.facade.product.ExchangeGuideFacade#getDistinctL4()
	 */
	public boolean isExchangable(String categoryCode);

	/**
	 *
	 * @param productCode
	 * @return List<ExchangeGuideData>
	 * @throws CMSItemNotFoundException
	 */
	public List<ExchangeGuideData> getExchangeGuide(final String productCode) throws CMSItemNotFoundException;

	/**
	 *
	 * @param exchangeParam
	 * @param cartguid
	 * @param productCode
	 * @param ussid
	 * @return Temporary Exchange Id
	 */
	public String getTemporaryExchangeId(final String exchangeParam, final String cartguid, final String productCode,
			final String ussid);


	public boolean isBackwardServiceble(final String pincode);

	public boolean changePincode(final String pincode, final String exchangeId);

	public boolean removeFromTransactionTable(final String exchangeId, final String reason, final CartModel cart);

	public List<ExchangeTransactionModel> getTeporaryExchangeModelforId(final String exId);

	/**
	 * @param childOrders
	 * @return
	 */
	String getExchangeRequestID(OrderModel childOrders);

	public boolean addToExchangeTable(final ExchangeTransactionModel ex);

	public void removeExchangefromCart(CartModel cart);

}