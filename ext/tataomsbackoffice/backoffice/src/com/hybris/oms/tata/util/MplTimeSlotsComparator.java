/**
 *
 */
package com.hybris.oms.tata.util;

/**
 * @author TOOW10
 *
 */

import java.io.Serializable;
import java.util.Comparator;

import com.hybris.oms.tata.data.MplTimeSlotsData;


public class MplTimeSlotsComparator implements Comparator<Object>, Serializable
{
	private static final long serialVersionUID = -2127053833562854322L;

	private boolean asc = true;
	private int type = 0;

	public MplTimeSlotsComparator(final boolean asc, final int type)
	{
		this.asc = asc;
		this.type = type;
	}

	public int getType()
	{
		return type;
	}

	public void setType(final int type)
	{
		this.type = type;
	}

	@Override
	public int compare(final Object o1, final Object o2)
	{
		final MplTimeSlotsData timeSlots1 = (MplTimeSlotsData) o1;
		final MplTimeSlotsData timeSlots2 = (MplTimeSlotsData) o2;
		switch (type)
		{
			case 1: // Compare Title
				return timeSlots1.getFromTime().compareTo(timeSlots2.getFromTime()) * (asc ? 1 : -1);
			case 2: // Compare First Name
				return timeSlots1.getToTime().compareTo(timeSlots2.getToTime()) * (asc ? 1 : -1);
			default: // Full Name
				return timeSlots1.getFromTime().compareTo(timeSlots2.getFromTime()) * (asc ? 1 : -1);
		}

	}

}