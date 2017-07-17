package za.co.tfg.arts.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import za.co.tfg.arts.model.Table;

public class CodeParser {
	static Pattern fieldNamePattern = Pattern.compile("\"([^\"]*)\"");
	static Pattern dataTypePattern = Pattern.compile("\\((.*?)\\)");
	private String fileName;

	private String getStringContent(String data){
		return getContent(data,"name");
	}

	private String getContent(String data, String type){
		Matcher m = null;
		if("attribute".equals(type))
			m = dataTypePattern.matcher(data);
		else if("name".equals(type))
			m = fieldNamePattern.matcher(data);
		if (m.find()) {
			return (m.group(1));
		}
		throw new RuntimeException("table/field details not found");
	}

	private String getAttributeType(String data){
		return getContent(data,"attribute").replaceAll("\\.(.)*", "");
	}
	private static int count = 1;
	public void processFile(String filePath, String outputFilePath)  {
		try
		{
			ArrayList<String> lines = readlinesFromFile(filePath);
			Table table = getTableDetailsFromLines(lines);

			if(table != null){				
				writeToFile(count+" "+table.toString()+"\n", outputFilePath);
				count++;
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private void writeToFile(String content,String outputFilePath ) throws IOException {		
		
		Files.write(Paths.get(outputFilePath), content.getBytes(),StandardOpenOption.CREATE,StandardOpenOption.APPEND);
		
	}

	private ArrayList<String> readlinesFromFile(String filePath) throws IOException {
		Path file = Paths.get(filePath);           
		fileName = file.toFile().getName().replaceAll("Base.java", "");		
		return (ArrayList<String>) Files.readAllLines(file, Charset.defaultCharset());
	}

	private Table getTableDetailsFromLines(ArrayList<String> lines) {
		Table table = null;
		

		for(String line : lines){				
			table = parseLine(table,line);
			
		}
		return table;
	}

	private Table parseLine(Table table,  String line) {

		if(table != null){
			if(line.indexOf("setJavaName")> -1){				
				table.getCurrentField().setLogicalName(getStringContent(line));
			}else if(line.indexOf("setColumnName")> -1){
				table.getCurrentField().setFieldName(getStringContent(line));
			}else if(line.indexOf("setAttributeClass")> -1){
				table.getCurrentField().setDataType(getAttributeType(line));				
				table.setPointerToNextField();
			}	
		}

		if(line.indexOf("classInfo.setTableName")> -1){
			String tableName = getStringContent(line);		
			table = new Table(tableName,fileName);			
		}		
		return table;		
	}
}
