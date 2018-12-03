package employees;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dbManagement.Database;

public class EmployeeManager {

	Connection conn = null;
	Database db = new Database();

	Map<Integer, Team> map = new HashMap<>();

	public void readFilePopulateDB(String filePath) throws Exception {

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));

			PreparedStatement insert;
			db.createConnection();
			db.createTable();
			conn = db.getConnection();

			// reading from the file and inserting values in the database
			String line = null;
			while ((line = br.readLine()) != null) {
				DatabaseRecord record = prepareRecord(line);

				String ins = "INSERT INTO Projects (EmpID, ProjectID, DateFrom, DateTo) VALUES ('"
						+ record.getEmpID().toString() + "','" + record.getProjectId().toString() + "','"
						+ parseDateToString(record.getDateFrom()) + "','" + parseDateToString(record.getDateTo())
						+ "')";

				insert = conn.prepareStatement(ins);
				insert.executeUpdate();
			}
		} finally {
			br.close();
		}
	}

	private DatabaseRecord prepareRecord(String recordLine) throws Exception {
		String tmp[] = recordLine.split(",");

		DatabaseRecord result = new DatabaseRecord();
		result.setEmpID(Integer.parseInt(tmp[0].trim()));
		result.setProjectId(Integer.parseInt(tmp[1].trim()));
		result.setDateFrom(tmp[2].equals("NULL") ? new Date() : parseDate(tmp[2].trim()));
		result.setDateTo(tmp[3].trim().toUpperCase().equals("NULL") ? new Date() : parseDate(tmp[3].trim()));
		return result;
	}

	private Date parseDate(String dateStr) throws Exception {
		SimpleDateFormat dt = new SimpleDateFormat("yyyyy-MM-dd");
		Date resultDate = dt.parse(dateStr);
		return resultDate;
	}

	private String parseDateToString(Date date) {
		SimpleDateFormat dt = new SimpleDateFormat("yyyyy-MM-dd");
		String resultString = dt.format(date);
		return resultString;
	}

	public void findTeamLongestPeriod() {
		// execute db query
		//selects the team that worked the longest within a project
		String oldSQL = "select p1.empid as empid1, p2.empid as empid2, p1.projectId as projectid ,MAX(datediff(dd,  CASE WHEN  p1.datefrom >  p2.datefrom THEN  p1.datefrom ELSE  p2.datefrom END, CASE WHEN  p1.dateto >  p2.dateto THEN  p2.dateto ELSE  p1.dateto END)) as period "
				+ "from projects p1, projects p2 "
				+ "where p1.empid <> p2.empid and p1.projectid = p2.projectid and ((p2.datefrom < p1.dateto) ) "
				+ "group by p1.empid, p2.empid " + "order by period desc";
		
		//selects the team that worked the longest at all no matter in which project 
		String SQL = "select p1.empid as empid1, p2.empid as empid2, Sum(datediff(dd,  CASE WHEN  p1.datefrom >  p2.datefrom THEN  p1.datefrom ELSE  p2.datefrom END, CASE WHEN  p1.dateto >  p2.dateto THEN  p2.dateto ELSE  p1.dateto END)) as period "
				+ "from projects p1, projects p2 "
				+ "where p1.empid < p2.empid and p1.projectid = p2.projectid and p2.datefrom <= p1.dateto and p2.dateto >= p1.datefrom "
				+ "group by p1.empid, p2.empid "
				+ "order by period desc";

		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement(SQL);
			ResultSet rs = ps.executeQuery();
			
			
			while(rs.next()) {
				if (map.isEmpty() || map.containsKey( rs.getInt("Period"))) {
					
						Team team = new Team(rs.getInt("empid1"), rs.getInt("empid2"));
						
						if(map.containsKey(team)) {
							continue;
						}
						else {
							Integer period = rs.getInt("Period");
							this.map.put(period,team);
						}
				}	
			}
			
			String resultString = "Team longest period: \n";
			
			Collection<Team> allTeams = map.values();
			for(Team t : allTeams) {
				resultString = resultString + "EmpId1 - " + t.getEmployeeId1() + ", EmpId2 - " + t.getEmployeeId2() + "\n";
			}
			
			
			System.out.println(resultString);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	public static void main(String[] args) throws Exception {
		EmployeeManager empManager = new EmployeeManager();
		
		String filePath = args[0];
		empManager.readFilePopulateDB(filePath);
		empManager.findTeamLongestPeriod();


		empManager.conn.close();
	}

}
