/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import java.util.Date;
import java.util.Map;

import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public interface GigyaDataPullDao
{
	public MplConfigurationModel fetchConfigDetails(String code);

	public Map<String, Date> fetchProductDetails();

	public Map<String, Date> specificProductDetails(Date lastFetchedDate);
}
