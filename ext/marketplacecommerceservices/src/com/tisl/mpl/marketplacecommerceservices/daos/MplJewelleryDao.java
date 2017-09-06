/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.JewellerySellerDetailsModel;

import java.util.List;

import com.tisl.mpl.core.model.BuyBoxModel;


/**
 * @author TCS
 *
 */
public interface MplJewelleryDao
{

	/**
	 * @param productCode
	 * @return
	 */
	List<JewelleryInformationModel> getJewelleryUssid(String productCode);


	/**
	 * @param ussid
	 * @return
	 */
	List<JewelleryInformationModel> getJewelleryInfoByUssid(String ussid);


	/**
	 * @param ussid
	 * @return
	 */
	List<String> getWeightVarientUssid(String ussid);


	/**
	 * @param orderId
	 * @return
	 */
	String getPanCardStatus(String orderLineId);


	/**
	 * @param ussid
	 * @return
	 */
	List<BuyBoxModel> getAllWeightVariant(String ussid);

	List<JewellerySellerDetailsModel> getSellerMsgForRetRefTab(String sellerId);

}
