/**
 *
 */
package com.tisl.mpl.facades.payment;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.core.model.BankforNetbankingModel;
import com.tisl.mpl.data.EMITermRateData;
import com.tisl.mpl.facades.payment.impl.MplPaymentFacadeImpl;
import com.tisl.mpl.marketplacecommerceservices.service.BlacklistService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.model.BankModel;
import com.tisl.mpl.model.PaymentTypeModel;


/**
 * @author TCS
 *
 */
public class MplPaymentFacadeImplUnitTest
{
	private MplPaymentFacadeImpl mplPaymentFacadeImpl;
	private MplPaymentService mplPaymentService;
	//private OTPGenericService otpGenericService;
	private BlacklistService blacklistService;
	private PaymentTypeModel paymentType;
	private ModelService modelService;
	private CartModel cartModel;
	private BankforNetbankingModel bankForNetbankingModel;
	private BankModel bank;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		this.mplPaymentFacadeImpl = new MplPaymentFacadeImpl();
		this.mplPaymentService = Mockito.mock(MplPaymentService.class);
		this.mplPaymentFacadeImpl.setMplPaymentService(mplPaymentService);
		//this.otpGenericService = Mockito.mock(OTPGenericService.class);
		//this.mplPaymentFacadeImpl.setOtpGenericService(otpGenericService);
		this.blacklistService = Mockito.mock(BlacklistService.class);
		this.mplPaymentFacadeImpl.setBlacklistService(blacklistService);
		this.paymentType = Mockito.mock(PaymentTypeModel.class);
		this.modelService = Mockito.mock(ModelService.class);
		this.mplPaymentFacadeImpl.setModelService(modelService);
		this.cartModel = Mockito.mock(CartModel.class);
		this.bankForNetbankingModel = Mockito.mock(BankforNetbankingModel.class);
		this.bank = Mockito.mock(BankModel.class);
	}


	@Test
	public void testGetPaymentModes()
	{
		final List<PaymentTypeModel> paymentTypeList = Arrays.asList(paymentType);
		Mockito.when(mplPaymentService.getPaymentModes("mpl")).thenReturn(paymentTypeList);
		Mockito.when(paymentType.getMode()).thenReturn("COD");
		Mockito.when(paymentType.getIsAvailable()).thenReturn(Boolean.TRUE);
		mplPaymentFacadeImpl.getPaymentModes("mpl");
	}


	//	@Test
	//	public void testGenerateOTPForCODWeb() throws InvalidKeyException, NoSuchAlgorithmException
	//	{
	//		Mockito.doNothing().when(otpGenericService).generateOTP("1234567890", "COD");
	//		mplPaymentFacadeImpl.generateOTPforCODWeb("1234567890");
	//	}


	//	@Test
	//	public void testValidateOTPForCODWeb()
	//	{
	//		Mockito.when(Boolean.valueOf(otpGenericService.validateOTP("1123456789", "123456", OTPTypeEnum.COD, 600000))).thenReturn(
	//				Boolean.TRUE);
	//		mplPaymentFacadeImpl.validateOTPforCODWeb("1123456789", "123456");
	//	}


	@Test
	public void testIsBlacklisted()
	{
		Mockito.when(Boolean.valueOf(blacklistService.blacklistedOrNot("1123456789"))).thenReturn(Boolean.TRUE);

		mplPaymentFacadeImpl.isBlackListed("1123456789", cartModel);
	}

	//	@Test
	//	public void testSaveCODPaymentInfo()
	//	{
	//		Mockito.when((CustomerModel) userService.getCurrentUser()).thenReturn(customerModel);
	//		Mockito.when(customerModel.getName()).thenReturn("Test Name");
	//		Mockito.doNothing().when(mplPaymentService).saveCODPaymentInfo("Test Name");
	//		mplPaymentFacadeImpl.saveCODPaymentInfo();
	//	}

	@Test
	public void testSaveCart()
	{
		Mockito.doNothing().when(modelService).save(cartModel);
		mplPaymentFacadeImpl.saveCart(cartModel);
	}

	@Test
	public void testGetBanksByPriority()
	{
		final List<BankforNetbankingModel> bankForNetbankingList = Arrays.asList(bankForNetbankingModel);
		Mockito.when(mplPaymentService.getBanksByPriority()).thenReturn(bankForNetbankingList);
		Mockito.when(bankForNetbankingModel.getName()).thenReturn(bank);
		Mockito.when(bank.getBankName()).thenReturn("HDFC Bank");
		Mockito.when(bankForNetbankingModel.getNbCode()).thenReturn("HDFB");
		mplPaymentFacadeImpl.getBanksByPriority();
	}

	//	@Test
	//	public void testGetEMIBankNames()
	//	{
	//		final List<String> emiBankList = new ArrayList<String>();
	//		Mockito.when(mplPaymentService.getEMIBanks(Double.valueOf(1500.00))).thenReturn(emiBankList);
	//		mplPaymentFacadeImpl.getEMIBankNames(Double.valueOf(1500.00));
	//	}


	@Test
	public void testGetBankTerms()
	{
		final List<EMITermRateData> emiTermRateList = new ArrayList<EMITermRateData>();
		Mockito.when(mplPaymentService.getBankTerms("HDFC", Double.valueOf(1500.00))).thenReturn(emiTermRateList);
		mplPaymentFacadeImpl.getBankTerms("HDFC", Double.valueOf(1500.00));
	}


	@Test
	public void testGetOtherBanks()
	{
		final List<BankforNetbankingModel> bankForNetbankingList = Arrays.asList(bankForNetbankingModel);
		Mockito.when(mplPaymentService.getOtherBanks()).thenReturn(bankForNetbankingList);
		Mockito.when(bankForNetbankingModel.getName()).thenReturn(bank);
		Mockito.when(bank.getBankName()).thenReturn("HDFC Bank");
		Mockito.when(bankForNetbankingModel.getNbCode()).thenReturn("HDFB");
		mplPaymentFacadeImpl.getBanksByPriority();
	}

}
