/**
 *
 */
package com.tisl.mpl.pojo.response;

/**
 * @author Rakesh Thodeti
 *
 */

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
{ "TrackData", "CardNumber", "CardPIN", "CardExpiry", "InvoiceNumber", "Amount", "BillAmount", "OriginalInvoiceNumber",
		"OriginalTransactionId", "OriginalBatchNumber", "OriginalApprovalCode", "OriginalAmount", "AddonCardNumber",
		"AddonCardTrackData", "TransferCardNumber", "MerchantName", "AdjustmentAmount", "OriginalCardNumber", "OriginalCardPin",
		"CardProgramID", "CorporateName", "Notes", "SettlementDate", "ActivationAmount", "ActivationCount",
		"CancelActivationAmount", "CancelActivationCount", "ReloadAmount", "ReloadCount", "CancelLoadAmount", "CancelLoadCount",
		"RedemptionAmount", "RedemptionCount", "CancelRedeemAmount", "CancelRedeemCount", "Expiry", "PurchaseOrderNumber",
		"PurchaseOrderValue", "DiscountPercentage", "DiscountAmount", "PaymentMode", "PaymentDetails", "BulkType",
		"ExternalCorporateId", "ExternalCardNumber", "MerchantOutletName", "MerchantOutletAddress1", "MerchantOutletAddress2",
		"MerchantOutletCity", "MerchantOutletState", "MerchantOutletPinCode", "MerchantOutletPhone", "MaskCard",
		"PrintMerchantCopy", "InvoiceNumberMandatory", "NumericUserPwd", "IntegerAmount", "Culture", "CurrencySymbol",
		"CurrencyPosition", "CurrencyDecimalDigits", "DisplayUnitForPoints", "ReceiptFooterLine1", "ReceiptFooterLine2",
		"ReceiptFooterLine3", "ReceiptFooterLine4", "Result", "TransferCardExpiry", "TransferCardBalance", "CardStatusId",
		"CardStatus", "CardCurrencySymbol", "ActivationDate", "SVRecentTransactions", "CardType", "TransferCardTrackData",
		"TransferCardPin", "CumulativeAmountSpent", "CurrencyConversionRate", "CurrencyConvertedAmount", "CardHolderName",
		"StoredValueUnitID", "XactionAmountConvertedValue", "StoredValueConvertedAmount", "PromotionalValue", "EarnedValue",
		"TransactionAmount", "PreviousBalance", "UpgradedCardProgramGroupName", "NewBatchNumber", "OriginalActivationAmount",
		"CardCreationType", "ActivationCode", "ActivationURL", "MerchantID", "ReloadableAmount", "Barcode", "CardProgramGroupType",
		"Customer", "ApiWebProperties", "ApprovalCode", "ResponseCode", "ResponseMessage", "TransactionId", "TransactionType",
		"ErrorCode", "ErrorDescription" })
public class QCInitializationResponse
{

