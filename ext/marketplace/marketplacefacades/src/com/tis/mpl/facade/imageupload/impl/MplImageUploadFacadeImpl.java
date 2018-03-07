package com.tis.mpl.facade.imageupload.impl;

import de.hybris.platform.core.model.media.MediaFolderModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.tis.mpl.facade.imageupload.MplImageUploadFacade;
import com.tisl.mpl.marketplacecommerceservices.service.ImageUploadService;


/**
 * @author Bhawana Purswani
 *
 */
public class MplImageUploadFacadeImpl implements MplImageUploadFacade
{

	@Resource
	ImageUploadService imageUploadService;

	public ImageUploadService getImageUploadService()
	{
		return imageUploadService;
	}

	public void setImageUploadService(final ImageUploadService imageUploadService)
	{
		this.imageUploadService = imageUploadService;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tis.mpl.facade.imageupload.MplImageUploadFacade#getMediaFolders()
	 */
	@Override
	public List<MediaFolderModel> getMediaFolders()
	{
		List<MediaFolderModel> mediaFolderDataList = new ArrayList<MediaFolderModel>();
		mediaFolderDataList = imageUploadService.getMediaFolderList();

		return mediaFolderDataList;
	}



}