/**
 *
 */
package com.tisl.mpl.util;

import java.util.Comparator;

import com.tisl.mpl.data.NotificationData;


/**
 * @author 682058
 *
 */
public class NotificationDataComparator implements Comparator<NotificationData>
{

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final NotificationData arg0, final NotificationData arg1)
	{
		if (arg1.getNotificationCreationDate().compareTo(arg0.getNotificationCreationDate()) > 0)
		{
			return 1;
		}
		else
		{
			return -1;
		}
	}

}
