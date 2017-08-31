/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import java.util.List;

import com.tisl.mpl.core.model.ExchangeCouponValueModel;
import com.tisl.mpl.core.model.ExchangeTransactionModel;


/**
 * @author TCS
 *
 */
public interface ExchangeGuideDao
{
	/*
	 * @Javadoc
	 * 
	 * @returns All L4 for which Exchange is Applicable
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.ExchangeGuideDao#getDistinctL4()
	 */
	public boolean isExchangable(final String categoryCode);

	/**
	 *
	 * @param categoryCode
	 * @return List<ExchangeCouponValueModel>
	 * @description Gets All Exchange Applicable L4's for L3
	 */
	List<ExchangeCouponValueModel> getExchangeOptionforCategorycode(String categoryCode);

	/**
	 * @param pincode
	 * @return boolean value
	 * @description Checks whether a pincode is Exchange serviceable
	 */
	boolean isBackwardServiceble(String pincode);

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
	 * @param exId
	 * @return ExchangeTransactionModel
	 * @description Gets Exchange Transaction Model for Id
	 */
	public List<ExchangeTransactionModel> getTeporaryExchangeModelforId(final String exId);


}