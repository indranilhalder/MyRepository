/**
 *
 */
package com.tisl.mpl.service;

import com.tisl.mpl.xml.pojo.AWBStatusResponse;


/**
 * @author TCS
 *
 */
public interface MplAwbStatusService
{

	/**
	 * @param awbNumber
	 * @param tplCode
	 * @return AwbStatusResponse
	 */
	public AWBStatusResponse prepAwbNumbertoOMS(String awbNumber, String tplCode);



}
