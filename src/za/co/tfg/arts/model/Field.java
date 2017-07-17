package za.co.tfg.arts.model;
public class Field {
		private String logicalName;
		private String fieldName;
		private String dataType;

		public Field() {			
		}

		public void setLogicalName(String logicalName) {
			this.logicalName = logicalName;
		}
		
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
		
		public void setDataType(String dataType) {
			this.dataType = dataType;
		}
		
		@Override
		public String toString() {
			return new StringBuilder(logicalName).append("(").append(fieldName).append(") - ").
					append(dataType).toString();
		}

	}