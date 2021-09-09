/**
 * 
 */
package net.ecology.auth.comp;

import static org.springframework.security.crypto.util.EncodingUtils.concatenate;
import static org.springframework.security.crypto.util.EncodingUtils.subArray;

import java.security.MessageDigest;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
/**
 * @author ducbq
 *
 */
public abstract class AbstractPasswordEncoder implements PasswordEncoder {
	private final BytesKeyGenerator saltGenerator;

	protected AbstractPasswordEncoder() {
		this.saltGenerator = KeyGenerators.secureRandom();
	}

	@Override
	public String encode(CharSequence rawPassword) {
		byte[] salt = this.saltGenerator.generateKey();
		byte[] encoded = encodeAndConcatenate(rawPassword, salt);
		return String.valueOf(Hex.encode(encoded));
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		byte[] digested = Hex.decode(encodedPassword);
		byte[] salt = subArray(digested, 0, this.saltGenerator.getKeyLength());
		return matches(digested, encodeAndConcatenate(rawPassword, salt));
	}

	protected abstract byte[] encode(CharSequence rawPassword, byte[] salt);

	protected byte[] encodeAndConcatenate(CharSequence rawPassword, byte[] salt) {
		return concatenate(salt, encode(rawPassword, salt));
	}

	/**
	 * Constant time comparison to prevent against timing attacks.
	 */
	protected static boolean matches(byte[] expected, byte[] actual) {
		return MessageDigest.isEqual(expected, actual);
	}
}
