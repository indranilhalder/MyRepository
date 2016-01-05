/**
 *
 */
package com.tisl.mpl.service;

import com.tisl.mpl.model.SellerTypeGlobalCodesModel;


/**
 * @author TCS
 *
 */
public interface MplSellerTypeGlobalCodesService
{

	/**
	 * @param code
	 * @return
	 */
	SellerTypeGlobalCodesModel getDescription(String code);
}
