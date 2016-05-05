/**
 *
 */
package com.hybris.oms.tata.services;

/**
 * @author Saood
 *
 */
public class FilePathProviderService
{

	private String pinSvcReptDwnldPath;
	private String pinMstrTmpPath;
	private String tplInbndCsvPath;
	private String tplOutbndCsvPath;
	private String validationErrFilePath;
	private String statesPropFilePath;

	/**
	 * @return the statesPropFilePath
	 */
	public String getStatesPropFilePath()
	{
		return statesPropFilePath;
	}

	/**
	 * @param statesPropFilePath
	 *           the statesPropFilePath to set
	 */
	public void setStatesPropFilePath(final String statesPropFilePath)
	{
		this.statesPropFilePath = statesPropFilePath;
	}

	/**
	 * @return the pinSvcReptDwnldPath
	 */
	public String getPinSvcReptDwnldPath()
	{
		return pinSvcReptDwnldPath;
	}

	/**
	 * @param pinSvcReptDwnldPath
	 *           the pinSvcReptDwnldPath to set
	 */
	public void setPinSvcReptDwnldPath(final String pinSvcReptDwnldPath)
	{
		this.pinSvcReptDwnldPath = pinSvcReptDwnldPath;
	}

	/**
	 * @return the pinMstrTmpPath
	 */
	public String getPinMstrTmpPath()
	{
		return pinMstrTmpPath;
	}

	/**
	 * @param pinMstrTmpPath
	 *           the pinMstrTmpPath to set
	 */
	public void setPinMstrTmpPath(final String pinMstrTmpPath)
	{
		this.pinMstrTmpPath = pinMstrTmpPath;
	}

	/**
	 * @return the tplInbndCsvPath
	 */
	public String getTplInbndCsvPath()
	{
		return tplInbndCsvPath;
	}

	/**
	 * @param tplInbndCsvPath
	 *           the tplInbndCsvPath to set
	 */
	public void setTplInbndCsvPath(final String tplInbndCsvPath)
	{
		this.tplInbndCsvPath = tplInbndCsvPath;
	}

	/**
	 * @return the tplOutbndCsvPath
	 */
	public String getTplOutbndCsvPath()
	{
		return tplOutbndCsvPath;
	}

	/**
	 * @param tplOutbndCsvPath
	 *           the tplOutbndCsvPath to set
	 */
	public void setTplOutbndCsvPath(final String tplOutbndCsvPath)
	{
		this.tplOutbndCsvPath = tplOutbndCsvPath;
	}

	/**
	 * @return the validationErrFilePath
	 */
	public String getValidationErrFilePath()
	{
		return validationErrFilePath;
	}

	/**
	 * @param validationErrFilePath
	 *           the validationErrFilePath to set
	 */
	public void setValidationErrFilePath(final String validationErrFilePath)
	{
		this.validationErrFilePath = validationErrFilePath;
	}




}