	@JsonProperty("TrackData")
	private Object trackData;
	@JsonProperty("CardNumber")
	private Object cardNumber;
	@JsonProperty("CardPIN")
	private Object cardPIN;
	@JsonProperty("CardExpiry")
	private String cardExpiry;
	@JsonProperty("InvoiceNumber")
	private Object invoiceNumber;
	@JsonProperty("Amount")
	private Integer amount;
	@JsonProperty("BillAmount")
	private Integer billAmount;
	@JsonProperty("OriginalInvoiceNumber")
	private Object originalInvoiceNumber;
	@JsonProperty("OriginalTransactionId")
	private Integer originalTransactionId;
	@JsonProperty("OriginalBatchNumber")
	private Integer originalBatchNumber;
	@JsonProperty("OriginalApprovalCode")
	private Object originalApprovalCode;
	@JsonProperty("OriginalAmount")
	private Object originalAmount;
	@JsonProperty("AddonCardNumber")
	private Object addonCardNumber;
	@JsonProperty("AddonCardTrackData")
	private Object addonCardTrackData;
	@JsonProperty("TransferCardNumber")
	private Object transferCardNumber;
	@JsonProperty("MerchantName")
	private String merchantName;
	@JsonProperty("AdjustmentAmount")
	private Integer adjustmentAmount;
	@JsonProperty("OriginalCardNumber")
	private Object originalCardNumber;
	@JsonProperty("OriginalCardPin")
	private Object originalCardPin;
	@JsonProperty("CardProgramID")
	private Object cardProgramID;
	@JsonProperty("CorporateName")
	private Object corporateName;
	@JsonProperty("Notes")
	private String notes;
	@JsonProperty("SettlementDate")
	private String settlementDate;
	@JsonProperty("ActivationAmount")
	private Integer activationAmount;
	@JsonProperty("ActivationCount")
	private Integer activationCount;
	@JsonProperty("CancelActivationAmount")
	private Integer cancelActivationAmount;
	@JsonProperty("CancelActivationCount")
	private Integer cancelActivationCount;
	@JsonProperty("ReloadAmount")
	private Integer reloadAmount;
	@JsonProperty("ReloadCount")
	private Integer reloadCount;
	@JsonProperty("CancelLoadAmount")
	private Integer cancelLoadAmount;
	@JsonProperty("CancelLoadCount")
	private Integer cancelLoadCount;
	@JsonProperty("RedemptionAmount")
	private Integer redemptionAmount;
	@JsonProperty("RedemptionCount")
	private Integer redemptionCount;
	@JsonProperty("CancelRedeemAmount")
	private Integer cancelRedeemAmount;
	@JsonProperty("CancelRedeemCount")
	private Integer cancelRedeemCount;
	@JsonProperty("Expiry")
	private String expiry;
	@JsonProperty("PurchaseOrderNumber")
	private Object purchaseOrderNumber;
	@JsonProperty("PurchaseOrderValue")
	private Integer purchaseOrderValue;
	@JsonProperty("DiscountPercentage")
	private Integer discountPercentage;
	@JsonProperty("DiscountAmount")
	private Integer discountAmount;
	@JsonProperty("PaymentMode")
	private Integer paymentMode;
	@JsonProperty("PaymentDetails")
	private Object paymentDetails;
	@JsonProperty("BulkType")
	private Boolean bulkType;
	@JsonProperty("ExternalCorporateId")
	private Object externalCorporateId;
	@JsonProperty("ExternalCardNumber")
	private Object externalCardNumber;
	@JsonProperty("MerchantOutletName")
	private String merchantOutletName;
	@JsonProperty("MerchantOutletAddress1")
	private String merchantOutletAddress1;
	@JsonProperty("MerchantOutletAddress2")
	private String merchantOutletAddress2;
	@JsonProperty("MerchantOutletCity")
	private String merchantOutletCity;
	@JsonProperty("MerchantOutletState")
	private String merchantOutletState;
	@JsonProperty("MerchantOutletPinCode")
	private String merchantOutletPinCode;
	@JsonProperty("MerchantOutletPhone")
	private String merchantOutletPhone;
	@JsonProperty("MaskCard")
	private String maskCard;
	@JsonProperty("PrintMerchantCopy")
	private String printMerchantCopy;
	@JsonProperty("InvoiceNumberMandatory")
	private String invoiceNumberMandatory;
	@JsonProperty("NumericUserPwd")
	private String numericUserPwd;
	@JsonProperty("IntegerAmount")
	private String integerAmount;
	@JsonProperty("Culture")
	private String culture;
	@JsonProperty("CurrencySymbol")
	private String currencySymbol;
	@JsonProperty("CurrencyPosition")
	private String currencyPosition;
	@JsonProperty("CurrencyDecimalDigits")
	private String currencyDecimalDigits;
	@JsonProperty("DisplayUnitForPoints")
	private String displayUnitForPoints;
	@JsonProperty("ReceiptFooterLine1")
	private Object receiptFooterLine1;
	@JsonProperty("ReceiptFooterLine2")
	private Object receiptFooterLine2;
	@JsonProperty("ReceiptFooterLine3")
	private Object receiptFooterLine3;
	@JsonProperty("ReceiptFooterLine4")
	private Object receiptFooterLine4;
	@JsonProperty("Result")
	private Boolean result;
	@JsonProperty("TransferCardExpiry")
	private String transferCardExpiry;
	@JsonProperty("TransferCardBalance")
	private Integer transferCardBalance;
	@JsonProperty("CardStatusId")
	private Integer cardStatusId;
	@JsonProperty("CardStatus")
	private Object cardStatus;
	@JsonProperty("CardCurrencySymbol")
	private Object cardCurrencySymbol;
	@JsonProperty("ActivationDate")
	private String activationDate;
	@JsonProperty("SVRecentTransactions")
	private Object sVRecentTransactions;
	@JsonProperty("CardType")
	private Object cardType;
	@JsonProperty("TransferCardTrackData")
	private Object transferCardTrackData;
	@JsonProperty("TransferCardPin")
	private Object transferCardPin;
	@JsonProperty("CumulativeAmountSpent")
	private Integer cumulativeAmountSpent;
	@JsonProperty("CurrencyConversionRate")
	private Integer currencyConversionRate;
	@JsonProperty("CurrencyConvertedAmount")
	private Integer currencyConvertedAmount;
	@JsonProperty("CardHolderName")
	private Object cardHolderName;
	@JsonProperty("StoredValueUnitID")
	private Integer storedValueUnitID;
	@JsonProperty("XactionAmountConvertedValue")
	private Integer xactionAmountConvertedValue;
	@JsonProperty("StoredValueConvertedAmount")
	private Integer storedValueConvertedAmount;
	@JsonProperty("PromotionalValue")
	private Integer promotionalValue;
	@JsonProperty("EarnedValue")
	private Integer earnedValue;
	@JsonProperty("TransactionAmount")
	private Integer transactionAmount;
	@JsonProperty("PreviousBalance")
	private Integer previousBalance;
	@JsonProperty("UpgradedCardProgramGroupName")
	private Object upgradedCardProgramGroupName;
	@JsonProperty("NewBatchNumber")
	private Integer newBatchNumber;
	@JsonProperty("OriginalActivationAmount")
	private Integer originalActivationAmount;
	@JsonProperty("CardCreationType")
	private Object cardCreationType;
	@JsonProperty("ActivationCode")
	private Object activationCode;
	@JsonProperty("ActivationURL")
	private Object activationURL;
	@JsonProperty("MerchantID")
	private Integer merchantID;
	@JsonProperty("ReloadableAmount")
	private Integer reloadableAmount;
	@JsonProperty("Barcode")
	private Object barcode;
	@JsonProperty("CardProgramGroupType")
	private Object cardProgramGroupType;
	@JsonProperty("Customer")
	private Object customer;
	@JsonProperty("ApiWebProperties")
	private ApiWebProperties apiWebProperties;
	@JsonProperty("ApprovalCode")
	private String approvalCode;
	@JsonProperty("ResponseCode")
	private Integer responseCode;
	@JsonProperty("ResponseMessage")
	private String responseMessage;
	@JsonProperty("TransactionId")
	private Integer transactionId;
	@JsonProperty("TransactionType")
	private String transactionType;
	@JsonProperty("ErrorCode")
	private Object errorCode;
	@JsonProperty("ErrorDescription")
	private Object errorDescription;
	@JsonIgnore
	private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("TrackData")
	public Object getTrackData()
	{
		return trackData;
	}

