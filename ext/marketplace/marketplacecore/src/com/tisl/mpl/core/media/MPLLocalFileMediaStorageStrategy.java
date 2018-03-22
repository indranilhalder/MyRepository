/**
 *
 */
package com.tisl.mpl.core.media;

import de.hybris.platform.media.exceptions.MediaNotFoundException;
import de.hybris.platform.media.exceptions.MediaRemovalException;
import de.hybris.platform.media.exceptions.MediaStoreException;
import de.hybris.platform.media.services.MediaLocationHashService;
import de.hybris.platform.media.services.MimeService;
import de.hybris.platform.media.services.impl.HierarchicalMediaPathBuilder;
import de.hybris.platform.media.storage.LocalStoringStrategy;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import de.hybris.platform.media.storage.MediaStorageStrategy;
import de.hybris.platform.media.storage.impl.StoredMediaData;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.MediaUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author Nirav Bhanushali
 *
 */

import com.google.common.base.Preconditions;


// Referenced classes of package de.hybris.platform.media.storage.impl:
//            StoredMediaData

public class MPLLocalFileMediaStorageStrategy implements MediaStorageStrategy, LocalStoringStrategy
{
	private static final Logger LOG = Logger.getLogger(MPLLocalFileMediaStorageStrategy.class);
	private File mainDataDir;
	private List replicationDirs;
	private MediaLocationHashService locationHashService;
	private MimeService mimeService;
	protected MediaStorageConfigService storageConfigService;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	/**
	 * @return the sessionService
	 */
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

	public MPLLocalFileMediaStorageStrategy()
	{
	}

	@Override
	public StoredMediaData store(final de.hybris.platform.media.storage.MediaStorageConfigService.MediaFolderConfig config,
			final String mediaId, final Map metaData, final InputStream dataStream)
	{
		String fileName;
		String relativeMediaDirPath;
		Collection fos;
		Collection files;
		File absoluteMediaDirPath;
		File media;
		Preconditions.checkNotNull(config);
		Preconditions.checkNotNull(mediaId);
		Preconditions.checkNotNull(metaData);
		Preconditions.checkNotNull(dataStream);

		final String orignalImageName = null != getSessionService().getAttribute("uploadImageName")
				? getSessionService().getAttribute("uploadImageName").toString() : StringUtils.EMPTY;

		if (null != orignalImageName && !orignalImageName.isEmpty())
		{
			fileName = getFileName(mediaId + "_" + orignalImageName, (String) metaData.get("mime"));
		}
		else
		{
			fileName = getFileName(mediaId, (String) metaData.get("mime"));
		}

		final HierarchicalMediaPathBuilder pathBuilder = HierarchicalMediaPathBuilder.forDepth(config.getHashingDepth());
		relativeMediaDirPath = pathBuilder.buildPath((String) metaData.get("folderPath"), fileName);
		final int replDirsSize = replicationDirs.size();
		fos = new ArrayList(replDirsSize);
		files = new ArrayList(replDirsSize);
		absoluteMediaDirPath = null;
		media = null;
		StoredMediaData storedmediadata;
		try
		{
			for (final Iterator iterator = replicationDirs.iterator(); iterator.hasNext();)
			{
				final File baseDir = (File) iterator.next();
				absoluteMediaDirPath = MediaUtil.composeOrGetParent(baseDir, relativeMediaDirPath);
				media = new File(absoluteMediaDirPath, fileName);
				files.add(media);
				final boolean fileCreated = createNewMediaFile(absoluteMediaDirPath, media);
				if (fileCreated)
				{
					fos.add(new FileOutputStream(media));
				}
				else
				{
					throw new MediaStoreException((new StringBuilder("New media file already exists! (mediaId: ")).append(mediaId)
							.append(", file:").append(media).append(", dir: ").append(absoluteMediaDirPath).append(")").toString());
				}
			}

			final long mediaSize = MediaUtil.copy(dataStream, fos, true);
			final String location = (new StringBuilder(String.valueOf(relativeMediaDirPath))).append(fileName).toString();
			final String hashForLocation = locationHashService.createHashForLocation(config.getFolderQualifier(), location);
			storedmediadata = new StoredMediaData(location, hashForLocation, mediaSize, (String) metaData.get("mime"));
		}
		catch (final IOException e)
		{
			closeInputStream(dataStream);
			closeOutputStreams(fos);
			throw new MediaStoreException((new StringBuilder("Error writing media file (mediaId: ")).append(mediaId)
					.append(", file:").append(media).append(", dir:").append(absoluteMediaDirPath).append(")").toString(), e);
		}
		closeInputStream(dataStream);
		closeOutputStreams(fos);
		return storedmediadata;
	}

