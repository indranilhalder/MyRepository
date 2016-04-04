/**
 *
 */
package com.tisl.mpl.bin.facade;

import com.tisl.mpl.binDb.model.BinModel;
import com.tisl.mpl.data.BinData;
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
	BinModel performBinCheck(final String bin) throws EtailNonBusinessExceptions;

	BinData binCheck(final String binNumber) throws EtailNonBusinessExceptions;
}
