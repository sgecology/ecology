/**
 * 
 */
package net.ecology.domain;

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
public class Context {
	private final static String SECRET_PREFIX = "secret.";
	public static String DEFAULT = "dfltContext";

	@Setter @Getter
  @Builder.Default
  private Map<Object, Object> values = new HashMap<>();

	public boolean containsKey(String key){
		return this.values.containsKey(key);
	}

	public Object get(Object key){
		return this.values.get(key);
	}

	public Context put(Object key, Object value) {
		this.values.put(key, value);
		return this;
	}

	public boolean isEmpty(){
		return this.values.isEmpty();
	}

	public Context putAll(Context executionContext){
		this.values.putAll(executionContext.values);
		return this;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		for (Object key :this.values.keySet()) {
			buffer.append(key).append("\t: ").append(this.values.get(key)).append("\n");
		}
		return buffer.toString();
	}

	public Collection<?> keys(){
		return this.values.keySet();
	}

	public Context clear() {
		this.values.clear();
		return this;
	}

	public Context putSecrets(Map<Object, String> secretKeys){
		if (null != secretKeys && !secretKeys.isEmpty()) {
			this.values.putAll(secretKeys);
		}
		return this;
	}

	public Context putSecret(Object key, String secret){
		this.values.put((new StringBuilder(SECRET_PREFIX).append(key)).toString(), secret);
		return this;
	}

	public String getSecret(Object key){
		return (String)this.values.get((new StringBuilder(SECRET_PREFIX).append(key)).toString());
	}

	public static Context valueOf(Map<Object, Object> source) {
		Context target = Context.builder().build();
		if (null != source && !source.isEmpty()) {
			target.values.putAll(source);
		}
		return target;
	}
}
