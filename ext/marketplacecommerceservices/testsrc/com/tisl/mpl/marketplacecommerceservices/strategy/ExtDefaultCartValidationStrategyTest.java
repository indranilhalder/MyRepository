/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.strategy;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.service.ExtCommerceStockService;


/**
 * @author TCS
 *
 */
@SuppressWarnings("deprecation")
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ExtDefaultCartValidationStrategyTest
{
	@Mock
	private ExtDefaultCartValidationStrategy extDefaultCartValidationStrategy = Mockito
			.mock(ExtDefaultCartValidationStrategy.class);
	@Mock
	private final CartModel cartModel = Mockito.mock(CartModel.class);
	@Mock
	private final ProductModel productModel = Mockito.mock(ProductModel.class);
	@Mock
	private final CartEntryModel cartEntryModel = Mockito.mock(CartEntryModel.class);
	@Autowired
	private ProductService productService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private ExtCommerceStockService commerceStockService;
	@Autowired
	private BaseStoreService baseStoreService;
	@Autowired
	private CartService cartService;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		this.productService = Mockito.mock(ProductService.class);
		this.cartService = Mockito.mock(CartService.class);
		this.baseStoreService = Mockito.mock(BaseStoreService.class);
		this.extDefaultCartValidationStrategy = Mockito.mock(ExtDefaultCartValidationStrategy.class);
	}

	@Test
	public void validateCartEntry()
	{
		productModel.setCode("987654321");
		cartEntryModel.setBasePrice(Double.valueOf(1234));
		cartEntryModel.setCurrDelCharge(Double.valueOf(50));
		cartEntryModel.setDescription("desc");
		cartEntryModel.setEntryNumber(Integer.valueOf(50));
		final PointOfServiceModel pointOfService = new PointOfServiceModel();
		pointOfService.setName("service");
		cartEntryModel.setDeliveryPointOfService(pointOfService);
		cartModel.setCode("1234c");
		cartModel.setGuid("guid123");
		cartModel.setTotalPrice(Double.valueOf(2000));

		given(productService.getProductForCode(cartEntryModel.getProduct().getCode())).willReturn(productModel);
		final CommerceCartModification modification = new CommerceCartModification();
		modification.setStatusCode("unavailable");
		modification.setQuantityAdded(0L);
		modification.setQuantity(0L);

		final CartEntryModel entry = new CartEntryModel();
		entry.setProduct(productModel);
		modification.setEntry(entry);
		modelService.remove(cartEntryModel);
		modelService.refresh(cartModel);
		final Long stockLevel = Long.valueOf(789);
		given(extDefaultCartValidationStrategy.getStockLevel(cartEntryModel)).willReturn(stockLevel);

		final long cartEntryLevel = 752;
		final Long stockLevelForProductInBaseStore = Long.valueOf(7896L);
		given(
				commerceStockService.getStockLevelForProductAndBaseStore(cartEntryModel.getSelectedUSSID(),
						baseStoreService.getCurrentBaseStore())).willReturn(stockLevelForProductInBaseStore);

		final long newOrderEntryLevel = Math.min(cartEntryLevel, stockLevelForProductInBaseStore.longValue());
		modification.setStatusCode("movedFromPOSToStore");
		final CartEntryModel existingEntryForProduct = getExistingShipCartEntryForProduct(cartModel, cartEntryModel.getProduct());

		modelService.remove(cartEntryModel);
		final long quantityAdded = 2;
		modification.setQuantityAdded(quantityAdded);
		final long updatedQuantity = 3;
		modification.setQuantity(updatedQuantity);
		existingEntryForProduct.setQuantity(Long.valueOf(updatedQuantity));
		modification.setEntry(existingEntryForProduct);

		modelService.refresh(cartModel);
		modification.setStatusCode("lowStock");
		modification.setQuantityAdded(newOrderEntryLevel);
		modification.setQuantity(cartEntryLevel);
		modification.setEntry(cartEntryModel);
		cartEntryModel.setQuantity(Long.valueOf(newOrderEntryLevel));
		modelService.save(cartEntryModel);
		given(extDefaultCartValidationStrategy.validateCartEntry(cartModel, cartEntryModel)).willReturn(modification);

		final CommerceCartModification modification1 = new CommerceCartModification();
		modification.setStatusCode("success");
		modification.setQuantityAdded(cartEntryLevel);
		modification.setQuantity(cartEntryLevel);
		modification.setEntry(cartEntryModel);
		given(extDefaultCartValidationStrategy.validateCartEntry(cartModel, cartEntryModel)).willReturn(modification1);
	}

	@Test
	public void getStockLevel()
	{
		final PointOfServiceModel pointOfService = new PointOfServiceModel();
		final BaseStoreModel baseStoreModel = new BaseStoreModel();
		baseStoreModel.setSubmitOrderProcessCode("1234");
		baseStoreModel.setCodLowerLimit(Long.valueOf(500));
		baseStoreModel.setConvenienceChargeForCOD(Long.valueOf(1000));
		pointOfService.setName("seller name");
		pointOfService.setLatitude(Double.valueOf(79));
		cartEntryModel.setSelectedUSSID("s00000000000000000000000001");
		given(cartEntryModel.getDeliveryPointOfService()).willReturn(pointOfService);

		cartEntryModel.setSelectedUSSID("s00000000000000000000000001");
		given(baseStoreService.getCurrentBaseStore()).willReturn(baseStoreModel);

	}

	private CartEntryModel getExistingShipCartEntryForProduct(final CartModel cartModel, final ProductModel product)
	{
		for (final CartEntryModel entryModel : cartService.getEntriesForProduct(cartModel, product))
		{
			if (entryModel.getDeliveryPointOfService() == null)
			{
				return entryModel;
			}
		}
		return null;
	}


}
