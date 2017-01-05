/**
 *
 */
package com.hybris.oms.tata.daos;

import java.util.List;

import com.tisl.mpl.model.SellerMasterModel;


/**
 * @author techouts
 *
 */
public interface SellerMasterModelDao
{

	/**
	 * @return
	 *
	 */
	List<String> getAllSellers();

	/**
	 *
	 * Based on SellerId return seller information
	 * 
	 * @return SellerMasterModel
	 *
	 */
	SellerMasterModel getSeller(String sellerID);
}
