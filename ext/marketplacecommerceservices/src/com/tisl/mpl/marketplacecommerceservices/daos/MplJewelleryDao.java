/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.PancardInformationModel;

import java.util.List;


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
	List<PancardInformationModel> getPanCardStatus(String orderId);

}
