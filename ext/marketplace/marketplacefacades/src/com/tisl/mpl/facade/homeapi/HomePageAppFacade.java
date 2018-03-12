/**
 *
 */
package com.tisl.mpl.facade.homeapi;

import com.tisl.mpl.wsdto.HomepageComponetsDTO;


/**
 * @author TCS
 *
 */
public interface HomePageAppFacade
{


	/**
	 * @param homepageComponentRequestDTO
	 * @return
	 */
	public HomepageComponetsDTO gethomepageComponentsDTO(String productCodes);


}
