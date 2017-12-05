/**
 *
 */
package com.tisl.mpl.coupon.facade;

import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.List;
import java.util.Map;

import com.tisl.mpl.data.CouponHistoryData;
import com.tisl.mpl.data.VoucherDiscountData;
import com.tisl.mpl.data.VoucherDisplayData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.wsdto.OfferListWsData;


/**
 * @author TCS
 *
 */
public interface MplCouponFacade
{

	/**
	 * @param orderModel
	 * @param cartModel
	 * @param couponStatus
	 * @param reddemIdentifier
	 * @return VoucherDiscountData
	 * @throws VoucherOperationException
	 */
	VoucherDiscountData calculateValues(OrderModel orderModel, CartModel cartModel, boolean couponStatus, boolean reddemIdentifier)
			throws VoucherOperationException;


	/**
	 * @return ArrayList<VoucherModel>
	 */
	List<VoucherModel> getAllCoupons();


	/**
	 * @param cart
	 * @param customer
	 * @param voucherList
	 * @return ArrayList<VoucherDisplayData>
	 */
	List<VoucherDisplayData> displayTopCoupons(CartModel cart, CustomerModel customer, List<VoucherModel> voucherList);


	/**
	 * @param voucherCode
	 * @param cartModel
	 * @param orderModel
	 * @return boolean
	 * @throws VoucherOperationException
	 * @throws JaloInvalidParameterException
	 * @throws NumberFormatException
	 */
	boolean applyVoucher(String voucherCode, final CartModel cartModel, final OrderModel orderModel)
			throws VoucherOperationException, EtailNonBusinessExceptions;


	/**
	 * @param cartModel
	 * @throws EtailNonBusinessExceptions
	 *
	 */
	void recalculateCartForCoupon(CartModel cartModel, OrderModel orderModel) throws EtailNonBusinessExceptions;


	/**
	 * @param cart
	 * @throws VoucherOperationException
	 * @throws EtailNonBusinessExceptions
	 */
	boolean releaseVoucherInCheckout(CartModel cart) throws VoucherOperationException, EtailNonBusinessExceptions;


	/**
	 * @param discountList
	 * @param voucherList
	 * @param cartSubTotal
	 * @param promoCalcValue
	 * @param lastVoucher
	 * @param discountAmt
	 * @return List<DiscountValue>
	 */
	List<DiscountValue> setGlobalDiscount(List<DiscountValue> discountList, List<DiscountModel> voucherList, double cartSubTotal,
			double promoCalcValue, VoucherModel lastVoucher, double discountAmt);


	/**
	 *
	 * @param voucherModel
	 * @param cartModel
	 * @return List<AbstractOrderEntry>
	 */
	List<AbstractOrderEntry> getOrderEntriesFromVoucherEntries(final VoucherModel voucherModel, final CartModel cartModel);


	/**
	 * @param voucherCode
	 * @param cartModel
	 * @param orderModel
	 * @throws VoucherOperationException
	 */
	void releaseVoucher(String voucherCode, CartModel cartModel, OrderModel orderModel) throws VoucherOperationException;


	/**
	 *
	 * @param voucher
	 * @param abstractOrderModel
	 * @param voucherCode
	 * @param applicableOrderEntryList
	 * @throws EtailNonBusinessExceptions
	 */
	void setApportionedValueForVoucher(VoucherModel voucher, AbstractOrderModel abstractOrderModel, String voucherCode,
			List<AbstractOrderEntryModel> applicableOrderEntryList);


	/**
	 *
	 * @param customer
	 * @param pageableData
	 * @return SearchPageData<VoucherDisplayData>
	 */
	SearchPageData<VoucherDisplayData> getAllClosedCoupons(CustomerModel customer, PageableData pageableData);

	/**
	 *
	 * TPR-7486
	 */
	List<VoucherModel> getAllPaymentModeSpecificOffers();

	Map<String, Double> getPaymentModerelatedVoucherswithTotal();

	OfferListWsData getAllOffersForMobile();



	/**
	 * @param customer
	 * @return Map<String, Double>
	 * @throws VoucherOperationException
	 */
	Map<String, Double> getInvalidatedCouponCountSaved(CustomerModel customer) throws VoucherOperationException;


	/**
	 * @param paymentInfo
	 * @param abstractOrderModel
	 */
	void updatePaymentInfoSession(Map<String, Double> paymentInfo, AbstractOrderModel abstractOrderModel);


	/**
	 * @param customer
	 * @param pageableData
	 * @return SearchPageData<CouponHistoryData>
	 * @throws VoucherOperationException
	 */
	SearchPageData<CouponHistoryData> getVoucherHistoryTransactions(final CustomerModel customer, final PageableData pageableData)
			throws VoucherOperationException;


	/**
	 * Added for TPR-4461
	 *
	 * @param orderModel
	 * @return String
	 */
	public String getCouponMessageInfo(final AbstractOrderModel orderModel);


	/**
	 * @param voucherCode
	 * @param cartModel
	 * @param orderModel
	 * @return boolean
	 * @throws VoucherOperationException
	 * @throws JaloInvalidParameterException
	 * @throws NumberFormatException
	 */
	boolean applyCartVoucher(String voucherCode, final CartModel cartModel, final OrderModel orderModel)
			throws VoucherOperationException, EtailNonBusinessExceptions;


}
