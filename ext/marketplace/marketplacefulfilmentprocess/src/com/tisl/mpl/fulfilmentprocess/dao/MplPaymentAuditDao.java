/**
 *
 */
package com.tisl.mpl.fulfilmentprocess.dao;

import com.tisl.mpl.core.model.MplPaymentAuditModel;


/**
 * @author TCS
 *
 */
public interface MplPaymentAuditDao
{

	/**
	 * @param cartGUID
	 * @return MplPaymentAuditModel
	 */
	public MplPaymentAuditModel getAuditList(String cartGUID);
}
