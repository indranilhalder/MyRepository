/**
 *
 */
package com.tisl.mpl.service;

import com.tisl.mpl.wsdto.SellerMasterWsDTO;



/**
 * @author TCS
 *
 */
public interface MplSellerMasterService
{
	//public String saveSellerInformation(final SellerMasterWsDTO sellerInformationWSDTO);

	/*
	 * public String saveSellerInformationUpdate(final SellerMasterWsDTO sellerMasterWsDTO, final SellerInformationModel
	 * resModelUpdate);
	 */

	public String insertUpdate(final SellerMasterWsDTO sellerMasterWsDTO);

}
