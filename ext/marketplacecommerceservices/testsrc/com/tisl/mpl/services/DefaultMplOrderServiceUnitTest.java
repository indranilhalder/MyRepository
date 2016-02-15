/**
 *
 */
package com.tisl.mpl.services;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.CancellationReasonModel;
import com.tisl.mpl.core.model.ReturnReasonModel;
import com.tisl.mpl.facades.product.data.ReturnReasonData;
import com.tisl.mpl.marketplacecommerceservices.daos.MplOrderDao;


/**
 * @author TCS
 *
 */
public class DefaultMplOrderServiceUnitTest
{
	@Autowired
	private MplOrderDao mplOrderDao;

	//	@Autowired
	//	private UserService userService;

	//	@Resource(name = "configurationService")
	//	private ConfigurationService configurationService;
	@Autowired
	//	private MplAwbStatusService mplAwbStatusService;
	//	private DefaultMplOrderService defaultMplOrderService;
	private static final Logger LOG = Logger.getLogger(DefaultMplOrderServiceUnitTest.class);

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		//		this.defaultMplOrderService = new DefaultMplOrderService();
		//		this.mplAwbStatusService = Mockito.mock(MplAwbStatusService.class);
		this.mplOrderDao = Mockito.mock(MplOrderDao.class);
		//		this.userService = Mockito.mock(UserService.class);
		//		this.configurationService = Mockito.mock(ConfigurationService.class);
	}


	@Test
	public void testGetReturnReasonForOrderItem()
	{
		final List<ReturnReasonData> reasonDataList = new ArrayList<ReturnReasonData>();
		final List<ReturnReasonModel> reasonModelList = new ArrayList<ReturnReasonModel>();
		Mockito.when(mplOrderDao.getReturnReasonForOrderItem()).thenReturn(reasonModelList);
		final ReturnReasonModel newModel = reasonModelList.get(0);
		final ReturnReasonData oReasonData = new ReturnReasonData();
		oReasonData.setReasonDescription(newModel.getReasonDescription());
		oReasonData.setCode(newModel.getReasonCode());
		reasonDataList.add(oReasonData);
		LOG.info("Method : testGetReturnReasonForOrderItem >>>>>>>");
	}

	@Test
	public void testFetchConsignment()
	{
		final String consignmentCode = "1235445";
		final ConsignmentModel consignmentModel = new ConsignmentModel();
		Mockito.when(mplOrderDao.fetchConsignment(consignmentCode)).thenReturn(consignmentModel);
		LOG.info("Method : testFetchConsignment >>>>>>>");
	}

	@Test
	public void testGetCancellationReason()
	{
		final List<CancellationReasonModel> consignmentModel = new ArrayList<CancellationReasonModel>();
		Mockito.when(mplOrderDao.fetchCancellationReason()).thenReturn(consignmentModel);
		LOG.info("Method : testGetCancellationReason >>>>>>>");
	}
}
