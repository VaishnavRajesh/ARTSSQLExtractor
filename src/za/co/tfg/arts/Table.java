package za.co.tfg.arts;

import java.util.ArrayList;

public class Table {

	private String tableName;
	private String logicalTableName;
	private ArrayList<Field> fields = new ArrayList<Field>();

	public Table(String tableName,String logicalTableName) {
		this.tableName = tableName;
		this.logicalTableName = logicalTableName;		
	}

	public void addField(Field field) {
		fields.add(field);
	}

	@Override
	public String toString() {

		StringBuilder tableDefinition =  new StringBuilder(logicalTableName).append("(").append(tableName).append(")\n");

		for(Field field:fields){
			tableDefinition.append(field.toString()).append("\n");
		}

		return tableDefinition.toString();
	}	
}
