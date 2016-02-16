/**
 *
 */
package com.tisl.mpl.facade;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.core.model.SizeGuideModel;
import com.tisl.mpl.marketplacecommerceservices.service.impl.SizeGuideServiceImpl;


/**
 * @author TCS
 *
 */
public class SizeGuideFacadeImplUnitTest
{

	private SizeGuideServiceImpl sizeGuideServiceImpl;

	private SizeGuideModel sizeGuideModel;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		this.sizeGuideServiceImpl = Mockito.mock(SizeGuideServiceImpl.class);
	}

	@Test
	public void testgetSizeguideList() throws CMSItemNotFoundException
	{
		final String ProductSource = "987654321";

		final List<SizeGuideModel> sizeguideList = Arrays.asList(sizeGuideModel);

		Mockito.when(sizeGuideServiceImpl.getProductSizeGuideList(ProductSource)).thenReturn(sizeguideList);

	}

}
