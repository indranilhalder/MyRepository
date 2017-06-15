/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.dataimport.batch.converter.impl;

import de.hybris.platform.acceleratorservices.dataimport.batch.converter.ImpexConverter;
import de.hybris.platform.acceleratorservices.dataimport.batch.converter.impl.DefaultImpexConverter;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.cronjobs.delisting.DelistingProcessorImpl;
import com.tisl.mpl.enums.SellerAssociationStatusEnum;
import com.tisl.mpl.marketplacecommerceservices.service.MplDelistingService;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * Default implementation of {@link ImpexConverter}.
 */
public class SellerDelistConverter extends DefaultImpexConverter
{


	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private MplDelistingService mplDelistingService;


	/**
	 * @return the mplDelistingService
	 */
	public MplDelistingService getMplDelistingService()
	{
		return mplDelistingService;
	}

	/**
	 * @param mplDelistingService
	 *           the mplDelistingService to set
	 */
	public void setMplDelistingService(final MplDelistingService mplDelistingService)
	{
		this.mplDelistingService = mplDelistingService;
	}

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DelistingProcessorImpl.class.getName());



	private static final char SEMICOLON_CHAR = ';';

	private static final String EMPTY_STRING = "";



	@Override
	public String convert(final Map<Integer, String> row, final Long sequenceId)
	{

		//CAR-292
		//INSERT_UPDATE MarketplaceDelist;sellerID[unique=true];DelistingStatus;isBlockOMS;isProcessed
		String result = EMPTY_STRING;
		final StringBuffer sBuffer = new StringBuffer();


		String sellerAssociationStatus = SellerAssociationStatusEnum.YES.getCode();

		if (!MapUtils.isEmpty(row) && row.get(Integer.valueOf(0)) != null)
		{
			final List<SellerInformationModel> ussidList = getMplDelistingService()
					.getAllUSSIDforSeller(row.get(Integer.valueOf(0)));

			if ("Y".equalsIgnoreCase(row.get(Integer.valueOf(1))))
			{
				sellerAssociationStatus = "no";
			}


			for (final SellerInformationModel sim : ussidList)
			{

				sBuffer.append(SEMICOLON_CHAR).append(row.get(Integer.valueOf(0))).append(SEMICOLON_CHAR).append(sim.getUSSID())
						.append(SEMICOLON_CHAR).append(sellerAssociationStatus).append(SEMICOLON_CHAR)
						.append(row.get(Integer.valueOf(2))).append(SEMICOLON_CHAR).append(defferedDate()).append("\n");

			}

		}
		else
		{
			throw new IllegalArgumentException("Missing value for  SellerID");
		}


		result = sBuffer.toString();
		return escapeQuotes(result);
	}

	private String defferedDate()
	{
		final int defferedTime = configurationService.getConfiguration().getInt("etail.delist.date.deffered", 15);

		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, defferedTime);

		final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		final String strDate = formatter.format(cal.getTime());

		return strDate;

	}


	@Override
	protected String escapeQuotes(final String input)
	{
		final String[] splitedInput = StringUtils.splitPreserveAllTokens(input, SEMICOLON_CHAR);
		final List<String> tmp = new ArrayList<String>();
		for (final String string : splitedInput)
		{
			if (doesNotContainNewLine(string))
			{
				tmp.add(StringEscapeUtils.escapeCsv(string));
			}
			else
			{
				tmp.add(string);
			}
		}
		return StringUtils.join(tmp, SEMICOLON_CHAR);
	}

}
