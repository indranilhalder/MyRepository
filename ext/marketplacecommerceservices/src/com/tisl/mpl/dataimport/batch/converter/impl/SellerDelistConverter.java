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
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
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

		/*
		 * //Calling Delisting Processor to delist the Seller final boolean processed =
		 * mplDelistingProcessor.processDatatoModelSeller(row);
		 *
		 * //Inserting boolean Value using hot Folder if (processed) { //true->1 row.put(ISPROCESSEDCOLOUMN, "1"); } else
		 * { //false->0 row.put(ISPROCESSEDCOLOUMN, "0"); }
		 */
		/*
		 * final StringBuilder builder = new StringBuilder(); int copyIdx = 0; int idx = impexRow.indexOf(BRACKET_START);
		 * while (idx > -1) { final int endIdx = impexRow.indexOf(BRACKET_END, idx); if (endIdx < 0) { throw new
		 * SystemException("Invalid row syntax [brackets not closed]: " + impexRow); }
		 * builder.append(impexRow.substring(copyIdx, idx)); if (impexRow.charAt(idx + 1) == SEQUENCE_CHAR) {
		 * builder.append(sequenceId); } else { final boolean mandatory = impexRow.charAt(idx + 1) == PLUS_CHAR; Integer
		 * mapIdx = null; try { mapIdx = Integer.valueOf(impexRow.substring(mandatory ? idx + 2 : idx + 1, endIdx)); }
		 * catch (final NumberFormatException e) { throw new
		 * SystemException("Invalid row syntax [invalid column number]: " + impexRow, e); } final String colValue =
		 * row.get(mapIdx); if (mandatory && StringUtils.isBlank(colValue)) { throw new
		 * IllegalArgumentException("Missing value for " + mapIdx); } if (colValue != null) { builder.append(colValue); }
		 * } copyIdx = endIdx + 1; idx = impexRow.indexOf(BRACKET_START, endIdx); } if (copyIdx < impexRow.length()) {
		 * builder.append(impexRow.substring(copyIdx)); } result = builder.toString(); } return escapeQuotes(result); }
		 */


		//CAR-292
		//INSERT_UPDATE MarketplaceDelist;sellerID[unique=true];DelistingStatus;isBlockOMS;isProcessed


		String result = EMPTY_STRING;
		String sellerAssociationStatus = SellerAssociationStatusEnum.YES.getCode();
		final StringBuffer sBuffer = new StringBuffer();
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

		result = sBuffer.toString();
		return result;
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

}
