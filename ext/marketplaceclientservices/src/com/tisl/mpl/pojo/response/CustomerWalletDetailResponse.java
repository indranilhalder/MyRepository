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
@JsonPropertyOrder({
"Wallet",
"ApiWebProperties",
"Notes",
"ApprovalCode",
"ResponseCode",
"ResponseMessage",
"TransactionId",
"TransactionType",
"ErrorCode",
"ErrorDescription"
})
public class CustomerWalletDetailResponse {

@JsonProperty("Wallet")
private Wallet wallet;
@JsonProperty("ApiWebProperties")
private ApiWebProperties apiWebProperties;
@JsonProperty("Notes")
private String notes;
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
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("Wallet")
public Wallet getWallet() {
return wallet;
}

@JsonProperty("Wallet")
public void setWallet(Wallet wallet) {
this.wallet = wallet;
}

@JsonProperty("ApiWebProperties")
public ApiWebProperties getApiWebProperties() {
return apiWebProperties;
}

@JsonProperty("ApiWebProperties")
public void setApiWebProperties(ApiWebProperties apiWebProperties) {
this.apiWebProperties = apiWebProperties;
}

@JsonProperty("Notes")
public String getNotes() {
return notes;
}

@JsonProperty("Notes")
public void setNotes(String notes) {
this.notes = notes;
}

@JsonProperty("ApprovalCode")
public String getApprovalCode() {
return approvalCode;
}

@JsonProperty("ApprovalCode")
public void setApprovalCode(String approvalCode) {
this.approvalCode = approvalCode;
}

@JsonProperty("ResponseCode")
public Integer getResponseCode() {
return responseCode;
}

@JsonProperty("ResponseCode")
public void setResponseCode(Integer responseCode) {
this.responseCode = responseCode;
}

@JsonProperty("ResponseMessage")
public String getResponseMessage() {
return responseMessage;
}

@JsonProperty("ResponseMessage")
public void setResponseMessage(String responseMessage) {
this.responseMessage = responseMessage;
}

@JsonProperty("TransactionId")
public Integer getTransactionId() {
return transactionId;
}

@JsonProperty("TransactionId")
public void setTransactionId(Integer transactionId) {
this.transactionId = transactionId;
}

@JsonProperty("TransactionType")
public String getTransactionType() {
return transactionType;
}

@JsonProperty("TransactionType")
public void setTransactionType(String transactionType) {
this.transactionType = transactionType;
}

@JsonProperty("ErrorCode")
public Object getErrorCode() {
return errorCode;
}

@JsonProperty("ErrorCode")
public void setErrorCode(Object errorCode) {
this.errorCode = errorCode;
}

@JsonProperty("ErrorDescription")
public Object getErrorDescription() {
return errorDescription;
}

@JsonProperty("ErrorDescription")
public void setErrorDescription(Object errorDescription) {
this.errorDescription = errorDescription;
}

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}