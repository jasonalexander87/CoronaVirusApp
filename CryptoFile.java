package com.iasonas.coronavirus;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class CryptoFile {

    String filename;
    String KSPass;
    String entryPassword;

public CryptoFile(String file, String keyStorePass, String entryPass){

    filename = file;
    KSPass = keyStorePass;
    entryPassword = entryPass;
}

public String decryptData() {

    SecretKey myKey = loadKey(KSPass,entryPassword);
    byte[] data = openFile(filename);
    byte[] dataDecrypted = decrypt(data, myKey);

    return new String(dataDecrypted);

}

public void encryptData(String data2Enc){

    SecretKey myKey = loadKey(KSPass,entryPassword);
    byte[] data = openFile(filename);
    byte[] dataDecrypted = decrypt(data, myKey);
    byte[] newData = appendData(dataDecrypted, data2Enc);
    byte[] newDataEnc = encrypt(newData, myKey);
    save(newDataEnc);

}

private void save(byte[] param){

    try {

        FileOutputStream fOut = new FileOutputStream("output.txt");
        fOut.write(param);

    } catch (java.io.IOException e) {}
}

private byte[] encrypt(byte[] param, SecretKey param2) {

    try {
        Cipher cipher = Cipher.getInstance("AES_256/CBC/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, param2);

        byte[] res = cipher.update(param);

        return res;

    } catch (NoSuchPaddingException e) {
        return null;
    } catch (NoSuchAlgorithmException e) {
        return null;
    } catch (InvalidKeyException e) {
        return null;
    }
}

private byte[] appendData(byte[] param, String param2) {

    String oldData = new String(param);
    String newData = oldData + param2;
     return newData.getBytes();

}


private byte[] decrypt(byte[] param, SecretKey param2) {

    try {
        Cipher cipher = Cipher.getInstance("AES_256/CBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, param2);

        byte[] res = cipher.update(param);

        return res;

    } catch (NoSuchPaddingException e) {
        return null;
    } catch (NoSuchAlgorithmException e) {
        return null;
    } catch (InvalidKeyException e) {
        return null;
    }
}

private byte[] openFile(String param) {

    try {
        File myData = new File(param);
        FileInputStream in = new FileInputStream(myData);

        int fileLenght = (int) myData.length();
        byte[] dataBytes = new byte[fileLenght];

        in.read(dataBytes);

        return dataBytes;

    } catch (FileNotFoundException e) {
        return null;
    } catch (IOException e) {
        return null;
    }
}

private SecretKey loadKey(String KSPassword, String entryPass){

    try {
        SecretKey sKey;
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "keystore.p12");
        KeyStore keyStore = KeyStore.getInstance("pkcs12");
        FileInputStream instream = new FileInputStream(file);
        keyStore.load(instream, KSPassword.toCharArray());
        instream.close();
        KeyStore.ProtectionParameter protparam = new KeyStore.PasswordProtection(entryPass.toCharArray());
        KeyStore.SecretKeyEntry sKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry("me", protparam);

        sKey = sKeyEntry.getSecretKey();

        return sKey;


    } catch (FileNotFoundException e) {
        return null;
    } catch (CertificateException e) {
        return null;
    } catch (NoSuchAlgorithmException e) {
        return null;
    } catch (IOException e) {
        return null;
    } catch (KeyStoreException e) {
        return null;
    } catch (UnrecoverableEntryException e) {
        return null;
    }
}


}
