/**
 *
 */
package com.tisl.mpl.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;


/**
 * @author TCS
 *
 */
public class MplValidateAgainstXSDServiceImpl implements MplValidateAgainstXSDService
{
	private final static Logger LOG = Logger.getLogger(MplValidateAgainstXSDServiceImpl.class);

	/*
	 * @param xml
	 *
	 * @param xsd (non-Javadoc)
	 *
	 * @see com.tisl.mpl.service.MplValidateAgainstXSDService#validateAgainstXSD(java.io.InputStream, java.lang.String)
	 *
	 * @returns a map containing validation status
	 */
	@Override
	public Map validateAgainstXSD(final InputStream xml, final String xsd)
	{
		final Map map = new HashMap();
		try
		{

			final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			final Schema schema = factory.newSchema(new StreamSource(xsd));
			final Validator validator = schema.newValidator();
			validator.validate(new StreamSource(xml));
			map.put(MarketplacecommerceservicesConstants.STATUS, MarketplacecommerceservicesConstants.TRUE);
			return map;
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex);
			map.put(MarketplacecommerceservicesConstants.STATUS, MarketplacecommerceservicesConstants.FALSE);
			final String exceptionMsg = ex.toString();
			if (exceptionMsg.indexOf(MarketplacecommerceservicesConstants.CVC_DATATYPE) != -1
					|| exceptionMsg.indexOf(MarketplacecommerceservicesConstants.CVC_COMPLEX) != -1)
			{
				final int index0 = exceptionMsg.indexOf(MarketplacecommerceservicesConstants.CVC_DATATYPE);
				final String substr = exceptionMsg.substring(index0, exceptionMsg.length());
				final int index = substr.indexOf(MarketplacecommerceservicesConstants.COLON) + 1;
				final String msg = substr.substring(index, substr.length());

				map.put(MarketplacecommerceservicesConstants.MSG, msg);
			}
			map.put(MarketplacecommerceservicesConstants.STATUS, MarketplacecommerceservicesConstants.FALSE);
			return map;
		}
	}


}
