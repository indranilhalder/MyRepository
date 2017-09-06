/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.core.model.product.ProductModel;

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

	//added for jewellery
	public List<BuyBoxModel> buyBoxPriceForJewellery(String pcmUssid) throws EtailNonBusinessExceptions;

	List<BuyBoxModel> getBuyboxPricesForSearch(String ProductCode) throws EtailNonBusinessExceptions;

	List<BuyBoxModel> invalidatePkofBuybox(Date currenttime) throws EtailNonBusinessExceptions;

	public BuyBoxModel buyBoxForSizeGuide(String productCode, String sellerId);

	Integer getBuyboxAvailableInventoryForSearch(String productCode, String productType) throws EtailNonBusinessExceptions;

	//if all the sellers has zero stock
	List<BuyBoxModel> buyBoxPriceNoStock(String productCode) throws EtailNonBusinessExceptions;


	//get rich Attribute data
	RichAttributeModel getRichAttributeData(String ussid) throws EtailNonBusinessExceptions;

	//get seller details
	//CKD: TPR-3809
	//Set<Map<BuyBoxModel, RichAttributeModel>> getsellersDetails(String productCode) throws EtailNonBusinessExceptions;
	Set<Map<BuyBoxModel, RichAttributeModel>> getsellersDetails(String productCode, String prodCatType)
			throws EtailNonBusinessExceptions;

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

	/**
	 * @param sellerArticleSKUList
	 * @return
	 */
	public List<BuyBoxModel> getBuyboxSellerPricesForSearch(List<String> sellerArticleSKUList) throws EtailNonBusinessExceptions;

	//INC144315542_INC144314878_INC_11113

	public List<BuyBoxModel> getBuyboxPricesForSizeVariant(String productCode) throws EtailNonBusinessExceptions;

	// TISPRD-8944
	List<BuyBoxModel> buyBoxPriceMobile(String ProductCode) throws EtailNonBusinessExceptions;

	/**
	 * TPR-5712 dao to get price for all the sellers
	 *
	 * @param productCode
	 * @return
	 */
	public List<BuyBoxModel> buyBoxPriceForAllSeller(final String productCode);

	//for fine jewellery pdp
	public ProductModel getProductDetailsByProductCode(final String productcode);

	/**
	 * @param ussID
	 * @return
	 */
	public List<BuyBoxModel> buyboxPriceForJewelleryWithVariant(String ussID);

	/**
	 * @param selectedUSSID
	 * @return
	 */
	String findPussid(String selectedUSSID);

	/**
	 * This Method is for Seller Monogramming Message Changes
	 *
	 * @param sellerId
	 * @param productCode
	 * @return String
	 */
	public String getSellerMonogrammingMsg(String productCode, String sellerId);

}
