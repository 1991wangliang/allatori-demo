package com.codingapi.allatori.demo.license;

import javax0.license3j.License;
import javax0.license3j.io.IOFormat;
import javax0.license3j.io.LicenseReader;

import java.io.IOException;

/**
 * @author lorne
 * @since 1.0.0
 */
public class MyLicense {

    public static void main(String[] args) {
        License license = new License();
        try {
            license= new LicenseReader(MyLicense.class.getResource("/").getPath() + "/license/license.bin")
                    .read(IOFormat.BASE64);

            byte [] key = new byte[] {
                    (byte)0x52,
                    (byte)0x53, (byte)0x41, (byte)0x00, (byte)0x30, (byte)0x81, (byte)0x9F, (byte)0x30, (byte)0x0D,
                    (byte)0x06, (byte)0x09, (byte)0x2A, (byte)0x86, (byte)0x48, (byte)0x86, (byte)0xF7, (byte)0x0D,
                    (byte)0x01, (byte)0x01, (byte)0x01, (byte)0x05, (byte)0x00, (byte)0x03, (byte)0x81, (byte)0x8D,
                    (byte)0x00, (byte)0x30, (byte)0x81, (byte)0x89, (byte)0x02, (byte)0x81, (byte)0x81, (byte)0x00,
                    (byte)0xCE, (byte)0xE8, (byte)0x0D, (byte)0x69, (byte)0xF2, (byte)0x86, (byte)0x3D, (byte)0xF9,
                    (byte)0x17, (byte)0x1E, (byte)0x36, (byte)0x8B, (byte)0x32, (byte)0xFD, (byte)0x8E, (byte)0x4D,
                    (byte)0xFB, (byte)0x3C, (byte)0xD6, (byte)0x8F, (byte)0x32, (byte)0x97, (byte)0xBB, (byte)0x3D,
                    (byte)0xEC, (byte)0xCC, (byte)0x46, (byte)0x7F, (byte)0x6F, (byte)0x6A, (byte)0x59, (byte)0x40,
                    (byte)0x6D, (byte)0xB1, (byte)0x1F, (byte)0x8C, (byte)0xE5, (byte)0x54, (byte)0xF0, (byte)0xB1,
                    (byte)0x27, (byte)0x09, (byte)0xBB, (byte)0x14, (byte)0x64, (byte)0xB1, (byte)0x4C, (byte)0x8E,
                    (byte)0x9B, (byte)0xF8, (byte)0xB5, (byte)0x07, (byte)0xF1, (byte)0x7E, (byte)0x1C, (byte)0x52,
                    (byte)0x65, (byte)0x6C, (byte)0x75, (byte)0x18, (byte)0x3C, (byte)0xBD, (byte)0x40, (byte)0x10,
                    (byte)0x4E, (byte)0x80, (byte)0xD0, (byte)0xC3, (byte)0xF1, (byte)0x8A, (byte)0x24, (byte)0x85,
                    (byte)0x1E, (byte)0x24, (byte)0x67, (byte)0x23, (byte)0x9D, (byte)0x47, (byte)0xF6, (byte)0xC6,
                    (byte)0x88, (byte)0x9C, (byte)0x14, (byte)0x0A, (byte)0x0E, (byte)0x0F, (byte)0x63, (byte)0x62,
                    (byte)0x8E, (byte)0x1F, (byte)0x3A, (byte)0x81, (byte)0xD8, (byte)0x89, (byte)0x15, (byte)0x97,
                    (byte)0x0C, (byte)0x40, (byte)0x09, (byte)0x3C, (byte)0xFC, (byte)0xD0, (byte)0x6B, (byte)0xAB,
                    (byte)0x24, (byte)0x33, (byte)0x82, (byte)0x76, (byte)0x05, (byte)0x04, (byte)0x63, (byte)0x9D,
                    (byte)0x86, (byte)0xDF, (byte)0x2B, (byte)0x36, (byte)0xB1, (byte)0x23, (byte)0x82, (byte)0xD1,
                    (byte)0x6B, (byte)0x1B, (byte)0xE8, (byte)0x16, (byte)0xFA, (byte)0x4C, (byte)0xB3, (byte)0x3F,
                    (byte)0x02, (byte)0x03, (byte)0x01, (byte)0x00, (byte)0x01,
            };
            if( !license.isOK(key) ){
                // if not signed, stop the application
                System.out.println("license not signed");
            } else {
                System.out.println("license signed");

                int id = license.get("id").getInt();
                System.out.println("Extracted feature id:" + id );
                String name = license.get("name").getString();
                System.out.println("Extracted feature name:" + name );
            }

        } catch (IOException e) {
            System.out.println("Error reading license file " + e);
        }
    }
}
