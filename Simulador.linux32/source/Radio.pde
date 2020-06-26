class Radio extends UI{
int radio;
boolean isActive;

  Radio(int x,int y,int r){
    super(x,y);
    this.radio = r;
    xo = x+2*r;
    yo = y+2*r;
  }
  
  void display(){
  	if (!isActive)
  		return;
  	onClick = false;
    isOverMe();
    noFill();
    stroke(#FFFFFF);
    strokeWeight(4);
    if(mouseX >= 300+radio){
    	if (mouseX <= 900-radio)
    		if (mouseX > 585 && mouseX < 615)
    			x = 600;
    		else
    			x=int(mouseX);
    	else
    		x = 900-radio;
    }else
    	x = 300+radio;

    if(mouseY >= radio){
    	if (mouseY <= 600-radio)
    		if (mouseY > 285 && mouseY < 315)
    			y = 300;
    		else
    			y=int(mouseY);
    	else
    		y = 600-radio;
    }else
    	y = radio; 

    xo = x+2*radio;
    yo = y+2*radio;

    circle(x,y,radio*2);
  }

  void aumentarRadio(){
  	if (radio <= 300)
  		radio+=4;
  	else
  		radio = 300;
  }
  void disminuirRadio(){
  	if (radio >= 10)
  		radio-=4;
  	else
  		radio = 10;
  }
  
}