	@JsonProperty("TrackData")
	public void setTrackData(final Object trackData)
	{
		this.trackData = trackData;
	}

	@JsonProperty("CardNumber")
	public Object getCardNumber()
	{
		return cardNumber;
	}

	@JsonProperty("CardNumber")
	public void setCardNumber(final Object cardNumber)
	{
		this.cardNumber = cardNumber;
	}

	@JsonProperty("CardPIN")
	public Object getCardPIN()
	{
		return cardPIN;
	}

	@JsonProperty("CardPIN")
	public void setCardPIN(final Object cardPIN)
	{
		this.cardPIN = cardPIN;
	}

	@JsonProperty("CardExpiry")
	public String getCardExpiry()
	{
		return cardExpiry;
	}

	@JsonProperty("CardExpiry")
	public void setCardExpiry(final String cardExpiry)
	{
		this.cardExpiry = cardExpiry;
	}

	@JsonProperty("InvoiceNumber")
	public Object getInvoiceNumber()
	{
		return invoiceNumber;
	}

	@JsonProperty("InvoiceNumber")
	public void setInvoiceNumber(final Object invoiceNumber)
	{
		this.invoiceNumber = invoiceNumber;
	}

	@JsonProperty("Amount")
	public Integer getAmount()
	{
		return amount;
	}

	@JsonProperty("Amount")
	public void setAmount(final Integer amount)
	{
		this.amount = amount;
	}

	@JsonProperty("BillAmount")
	public Integer getBillAmount()
	{
		return billAmount;
	}

	@JsonProperty("BillAmount")
	public void setBillAmount(final Integer billAmount)
	{
		this.billAmount = billAmount;
	}

	@JsonProperty("OriginalInvoiceNumber")
	public Object getOriginalInvoiceNumber()
	{
		return originalInvoiceNumber;
	}

	@JsonProperty("OriginalInvoiceNumber")
	public void setOriginalInvoiceNumber(final Object originalInvoiceNumber)
	{
		this.originalInvoiceNumber = originalInvoiceNumber;
	}

	@JsonProperty("OriginalTransactionId")
	public Integer getOriginalTransactionId()
	{
		return originalTransactionId;
	}

	@JsonProperty("OriginalTransactionId")
	public void setOriginalTransactionId(final Integer originalTransactionId)
	{
		this.originalTransactionId = originalTransactionId;
	}

	@JsonProperty("OriginalBatchNumber")
	public Integer getOriginalBatchNumber()
	{
		return originalBatchNumber;
	}

	@JsonProperty("OriginalBatchNumber")
	public void setOriginalBatchNumber(final Integer originalBatchNumber)
	{
		this.originalBatchNumber = originalBatchNumber;
	}

	@JsonProperty("OriginalApprovalCode")
	public Object getOriginalApprovalCode()
	{
		return originalApprovalCode;
	}

	@JsonProperty("OriginalApprovalCode")
	public void setOriginalApprovalCode(final Object originalApprovalCode)
	{
		this.originalApprovalCode = originalApprovalCode;
	}

	@JsonProperty("OriginalAmount")
	public Object getOriginalAmount()
	{
		return originalAmount;
	}

	@JsonProperty("OriginalAmount")
	public void setOriginalAmount(final Object originalAmount)
	{
		this.originalAmount = originalAmount;
	}

	@JsonProperty("AddonCardNumber")
	public Object getAddonCardNumber()
	{
		return addonCardNumber;
	}

	@JsonProperty("AddonCardNumber")
	public void setAddonCardNumber(final Object addonCardNumber)
	{
		this.addonCardNumber = addonCardNumber;
	}

	@JsonProperty("AddonCardTrackData")
	public Object getAddonCardTrackData()
	{
		return addonCardTrackData;
	}

	@JsonProperty("AddonCardTrackData")
	public void setAddonCardTrackData(final Object addonCardTrackData)
	{
		this.addonCardTrackData = addonCardTrackData;
	}

	@JsonProperty("TransferCardNumber")
	public Object getTransferCardNumber()
	{
		return transferCardNumber;
	}

	@JsonProperty("TransferCardNumber")
	public void setTransferCardNumber(final Object transferCardNumber)
	{
		this.transferCardNumber = transferCardNumber;
	}

	@JsonProperty("MerchantName")
	public String getMerchantName()
	{
		return merchantName;
	}

	@JsonProperty("MerchantName")
	public void setMerchantName(final String merchantName)
	{
		this.merchantName = merchantName;
	}

	@JsonProperty("AdjustmentAmount")
	public Integer getAdjustmentAmount()
	{
		return adjustmentAmount;
	}

	@JsonProperty("AdjustmentAmount")
	public void setAdjustmentAmount(final Integer adjustmentAmount)
	{
		this.adjustmentAmount = adjustmentAmount;
	}

