/**
 *
 */
package com.tisl.mpl.facade.product.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.core.model.SizeGuideModel;
import com.tisl.mpl.facade.comparator.SizeGuideComparator;
import com.tisl.mpl.facades.product.data.SizeGuideData;
import com.tisl.mpl.marketplacecommerceservices.service.SizeGuideService;


/**
 * @author TCS
 *
 */

@SuppressWarnings("deprecation")
public class DefaultSizeGuideFacadeImplTest
{


	//	@Resource
	private SizeGuideService sizeGuideService;

	//	@Resource
	private Converter<SizeGuideModel, SizeGuideData> sizeGuideConverter;

	//	@Resource(name = "sizeGuideComparator")
	private SizeGuideComparator sizeGuideComparator;
	//@Autowired
	private ModelService modelService;
	private final DefaultSizeGuideFacade sizeGuideFacade = new DefaultSizeGuideFacade();



	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		//this.mplCouponServiceImpl = new MplCouponServiceImpl();
		this.sizeGuideService = Mockito.mock(SizeGuideService.class);
		this.sizeGuideFacade.setSizeGuideService(sizeGuideService);
		this.sizeGuideComparator = Mockito.mock(SizeGuideComparator.class);
		this.sizeGuideFacade.setSizeGuideComparator(sizeGuideComparator);
		//	this.voucher = Mockito.mock(VoucherModel.class);
	}

	@Test
	public void testGetProductSizeguide() throws CMSItemNotFoundException
	{
		final String productCode = "P1";
		final String categoryType = "Footwear";
		final List<SizeGuideData> sizeGuideDataListForFootwear = new ArrayList<SizeGuideData>();
		final List<SizeGuideModel> sizeModelList = new ArrayList<SizeGuideModel>();
		final SizeGuideModel sizeGuideModel = modelService.create(SizeGuideModel.class);
		sizeModelList.add(sizeGuideModel);
		assertEquals(sizeModelList, sizeGuideService.getProductSizeGuideList(productCode));
		final SizeGuideData sizeGuideData = sizeGuideConverter.convert(sizeGuideModel);
		sizeGuideData.setDimension("5");
		sizeGuideDataListForFootwear.add(sizeGuideData);
		Collections.sort(sizeGuideDataListForFootwear, sizeGuideComparator);
		sizeGuideFacade.getProductSizeguide(productCode, categoryType);
	}
}
