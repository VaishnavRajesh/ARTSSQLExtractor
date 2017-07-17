package za.co.tfg.arts.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryIterator {

	public static void main(String[] args) throws IOException {
		new DirectoryIterator().start();
	}

	private void start() throws IOException{
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



}
