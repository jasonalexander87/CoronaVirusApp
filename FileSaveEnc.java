package com.iasonas.coronavirus;

import android.app.IntentService;
import android.content.Intent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileSaveEnc extends IntentService {

    public FileSaveEnc() { super("test-service"); }


    @Override
    protected void onHandleIntent(Intent intent) {


        String filename = "filename";
        String locData[] = intent.getStringArrayExtra("data");
        String KSpass = intent.getStringExtra("KSpass");
        String Entrypass = intent.getStringExtra("Entrypass");

        String dataS = null;

        for(int i=0; i<locData.length; i++) {

            dataS = dataS + locData[i];

        }

        CryptoFile cf = new CryptoFile(filename,KSpass, Entrypass);
        cf.encryptData(dataS);


    }
}
