/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.Importer;
import de.hybris.platform.impex.jalo.imp.DefaultDumpHandler;
import de.hybris.platform.impex.jalo.media.DefaultMediaDataHandler;
import de.hybris.platform.impex.jalo.media.MediaDataTranslator;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.media.impl.MediaDao;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.CSVReader;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tis.mpl.facade.imageupload.MplImageUploadFacade;
import com.tisl.mpl.constants.MarketplacecheckoutaddonConstants;
import com.tisl.mpl.core.constants.MarketplaceCoreConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author Nirav Bhanushali
 *
 */

@Controller
@RequestMapping(value = "/my-account-upload")
public class ImageUploadController extends AbstractMplSearchPageController
{

	@Resource(name = ModelAttributetConstants.ACCOUNT_BREADCRUMB_BUILDER)
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@Autowired
	private MplImageUploadFacade mplImageUploadFacade;

	@Autowired
	private CommonsMultipartResolver multipartResolver;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private UserService userService;

	@Resource(name = "mediaDao")
	private MediaDao mediaDao;



	@Resource(name = "sessionService")
	private SessionService sessionService;

	/**
	 * @return the sessionService
	 */
	@Override
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	private static final String REDIRECTURL = "/my-account";
	private static final String TEMPIMAGESDIR = "/tempImages";
	private static final String PARAFOLDER_NAME = "folderName";
	public static final String PAGEID = "imageUpload";
	public static final String UPLOAD_FILE_PATH = "mpl.bulkimage.uploadpath";
	private static final String CATALOG_DATA = "mplContentCatalog";
	private static final String CATALOG_VERSION_DATA = "Online";
	private static final String CATALOG_VERSION_DATA_STAGED = "Staged";
	private static final String siteResource = "file:/";
	private static final String IMAGESFOLDER_NAME = "images";
	public static final String CONTEXT_PARAM_MARKERS = "//";
	private static final String URL_SEPARATOR = "/";

	private static final Logger LOG = LoggerFactory.getLogger(ImageUploadController.class);

	private static final String IMPORT_DATA = "$catalogversion=catalogversion(catalog(id[default='" + CATALOG_DATA
			+ "']),version[default='" + CATALOG_VERSION_DATA + "'])" + "[unique=true,default='" + CATALOG_DATA + ":"
			+ CATALOG_VERSION_DATA + "'] \n"
			+ "INSERT_UPDATE Media;code[unique=true];$catalogversion;mime[default='image/jpeg'];realfilename;@media[translator=de.hybris.platform.impex.jalo.media.MediaDataTranslator];folder(qualifier)[default='root']\n";

	private static final String IMPORT_DATA_STAGED = "$catalogversion=catalogversion(catalog(id[default='" + CATALOG_DATA
			+ "']),version[default='" + CATALOG_VERSION_DATA_STAGED + "'])" + "[unique=true,default='" + CATALOG_DATA + ":"
			+ CATALOG_VERSION_DATA_STAGED + "'] \n"
			+ "INSERT_UPDATE Media;code[unique=true];$catalogversion;mime[default='image/jpeg'];realfilename;@media[translator=de.hybris.platform.impex.jalo.media.MediaDataTranslator];folder(qualifier)[default='root']\n";


