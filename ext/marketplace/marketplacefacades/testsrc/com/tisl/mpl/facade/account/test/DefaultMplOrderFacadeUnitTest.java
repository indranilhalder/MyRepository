/**
 *
 */
package com.tisl.mpl.facade.account.test;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.CancellationReasonModel;
import com.tisl.mpl.facades.account.register.impl.DefaultMplOrderFacade;
import com.tisl.mpl.facades.product.data.ReturnReasonData;
import com.tisl.mpl.marketplacecommerceservices.service.MplOrderService;


/**
 * @author TCS
 *
 */
@UnitTest
public class DefaultMplOrderFacadeUnitTest
{
	@Autowired
	private MplOrderService mplOrderService;
	private DefaultMplOrderFacade defaultMplOrderFacade;
	protected static final Logger LOG = Logger.getLogger(DefaultMplOrderFacadeUnitTest.class);

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		this.defaultMplOrderFacade = new DefaultMplOrderFacade();
		this.mplOrderService = Mockito.mock(MplOrderService.class);
		//this.baseStoreService = Mockito.mock(BaseStoreService.class);
		//this.userService = Mockito.mock(UserService.class);
	}

	@Test
	public void testGetReturnReasonForOrderItem1()
	{
		List<ReturnReasonData> reasonList = new ArrayList<ReturnReasonData>();
		reasonList = mplOrderService.getReturnReasonForOrderItem();
		Mockito.when(mplOrderService.getReturnReasonForOrderItem()).thenReturn(reasonList);
		reasonList = mplOrderService.getReturnReasonForOrderItem();
		LOG.info("Method : testGetBaseSitePreferredCategories >>>>>>>");
	}

	@Test
	public void testGetCancellationReason()
	{
		final List<CancellationReasonModel> cancelReason = new ArrayList<CancellationReasonModel>();
		Mockito.when(mplOrderService.getCancellationReason()).thenReturn(cancelReason);
	}

	@Test
	public void testGetReturnReasonForOrderItem()
	{
		final String returnCancelFlag = "";
		List<ReturnReasonData> returnReasonDataList = new ArrayList<ReturnReasonData>();
		List<CancellationReasonModel> cancellationReasonModel = new ArrayList<CancellationReasonModel>();

		if (returnCancelFlag.equalsIgnoreCase("R"))
		{
			returnReasonDataList = defaultMplOrderFacade.getReturnReasonForOrderItem();
		}
		else if (returnCancelFlag.equalsIgnoreCase("C"))
		{
			cancellationReasonModel = defaultMplOrderFacade.getCancellationReason();
		}

		final ReturnReasonData returnReasonDataObj = new ReturnReasonData();
		final CancellationReasonModel cnewModel = cancellationReasonModel.get(0);
		returnReasonDataObj.setReasonDescription(cnewModel.getReasonDescription());
		returnReasonDataObj.setCode(cnewModel.getReasonCode());

		returnReasonDataList.add(returnReasonDataObj);
	}
}
