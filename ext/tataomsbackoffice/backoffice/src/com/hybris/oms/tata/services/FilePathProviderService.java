/**
 *
 */
package com.hybris.oms.tata.services;

import org.zkoss.zul.Messagebox;


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
	private String pincodesUploadPath;
	private String shipmentStatusUpload;
	private String landmarkUploadPath;
	private String returnOrderFileUploadPath;
	private String fragileOrderFileUploadPath;
	private String forwardFileHeaderProperty;
	private String returnFileHeaderProperty;


	/**
	 * @return the forwardFileHeaderProperty
	 */
	public String getForwardFileHeaderProperty()
	{
		return forwardFileHeaderProperty;
	}

	/**
	 * @param forwardFileHeaderProperty
	 *           the forwardFileHeaderProperty to set
	 */
	public void setForwardFileHeaderProperty(final String forwardFileHeaderProperty)
	{
		this.forwardFileHeaderProperty = forwardFileHeaderProperty;
	}

	/**
	 * @return the returnFileHeaderProperty
	 */
	public String getReturnFileHeaderProperty()
	{
		return returnFileHeaderProperty;
	}

	/**
	 * @param returnFileHeaderProperty
	 *           the returnFileHeaderProperty to set
	 */
	public void setReturnFileHeaderProperty(final String returnFileHeaderProperty)
	{
		this.returnFileHeaderProperty = returnFileHeaderProperty;
	}

	/**
	 * @author prabhakar
	 * @return the returnOrderFileUploadPath
	 */
	public String getReturnOrderFileUploadPath()
	{
		return returnOrderFileUploadPath;
	}

	/**
	 * @param returnOrderFileUploadPath
	 *           the returnOrderFileUploadPath to set
	 */
	public void setReturnOrderFileUploadPath(final String returnOrderFileUploadPath)
	{
		this.returnOrderFileUploadPath = returnOrderFileUploadPath;
	}

	/**
	 * @author prabhakar
	 * @return the fragileOrderFileUploadPath
	 */
	public String getFragileOrderFileUploadPath()
	{
		return fragileOrderFileUploadPath;
	}

	/**
	 * @param fragileOrderFileUploadPath
	 *           the fragileOrderFileUploadPath to set
	 */
	public void setFragileOrderFileUploadPath(final String fragileOrderFileUploadPath)
	{
		this.fragileOrderFileUploadPath = fragileOrderFileUploadPath;
	}

	/**
	 * @author prabhakar
	 * @return the landmarkUploadPath
	 */
	public String getLandmarkUploadPath()
	{
		return landmarkUploadPath;
	}

	/**
	 * @param landmarkUploadPath
	 *           the landmarkUploadPath to set
	 */
	public void setLandmarkUploadPath(final String landmarkUploadPath)
	{
		this.landmarkUploadPath = landmarkUploadPath;
	}

	/**
	 *
	 * @param ErrorMsg
	 * @param values
	 * @return boolean
	 * @throws InterruptedException
	 *            This method is called from controller for validate Data from localProprties file.
	 */
	public boolean propertyFilePathValidation(final String[] ErrorMsg, final String... values) throws InterruptedException
	{
		for (int i = 0; i < values.length; i++)
		{
			if ("null".equals(values[i]) || "".equals(values[i]) || values[i] == null)
			{
				Messagebox.show("Unable to find " + ErrorMsg[i] + " config inside PropertyFile", "Error", Messagebox.OK,
						Messagebox.ERROR);
				return false;
			}
		}
		return true;
	}

	/**
	 * @return the pincodesUploadPath
	 */
	public String getPincodesUploadPath()
	{
		return pincodesUploadPath;
	}

	/**
	 * @param pincodesUploadPath
	 *           the pincodesUploadPath to set
	 */
	public void setPincodesUploadPath(final String pincodesUploadPath)
	{
		this.pincodesUploadPath = pincodesUploadPath;
	}

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

	/**
	 * @return the shipmentStatusUpload
	 */
	public String getShipmentStatusUpload()
	{
		return shipmentStatusUpload;
	}

	/**
	 * @param shipmentStatusUpload
	 *           the shipmentStatusUpload to set
	 */
	public void setShipmentStatusUpload(final String shipmentStatusUpload)
	{
		this.shipmentStatusUpload = shipmentStatusUpload;
	}




}
