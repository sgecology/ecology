/**
 * 
 */
package net.ecology.dmx.helper;

import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import net.ecology.common.CollectionsUtility;
import net.ecology.css.service.config.ConfigurationService;
import net.ecology.entity.config.Configuration;

/**
 * @author ducbq
 *
 */
@Component
public class DmxConfigurationHelper {
	//private static final String configInvtrItem = "load-inventory-items";

	public static byte idxInvtrItemCode = 1;
	public static byte idxInvtrItemName = 2;
	public static byte idxInvtrItemContent = 8; // Hàm lượng
	public static byte idxInvtrItemPackaging = 9;

	public static byte idxUsageDirectionCode = 6;
	public static byte idxUsageDirectionName = 7;

	public static byte idxGenericDrugCode = 3;
	public static byte idxGenericDrugName = 4;
	public static byte idxGenericDrugNameRegistered = 5;

	@Inject
	private ConfigurationService configurationSerice;

	public Map<String, Configuration> fetchInventoryItemConfig(String configName) {
		Map<String, Configuration> configDetailMap = CollectionsUtility.createMap();
		Optional<Configuration> optInventoryItemConfig = configurationSerice.getByName(configName);
		if (optInventoryItemConfig.isPresent()) {
			for (Configuration config :optInventoryItemConfig.get().getSubordinates()) {
				configDetailMap.put(config.getName(), config);
			}
			/*
			for (ConfigurationDetail configDetail :optInventoryItemConfig.get().getConfigurationDetails()) {
				configDetailMap.put(configDetail.getName(), configDetail);
			}
			*/
		}

		return configDetailMap;
	}
}
