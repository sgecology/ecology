/**
 * 
 */
package net.ecology.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * @author ducbq
 *
 */
public class SystemIOUtility {
	public static List<InputStream> getZipFileInputStreams(File file) throws ZipException, IOException {
		List<InputStream> resp = CollectionsUtility.newList();
		ZipFile zipFile;
			zipFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
			ZipEntry zipEntry = null;
			while (zipEntries.hasMoreElements()) {
				zipEntry = (ZipEntry) zipEntries.nextElement();
				resp.add(buildInputStream(zipFile.getInputStream(zipEntry)));
			}
			zipFile.close();
		return resp;
	}

	public static List<InputStream> extractZipFile(File zipFile, List<String> zipEntryNames) throws IOException {
		List<InputStream> resp = CollectionsUtility.newList();
		ZipFile innerZipFile = null;
		Enumeration<? extends ZipEntry> zipEntries = null;
		ZipEntry zipEntry = null;
		try {
			innerZipFile = new ZipFile(zipFile);
			zipEntries = innerZipFile.entries();
			while (zipEntries.hasMoreElements()) {
				zipEntry = (ZipEntry) zipEntries.nextElement();
				if (zipEntryNames.contains(zipEntry.getName())) {
					resp.add(buildInputStream(innerZipFile.getInputStream(zipEntry)));
				}
			}
		} finally {
      	innerZipFile.close();
		}
		return resp;
	}

	public static Map<String, InputStream> extractZipInputStreams(File zipFile) throws ZipException, IOException  {
		Map<String, InputStream> resp = CollectionsUtility.newMap();
		ZipFile innerZipFile = null;
		Enumeration<? extends ZipEntry> zipEntries = null;
		ZipEntry zipEntry = null;
		try {
			innerZipFile = new ZipFile(zipFile);
			zipEntries = innerZipFile.entries();
			while (zipEntries.hasMoreElements()) {
				zipEntry = (ZipEntry) zipEntries.nextElement();
				resp.put(zipEntry.getName(), cloneInputStream(innerZipFile.getInputStream(zipEntry)));
			}
		} finally {
      	innerZipFile.close();
		}
		return resp;
	}

	public static InputStream cloneInputStream(final InputStream inputStream) throws IOException  {
		InputStream clonedInputStream = null;
		ByteArrayOutputStream outputStream = null;
		byte[] buffer = null;
		int readLength = 0;
		  if (null == inputStream)
				return null;

		  inputStream.mark(0);
			outputStream = new ByteArrayOutputStream();
			buffer = new byte[1024];
			readLength = 0;
			while ((readLength = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, readLength);
			}
			//inputStream.reset();
			outputStream.flush();
			clonedInputStream = new ByteArrayInputStream(outputStream.toByteArray());
		return clonedInputStream;
	}

	public static Map<String, InputStream> extractZipInputStreams(File zipFile, List<String> zipEntryNames) throws ZipException, IOException  {
		if (CommonUtility.isEmpty(zipEntryNames))
			return extractAllZipInputStreams(zipFile);

		Map<String, InputStream> resp = CollectionsUtility.newMap();
		ZipFile innerZipFile = null;
		Enumeration<? extends ZipEntry> zipEntries = null;
		ZipEntry zipEntry = null;
		try {
			innerZipFile = new ZipFile(zipFile);
			zipEntries = innerZipFile.entries();
			if (CommonUtility.isNotEmpty(zipEntryNames)) {
				while (zipEntries.hasMoreElements()) {
					zipEntry = (ZipEntry) zipEntries.nextElement();
					if (zipEntryNames.contains(zipEntry.getName())) {
						resp.put(zipEntry.getName(), buildInputStream(innerZipFile.getInputStream(zipEntry)));
					}
				}
			} else {
				while (zipEntries.hasMoreElements()) {
					zipEntry = (ZipEntry) zipEntries.nextElement();
					resp.put(zipEntry.getName(), buildInputStream(innerZipFile.getInputStream(zipEntry)));
				}
			}
		} finally {
      	innerZipFile.close();
		}
		return resp;
	}

	public static Map<String, InputStream> extractAllZipInputStreams(File zipFile) throws ZipException, IOException  {
		Map<String, InputStream> resp = CollectionsUtility.newMap();
		ZipFile innerZipFile = null;
		Enumeration<? extends ZipEntry> zipEntries = null;
		ZipEntry zipEntry = null;
		try {
			innerZipFile = new ZipFile(zipFile);
			zipEntries = innerZipFile.entries();

			while (zipEntries.hasMoreElements()) {
				zipEntry = (ZipEntry) zipEntries.nextElement();
				resp.put(zipEntry.getName(), buildInputStream(innerZipFile.getInputStream(zipEntry)));
			}
		} finally {
      	innerZipFile.close();
		}
		return resp;
	}

