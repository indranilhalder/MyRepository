/**
 *
 */
package com.tisl.mpl.facade.pancard;

import de.hybris.platform.core.model.PancardInformationModel;
import de.hybris.platform.core.model.order.OrderModel;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.springframework.web.multipart.MultipartFile;

import com.tisl.mpl.pojo.PanCardResDTO;


/**
 * @author TCS
 *
 */
public interface MplPancardFacade
{
	//public void setPancardFileAndOrderId(String orderreferancenumber, MultipartFile file);

	public List<PancardInformationModel> getPanCardOredrId(String orderreferancenumber);

	//For sending pancard details to SP through PI and save data into database for new pancard entry
	public void setPanCardDetailsAndPIcall(String orderreferancenumber, List<String> transactionidList, String customername,
			String pancardnumber, MultipartFile file) throws IllegalStateException, IOException, JAXBException;

	public void refreshPanCardDetailsAndPIcall(List<PancardInformationModel> pModelList, List<PancardInformationModel> pModel,
			String pancardnumber, MultipartFile file) throws IllegalStateException, IOException, JAXBException;

	public List<OrderModel> getOrderForCode(String orderreferancenumber);

	public void setPancardRes(final PanCardResDTO resDTO) throws JAXBException;
}
