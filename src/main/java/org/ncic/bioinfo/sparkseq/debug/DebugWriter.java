package org.ncic.bioinfo.sparkseq.debug;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Author: wbc
 */
public class DebugWriter {

    BufferedWriter writer = null;

    public DebugWriter(String filePath) {
        try {
            writer = new BufferedWriter(new FileWriter(new File(filePath)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void write(byte content) {
        try{
            writer.write(content);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void write(String content) {
        try{
            writer.write(content);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void newline(){
        try{
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
