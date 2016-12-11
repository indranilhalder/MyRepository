/**
 *
 */
package com.hybris.oms.tata.services;

import org.zkoss.zk.ui.event.UploadEvent;


/**
 * @author Techouts
 *
 */
public interface LpAwbDataUploadService
{

	public void lpAwbBulkUploadCommon(UploadEvent uploadEvent, String transactionType,String userId,String roleId);

}
