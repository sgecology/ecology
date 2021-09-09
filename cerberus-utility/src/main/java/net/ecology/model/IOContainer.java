/**
 * 
 */
package net.ecology.model;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ducbq
 *
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IOContainer {
	public static final String DEFAULT_INPUT_DATA_STREAM = "defaultInputDataStream";
	
	@Builder.Default
	@Setter @Getter
	private Map<Object, InputStream> inputDataStreams = new HashMap<>();

	@Builder.Default
	@Setter @Getter
	private Map<Object, String> secrets = new HashMap<>();

	public boolean isEmpty(){
		return (null==inputDataStreams || inputDataStreams.isEmpty());
	}

	public Collection<?> keys(){
		return this.inputDataStreams.keySet();
	}

	public InputStream get(Object key){
		return this.inputDataStreams.get(key);
	}

	public InputStream get(){
		return this.inputDataStreams.get(DEFAULT_INPUT_DATA_STREAM);
	}

	public IOContainer put(InputStream inputDataStream){
		this.inputDataStreams.put(DEFAULT_INPUT_DATA_STREAM, inputDataStream);
		return this;
	}

	public IOContainer put(Object key, InputStream inputDataStream){
		this.inputDataStreams.put(key, inputDataStream);
		return this;
	}

	public IOContainer putSecret(Object key, String secret){
		this.secrets.put(key, secret);
		return this;
	}

	public String getSecret(Object key){
		return this.secrets.get(key);
	}
}