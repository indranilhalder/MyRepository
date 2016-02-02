/**
 *
 */
package com.hybris.oms.tata.pincodeservice;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.tata.services.FilePathProviderService;


/**
 * @author Nagarjuna
 * 
 *         This class is for showing the Pincode Serviceability Report
 * 
 */
public class PincodeServiceReportController extends DefaultWidgetController
{
	private static final Logger LOG = Logger.getLogger(PincodeServiceReportController.class.getName());
	private Listbox listview;


	@WireVariable("filePathProviderService")
	private FilePathProviderService filePathProviderService;

	@Override
	public void initialize(final Component comp)
	{

		super.initialize(comp);
		if (filePathProviderService == null)
		{
			LOG.error("saveFilePath genearating error..");
			return;
		}
		final String saveFilePath = filePathProviderService.getPinSvcReptDwnldPath();
		if ("null".equals(saveFilePath) || "".equals(saveFilePath))
		{

			Messagebox.show("Unable to find reports download path", "Error", Messagebox.OK, Messagebox.ERROR);
			return;
		}

		LOG.info("upload saveFilePath*******************::: " + saveFilePath);
		final List<File> fileList = (List<File>) listFilesForFolder(new File(saveFilePath.trim()), true, "");
		fileList.sort(PincodeServiceReportController.listFilesForFolderComparator);
		listview.setModel(new ListModelList(fileList));
		listview.setItemRenderer(new PincodeServiceListItemRenderer());

	}

	/**
	 * This method is to return list of files inside a folders recursively
	 * 
	 * @param folder
	 * @param recursivity
	 * @param patternFileFilter
	 * @return
	 */
	public static Collection<File> listFilesForFolder(final File folder, final boolean recursivity, final String patternFileFilter)

	{

		// Inputs
		boolean filteredFile = false;

		// Ouput
		final Collection<File> output = new ArrayList<File>();

		if (!folder.exists())
		{
			LOG.info("Folder does not exist with the name" + folder.getName());
			return output;
		}
		// Foreach elements
		for (final File fileEntry : folder.listFiles())
		{


			// If this element is a directory, do it recursivly
			if (fileEntry.isDirectory())
			{
				if (recursivity)
				{
					output.addAll(listFilesForFolder(fileEntry, recursivity, patternFileFilter));

				}
			}
			else
			{
				//If there is no pattern, the file is correct

				if (patternFileFilter.length() == 0)
				{
					filteredFile = true;
				}

				/*
				 * // Otherwise we need to filter by pattern else { filteredFile = Pattern.matches(patternFileFilter,
				 * fileEntry.getName()); }
				 */

				// If the file has a name which match with the pattern, then add it to the list
				if (filteredFile)
				{
					output.add(fileEntry);

				}
			}
		}

		return output;
	}

	public static Comparator<File> listFilesForFolderComparator = new Comparator<File>()
	{
		public int compare(final File arg0, final File arg1)
		{
			if (arg0 == null || arg1 == null)
			{
				return -1;
			}
			return Double.compare(arg1.lastModified(), arg0.lastModified());
		}
	};

}
