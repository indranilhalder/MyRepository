/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.service.impl.MplPancardUploadserviceImpl;
import com.tisl.mpl.xml.pojo.LPAWBUpdate;
import com.tisl.mpl.xml.pojo.OrderLine;


/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "unused", "deprecation" })
public class MplPancardUploadServiceImplTest
{
	private static final Logger LOG = Logger.getLogger(MplPancardUploadserviceImpl.class);
	private MplPancardUploadserviceImpl mplPancardUploadServiceImpl;
	private ConfigurationService configurationService;
	private LPAWBUpdate lpAwbUpdate;
	private OrderLine orderLine;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		this.mplPancardUploadServiceImpl = new MplPancardUploadserviceImpl();
		this.configurationService = Mockito.mock(ConfigurationService.class);
		this.lpAwbUpdate = Mockito.mock(LPAWBUpdate.class);
		this.orderLine = Mockito.mock(OrderLine.class);
	}

	@Test
	public void testGenerateXmlForPanCard()
	{
		try
		{
			final List<OrderLine> orderLineList = new ArrayList<OrderLine>();
			Mockito.when(orderLine.getInterfaceType()).thenReturn("PANCARD");
			Mockito.when(orderLine.getPancardPath()).thenReturn("panCardImagePath");
			Mockito.when(orderLine.getTransactionId()).thenReturn("11111111111");
			Mockito.when(orderLine.getPancardStatus()).thenReturn("PENDING_FOR_VERIFICATION");

			orderLineList.add(orderLine);
			Mockito.when(lpAwbUpdate.getOrderId()).thenReturn("orderReferanceNumber");
			Mockito.when(lpAwbUpdate.getOrderLine()).thenReturn(orderLineList);
			Mockito.when(mplPancardUploadServiceImpl.getResponseFromPIforPanCard(lpAwbUpdate)).thenReturn("status");
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + e);
		}
	}
}
