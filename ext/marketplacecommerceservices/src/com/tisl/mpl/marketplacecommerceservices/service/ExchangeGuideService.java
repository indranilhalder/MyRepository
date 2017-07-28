/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.List;

import com.tisl.mpl.core.model.ExchangeCouponValueModel;
import com.tisl.mpl.core.model.ExchangeTransactionModel;


/**
 * @author TCS
 *
 */
public interface ExchangeGuideService
{
	/**
	 * @Javadoc
	 * @description Fetches all L4 for which exchange is applicable
	 * @returns All L4 for which Exchange is Applicable
	 * @see com.tisl.mpl.marketplacecommerceservices.service.ExchangeGuideService#getDistinctL4()
	 * @description Fetches all L4 for which exchange is applicabl
	 */
	public boolean isExchangable(final String categoryCode);

	/**
	 *
	 * @param categoryCode
	 * @return List<ExchangeCouponValueModel>
	 * @description It will take the L3 category and fetch Price Matrix
	 *
	 */
	public List<ExchangeCouponValueModel> getExchangeGuideList(String categoryCode) throws ModelNotFoundException;

	/**
	 *
	 * @param l3code
	 * @param l4
	 * @param isWorking
	 * @param brand
	 * @param pincode
	 * @param guid
	 * @param productCode
	 * @param ussid
	 * @return String ExchangeId
	 * @description Generates Exchange Id before Add to Cart
	 */

	public String getExchangeID(final String l3code, final String l4, final String isWorking, final String brand,
			final String pincode, final String guid, final String productCode, final String ussid);

	/**
	 *
	 * @param l3code
	 * @param l4
	 * @param isWorking
	 * @return List<ExchangeCouponValueModel>
	 * @description Gets ExchangeCouponValueModel for Linking with Temporary Transaction Table
	 */
	public List<ExchangeCouponValueModel> getPriceMatrix(final String l3code, final String l4, final String isWorking);

	/**
	 *
	 * @param pincode
	 * @return boolean
	 */
	boolean isBackwardServiceble(String pincode);

	/**
	 *
	 * @param pincode
	 * @param exchangeId
	 * @return
	 */

	public boolean changePincode(final String pincode, final String exchangeId);


	public List<ExchangeTransactionModel> getTeporaryExchangeModelforId(final String exId);

	public boolean addToExchangeTable(final ExchangeTransactionModel ex);


	public String getExchangeRequestID(final List<ExchangeTransactionModel> exTraxList, final boolean isInternal,
			final String reason);


	public boolean removeFromTransactionTable(final String exchangeId, final String reason, final CartModel cart);

	/**
	 * @param childOrders
	 * @return
	 */
	String getExchangeRequestID(OrderModel orders);

	public void removeExchangefromCart(final CartModel cart);

	public void changeGuidforCartMerge(final CartModel cartGuid);

}