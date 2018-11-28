package employees;

class Team {
	
	private Integer employeeId1;
	private Integer employeeId2;
	
	public Team(Integer id1, Integer id2) {
		this.employeeId1 = id1;
		this.employeeId2 = id2;
	}
	
	Integer getEmployeeId1() {
		return this.employeeId1;
	}
	
	Integer getEmployeeId2() {
		return this.employeeId2;
	}
	
	public String print() {
		String result = "empid1 = " + employeeId1 + ", empid2 = " + employeeId2;
		return result;
	}
	
	boolean isSame(Team t) {
		if(t.employeeId1 == this.employeeId2 && t.employeeId2== this.employeeId1) {
			return true;
		}
		else {
			return false;
		}
	}
	
}



