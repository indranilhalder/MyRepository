/**
 *
 */
package com.tisl.mpl.bin.facade.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.tisl.mpl.bin.constants.MarketplaceBinDbConstants;
import com.tisl.mpl.bin.facade.BinFacade;
import com.tisl.mpl.bin.service.BinService;
import com.tisl.mpl.binDb.model.BinModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
@Component
@Qualifier(MarketplaceBinDbConstants.BINFACADE)
public class BinFacadeImpl implements BinFacade
{
	@Resource
	private BinService binService;

	/**
	 * This method takes the bin from Controller and calls the service to check bin details
	 *
	 * @param bin
	 * @return BinModel
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public BinModel performBinCheck(final String bin) throws EtailNonBusinessExceptions
	{
		final BinModel binModel = getBinService().checkBin(bin);

		return binModel;
	}

	/**
	 * @return the binService
	 */
	public BinService getBinService()
	{
		return binService;
	}

	/**
	 * @param binService
	 *           the binService to set
	 */
	public void setBinService(final BinService binService)
	{
		this.binService = binService;
	}

}
