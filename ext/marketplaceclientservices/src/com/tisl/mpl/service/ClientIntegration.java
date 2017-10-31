/**
 *
 */
package com.tisl.mpl.service;

import javax.xml.bind.JAXBException;


/**
 * @author TCS
 *
 */
public interface ClientIntegration
{
	public String sendWebFormTicket();

	public String checkDuplicateWebFormTicket(String stringXml) throws JAXBException;
}
