/**
 *
 */
package com.tisl.mpl.facade.msd.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.msd.MSDWidgetFacade;
import com.tisl.mpl.facades.data.MSDRequestdata;
import com.tisl.mpl.facades.data.MSDResponsedata;
import com.tisl.mpl.service.MSDService;





/**
 * @author TCS
 *
 */

public class MSDWidgetFacadeImpl implements MSDWidgetFacade
{

	@Autowired
	private MSDService msdService;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.msd.MSDWidgetFacade#getMSDWidgetFinalData(java.util.Map)
	 */

	@Override
	public MSDResponsedata getMSDWidgetFinalData(final MSDRequestdata msdRequest) throws EtailNonBusinessExceptions
	{
		return msdService.checkMSDServiceResponse(msdRequest);
	}
}
