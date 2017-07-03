/**
 *
 */
package com.tisl.mpl.facade.pancard;

import de.hybris.platform.core.model.PancardInformationModel;
import de.hybris.platform.core.model.order.OrderModel;

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

	public void refreshPancardDetailsFacade(PancardInformationModel oModel, MultipartFile file, String pancardnumber);

	/**
	 * @param orderreferancenumber
	 * @param customername
	 * @param pancardnumber
	 * @param file
	 */
	public void setPancardFileAndOrderId(String orderreferancenumber, String transactionid, String customername,
			String pancardnumber, MultipartFile file);

	/**
	 * @param oModel
	 * @return
	 */
	public String getCrmStatusForPancardDetailsFacade(PancardInformationModel oModel);

	//For sending pancard details to SP through PI and save data into database for new pancard entry
	public void setPanCardDetailsAndPIcall(String orderreferancenumber, List<String> transactionidList, String customername,
			String pancardnumber, MultipartFile file);

	public void refreshPanCardDetailsAndPIcall(List<PancardInformationModel> pModelList, PancardInformationModel pModel,
			String pancardnumber, MultipartFile file);

	public List<OrderModel> getOrderForCode(String orderreferancenumber);

	public void setPancardRes(final PanCardResDTO resDTO) throws JAXBException;
}
