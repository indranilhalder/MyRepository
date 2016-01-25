/**
 *
 */
package com.tisl.mpl.facades.populators;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.ImageFormatMapping;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.media.MediaContainerService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.variants.model.VariantProductModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * @author TCS
 *
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CustomImagePopulatorTest
{
	private CustomImagePopulator customPopulator;

	@Mock
	private Converter<MediaModel, ImageData> imageConverterMock;

	@Mock
	private ImageFormatMapping imageMock;
	@Mock
	private MediaService mediaService;

	@Mock
	private MediaContainerService mediaContainerService;

	@Before
	public void setUp()
	{
		customPopulator = new CustomImagePopulator();
		customPopulator.setImageConverter(imageConverterMock);
		//customPopulator.setImageFormatMapping(imageMock);
		customPopulator.setMediaService(mediaService);
		customPopulator.setMediaContainerService(mediaContainerService);
	}


	@Test
	public void testPopulateImageData()
	{
		final VariantProductModel vmModel = Mockito.mock(VariantProductModel.class);
		final String styleSwatch = "styleSwatch";
		final String mediaFormatQualifier = "qualifier";
		final VariantOptionData variantOptionData = Mockito.mock(VariantOptionData.class);
		final MediaModel mediaModel = Mockito.mock(MediaModel.class);
		final MediaContainerModel mediaContainer = Mockito.mock(MediaContainerModel.class);
		given(vmModel.getPicture()).willReturn(mediaModel);
		given(mediaModel.getMediaContainer()).willReturn(mediaContainer);
		given(imageMock.getMediaFormatQualifierForImageFormat(styleSwatch)).willReturn(mediaFormatQualifier);
		final MediaFormatModel mediaFormatModel = Mockito.mock(MediaFormatModel.class);
		given(mediaService.getFormat(mediaFormatQualifier)).willReturn(mediaFormatModel);
		final MediaModel mediaModelMock = Mockito.mock(MediaModel.class);
		given(mediaContainerService.getMediaForFormat(mediaContainer, mediaFormatModel)).willReturn(mediaModelMock);
		final ImageData imageData = Mockito.mock(ImageData.class);
		given(imageConverterMock.convert(mediaModel)).willReturn(imageData);
		customPopulator.populate(vmModel, variantOptionData);
	}




}
