/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import java.util.Date;

import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author 682160
 *
 */
public interface MplThirdPartyWalletService
{
	MplConfigurationModel getCronDetails(String code);


	void fetchThirdPartyAuditTableData();

	/**
	 * @param startTime
	 * @param code
	 */
	void saveCronDetails(Date startTime, String code);

	/**
	 * @return
	 */
	Double getmRupeeJobTAT();
}
