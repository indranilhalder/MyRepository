/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;


/**
 * @author Ashish Vyas
 *
 */
public interface MplCMSComponentService
{
	public AbstractCMSComponentModel getPagewiseComponent(String pageId, String componentId);

}
