/**
 *
 */
package com.tisl.mpl.pojo.response;

/**
 * @author TUL
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
public class PurchaseEGVResponse
{

	@JsonProperty("TrackData")
	private String trackData;
	@JsonProperty("CardNumber")
	private String cardNumber;
	@JsonProperty("CardPIN")
	private String cardPIN;
	@JsonProperty("CardExpiry")
	private String cardExpiry;
	@JsonProperty("InvoiceNumber")
	private String invoiceNumber;
	@JsonProperty("Amount")
	private Double amount;
	@JsonProperty("BillAmount")
	private Double billAmount;
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
	private String transferCardNumber;
	@JsonProperty("MerchantName")
	private Object merchantName;
	@JsonProperty("AdjustmentAmount")
	private Double adjustmentAmount;
	@JsonProperty("OriginalCardNumber")
	private Object originalCardNumber;
	@JsonProperty("OriginalCardPin")
	private Object originalCardPin;
	@JsonProperty("CardProgramID")
	private Object cardProgramID;
	@JsonProperty("CorporateName")
	private String corporateName;
	@JsonProperty("Notes")
	private String notes;
	@JsonProperty("SettlementDate")
	private String settlementDate;
	@JsonProperty("ActivationAmount")
	private Double activationAmount;
	@JsonProperty("ActivationCount")
	private Double activationCount;
	@JsonProperty("CancelActivationAmount")
	private Double cancelActivationAmount;
	@JsonProperty("CancelActivationCount")
	private Double cancelActivationCount;
	@JsonProperty("ReloadAmount")
	private Double reloadAmount;
	@JsonProperty("ReloadCount")
	private Double reloadCount;
	@JsonProperty("CancelLoadAmount")
	private Double cancelLoadAmount;
	@JsonProperty("CancelLoadCount")
	private Double cancelLoadCount;
	@JsonProperty("RedemptionAmount")
	private Double redemptionAmount;
	@JsonProperty("RedemptionCount")
	private Double redemptionCount;
	@JsonProperty("CancelRedeemAmount")
	private Double cancelRedeemAmount;
	@JsonProperty("CancelRedeemCount")
	private Double cancelRedeemCount;
	@JsonProperty("Expiry")
	private String expiry;
	@JsonProperty("PurchaseOrderNumber")
	private Object purchaseOrderNumber;
	@JsonProperty("PurchaseOrderValue")
	private Double purchaseOrderValue;
	@JsonProperty("DiscountPercentage")
	private Double discountPercentage;
	@JsonProperty("DiscountAmount")
	private Double discountAmount;
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
	private Object merchantOutletName;
	@JsonProperty("MerchantOutletAddress1")
	private Object merchantOutletAddress1;
	@JsonProperty("MerchantOutletAddress2")
	private Object merchantOutletAddress2;
	@JsonProperty("MerchantOutletCity")
	private Object merchantOutletCity;
	@JsonProperty("MerchantOutletState")
	private Object merchantOutletState;
	@JsonProperty("MerchantOutletPinCode")
	private Object merchantOutletPinCode;
	@JsonProperty("MerchantOutletPhone")
	private Object merchantOutletPhone;
	@JsonProperty("MaskCard")
	private Object maskCard;
	@JsonProperty("PrintMerchantCopy")
	private Object printMerchantCopy;
	@JsonProperty("InvoiceNumberMandatory")
	private Object invoiceNumberMandatory;
	@JsonProperty("NumericUserPwd")
	private Object numericUserPwd;
	@JsonProperty("IntegerAmount")
	private Object integerAmount;
	@JsonProperty("Culture")
	private Object culture;
	@JsonProperty("CurrencySymbol")
	private Object currencySymbol;
	@JsonProperty("CurrencyPosition")
	private Object currencyPosition;
	@JsonProperty("CurrencyDecimalDigits")
	private Object currencyDecimalDigits;
	@JsonProperty("DisplayUnitForPoints")
	private Object displayUnitForPoints;
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
	private Double transferCardBalance;
	@JsonProperty("CardStatusId")
	private Integer cardStatusId;
	@JsonProperty("CardStatus")
	private String cardStatus;
	@JsonProperty("CardCurrencySymbol")
	private String cardCurrencySymbol;
	@JsonProperty("ActivationDate")
	private String activationDate;
	@JsonProperty("SVRecentTransactions")
	private Object sVRecentTransactions;
	@JsonProperty("CardType")
	private String cardType;
	@JsonProperty("TransferCardTrackData")
	private Object transferCardTrackData;
	@JsonProperty("TransferCardPin")
	private Object transferCardPin;
	@JsonProperty("CumulativeAmountSpent")
	private Double cumulativeAmountSpent;
	@JsonProperty("CurrencyConversionRate")
	private Double currencyConversionRate;
	@JsonProperty("CurrencyConvertedAmount")
	private Double currencyConvertedAmount;
	@JsonProperty("CardHolderName")
	private String cardHolderName;
	@JsonProperty("StoredValueUnitID")
	private Integer storedValueUnitID;
	@JsonProperty("XactionAmountConvertedValue")
	private Integer xactionAmountConvertedValue;
	@JsonProperty("StoredValueConvertedAmount")
	private Integer storedValueConvertedAmount;
	@JsonProperty("PromotionalValue")
	private Double promotionalValue;
	@JsonProperty("EarnedValue")
	private Double earnedValue;
	@JsonProperty("TransactionAmount")
	private Double transactionAmount;
	@JsonProperty("PreviousBalance")
	private Double previousBalance;
	@JsonProperty("UpgradedCardProgramGroupName")
	private Object upgradedCardProgramGroupName;
	@JsonProperty("NewBatchNumber")
	private Integer newBatchNumber;
	@JsonProperty("OriginalActivationAmount")
	private Double originalActivationAmount;
	@JsonProperty("CardCreationType")
	private String cardCreationType;
	@JsonProperty("ActivationCode")
	private Object activationCode;
	@JsonProperty("ActivationURL")
	private Object activationURL;
	@JsonProperty("MerchantID")
	private Integer merchantID;
	@JsonProperty("ReloadableAmount")
	private Double reloadableAmount;
	@JsonProperty("Barcode")
	private Object barcode;
	@JsonProperty("CardProgramGroupType")
	private String cardProgramGroupType;
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
	public String getTrackData()
	{
		return trackData;
	}

	@JsonProperty("TrackData")
	public void setTrackData(final String trackData)
	{
		this.trackData = trackData;
	}

	@JsonProperty("CardNumber")
	public String getCardNumber()
	{
		return cardNumber;
	}

	@JsonProperty("CardNumber")
	public void setCardNumber(final String cardNumber)
	{
		this.cardNumber = cardNumber;
	}

	@JsonProperty("CardPIN")
	public String getCardPIN()
	{
		return cardPIN;
	}

	@JsonProperty("CardPIN")
	public void setCardPIN(final String cardPIN)
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
	public String getInvoiceNumber()
	{
		return invoiceNumber;
	}

	@JsonProperty("InvoiceNumber")
	public void setInvoiceNumber(final String invoiceNumber)
	{
		this.invoiceNumber = invoiceNumber;
	}

	@JsonProperty("Amount")
	public Double getAmount()
	{
		return amount;
	}

	@JsonProperty("Amount")
	public void setAmount(final Double amount)
	{
		this.amount = amount;
	}

	@JsonProperty("BillAmount")
	public Double getBillAmount()
	{
		return billAmount;
	}

	@JsonProperty("BillAmount")
	public void setBillAmount(final Double billAmount)
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
	public String getTransferCardNumber()
	{
		return transferCardNumber;
	}

	@JsonProperty("TransferCardNumber")
	public void setTransferCardNumber(final String transferCardNumber)
	{
		this.transferCardNumber = transferCardNumber;
	}

	@JsonProperty("MerchantName")
	public Object getMerchantName()
	{
		return merchantName;
	}

	@JsonProperty("MerchantName")
	public void setMerchantName(final Object merchantName)
	{
		this.merchantName = merchantName;
	}

	@JsonProperty("AdjustmentAmount")
	public Double getAdjustmentAmount()
	{
		return adjustmentAmount;
	}

	@JsonProperty("AdjustmentAmount")
	public void setAdjustmentAmount(final Double adjustmentAmount)
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
	public String getCorporateName()
	{
		return corporateName;
	}

	@JsonProperty("CorporateName")
	public void setCorporateName(final String corporateName)
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
	public Double getActivationAmount()
	{
		return activationAmount;
	}

	@JsonProperty("ActivationAmount")
	public void setActivationAmount(final Double activationAmount)
	{
		this.activationAmount = activationAmount;
	}

	@JsonProperty("ActivationCount")
	public Double getActivationCount()
	{
		return activationCount;
	}

	@JsonProperty("ActivationCount")
	public void setActivationCount(final Double activationCount)
	{
		this.activationCount = activationCount;
	}

	@JsonProperty("CancelActivationAmount")
	public Double getCancelActivationAmount()
	{
		return cancelActivationAmount;
	}

	@JsonProperty("CancelActivationAmount")
	public void setCancelActivationAmount(final Double cancelActivationAmount)
	{
		this.cancelActivationAmount = cancelActivationAmount;
	}

	@JsonProperty("CancelActivationCount")
	public Double getCancelActivationCount()
	{
		return cancelActivationCount;
	}

	@JsonProperty("CancelActivationCount")
	public void setCancelActivationCount(final Double cancelActivationCount)
	{
		this.cancelActivationCount = cancelActivationCount;
	}

	@JsonProperty("ReloadAmount")
	public Double getReloadAmount()
	{
		return reloadAmount;
	}

	@JsonProperty("ReloadAmount")
	public void setReloadAmount(final Double reloadAmount)
	{
		this.reloadAmount = reloadAmount;
	}

	@JsonProperty("ReloadCount")
	public Double getReloadCount()
	{
		return reloadCount;
	}

	@JsonProperty("ReloadCount")
	public void setReloadCount(final Double reloadCount)
	{
		this.reloadCount = reloadCount;
	}

	@JsonProperty("CancelLoadAmount")
	public Double getCancelLoadAmount()
	{
		return cancelLoadAmount;
	}

	@JsonProperty("CancelLoadAmount")
	public void setCancelLoadAmount(final Double cancelLoadAmount)
	{
		this.cancelLoadAmount = cancelLoadAmount;
	}

	@JsonProperty("CancelLoadCount")
	public Double getCancelLoadCount()
	{
		return cancelLoadCount;
	}

	@JsonProperty("CancelLoadCount")
	public void setCancelLoadCount(final Double cancelLoadCount)
	{
		this.cancelLoadCount = cancelLoadCount;
	}

	@JsonProperty("RedemptionAmount")
	public Double getRedemptionAmount()
	{
		return redemptionAmount;
	}

	@JsonProperty("RedemptionAmount")
	public void setRedemptionAmount(final Double redemptionAmount)
	{
		this.redemptionAmount = redemptionAmount;
	}

	@JsonProperty("RedemptionCount")
	public Double getRedemptionCount()
	{
		return redemptionCount;
	}

	@JsonProperty("RedemptionCount")
	public void setRedemptionCount(final Double redemptionCount)
	{
		this.redemptionCount = redemptionCount;
	}

	@JsonProperty("CancelRedeemAmount")
	public Double getCancelRedeemAmount()
	{
		return cancelRedeemAmount;
	}

	@JsonProperty("CancelRedeemAmount")
	public void setCancelRedeemAmount(final Double cancelRedeemAmount)
	{
		this.cancelRedeemAmount = cancelRedeemAmount;
	}

	@JsonProperty("CancelRedeemCount")
	public Double getCancelRedeemCount()
	{
		return cancelRedeemCount;
	}

	@JsonProperty("CancelRedeemCount")
	public void setCancelRedeemCount(final Double cancelRedeemCount)
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
	public Double getPurchaseOrderValue()
	{
		return purchaseOrderValue;
	}

	@JsonProperty("PurchaseOrderValue")
	public void setPurchaseOrderValue(final Double purchaseOrderValue)
	{
		this.purchaseOrderValue = purchaseOrderValue;
	}

	@JsonProperty("DiscountPercentage")
	public Double getDiscountPercentage()
	{
		return discountPercentage;
	}

	@JsonProperty("DiscountPercentage")
	public void setDiscountPercentage(final Double discountPercentage)
	{
		this.discountPercentage = discountPercentage;
	}

	@JsonProperty("DiscountAmount")
	public Double getDiscountAmount()
	{
		return discountAmount;
	}

	@JsonProperty("DiscountAmount")
	public void setDiscountAmount(final Double discountAmount)
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
	public Object getMerchantOutletName()
	{
		return merchantOutletName;
	}

	@JsonProperty("MerchantOutletName")
	public void setMerchantOutletName(final Object merchantOutletName)
	{
		this.merchantOutletName = merchantOutletName;
	}

	@JsonProperty("MerchantOutletAddress1")
	public Object getMerchantOutletAddress1()
	{
		return merchantOutletAddress1;
	}

	@JsonProperty("MerchantOutletAddress1")
	public void setMerchantOutletAddress1(final Object merchantOutletAddress1)
	{
		this.merchantOutletAddress1 = merchantOutletAddress1;
	}

	@JsonProperty("MerchantOutletAddress2")
	public Object getMerchantOutletAddress2()
	{
		return merchantOutletAddress2;
	}

	@JsonProperty("MerchantOutletAddress2")
	public void setMerchantOutletAddress2(final Object merchantOutletAddress2)
	{
		this.merchantOutletAddress2 = merchantOutletAddress2;
	}

	@JsonProperty("MerchantOutletCity")
	public Object getMerchantOutletCity()
	{
		return merchantOutletCity;
	}

	@JsonProperty("MerchantOutletCity")
	public void setMerchantOutletCity(final Object merchantOutletCity)
	{
		this.merchantOutletCity = merchantOutletCity;
	}

	@JsonProperty("MerchantOutletState")
	public Object getMerchantOutletState()
	{
		return merchantOutletState;
	}

	@JsonProperty("MerchantOutletState")
	public void setMerchantOutletState(final Object merchantOutletState)
	{
		this.merchantOutletState = merchantOutletState;
	}

	@JsonProperty("MerchantOutletPinCode")
	public Object getMerchantOutletPinCode()
	{
		return merchantOutletPinCode;
	}

	@JsonProperty("MerchantOutletPinCode")
	public void setMerchantOutletPinCode(final Object merchantOutletPinCode)
	{
		this.merchantOutletPinCode = merchantOutletPinCode;
	}

	@JsonProperty("MerchantOutletPhone")
	public Object getMerchantOutletPhone()
	{
		return merchantOutletPhone;
	}

	@JsonProperty("MerchantOutletPhone")
	public void setMerchantOutletPhone(final Object merchantOutletPhone)
	{
		this.merchantOutletPhone = merchantOutletPhone;
	}

	@JsonProperty("MaskCard")
	public Object getMaskCard()
	{
		return maskCard;
	}

	@JsonProperty("MaskCard")
	public void setMaskCard(final Object maskCard)
	{
		this.maskCard = maskCard;
	}

	@JsonProperty("PrintMerchantCopy")
	public Object getPrintMerchantCopy()
	{
		return printMerchantCopy;
	}

	@JsonProperty("PrintMerchantCopy")
	public void setPrintMerchantCopy(final Object printMerchantCopy)
	{
		this.printMerchantCopy = printMerchantCopy;
	}

	@JsonProperty("InvoiceNumberMandatory")
	public Object getInvoiceNumberMandatory()
	{
		return invoiceNumberMandatory;
	}

	@JsonProperty("InvoiceNumberMandatory")
	public void setInvoiceNumberMandatory(final Object invoiceNumberMandatory)
	{
		this.invoiceNumberMandatory = invoiceNumberMandatory;
	}

	@JsonProperty("NumericUserPwd")
	public Object getNumericUserPwd()
	{
		return numericUserPwd;
	}

	@JsonProperty("NumericUserPwd")
	public void setNumericUserPwd(final Object numericUserPwd)
	{
		this.numericUserPwd = numericUserPwd;
	}

	@JsonProperty("IntegerAmount")
	public Object getIntegerAmount()
	{
		return integerAmount;
	}

	@JsonProperty("IntegerAmount")
	public void setIntegerAmount(final Object integerAmount)
	{
		this.integerAmount = integerAmount;
	}

	@JsonProperty("Culture")
	public Object getCulture()
	{
		return culture;
	}

	@JsonProperty("Culture")
	public void setCulture(final Object culture)
	{
		this.culture = culture;
	}

	@JsonProperty("CurrencySymbol")
	public Object getCurrencySymbol()
	{
		return currencySymbol;
	}

	@JsonProperty("CurrencySymbol")
	public void setCurrencySymbol(final Object currencySymbol)
	{
		this.currencySymbol = currencySymbol;
	}

	@JsonProperty("CurrencyPosition")
	public Object getCurrencyPosition()
	{
		return currencyPosition;
	}

	@JsonProperty("CurrencyPosition")
	public void setCurrencyPosition(final Object currencyPosition)
	{
		this.currencyPosition = currencyPosition;
	}

	@JsonProperty("CurrencyDecimalDigits")
	public Object getCurrencyDecimalDigits()
	{
		return currencyDecimalDigits;
	}

	@JsonProperty("CurrencyDecimalDigits")
	public void setCurrencyDecimalDigits(final Object currencyDecimalDigits)
	{
		this.currencyDecimalDigits = currencyDecimalDigits;
	}

	@JsonProperty("DisplayUnitForPoints")
	public Object getDisplayUnitForPoints()
	{
		return displayUnitForPoints;
	}

	@JsonProperty("DisplayUnitForPoints")
	public void setDisplayUnitForPoints(final Object displayUnitForPoints)
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
	public Double getTransferCardBalance()
	{
		return transferCardBalance;
	}

	@JsonProperty("TransferCardBalance")
	public void setTransferCardBalance(final Double transferCardBalance)
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
	public String getCardStatus()
	{
		return cardStatus;
	}

	@JsonProperty("CardStatus")
	public void setCardStatus(final String cardStatus)
	{
		this.cardStatus = cardStatus;
	}

	@JsonProperty("CardCurrencySymbol")
	public String getCardCurrencySymbol()
	{
		return cardCurrencySymbol;
	}

	@JsonProperty("CardCurrencySymbol")
	public void setCardCurrencySymbol(final String cardCurrencySymbol)
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
	public String getCardType()
	{
		return cardType;
	}

	@JsonProperty("CardType")
	public void setCardType(final String cardType)
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
	public Double getCumulativeAmountSpent()
	{
		return cumulativeAmountSpent;
	}

	@JsonProperty("CumulativeAmountSpent")
	public void setCumulativeAmountSpent(final Double cumulativeAmountSpent)
	{
		this.cumulativeAmountSpent = cumulativeAmountSpent;
	}

	@JsonProperty("CurrencyConversionRate")
	public Double getCurrencyConversionRate()
	{
		return currencyConversionRate;
	}

	@JsonProperty("CurrencyConversionRate")
	public void setCurrencyConversionRate(final Double currencyConversionRate)
	{
		this.currencyConversionRate = currencyConversionRate;
	}

	@JsonProperty("CurrencyConvertedAmount")
	public Double getCurrencyConvertedAmount()
	{
		return currencyConvertedAmount;
	}

	@JsonProperty("CurrencyConvertedAmount")
	public void setCurrencyConvertedAmount(final Double currencyConvertedAmount)
	{
		this.currencyConvertedAmount = currencyConvertedAmount;
	}

	@JsonProperty("CardHolderName")
	public String getCardHolderName()
	{
		return cardHolderName;
	}

	@JsonProperty("CardHolderName")
	public void setCardHolderName(final String cardHolderName)
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
	public Double getPromotionalValue()
	{
		return promotionalValue;
	}

	@JsonProperty("PromotionalValue")
	public void setPromotionalValue(final Double promotionalValue)
	{
		this.promotionalValue = promotionalValue;
	}

	@JsonProperty("EarnedValue")
	public Double getEarnedValue()
	{
		return earnedValue;
	}

	@JsonProperty("EarnedValue")
	public void setEarnedValue(final Double earnedValue)
	{
		this.earnedValue = earnedValue;
	}

	@JsonProperty("TransactionAmount")
	public Double getTransactionAmount()
	{
		return transactionAmount;
	}

	@JsonProperty("TransactionAmount")
	public void setTransactionAmount(final Double transactionAmount)
	{
		this.transactionAmount = transactionAmount;
	}

	@JsonProperty("PreviousBalance")
	public Double getPreviousBalance()
	{
		return previousBalance;
	}

	@JsonProperty("PreviousBalance")
	public void setPreviousBalance(final Double previousBalance)
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
	public Double getOriginalActivationAmount()
	{
		return originalActivationAmount;
	}

	@JsonProperty("OriginalActivationAmount")
	public void setOriginalActivationAmount(final Double originalActivationAmount)
	{
		this.originalActivationAmount = originalActivationAmount;
	}

	@JsonProperty("CardCreationType")
	public String getCardCreationType()
	{
		return cardCreationType;
	}

	@JsonProperty("CardCreationType")
	public void setCardCreationType(final String cardCreationType)
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
	public Double getReloadableAmount()
	{
		return reloadableAmount;
	}

	@JsonProperty("ReloadableAmount")
	public void setReloadableAmount(final Double reloadableAmount)
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
	public String getCardProgramGroupType()
	{
		return cardProgramGroupType;
	}

	@JsonProperty("CardProgramGroupType")
	public void setCardProgramGroupType(final String cardProgramGroupType)
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