	@RequestMapping(value = "/images", method = RequestMethod.GET)
	@RequireHardLogIn
	public String getImageUploaded(final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		final List<MediaFolderModel> mediaFolder = getMediaFolderList();
		storeCmsPageInModel(model, getContentPageForLabelOrId(PAGEID));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PAGEID));
		model.addAttribute("mediaFolderList", mediaFolder);
		model.addAttribute(ModelAttributetConstants.BREADCRUMBS, accountBreadcrumbBuilder.getBreadcrumbs(PAGEID));
		model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
		boolean userAccessCheck = false;
		final UserModel userModel = userService.getCurrentUser();
		final Set<PrincipalGroupModel> groups = userModel.getAllGroups();

		if (null != groups && !groups.isEmpty() && groups.iterator().hasNext())
		{
			for (final PrincipalGroupModel principalGroupModel : groups)
			{
				if (principalGroupModel.getUid().equalsIgnoreCase("admingroup"))
				{
					userAccessCheck = true;
					break;
				}
			}
		}

		if (userAccessCheck)
		{
			return getViewForPage(model);
		}

		LOG.error("USER IS NOT ADMIN");
		GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
				null != userModel.getName() ? userModel.getName() : userModel.getUid() + " does not have admin role for access");
		return MarketplacecheckoutaddonConstants.REDIRECT + REDIRECTURL;

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
	@RequireHardLogIn
	public org.json.simple.JSONArray imageUpload(@RequestParam final MultipartFile[] file,
			final MultipartHttpServletRequest request) throws ServletException, IOException
	{


		final JSONArray returnObject = new JSONArray();
		//final StringBuilder stringBuilder = new StringBuilder();
		final List<String> list = new ArrayList<String>();
		String fileUploadLocation = null;
		String uploadFolderName = StringUtils.EMPTY;

		try
		{
			final String mediaHostName = configurationService.getConfiguration().getString(MarketplaceCoreConstants.MEDIA_HOST_NAME);

			if (null != configurationService)
			{
				fileUploadLocation = (configurationService.getConfiguration().getString(UPLOAD_FILE_PATH) + TEMPIMAGESDIR).toString();
				if (null != fileUploadLocation && !fileUploadLocation.isEmpty())
				{
					final Path path = Paths.get(fileUploadLocation);
					//if directory exists?
					if (!Files.exists(path))
					{
						try
						{
							Files.createDirectories(path);
						}
						catch (final IOException e)
						{
							//fail to create directory
							LOG.error("Exception Occer While Creating the File Location :" + e.getMessage());
						}
					}
				}
			}
			if (null != request.getParameterValues(PARAFOLDER_NAME))
			{
				uploadFolderName = request.getParameterValues(PARAFOLDER_NAME)[0];
			}
			else
			{
				uploadFolderName = "root";
			}
			//File f = new File("");
			for (final MultipartFile files : file)
			{
				final StringBuilder stringBuilder = new StringBuilder();
				if (files.isEmpty())
				{
					continue; //next pls
				}
				try
				{
					final byte[] bytes = files.getBytes();
					final Path path = Paths.get(fileUploadLocation + "/" + files.getOriginalFilename());
					//f = new File(fileUploadLocation + files.getOriginalFilename());
					Files.write(path, bytes);
					final String fileExtension[] = files.getOriginalFilename().toString().split("\\.");
					stringBuilder.append(";" + files.getOriginalFilename() + ";;image/" + fileExtension[1] + ";;" + siteResource + path
							+ ";" + uploadFolderName + ";/n");
					LOG.info("Upload File Path" + path);



					final InputStream inputStreamStaged = new ByteArrayInputStream(
							(IMPORT_DATA_STAGED + stringBuilder.toString()).getBytes());
					final InputStream inputStream = new ByteArrayInputStream((IMPORT_DATA + stringBuilder.toString()).getBytes());

					//create stream reader
					CSVReader readerStaged = null;
					CSVReader reader = null;

					readerStaged = new CSVReader(inputStreamStaged, "UTF-8");
					reader = new CSVReader(inputStream, "UTF-8");

					//final String imageName[] = files.getOriginalFilename().toString().split("\\.");
					getSessionService().setAttribute("uploadImageName", fileExtension[0]);

					// import
					MediaDataTranslator.setMediaDataHandler(new DefaultMediaDataHandler());
					Importer importerStaged = null;
					Importer importer = null;

					//ImpExException error = null;
					try
					{
						importerStaged = new Importer(readerStaged);
						importerStaged.getReader().enableCodeExecution(true);
						importerStaged.setMaxPass(-1);
						importerStaged.setDumpHandler(new FirstLinesDumpReader());
						importerStaged.importAll();

						importer = new Importer(reader);
						importer.getReader().enableCodeExecution(true);
						importer.setMaxPass(-1);
						importer.setDumpHandler(new FirstLinesDumpReader());
						importer.importAll();
					}
					catch (final ImpExException e)
					{
						path.toFile().delete();
						getSessionService().removeAttribute("uploadImageName");
						e.printStackTrace();
					}
					path.toFile().delete();
					getSessionService().removeAttribute("uploadImageName");
					list.add(files.getOriginalFilename());
				}
				catch (final IOException e)
				{
					LOG.error("Error while loading Impex for Image Upload Controller: " + e.getMessage());
				}
			}
			for (final String object : list)
			{
				final org.json.simple.JSONObject jObject = new org.json.simple.JSONObject();
				//final Media mediaData = getMediaByCode(object);
				final List<MediaModel> mediaModel = mediaDao.findMediaByCode(object);
				for (final MediaModel mediaData : mediaModel)
				{
					if (mediaData.getCatalogVersion().getVersion().equalsIgnoreCase("Online"))
					{
						jObject.put("imageName", mediaData.getCode());
						if (!uploadFolderName.equalsIgnoreCase(IMAGESFOLDER_NAME))
						{
							final StringBuilder sb = new StringBuilder(
									CONTEXT_PARAM_MARKERS + mediaHostName + URL_SEPARATOR + "medias/");
							sb.append(MarketplaceCoreConstants.SYSTEM).append(getTenantId()).append(URL_SEPARATOR);
							sb.append(mediaData.getLocation());
							LOG.debug("getUrlForMedia - " + sb.toString());

							jObject.put("imageUrl", sb.toString());
						}
						else
						{
							jObject.put("imageUrl", mediaData.getURL());
						}
						jObject.put("size", mediaData.getSize());
						jObject.put("creationTime", new Date());
						returnObject.add(jObject);
					}

				}
			}

			return returnObject;
		}
		catch (final Exception e)
		{
			LOG.error("Error accured in ImageUploadController: " + e.getMessage());
		}
		finally
		{
			MediaDataTranslator.unsetMediaDataHandler();
		}
		return returnObject;
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

	protected String getTenantId()
	{
		return Registry.getCurrentTenantNoFallback().getTenantID();
	}

}
