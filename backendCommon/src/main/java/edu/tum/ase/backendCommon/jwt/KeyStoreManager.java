package edu.tum.ase.backendCommon.jwt;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PublicKey;

@Component
public class KeyStoreManager {

    private KeyStore keyStore;

    private String keyAlias;

    private char[] password = "asedelivery10".toCharArray();

    public KeyStoreManager() throws KeyStoreException, IOException {
        loadKeyStore();
    }

    public void loadKeyStore() throws KeyStoreException, IOException {
        keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream fis = null;
        try {
            // Get the path to the keystore file in the resources folder
            File keystoreFile = ResourceUtils.getFile("classpath:ase_delivery.keystore");
            fis = new FileInputStream(keystoreFile);
            keyStore.load(fis, password);
            keyAlias = keyStore.aliases().nextElement();
        } catch (Exception e) {
            System.err.println("Error when loading KeyStore");
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    public PublicKey getPublicKey() { // In the problem statement this was protected
        try {
            // DONE: return the public key in the keystore
            return keyStore.getCertificate(keyAlias).getPublicKey();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Key getPrivateKey() {
        try {
            // DONE: return the private key in the keystore.
            return keyStore.getKey(keyAlias, password);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}