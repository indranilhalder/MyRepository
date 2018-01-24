/**
 * 
 */
package com.tisl.mpl.service;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.CustomerModel;

import com.tisl.mpl.pojo.response.CustomerWalletDetailResponse;
import com.tisl.mpl.pojo.response.QCCustomerRegisterResponse;
import com.tisl.mpl.wsdto.ApplyCliqCashWsDto;
import com.tisl.mpl.wsdto.EgvWalletCreateResponceWsDTO;
import com.tisl.mpl.wsdto.UserCliqCashWsDto;

/**
 * @author TUL
 *
 */
public interface MplEgvWalletService
{
	public QCCustomerRegisterResponse createWalletContainer(CustomerModel currentCustomer);
    
	public EgvWalletCreateResponceWsDTO verifyOtpAndCreateWallet(CustomerModel currentCustomer,String otp);
	public UserCliqCashWsDto getCustomerWalletAmount(CustomerWalletDetailResponse customerWalletDetailData);
	public UserCliqCashWsDto getUserCliqCashDetails(CustomerModel currentCustomer);
	ApplyCliqCashWsDto applyCLiqCash(AbstractOrderModel order,Double walletAmount);
	public void useCliqCash(AbstractOrderModel order);


	
	//public UserCliqCashWsDto getUserCliqCashDetails(CustomerModel currentCustomer);


}
