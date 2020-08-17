package bartecki_CSCI201L_Assignment3;

import java.io.Serializable;

public class Message<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	private String type;
	private T message;
	
	Message(String type, T message)
	{
		this.type = type;
		this.message = message;
	}
	
	public String getType() {
		return type;
	}
	
	public T getMessage() {
		return message;
	}
		
}
