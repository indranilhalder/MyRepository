/**
 * 
 */
package com.tisl.mpl.facades.populators;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.tisl.mpl.core.model.PcmProductVariantModel;


/**
 * @author 592991
 * 
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CustomVariantDataPopulatorTest
{
	private CustomVariantDataPopulator customPopulator;
	@Mock
	private Converter<VariantProductModel, VariantOptionData> variantOptionDataConverter;

	@Before
	public void setUp()
	{
		customPopulator = new CustomVariantDataPopulator();
		customPopulator.setVariantOptionDataConverter(variantOptionDataConverter);
	}

	@Test
	public void shouldPopulateVariantForColorSwatch()
	{
		final String selectedSize = "S";
		final PcmProductVariantModel vmModel = Mockito.mock(PcmProductVariantModel.class);
		final Collection<VariantProductModel> variantList = new ArrayList<VariantProductModel>();
		final VariantOptionData variantOptionData = mock(VariantOptionData.class);
		variantList.add(vmModel);
		final List<VariantOptionData> variantOptions = new ArrayList<VariantOptionData>();
		variantOptions.add(variantOptionData);
		final ProductModel productmodel = Mockito.mock(ProductModel.class);
		final PcmProductVariantModel pcmModel = Mockito.mock(PcmProductVariantModel.class);
		given(pcmModel.getBaseProduct()).willReturn(productmodel);
		given(pcmModel.getSize()).willReturn(selectedSize);
		given(productmodel.getVariants()).willReturn(variantList);
		given(variantOptionDataConverter.convert(vmModel)).willReturn(variantOptionData);
		given(vmModel.getSize()).willReturn("S");
		given(variantOptionData.getDefaultUrl()).willReturn("URL");
		given(variantOptionData.getColour()).willReturn(vmModel.getColour());
		given(variantOptionData.getUrl()).willReturn("URL");
		final ProductData result = new ProductData();
		customPopulator.populate(pcmModel, result);
	}

	@Test
	public void shouldPopulateVariant()
	{
		final String selectedSize = "S";
		final PcmProductVariantModel vmModel = Mockito.mock(PcmProductVariantModel.class);
		final Collection<VariantProductModel> variantList = new ArrayList<VariantProductModel>();
		final VariantOptionData variantOptionData = mock(VariantOptionData.class);
		variantList.add(vmModel);
		final List<VariantOptionData> variantOptions = new ArrayList<VariantOptionData>();
		variantOptions.add(variantOptionData);
		final ProductModel productmodel = Mockito.mock(ProductModel.class);
		final PcmProductVariantModel pcmModel = Mockito.mock(PcmProductVariantModel.class);
		given(pcmModel.getBaseProduct()).willReturn(productmodel);
		given(pcmModel.getSize()).willReturn(selectedSize);
		given(productmodel.getVariants()).willReturn(variantList);
		given(variantOptionDataConverter.convert(vmModel)).willReturn(variantOptionData);
		given(vmModel.getSize()).willReturn("M");
		given(variantOptionData.getDefaultUrl()).willReturn("");
		given(variantOptionData.getColour()).willReturn(vmModel.getColour());
		given(variantOptionData.getUrl()).willReturn("URL");
		final ProductData result = new ProductData();
		customPopulator.populate(pcmModel, result);
	}
}
