package data;

public abstract class GenericEntity {
	
	private boolean newRecord = false;

	
	abstract public boolean isDuplicatedEntity();  
	
	public boolean isNewRecord() {
		return newRecord;
	}

	public void setNewRecord(boolean newRecord) {
		this.newRecord = newRecord;
	}

}
