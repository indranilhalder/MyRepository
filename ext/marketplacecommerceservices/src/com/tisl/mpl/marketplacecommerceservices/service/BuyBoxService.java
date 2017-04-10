/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;

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

	//List<BuyBoxModel> getStockFromBuyBox(String ProductCode) throws EtailNonBusinessExceptions;

	List<BuyBoxModel> getBuyboxPricesForSearch(String productCode) throws EtailNonBusinessExceptions;

	//INC144315542_INC144314878_INC_11113

	List<BuyBoxModel> getBuyboxPricesForSizeVariant(String productCode) throws EtailNonBusinessExceptions;

	List<BuyBoxModel> invalidatePkofBuybox(Date currenttime) throws EtailNonBusinessExceptions;

	/**
	 * This service method will return buybox prices for product code
	 *
	 * @param productCode
	 * @param sellerId
	 * @return
	 */
	BuyBoxModel buyboxForSizeGuide(String productCode, String sellerId);

	Integer getBuyboxInventoryForSearch(String productCode, String productType) throws EtailNonBusinessExceptions;

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

	List<ClassAttributeAssignmentModel> getClassAttrAssignmentsForCode(String code);

	//TPR-3736
	public Map<String, List<Double>> getBuyBoxDataForUssids(final String ussidList) throws EtailNonBusinessExceptions;



}