	@JsonProperty("OriginalCardNumber")
	public Object getOriginalCardNumber()
	{
		return originalCardNumber;
	}

	@JsonProperty("OriginalCardNumber")
	public void setOriginalCardNumber(final Object originalCardNumber)
	{
		this.originalCardNumber = originalCardNumber;
	}

	@JsonProperty("OriginalCardPin")
	public Object getOriginalCardPin()
	{
		return originalCardPin;
	}

	@JsonProperty("OriginalCardPin")
	public void setOriginalCardPin(final Object originalCardPin)
	{
		this.originalCardPin = originalCardPin;
	}

	@JsonProperty("CardProgramID")
	public Object getCardProgramID()
	{
		return cardProgramID;
	}

	@JsonProperty("CardProgramID")
	public void setCardProgramID(final Object cardProgramID)
	{
		this.cardProgramID = cardProgramID;
	}

	@JsonProperty("CorporateName")
	public Object getCorporateName()
	{
		return corporateName;
	}

	@JsonProperty("CorporateName")
	public void setCorporateName(final Object corporateName)
	{
		this.corporateName = corporateName;
	}

	@JsonProperty("Notes")
	public String getNotes()
	{
		return notes;
	}

	@JsonProperty("Notes")
	public void setNotes(final String notes)
	{
		this.notes = notes;
	}

	@JsonProperty("SettlementDate")
	public String getSettlementDate()
	{
		return settlementDate;
	}

	@JsonProperty("SettlementDate")
	public void setSettlementDate(final String settlementDate)
	{
		this.settlementDate = settlementDate;
	}

	@JsonProperty("ActivationAmount")
	public Integer getActivationAmount()
	{
		return activationAmount;
	}

	@JsonProperty("ActivationAmount")
	public void setActivationAmount(final Integer activationAmount)
	{
		this.activationAmount = activationAmount;
	}

	@JsonProperty("ActivationCount")
	public Integer getActivationCount()
	{
		return activationCount;
	}

	@JsonProperty("ActivationCount")
	public void setActivationCount(final Integer activationCount)
	{
		this.activationCount = activationCount;
	}

	@JsonProperty("CancelActivationAmount")
	public Integer getCancelActivationAmount()
	{
		return cancelActivationAmount;
	}

	@JsonProperty("CancelActivationAmount")
	public void setCancelActivationAmount(final Integer cancelActivationAmount)
	{
		this.cancelActivationAmount = cancelActivationAmount;
	}

	@JsonProperty("CancelActivationCount")
	public Integer getCancelActivationCount()
	{
		return cancelActivationCount;
	}

	@JsonProperty("CancelActivationCount")
	public void setCancelActivationCount(final Integer cancelActivationCount)
	{
		this.cancelActivationCount = cancelActivationCount;
	}

	@JsonProperty("ReloadAmount")
	public Integer getReloadAmount()
	{
		return reloadAmount;
	}

	@JsonProperty("ReloadAmount")
	public void setReloadAmount(final Integer reloadAmount)
	{
		this.reloadAmount = reloadAmount;
	}

	@JsonProperty("ReloadCount")
	public Integer getReloadCount()
	{
		return reloadCount;
	}

	@JsonProperty("ReloadCount")
	public void setReloadCount(final Integer reloadCount)
	{
		this.reloadCount = reloadCount;
	}

	@JsonProperty("CancelLoadAmount")
	public Integer getCancelLoadAmount()
	{
		return cancelLoadAmount;
	}

	@JsonProperty("CancelLoadAmount")
	public void setCancelLoadAmount(final Integer cancelLoadAmount)
	{
		this.cancelLoadAmount = cancelLoadAmount;
	}

	@JsonProperty("CancelLoadCount")
	public Integer getCancelLoadCount()
	{
		return cancelLoadCount;
	}

	@JsonProperty("CancelLoadCount")
	public void setCancelLoadCount(final Integer cancelLoadCount)
	{
		this.cancelLoadCount = cancelLoadCount;
	}

	@JsonProperty("RedemptionAmount")
	public Integer getRedemptionAmount()
	{
		return redemptionAmount;
	}

	@JsonProperty("RedemptionAmount")
	public void setRedemptionAmount(final Integer redemptionAmount)
	{
		this.redemptionAmount = redemptionAmount;
	}

	@JsonProperty("RedemptionCount")
	public Integer getRedemptionCount()
	{
		return redemptionCount;
	}

	@JsonProperty("RedemptionCount")
	public void setRedemptionCount(final Integer redemptionCount)
	{
		this.redemptionCount = redemptionCount;
	}

	@JsonProperty("CancelRedeemAmount")
	public Integer getCancelRedeemAmount()
	{
		return cancelRedeemAmount;
	}

	@JsonProperty("CancelRedeemAmount")
	public void setCancelRedeemAmount(final Integer cancelRedeemAmount)
	{
		this.cancelRedeemAmount = cancelRedeemAmount;
	}

	@JsonProperty("CancelRedeemCount")
	public Integer getCancelRedeemCount()
	{
		return cancelRedeemCount;
	}

	@JsonProperty("CancelRedeemCount")
	public void setCancelRedeemCount(final Integer cancelRedeemCount)
	{
		this.cancelRedeemCount = cancelRedeemCount;
	}

	@JsonProperty("Expiry")
	public String getExpiry()
	{
		return expiry;
	}

