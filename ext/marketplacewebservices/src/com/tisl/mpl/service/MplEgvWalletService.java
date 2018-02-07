/**
 * 
 */
package com.tisl.mpl.service;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.promotions.util.Tuple2;

import java.util.List;

import com.tisl.mpl.facades.product.data.MplCustomerProfileData;
import com.tisl.mpl.pojo.response.CustomerWalletDetailResponse;
import com.tisl.mpl.pojo.response.QCCustomerRegisterResponse;
import com.tisl.mpl.wsdto.ApplyCartCouponsDTO;
import com.tisl.mpl.wsdto.ApplyCliqCashWsDto;
import com.tisl.mpl.wsdto.ApplyCouponsDTO;
import com.tisl.mpl.wsdto.EgvCheckMobileNumberWsDto;
import com.tisl.mpl.wsdto.EgvWalletCreateRequestWsDTO;
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
	public boolean generateOtpForUpdateWallet(String  mobileNumber,CustomerModel customer);
	public ApplyCliqCashWsDto setTotalPrice(ApplyCliqCashWsDto applyCliqCashWsDto, AbstractOrderModel cartModel);
	public Tuple2<Boolean, String> isCartVoucherPresent(final List<DiscountModel> discounts);
	public EgvCheckMobileNumberWsDto checkWalletMobileNumber(final EgvWalletCreateRequestWsDTO request , boolean isUpdateProfile);
	public ApplyCouponsDTO setTotalPrice(final ApplyCouponsDTO applycouponDto, final AbstractOrderModel cartModel);
	public ApplyCartCouponsDTO setTotalPrice(ApplyCartCouponsDTO applycouponDto, AbstractOrderModel cartModel);
	public boolean updateWallet(CustomerModel customer , String otp ,MplCustomerProfileData customerToSave);



}
