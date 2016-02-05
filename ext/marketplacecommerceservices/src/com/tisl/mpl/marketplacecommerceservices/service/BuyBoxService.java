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

	Integer getBuyboxInventoryForSearch(String productCode) throws EtailNonBusinessExceptions;

	//sellers display
	List<BuyBoxModel> buyBoxPriceNoStock(String ProductCode) throws EtailNonBusinessExceptions;

	//get rich Attribute data
	RichAttributeModel getRichAttributeData(String ussid) throws EtailNonBusinessExceptions;

	//get seller data
	Set<Map<BuyBoxModel, RichAttributeModel>> getsellersDetails(String productCode) throws EtailNonBusinessExceptions;

	/**
	 * @param ussid
	 * @return
	 */
	BuyBoxModel getpriceForUssid(String ussid);


}
