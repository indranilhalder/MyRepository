/**
 *
 */
package com.tisl.mpl.seller.product.facades;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;
import java.util.Map;

import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.RichAttributeData;
import com.tisl.mpl.facades.product.data.BuyBoxData;


/**
 * @author TCS
 *
 */
public interface BuyBoxFacade
{
	public BuyBoxData buyboxPrice(String ProductCode) throws EtailNonBusinessExceptions;

//CKD:TPR-250 Start
	//Added channel for TISPRD-8944
	public Map<String, Object> buyboxPricePDP(String ProductCode, String bBoxSellerId, String Channel) throws EtailNonBusinessExceptions;

	//CKD: TPR-3809
	//public List<SellerInformationData> getsellersDetails(String productCode) throws EtailNonBusinessExceptions;
	public List<SellerInformationData> getsellersDetails(String productCode, String productCatType) throws EtailNonBusinessExceptions;

	/**
	 * @param productModel
	 * @param buyboxid
	 */
	public RichAttributeData getRichAttributeDetails(ProductModel productModel, String buyboxid);

	public BuyBoxModel getpriceForUssid(String ussid);

	public BuyBoxData buyboxForSizeGuide(final String productCode, final String sellerId) throws EtailNonBusinessExceptions;

	public boolean isCatLingerie(final List<CategoryModel> categoryList, final String configLingerieCategoris)
			throws EtailNonBusinessExceptions;

	//TPR-3736
	public Map<String, List<Double>> getBuyBoxDataForUssids(final String ussidList) throws EtailNonBusinessExceptions;
	
	//CKD:TPR-3809
	/**
	 * @param selectedUSSID
	 * @return
	 */
	public String findPussid(String selectedUSSID);


}

