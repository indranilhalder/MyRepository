/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.core.model.PancardInformationModel;

import java.util.List;

import javax.xml.bind.JAXBException;


/**
 * @author TCS
 *
 */
public interface MplPancardUploadService
{

	//For sending pancard details to SP through PI and save data into database for new pancard entry
	public String generateXmlForPanCard(List<PancardInformationModel> pModelList, String orderreferancenumber,
			List<String> transactionidList, String panCardImagePath) throws JAXBException;
}
