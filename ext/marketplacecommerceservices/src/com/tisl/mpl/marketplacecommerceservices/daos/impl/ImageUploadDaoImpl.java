package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.daos.ImageUploadDao;


/**
 * @author Bhawana Purswani
 *
 */
public class ImageUploadDaoImpl extends AbstractItemDao implements ImageUploadDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<MediaFolderModel> getAllMediaFolders()
	{
		List<MediaFolderModel> mediaFolderList = new ArrayList();
		String queryString = StringUtils.EMPTY;
		try
		{
			queryString = "select {pk} from {" + MediaFolderModel._TYPECODE + "}";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			mediaFolderList = flexibleSearchService.<MediaFolderModel> search(query).getResult();

		}
		catch (final Exception e)
		{
			e.getMessage();
		}
		return mediaFolderList;

	}

}

/*
 * (non-Javadoc)
 *
 * @see com.tisl.mpl.marketplacecommerceservices.daos.ImageUploadDao#getAllMediaFolders()
 */