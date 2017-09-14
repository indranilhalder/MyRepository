/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.JewellerySellerDetailsModel;
import de.hybris.platform.core.model.JwlryRevSealInfoModel;

import java.util.List;

import com.tisl.mpl.core.model.BuyBoxModel;


/**
 * @author TCS
 *
 */
public interface MplJewelleryService
{

	/**
	 * @param productCode
	 * @return
	 */
	public List<JewelleryInformationModel> getJewelleryUssid(String productCode);

	/**
	 * @param ussid
	 * @return
	 */
	public List<JewelleryInformationModel> getJewelleryInfoByUssid(String ussid);

	/**
	 * @param ussid
	 * @return
	 */
	public List<String> getWeightVarientUssid(String ussid);

	/**
	 * @param code
	 * @return
	 */
	public String getPanCardStatus(String orderLineId);

	/**
	 * @param ussid
	 * @return
	 */
	List<BuyBoxModel> getAllWeightVariant(String ussid);

	List<JewellerySellerDetailsModel> getSellerMsgForRetRefTab(String sellerId);

	/**
	 * @param sellerId
	 */
	public JwlryRevSealInfoModel getSealInfo(String sellerId);

}
