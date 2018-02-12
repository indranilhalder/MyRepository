/**
 *
 */
package com.tisl.mpl.v2.controller;

import de.hybris.platform.commercewebservicescommons.cache.CacheControl;
import de.hybris.platform.commercewebservicescommons.cache.CacheControlDirective;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;

import java.io.InputStream;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.marketplacecommerceservices.service.MplDelistingService;
import com.tisl.mpl.pojo.ProductDelistingDTO;
import com.tisl.mpl.pojo.SellerDelistingDTO;


/**
 * @author TCS
 *
 */
@Controller
@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 1800)
public class DelistController extends BaseController
{

	@Resource(name = "mplDelistingService")
	private MplDelistingService mplDelistingService;
	private static final String EXCEPTION = "the exception is **** ";
	private static final Logger LOG = Logger.getLogger(DelistController.class);

	/*
	 * to receive delist xml from PI
	 * 
	 * @param restrictionXML
	 * 
	 * @return void
	 */
	@RequestMapping(value = "/{baseSiteId}/Delist/productDelisting", method = RequestMethod.POST)
	@ResponseBody
	public void productDelisting(final InputStream delistXML) throws RequestParameterException, JAXBException
	{
		try
		{
			final JAXBContext jaxbContext = JAXBContext.newInstance(ProductDelistingDTO.class);
			final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			final ProductDelistingDTO delistDTO = (ProductDelistingDTO) jaxbUnmarshaller.unmarshal(delistXML);

			mplDelistingService.delistProduct(delistDTO);
		}
		catch (final RequestParameterException e)
		{
			LOG.error(EXCEPTION + e);
		}
		catch (final JAXBException e)
		{
			LOG.error(EXCEPTION + e);
		}
		catch (final Exception e)
		{
			LOG.error(EXCEPTION + e);
		}
	}

	@RequestMapping(value = "/{baseSiteId}/Delist/sellerDelisting", method = RequestMethod.POST)
	@ResponseBody
	public void sellerDelisting(final InputStream delistXML) throws RequestParameterException, JAXBException
	{
		try
		{
			final JAXBContext jaxbContext = JAXBContext.newInstance(SellerDelistingDTO.class);
			final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			final SellerDelistingDTO sellerDelistDTO = (SellerDelistingDTO) jaxbUnmarshaller.unmarshal(delistXML);

			mplDelistingService.sellerDelist(sellerDelistDTO);
		}
		catch (final RequestParameterException e)
		{
			LOG.error(EXCEPTION + e);
		}
		catch (final JAXBException e)
		{
			LOG.error(EXCEPTION + e);
		}
		catch (final Exception e)
		{
			LOG.error(EXCEPTION + e);
		}
	}

}
