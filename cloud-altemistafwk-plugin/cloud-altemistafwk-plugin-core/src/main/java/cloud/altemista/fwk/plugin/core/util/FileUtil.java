/**
 * 
 */
package cloud.altemista.fwk.plugin.core.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 * Utility class related to files within the plug-in 
 * @author NTT DATA
 */
public class FileUtil {

	/**
	 * Gets a file that exists and is readable
	 * @param names the path of the file (set of name elements)
	 * @return File if the file exists and is readable; null otherwise
	 */
	public static File getFile(String... names) {
		
		// (sanity check)
		if (names == null) {
			return null;
		}
		
		File file = FileUtils.getFile(names);
		if (file.exists() && (!file.isDirectory()) && file.canRead()) {
			return file;
		}
		return null;
	}
	
	/**
	 * Gets a directory that exists and is actually a directory
	 * @param names the path of the file (set of name elements)
	 * @return File if the directory exists; null otherwise
	 */
	public static File getDirectory(String... names) {
		
		// (sanity check)
		if (names == null) {
			return null;
		}
		
		File file = FileUtils.getFile(names);
		if (file.exists() && file.isDirectory()) {
			return file;
		}
		return null;
	}
	
	/**
	 * Creates a filter that selects files or folders based on one or more wildcards.
	 * @param wildcards the wildcards to match
	 * @return the filter, or null if the wildcards array was null or empty
	 */
	public static IOFileFilter makeWildcard(String... wildcards) {
		
		if ((wildcards == null) || (wildcards.length == 0)) {
			return null;
		}
		
		return new WildcardFileFilter(wildcards, IOCase.INSENSITIVE);
	}
	
	/**
	 * Creates a filter that selects files based on one or more wildcards,
	 * and that matches any directory (so it is suitable for recursive algorithms)
	 * @param wildcards the wildcards to match, null means an unrestricted filter
	 * @return the filter
	 */
	public static IOFileFilter makeWildcardRecursive(String... wildcards) {
		
		return makeFileFilterRecursive(makeWildcard(wildcards));
	}
	
	/**
	 * Decorates a filter so that it only applies to files,
	 * and also makes the filter match any directory (so it is suitable for recursive algorithms)
	 * @param fileFilter the filter to decorate, null means an unrestricted filter
	 * @return the decorated filter, never null
	 */
	public static IOFileFilter makeFileFilterRecursive(IOFileFilter fileFilter) {
		
		return FileFilterUtils.or(FileFilterUtils.directoryFileFilter(), FileFilterUtils.makeFileOnly(fileFilter));
	}
	
	/**
	 * Conditionally moves files from one directory to another
	 * @param sourceFolder the source folder
	 * @param targetFolder the target folder
	 * @param filter FileFilter that matches the files to move, and the folders to recurse
	 * @param renameIfExists true if copied file should be renamed if the target file exists
	 * @return true if any file was actually moved; false otherwise
	 * @throws IOException in case of error
	 * @see #makeFileFilterRecursive(org.apache.commons.io.filefilter.IOFileFilter)
	 */
	public static boolean moveFiles(File sourceFolder, File targetFolder, FileFilter filter,
			boolean renameIfExists) throws IOException {
		
		// Retrieves the contents of the source folder
		final File[] contents = sourceFolder.listFiles(filter);
		if ((contents == null) || (contents.length == 0)) {
			return false; // (nothing to do)
		}

		// Ensures the destination folder exists
		FileUtils.forceMkdir(targetFolder);

		// Iterates the contents of the source folder
		boolean filesMoved = false;
		for (File source : contents) {
			File target = new File(targetFolder, source.getName());

			// If it is a directory, invoke this method recursively
			if (source.isDirectory()) {
				filesMoved |= moveFiles(source, target, filter, renameIfExists);

			// If it is a file, moves to the destination folder
			} else {
				moveFile(source, target, renameIfExists);
				filesMoved = true;
			}
		}
		return filesMoved;
	}
	
	/**
	 * Moves a file
	 * @param source the file to be moved
	 * @param target0 the destination file
	 * @param renameIfExists true if copied file should be renamed if the target file exists
	 * @throws IOException in case of error
	 */
	public static void moveFile(File source, File target0, boolean renameIfExists) throws IOException {
		
		// If the target does not exist, or the flag is false, simply try to move the file
		if ((!target0.exists()) || (!renameIfExists)) {
			FileUtils.moveFile(source, target0);
			return;
		}
		
		final String target0Path = target0.getPath();
		for (int i = 0; ; i++) {
			File target = new File(String.format("%s.%d", target0Path, ++i));
			if (!target.exists()) {
				FileUtils.moveFile(source, target);
				return;
			}
		}
	}
	
	/**
	 * Backs up a file
	 * @param source the file to be backed up
	 * @param keepOriginal true to make a copy; false to rename the file
	 * @throws IOException in case of error
	 */
	public static void backupFile(File source, boolean keepOriginal) throws IOException {
		
		if (!source.exists()) {
			return;
		}
		
		final String sourcePath = source.getPath();
		for (int i = 0; ; i++) {
			File target = new File(String.format("%s.%d", sourcePath, ++i));
			if (!target.exists()) {
				if (keepOriginal) {
					FileUtils.copyFile(source, target);
				} else {
					FileUtils.moveFile(source, target);
				}
				return;
			}
		}
	}
	
	/**
	 * Conditionally removes files from one directory
	 * @param sourceFolder the source folder
	 * @param filter FileFilter that matches the files to delete, and the folders to recurse
	 * @return true if any file was actually deleted; false otherwise
	 * @throws IOException in case of error
	 */
	public static boolean deleteFiles(File sourceFolder, FileFilter filter) throws IOException {

		// Retrieves the contents of the source folder
		final File[] contents = sourceFolder.listFiles(filter);
		if ((contents == null) || (contents.length == 0)) {
			return false; // (nothing to do)
		}

		// Iterates the contents of the source folder
		boolean filesDeleted = false;
		for (File source : contents) {

			// If it is a directory, invoke this method recursively
			if (source.isDirectory()) {
				filesDeleted |= deleteFiles(source, filter);

			// If it is a file, removes it
			} else {
				deleteFileQuietly(source);
			}
		}
		return filesDeleted;
	}
	
	/**
	 * Deletes a file, never throwing an exception.
	 * @param file file to delete, can be {@code null}
	 * @return {@code true} if the file was deleted, otherwise {@code false}
	 */
	public static boolean deleteFileQuietly(File file) {

		if (file == null) {
			return false;
		}

		try {
			return file.delete();
		} catch (Exception e) { // NOSONAR
			return false;
		}
	}
	
	/**
	 * Private default constructor (utility class)
	 */
	private FileUtil() {
		super();
	}
}
