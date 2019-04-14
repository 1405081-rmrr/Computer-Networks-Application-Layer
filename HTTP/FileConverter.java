/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 *
 * @author Nafis
 */
public class FileConverter {
    private File file;
    private byte[] convertedFile;
    private FileInputStream fis;
    
    byte[] ConvertHTMLtoBytes(String path) 
    {
        try{
        file=new File(path);
        convertedFile=new byte[(int)file.length()];
        fis=new FileInputStream(file);
        fis.read(convertedFile);
        fis.close();
        }catch(Exception e)
        {
            System.out.println(e);
        }
        return convertedFile;
    }
    
    
}
