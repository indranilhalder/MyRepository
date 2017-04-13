/**
 *
 */
package com.tis.mpl.facade.shorturl;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.tisl.mpl.core.model.OrderShortUrlInfoModel;
import com.tisl.mpl.shorturl.service.ShortUrlService;


/**
 * @author prabhakar
 *
 */
public class DefaultShortUrlTrackFacade implements ShortUrlTrackFacade
{

	@Resource(name = "shortUrlConverter")
	private Converter<OrderShortUrlInfoModel, ShortUrlReportData> shortUrlConverter;

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
		final List<OrderShortUrlInfoModel> listOfShortUrls = googleShortUrlService.getShortUrlReportModels(fromDate, toDate);
		return Converters.convertAll(listOfShortUrls, shortUrlConverter);

	}


	/**
	 * @param shortUrlConverter
	 *           the shortUrlConverter to set
	 */
	public void setShortUrlConverter(final Converter<OrderShortUrlInfoModel, ShortUrlReportData> shortUrlConverter)
	{
		this.shortUrlConverter = shortUrlConverter;
	}

}
