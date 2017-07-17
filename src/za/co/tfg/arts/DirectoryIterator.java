package za.co.tfg.arts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryIterator {

	public static void main(String[] args) throws IOException {
		
		    File path = new File("C:\\Users\\rajeshv\\Downloads\\com.argility.rop.esb");
		    String outputFilePath = "C:/Users/rajeshv/Desktop/"+path.getName()+".txt";
		    Path file = Paths.get(outputFilePath);
			if(Files.exists(file))
				Files.delete(file);			
		    traverseDirectory(path,outputFilePath);
		}

	private static void traverseDirectory(File path,String outputFilePath) throws IOException {
		
		
		File [] files = path.listFiles();
	    for (int i = 0; i < files.length; i++){
	        if (files[i].isFile()){ 
	        	new CodeParser().processFile(files[i].getAbsolutePath(),outputFilePath);
	        }else{
	        	traverseDirectory(files[i],outputFilePath);
	        }
	    }
		
	}

	

}
