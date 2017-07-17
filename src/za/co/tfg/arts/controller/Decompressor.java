package za.co.tfg.arts.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Decompressor {

	 static byte[] buffer = new byte[1024];
	 
	public static void unzipFile(String file) throws IOException{		
        ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
        ZipEntry zipEntry = zis.getNextEntry();
        while(zipEntry != null){
            String fileName = zipEntry.getName();
            File newFile = new File(CONSTANTS.UNZIP_FILE_LOCATION + File.separator+new File(file).getName().replaceAll(".zip", "")+File.separator+fileName);
            newFile.getParentFile().mkdirs();
            if(!newFile.exists()){
            	if(newFile.getName().indexOf(".java") > -1){
            		newFile.createNewFile();
            	}else{
            		newFile.mkdir();
            		 zipEntry = zis.getNextEntry();
            		continue;
            	}
            }else{
            	if(newFile.isDirectory()){
            		 zipEntry = zis.getNextEntry();
            		continue;
            	}
            }
           
            FileOutputStream fos = new FileOutputStream(newFile,true);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
	}
}
