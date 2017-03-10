/**
 *
 */
package com.tils.mpl.media;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;


/**
 * @author TCS
 *
 */
public interface MplMediaDao
{

	List<MediaModel> findMediaForQualifier(MediaContainerModel container, String mediaFormatList);

	/**
	 * @param product
	 * @param mediaFormat
	 * @return
	 */
	public MediaModel getMediaForIndexing(final ProductModel product, final MediaFormatModel mediaFormat, final String productCode);

}
