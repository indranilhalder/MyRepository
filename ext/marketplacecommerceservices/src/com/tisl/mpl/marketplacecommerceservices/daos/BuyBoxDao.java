/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;

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

	public BuyBoxModel buyBoxForSizeGuide(String productCode, String sellerId);

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

	List<ClassAttributeAssignmentModel> getClassAttrAssignmentsForCode(String code) throws EtailNonBusinessExceptions;


	//TPR-3736
	public Map<String, List<Double>> getBuyBoxDataForUssids(final String ussidList) throws EtailNonBusinessExceptions;

	/**
	 * @param productCode
	 * @param pdpproduct
	 * @return
	 */
	public List<BuyBoxModel> buyboxPriceForMicrosite(String productCode, String pdpproduct) throws EtailNonBusinessExceptions;

	//INC144315542_INC144314878_INC_11113

	public List<BuyBoxModel> getBuyboxPricesForSizeVariant(String productCode) throws EtailNonBusinessExceptions;

}
