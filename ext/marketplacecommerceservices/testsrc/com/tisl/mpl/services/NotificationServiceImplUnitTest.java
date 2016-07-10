/**
 *
 */
package com.tisl.mpl.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

//import com.tisl.mpl.core.model.NotificationModel;
//import com.tisl.mpl.marketplacecommerceservices.daos.NotificationDao;


/**
 * @author 592217
 *
 */
public class NotificationServiceImplUnitTest
{
//	@Autowired
//	private NotificationDao notificationDao;

//	@Autowired
//	private NotificationModel notificationModel;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

	//	this.notificationDao = Mockito.mock(NotificationDao.class);

	}

	@Test
	public void testgetNotificationDetails()
	{
		//final List<NotificationModel> notification = Arrays.asList(notificationModel);


		//final String email = "test@tcs.com";
		//	Mockito.when(notificationDao.getNotificationDetail(email)).thenReturn(notification);

	}



}
