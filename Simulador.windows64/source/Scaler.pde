class Scaler extends UI{
	int niveles;
	int l;
	int nivel;
  String titulo;
	Scaler(String titulo, int x, int y, int l, int niveles){
		super(x,y);
		this.xo = l;
		this.yo = l;
		this.l = l;
    this.titulo = titulo;
		this.niveles = niveles;
    primAvailable=true;
	}

	void display(){
	    isOverMe();
	    noStroke();
	    fill(actual_bg);
	    rect(x,y,xo,yo);

	    int medida = (l/niveles)/2;

	    float distance;
	    distance = sqrt(pow((x+xo/2)-mouseX,2)+pow((y+yo/2)-mouseY,2));

	    if(onClick && isVisible){
      		if(mouseX >= x && mouseY >= y)
        		if(mouseX <= x+xo && y <= y+yo)
          			nivel = int(map(distance,medida*2,l/2,2.0,float(niveles)));
        		else
        			nivel = niveles;
      		else
        		nivel = niveles;

        	if(nivel < 1)
        		nivel = 1;
        	if (nivel > 5)
        		nivel = 5;
    	}

	    noStroke();
	    fill(actual_prim);
	    rect(x+medida*(niveles-nivel), y+medida*(niveles-nivel), l-medida*(niveles-nivel)*2, l-medida*(niveles-nivel)*2);

	    noFill();
	    strokeWeight(1);
	    stroke(actual_white);

	    noStroke();
	    fill(actual_white);
	    rect(x+xo/2-medida/2, y+yo/2-medida/2, medida, medida);

	    textFont(quickTitle);
	    fill(actual_white);
	    text(titulo, x-titulo.length()/2,y-7);

	    textFont(quick12);
	    fill(actual_white);
	    text(str(nivel), x+xo-str(nivel).length()-8,y+yo-5);
	    
	}
}
