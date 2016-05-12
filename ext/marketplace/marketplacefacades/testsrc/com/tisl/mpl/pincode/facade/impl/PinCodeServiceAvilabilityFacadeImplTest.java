/**
 *
 */
package com.tisl.mpl.pincode.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.PincodeServiceData;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPincodeRestrictionService;


/**
 * @author TCS
 *
 */
@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class PinCodeServiceAvilabilityFacadeImplTest
{
	private final PinCodeServiceAvilabilityFacadeImpl pinCodeServiceAvilabilityFacadeImpl = new PinCodeServiceAvilabilityFacadeImpl();

	@Resource(name = "mplPincodeRestrictionService")
	private MplPincodeRestrictionService mplPincodeRestrictionService;

	@Autowired
	private MplCommerceCartService mplCommerceCartService;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetResonseForPinCode()
	{
		//TISSEC-50
		final List<PincodeServiceData> requestData = new ArrayList<PincodeServiceData>();
		final List<String> ussidList = new ArrayList<String>();
		final List<String> sellerIdList = new ArrayList<String>();
		ussidList.add("");//TODO : Please enter ussid
		sellerIdList.add("");//TODO : Please enter seller id
		final String productCode = MarketplacecommerceservicesConstants.EMPTY;//TODO : Please enter product code
		final String pin = MarketplacecommerceservicesConstants.EMPTY;//TODO : Please enter pincode
		final PincodeServiceData reqData = new PincodeServiceData();
		assertNotNull(reqData.getUssid());
		assertFalse(requestData.isEmpty());
		assertNotNull(reqData.getSellerId());
		assertNotNull(mplPincodeRestrictionService.getRestrictedPincode(ussidList, sellerIdList, productCode, pin, requestData));
		assertFalse(mplPincodeRestrictionService.getRestrictedPincode(ussidList, sellerIdList, productCode, pin, requestData)
				.isEmpty());
		final List<PincodeServiceData> validReqData = mplPincodeRestrictionService.getRestrictedPincode(ussidList, sellerIdList,
				productCode, pin, requestData);
		assertNotNull(mplCommerceCartService.getAllResponsesForPinCode(pin, validReqData));
		assertFalse(mplCommerceCartService.getAllResponsesForPinCode(pin, validReqData).isEmpty());
		assertEquals(requestData, pinCodeServiceAvilabilityFacadeImpl.getResonseForPinCode(productCode, pin, requestData));
	}

	@Test
	public void testGetServiceablePinCodeCart()
	{
		final String pin = MarketplacecommerceservicesConstants.EMPTY;//TODO : Please enter pincode
		final List<PincodeServiceData> pincodeServiceData = new ArrayList<PincodeServiceData>();
		assertNotNull(mplCommerceCartService.getServiceablePinCodeCart(pin, pincodeServiceData));
		assertEquals(pincodeServiceData, pinCodeServiceAvilabilityFacadeImpl.getServiceablePinCodeCart(pin, pincodeServiceData));
	}
}
