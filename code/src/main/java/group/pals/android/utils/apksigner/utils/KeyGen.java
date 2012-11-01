/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.android.utils.apksigner.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class KeyGen {

    public static void genKey(File jdkPath, File target, String storepass, String alias,
            String keypass, int aliasYears, String coName, String ouName,
            String oName, String city, String state, String country)
            throws IOException, InterruptedException {
        target.delete();

        /*
         * keytool -genkey -alias ALIAS_NAME -keypass KEY_PASS -validity YEARS
         * -keystore TARGET_FILE -storepass STORE_PASS -genkeypair -dname
         * "CN=Mark Jones, OU=JavaSoft, O=Sun, L=city, S=state C=US"
         */

        String[] values = {coName, ouName, oName, city, state, country};
        String[] keys = {"CN", "OU", "O", "L", "S", "C"};
        String dname = "";
        for (int i = 0; i < values.length; i++) {
            if (!values[i].isEmpty()) {
                dname += String.format("%s=%s ", keys[i], values[i]);
            }
        }
        dname = dname.trim();

        /*
         * JDK for Linux does not need to specify full path
         */
        String keytool = jdkPath != null && jdkPath.isDirectory() ? jdkPath.getAbsolutePath() + "/keytool.exe" : "keytool";
        
        ProcessBuilder pb = new ProcessBuilder(new String[]{
                    keytool,
                    "-genkey",
                    "-alias",
                    alias,
                    "-keypass",
                    keypass,
                    "-validity",
                    Integer.toString(aliasYears),
                    "-keystore",
                    target.getAbsolutePath(),
                    "-storepass",
                    storepass,
                    "-genkeypair",
                    "-dname",
                    String.format("%s", dname)});
        Process p = pb.start();

        StringBuffer sb = new StringBuffer();
        InputStream stream = p.getInputStream();
        try {
            int read = 0;
            byte[] buf = new byte[1024 * 99];
            while ((read = stream.read(buf)) > 0) {
                sb.append(new String(buf, 0, read));
            }
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        /*
         * TODO: get output of keytool to parse for errors, warnings...
         */

        p.waitFor();

        if (!target.isFile()) {
            throw new IOException("Error: " + sb);
        }
    }//genKey
}
