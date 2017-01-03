/**
 *
 */
package com.tisl.mpl.facades;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.exceptions.CalculationException;

import java.util.Map;

import com.tisl.mpl.data.MplPromoPriceWsDTO;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.wsdto.BillingAddressWsData;
import com.tisl.mpl.wsdto.CartDataDetailsWsDTO;
import com.tisl.mpl.wsdto.MplSavedCardDTO;
import com.tisl.mpl.wsdto.MplUserResultWsDto;
import com.tisl.mpl.wsdto.PaymentServiceWsData;


/**
 * @author TCS
 *
 */
public interface MplPaymentWebFacade
{
	/**
	 * To get the COD Eligibility for Items in the Cart
	 *
	 * @param abstractOrder
	 * @param customerID
	 * @return PaymentServiceWsData
	 */
	public PaymentServiceWsData getCODDetails(final AbstractOrderModel abstractOrder, final String customerID);

	/**
	 * Update CARD Transactions and Save Payment Ibnfo and Address details
	 *
	 * @param orderStatusResponse
	 * @param paymentMode
	 * @param cartID
	 * @param userId
	 * @return PaymentServiceWsData
	 */
	public PaymentServiceWsData updateCardTransactionDetails(final String orderStatusResponse, final String paymentMode,
			final String cartID, String userId);

	/**
	 * Update COD Transactions and Save Payment Info
	 *
	 * @param paymentMode
	 * @param cartID
	 * @param custName
	 * @param cartValue
	 * @param totalCODCharge
	 * @param userId
	 * @return PaymentServiceWsData
	 */
	public PaymentServiceWsData updateCODTransactionDetails(final String cartID, final String userId);//totalCODCharge (total price+conv charge)

	/**
	 * Get Billing Address Details
	 *
	 * @param cardRefNo
	 * @return BillingAddressWsData
	 */
	public BillingAddressWsData getBillingAddress(final String originalUid, final String cardRefNo);


	/**
	 * This method fetches delete the saved cards
	 *
	 * @param cardToken
	 * @return MplSavedCardDTO
	 * @throws EtailNonBusinessExceptions
	 */
	public MplSavedCardDTO deleteSavedCards(final String cardToken) throws EtailNonBusinessExceptions;

	/**
	 * @Description Update Transaction and related Retails for COD
	 * @param juspayOrderId
	 * @param channel
	 * @param cartID
	 * @return MplUserResultWsDto
	 * @throws EtailNonBusinessExceptions
	 */

	public MplUserResultWsDto createEntryInAudit(final String juspayOrderId, final String channel, final String cartID,
			final String userId) throws EtailNonBusinessExceptions;

	/**
	 * Check Valid Bin Number and Apply promotion for new char and saved card
	 *
	 * @param binNo
	 * @param bankName
	 * @param paymentMode
	 * @param cart
	 * @return MplPromotionDTO
	 * @throws EtailNonBusinessExceptions
	 */
	public MplPromoPriceWsDTO binValidation(final String binNo, final String paymentMode, final CartModel cart,
			final String userId, final String bankName) throws EtailNonBusinessExceptions;

	/**
	 * Check Valid Bin Number and Apply promotion for new char and saved card
	 *
	 * @param binNo
	 * @param bankName
	 * @param paymentMode
	 * @param order
	 * @return MplPromotionDTO
	 * @throws EtailNonBusinessExceptions
	 */
	public MplPromoPriceWsDTO binValidation(final String binNo, final String paymentMode, final OrderModel order,
			final String userId, final String bankName) throws EtailNonBusinessExceptions;

	/**
	 * @param userId
	 * @return CustomerModel
	 */
	public CustomerModel getCustomer(final String userId);

	/**
	 * @param cartID
	 * @return CartModel
	 */
	public CartModel findCartValues(final String cartID);

	/**
	 * @param guid
	 * @return CartModel
	 */
	public CartModel findCartAnonymousValues(final String guid);

	/**
	 * @param cartModel
	 * @return PaymentServiceWsData
	 */
	public PaymentServiceWsData potentialPromotionOnPaymentMode(final AbstractOrderModel cartModel);

	/**
	 * @param userId
	 * @param cartGuId
	 * @param pincode
	 * @return CartDataDetailsWsDTO
	 */
	public CartDataDetailsWsDTO displayOrderSummary(final String userId, final String cartId, final String cartGuId,
			final String pincode);

	/**
	 * @param order
	 * @return updated
	 */
	public boolean updateOrder(final OrderModel order) throws EtailBusinessExceptions, InvalidCartException, CalculationException;


	/**
	 *
	 * @param status
	 * @param mWRefCode
	 * @param channelWeb
	 * @param guid
	 * @param walletOrderId
	 */
	void entryInTPWaltAuditMobile(final String status, final String mWRefCode, String channelWeb, String guid,
			String walletOrderId);


	/**
	 *
	 * @param cart
	 * @param refernceCode
	 */
	void saveTPWalletPaymentInfoMobile(AbstractOrderModel cart, final String refernceCode, Map<String, Double> paymentMode);
}
