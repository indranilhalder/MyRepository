/**
 *
 */
package com.tisl.mpl.services;

import de.hybris.platform.core.model.PancardInformationModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.daos.pancard.MplPancardDao;
import com.tisl.mpl.marketplacecommerceservices.service.NotificationService;
import com.tisl.mpl.marketplacecommerceservices.services.pancard.impl.MplPancardServiceImpl;
import com.tisl.mpl.pojo.OrderLineResData;
import com.tisl.mpl.pojo.PanCardResDTO;
import com.tisl.mpl.service.MplPancardUploadService;


/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "unused", "deprecation" })
public class MplPancardServiceImplTest
{
	private static final Logger LOG = Logger.getLogger(MplPancardServiceImpl.class);
	private MplPancardServiceImpl mplPancardServiceImpl;
	private MplPancardDao mplPancardDao;
	private ConfigurationService configurationService;
	private ModelService modelService;
	private MplPancardUploadService mplPancardUploadserviceImpl;
	private NotificationService notificationService;
	private PancardInformationModel panInfo;
	private MultipartFile file;
	private Configuration configuration;
	private OrderModel orderModel;
	private PanCardResDTO resDTO;
	private OrderLineResData olrs;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		this.mplPancardServiceImpl = new MplPancardServiceImpl();
		this.mplPancardDao = Mockito.mock(MplPancardDao.class);
		this.mplPancardServiceImpl.setMplPancardDao(mplPancardDao);
		this.configurationService = Mockito.mock(ConfigurationService.class);
		this.modelService = Mockito.mock(ModelService.class);
		this.mplPancardUploadserviceImpl = Mockito.mock(MplPancardUploadService.class);
		this.notificationService = Mockito.mock(NotificationService.class);
		this.panInfo = Mockito.mock(PancardInformationModel.class);
		this.file = Mockito.mock(MultipartFile.class);
		this.configuration = Mockito.mock(Configuration.class);
		this.orderModel = Mockito.mock(OrderModel.class);
		this.resDTO = Mockito.mock(PanCardResDTO.class);
		this.olrs = Mockito.mock(OrderLineResData.class);
	}

	@Test
	public void testGetPanCardOrderId()
	{
		final List<PancardInformationModel> panInfoList = Arrays.asList(panInfo);
		Mockito.when(mplPancardDao.getPanCardOrderId("orderreferancenumber")).thenReturn(panInfoList);
		final List<PancardInformationModel> actual = mplPancardServiceImpl.getPanCardOrderId("orderreferancenumber");
		Assert.assertEquals(actual, panInfoList);
	}

	@Test
	public void testSetPanCardDetailsAndPIcall()
	{
		try
		{
			final List<String> transactionidList = new ArrayList<String>();
			transactionidList.add("11111111111");
			Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
			Mockito.when(configuration.getString("user.pancard.folder.location")).thenReturn("pancardImagePath");

			Mockito.when(
					mplPancardUploadserviceImpl.generateXmlForPanCard(null, "orderreferancenumber", transactionidList,
							"pancardImagePath")).thenReturn("status");
			final List<PancardInformationModel> panCardModelList = new ArrayList<PancardInformationModel>();

			Mockito.when(panInfo.getOrderId()).thenReturn("orderreferancenumber");
			Mockito.when(panInfo.getPath()).thenReturn("pancardImagePath");
			Mockito.when(panInfo.getPancardNumber()).thenReturn("pancardnumber");
			Mockito.when(panInfo.getStatus()).thenReturn("status");
			Mockito.when(panInfo.getName()).thenReturn("customername");
			Mockito.when(panInfo.getTransactionId()).thenReturn("11111111111");
			panCardModelList.add(panInfo);

			Mockito.doNothing().when(modelService).saveAll(panCardModelList);
			mplPancardServiceImpl.setPanCardDetailsAndPIcall("orderreferancenumber", transactionidList, "customername",
					"pancardnumber", file);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
		}
	}

	@Test
	public void testRefreshPanCardDetailsAndPIcall()
	{
		try
		{
			final List<PancardInformationModel> pModelList = new ArrayList<PancardInformationModel>();

			final List<PancardInformationModel> pModel = new ArrayList<PancardInformationModel>();
			Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
			Mockito.when(configuration.getString("user.pancard.folder.location")).thenReturn("pancardImagePath");

			Mockito.when(
					mplPancardUploadserviceImpl.generateXmlForPanCard(pModelList, "orderreferancenumber", null, "pancardImagePath"))
					.thenReturn("status");
			final List<PancardInformationModel> panCardModelList = new ArrayList<PancardInformationModel>();
			for (final PancardInformationModel pm : pModel)
			{
				Mockito.when(pm.getPath()).thenReturn("pancardImagePath");
				Mockito.when(pm.getPancardNumber()).thenReturn("pancardnumber");
				Mockito.when(pm.getStatus()).thenReturn("status");

				panCardModelList.add(pm);
			}
			Mockito.doNothing().when(modelService).save(orderModel);
			Mockito.doNothing().when(modelService).saveAll(panCardModelList);
			mplPancardServiceImpl.refreshPanCardDetailsAndPIcall(pModelList, pModel, "orderreferancenumber", file);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
		}
	}

	@Test
	public void testSetPancardRes()
	{
		try
		{
			final List<PancardInformationModel> panInfoList = Arrays.asList(panInfo);
			Mockito.when(mplPancardDao.getPanCardOrderId(resDTO.getOrderId())).thenReturn(panInfoList);
			final List<OrderModel> orderModelList = Arrays.asList(orderModel);
			Mockito.when(mplPancardDao.getOrderForCode(resDTO.getOrderId())).thenReturn(orderModelList);
			for (final PancardInformationModel pmNew : panInfoList)
			{
				Mockito.when(pmNew.getStatus()).thenReturn("APPROVED");
				Mockito.doNothing().when(modelService).save(pmNew);
			}
			Mockito.doNothing().when(notificationService).triggerEmailAndSmsOnPancardReject(orderModelList.get(0));
			Mockito.doNothing().when(modelService).save(orderModelList.get(0));
			mplPancardServiceImpl.setPancardRes(resDTO);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
		}
	}
}
