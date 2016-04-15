/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.commercefacades.product.data.PincodeServiceData;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.PincodeServiceabilityDataModel;


/**
 * @author TCS
 *
 */
public interface MplPincodeServiceDao
{
	List<PincodeServiceabilityDataModel> getPincodeServicableDataAtCommerce(String pin,
			List<PincodeServiceData> pincodeServiceDataList);

	/**
	 * List of Data to invalidate
	 * 
	 * @param jobLastRunDate
	 * @param date
	 * @return List<PincodeServiceabilityDataModel>
	 */
	List<PincodeServiceabilityDataModel> getPincodeData(Date jobLastRunDate, Date date);
}
