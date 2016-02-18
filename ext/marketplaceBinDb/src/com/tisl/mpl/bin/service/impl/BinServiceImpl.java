/**
 *
 */
package com.tisl.mpl.bin.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.tisl.mpl.bin.constants.MarketplaceBinDbConstants;
import com.tisl.mpl.bin.dao.BinDao;
import com.tisl.mpl.bin.service.BinService;
import com.tisl.mpl.binDb.model.BinModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
@Component
@Qualifier(MarketplaceBinDbConstants.BINSERVICE)
public class BinServiceImpl implements BinService
{
	@Resource
	private BinDao binDao;

	/**
	 * This method sends the bin to call DB to fetch details related to Bin
	 *
	 * @param bin
	 * @return BinModel
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public BinModel checkBin(final String bin) throws EtailNonBusinessExceptions
	{
		final BinModel binModel = getBinDao().fetchBankFromBin(bin);

		return binModel;
	}

	/**
	 * @return the binDao
	 */
	public BinDao getBinDao()
	{
		return binDao;
	}

	/**
	 * @param binDao
	 *           the binDao to set
	 */
	public void setBinDao(final BinDao binDao)
	{
		this.binDao = binDao;
	}
}
