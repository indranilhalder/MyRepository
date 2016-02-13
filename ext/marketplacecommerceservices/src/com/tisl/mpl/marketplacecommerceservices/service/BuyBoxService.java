/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;



public interface BuyBoxService
{

	List<BuyBoxModel> buyboxPrice(String productCode) throws EtailNonBusinessExceptions;

	List<BuyBoxModel> getBuyboxPricesForSearch(String productCode) throws EtailNonBusinessExceptions;

	List<BuyBoxModel> invalidatePkofBuybox(Date currenttime) throws EtailNonBusinessExceptions;

<<<<<<< HEAD
	List<BuyBoxModel> buyboxForSizeGuide(String productCode, String sellerId);

	Integer getBuyboxInventoryForSearch(String productCode, String productType) throws EtailNonBusinessExceptions;
=======
	Integer getBuyboxInventoryForSearch(String productCode) throws EtailNonBusinessExceptions;
>>>>>>> BRANCH_TCS-HYCOMM-R1PS-BN-38

	//sellers display
	List<BuyBoxModel> buyBoxPriceNoStock(String ProductCode) throws EtailNonBusinessExceptions;

	//get rich Attribute data
	RichAttributeModel getRichAttributeData(String ussid) throws EtailNonBusinessExceptions;

	//get seller data
	Set<Map<BuyBoxModel, RichAttributeModel>> getsellersDetails(String productCode) throws EtailNonBusinessExceptions;

	//
	List<BuyBoxModel> buyBoxStockForSeller(final String sellerID);

	/**
	 * @param ussid
	 * @return
	 */
	BuyBoxModel getpriceForUssid(String ussid);


}
