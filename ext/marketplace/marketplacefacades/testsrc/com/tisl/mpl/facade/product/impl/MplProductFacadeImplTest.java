/**
 *
 */
package com.tisl.mpl.facade.product.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.product.ProductService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.facade.product.MplProductFacade;
import com.tisl.mpl.marketplacecommerceservices.service.MplProductService;


/**
 * @author i313024
 *
 */
@UnitTest
public class MplProductFacadeImplTest
{
	@Autowired
	private MplProductService mplProductService;

	@Resource(name = "productService")
	private ProductService productService;

	@Resource(name = "mplProductFacade")
	private MplProductFacade mplProductFacade;

	protected static final Logger LOG = Logger.getLogger(MplProductFacadeImplTest.class);

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		this.mplProductService = Mockito.mock(MplProductService.class);
		this.productService = Mockito.mock(ProductService.class);
		this.mplProductFacade = Mockito.mock(MplProductFacade.class);
	}

	@Test
	public ProductFeatureModel testGetProductFeatureModelByProductAndQualifier()
	{
		final String code = "987654321";
		final String qualifier = "pcmClassification/1/tshirttypeCC.tshirttype";
		final List<ProductFeatureModel> productFeatures = mplProductService.findProductFeaturesByQualifierAndProductCode(code,
				qualifier);
		if (productFeatures != null && productFeatures.size() > 0)
		{
			return productFeatures.get(0);
		}
		return null;
	}
}
