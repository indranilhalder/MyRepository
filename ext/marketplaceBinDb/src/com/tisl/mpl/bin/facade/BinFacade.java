/**
 *
 */
package com.tisl.mpl.bin.facade;

import com.tisl.mpl.binDb.model.BinModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
public interface BinFacade
{
	/**
	 * This method takes the bin from Controller and calls the service to check bin details
	 *
	 * @param bin
	 * @return BinModel
	 * @throws EtailNonBusinessExceptions
	 */
	public BinModel performBinCheck(final String bin) throws EtailNonBusinessExceptions;
}
