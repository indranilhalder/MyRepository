/**
 * 
 */
package com.tisl.mpl.pojo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author Tech
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
{ "OriginalTransactionId", "OriginalBatchNumber",})
public class QCRefundRequest
{
	@JsonProperty("OriginalTransactionId")
	private String originalTransactionId;
	
	@JsonProperty("OriginalBatchNumber")
	private String originalBatchNumber;

	@JsonProperty("OriginalTransactionId")
	public String getOriginalTransactionId()
	{
		return originalTransactionId;
	}

	@JsonProperty("OriginalTransactionId")
	public void setOriginalTransactionId(String originalTransactionId)
	{
		this.originalTransactionId = originalTransactionId;
	}

	@JsonProperty("OriginalBatchNumber")
	public String getOriginalBatchNumber()
	{
		return originalBatchNumber;
	}

	@JsonProperty("OriginalBatchNumber")
	public void setOriginalBatchNumber(String originalBatchNumber)
	{
		this.originalBatchNumber = originalBatchNumber;
	}
}
