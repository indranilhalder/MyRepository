/**
 *
 */
package com.tisl.mpl.pojo.response;

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
{ "MerchantOutletName", "AcquirerId", "OrganizationName", "POSEntryMode", "POSTypeId", "POSName", "TermAppVersion",
		"CurrentBatchNumber", "TerminalId", "UserName", "Password", "ForwardingEntityId", "ForwardingEntityPassword",
		"DateAtClient", "IsForwardingEntityExists" })
public class ApiWebProperties
{

	@JsonProperty("MerchantOutletName")
	private String merchantOutletName;
	@JsonProperty("AcquirerId")
	private String acquirerId;
	@JsonProperty("OrganizationName")
	private String organizationName;
	@JsonProperty("POSEntryMode")
	private Integer pOSEntryMode;
	@JsonProperty("POSTypeId")
	private Integer pOSTypeId;
	@JsonProperty("POSName")
	private String pOSName;
	@JsonProperty("TermAppVersion")
	private String termAppVersion;
	@JsonProperty("CurrentBatchNumber")
	private Integer currentBatchNumber;
	@JsonProperty("TerminalId")
	private String terminalId;
	@JsonProperty("UserName")
	private String userName;
	@JsonProperty("Password")
	private String password;
	@JsonProperty("ForwardingEntityId")
	private String forwardingEntityId;
	@JsonProperty("ForwardingEntityPassword")
	private String forwardingEntityPassword;
	@JsonProperty("DateAtClient")
	private String dateAtClient;
	@JsonProperty("IsForwardingEntityExists")
	private Boolean isForwardingEntityExists;
	@JsonIgnore
	private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

	@JsonProperty("AcquirerId")
	public String getAcquirerId()
	{
		return acquirerId;
	}

	@JsonProperty("AcquirerId")
	public void setAcquirerId(final String acquirerId)
	{
		this.acquirerId = acquirerId;
	}

	@JsonProperty("OrganizationName")
	public String getOrganizationName()
	{
		return organizationName;
	}

	@JsonProperty("OrganizationName")
	public void setOrganizationName(final String organizationName)
	{
		this.organizationName = organizationName;
	}

	@JsonProperty("POSEntryMode")
	public Integer getPOSEntryMode()
	{
		return pOSEntryMode;
	}

	@JsonProperty("POSEntryMode")
	public void setPOSEntryMode(final Integer pOSEntryMode)
	{
		this.pOSEntryMode = pOSEntryMode;
	}

	@JsonProperty("POSTypeId")
	public Integer getPOSTypeId()
	{
		return pOSTypeId;
	}

	@JsonProperty("POSTypeId")
	public void setPOSTypeId(final Integer pOSTypeId)
	{
		this.pOSTypeId = pOSTypeId;
	}

	@JsonProperty("POSName")
	public String getPOSName()
	{
		return pOSName;
	}

	@JsonProperty("POSName")
	public void setPOSName(final String pOSName)
	{
		this.pOSName = pOSName;
	}

	@JsonProperty("TermAppVersion")
	public String getTermAppVersion()
	{
		return termAppVersion;
	}

	@JsonProperty("TermAppVersion")
	public void setTermAppVersion(final String termAppVersion)
	{
		this.termAppVersion = termAppVersion;
	}

	@JsonProperty("CurrentBatchNumber")
	public Integer getCurrentBatchNumber()
	{
		return currentBatchNumber;
	}

	@JsonProperty("CurrentBatchNumber")
	public void setCurrentBatchNumber(final Integer currentBatchNumber)
	{
		this.currentBatchNumber = currentBatchNumber;
	}

	@JsonProperty("TerminalId")
	public String getTerminalId()
	{
		return terminalId;
	}

	@JsonProperty("TerminalId")
	public void setTerminalId(final String terminalId)
	{
		this.terminalId = terminalId;
	}

	@JsonProperty("UserName")
	public String getUserName()
	{
		return userName;
	}

	@JsonProperty("UserName")
	public void setUserName(final String userName)
	{
		this.userName = userName;
	}

	@JsonProperty("Password")
	public String getPassword()
	{
		return password;
	}

	@JsonProperty("Password")
	public void setPassword(final String password)
	{
		this.password = password;
	}

	@JsonProperty("ForwardingEntityId")
	public String getForwardingEntityId()
	{
		return forwardingEntityId;
	}

	@JsonProperty("ForwardingEntityId")
	public void setForwardingEntityId(final String forwardingEntityId)
	{
		this.forwardingEntityId = forwardingEntityId;
	}

	@JsonProperty("ForwardingEntityPassword")
	public String getForwardingEntityPassword()
	{
		return forwardingEntityPassword;
	}

	@JsonProperty("ForwardingEntityPassword")
	public void setForwardingEntityPassword(final String forwardingEntityPassword)
	{
		this.forwardingEntityPassword = forwardingEntityPassword;
	}

	@JsonProperty("DateAtClient")
	public String getDateAtClient()
	{
		return dateAtClient;
	}

	@JsonProperty("DateAtClient")
	public void setDateAtClient(final String dateAtClient)
	{
		this.dateAtClient = dateAtClient;
	}

	@JsonProperty("IsForwardingEntityExists")
	public Boolean getIsForwardingEntityExists()
	{
		return isForwardingEntityExists;
	}

	@JsonProperty("IsForwardingEntityExists")
	public void setIsForwardingEntityExists(final Boolean isForwardingEntityExists)
	{
		this.isForwardingEntityExists = isForwardingEntityExists;
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