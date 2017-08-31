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
	public String ticketPancardModeltoDTO(PancardInformationModel oModel, String newpath, String pancardnumber)
			throws JAXBException;

	public String createticketPancardModeltoDTO(String orderreferancenumber, String transactionid, String newpath,
			String pancardnumber) throws JAXBException;

	public String createCrmTicket(String ticket);

	/**
	 * @param oModel
	 * @return
	 */
	public CRMpancardResponse getCrmStatusForPancardDetails(PancardInformationModel oModel);


}
