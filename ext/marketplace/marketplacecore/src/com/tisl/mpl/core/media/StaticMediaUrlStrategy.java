package com.tisl.mpl.core.media;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.media.MediaSource;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import de.hybris.platform.media.url.MediaURLStrategy;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.MediaUtil;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.base.Preconditions;
import com.tisl.mpl.core.constants.MarketplaceCoreConstants;


public class StaticMediaUrlStrategy implements MediaURLStrategy
{


	private static final Logger LOG = Logger.getLogger(StaticMediaUrlStrategy.class);

	public static final String CONTEXT_PARAM_DELIM = "|";
	public static final String NO_CTX_PART_MARKER = "-";
	public static final String CONTEXT_PARAM = "context";
	public static final String MEDIA_LEGACY_PRETTY_URL = "media.legacy.prettyURL";
	private static final String URL_SEPARATOR = "/";
	private static final String MEDIAWEB_DEFAULT_CONTEXT = "/medias";
	public static final String CONTEXT_PARAM_MARK = "?";
	public static final String CONTEXT_PARAM_AMP = "&";
	public static final String CONTEXT_PARAM_EQ = "=";
	public static final String CONTEXT_PARAM_TRUE = "true";
	public static final String CONTEXT_PARAM_MARKERS = "//";


	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Value(MarketplaceCoreConstants.MEDIA_WEBROOT)
	private String mediaWebRoot;
	
	@Value(MarketplaceCoreConstants.MEDIA_LEGACY_PRETTY_URL_ENABLED)
	private boolean prettyUrlEnabled;

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Override
	public String getUrlForMedia(final MediaStorageConfigService.MediaFolderConfig config, final MediaSource mediaSource)
	{
		Preconditions.checkArgument(config != null, MarketplaceCoreConstants.FOLDER_CONF_REQ_ERROR);
		Preconditions.checkArgument(mediaSource != null, MarketplaceCoreConstants.MEDIA_SRC_REQ_ERROR);

		final String mediaHostName = getConfigurationService().getConfiguration()
				.getString(MarketplaceCoreConstants.MEDIA_HOST_NAME);

		if (!mediaHostName.isEmpty())
		{
			if (mediaSource.getSource() instanceof MediaModel)
			{
				final MediaModel media = (MediaModel) mediaSource.getSource();

				if (media.getLocation() != null)
				{

					/*
					 * final String protocol = MediaUtil.isCurrentRequestSSLModeEnabled() ? MarketplaceCoreConstants.HTTPS :
					 * MarketplaceCoreConstants.HTTP; final String location = mediaSource.getLocation();
					 *
					 * return new StringBuilder(protocol.length() + mediaHostName.length() + location.length() +
					 * 4).append(protocol)
					 * .append(CONTEXT_PARAM_MARKERS).append(mediaHostName).append(URL_SEPARATOR).append(mediaSource.
					 * getLocation()) .toString();
					 */

					final StringBuilder sb = new StringBuilder(CONTEXT_PARAM_MARKERS + mediaHostName + URL_SEPARATOR + "medias/");
					sb.append(MarketplaceCoreConstants.SYSTEM).append(getTenantId()).append(URL_SEPARATOR);
					//		sb.append(folderQualifier).append(URL_SEPARATOR);
					//		String realFileName = getRealFileNameForMedia(mediaSource);
					//		if (realFileName == null)
					//		{
					sb.append(mediaSource.getLocation());
					LOG.debug("getUrlForMedia - " + sb.toString());
					return sb.toString();

				}
				else
				{
					return new StringBuilder(media.getURL()).toString();
				}
			}
		}
		return assembleUrl(config.getFolderQualifier(), mediaSource);
	}

	public static String getSystemDir()
	{
		final String sysID = Registry.getCurrentTenant().getTenantID();
		return MarketplaceCoreConstants.SYSTEM + sysID.trim().toLowerCase();
	}


	private String assembleUrl(final String folderQualifier, final MediaSource mediaSource)
	{
		String result = "";
		if ((!(GenericValidator.isBlankOrNull(folderQualifier))) && (!(GenericValidator.isBlankOrNull(mediaSource.getLocation()))))
		{
			if (this.prettyUrlEnabled)
			{
				result = assembleLegacyURL(folderQualifier, mediaSource);
			}
			else
			{
				result = assembleURLWithMediaContext(folderQualifier, mediaSource);
			}
		}
		return result;
	}

