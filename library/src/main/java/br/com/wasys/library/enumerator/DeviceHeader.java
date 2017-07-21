package br.com.wasys.library.enumerator;

/**
 * 
 * MobileHeader
 * 31 de jul de 2016 13:06:23
 * @autor Everton Luiz Pascke
 */
public enum DeviceHeader {
	AUTHORIZATION ("Authorization"),
	DEVICE_SO ("deviceSO"),
	DEVICE_IMEI ("deviceIMEI"),
	DEVICE_MODEL ("deviceModel"),
	DEVICE_WIDTH ("deviceWidth"),
	DEVICE_HEIGHT ("deviceHeight"),
	DEVICE_SO_VERSION ("deviceSOVersion"),
	DEVICE_APP_VERSION ("deviceAppVersion");
	public String key;
	DeviceHeader(String key) {
		this.key = key;
	}
}
