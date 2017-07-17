package za.co.tfg.arts.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DirectoryIterator {

	public static void main(String[] args) throws IOException {
		new DirectoryIterator().traverseCordysFiles(CONSTANTS.LFS_JAR_FILE_LOCATION);
	}

	private void initiateExtractionProcess() throws IOException{
		traverseZIPFileDirectory(CONSTANTS.ZIP_FILE_LOCATION);

		File [] files = getFileList(new File(CONSTANTS.UNZIP_FILE_LOCATION));
		for (int i = 0; i < files.length; i++){

			File path = files[i];
			String outputFilePath = CONSTANTS.DB_SQL_OUTPUT_FILE_LOCATION+File.separator+path.getName()+".txt";
			Path file = Paths.get(outputFilePath);
			if(Files.exists(file))
				Files.delete(file);			
			new DirectoryIterator().traverseSourceFileDirectory(path,outputFilePath);
		}
	}

	private void traverseSourceFileDirectory(File path,String outputFilePath) throws IOException {


		File [] files = getFileList(path);
		for (int i = 0; i < files.length; i++){
			if (files[i].isFile()){ 
				new CodeParser().processFile(files[i].getAbsolutePath(),outputFilePath);
			}else{
				traverseSourceFileDirectory(files[i],outputFilePath);
			}
		}

	}

	private File[] getFileList(File path){
		return path.listFiles();
	}

	private void traverseZIPFileDirectory(String directoryPath) throws IOException{
		File [] files = getFileList(new File(directoryPath));
		for (int i = 0; i < files.length; i++){
			if (files[i].isFile() && files[i].getName().indexOf(".zip")>-1){ 
				Decompressor.unzipFile(files[i].getAbsolutePath());
			}
		}
	}
	
	
	public static void copyJarFile(JarFile jarFile, File destDir) throws IOException {
	       String fileName = jarFile.getName();
	       String fileNameLastPart = fileName.substring(fileName.lastIndexOf(File.separator));
	       File destFile = new File(destDir, fileNameLastPart);

	       JarOutputStream jos = new JarOutputStream(new FileOutputStream(destFile));
	       Enumeration<JarEntry> entries = jarFile.entries();

	       while (entries.hasMoreElements()) {
	           JarEntry entry = entries.nextElement();
	           InputStream is = jarFile.getInputStream(entry);

	           //jos.putNextEntry(entry);
	           //create a new entry to avoid ZipException: invalid entry compressed size
	           jos.putNextEntry(new JarEntry(entry.getName()));
	           byte[] buffer = new byte[4096];
	           int bytesRead = 0;
	           while ((bytesRead = is.read(buffer)) != -1) {
	               jos.write(buffer, 0, bytesRead);
	           }
	           is.close();
	           jos.flush();
	           jos.closeEntry();
	       }
	       jos.close();
	   }
	
	
	private boolean isTFGJarFile(String jarFileName){

		    Pattern pattern = Pattern.compile("ROP|tfg|Retail",Pattern.CASE_INSENSITIVE);
		    Matcher m = pattern.matcher(jarFileName);
		    if(m.find()){
		    	return true;
		    }
		    return false;
		
	}
	
	private void traverseCordysFiles(String directoryPath) throws FileNotFoundException, IOException{
		File [] files = getFileList(new File(directoryPath));
		for (int i = 0; i < files.length; i++){
			String jartFileName = files[i].getName();
			if (files[i].isFile() && jartFileName.indexOf(".jar")>-1 && isTFGJarFile(jartFileName)){				  
				
				String outputFilePath = CONSTANTS.OUTPUT_JAR_FILE_LOCATION+File.separator+files[i].getName();
				File file = new File(outputFilePath);
				
				file.getParentFile().mkdirs();
				/*if(file.exists()){
					file.delete();	
				}
				file.createNewFile();
				*/
				copyJarFile(new JarFile(files[i]), new File(CONSTANTS.OUTPUT_JAR_FILE_LOCATION));
			
			}else{
				if(files[i].isDirectory()){
					traverseCordysFiles(files[i].getAbsolutePath());
				}
			}
		}
	}
	
}
