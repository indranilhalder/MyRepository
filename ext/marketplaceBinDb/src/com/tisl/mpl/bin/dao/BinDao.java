/**
 *
 */
package com.tisl.mpl.bin.dao;

import de.hybris.platform.core.model.user.CustomerModel;

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

	//TPR-7486
	public String fetchBankFromCustomerSavedCard(final String cardRefNum, final CustomerModel customer);

}
