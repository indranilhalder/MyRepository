/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import java.util.List;

import com.tisl.mpl.data.InternalCampaignReportData;


/**
 * @author TCS
 *
 */
public interface InternalExternalAutomationService
{
	public List<InternalCampaignReportData> automationGetAllBanner();

	//public void automationGetAllBanner();

	public void createCSVExcel(List<InternalCampaignReportData> campaignDataConsolidatedList);
}
