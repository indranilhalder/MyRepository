/**
 *
 */
package com.tils.mpl.media.impl;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tils.mpl.media.MplMediaDao;
import com.tils.mpl.media.MplMediaService;


/**
 * @author TCS
 *
 */
public class MplMediaServiceImpl implements MplMediaService
{

	@Autowired
	private MplMediaDao mediaDao;

	/*
	 * @Javadoc Method to Optimize Image load in PDP.Single Db Call to Populate Different Image Format
	 *
	 * @param MediaContainerModel container , String mediaFormatList
	 *
	 * @return List<MediaModel>
	 */

	@Override
	public List<MediaModel> findMediaForQualifier(final MediaContainerModel container, final String mediaFormatList)
	{
		return mediaDao.findMediaForQualifier(container, mediaFormatList);
	}

	@Override
	public MediaModel getMediaForIndexing(final ProductModel product, final MediaFormatModel mediaFormat, final String productCode)
	{
		return mediaDao.getMediaForIndexing(product, mediaFormat, productCode);
	}
}
