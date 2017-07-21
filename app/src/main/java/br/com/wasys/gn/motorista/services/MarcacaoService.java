package br.com.wasys.gn.motorista.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public class MarcacaoService extends IntentService {

    private static final String EXTRA_ID = MarcacaoService.class.getName() + ".EXTRA_ID";

    public MarcacaoService() {
        super(MarcacaoService.class.getSimpleName());
    }

    public static void stopLocation(Context context) {
        Intent intent = new Intent(context, MarcacaoService.class);
        context.stopService(intent);
    }

    public static void startLocation(Context context, Long id) {
        Intent intent = new Intent(context, MarcacaoService.class);
        intent.putExtra(EXTRA_ID, id);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            long id = intent.getLongExtra(EXTRA_ID, -1);
            if (id > -1) {
                handleLocation(id);
            }
        }
    }

    private void handleLocation(Long id) {

    }
}
