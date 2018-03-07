
/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.media.MediaFolderModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.marketplacecommerceservices.daos.ImageUploadDao;
import com.tisl.mpl.marketplacecommerceservices.service.ImageUploadService;


/**
 * @author Bhawana Purswani
 *
 */
public class ImageUploadServiceImpl implements ImageUploadService
{

	private ImageUploadDao imageUploadDao;

	@Required
	public void setImageUploadDao(final ImageUploadDao imageUploadDao)
	{
		this.imageUploadDao = imageUploadDao;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.ImageUploadService#getMediaFolderList()
	 */
	@Override
	public List<MediaFolderModel> getMediaFolderList()
	{
		final List<MediaFolderModel> resultList = imageUploadDao.getAllMediaFolders();
		return resultList;
	}

}