	private String assembleLegacyURL(final String folderQualifier, final MediaSource mediaSource)
	{
		final StringBuilder sb = new StringBuilder(MediaUtil.addTrailingFileSepIfNeeded(getMediaWebRootContext()));
		sb.append(MarketplaceCoreConstants.SYSTEM).append(getTenantId()).append(URL_SEPARATOR);
		//		sb.append(folderQualifier).append(URL_SEPARATOR);
		//		String realFileName = getRealFileNameForMedia(mediaSource);
		//		if (realFileName == null)
		//		{
		sb.append(mediaSource.getLocation());
		//		}
		//		else
		//		{
		//			final String location = mediaSource.getLocation();
		//			final int lastDotIndex = location.lastIndexOf(46);
		//			final String basePath = location.substring(0, lastDotIndex);
		//			final String fileExtension = location.substring(lastDotIndex);
		//			final int lastDotIndexForRealFileName = realFileName.lastIndexOf(46);
		//			if (lastDotIndexForRealFileName != -1)
		//			{
		//				realFileName = realFileName.substring(0, lastDotIndexForRealFileName);
		//			}
		//			sb.append(basePath).append(URL_SEPARATOR).append(realFileName).append(fileExtension);
		//		}
		LOG.debug("assembleLegacyURL - " + sb.toString());
		return sb.toString();
	}

	private String assembleURLWithMediaContext(final String folderQualifier, final MediaSource mediaSource)
	{
		final StringBuilder builder = new StringBuilder(MediaUtil.addTrailingFileSepIfNeeded(getMediaWebRootContext()));

		final String realFilename = getRealFileNameForMedia(mediaSource);
		if (realFilename != null)
		{
			builder.append(realFilename);
		}

		builder.append(CONTEXT_PARAM_MARK).append(CONTEXT_PARAM).append(CONTEXT_PARAM_EQ);
		builder.append(assembleMediaLocationContext(folderQualifier, mediaSource));
		return builder.toString();
	}

	private String getRealFileNameForMedia(final MediaSource mediaSource)
	{
		final String realFileName = mediaSource.getRealFileName();
		return ((StringUtils.isNotBlank(realFileName)) ? MediaUtil.normalizeRealFileName(realFileName) : null);
	}

	public String getMediaWebRootContext()
	{
		return MediaUtil
				.addLeadingFileSepIfNeeded((StringUtils.isBlank(this.mediaWebRoot)) ? MEDIAWEB_DEFAULT_CONTEXT : this.mediaWebRoot);
	}

	private String assembleMediaLocationContext(final String folderQualifier, final MediaSource mediaSource)
	{
		final StringBuilder builder = new StringBuilder(getTenantId());
		builder.append(CONTEXT_PARAM_DELIM).append(folderQualifier.replace(CONTEXT_PARAM_DELIM, ""));
		builder.append(CONTEXT_PARAM_DELIM).append(mediaSource.getSize());
		builder.append(CONTEXT_PARAM_DELIM).append(getCtxPartOrNullMarker(mediaSource.getMime()));
		builder.append(CONTEXT_PARAM_DELIM).append(getCtxPartOrNullMarker(mediaSource.getLocation()));
		builder.append(CONTEXT_PARAM_DELIM).append(getCtxPartOrNullMarker(mediaSource.getLocationHash()));

		return new Base64(-1, null, true).encodeAsString(builder.toString().getBytes());
	}

	private String getCtxPartOrNullMarker(final String ctxPart)
	{
		return ((StringUtils.isNotBlank(ctxPart)) ? ctxPart : NO_CTX_PART_MARKER);
	}

	protected String getTenantId()
	{
		return Registry.getCurrentTenantNoFallback().getTenantID();
	}

	public String getDownloadUrlForMedia(final MediaStorageConfigService.MediaFolderConfig config, final MediaSource mediaSource)
	{
		final StringBuilder url = new StringBuilder(getUrlForMedia(config, mediaSource));
		if (this.prettyUrlEnabled)
		{
			url.append(CONTEXT_PARAM_MARK);
		}
		else
		{
			url.append(CONTEXT_PARAM_AMP);
		}
		url.append(MarketplaceCoreConstants.ATTACHMENT).append(CONTEXT_PARAM_EQ).append(CONTEXT_PARAM_TRUE);
		return url.toString();
	}

	public void setPrettyUrlEnabled(final boolean prettyUrlEnabled)
	{
		this.prettyUrlEnabled = prettyUrlEnabled;
	}


}