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

public class Signer {

    public static String sign(File jdkPath, File apk, File key, String storepass, String alias,
            String keypass) throws IOException, InterruptedException {
        
        /*
         * JDK for Linux does not need to specify full path
         */
        String jarsigner = jdkPath != null && jdkPath.isDirectory() ? jdkPath.getAbsolutePath() + "/jarsigner.exe" : "jarsigner";
        
        /*
         * jarsigner -keystore KEY_FILE -storepass STORE_PASS -keypass KEY_PASS
         * APK_FILE ALIAS_NAME
         */
        ProcessBuilder pb = new ProcessBuilder(new String[]{jarsigner,
                    "-keystore", key.getAbsolutePath(), "-storepass", storepass,
                    "-keypass", keypass, apk.getAbsolutePath(), alias});
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
         * TODO: get output of jarsigner to parse for errors, warnings...
         */

        p.waitFor();

        return sb.toString().trim();
    }//sign
}
