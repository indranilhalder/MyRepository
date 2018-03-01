/**
 *
 */
package com.tisl.mpl.core.mplqcprogram.dao;

import com.tisl.mpl.core.model.MplQCProgramConfigModel;
import com.tisl.mpl.core.model.WalletCardApportionDetailModel;


/**
 * @author Tech
 *
 */
public interface MplQCProgramDao
{
	public MplQCProgramConfigModel getProgramIdConfigValueById(final String id);

	public WalletCardApportionDetailModel getCardTotalAmount(final String cardNumber);
}
