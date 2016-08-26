
package com.techouts.dataonboarding.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import javax.annotation.processing.FilerException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csvreader.CsvReader;


/**
 *
 */
public class BUCFileComparitor
{
	private static final Logger LOG = LoggerFactory.getLogger(BUCFileComparitor.class);


	private static final String FILE_SEP = File.separator;
	private static String FILE_PATH = null;
	private static final String DELETE_PATH = ".delete";
	private static final String INSERT_PATH = ".insert";
	private static final String UPDATE_PATH = ".update";
	private static final String NAME_DIV = "_";
	private static final String BCK_FOLER = ".backup";
	private static final String ORG_FOLER = ".original";
	private static final String INV_FOLER = ".invalid";
	private static final String ERROR_FILE = "File contain invalid records (records contain invalid column)";
	private static final String INVAL_HEDDES = "File has invalid hedders .";
	private static final String CSV = ".csv";

	/**
	 * Performs the main the process to determine the Delta.
	 */
	public void process(final String filePath, final String filename, final Set<String> fileHedders, final String previousFileName,
			final int noOfKeyFields)
	{

		FILE_PATH = filePath;
		LOG.debug("File is Processing for  compare {}", filename);

		final long time1 = System.currentTimeMillis();
		final File originalFile = new File(filePath, filename);
		File sortedFile = null;
		String sortedFileName = null;
		final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssS");
		final Date curDate = new Date();
		final String original = filename.replace(".csv", "");
		final String fileNameDate = original + NAME_DIV + format.format(curDate).concat(CSV);
		CsvReader tmpread = null;
		FileReader fileReader = null;
		try
		{
			FileUtils.copyFile(originalFile, new File(filePath + FILE_SEP + ORG_FOLER + FILE_SEP + fileNameDate));
			fileReader = new FileReader(originalFile);
			tmpread = new CsvReader(fileReader);
			tmpread.readHeaders();
			if (!validateHedders(tmpread.getHeaders(), fileHedders, originalFile, fileNameDate))
			{
				throw new FilerException(INVAL_HEDDES);
			}
			sortedFileName = sortFile(originalFile, fileHedders.size() - 1, fileNameDate);
			sortedFile = new File(sortedFileName);
			final String existFilename = filePath + FILE_SEP + BCK_FOLER + FILE_SEP + previousFileName;

			final File previousFile = new File(existFilename);
			if (previousFile.exists())
			{
				diffFile(previousFile, sortedFile, fileNameDate, noOfKeyFields);

			}
			else
			{
				FileUtils.copyFile(sortedFile, new File(FILE_PATH + FILE_SEP + INSERT_PATH + FILE_SEP + "I_" + fileNameDate));
				FileUtils.copyFile(sortedFile, previousFile);
			}
		}
		catch (final Exception e)
		{
			LOG.error("Error while processing : ", e.getMessage());


		}
		finally
		{
			tmpread.close();
			try
			{
				fileReader.close();
			}
			catch (final IOException e)
			{
				LOG.error(e.getMessage(), e);
			}
			sortedFile.delete();
			originalFile.delete();

		}

		final long time2 = System.currentTimeMillis();
		LOG.debug("=============== Total Time taken ={} {} ", (time2 - time1));


	}

