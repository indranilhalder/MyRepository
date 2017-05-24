/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.store.BaseStoreModel;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author 682160
 *
 */
public interface MplThirdPartyWalletDao
{
	MplConfigurationModel getCronDetails(String code);

	/**
	 * @return
	 */
	BaseStoreModel getJobTAT();

	List<MplPaymentAuditModel> fetchSpecificAuditTableData(Date mplConfigDate, Date startTime);
}
