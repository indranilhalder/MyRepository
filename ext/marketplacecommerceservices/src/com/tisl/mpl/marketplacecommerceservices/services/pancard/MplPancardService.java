/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.services.pancard;

import de.hybris.platform.core.model.PancardInformationModel;

import org.springframework.web.multipart.MultipartFile;


/**
 * @author TCS
 *
 */
public interface MplPancardService
{
	//public void setPancardFileAndOrderIdservice(String orderreferancenumber, MultipartFile file);

	public PancardInformationModel getPanCardOrderId(String orderreferancenumber);

	public void refreshPancardDetailsService(PancardInformationModel oModel, MultipartFile file, String pancardnumber);

	public void setPancardFileAndOrderIdservice(final String orderreferancenumber, String transactionid, String customername,
			String pancardnumber, MultipartFile file);

	/**
	 * @param oModel
	 * @return
	 */
	public String getCrmStatusForPancardDetailsService(PancardInformationModel oModel);

}
