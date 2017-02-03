/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.core.model.PancardInformationModel;

import javax.xml.bind.JAXBException;

import com.tisl.mpl.xml.pojo.CRMpancardResponse;


/**
 * @author TCS
 *
 */
public interface MplPancardTicketCRMservice
{



	/**
	 * @param orderreferancenumber
	 * @param newpath
	 * @param customername
	 * @param pancardnumber
	 * @return
	 */
	public CRMpancardResponse ticketPancardModeltoDTO(PancardInformationModel oModel) throws JAXBException;

	public void createticketPancardModeltoDTO(String orderreferancenumber, String newpath, String customername,
			String pancardnumber) throws JAXBException;
}