	public static InputStream buildInputStream(final String fileName, final byte[] data) throws IOException  {
		InputStream inputStream = null;
		File tempDataFile = null;
		  if (CommonUtility.isEmpty(data))
				return null;

			String baseName = getFileBaseName(fileName);
			String extension = getFileExtension(fileName);
			tempDataFile = File.createTempFile(baseName, "." + extension);
			tempDataFile.deleteOnExit();

			//FileCopyUtils.copy(data, tempDataFile);

			FileUtils.writeByteArrayToFile(tempDataFile, data);
			inputStream = new FileInputStream(tempDataFile);
		return inputStream;
	}

	public static Map<String, InputStream> buildDataStream(final String name, final byte[] buffer) throws IOException  {
		Map<String, InputStream> ioContainer = CollectionsUtility.newMap();
		File tempDataFile = null;
		  if (CommonUtility.isEmpty(buffer))
				return null;

			String baseName = getFileBaseName(name);
			String extension = getFileExtension(name);
			tempDataFile = File.createTempFile(baseName, "." + extension);
			tempDataFile.deleteOnExit();
			FileUtils.writeByteArrayToFile(tempDataFile, buffer);
			ioContainer.put(name, new FileInputStream(tempDataFile));
		return ioContainer;
	}

	public static File createDataFile(final String fileName, final byte[] data) throws IOException  {
		File targetDataFile = null;
		  if (CommonUtility.isEmpty(data))
				return null;

			String baseName = getFileBaseName(fileName);
			String extension = getFileExtension(fileName);
			targetDataFile = File.createTempFile(baseName, "." + extension);
			targetDataFile.deleteOnExit();

			FileUtils.writeByteArrayToFile(targetDataFile, data);
		return targetDataFile;
	}

	public static File createFileFromInputStream(final String originFileName, final InputStream inputStream) throws IOException  {
		File targetDataFile = null;
		  if (CommonUtility.isEmpty(inputStream))
				return null;

			String baseName = getFileBaseName(originFileName);
			String extension = getFileExtension(originFileName);
			targetDataFile = File.createTempFile(baseName, "." + extension);
			targetDataFile.deleteOnExit();

			FileUtils.copyInputStreamToFile(inputStream, targetDataFile);
		return targetDataFile;
	}

	public static byte[] getByteArray(final File dataFile) throws IOException {
		return FileUtils.readFileToByteArray(dataFile);
	}

	public static byte[] getByteArray(final InputStream inputStream) throws IOException {
		return IOUtils.toByteArray(inputStream);
	}


	public static String getFileExtension(String path) {
		int lastSepPos = path.lastIndexOf(CommonConstants.FILE_EXTENSION_SEPARATOR);
		return path.substring(lastSepPos+1);
	}

	public static String getFileBaseName(String path) {
		int lastPathSep = path.lastIndexOf(CommonConstants.FILE_PATH_SEPARATOR);
		int extSepPos = path.lastIndexOf(CommonConstants.FILE_EXTENSION_SEPARATOR);
		return path.substring(lastPathSep+1, extSepPos);
	}

	public static void moveFile(String fromPath, String toPath) {
		File file = new File(fromPath);
		File processedFile = new File(toPath);
		if (processedFile.exists()) {
			processedFile.delete();
		}
		file.renameTo(processedFile);
	}

	public static void deleteFile(String fileName){
		File delFile = new File(fileName);
		delFile.delete();
	}

	public static void deleteFiles(List<File> files){
		for (File file : files) {
			file.delete();
		}
	}

	protected static InputStream buildInputStream(final InputStream inputStream) throws IOException {
		InputStream clonedInputStream = null;
		ByteArrayOutputStream outputStream = null;
		byte[] buffer = null;
		int readLength = 0;
		  if (null == inputStream)
				return null;

		  inputStream.mark(0);
			outputStream = new ByteArrayOutputStream();
			buffer = new byte[1024];
			readLength = 0;
			while ((readLength = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, readLength);
			}
			//inputStream.reset();
			outputStream.flush();
			clonedInputStream = new ByteArrayInputStream(outputStream.toByteArray());
		return clonedInputStream;
	}

	public static InputStream[] cloneInputStreams(InputStream inputStream, byte clonedCounter) throws IOException {
		InputStream[] targetInputStreams = new InputStream[clonedCounter];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		inputStream.transferTo(baos);
		for (byte idx = 0; idx < clonedCounter; idx++) {
			targetInputStreams[idx] = new ByteArrayInputStream(baos.toByteArray());
		}
		return targetInputStreams;
	}

	public static byte[] toByteArray(Object inStreamObject) throws IOException {
		return IOUtils.toByteArray((InputStream)inStreamObject);
	}
}