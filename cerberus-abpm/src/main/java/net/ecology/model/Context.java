/**
 * 
 */
package net.ecology.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Base Context
 * @author bqduc
 *
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Context implements Serializable{
	private static final long serialVersionUID = 2622407790323667698L;

	public static String DEFAULT = "dfltContext";

	//@Setter @Getter
  //private String request;

	//@Setter @Getter
  //private String executionStage;

  @Setter @Getter @Builder.Default
  private Map<String, Object> contextData = new HashMap<>();

	public boolean containsKey(String key){
		return this.contextData.containsKey(key);
	}

	public Object get(String key){
		return this.contextData.get(key);
	}

	public Object get(){
		return this.contextData.get(DEFAULT);
	}

	public Context put(Object contextData){
		this.contextData.put(DEFAULT, contextData);
		return this;
	}

	public Context put(String key, Object contextData){
		this.contextData.put(key, contextData);
		return this;
	}

	public boolean isEmpty(){
		return this.contextData.isEmpty();
	}

	public Context putAll(Context executionContext){
		this.contextData.putAll(executionContext.getContextData());
		return this;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		for (String key :this.contextData.keySet()) {
			buffer.append(key).append("\t: ").append(this.contextData.get(key)).append("\n");
		}
		return buffer.toString();
	}

	public Collection<?> keys(){
		return this.contextData.keySet();
	}
}
