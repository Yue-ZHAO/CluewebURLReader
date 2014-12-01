package com.yuezhao.temporal.CluewebURLReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileProcess {

	/**
	 * listFilesForFolder
	 * @param final File folder
	 * @return List<String> listFilePath
	 */
	public static List<String> listFilesForFolder(final File folder) {
	    List<String> listFilePath = new ArrayList<String>();
		
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	            //System.out.println(fileEntry.getName());
	            listFilePath.add(fileEntry.getPath());
	        }
	    }
		
		return listFilePath;
	}
	/**
	 * listFilesForFolder
	 * @param String sourceFolder
	 * @return List<String> listFilePath
	 */
	public static List<String> listFilesForFolder(String sourceFolder) {
		final File sourceFolderPath = new File(sourceFolder);
		return listFilesForFolder(sourceFolderPath);
	}
	
	/**
	 * readURLFromCluewebFiles
	 * @param folder
	 * @return List<String> listURLS
	 * @throws IOException
	 */	
	public static List<String> readURLFromCluewebFiles(final File folder) throws IOException{
		//	Get the list of files in the folder.
		List<String> listFilePath = new ArrayList<String>();
		
		//	Use to store URLs extracted from the files
		List<String> listURLS = new ArrayList<String>();
		
		listFilePath = listFilesForFolder(folder);
		for (String filePath: listFilePath){
			//	Read the file line by line to find the URL
			InputStream fis;
			BufferedReader br;
			String line;
			
			fis = new FileInputStream(filePath);
			br = new BufferedReader(new InputStreamReader(fis));
			
			while ((line = br.readLine()) != null) {
			    //	Deal with the line
				if ((line.contains("WARC-Target-URI:"))&&(line.length() > 20)) {
					listURLS.add(line.substring(17));
					break;
				}
			}

			//	Done with the file
			br.close();
			br = null;
			fis = null;
		}
		
		return listURLS;
	}
	
	/**
	 * readURLFromCluewebFiles
	 * @param sourceFolder
	 * @return List<String> listURLS
	 * @throws IOException
	 */
	public static List<String> readURLFromCluewebFiles(String sourceFolder) throws IOException{
		final File sourceFolderPath = new File(sourceFolder);
		return readURLFromCluewebFiles(sourceFolderPath);
	}
	
	public static String fileNameTransform(String URL){
		char[] listcharURL = URL.toCharArray();
		String transURL = "";
		for (char charURL: listcharURL){
			if ((charURL == '\\')||(charURL == '/')||(charURL == ':')||(charURL == '*')||(charURL == '?')||(charURL == '\"')||(charURL == '<')||(charURL == '>')||(charURL == '|'))
				charURL = '1';
			transURL = transURL + charURL;
		}
		return transURL;
	}
	
	public static String fileNameTransform_MD5(String URL) throws NoSuchAlgorithmException{
		// 	TODO transform the url to be a MD5 code 
		byte[] bytesOfMessage = URL.getBytes();
		
		MessageDigest md = MessageDigest.getInstance("MD5");		
		md.update(bytesOfMessage);		
		byte[] resultByteArray = md.digest();
		
		StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < resultByteArray.length; i++)
	        sb.append(Integer.toString((resultByteArray[i] & 0xff) + 0x100, 16).substring(1));

		String transURL = sb.toString();

		return transURL;
	}
	
	public static String generateSubFolderPath(String originalURL,
			String targetFolder) throws NoSuchAlgorithmException {
		String subFolderName = fileNameTransform_MD5(originalURL);
		// not using JAVA 7 API
		File dir = new File(targetFolder, subFolderName);
		dir.mkdir();
		return dir.getAbsolutePath();
	}
	
	public static File generateSubFolder(String originalURL,
			String targetFolder) throws NoSuchAlgorithmException {
		String subFolderName = fileNameTransform_MD5(originalURL);
		// not using JAVA 7 API
		File dir = new File(targetFolder, subFolderName);
		
		// TODO avoid the problem that the MD5 conflict, needed?
		
		dir.mkdir();
		return dir;
	}	
	
	public static void addLinetoaFile(String line, String filePath) {
		FileWriter fw = null;
		try {
			//	Write a line in the file.
		    File f = new File(filePath);
		    fw = new FileWriter(f, true);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(fw);
		pw.println(line);
		pw.flush();
		try {
		    fw.flush();
		    pw.close();
		    fw.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}

	public static void main(String[] args) {

	}
}
