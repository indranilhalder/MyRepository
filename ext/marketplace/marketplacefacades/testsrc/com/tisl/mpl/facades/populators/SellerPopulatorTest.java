/**
 *
 */
package com.tisl.mpl.facades.populators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.core.model.product.ProductModel;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.core.enums.PaymentModesEnum;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.helper.ProductDetailsHelper;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 *
 */

public class SellerPopulatorTest
{
	@Resource
	private final ProductModel productModel = new ProductModel();

	@Resource
	private final SellerInformationModel sellerInformationModel = new SellerInformationModel();

	@Resource
	private final ProductDetailsHelper productDetailsHelper = new ProductDetailsHelper();

	@Resource
	private final RichAttributeModel richAttributeModel = new RichAttributeModel();

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void populate()
	{
		assertNotNull(productModel.getSellerInformationRelator());
		assertNotNull(sellerInformationModel.getSellerArticleSKU());
		assertNotNull(sellerInformationModel.getRichAttribute());
		assertNotNull(productDetailsHelper.getDeliveryModeLlist(richAttributeModel, sellerInformationModel.getSellerArticleSKU()));
		assertNotNull(richAttributeModel.getPaymentModes());
		assertNotNull(richAttributeModel.getPaymentModes());
		assertEquals(richAttributeModel.getPaymentModes(), PaymentModesEnum.BOTH);
		assertEquals(richAttributeModel.getPaymentModes(), PaymentModesEnum.COD);
		assertNotNull(richAttributeModel.getDeliveryFulfillModes());
		assertNotNull(richAttributeModel.getDeliveryFulfillModes().getCode());
		assertNotNull(sellerInformationModel.getSellerName());
		assertNotNull(richAttributeModel.getShippingModes());
		assertNotNull(richAttributeModel.getShippingModes().getCode());
		assertNotNull(richAttributeModel.getReturnWindow());
		assertNotNull(richAttributeModel.getReplacementWindow());
		assertNotNull(richAttributeModel.getCancellationWindow());
		assertNotNull(richAttributeModel.getExchangeAllowedWindow());
		assertNotNull(sellerInformationModel.getSellerID());

	}

	public void testIsApproved()
	{
		final Boolean boolean1 = Boolean.TRUE;
		assertNotNull(productModel.getApprovalStatus());
		assertEquals(boolean1, productModel.getApprovalStatus());
	}
}
