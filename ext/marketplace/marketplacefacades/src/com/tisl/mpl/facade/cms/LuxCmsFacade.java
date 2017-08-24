/**
 *
 */
package com.tisl.mpl.facade.cms;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;

import com.tisl.mpl.wsdto.LuxuryComponentsListWsDTO;




public interface LuxCmsFacade
{

	LuxuryComponentsListWsDTO getLuxuryHomePage() throws CMSItemNotFoundException;

}
