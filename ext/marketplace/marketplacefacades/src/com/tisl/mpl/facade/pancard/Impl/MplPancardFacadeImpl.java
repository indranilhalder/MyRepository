/**
 *
 */
package com.tisl.mpl.facade.pancard.Impl;

import de.hybris.platform.core.model.PancardInformationModel;
import de.hybris.platform.core.model.order.OrderModel;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.multipart.MultipartFile;

import com.tisl.mpl.facade.pancard.MplPancardFacade;
import com.tisl.mpl.marketplacecommerceservices.services.pancard.MplPancardService;
import com.tisl.mpl.pojo.PanCardResDTO;


/**
 * @author TCS
 *
 */
public class MplPancardFacadeImpl implements MplPancardFacade
{
	@Resource(name = "mplPancardServiceImpl")
	private MplPancardService mplPancardService;

	/**
	 * @return the mplPancardService
	 */
	public MplPancardService getMplPancardService()
	{
		return mplPancardService;
	}

	/**
	 * @param mplPancardService
	 *           the mplPancardService to set
	 */
	public void setMplPancardService(final MplPancardService mplPancardService)
	{
		this.mplPancardService = mplPancardService;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.pancard.MplPancardFacade#getPanCardOredrId(java.lang.String)
	 */
	@Override
	public List<PancardInformationModel> getPanCardOredrId(final String orderreferancenumber)
	{
		// YTODO Auto-generated method stub

		return mplPancardService.getPanCardOrderId(orderreferancenumber);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.pancard.MplPancardFacade#refreshPancardDetailsFacade(de.hybris.platform.core.model.
	 * PancardInformationModel, org.springframework.web.multipart.MultipartFile)
	 */
	@Override
	public void refreshPancardDetailsFacade(final PancardInformationModel oModel, final MultipartFile file,
			final String pancardnumber)
	{
		// YTODO Auto-generated method stub
		mplPancardService.refreshPancardDetailsService(oModel, file, pancardnumber);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.pancard.MplPancardFacade#setPancardFileAndOrderId(java.lang.String, java.lang.String,
	 * java.lang.String, org.springframework.web.multipart.MultipartFile)
	 */
	@Override
	public void setPancardFileAndOrderId(final String orderreferancenumber, final String transactionid, final String customername,
			final String pancardnumber, final MultipartFile file)
	{
		// YTODO Auto-generated method stub
		mplPancardService.setPancardFileAndOrderIdservice(orderreferancenumber, transactionid, customername, pancardnumber, file);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.facade.pancard.MplPancardFacade#getCrmStatusForPancardDetailsFacade(de.hybris.platform.core.model
	 * .PancardInformationModel)
	 */
	@Override
	public String getCrmStatusForPancardDetailsFacade(final PancardInformationModel oModel)
	{
		// YTODO Auto-generated method stub
		return mplPancardService.getCrmStatusForPancardDetailsService(oModel);

	}


	//For sending pancard details to SP through PI and save data into database for new pancard entry
	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.pancard.MplPancardFacade#setPanCardDetailsAndPIcall(java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, org.springframework.web.multipart.MultipartFile)
	 */

	@Override
	public void setPanCardDetailsAndPIcall(final String orderreferancenumber, final List<String> transactionidList,
			final String customername, final String pancardnumber, final MultipartFile file)
	{
		mplPancardService.setPanCardDetailsAndPIcall(orderreferancenumber, transactionidList, customername, pancardnumber, file);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.pancard.MplPancardFacade#refreshPanCardDetailsAndPIcall(de.hybris.platform.core.model.
	 * PancardInformationModel, java.lang.String, org.springframework.web.multipart.MultipartFile)
	 */
	@Override
	public void refreshPanCardDetailsAndPIcall(final List<PancardInformationModel> pModelList,
			final PancardInformationModel pModel, final String pancardnumber, final MultipartFile file)
	{
		// YTODO Auto-generated method stub
		mplPancardService.refreshPanCardDetailsAndPIcall(pModelList, pModel, pancardnumber, file);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.pancard.MplPancardFacade#getOrderForCode(java.lang.String)
	 */
	@Override
	public List<OrderModel> getOrderForCode(final String orderreferancenumber)
	{
		// YTODO Auto-generated method stub
		return mplPancardService.getOrderForCode(orderreferancenumber);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.pancard.MplPancardFacade#setPancardRes(com.tisl.mpl.pojo.PanCardResDTO)
	 */
	@Override
	public void setPancardRes(final PanCardResDTO resDTO)
	{
		// YTODO Auto-generated method stub
		mplPancardService.setPancardRes(resDTO);
	}

}
