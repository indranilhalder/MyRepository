/**
 *
 */
package com.tisl.mpl.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.core.model.BankforNetbankingModel;
import com.tisl.mpl.core.model.EMIBankModel;
import com.tisl.mpl.core.model.EMITermRowModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MplPaymentDao;
import com.tisl.mpl.marketplacecommerceservices.service.impl.MplPaymentServiceImpl;
import com.tisl.mpl.model.BankModel;
import com.tisl.mpl.model.PaymentTypeModel;


/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "unused", "deprecation" })
public class MplPaymentServiceImplUnitTest
{
	private MplPaymentServiceImpl mplPaymentServiceImpl;
	private MplPaymentDao mplPaymentDao;
	private PaymentTypeModel paymentType;
	private BankforNetbankingModel bankForNB;

	private EMIBankModel emiBankModel;
	private BankModel bank;
	private EMITermRowModel emiTermRowModel;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		this.mplPaymentServiceImpl = new MplPaymentServiceImpl();
		this.mplPaymentDao = Mockito.mock(MplPaymentDao.class);
		this.mplPaymentServiceImpl.setMplPaymentDao(mplPaymentDao);
		this.paymentType = Mockito.mock(PaymentTypeModel.class);
		this.bankForNB = Mockito.mock(BankforNetbankingModel.class);
		this.emiBankModel = Mockito.mock(EMIBankModel.class);
		this.bank = Mockito.mock(BankModel.class);
		this.emiTermRowModel = Mockito.mock(EMITermRowModel.class);
	}


	@Test
	public void testGetPaymentModes()
	{
		final List<PaymentTypeModel> paymentTypeList = Arrays.asList(paymentType);
		Mockito.when(mplPaymentDao.getPaymentTypes("mpl")).thenReturn(paymentTypeList);
		final List<PaymentTypeModel> actual = mplPaymentServiceImpl.getPaymentModes("mpl");
		Assert.assertEquals(actual, paymentTypeList);
	}


	@Test
	public void testGetBanksByPriority()
	{
		final List<BankforNetbankingModel> bankForNBList = Arrays.asList(bankForNB);
		Mockito.when(mplPaymentDao.getBanksByPriority()).thenReturn(bankForNBList);
		final List<BankforNetbankingModel> actual = mplPaymentServiceImpl.getBanksByPriority();
		Assert.assertEquals(actual, bankForNBList);
	}


	@Test
	public void testGetOtherBanks()
	{
		final List<BankforNetbankingModel> bankForNBList = Arrays.asList(bankForNB);
		Mockito.when(mplPaymentDao.getOtherBanks()).thenReturn(bankForNBList);
		final List<BankforNetbankingModel> actual = mplPaymentServiceImpl.getOtherBanks();
		Assert.assertEquals(actual, bankForNBList);
	}


	//	@Test
	//	public void testSaveCODPaymentInfo()
	//	{
	//		Mockito.when(userService.getCurrentUser()).thenReturn(userModel);
	//		Mockito.doNothing().when(modelService).save(codPaymentInfoModel);
	//		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);
	//		Mockito.doNothing().when(modelService).save(cartModel);
	//		mplPaymentServiceImpl.saveCODPaymentInfo("Test Name");
	//	}


	@Test
	public void testGetEMIBanks()
	{
		final List<EMIBankModel> emiBankList = Arrays.asList(emiBankModel);
		Mockito.when(mplPaymentDao.getEMIBanks(Double.valueOf(100))).thenReturn(emiBankList);
		Mockito.when(emiBankModel.getName()).thenReturn(bank);
		Mockito.when(bank.getBankName()).thenReturn("HDFC");
		mplPaymentServiceImpl.getEMIBanks(Double.valueOf(100));
	}


	@Test
	public void testGetBankTerms()
	{
		final List<EMIBankModel> emiTermList = Arrays.asList(emiBankModel);
		Mockito.when(mplPaymentDao.getEMIBankTerms("HDFC")).thenReturn(emiTermList);
		final Collection<EMITermRowModel> emiTerms = Arrays.asList(emiTermRowModel);
		Mockito.when(emiBankModel.getEMITermRates()).thenReturn(emiTerms);
		Mockito.when(emiTermRowModel.getTermInMonths()).thenReturn(Integer.valueOf(12));
		Mockito.when(emiTermRowModel.getInterestRate()).thenReturn(Double.valueOf(12));
		mplPaymentServiceImpl.getBankTerms("HDFC", Double.valueOf(1000));
	}
}