	@JsonProperty("Expiry")
	public void setExpiry(final String expiry)
	{
		this.expiry = expiry;
	}

	@JsonProperty("PurchaseOrderNumber")
	public Object getPurchaseOrderNumber()
	{
		return purchaseOrderNumber;
	}

	@JsonProperty("PurchaseOrderNumber")
	public void setPurchaseOrderNumber(final Object purchaseOrderNumber)
	{
		this.purchaseOrderNumber = purchaseOrderNumber;
	}

	@JsonProperty("PurchaseOrderValue")
	public Integer getPurchaseOrderValue()
	{
		return purchaseOrderValue;
	}

	@JsonProperty("PurchaseOrderValue")
	public void setPurchaseOrderValue(final Integer purchaseOrderValue)
	{
		this.purchaseOrderValue = purchaseOrderValue;
	}

	@JsonProperty("DiscountPercentage")
	public Integer getDiscountPercentage()
	{
		return discountPercentage;
	}

	@JsonProperty("DiscountPercentage")
	public void setDiscountPercentage(final Integer discountPercentage)
	{
		this.discountPercentage = discountPercentage;
	}

	@JsonProperty("DiscountAmount")
	public Integer getDiscountAmount()
	{
		return discountAmount;
	}

	@JsonProperty("DiscountAmount")
	public void setDiscountAmount(final Integer discountAmount)
	{
		this.discountAmount = discountAmount;
	}

	@JsonProperty("PaymentMode")
	public Integer getPaymentMode()
	{
		return paymentMode;
	}

	@JsonProperty("PaymentMode")
	public void setPaymentMode(final Integer paymentMode)
	{
		this.paymentMode = paymentMode;
	}

	@JsonProperty("PaymentDetails")
	public Object getPaymentDetails()
	{
		return paymentDetails;
	}

	@JsonProperty("PaymentDetails")
	public void setPaymentDetails(final Object paymentDetails)
	{
		this.paymentDetails = paymentDetails;
	}

	@JsonProperty("BulkType")
	public Boolean getBulkType()
	{
		return bulkType;
	}

	@JsonProperty("BulkType")
	public void setBulkType(final Boolean bulkType)
	{
		this.bulkType = bulkType;
	}

	@JsonProperty("ExternalCorporateId")
	public Object getExternalCorporateId()
	{
		return externalCorporateId;
	}

	@JsonProperty("ExternalCorporateId")
	public void setExternalCorporateId(final Object externalCorporateId)
	{
		this.externalCorporateId = externalCorporateId;
	}

	@JsonProperty("ExternalCardNumber")
	public Object getExternalCardNumber()
	{
		return externalCardNumber;
	}

	@JsonProperty("ExternalCardNumber")
	public void setExternalCardNumber(final Object externalCardNumber)
	{
		this.externalCardNumber = externalCardNumber;
	}

	@JsonProperty("MerchantOutletName")
	public String getMerchantOutletName()
	{
		return merchantOutletName;
	}

	@JsonProperty("MerchantOutletName")
	public void setMerchantOutletName(final String merchantOutletName)
	{
		this.merchantOutletName = merchantOutletName;
	}

	@JsonProperty("MerchantOutletAddress1")
	public String getMerchantOutletAddress1()
	{
		return merchantOutletAddress1;
	}

	@JsonProperty("MerchantOutletAddress1")
	public void setMerchantOutletAddress1(final String merchantOutletAddress1)
	{
		this.merchantOutletAddress1 = merchantOutletAddress1;
	}

	@JsonProperty("MerchantOutletAddress2")
	public String getMerchantOutletAddress2()
	{
		return merchantOutletAddress2;
	}

	@JsonProperty("MerchantOutletAddress2")
	public void setMerchantOutletAddress2(final String merchantOutletAddress2)
	{
		this.merchantOutletAddress2 = merchantOutletAddress2;
	}

	@JsonProperty("MerchantOutletCity")
	public String getMerchantOutletCity()
	{
		return merchantOutletCity;
	}

	@JsonProperty("MerchantOutletCity")
	public void setMerchantOutletCity(final String merchantOutletCity)
	{
		this.merchantOutletCity = merchantOutletCity;
	}

	@JsonProperty("MerchantOutletState")
	public String getMerchantOutletState()
	{
		return merchantOutletState;
	}

	@JsonProperty("MerchantOutletState")
	public void setMerchantOutletState(final String merchantOutletState)
	{
		this.merchantOutletState = merchantOutletState;
	}

	@JsonProperty("MerchantOutletPinCode")
	public String getMerchantOutletPinCode()
	{
		return merchantOutletPinCode;
	}

	@JsonProperty("MerchantOutletPinCode")
	public void setMerchantOutletPinCode(final String merchantOutletPinCode)
	{
		this.merchantOutletPinCode = merchantOutletPinCode;
	}

	@JsonProperty("MerchantOutletPhone")
	public String getMerchantOutletPhone()
	{
		return merchantOutletPhone;
	}

	@JsonProperty("MerchantOutletPhone")
	public void setMerchantOutletPhone(final String merchantOutletPhone)
	{
		this.merchantOutletPhone = merchantOutletPhone;
	}

	@JsonProperty("MaskCard")
	public String getMaskCard()
	{
		return maskCard;
	}

	@JsonProperty("MaskCard")
	public void setMaskCard(final String maskCard)
	{
		this.maskCard = maskCard;
	}

