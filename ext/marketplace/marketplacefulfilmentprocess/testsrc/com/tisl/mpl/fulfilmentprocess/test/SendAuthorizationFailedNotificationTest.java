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
package com.tisl.mpl.fulfilmentprocess.test;

import de.hybris.platform.orderprocessing.events.AuthorizationFailedEvent;
import de.hybris.platform.orderprocessing.events.FraudErrorEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.event.EventService;
import com.tisl.mpl.fulfilmentprocess.actions.order.SendAuthorizationFailedNotificationAction;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
*
*/
public class SendAuthorizationFailedNotificationTest
{
	@InjectMocks
	private final SendAuthorizationFailedNotificationAction sendAuthorizationFailedNotification = new SendAuthorizationFailedNotificationAction();

	@Mock
	private EventService eventService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test method for
	 * {@link com.tisl.mpl.fulfilmentprocess.actions.order.SendOrderPlacedNotificationAction#executeAction(OrderProcessModel)}
	 * .
	 */
	@Test
	public void testExecuteActionOrderProcessModel()
	{
		final OrderProcessModel process = new OrderProcessModel();
		sendAuthorizationFailedNotification.executeAction(process);

		Mockito.verify(eventService).publishEvent(Mockito.argThat(new BaseMatcher<FraudErrorEvent>()
		{

			@Override
			public boolean matches(final Object item)
			{
				if (item instanceof AuthorizationFailedEvent)
				{
					final AuthorizationFailedEvent event = (AuthorizationFailedEvent) item;
					if (event.getProcess().equals(process))
					{
						return true;
					}
				}
				return false;
			}

			@Override
			public void describeTo(final Description description)
			{
				//nothing to do

			}
		}));
	}
}
