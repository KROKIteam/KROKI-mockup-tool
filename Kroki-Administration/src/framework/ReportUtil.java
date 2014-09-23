package framework;

public class ReportUtil {
	
	public static String getReportFilePath(String className){
		//Example
		//if (className.equals("State"))
		//	return "/reports/state.jasper";
		if (className.equals("OrderInformation"))
			return "/reports/report3.jasper";
		
		return null;
	}

}
