/**
 *
 */
package com.tisl.mpl.coupon.facade;

import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.List;
import java.util.Map;

import com.tisl.mpl.data.CouponHistoryData;
import com.tisl.mpl.data.CouponHistoryStoreDTO;
import com.tisl.mpl.data.VoucherDiscountData;
import com.tisl.mpl.data.VoucherDisplayData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
public interface MplCouponFacade
{

	/**
	 * @param cartModel
	 * @param couponStatus
	 * @param reddemIdentifier
	 * @return VoucherDiscountData
	 */
	VoucherDiscountData calculateValues(CartModel cartModel, boolean couponStatus, boolean reddemIdentifier);


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
	 * @return boolean
	 * @throws VoucherOperationException
	 * @throws JaloInvalidParameterException
	 * @throws NumberFormatException
	 */
	boolean applyVoucher(String voucherCode, CartModel cartModel) throws VoucherOperationException, EtailNonBusinessExceptions;


	/**
	 * @param cartModel
	 * @throws EtailNonBusinessExceptions
	 *
	 */
	void recalculateCartForCoupon(CartModel cartModel) throws EtailNonBusinessExceptions;


	/**
	 * @param cart
	 * @throws VoucherOperationException
	 * @throws EtailNonBusinessExceptions
	 */
	void releaseVoucherInCheckout(CartModel cart) throws VoucherOperationException, EtailNonBusinessExceptions;


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
	 * @throws VoucherOperationException
	 */
	void releaseVoucher(String voucherCode, CartModel cartModel) throws VoucherOperationException;


	/**
	 *
	 * @param voucher
	 * @param cartModel
	 * @param voucherCode
	 * @param applicableOrderEntryList
	 * @throws EtailNonBusinessExceptions
	 */
	void setApportionedValueForVoucher(VoucherModel voucher, CartModel cartModel, String voucherCode,
			List<AbstractOrderEntryModel> applicableOrderEntryList);


	/**
	 *
	 * @param customer
	 * @param pageableData
	 * @return SearchPageData<VoucherDisplayData>
	 */
	SearchPageData<VoucherDisplayData> getAllClosedCoupons(CustomerModel customer, PageableData pageableData);


	/**
	 * @param customer
	 * @return CouponHistoryStoreDTO
	 * @throws VoucherOperationException
	 */
	CouponHistoryStoreDTO getCouponTransactions(CustomerModel customer) throws VoucherOperationException;


	/**
	 * @param paymentInfo
	 * @param cartModel
	 */
	void updatePaymentInfoSession(Map<String, Double> paymentInfo, CartModel cartModel);


	/**
	 * @param customer
	 * @param pageableData
	 * @return SearchPageData<CouponHistoryData>
	 * @throws VoucherOperationException
	 */
	SearchPageData<CouponHistoryData> getVoucherHistoryTransactions(final CustomerModel customer, final PageableData pageableData)
			throws VoucherOperationException;

}
