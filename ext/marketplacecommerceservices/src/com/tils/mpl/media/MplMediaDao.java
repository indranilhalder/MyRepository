/**
 *
 */
package com.tils.mpl.media;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;

import java.util.List;


/**
 * @author TCS
 *
 */
public interface MplMediaDao
{

	List<MediaModel> findMediaForQualifier(MediaContainerModel container, String mediaFormatList);

}
