package log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Logger {

	public static void log(String log) {
		File logFile = new File("C:" + File.separator + "Users" + File.separator + "mrd" + File.separator + "Desktop" + File.separator + "log.txt");

		//read log file
		Scanner scan;
		ArrayList<String> lines = new ArrayList<String>();
		try {
			scan = new Scanner(logFile);
			while(scan.hasNext()){
				String line = scan.nextLine();
				lines.add(line);
			}
			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println("[ERROR] log file not found");
		}

		//append message
		lines.add("=========================");
		lines.add(log);
		lines.add("=========================");
		
		//write main.properties file with main form title
		try {
			PrintWriter pw=new PrintWriter(new FileOutputStream(logFile));
			for(int i=0; i<lines.size(); i++) {
				pw.println(lines.get(i));
			}
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
