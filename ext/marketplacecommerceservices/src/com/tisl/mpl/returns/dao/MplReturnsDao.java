/**
 * 
 */
package com.tisl.mpl.returns.dao;

import com.tisl.mpl.core.model.MplCustomerBankAccountDetailsModel;

/**
 * @author TECHOUTS
 *
 */
public interface MplReturnsDao
{
	/**
	 * 
	 * @param customerId
	 * @return MplCustomerBankAccountDetailsModel
	 */
  MplCustomerBankAccountDetailsModel getCustomerBankDetailsById(String customerId);
}
