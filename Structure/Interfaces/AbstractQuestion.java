package Structure.Interfaces;

public abstract class AbstractQuestion implements java.io.Serializable {
	private Object question;
	private boolean optionsAvailable;
	private String[] options;
	
	public AbstractQuestion(Object quest){
		question=quest;
	}
	
	public Object getQuestion(){
		return question;
	}
	
	public boolean areOptionsAvailable(){
		return optionsAvailable;
	}
	
	public void setOptions(String...options){
		optionsAvailable=true;
		this.options=options;
	}
	
	public String[] getOptions(){
		return options;
	}
}