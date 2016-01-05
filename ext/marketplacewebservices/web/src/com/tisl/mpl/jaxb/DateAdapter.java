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
package com.tisl.mpl.jaxb;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;


/**
 * DateAdaper is used by JAXB to convert Dates to String and vice versa.
 */
@SuppressWarnings(
{ "PMD" })
public class DateAdapter extends XmlAdapter<String, Date>
{
	public final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

	//because SimpleDateFormat is not thread safe, it is used in conjunction with ThreadLocal
	private final ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>()
	{

		@Override
		public DateFormat get()
		{
			return super.get();
		}

		@Override
		protected DateFormat initialValue()
		{
			final SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
			return format;
		}

		@Override
		public void remove()
		{
			super.remove();
		}

		@Override
		public void set(final DateFormat value)
		{
			super.set(value);
		}

	};

	@Override
	public String marshal(final Date d)
	{
		if (d == null)
		{
			return null;
		}
		return dateFormat.get().format(d);
	}

	@Override
	public Date unmarshal(final String d) throws ParseException
	{
		if (d == null)
		{
			return null;
		}
		return dateFormat.get().parse(d);
	}
}
