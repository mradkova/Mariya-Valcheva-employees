package employees;

class Tuple{
	private Team team;
	private Integer projectID;
	
	public Tuple(Team t, Integer id) {
		this.team = t;
		this.projectID = id;
	}
	
	Team getTeam() {
		return team;
	}
	
	boolean isSameTuple(Tuple tuple) {
		if(tuple.projectID == this.projectID && tuple.getTeam().isSame(this.team)) {
			return true;
		}
		else {
			return false;
		}
	}
}