	@JsonProperty("PrintMerchantCopy")
	public String getPrintMerchantCopy()
	{
		return printMerchantCopy;
	}

	@JsonProperty("PrintMerchantCopy")
	public void setPrintMerchantCopy(final String printMerchantCopy)
	{
		this.printMerchantCopy = printMerchantCopy;
	}

	@JsonProperty("InvoiceNumberMandatory")
	public String getInvoiceNumberMandatory()
	{
		return invoiceNumberMandatory;
	}

	@JsonProperty("InvoiceNumberMandatory")
	public void setInvoiceNumberMandatory(final String invoiceNumberMandatory)
	{
		this.invoiceNumberMandatory = invoiceNumberMandatory;
	}

	@JsonProperty("NumericUserPwd")
	public String getNumericUserPwd()
	{
		return numericUserPwd;
	}

	@JsonProperty("NumericUserPwd")
	public void setNumericUserPwd(final String numericUserPwd)
	{
		this.numericUserPwd = numericUserPwd;
	}

	@JsonProperty("IntegerAmount")
	public String getIntegerAmount()
	{
		return integerAmount;
	}

	@JsonProperty("IntegerAmount")
	public void setIntegerAmount(final String integerAmount)
	{
		this.integerAmount = integerAmount;
	}

	@JsonProperty("Culture")
	public String getCulture()
	{
		return culture;
	}

	@JsonProperty("Culture")
	public void setCulture(final String culture)
	{
		this.culture = culture;
	}

	@JsonProperty("CurrencySymbol")
	public String getCurrencySymbol()
	{
		return currencySymbol;
	}

	@JsonProperty("CurrencySymbol")
	public void setCurrencySymbol(final String currencySymbol)
	{
		this.currencySymbol = currencySymbol;
	}

	@JsonProperty("CurrencyPosition")
	public String getCurrencyPosition()
	{
		return currencyPosition;
	}

	@JsonProperty("CurrencyPosition")
	public void setCurrencyPosition(final String currencyPosition)
	{
		this.currencyPosition = currencyPosition;
	}

	@JsonProperty("CurrencyDecimalDigits")
	public String getCurrencyDecimalDigits()
	{
		return currencyDecimalDigits;
	}

	@JsonProperty("CurrencyDecimalDigits")
	public void setCurrencyDecimalDigits(final String currencyDecimalDigits)
	{
		this.currencyDecimalDigits = currencyDecimalDigits;
	}

	@JsonProperty("DisplayUnitForPoints")
	public String getDisplayUnitForPoints()
	{
		return displayUnitForPoints;
	}

	@JsonProperty("DisplayUnitForPoints")
	public void setDisplayUnitForPoints(final String displayUnitForPoints)
	{
		this.displayUnitForPoints = displayUnitForPoints;
	}

	@JsonProperty("ReceiptFooterLine1")
	public Object getReceiptFooterLine1()
	{
		return receiptFooterLine1;
	}

	@JsonProperty("ReceiptFooterLine1")
	public void setReceiptFooterLine1(final Object receiptFooterLine1)
	{
		this.receiptFooterLine1 = receiptFooterLine1;
	}

	@JsonProperty("ReceiptFooterLine2")
	public Object getReceiptFooterLine2()
	{
		return receiptFooterLine2;
	}

	@JsonProperty("ReceiptFooterLine2")
	public void setReceiptFooterLine2(final Object receiptFooterLine2)
	{
		this.receiptFooterLine2 = receiptFooterLine2;
	}

	@JsonProperty("ReceiptFooterLine3")
	public Object getReceiptFooterLine3()
	{
		return receiptFooterLine3;
	}

	@JsonProperty("ReceiptFooterLine3")
	public void setReceiptFooterLine3(final Object receiptFooterLine3)
	{
		this.receiptFooterLine3 = receiptFooterLine3;
	}

	@JsonProperty("ReceiptFooterLine4")
	public Object getReceiptFooterLine4()
	{
		return receiptFooterLine4;
	}

	@JsonProperty("ReceiptFooterLine4")
	public void setReceiptFooterLine4(final Object receiptFooterLine4)
	{
		this.receiptFooterLine4 = receiptFooterLine4;
	}

	@JsonProperty("Result")
	public Boolean getResult()
	{
		return result;
	}

	@JsonProperty("Result")
	public void setResult(final Boolean result)
	{
		this.result = result;
	}

	@JsonProperty("TransferCardExpiry")
	public String getTransferCardExpiry()
	{
		return transferCardExpiry;
	}

	@JsonProperty("TransferCardExpiry")
	public void setTransferCardExpiry(final String transferCardExpiry)
	{
		this.transferCardExpiry = transferCardExpiry;
	}

	@JsonProperty("TransferCardBalance")
	public Integer getTransferCardBalance()
	{
		return transferCardBalance;
	}

	@JsonProperty("TransferCardBalance")
	public void setTransferCardBalance(final Integer transferCardBalance)
	{
		this.transferCardBalance = transferCardBalance;
	}

	@JsonProperty("CardStatusId")
	public Integer getCardStatusId()
	{
		return cardStatusId;
	}

	@JsonProperty("CardStatusId")
	public void setCardStatusId(final Integer cardStatusId)
	{
		this.cardStatusId = cardStatusId;
	}

	@JsonProperty("CardStatus")
	public Object getCardStatus()
	{
		return cardStatus;
	}

