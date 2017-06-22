/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.pancard;

import de.hybris.platform.core.model.PancardInformationModel;

/**
 * @author TCS
 *
 */
public interface MplPancardDao
{
	public PancardInformationModel getPanCardOrderId(String orderreferancenumber);
}
