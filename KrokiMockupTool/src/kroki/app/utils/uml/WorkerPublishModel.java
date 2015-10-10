package kroki.app.utils.uml;

public class WorkerPublishModel {

	protected String text;
	protected int typeOfMessage;
	public WorkerPublishModel(String text, int typeOfMessage) {
		super();
		this.text = text;
		this.typeOfMessage = typeOfMessage;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getTypeOfMessage() {
		return typeOfMessage;
	}
	public void setTypeOfMessage(int typeOfMessage) {
		this.typeOfMessage = typeOfMessage;
	}
	
	
}
