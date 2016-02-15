/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import com.tisl.mpl.model.SellerMasterModel;


/**
 * @author TCS
 *
 */
public interface MplSellerMasterDao
{
	SellerMasterModel getSellerMaster(final String sellerID);
}
