/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.daos.MplCMSComponentDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplCMSComponentService;


/**
 * @author Ashish Vyas
 *
 */
public class DefaultMplCMSComponentService implements MplCMSComponentService
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DefaultMplCMSComponentService.class.getName());

	@Autowired
	private MplCMSComponentDao mplCmsComponentDao;

	@Autowired
	protected CatalogService catalogService;


	/**
	 * @return the mplCmsComponentDao
	 */
	public MplCMSComponentDao getMplCmsComponentDao()
	{
		return mplCmsComponentDao;
	}


	/**
	 * @param mplCmsComponentDao
	 *           the mplCmsComponentDao to set
	 */
	public void setMplCmsComponentDao(final MplCMSComponentDao mplCmsComponentDao)
	{
		this.mplCmsComponentDao = mplCmsComponentDao;
	}




	/**
	 * @return the catalogService
	 */
	public CatalogService getCatalogService()
	{
		return catalogService;
	}




	/**
	 * @param catalogService
	 *           the catalogService to set
	 */
	public void setCatalogService(final CatalogService catalogService)
	{
		this.catalogService = catalogService;
	}




	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.CMSComponentService#getPagewiseComponent(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<AbstractCMSComponentModel> getPagewiseComponent(final String pageId, final String componentId)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("inside getPagewiseComponent()");
		}
		final List<AbstractCMSComponentModel> abstractCMSComponentModel = getMplCmsComponentDao().getPagewiseComponent(pageId,
				componentId, this.catalogService.getSessionCatalogVersions());
		return abstractCMSComponentModel;
	}




}
