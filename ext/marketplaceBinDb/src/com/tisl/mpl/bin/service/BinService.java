/**
 *
 */
package com.tisl.mpl.bin.service;

import com.tisl.mpl.bindb.model.BinModel;
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
	//Added For TPR-1035
	public BinModel checkBin(final String bin, final String cardType, final String mplCustomerID, final boolean isErrorCreation)
			throws EtailNonBusinessExceptions;

	/**
	 * Generate CSV with Bank Details present in Bin but not in Bank
	 *
	 */
	public void generateFileData();
}
