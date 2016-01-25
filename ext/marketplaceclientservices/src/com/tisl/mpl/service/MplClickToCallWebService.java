/**
 *
 */
package com.tisl.mpl.service;

import java.io.IOException;
import java.net.MalformedURLException;

import com.tisl.mpl.wsdto.ClickToCallWsDTO;


/**
 * @author TCS
 *
 */
public interface MplClickToCallWebService
{

	public String clickToCallWebService(ClickToCallWsDTO clickToCallWsDTO) throws MalformedURLException, IOException;
}
