/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;

import com.tisl.mpl.data.MplPromoPriceWsDTO;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.wsdto.BillingAddressWsData;
import com.tisl.mpl.wsdto.MplUserResultWsDto;
import com.tisl.mpl.wsdto.PaymentServiceWsData;


/**
 * @author TCS
 * @param <GetOrderStatusResponse>
 *
 */
public interface MplPaymentWebService
{

	/**
	 * To get the COD Eligiblity for Items in the Cart
	 *
	 * @param cartID
	 * @param customer_ID
	 * @return
	 */
	public PaymentServiceWsData getCODDetails(final String cartID, final String customerID) throws EtailNonBusinessExceptions;

	/**
	 * Update CARD Transactions and Save Payment Ibnfo and Address details
	 *
	 * @param orderStatusResponse
	 * @param paymentMode
	 * @param cart_ID
	 * @return
	 */
	public PaymentServiceWsData updateCardTransactionDetails(GetOrderStatusResponse orderStatusResponseJuspay,
			final String paymentMode, final String cartID, final String userID) throws EtailNonBusinessExceptions;

	/**
	 * Update COD Transactions and Save Payment Info
	 *
	 * @param paymentMode
	 * @param cart_ID
	 * @param custName
	 * @param cartValue
	 * @param totalCODCharge
	 * @return
	 */
	public PaymentServiceWsData updateCODTransactionDetails(final String cartID, final String userID)
			throws EtailNonBusinessExceptions;//totalCODCharge (total price+conv charge)

	/**
	 * Get Billing Address Details
	 *
	 * @param cardRefNo
	 * @return
	 */
	public BillingAddressWsData getBillingAddress(final String originalUid, final String cardRefNo)
			throws EtailNonBusinessExceptions;

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
	 * @param cartID
	 * @return MplPromotionDTO
	 * @throws EtailNonBusinessExceptions
	 */
	public MplPromoPriceWsDTO validateBinNumber(final String binNo, final String paymentMode, final String bankName)
			throws EtailNonBusinessExceptions;

	/**
	 * @param userId
	 * @return
	 */
	public CustomerModel getCustomer(String userId);

	/**
	 * @param cartID
	 * @return
	 */
	public CartModel findCartValues(String cartID);

	/**
	 * @param guid
	 * @return CartModel
	 */
	public CartModel findCartAnonymousValues(final String guid);

	/**
	 * @param cartId
	 * @return PaymentServiceWsData
	 */
	public PaymentServiceWsData potentialPromotionOnPaymentMode(final String userId, final String cartId);

}
