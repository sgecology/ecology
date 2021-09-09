/**
 * 
 */
package net.ecology.css.service.system;

import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import net.ecology.common.CollectionsUtility;
import net.ecology.entity.system.Option;
import net.ecology.entity.system.Sequence;
import net.ecology.framework.component.ComponentRoot;
import net.ecology.global.GlobalConstants;

/**
 * @author ducbq
 *
 */
@Component
public class SequenceManager extends ComponentRoot {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6124242191964956710L;

	@Inject 
	private SequenceService systemSequenceService;
	
	@Inject 
	private OptionService optionService;

	private Map<String, Sequence> sequenceMap = CollectionsUtility.createMap();

	public String getNewNumber(String serial, Integer len) {
		// TODO: burda gerçek numaranın bir şekilde veri tabanından alınması lazım. Veri tabanı sequence'i v.s. bişey kullnılması lazım.'
		Sequence seq = findSequence(serial);

		logger.debug("SystemSequence #0 oldValue #1", seq.getSerial(), seq.getNumber());

		seq.setNumber(seq.getNumber() + 1);
		systemSequenceService.saveOrUpdate(seq);

		logger.debug("SystemSequence #0 newValue #1", seq.getSerial(), seq.getNumber());

		// TODO: numara digit sayısı dönecek digit sayısından büyük ise hata dönmesi lazım. Taşma var.
		String newSerial = GlobalConstants.SERIAL_PATTERN + seq.getNumber();

		return newSerial.substring(newSerial.length() - len);
	}

	public String getNewTempNumber(String serial, Integer len) {

		// TODO: burda gerçek numaranın bir şekilde veri tabanından alınması lazım. Veri tabanı sequence'i v.s. bişey kullnılması lazım.'
		Sequence seq = findSequence(serial);

		logger.debug("SystemSequence #0 oldValue #1", seq.getSerial(), seq.getNumber());

		Long num = seq.getNumber() + 1;
		// entityManager.merge(seq);
		// entityManager.flush();

		logger.debug("SystemSequence #0 newValue #1", seq.getSerial(), num);

		// TODO: numara digit sayısı dönecek digit sayısından büyük ise hata dönmesi lazım. Taşma var.
		String newSerial = GlobalConstants.SERIAL_PATTERN + num;

		return newSerial.substring(newSerial.length() - len);
	}

	protected Sequence findSequence(String serial) {
		logger.debug("Find SystemSequence : #0", serial);
		Sequence seq = null;

		// Önce eldeki Map kontrol ediliyor...
		if (!sequenceMap.containsKey(serial)) {
			// Veri tabanından isteniyor
			seq = this.systemSequenceService.getObjectBySerial(serial);
			if (null==seq) {
				// Veri tabanında da yoksa yeni açılıyor...
				seq = new Sequence();
				seq.setSerial(serial);
				seq.setNumber(0l);
				this.systemSequenceService.saveOrUpdate(seq);
			}

			sequenceMap.put(serial, seq);
		} else {
			seq = sequenceMap.get(serial);
		}

		return seq;
	}

	public Long getCurrenctNumber(String serial) {
		Sequence seq = findSequence(serial);

		return seq.getNumber();
	}

	public void setCurrenctNumber(String serial, Long number) {
		Sequence seq = findSequence(serial);
		seq.setNumber(number);
		this.systemSequenceService.saveOrUpdate(seq);
	}

	/*
	 * public String getNewSerialNumber( String serial ){ //TODO: Taşma kontrolü yapılması lazım. ona göre belkide seri kısmını da otomatik arttırmalı... return serial + "-" +
	 * getNewNumber( serial, 6 ); }
	 */
	public String getNewSerialNumber(String key) {
		// TODO: Burda ilgili optiondan değeri okuyacak.
		String serial = "AA";
		Option o = this.optionService.getByKey(key);
		if (o != null) {
			serial = o.getAsString();
		}

		return serial + "-" + getNewNumber(key + "." + serial, 6);
	}

	public String generateNewSerialNumber(String key) {
		String generatedSerialNumber = getNewNumber(key, (int)GlobalConstants.SIZE_CODE_MEDIUM-key.length());
		return new StringBuilder(key).append(generatedSerialNumber).toString();
	}

	/**
	 * Geçici olarak numara isteniyor. Eğer kayıt saklanmazsa kaybolmasın diye.
	 * 
	 * @param key
	 * @return geçici olarak atanmış numara döner.
	 */
	public String getTempSerialNumber(String key) {
		String serial = "AA";
		Option o = this.optionService.getByKey(key);
		if (o != null) {
			serial = o.getAsString();
		}

		return serial + "-" + getNewTempNumber(key + "." + serial, 6);
	}

	public String getBarcodeNumber(String key, Integer len) {

		// 12 digit seri no uretecek
		String serial = "B1";
		// Integer len = 12; //paritesiz basamak sayisi

		Option o = this.optionService.getByKey(key);
		if (o != null) {
			serial = o.getAsString();
		}

		serial = key + "." + serial;

		Sequence seq = findSequence(serial);

		logger.debug("SystemSequence #0 oldValue #1", seq.getSerial(), seq.getNumber());

		seq.setNumber(seq.getNumber() + 1);
		this.systemSequenceService.saveOrUpdate(seq);

		logger.debug("SystemSequence #0 newValue #1", seq.getSerial(), seq.getNumber());

		// TODO: numara digit sayısı dönecek digit sayısından büyük ise hata dönmesi lazım. Taşma var.
		String s = "00000000000000" + seq.getNumber();

		s = s.substring(s.length() - (len - 1));
		return s = '1' + s;
	}

}
