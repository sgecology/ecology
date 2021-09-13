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
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import net.ecology.exceptions.CerberusException;
import net.ecology.global.GlobeConstants;
import net.ecology.model.IOContainer;

/**
 * @author ducbq
 *
 */
public class FileIOUtility {
	public static List<InputStream> getZipFileInputStreams(File file) throws CerberusException {
		List<InputStream> resp = CollectionsUtility.newList();
		ZipFile zipFile;
		try {
			zipFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
			ZipEntry zipEntry = null;
			while (zipEntries.hasMoreElements()) {
				zipEntry = (ZipEntry) zipEntries.nextElement();
				resp.add(buildInputStream(zipFile.getInputStream(zipEntry)));
			}
			zipFile.close();
		} catch (IOException e) {
			throw new CerberusException(e);
		}
		return resp;
	}

	public static List<InputStream> extractZipFile(File zipFile, List<String> zipEntryNames) throws CerberusException {
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
		} catch (IOException e) {
			throw new CerberusException(e);
		} finally {
      try {
      	innerZipFile.close();
      } catch (Exception e2) {
      	throw new CerberusException(e2);
      }			
		}
		return resp;
	}

	public static Map<String, InputStream> extractZipInputStreams(File zipFile) throws CerberusException {
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
		} catch (IOException e) {
			throw new CerberusException(e);
		} finally {
      try {
      	innerZipFile.close();
      } catch (Exception e2) {
      	throw new CerberusException(e2);
      }			
		}
		return resp;
	}

	public static InputStream cloneInputStream(final InputStream inputStream) throws CerberusException {
		InputStream clonedInputStream = null;
		ByteArrayOutputStream outputStream = null;
		byte[] buffer = null;
		int readLength = 0;
		try {
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
		} catch (Exception ex) {
			throw new CerberusException(ex);
		}
		return clonedInputStream;
	}

	public static Map<String, InputStream> extractZipInputStreams(File zipFile, List<String> zipEntryNames) throws CerberusException {
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
		} catch (IOException e) {
			throw new CerberusException(e);
		} finally {
      try {
      	innerZipFile.close();
      } catch (Exception e2) {
      	throw new CerberusException(e2);
      }			
		}
		return resp;
	}

	public static Map<String, InputStream> extractAllZipInputStreams(File zipFile) throws CerberusException {
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
		} catch (IOException e) {
			throw new CerberusException(e);
		} finally {
      try {
      	innerZipFile.close();
      } catch (Exception e2) {
      	throw new CerberusException(e2);
      }			
		}
		return resp;
	}

	public static InputStream buildInputStream(final String fileName, final byte[] data) throws CerberusException {
		InputStream inputStream = null;
		File tempDataFile = null;
		try {
		  if (CommonUtility.isEmpty(data))
				return null;

			String baseName = getFileBaseName(fileName);
			String extension = getFileExtension(fileName);
			tempDataFile = File.createTempFile(baseName, "." + extension);
			tempDataFile.deleteOnExit();

			//FileCopyUtils.copy(data, tempDataFile);

			FileUtils.writeByteArrayToFile(tempDataFile, data);
			inputStream = new FileInputStream(tempDataFile);
		} catch (Exception ex) {
			throw new CerberusException(ex);
		}
		return inputStream;
	}

	public static IOContainer buildDataStream(final String name, final byte[] buffer) throws CerberusException {
		IOContainer ioContainer = IOContainer.builder().build();
		File tempDataFile = null;
		try {
		  if (CommonUtility.isEmpty(buffer))
				return null;

			String baseName = getFileBaseName(name);
			String extension = getFileExtension(name);
			tempDataFile = File.createTempFile(baseName, "." + extension);
			tempDataFile.deleteOnExit();
			FileUtils.writeByteArrayToFile(tempDataFile, buffer);
			ioContainer.put(name, new FileInputStream(tempDataFile));
		} catch (Exception ex) {
			throw new CerberusException(ex);
		}
		return ioContainer;
	}

	public static File createDataFile(final String fileName, final byte[] data) throws CerberusException {
		File targetDataFile = null;
		try {
		  if (CommonUtility.isEmpty(data))
				return null;

			String baseName = getFileBaseName(fileName);
			String extension = getFileExtension(fileName);
			targetDataFile = File.createTempFile(baseName, "." + extension);
			targetDataFile.deleteOnExit();

			FileUtils.writeByteArrayToFile(targetDataFile, data);
		} catch (Exception ex) {
			throw new CerberusException(ex);
		}
		return targetDataFile;
	}

	public static File createFileFromInputStream(final String originFileName, final InputStream inputStream) throws CerberusException {
		File targetDataFile = null;
		try {
		  if (CommonUtility.isEmpty(inputStream))
				return null;

			String baseName = getFileBaseName(originFileName);
			String extension = getFileExtension(originFileName);
			targetDataFile = File.createTempFile(baseName, "." + extension);
			targetDataFile.deleteOnExit();

			FileUtils.copyInputStreamToFile(inputStream, targetDataFile);
		} catch (Exception ex) {
			throw new CerberusException(ex);
		}
		return targetDataFile;
	}

	public static byte[] getByteArray(final File dataFile) throws IOException {
		return FileUtils.readFileToByteArray(dataFile);
	}

	public static byte[] getByteArray(final InputStream inputStream) throws IOException {
		return IOUtils.toByteArray(inputStream);
	}


	public static String getFileExtension(String path) {
		int lastSepPos = path.lastIndexOf(GlobeConstants.FILE_EXTENSION_SEPARATOR);
		return path.substring(lastSepPos+1);
	}

	public static String getFileBaseName(String path) {
		int lastPathSep = path.lastIndexOf(GlobeConstants.FILE_PATH_SEPARATOR);
		int extSepPos = path.lastIndexOf(GlobeConstants.FILE_EXTENSION_SEPARATOR);
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

	protected static InputStream buildInputStream(final InputStream inputStream) throws CerberusException {
		InputStream clonedInputStream = null;
		ByteArrayOutputStream outputStream = null;
		byte[] buffer = null;
		int readLength = 0;
		try {
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
		} catch (Exception ex) {
			throw new CerberusException(ex);
		}
		return clonedInputStream;
	}
}
