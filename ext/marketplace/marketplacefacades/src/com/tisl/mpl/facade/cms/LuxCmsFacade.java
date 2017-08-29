/**
 *
 */
package com.tisl.mpl.facade.cms;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;

import com.tisl.mpl.wsdto.LuxuryComponentsListWsDTO;
import de.hybris.platform.cms2.model.pages.ContentPageModel;


public interface LuxCmsFacade
{

	LuxuryComponentsListWsDTO getLuxuryPage(ContentPageModel contentPage) throws CMSItemNotFoundException;

}
