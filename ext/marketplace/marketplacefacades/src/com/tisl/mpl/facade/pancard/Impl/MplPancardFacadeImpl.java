/**
 *
 */
package com.tisl.mpl.facade.pancard.Impl;

import de.hybris.platform.core.model.PancardInformationModel;

import javax.annotation.Resource;

import org.springframework.web.multipart.MultipartFile;

import com.tisl.mpl.facade.pancard.MplPancardFacade;
import com.tisl.mpl.marketplacecommerceservices.services.pancard.MplPancardService;


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
	 * @see com.tisl.mpl.facade.pancard.MplPancardFacade#setPancardFileAndOrderId(java.lang.String,
	 * org.springframework.web.multipart.MultipartFile)
	 */
	/*
	 * @Override public void setPancardFileAndOrderId(final String orderreferancenumber, final MultipartFile file) {
	 * mplPancardService.setPancardFileAndOrderIdservice(orderreferancenumber, file);
	 *
	 * }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.pancard.MplPancardFacade#getPanCardOredrId(java.lang.String)
	 */
	@Override
	public PancardInformationModel getPanCardOredrId(final String orderreferancenumber)
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
	public void refreshPancardDetailsFacade(final PancardInformationModel oModel, final MultipartFile file)
	{
		// YTODO Auto-generated method stub
		mplPancardService.refreshPancardDetailsService(oModel, file);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.pancard.MplPancardFacade#setPancardFileAndOrderId(java.lang.String, java.lang.String,
	 * java.lang.String, org.springframework.web.multipart.MultipartFile)
	 */
	@Override
	public void setPancardFileAndOrderId(final String orderreferancenumber, final String customername, final String pancardnumber,
			final MultipartFile file)
	{
		// YTODO Auto-generated method stub
		mplPancardService.setPancardFileAndOrderIdservice(orderreferancenumber, customername, pancardnumber, file);
	}




}
