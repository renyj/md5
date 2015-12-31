package getmd5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.ArrayList;

public class GetFileMD5{

	private String path;
	private String folder;
	private String pathTxt;
	private String md5Txt;

	/**
	 * Structure the complete path(including its name) of folder to be computed MD5 value, 
	 * the txt to save file path and the txt to save MD5 value.
	 * @param path the path of the folder to save these files
	 * @param folderName the name of the folder to be computed MD5 value (not including its path)
	 * @param pathTxt the name of the txt to save path(including name) of every file(similarly, not including its path)
	 * @param MD5Txt the name of the txt to save MD5 value of every file(similarly, not including its path)
	 */
	public GetFileMD5( String path, String folderName, String pathTxt, String MD5Txt){

		this.path = path;
		this.folder = path + "\\" + folderName;	
		this.pathTxt = path + "\\" + pathTxt;
		this.md5Txt = path + "\\" + MD5Txt;	

	}

	/**
	 * compute the MD5 value of every file in folder.
	 * save the path within name of every file in pathTxt.
	 * save the MD5 value of every file in md5Txt.
	 */
	public void computeMD5() throws Exception {
		File file = new File(folder);
		createFile(new File(pathTxt));
		createFile(new File(md5Txt));
		deepList(file);
	}

	/**
	 * Traverse the folder to get every file(except .gitattributes and .gitignore) and its MD5 value
	 * @param file 
	 * 
	 */
	public void deepList(File file) throws FileNotFoundException{

		if (file.isFile() || 0 == file.listFiles().length)
		{
			return;
		}
		else
		{
			File[] files = file.listFiles();

			files = sort(files);

			for(File f : files)
			{
				StringBuffer output = new StringBuffer();

				if(f.isFile())
				{
					output.append(f.getPath());
					String path = null;
					String md5 = null;
					if(!f.getName().equals(".gitignore") && !f.getName().equals(".gitattributes")){

						path = output + "\n";
						addWrite(pathTxt, path);										
						md5 = getMd5OfFile(f).toUpperCase() + "\n";
						addWrite(md5Txt, md5);					
					}

				}
				else if(f.isDirectory())
				{
					deepList(f);
				}
			}
		}
	}

	/**
	 * Sort file so that the folder is behind of file
	 * @param files 
	 * 
	 */
	private static File[] sort(File[] files){

		ArrayList<File> sorted = new ArrayList<File>();

		for (File f : files)
		{
			if (f.isDirectory())
			{
				sorted.add(f);
			}
		}
		for (File f : files)
		{
			if (f.isFile())
			{
				sorted.add(f);
			}
		}
		return sorted.toArray(new File[files.length]);
	}

	/**
	 * compute the MD5 value of a file
	 * @param file
	 * 
	 */
	public static String getMd5OfFile(File file) throws FileNotFoundException {

		String value = null;
		FileInputStream in = new FileInputStream(file);
		try {
			MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}

	/**
	 * create a file if it do not exist
	 * @param fileName
	 * 
	 */
	public static void createFile(File fileName)throws Exception{  

		try{  
			if(!fileName.exists()){  
				fileName.createNewFile(); 
			}  
		}catch(Exception e){  
			e.printStackTrace();  
		}   
	}  

	/**
	 * add some content into a file
	 * @param fileName the name of file
	 * @param content the content to be added
	 */
	public static void addWrite(String fileName, String content) {   

		FileWriter writer = null;  
		try {     
			writer = new FileWriter(fileName, true);     
			writer.write(content);       
		} catch (IOException e) {     
			e.printStackTrace();     
		} finally {     
			try {     
				if(writer != null){  
					writer.close();     
				}  
			} catch (IOException e) {     
				e.printStackTrace();     
			}     
		}   
	} 

	/**
	 * compare every line of two files.if a line is different,print its line number
	 * @param m1 the name of one file(not including its path)
	 * @param m2 the name of the other file(not including its path)
	 */
	public void CompareLine(String m1, String m2) throws IOException{


		BufferedReader br1 = new BufferedReader(new InputStreamReader(
				new FileInputStream(path + "\\" + m1)));
		BufferedReader br2 = new BufferedReader(new InputStreamReader(
				new FileInputStream(path + "\\" + m2)));

		String line1 = "";
		String line2 = "";
		int i = 1;

		while (br1.ready() && br2.ready()) {

			line1 = br1.readLine();
			line2 = br2.readLine();
			if(!line1.equals(line2)){		
				System.out.println("line " + i + " is different" );
			}
			i += 1;
		}

		br1.close();  
		br2.close();  
	}

}

