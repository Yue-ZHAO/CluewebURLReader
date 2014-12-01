package com.yuezhao.temporal.CluewebURLReader;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.yuezhao.temporal.CluewebURLReader.FileProcess;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
        System.out.println( "Hello World!" );
        //	Read args[]
        String sourceFolder = args[0];
        String targetFolder = args[1];
        System.out.println(sourceFolder);
        System.out.println(targetFolder);
        
        System.out.println("Start to read urls from clueweb12 files...");
        //	Read files in the source folder to get the URLs
        List<String> OriginalURLs = FileProcess.readURLFromCluewebFiles(sourceFolder);        
        System.out.println("The total number of url is " + OriginalURLs.size());
        
        //	Write them in to a particular file
        File targetFile = new File(targetFolder, "urls_clueweb12");
        System.out.println("The URLs are recorded in the File: " + targetFile.getAbsolutePath());
        
        int count = 0;
        for(String OriginalURL: OriginalURLs){
        	FileProcess.addLinetoaFile(OriginalURL, targetFile.getAbsolutePath());
        	count++;
        	System.out.println("Processing: " + count + "/" + OriginalURLs.size());
        }
        System.out.println("Finished");
    }
}