	@JsonProperty("CardStatus")
	public void setCardStatus(final Object cardStatus)
	{
		this.cardStatus = cardStatus;
	}

	@JsonProperty("CardCurrencySymbol")
	public Object getCardCurrencySymbol()
	{
		return cardCurrencySymbol;
	}

	@JsonProperty("CardCurrencySymbol")
	public void setCardCurrencySymbol(final Object cardCurrencySymbol)
	{
		this.cardCurrencySymbol = cardCurrencySymbol;
	}

	@JsonProperty("ActivationDate")
	public String getActivationDate()
	{
		return activationDate;
	}

	@JsonProperty("ActivationDate")
	public void setActivationDate(final String activationDate)
	{
		this.activationDate = activationDate;
	}

	@JsonProperty("SVRecentTransactions")
	public Object getSVRecentTransactions()
	{
		return sVRecentTransactions;
	}

	@JsonProperty("SVRecentTransactions")
	public void setSVRecentTransactions(final Object sVRecentTransactions)
	{
		this.sVRecentTransactions = sVRecentTransactions;
	}

	@JsonProperty("CardType")
	public Object getCardType()
	{
		return cardType;
	}

	@JsonProperty("CardType")
	public void setCardType(final Object cardType)
	{
		this.cardType = cardType;
	}

	@JsonProperty("TransferCardTrackData")
	public Object getTransferCardTrackData()
	{
		return transferCardTrackData;
	}

	@JsonProperty("TransferCardTrackData")
	public void setTransferCardTrackData(final Object transferCardTrackData)
	{
		this.transferCardTrackData = transferCardTrackData;
	}

	@JsonProperty("TransferCardPin")
	public Object getTransferCardPin()
	{
		return transferCardPin;
	}

	@JsonProperty("TransferCardPin")
	public void setTransferCardPin(final Object transferCardPin)
	{
		this.transferCardPin = transferCardPin;
	}

	@JsonProperty("CumulativeAmountSpent")
	public Integer getCumulativeAmountSpent()
	{
		return cumulativeAmountSpent;
	}

	@JsonProperty("CumulativeAmountSpent")
	public void setCumulativeAmountSpent(final Integer cumulativeAmountSpent)
	{
		this.cumulativeAmountSpent = cumulativeAmountSpent;
	}

	@JsonProperty("CurrencyConversionRate")
	public Integer getCurrencyConversionRate()
	{
		return currencyConversionRate;
	}

	@JsonProperty("CurrencyConversionRate")
	public void setCurrencyConversionRate(final Integer currencyConversionRate)
	{
		this.currencyConversionRate = currencyConversionRate;
	}

	@JsonProperty("CurrencyConvertedAmount")
	public Integer getCurrencyConvertedAmount()
	{
		return currencyConvertedAmount;
	}

	@JsonProperty("CurrencyConvertedAmount")
	public void setCurrencyConvertedAmount(final Integer currencyConvertedAmount)
	{
		this.currencyConvertedAmount = currencyConvertedAmount;
	}

	@JsonProperty("CardHolderName")
	public Object getCardHolderName()
	{
		return cardHolderName;
	}

	@JsonProperty("CardHolderName")
	public void setCardHolderName(final Object cardHolderName)
	{
		this.cardHolderName = cardHolderName;
	}

	@JsonProperty("StoredValueUnitID")
	public Integer getStoredValueUnitID()
	{
		return storedValueUnitID;
	}

	@JsonProperty("StoredValueUnitID")
	public void setStoredValueUnitID(final Integer storedValueUnitID)
	{
		this.storedValueUnitID = storedValueUnitID;
	}

	@JsonProperty("XactionAmountConvertedValue")
	public Integer getXactionAmountConvertedValue()
	{
		return xactionAmountConvertedValue;
	}

	@JsonProperty("XactionAmountConvertedValue")
	public void setXactionAmountConvertedValue(final Integer xactionAmountConvertedValue)
	{
		this.xactionAmountConvertedValue = xactionAmountConvertedValue;
	}

	@JsonProperty("StoredValueConvertedAmount")
	public Integer getStoredValueConvertedAmount()
	{
		return storedValueConvertedAmount;
	}

	@JsonProperty("StoredValueConvertedAmount")
	public void setStoredValueConvertedAmount(final Integer storedValueConvertedAmount)
	{
		this.storedValueConvertedAmount = storedValueConvertedAmount;
	}

	@JsonProperty("PromotionalValue")
	public Integer getPromotionalValue()
	{
		return promotionalValue;
	}

	@JsonProperty("PromotionalValue")
	public void setPromotionalValue(final Integer promotionalValue)
	{
		this.promotionalValue = promotionalValue;
	}

	@JsonProperty("EarnedValue")
	public Integer getEarnedValue()
	{
		return earnedValue;
	}

	@JsonProperty("EarnedValue")
	public void setEarnedValue(final Integer earnedValue)
	{
		this.earnedValue = earnedValue;
	}

	@JsonProperty("TransactionAmount")
	public Integer getTransactionAmount()
	{
		return transactionAmount;
	}

	@JsonProperty("TransactionAmount")
	public void setTransactionAmount(final Integer transactionAmount)
	{
		this.transactionAmount = transactionAmount;
	}

	@JsonProperty("PreviousBalance")
	public Integer getPreviousBalance()
	{
		return previousBalance;
	}

	@JsonProperty("PreviousBalance")
	public void setPreviousBalance(final Integer previousBalance)
	{
		this.previousBalance = previousBalance;
	}

