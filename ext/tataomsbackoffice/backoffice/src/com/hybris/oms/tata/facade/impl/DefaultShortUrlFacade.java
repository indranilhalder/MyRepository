/**
 *
 */
package com.hybris.oms.tata.facade.impl;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.hybris.oms.tata.facade.ShortUrlFacade;
import com.techouts.backoffice.ShortUrlReportData;
import com.tisl.mpl.core.model.TULShortUrlReportModel;
import com.tisl.mpl.shorturl.service.ShortUrlService;


/**
 * @author prabhakar
 *
 */
public class DefaultShortUrlFacade implements ShortUrlFacade
{

	@Resource(name = "shortUrlConverter")
	private Converter<TULShortUrlReportModel, ShortUrlReportData> shortUrlConverter;

	@Resource(name = "googleShortUrlService")
	private ShortUrlService googleShortUrlService;

	/**
	 * @param googleShortUrlService
	 *           the googleShortUrlService to set
	 */
	public void setGoogleShortUrlService(final ShortUrlService googleShortUrlService)
	{
		this.googleShortUrlService = googleShortUrlService;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.techouts.backoffice.facade.ShortUrlFacade#getShortUrlReportModels(java.util.Date, java.util.Date)
	 */
	@Override
	public List<ShortUrlReportData> getShortUrlReportModels(final Date fromDate, final Date toDate)
	{
		final List<TULShortUrlReportModel> listOfShortUrls = googleShortUrlService.getShortUrlReportModels(fromDate, toDate);
		return Converters.convertAll(listOfShortUrls, shortUrlConverter);

	}


	/**
	 * @param shortUrlConverter
	 *           the shortUrlConverter to set
	 */
	public void setShortUrlConverter(final Converter<TULShortUrlReportModel, ShortUrlReportData> shortUrlConverter)
	{
		this.shortUrlConverter = shortUrlConverter;
	}

}
