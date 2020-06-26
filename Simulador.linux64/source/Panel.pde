class Panel extends UI{
  ArrayList<UI> elements;
  Panel(int x, int y, int w, int h){
    super(x,y);
    this.xo = w;
    this.yo = h;
    bgAvailable=false;
  }
  
  void display(){
    isOverMe();
    noStroke();
    fill(actual_bg);
    rect(x,y,xo,yo);
  }
}
