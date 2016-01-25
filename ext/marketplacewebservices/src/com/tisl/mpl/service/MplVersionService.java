/**
 *
 */
package com.tisl.mpl.service;

import com.tisl.mpl.wsdto.VersionListResponseData;


/**
 * @author TCS
 *
 */
public interface MplVersionService
{

	/**
	 * Mobile caching - sending modified time for cacheable components
	 *
	 * @return VersionListResponseData
	 */
	public VersionListResponseData getVersionResponseDetails();

}
