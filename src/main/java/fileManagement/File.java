package fileManagement;

import java.util.Scanner;

public class File {

	//private String fileDest = "./Employees.txt";
	private String fileDest;
	private Scanner scanner;
	
	public File(String dest) {
		if(!dest.isEmpty()) {
			fileDest = dest;
		}
	}

	public void openFile() {
		try {
			scanner = new Scanner(fileDest);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readFile() {
		while(scanner.hasNext()) {
			String empID = scanner.next(",");
			String projectID = scanner.next(",");
			String dateFrom = scanner.next(",");
			String dateTo = scanner.next(",");
			
			System.out.printf("%s %s %s %s", empID, projectID, dateFrom, dateTo);
		}
	}
	
	public void closeFile() {
		scanner.close();
	}

}
