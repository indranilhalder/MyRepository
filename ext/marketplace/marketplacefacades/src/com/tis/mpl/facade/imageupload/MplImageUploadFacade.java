/**
 *
 */
package com.tis.mpl.facade.imageupload;

import de.hybris.platform.core.model.media.MediaFolderModel;

import java.util.List;


/**
 * @author Bhawana Purswani
 *
 */
public interface MplImageUploadFacade
{

	public List<MediaFolderModel> getMediaFolders();

}