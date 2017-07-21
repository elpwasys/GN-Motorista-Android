package br.com.wasys.gn.motorista.endpoint;

import android.content.Context;
import android.os.Build;

import br.com.wasys.gn.motorista.Application;
import br.com.wasys.gn.motorista.BuildConfig;

import java.util.HashMap;
import java.util.Map;

import br.com.wasys.library.enumerator.DeviceHeader;
import br.com.wasys.library.utils.AndroidUtils;

/**
 * Created by pascke on 03/08/16.
 */
public class Endpoint {

    public static final String BASE_URL = BuildConfig.BASE_URL;

    public static <T> T create(Class<T> clazz) {
        Context context = Application.getContext();
        Map<String, String> headers = new HashMap<>();
        headers.put(DeviceHeader.DEVICE_SO.key, "Android");
        headers.put(DeviceHeader.DEVICE_SO_VERSION.key, Build.VERSION.RELEASE);
        headers.put(DeviceHeader.DEVICE_MODEL.key, Build.MODEL);
        headers.put(DeviceHeader.DEVICE_IMEI.key, AndroidUtils.getIMEI(context));
        headers.put(DeviceHeader.DEVICE_WIDTH.key, String.valueOf(AndroidUtils.getWidthPixels(context)));
        headers.put(DeviceHeader.DEVICE_HEIGHT.key, String.valueOf(AndroidUtils.getHeightPixels(context)));
        headers.put(DeviceHeader.DEVICE_APP_VERSION.key, String.valueOf(AndroidUtils.getVersionCode(context)));
        return br.com.wasys.library.http.Endpoint.create(clazz, BASE_URL, headers);
    }
}