	public String sortFile(final File file, final int noOfColums, final String fileNameDate) throws Exception
	{
		final String sortedFileName = file.getParent() + FILE_SEP + "S_" + file.getName();
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		PrintWriter printWriter = null;
		try
		{
			bufferedReader = new BufferedReader(new FileReader(file));
			bufferedWriter = new BufferedWriter(new FileWriter(sortedFileName));

			printWriter = new PrintWriter(bufferedWriter);
			final String hedders = bufferedReader.readLine();


			printWriter.println(hedders);
			final ArrayList<String> records = new ArrayList<String>();
			String record = bufferedReader.readLine();
			final StringBuffer invalidHeaderFileds = new StringBuffer();
			invalidHeaderFileds.append("Invalid headers records are : ");
			boolean isValidRecord = true;
			while (StringUtils.isNotBlank(record))
			{
				final int occurance = StringUtils.countMatches(record, ",");

				if (occurance != noOfColums)
				{
					isValidRecord = false;
					invalidHeaderFileds.append(record).append('\n');
				}
				records.add(record);
				record = bufferedReader.readLine();
			}
			if (!isValidRecord)
			{
				printWriter.close();
				bufferedReader.close();
				new File(sortedFileName).delete();
				final Path path = FileSystems.getDefault().getPath(file.getAbsolutePath());
				try
				{
					Files.write(path, String.format("\n" + invalidHeaderFileds, file.getName()).getBytes(), StandardOpenOption.APPEND);
				}
				catch (final IOException e)
				{
					LOG.error(e.getMessage(), e);
				}
				FileUtils.copyFile(file, new File(FILE_PATH + FILE_SEP + INV_FOLER + FILE_SEP + fileNameDate));
				file.delete();
				throw new FilerException(ERROR_FILE);
			}

			Collections.sort(records);
			for (final String newLine : records)
			{
				if (StringUtils.isNotBlank(newLine.trim()))
				{
					printWriter.println(newLine);
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new Exception(e);
		}
		finally
		{

			try
			{
				if (bufferedReader != null)
				{
					bufferedReader.close();
				}
				if (printWriter != null)
				{
					printWriter.close();
				}
				if (bufferedWriter != null)
				{
					bufferedWriter.flush();
					bufferedWriter.close();
					if (file.delete())
					{
						LOG.debug("deliting actal  file :{}", file.getAbsolutePath());
					}
					else
					{
						LOG.debug("unable to delete actal file :{}", file.getAbsolutePath());
					}
				}
			}
			catch (final Exception e1)
			{
				LOG.error(e1.getMessage());
			}
		}

		return sortedFileName;
	}


	private boolean validateHedders(final String[] hedders, final Set<String> fileHedders, final File originalFile,
			final String fileNameDate)
	{
		boolean isValidHedder = true;
		final StringBuffer invalidHeaderFileds = new StringBuffer();
		invalidHeaderFileds.append("Invalid headers fields :");
		for (final String headerField : hedders)
		{

			if (!fileHedders.contains(headerField))
			{
				invalidHeaderFileds.append(headerField).append(',');
				isValidHedder = false;

			}

		}
		if (!isValidHedder)
		{
			final Path path = FileSystems.getDefault().getPath(originalFile.getAbsolutePath());
			try
			{
				Files.write(path, String.format("\n" + invalidHeaderFileds, originalFile.getName()).getBytes(),
						StandardOpenOption.APPEND);
				FileUtils.copyFile(originalFile, new File(FILE_PATH + FILE_SEP + INV_FOLER + FILE_SEP + fileNameDate));
			}
			catch (final IOException e)
			{
				LOG.error(e.getMessage(), e);
			}

			originalFile.delete();
		}
		return isValidHedder;
	}

	/**
	 * Calculates the Diff between two files.
	 *
	 * @param previousFile
	 * @param sortedFile
	 * @param fileName
	 * @param keyFieldCount
	 * @throws IOException
	 */
	public void diffFile(final File previousFile, final File sortedFile, final String fileName, final int keyFieldCount)
			throws IOException
	{
		LOG.debug("File is Differentiating");
		final long time1 = System.currentTimeMillis();
		BufferedReader br1 = null;
		BufferedReader br2 = null;
		BufferedWriter outdelete = null;
		PrintWriter pwUpdate = null;
		PrintWriter pwDelete = null;
		PrintWriter pwInsert = null;
		try
		{
			br1 = new BufferedReader(new FileReader(previousFile));
			br2 = new BufferedReader(new FileReader(sortedFile));
			pwInsert = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH + FILE_SEP + "I_" + fileName)));
			outdelete = new BufferedWriter(new FileWriter(FILE_PATH + FILE_SEP + "D_" + fileName));
			pwUpdate = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH + FILE_SEP + "U_" + fileName)));
			pwDelete = new PrintWriter(outdelete);
			final ArrayList<String> updateList = new ArrayList<String>();
			final ArrayList<String> insertList = new ArrayList<String>();
			final ArrayList<String> delList = new ArrayList<String>();

			final String hedders = br2.readLine();
			// Add headers
			updateList.add(hedders);
			insertList.add(hedders);
			delList.add(hedders);
			// skip headers from compare
			br1.readLine();
			// start reading records from both files
			String oldLine = br1.readLine();
			String newLine = br2.readLine();
			while (oldLine != null && newLine != null)
			{
				if (oldLine.equals(newLine))
				{
					// Rows are same, so move forward in each file.(same record skip)
					oldLine = br1.readLine();
					newLine = br2.readLine();

				}
				else
				{
					// Records are different so now check what is the difference by compare keys
					final int keyComp = compareKeys(oldLine, newLine, keyFieldCount);

					if (keyComp == 0)
					{
						// Its an update.
						updateList.add(newLine);
						oldLine = br1.readLine();
						newLine = br2.readLine();
					}
					else if (keyComp < 0)
					{ // line1 < newLine
						  // its a Delete
						delList.add(oldLine);
						oldLine = br1.readLine();
					}
					else
					{ // line1 > line2
						  // its an insert.
						insertList.add(newLine);
						newLine = br2.readLine();
					}
				}
			} // First loop Ends

			// handle the left overs. Anything left over in file 1 is for delete.
			while (oldLine != null)
			{
				delList.add(oldLine);
				oldLine = br1.readLine();
			}

			// handle the left overs. Anything left over in file 2 is for insert.
			while (newLine != null)
			{
				insertList.add(newLine);
				newLine = br2.readLine();
			}

			// Now write all these into the file.
			writeToFile(pwUpdate, updateList);
			writeToFile(pwInsert, insertList);
			writeToFile(pwDelete, delList);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
		}
		finally
		{
			try
			{
				if (br1 != null)
				{
					br1.close();
				}
				if (br2 != null)
				{
					br2.close();
				}
				if (pwUpdate != null)
				{
					pwUpdate.flush();
					pwUpdate.close();
				}
				if (pwDelete != null)
				{
					pwDelete.flush();
					pwDelete.close();
				}
				if (pwInsert != null)
				{
					pwInsert.flush();
					pwInsert.close();
				}

			}
			catch (final Exception e1)
			{
				LOG.error(e1.getMessage());
			}
		}
		moveFile(fileName);
		sortedFile.renameTo(previousFile);
		final long time2 = System.currentTimeMillis();
		LOG.debug("Time Taken to calc Delta: " + (time2 - time1) + "Milli Sec");

	}

	private void writeToFile(final PrintWriter printWriter, final ArrayList<String> records)
	{
		for (final String tmpLine : records)
		{
			printWriter.println(tmpLine);
		}
	}

	private void moveFile(final String fileName) throws IOException
	{
		File temFile = new File(FILE_PATH + FILE_SEP + "D_" + fileName);

		File destFile = new File(FILE_PATH + FILE_SEP + DELETE_PATH, "D_" + fileName);

		if (countNoOfLines(temFile) > 2)
		{
			FileUtils.copyFile(temFile, destFile);
			temFile.delete();
		}
		else
		{
			temFile.delete();
		}
		temFile = new File(FILE_PATH + FILE_SEP + "I_" + fileName);
		destFile = new File(FILE_PATH + FILE_SEP + INSERT_PATH, "I_" + fileName);
		if (countNoOfLines(temFile) > 2)
		{
			FileUtils.copyFile(temFile, destFile);
			temFile.delete();
		}
		else
		{
			temFile.delete();
		}
		temFile = new File(FILE_PATH + FILE_SEP + "U_" + fileName);
		destFile = new File(FILE_PATH + FILE_SEP + UPDATE_PATH, "U_" + fileName);
		if (countNoOfLines(temFile) > 2)
		{
			FileUtils.copyFile(temFile, destFile);
			temFile.delete();
		}
		else
		{
			temFile.delete();
		}
	}

	private int compareKeys(final String recordLine1, final String recordLine2, final int keyFieldCount)
	{
		final String keyString1 = getKeyField(recordLine1, keyFieldCount);
		final String keyString2 = getKeyField(recordLine2, keyFieldCount);
		return keyString1.compareTo(keyString2);
	}

	private String getKeyField(final String recordLine1, final int keyFieldCount)
	{
		final String[] fields = recordLine1.split(",");
		final int fieldCount = (keyFieldCount < fields.length) ? keyFieldCount : fields.length;
		final StringBuffer buff = new StringBuffer();
		for (int i = 0; i < fieldCount; i++)
		{
			buff.append(fields[i]);
			buff.append(',');
		}
		return buff.toString();
	}

	public long countNoOfLines(final File temFile)
	{
		long totalNumberOfLines = 0L;
		LineNumberReader lineReader = null;
		try
		{
			lineReader = new LineNumberReader(new FileReader(temFile));
			lineReader.skip(Long.MAX_VALUE);
			totalNumberOfLines = lineReader.getLineNumber() + 1;
			lineReader.close();
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}

		LOG.debug(temFile.getName() + "records found " + totalNumberOfLines);
		return totalNumberOfLines;
	}
}