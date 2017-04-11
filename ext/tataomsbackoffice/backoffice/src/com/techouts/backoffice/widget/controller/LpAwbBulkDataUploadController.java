/**
 *
 */
package com.techouts.backoffice.widget.controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.csvreader.CsvReader;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.core.user.AuthorityGroupService;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.core.user.impl.AuthorityGroup;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.api.orderlogistics.OrderLogisticsFacade;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.lpawb.dto.LPOverrideAWBEdit;
import com.hybris.oms.domain.lpawb.dto.OrderLineInfo;
import com.hybris.oms.tata.constants.TataomsbackofficeConstants;
import com.hybris.oms.tata.services.FilePathProviderService;
import com.techouts.backoffice.exception.InvalidFileNameException;


/**
 * @author TOOW10
 *
 */
public class LpAwbBulkDataUploadController extends DefaultWidgetController
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	private Textbox textBoxFileName;
	@Wire
	private Label uploadFileError;
	private static final Logger LOG = Logger.getLogger(LpAwbBulkDataUploadController.class);
	private static final String[] PROPERTY_FILE_ERRORS =
	{ "LP awb forward File Path", "Lp Awb reverse File Path", "awb forward File Path", "Awb reverse File Path" };
	private static final String DATE_FORMAT = "ddMMyyyyHHmm";
	private Media media;
	private static final String CSV = ".csv";
	@WireVariable("filePathProviderService")
	private FilePathProviderService filePathProviderService;
	@WireVariable
	private transient CockpitUserService cockpitUserService;
	@WireVariable
	private transient AuthorityGroupService authorityGroupService;
	@WireVariable
	private transient AuthorityGroup activeUserRole;
	private final String header = "orderID,transactionId,lpname,awbnumber";
	@WireVariable("orderLogisticsRestClient")
	private OrderLogisticsFacade orderLogisticsUpdateFacade;
	private static final String LP_TRANSACTION_TYPE = "LP";
	private static final String AWB_TRANSACTION_TYPE = "AWB";
	boolean lpOverride = false;
	boolean isReturn = false;
	String transactionType = "";

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
		textBoxFileName.setValue("");
		uploadFileError.setValue("");

	}

	@ViewEvent(componentID = "lpAwbUpload_Button", eventName = Events.ON_UPLOAD)
	public void selectHomeUploadZip(final UploadEvent uploadEvent) throws InterruptedException, IOException
	{

		uploadFileError.setValue("");
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		final String TimeStamp = simpleDateFormat.format(new Date());
		if (!filePathProviderService.propertyFilePathValidation(PROPERTY_FILE_ERRORS,
				TataomsbackofficeConstants.LPAWB_FORWARD_FILE_PATH, TataomsbackofficeConstants.LPAWB_REVERSE_FILE_PATH,
				TataomsbackofficeConstants.AWB_FORWARD_FILE_PATH, TataomsbackofficeConstants.AWB_REVERSE_FILE_PATH))
		{
			return;
		}
		final String tempFile;
		final String tempFilePath;
		final java.io.File destFile;
		media = uploadEvent.getMedia();
		try
		{
			if (media.getName().startsWith(TataomsbackofficeConstants.LPAWB_FORWARD_FiLE_PREFIX))
			{
				tempFile = TataomsbackofficeConstants.LPAWB_FORWARD_FiLE_PREFIX.concat(TimeStamp).concat(CSV);
				tempFilePath = TataomsbackofficeConstants.LPAWB_FORWARD_FILE_PATH;
				this.lpOverride = true;
				this.transactionType = LP_TRANSACTION_TYPE;
				this.isReturn = false;
			}
			else if (media.getName().startsWith(TataomsbackofficeConstants.LPAWB_REVERSE_FILE_PREFIX))
			{
				tempFile = TataomsbackofficeConstants.LPAWB_REVERSE_FILE_PREFIX.concat(TimeStamp).concat(CSV);
				tempFilePath = TataomsbackofficeConstants.LPAWB_REVERSE_FILE_PATH;
				this.lpOverride = true;
				this.isReturn = true;
				this.transactionType = LP_TRANSACTION_TYPE;

			}
			else if (media.getName().startsWith(TataomsbackofficeConstants.AWB_FORWARD__FILE_PREFIX))
			{
				tempFile = TataomsbackofficeConstants.AWB_FORWARD__FILE_PREFIX.concat(TimeStamp).concat(CSV);
				tempFilePath = TataomsbackofficeConstants.AWB_FORWARD_FILE_PATH;
				this.lpOverride = false;
				this.transactionType = AWB_TRANSACTION_TYPE;
				this.isReturn = false;
			}
			else if (media.getName().startsWith(TataomsbackofficeConstants.AWB_REVERSE__FILE_PREFIX))
			{
				tempFile = TataomsbackofficeConstants.AWB_REVERSE__FILE_PREFIX.concat(TimeStamp).concat(CSV);
				tempFilePath = TataomsbackofficeConstants.LPAWB_REVERSE_FILE_PATH;
				this.lpOverride = false;
				this.isReturn = true;
				this.transactionType = AWB_TRANSACTION_TYPE;
			}
			else
			{
				throw new InvalidFileNameException("Invalid file prefix name");
			}
			if (media.isBinary())
			{
				if (media.getStreamData().read() == -1)
				{
					LOG.info("File is Empty ...");
					Messagebox.show("File is Empty ");
					return;
				}
				destFile = new File(tempFilePath.trim(), tempFile);
				FileUtils.copyInputStreamToFile(media.getStreamData(), destFile);
			} //end is Binary Check
			else
			{
				if (media.getStringData().length() == 0)
				{
					LOG.info("File is Empty ...");
					Messagebox.show("File is Empty ");
					return;
				}
				destFile = new File(tempFilePath.trim(), tempFile);
				FileUtils.writeStringToFile(destFile, media.getStringData());
			}
			sendBulkDatatoOms(destFile);
		}
		catch (final InvalidFileNameException e)
		{
			Messagebox.show(e.getMessage());
		}
	}

	private void sendBulkDatatoOms(final File destFile)
	{
		final String userId = cockpitUserService.getCurrentUser();
		String roleId = "none";
		activeUserRole = authorityGroupService.getActiveAuthorityGroupForUser(userId);
		if (activeUserRole != null)
		{
			roleId = activeUserRole.getCode();
		}
		int count = 0;
		final LPOverrideAWBEdit lpEdit = new LPOverrideAWBEdit();
		lpEdit.setTransactionType(this.transactionType);//set transaction Type
		lpEdit.setUserId(userId);
		lpEdit.setRoleId(roleId);
		lpEdit.setIsReturn(this.isReturn);

		final ArrayList<OrderLineInfo> orderLineInfoList = new ArrayList<OrderLineInfo>();
		CsvReader records = null;
		FileReader fileReader = null;
		try
		{
			fileReader = new FileReader(destFile);
			records = new CsvReader(fileReader);
			if (records.readHeaders())
			{
				LOG.info("Record Object Headers " + records.getHeaders() + records.getHeaderCount() + records.getHeader(0));
				for (final String headerField : records.getHeaders())
				{
					if (!header.contains(headerField))
					{
						throw new InvalidFileNameException("Invalid File Headers Found");
					}
				}
				String orderId = null;
				String transactionId = null;
				String lpname = null;
				while (records.readRecord())
				{
					++count;
					orderId = records.get("orderID");
					transactionId = records.get("transactionId");
					lpname = records.get("lpname");
					if (StringUtils.isBlank(orderId) || StringUtils.isBlank(transactionId) || StringUtils.isBlank(lpname))
					{
						uploadFileError.setValue("Invalid Data Record Found At Row Number = " + count);
						return;
					}
					else
					{
						final OrderLineInfo info = new OrderLineInfo();
						info.setOrderId(orderId);
						info.setTransactionId(transactionId);
						info.setLogisticName(lpname);
						info.setAwbNumber(records.get("awbnumber"));
						info.setLpOverride(this.lpOverride);
						info.setNextLP(false);
						orderLineInfoList.add(info);
					}
				} //end while
				lpEdit.setOrderLineInfo(orderLineInfoList);
				final Boolean updateResult = orderLogisticsUpdateFacade.bulkUpload(lpEdit);
				if (updateResult.booleanValue())
				{
					textBoxFileName.setValue(destFile.getName());
					Messagebox.show("Lp awb BulkUpload Sucess");
					return;
				}
				else
				{
					textBoxFileName.setValue(destFile.getName());
					uploadFileError.setValue("Lp awb BulkUpload Faild");
					return;
				}
			} //main if
			else
			{
				uploadFileError.setValue("Bulk Upload Faild due to heders");
				return;
			}
		}
		catch (final EntityValidationException e)
		{
			LOG.info("" + e.getMessage());
			uploadFileError.setValue("Bulk Uplaod Faild due to Invalid Data ");
		}
		catch (final Exception e)
		{
			LOG.info(e.getMessage());
		}
	}


}
