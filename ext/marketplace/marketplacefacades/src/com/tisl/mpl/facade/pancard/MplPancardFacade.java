/**
 *
 */
package com.tisl.mpl.facade.pancard;

import de.hybris.platform.core.model.PancardInformationModel;

import org.springframework.web.multipart.MultipartFile;


/**
 * @author TCS
 *
 */
public interface MplPancardFacade
{
	//public void setPancardFileAndOrderId(String orderreferancenumber, MultipartFile file);

	public PancardInformationModel getPanCardOredrId(String orderreferancenumber);

	public void refreshPancardDetailsFacade(PancardInformationModel oModel, MultipartFile file);

	/**
	 * @param orderreferancenumber
	 * @param customername
	 * @param pancardnumber
	 * @param file
	 */
	public void setPancardFileAndOrderId(String orderreferancenumber, String customername, String pancardnumber, MultipartFile file);
}
