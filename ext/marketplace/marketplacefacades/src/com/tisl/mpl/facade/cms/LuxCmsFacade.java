/**
 *
 */
package com.tisl.mpl.facade.cms;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;

import com.tisl.mpl.wsdto.LuxuryComponentsListWsDTO;


public interface LuxCmsFacade
{

	LuxuryComponentsListWsDTO getLuxuryPage(ContentPageModel contentPage) throws CMSItemNotFoundException;

	LuxuryComponentsListWsDTO getBrandById(String brandCode);

	LuxuryComponentsListWsDTO getContentPagesBylableOrId(String label);

	LuxuryComponentsListWsDTO getLuxHomepage();

}
