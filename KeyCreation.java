package com.iasonas.coronavirus;

import android.app.IntentService;
import android.content.Intent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.ProtectionDomain;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class KeyCreation extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public KeyCreation() { super("test-Keycreation"); }


    @Override
    protected void onHandleIntent(Intent intent) {

        String KSpass = intent.getStringExtra("KSpass");
        String Entrypass = intent.getStringExtra("Entrypass");
        String PBKDFpass = intent.getStringExtra("PBDKFpass");

        byte[] salt = generateSalt();

        try {
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            char[] passKS = KSpass.toCharArray();
            char[] passE = Entrypass.toCharArray();

            KeyStore.ProtectionParameter protparam = new KeyStore.PasswordProtection(passE);

            ks.load(null, passKS);

            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2withHmacSHA256");
            KeySpec keyspec = new PBEKeySpec(PBKDFpass.toCharArray(), salt,1000,256);
            SecretKey myKey = skf.generateSecret(keyspec);
            KeyStore.SecretKeyEntry myKey2 = new KeyStore.SecretKeyEntry(myKey);

            ks.setEntry("me", myKey2, protparam);

            FileOutputStream fos = new FileOutputStream("keystoreName");
            ks.store(fos,passKS);
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    private byte[] generateSalt(){

        byte[] salt = new byte[1024];
        SecureRandom ransec = new SecureRandom();
        ransec.nextBytes(salt);

        return salt;
    }
}