	private String getFileName(final String mediaId, final String mime)
	{
		final String fileExtension = mimeService.getBestExtensionFromMime(mime);
		final String fileName = StringUtils.isNotBlank(fileExtension)
				? (new StringBuilder(String.valueOf(mediaId))).append('.').append(fileExtension).toString() : mediaId;
		return fileName;
	}

	private void closeInputStream(final InputStream dataStream)
	{
		try
		{
			dataStream.close();
		}
		catch (final IOException e)
		{
			LOG.error("cannot close stream", e);
		}
	}

	private void closeOutputStreams(final Collection fos)
	{
		for (final Iterator iterator = fos.iterator(); iterator.hasNext();)
		{
			final OutputStream outputStream = (OutputStream) iterator.next();
			try
			{
				outputStream.close();
			}
			catch (final IOException e)
			{
				LOG.error("cannot close stream", e);
			}
		}

	}

	@Override
	public void delete(final de.hybris.platform.media.storage.MediaStorageConfigService.MediaFolderConfig config,
			final String location)
	{
		Preconditions.checkArgument(config != null, "config is required!");
		Preconditions.checkArgument(location != null, "location is required!");
		for (final Iterator iterator = replicationDirs.iterator(); iterator.hasNext();)
		{
			final File baseDir = (File) iterator.next();
			final File media = MediaUtil.composeOrGetParent(baseDir, location);
			if (media.exists() && !media.delete())
			{
				throw new MediaRemovalException(
						(new StringBuilder("Removal of file: ")).append(media).append(" has failed.").toString());
			}
		}

	}

	@Override
	public InputStream getAsStream(final de.hybris.platform.media.storage.MediaStorageConfigService.MediaFolderConfig config,
			final String location)
	{
		Preconditions.checkArgument(config != null, "config is required!");
		Preconditions.checkArgument(location != null, "location is required!");
		final File media = MediaUtil.composeOrGetParent(mainDataDir, location);
		try
		{
			return new FileInputStream(media);
		}
		catch (final FileNotFoundException e)
		{
			throw new MediaNotFoundException(
					(new StringBuilder("Media not found (requested media location: ")).append(location).append(")").toString(), e);
		}
	}

	@Override
	public File getAsFile(final de.hybris.platform.media.storage.MediaStorageConfigService.MediaFolderConfig config,
			final String location)
	{
		Preconditions.checkArgument(config != null, "folderQualifier is required!");
		Preconditions.checkArgument(location != null, "location is required!");
		return MediaUtil.composeOrGetParent(mainDataDir, location);
	}

	@Override
	public long getSize(final de.hybris.platform.media.storage.MediaStorageConfigService.MediaFolderConfig config,
			final String location)
	{
		final File file = MediaUtil.composeOrGetParent(mainDataDir, location);
		if (file.exists())
		{
			return file.length();
		}
		else
		{
			throw new MediaNotFoundException(
					(new StringBuilder("Media not found (requested media location: ")).append(location).append(")").toString());
		}
	}

	private boolean createNewMediaFile(final File absoluteMediaDirPath, final File media) throws IOException
	{
		if (!absoluteMediaDirPath.exists())
		{
			absoluteMediaDirPath.mkdirs();
		}
		return media.createNewFile();
	}

	public void setStorageConfigService(final MediaStorageConfigService storageConfigService)
	{
		this.storageConfigService = storageConfigService;
	}

	public void setMainDataDir(final File mainDataDir)
	{
		this.mainDataDir = mainDataDir;
	}

	public void setReplicationDirs(final List replicationDirs)
	{
		this.replicationDirs = replicationDirs;
	}

	public void setLocationHashService(final MediaLocationHashService locationHashService)
	{
		this.locationHashService = locationHashService;
	}

	public void setMimeService(final MimeService mimeService)
	{
		this.mimeService = mimeService;
	}
}