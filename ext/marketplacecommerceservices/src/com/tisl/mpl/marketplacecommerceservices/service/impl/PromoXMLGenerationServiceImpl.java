/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.PromoXMLGenerationService;
import com.tisl.mpl.pojo.PromotionXMLData;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.PromoXMLUtility;


/**
 * @author TCS
 *
 */
public class PromoXMLGenerationServiceImpl implements PromoXMLGenerationService
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PromoXMLGenerationServiceImpl.class.getName());

	/**
	 * @Description : Promotion XML generation for Seller Portal
	 * @param : item
	 * @return : String
	 */
	@Override
	public String promoXMLGeneration(final Item item)
	{
		String xmlString = MarketplacecommerceservicesConstants.EMPTY;
		PromotionXMLData promoData = new PromotionXMLData();


		try
		{
			promoData = getPromoXMLUtility().getPromoXMLData(item);

			if (null != promoData)
			{
				final JAXBContext context = JAXBContext.newInstance(PromotionXMLData.class);
				final Marshaller m = context.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				final StringWriter sw = new StringWriter();
				m.marshal(promoData, sw);
				xmlString = sw.toString();
				LOG.debug(xmlString);
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
		}
		return xmlString;
	}

	protected PromoXMLUtility getPromoXMLUtility()
	{
		return Registry.getApplicationContext().getBean("promoXMLUtility", PromoXMLUtility.class);
	}

	protected PromotionXMLData getPromotionXMLData()
	{
		return Registry.getApplicationContext().getBean("promotionXMLData", PromotionXMLData.class);
	}

}
