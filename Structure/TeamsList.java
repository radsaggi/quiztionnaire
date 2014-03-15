package Structure;

public class TeamsList extends Utilities.SearchableList<QuizTeam> implements java.io.Serializable {
	
	public boolean add(QuizTeam t){
		final String userName = t.getUserName();
		int i[]=this.getIndexOfAll(new ListFilter<QuizTeam,QuizTeam>(){
			public QuizTeam accept(QuizTeam t){
				if (t.getUserName().equals(userName))
					return t;
				else return null;
			}
		});
		
		if (i.length==0)	return super.add(t);
		else throw new IllegalArgumentException(userName +" already Exists");
	}
}