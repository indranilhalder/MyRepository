/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;




public interface BuyBoxDao
{


	public List<BuyBoxModel> getBuyBoxPriceForUssId(final String ussid) throws EtailNonBusinessExceptions;


	List<BuyBoxModel> buyBoxPrice(String ProductCode) throws EtailNonBusinessExceptions;

	List<BuyBoxModel> getBuyboxPricesForSearch(String ProductCode) throws EtailNonBusinessExceptions;

	List<BuyBoxModel> invalidatePkofBuybox(Date currenttime) throws EtailNonBusinessExceptions;

	List<BuyBoxModel> buyBoxForSizeGuide(String productCode, String sellerId);

	Integer getBuyboxAvailableInventoryForSearch(String productCode, String productType) throws EtailNonBusinessExceptions;

	//if all the sellers has zero stock
	List<BuyBoxModel> buyBoxPriceNoStock(String productCode) throws EtailNonBusinessExceptions;


	//get rich Attribute data
	RichAttributeModel getRichAttributeData(String ussid) throws EtailNonBusinessExceptions;

	//get seller details
	Set<Map<BuyBoxModel, RichAttributeModel>> getsellersDetails(String productCode) throws EtailNonBusinessExceptions;

	public List<BuyBoxModel> buyBoxStockForSeller(final String sellerID);

	/**
	 * @param ussid
	 * @return
	 */
	BuyBoxModel priceForUssid(String ussid);

}