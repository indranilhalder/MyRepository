/**
 *
 */
package com.tisl.mpl.wsdto;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * @author TCS
 *
 */
@XStreamAlias("PaymentInfo")
public class PaymentInfoWsDTO
{
	private String BankCountry;
	private String BankState;
	private String BankCity;
	private String BankAddress;
	private String BankName;
	private String BankType;
	private String IFSCCode;
	private String BranchName;
	private String AccHolderName;
	private String AccNo;
	private String RecoAcc;
	private String MinorityIndicator;
	private String CheckDuplInv;
	private String PaymentMethodID;
	private String PaymentBlock;
	private String AlternatePayee;
	private String HouseBank;

	/**
	 * @return the bankCountry
	 */
	public String getBankCountry()
	{
		return BankCountry;
	}

	/**
	 * @param bankCountry
	 *           the bankCountry to set
	 */
	public void setBankCountry(final String bankCountry)
	{
		BankCountry = bankCountry;
	}

	/**
	 * @return the bankState
	 */
	public String getBankState()
	{
		return BankState;
	}

	/**
	 * @param bankState
	 *           the bankState to set
	 */
	public void setBankState(final String bankState)
	{
		BankState = bankState;
	}

	/**
	 * @return the bankCity
	 */
	public String getBankCity()
	{
		return BankCity;
	}

	/**
	 * @param bankCity
	 *           the bankCity to set
	 */
	public void setBankCity(final String bankCity)
	{
		BankCity = bankCity;
	}

	/**
	 * @return the bankAddress
	 */
	public String getBankAddress()
	{
		return BankAddress;
	}

	/**
	 * @param bankAddress
	 *           the bankAddress to set
	 */
	public void setBankAddress(final String bankAddress)
	{
		BankAddress = bankAddress;
	}

	/**
	 * @return the bankName
	 */
	public String getBankName()
	{
		return BankName;
	}

	/**
	 * @param bankName
	 *           the bankName to set
	 */
	public void setBankName(final String bankName)
	{
		BankName = bankName;
	}

	/**
	 * @return the bankType
	 */
	public String getBankType()
	{
		return BankType;
	}

	/**
	 * @param bankType
	 *           the bankType to set
	 */
	public void setBankType(final String bankType)
	{
		BankType = bankType;
	}

	/**
	 * @return the iFSCCode
	 */
	public String getIFSCCode()
	{
		return IFSCCode;
	}

	/**
	 * @param iFSCCode
	 *           the iFSCCode to set
	 */
	public void setIFSCCode(final String iFSCCode)
	{
		IFSCCode = iFSCCode;
	}

	/**
	 * @return the branchName
	 */
	public String getBranchName()
	{
		return BranchName;
	}

	/**
	 * @param branchName
	 *           the branchName to set
	 */
	public void setBranchName(final String branchName)
	{
		BranchName = branchName;
	}

	/**
	 * @return the accHolderName
	 */
	public String getAccHolderName()
	{
		return AccHolderName;
	}

	/**
	 * @param accHolderName
	 *           the accHolderName to set
	 */
	public void setAccHolderName(final String accHolderName)
	{
		AccHolderName = accHolderName;
	}

	/**
	 * @return the accNo
	 */
	public String getAccNo()
	{
		return AccNo;
	}

	/**
	 * @param accNo
	 *           the accNo to set
	 */
	public void setAccNo(final String accNo)
	{
		AccNo = accNo;
	}

	/**
	 * @return the recoAcc
	 */
	public String getRecoAcc()
	{
		return RecoAcc;
	}

	/**
	 * @param recoAcc
	 *           the recoAcc to set
	 */
	public void setRecoAcc(final String recoAcc)
	{
		RecoAcc = recoAcc;
	}

	/**
	 * @return the minorityIndicator
	 */
	public String getMinorityIndicator()
	{
		return MinorityIndicator;
	}

	/**
	 * @param minorityIndicator
	 *           the minorityIndicator to set
	 */
	public void setMinorityIndicator(final String minorityIndicator)
	{
		MinorityIndicator = minorityIndicator;
	}

	/**
	 * @return the checkDuplInv
	 */
	public String getCheckDuplInv()
	{
		return CheckDuplInv;
	}

	/**
	 * @param checkDuplInv
	 *           the checkDuplInv to set
	 */
	public void setCheckDuplInv(final String checkDuplInv)
	{
		CheckDuplInv = checkDuplInv;
	}

	/**
	 * @return the paymentMethodID
	 */
	public String getPaymentMethodID()
	{
		return PaymentMethodID;
	}

	/**
	 * @param paymentMethodID
	 *           the paymentMethodID to set
	 */
	public void setPaymentMethodID(final String paymentMethodID)
	{
		PaymentMethodID = paymentMethodID;
	}

	/**
	 * @return the paymentBlock
	 */
	public String getPaymentBlock()
	{
		return PaymentBlock;
	}

	/**
	 * @param paymentBlock
	 *           the paymentBlock to set
	 */
	public void setPaymentBlock(final String paymentBlock)
	{
		PaymentBlock = paymentBlock;
	}

	/**
	 * @return the alternatePayee
	 */
	public String getAlternatePayee()
	{
		return AlternatePayee;
	}

	/**
	 * @param alternatePayee
	 *           the alternatePayee to set
	 */
	public void setAlternatePayee(final String alternatePayee)
	{
		AlternatePayee = alternatePayee;
	}

	/**
	 * @return the houseBank
	 */
	public String getHouseBank()
	{
		return HouseBank;
	}

	/**
	 * @param houseBank
	 *           the houseBank to set
	 */
	public void setHouseBank(final String houseBank)
	{
		HouseBank = houseBank;
	}

}
