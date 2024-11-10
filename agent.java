public class agent {
  
  //field to hold vote of agent
  String vote;
//field to hold agent type
  String type;

  //position of agent:
  int row;
  int col;

  //constructor
  public agent(int x, int y, String s)
  {
    this.vote = "V";
    this.row = x;
    this.col= y;
    this.type = s;
  }

  //getter for weight of vote
  public int getW() {
    if(this.type.equalsIgnoreCase("penalty")) {
      return 0;
    } else 
    if(this.type.equalsIgnoreCase(""))
  }

}
