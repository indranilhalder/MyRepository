/**
 *
 */
package com.tisl.mpl.bin.service;

import com.tisl.mpl.binDb.model.BinModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
public interface BinService
{
	/**
	 * This method sends the bin to call DB to fetch details related to Bin
	 *
	 * @param bin
	 * @return BinModel
	 * @throws EtailNonBusinessExceptions
	 */
	public BinModel checkBin(final String bin) throws EtailNonBusinessExceptions;
}
