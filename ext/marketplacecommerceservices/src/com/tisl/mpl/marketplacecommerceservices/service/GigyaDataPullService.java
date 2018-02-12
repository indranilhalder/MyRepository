/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;


import java.util.Date;
import java.util.Map;

import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public interface GigyaDataPullService
{
	public void saveCronDetails(Date endTime, String cronCode);

	public MplConfigurationModel getCronDetails(String cronCode);

	public Map<String, Date> fetchProductDetails();

	public Map<String, Date> specificProductDetails(Date lastFetchedDate);
}
