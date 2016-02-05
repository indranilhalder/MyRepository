/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import java.util.Map;


/**
 * @author TCS
 *
 */
public interface InternalExternalAutomationService
{
	public Map<String, String> automationGetAllBanner();

	public boolean createCSVExcel(Map<String, String> exportMap);
}