	@JsonProperty("UpgradedCardProgramGroupName")
	public Object getUpgradedCardProgramGroupName()
	{
		return upgradedCardProgramGroupName;
	}

	@JsonProperty("UpgradedCardProgramGroupName")
	public void setUpgradedCardProgramGroupName(final Object upgradedCardProgramGroupName)
	{
		this.upgradedCardProgramGroupName = upgradedCardProgramGroupName;
	}

	@JsonProperty("NewBatchNumber")
	public Integer getNewBatchNumber()
	{
		return newBatchNumber;
	}

	@JsonProperty("NewBatchNumber")
	public void setNewBatchNumber(final Integer newBatchNumber)
	{
		this.newBatchNumber = newBatchNumber;
	}

	@JsonProperty("OriginalActivationAmount")
	public Integer getOriginalActivationAmount()
	{
		return originalActivationAmount;
	}

	@JsonProperty("OriginalActivationAmount")
	public void setOriginalActivationAmount(final Integer originalActivationAmount)
	{
		this.originalActivationAmount = originalActivationAmount;
	}

	@JsonProperty("CardCreationType")
	public Object getCardCreationType()
	{
		return cardCreationType;
	}

	@JsonProperty("CardCreationType")
	public void setCardCreationType(final Object cardCreationType)
	{
		this.cardCreationType = cardCreationType;
	}

	@JsonProperty("ActivationCode")
	public Object getActivationCode()
	{
		return activationCode;
	}

	@JsonProperty("ActivationCode")
	public void setActivationCode(final Object activationCode)
	{
		this.activationCode = activationCode;
	}

	@JsonProperty("ActivationURL")
	public Object getActivationURL()
	{
		return activationURL;
	}

	@JsonProperty("ActivationURL")
	public void setActivationURL(final Object activationURL)
	{
		this.activationURL = activationURL;
	}

	@JsonProperty("MerchantID")
	public Integer getMerchantID()
	{
		return merchantID;
	}

	@JsonProperty("MerchantID")
	public void setMerchantID(final Integer merchantID)
	{
		this.merchantID = merchantID;
	}

	@JsonProperty("ReloadableAmount")
	public Integer getReloadableAmount()
	{
		return reloadableAmount;
	}

	@JsonProperty("ReloadableAmount")
	public void setReloadableAmount(final Integer reloadableAmount)
	{
		this.reloadableAmount = reloadableAmount;
	}

	@JsonProperty("Barcode")
	public Object getBarcode()
	{
		return barcode;
	}

	@JsonProperty("Barcode")
	public void setBarcode(final Object barcode)
	{
		this.barcode = barcode;
	}

	@JsonProperty("CardProgramGroupType")
	public Object getCardProgramGroupType()
	{
		return cardProgramGroupType;
	}

	@JsonProperty("CardProgramGroupType")
	public void setCardProgramGroupType(final Object cardProgramGroupType)
	{
		this.cardProgramGroupType = cardProgramGroupType;
	}

	@JsonProperty("Customer")
	public Object getCustomer()
	{
		return customer;
	}

	@JsonProperty("Customer")
	public void setCustomer(final Object customer)
	{
		this.customer = customer;
	}

	@JsonProperty("ApiWebProperties")
	public ApiWebProperties getApiWebProperties()
	{
		return apiWebProperties;
	}

	@JsonProperty("ApiWebProperties")
	public void setApiWebProperties(final ApiWebProperties apiWebProperties)
	{
		this.apiWebProperties = apiWebProperties;
	}

	@JsonProperty("ApprovalCode")
	public String getApprovalCode()
	{
		return approvalCode;
	}

	@JsonProperty("ApprovalCode")
	public void setApprovalCode(final String approvalCode)
	{
		this.approvalCode = approvalCode;
	}

	@JsonProperty("ResponseCode")
	public Integer getResponseCode()
	{
		return responseCode;
	}

	@JsonProperty("ResponseCode")
	public void setResponseCode(final Integer responseCode)
	{
		this.responseCode = responseCode;
	}

	@JsonProperty("ResponseMessage")
	public String getResponseMessage()
	{
		return responseMessage;
	}

	@JsonProperty("ResponseMessage")
	public void setResponseMessage(final String responseMessage)
	{
		this.responseMessage = responseMessage;
	}

	@JsonProperty("TransactionId")
	public Integer getTransactionId()
	{
		return transactionId;
	}

	@JsonProperty("TransactionId")
	public void setTransactionId(final Integer transactionId)
	{
		this.transactionId = transactionId;
	}

	@JsonProperty("TransactionType")
	public String getTransactionType()
	{
		return transactionType;
	}

	@JsonProperty("TransactionType")
	public void setTransactionType(final String transactionType)
	{
		this.transactionType = transactionType;
	}

	@JsonProperty("ErrorCode")
	public Object getErrorCode()
	{
		return errorCode;
	}

	@JsonProperty("ErrorCode")
	public void setErrorCode(final Object errorCode)
	{
		this.errorCode = errorCode;
	}

	@JsonProperty("ErrorDescription")
	public Object getErrorDescription()
	{
		return errorDescription;
	}

	@JsonProperty("ErrorDescription")
	public void setErrorDescription(final Object errorDescription)
	{
		this.errorDescription = errorDescription;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties()
	{
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(final String name, final Object value)
	{
		this.additionalProperties.put(name, value);
	}

}