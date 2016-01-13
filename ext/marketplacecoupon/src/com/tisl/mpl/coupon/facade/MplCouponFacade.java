/**
 *
 */
package com.tisl.mpl.coupon.facade;

import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.voucher.jalo.util.VoucherEntrySet;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.List;

import com.tisl.mpl.data.AllVoucherListData;
import com.tisl.mpl.data.CouponHistoryStoreDTO;
import com.tisl.mpl.data.VoucherDiscountData;
import com.tisl.mpl.data.VoucherDisplayData;


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
	 * @throws CalculationException
	 * @throws JaloSecurityException
	 * @throws JaloInvalidParameterException
	 * @throws NumberFormatException
	 */
	boolean applyVoucher(String voucherCode, CartModel cartModel) throws VoucherOperationException, CalculationException,
			NumberFormatException, JaloInvalidParameterException, JaloSecurityException;


	/**
	 * @param cartModel
	 * @throws JaloPriceFactoryException
	 * @throws CalculationException
	 */
	void recalculateCartForCoupon(CartModel cartModel) throws JaloPriceFactoryException, CalculationException;


	/**
	 * @param cart
	 * @throws JaloPriceFactoryException
	 * @throws CalculationException
	 */
	void releaseVoucherInCheckout(CartModel cart) throws JaloPriceFactoryException, CalculationException;


	/**
	 * @param customer
	 * @param voucherList
	 * @return AllVoucherListData
	 */
	AllVoucherListData getAllVoucherList(CustomerModel customer, List<VoucherModel> voucherList);

	/**
	 * @param customer
	 * @return CouponHistoryStoreDTO
	 * @throws VoucherOperationException
	 */
	CouponHistoryStoreDTO getCouponTransactions(CustomerModel customer) throws VoucherOperationException;


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
	 * @param voucherEntrySet
	 * @return List<AbstractOrderEntry>
	 */
	List<AbstractOrderEntry> getOrderEntriesFromVoucherEntries(VoucherEntrySet voucherEntrySet);


	/**
	 * @param voucherCode
	 * @param cartModel
	 * @throws VoucherOperationException
	 */
	void releaseVoucher(String voucherCode, CartModel cartModel) throws VoucherOperationException;


	/**
	 * @param voucher
	 * @param cartModel
	 * @param voucherCode
	 */
	void setApportionedValueForVoucher(VoucherModel voucher, CartModel cartModel, String voucherCode);

}