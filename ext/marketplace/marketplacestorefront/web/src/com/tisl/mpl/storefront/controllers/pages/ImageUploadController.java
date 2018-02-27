/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.Importer;
import de.hybris.platform.impex.jalo.imp.DefaultDumpHandler;
import de.hybris.platform.impex.jalo.media.DefaultMediaDataHandler;
import de.hybris.platform.impex.jalo.media.MediaDataTranslator;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.util.CSVReader;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.tis.mpl.facade.imageupload.MplImageUploadFacade;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author Nirav Bhanushali
 *
 */

@Controller
@RequestMapping(value = "/my-account-imageUpload")
public class ImageUploadController extends AbstractMplSearchPageController
{

	@Resource(name = ModelAttributetConstants.ACCOUNT_BREADCRUMB_BUILDER)
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@Autowired
	private MplImageUploadFacade mplImageUploadFacade;

	@Autowired
	private CommonsMultipartResolver multipartResolver;

	private static final String CATALOG_DATA = "mplContentCatalog";
	private static final String CATALOG_VERSION_DATA = "Online";
	private static final String siteResource = "file:/";

	private static final String IMPORT_DATA = "$catalogversion=catalogversion(catalog(id[default='" + CATALOG_DATA
			+ "']),version[default='" + CATALOG_VERSION_DATA + "'])" + "[unique=true,default='" + CATALOG_DATA + ":"
			+ CATALOG_VERSION_DATA + "'] \n"
			+ "INSERT_UPDATE Media;code[unique=true];$catalogversion;mime[default='image/jpeg'];realfilename;@media[translator=de.hybris.platform.impex.jalo.media.MediaDataTranslator];folder(qualifier)[default='root']\n";

	@RequestMapping(value = "/imageUpload", method = RequestMethod.GET)
	@RequireHardLogIn
	public String getImageUploaded(final Model model) throws CMSItemNotFoundException
	{
		final List<MediaFolderModel> mediaFolder = getMediaFolderList();
		storeCmsPageInModel(model, getContentPageForLabelOrId("imageUpload"));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId("imageUpload"));
		model.addAttribute("mediaFolderList", mediaFolder);
		model.addAttribute(ModelAttributetConstants.BREADCRUMBS, accountBreadcrumbBuilder.getBreadcrumbs("imageUpload"));
		model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}

	public List<MediaFolderModel> getMediaFolderList()
	{

		List<MediaFolderModel> MediaFolderDataList = null;
		try
		{
			MediaFolderDataList = mplImageUploadFacade.getMediaFolders();

		}
		catch (final EtailNonBusinessExceptions e)
		{
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(e);
			}
		}

		return MediaFolderDataList;
	}


	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public org.json.simple.JSONArray imageUpload(@RequestParam final MultipartFile[] file,
			final MultipartHttpServletRequest request) throws ServletException, IOException
	{
		final org.json.simple.JSONObject jObject = new org.json.simple.JSONObject();
		final StringBuilder stringBuilder = new StringBuilder();
		final List<String> list = new ArrayList<String>();
		final JSONArray ja = new JSONArray();
		try
		{

			File f = new File("");
			for (final MultipartFile files : file)
			{
				if (files.isEmpty())
				{
					continue; //next pls
				}

				try
				{
					final byte[] bytes = files.getBytes();
					final Path path = Paths.get("f:/images/" + files.getOriginalFilename());
					f = new File("F:/" + "/images/" + files.getOriginalFilename());
					Files.write(path, bytes);
					stringBuilder.append(";" + f.getName() + ";;image/jpg;;" + siteResource + f.getAbsolutePath() + ";root" + "/n");
					System.out.println("------------" + f.getAbsolutePath());
					list.add(f.getName());
					//stringBuilder.append(stringBuilder.toString() + "/n");
				}
				catch (final IOException e)
				{
					e.printStackTrace();
				}

			}



			final InputStream inputStream = new ByteArrayInputStream((IMPORT_DATA + stringBuilder.toString()).getBytes());

			//create stream reader
			CSVReader reader = null;

			reader = new CSVReader(inputStream, "UTF-8");

			// import
			MediaDataTranslator.setMediaDataHandler(new DefaultMediaDataHandler());
			Importer importer = null;
			//ImpExException error = null;
			try
			{
				importer = new Importer(reader);
				importer.getReader().enableCodeExecution(true);
				importer.setMaxPass(-1);
				importer.setDumpHandler(new FirstLinesDumpReader());
				importer.importAll();
			}
			catch (final ImpExException e)
			{
				e.printStackTrace();
			}

			for (final String object : list)
			{
				final Media mediaData = getMediaByCode(object);
				jObject.put("imageName", object);
				jObject.put("imageUrl", mediaData.getURL().replace(".*", " / " + object));
				jObject.put("size", mediaData.getSize());
				ja.add(jObject);
			}

			// failure handling
			if (importer.hasUnresolvedLines())
			{
				System.out.println("Import has " + importer.getDumpedLineCountPerPass() + "+unresolved lines, first lines are:\n"
						+ importer.getDumpHandler().getDumpAsString());
			}
			return ja;
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			MediaDataTranslator.unsetMediaDataHandler();
		}

		return ja;
	}

	private static class FirstLinesDumpReader extends DefaultDumpHandler
	{
		@Override
		public String getDumpAsString()
		{
			final StringBuffer result = new StringBuffer(100);
			try
			{
				final BufferedReader reader = new BufferedReader(new FileReader(getDumpAsFile()));
				result.append(reader.readLine() + "\n");
				result.append(reader.readLine() + "\n");
				result.append(reader.readLine() + "\n");
				reader.close();
			}
			catch (final FileNotFoundException e)
			{
				result.append("Error while reading dump " + e.getMessage());
			}
			catch (final IOException e)
			{
				result.append("Error while reading dump " + e.getMessage());
			}
			return result.toString();
		}
	}

	private Media getMediaByCode(final String mediaCode)
	{
		return (Media) MediaManager.getInstance().getMediaByCode(mediaCode).iterator().next();
	}



}
