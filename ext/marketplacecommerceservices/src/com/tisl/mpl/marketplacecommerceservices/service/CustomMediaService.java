/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;


/**
 * @author TCS
 *
 */
public interface CustomMediaService extends MediaService
{
	/**
	 * @param code
	 * @return MediaModel
	 */
	MediaModel getMediaByCode(String code);
}