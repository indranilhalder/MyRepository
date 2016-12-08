/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.List;

import com.tisl.mpl.data.VoucherDiscountData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
public interface MplVoucherService
{

	/**
	 * @param cartModel
	 *
	 */
	void recalculateCartForCoupon(CartModel cartModel, OrderModel orderModel) throws EtailNonBusinessExceptions;

	/**
	 * @param voucherModel
	 * @param abstractOrderModel
	 * @return List<AbstractOrderEntry>
	 */
	List<AbstractOrderEntry> getOrderEntriesFromVoucherEntries(VoucherModel voucherModel, AbstractOrderModel abstractOrderModel);

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
	 * @param voucherModel
	 * @param abstractOrderModel
	 * @return List<AbstractOrderEntryModel>
	 */
	List<AbstractOrderEntryModel> getOrderEntryModelFromVouEntries(VoucherModel voucherModel, AbstractOrderModel abstractOrderModel);

	/**
	 * @param voucher
	 * @param abstractOrderModel
	 * @param voucherCode
	 * @param applicableOrderEntryList
	 * @throws EtailNonBusinessExceptions
	 */
	void setApportionedValueForVoucher(VoucherModel voucher, AbstractOrderModel abstractOrderModel, String voucherCode,
			List<AbstractOrderEntryModel> applicableOrderEntryList) throws EtailNonBusinessExceptions;

	/**
	 * @param lastVoucher
	 * @param cartModel
	 * @param applicableOrderEntryList
	 * @return VoucherDiscountData
	 * @throws VoucherOperationException
	 */
	VoucherDiscountData checkCartAfterApply(VoucherModel lastVoucher, CartModel cartModel, OrderModel orderModel,
			List<AbstractOrderEntryModel> applicableOrderEntryList) throws VoucherOperationException, EtailNonBusinessExceptions;

	/**
	 * @param voucherCode
	 * @param cartModel
	 * @throws VoucherOperationException
	 */
	void releaseVoucher(String voucherCode, CartModel cartModel, OrderModel orderModel) throws VoucherOperationException;

	/**
	 * @param cartModel
	 * @throws VoucherOperationException
	 * @throws EtailNonBusinessExceptions
	 */
	void checkCartWithVoucher(CartModel cartModel) throws VoucherOperationException, EtailNonBusinessExceptions;

}
