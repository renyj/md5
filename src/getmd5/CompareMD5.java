package getmd5;

public class CompareMD5 {
	public static void main(String[] args) throws Exception {

		GetFileMD5 fmd1 = new GetFileMD5("E:\\md5", "hadoop.git", "git-fileName.txt", "git-MD5.txt");
		fmd1.computeMD5();
		
		GetFileMD5 fmd2 = new GetFileMD5("E:\\md5", "hadoop-2.7.1-src", "2.7.1-fileName.txt", "2.7.1-MD5.txt");
		fmd2.computeMD5();
		
		fmd1.CompareLine("git-MD5.txt", "2.7.1-MD5.txt");
	}
}
