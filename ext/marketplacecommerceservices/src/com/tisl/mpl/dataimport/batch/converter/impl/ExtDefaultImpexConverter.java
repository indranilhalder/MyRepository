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

import de.hybris.platform.acceleratorservices.dataimport.batch.converter.impl.DefaultImpexConverter;
import de.hybris.platform.servicelayer.exceptions.SystemException;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.tisl.mpl.constants.MplConstants;


/**
 * @author TCS
 *
 */

public class ExtDefaultImpexConverter extends DefaultImpexConverter
{

	private String impexRow;

	/**
	 * @param impexRow
	 *           the impexRow to set
	 */
	@Override
	@Required
	public void setImpexRow(final String impexRow)
	{
		Assert.hasText(impexRow);
		this.impexRow = impexRow;
	}

	/**
	 * Converts a CSV row to impex.
	 *
	 * @Javadoc
	 * @param row
	 *           a CSV row containing column indexes and values
	 * @param sequenceId
	 * @return a converted impex line
	 */

	@Override
	public String convert(final Map<Integer, String> row, final Long sequenceId)
	{
		String result = MplConstants.EMPTY_STRING;
		if (!MapUtils.isEmpty(row))
		{
			final StringBuilder builder = new StringBuilder();
			int copyIdx = 0;
			int idx = impexRow.indexOf(MplConstants.BRACKET_START);
			while (idx > -1)
			{
				final int endIdx = impexRow.indexOf(MplConstants.BRACKET_END, idx);
				if (endIdx < 0)
				{
					throw new SystemException(MplConstants.MESSAGE_ROW_SYNTAX_BRACKETS + impexRow);
				}
				builder.append(impexRow.substring(copyIdx, idx));
				if (impexRow.charAt(idx + 1) == MplConstants.SEQUENCE_CHAR)
				{
					builder.append(sequenceId);
				}
				else
				{
					final boolean mandatory = impexRow.charAt(idx + 1) == MplConstants.PLUS_CHAR;
					Integer mapIdx = null;
					try
					{
						mapIdx = Integer.valueOf(impexRow.substring(mandatory ? idx + 2 : idx + 1, endIdx));
					}
					catch (final NumberFormatException e)
					{
						throw new SystemException(MplConstants.MESSAGE_ROW_SYNTAX_COLUMNS + impexRow, e);
					}
					final String colValue = row.get(mapIdx);
					if (mandatory && StringUtils.isBlank(colValue))
					{
						throw new IllegalArgumentException(MplConstants.MESSAGE_MISSING_VALUE + mapIdx);
					}
					//Checking the Row value for Price Row
					if (Double.parseDouble(row.get(MplConstants.PRICE_COLUMN)) <= 0)
					{
						//Less than zero value for Price Row triggers Exception
						throw new IllegalArgumentException(MplConstants.MESSAGE_PRICE_ZERO + mapIdx);
					}


					if (colValue != null)
					{
						builder.append(colValue);
					}
				}
				copyIdx = endIdx + 1;
				idx = impexRow.indexOf(MplConstants.BRACKET_START, endIdx);
			}
			if (copyIdx < impexRow.length())
			{
				builder.append(impexRow.substring(copyIdx));
			}
			result = builder.toString();
		}
		return escapeQuotes(result);
	}


}
