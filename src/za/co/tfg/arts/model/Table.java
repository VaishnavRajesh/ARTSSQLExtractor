package za.co.tfg.arts.model;

import java.util.ArrayList;

public class Table {

	private String tableName;
	private String logicalTableName;
	private ArrayList<Field> fields = new ArrayList<Field>();
	private int currentField;

	public Table(String tableName,String logicalTableName) {
		this.tableName = tableName;
		this.logicalTableName = logicalTableName;
		this.currentField = 0;
	}

	@Override
	public String toString() {

		StringBuilder tableDefinition =  new StringBuilder(logicalTableName).append("(").append(tableName).append(")\n");

		for(Field field:fields){
			tableDefinition.append(field.toString()).append("\n");
		}

		return tableDefinition.toString();
	}

	public Field getCurrentField() {
		if(fields.size() == currentField){
			fields.add(new Field());
		}
		
		return fields.get(currentField);
	}

	public void setPointerToNextField() {
		currentField++;
		
	}	
}
