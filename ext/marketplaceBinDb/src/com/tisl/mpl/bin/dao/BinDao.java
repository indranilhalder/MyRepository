/**
 *
 */
package com.tisl.mpl.bin.dao;

import java.util.List;

import com.tisl.mpl.binDb.model.BinModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
public interface BinDao
{
	/**
	 * This method fetches the details wrt a bin
	 *
	 * @param bin
	 * @return BinModel
	 * @throws EtailNonBusinessExceptions
	 */
	public BinModel fetchBankFromBin(final String bin) throws EtailNonBusinessExceptions;

	/**
	 * This method fetches bank names in Bin but not in Bank
	 *
	 * @return: List<String>
	 */
	public List<String> getBankDetails() throws EtailNonBusinessExceptions;
}
