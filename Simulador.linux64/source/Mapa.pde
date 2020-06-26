boolean occuped = false;
boolean go = true;
boolean start = false;
boolean play = true;
boolean info = false;

float mxo = 0;
float myo = 0;

int page = 0;

PFont quick12;
PFont quickTitle;

PImage panel;
PImage icon;

//UI
Slider slDensidad;
Slider slEnfermar;
Slider slInfeccioso;
Slider slReInfeccioso;
Slider slReAsintomatico;
Slider slMorir;
Slider slMinInfectados;
Slider slVelocidad;
Slider slDias;
Slider slDimensiones;

Button btPlay;
Button btMostrarInfectados;
Button btMostrarRecuperados;
Button btMostrarSusceptibles;
Button btMostrarMuertos;
Button btNext;
Button btStop;
Button btRestart;
Button btInfo;

Scaler scVecindad;

void setup() {
  strokeJoin(ROUND);
  icon = loadImage("icon.png");
  surface.setTitle("Simulador expansión vírica");
  surface.setIcon(icon);
  size(900, 600);
  background(#353744);
  smooth();
  quick12 = createFont("Quicksand-Bold.ttf", 11);
  quickTitle = createFont("Quicksand-Medium.ttf", 15);

  panel = loadImage("panel.png");
  slDensidad				= new Slider("Densidad inicial",40,300,200,70,0,100);
  slEnfermar				= new Slider("Prob. enfermar",40,350,200,30,0,100);
  slInfeccioso			= new Slider("Prob. ser infeccioso",40,400,200,60,0,100);
  slReInfeccioso		= new Slider("Recuperación (infeccioso)",40,450,200,5,0,100);
  slReAsintomatico	= new Slider("Recuperación (asintomático)",40,500,200,2,0,100);
  slMorir						= new Slider("Prob. de morir",40,550,200,0.1,0,100);
  slMinInfectados		= new Slider("Mínimo de infectados",40,500,200,15,5,40);
  slVelocidad				= new Slider("Velocidad",40,300,200,100,5,500);
  slDias				= new Slider("Días de simulación",40,350,200,400,10,1000);
  slDias.changeFormat(NUMERO);
  slDimensiones				= new Slider("Dimensiones",40,400,200,300,300,1200);
  slDimensiones.changeFormat(DIMENSIONES);
  
  slDias.hide();
  slDimensiones.hide();
  slMinInfectados.hide();
  slVelocidad.hide();
  btPlay = new Button(820,510,55,55,"Play", 7);
  btStop = new Button(820,470,30,30,"Stop", 10);
	btStop.hide();

  btMostrarInfectados = new Button(275, 30, 15, color_in);
  btMostrarRecuperados = new Button(275, 50, 15, color_re);
  btMostrarSusceptibles = new Button(275, 70, 15, color_su);
  btMostrarMuertos = new Button(275, 90, 15, color_mu);
  btInfo = new Button(275,230,15, 15, "i",5);
  btNext = new Button(265,560,20,20,"Play",5);

  scVecindad = new Scaler("Radio de vecindad",90,300,110,5);
  scVecindad.nivel = 2;
	scVecindad.hide();

  model_init();
}
 
void goTo(float x2, float y2){
  int directionX;
  float m; //Pendiente
  m = (y2 - myo) / (x2 - mxo);
  if (x2 > mxo)
    directionX = 1;
  else
    directionX = -1;
  
  if((x2 != mxo) && go){
    mxo = mxo + directionX*Times.VEL;
  }
  if((y2 != myo) && go){
    myo = m * mxo;
  }
  
  if(abs(x2-mxo) <= Times.VEL){
    go = false;
    mxo = x2;
    myo = y2;
  }
}
void draw() {
    image(panel, 0, 0);
	  model_draw();	
  	if (play){
    	data = contar();
  	}
    graficar(36,258,230,230,data);
    slDensidad.display();
    slEnfermar.display();
    slInfeccioso.display();
    slReInfeccioso.display();
    slReAsintomatico.display();
    slMorir.display();
    btPlay.display(0);
    btMostrarInfectados.display(0);
    btMostrarRecuperados.display(0);
    btMostrarSusceptibles.display(0);
    btMostrarMuertos.display(0);
    btNext.display(1);
    scVecindad.display();
    slMinInfectados.display();
    slVelocidad.display();
    slDias.display();
    slDimensiones.display();
    btStop.display(0);
    btInfo.display(0);

    if(btPlay.onClick){
      if (start){
        play = !play;

      	if (!play)
      		btPlay.text = "Play";
      	else
      		btPlay.text = "Pause";
      }

      else{
      	btStop.show();
        start = true;
      	btPlay.text = "Pause";
      	slDias.isActive=false;
      	slDimensiones.isActive=false;
      	d = int(slDimensiones.value);
      	total_days = int(slDias.value);
      	model_init();
			}
   		delay(50);
    }

    if(btNext.onClick){
    	page++;
    	if (page == 3)
    		page = 0;

    	switch (page){
    		case 0:
		    	slDensidad.show();
		    	slEnfermar.show();
		    	slInfeccioso.show();
		    	slReInfeccioso.show();
		    	slReAsintomatico.show();
		    	slMorir.show();
    			scVecindad.hide();
    			slMinInfectados.hide();
    			slVelocidad.hide();
          slDias.hide();
          slDimensiones.hide();
    		break;

	    	case 1:
    			scVecindad.show();
    			slMinInfectados.show();
		    	slDensidad.hide();
		    	slEnfermar.hide();
		    	slInfeccioso.hide();
		    	slReInfeccioso.hide();
		    	slReAsintomatico.hide();
		    	slMorir.hide();
	    	break;

	    	case 2:
    			slVelocidad.show();
          slDias.show();
          slDimensiones.show();
    			scVecindad.hide();
    			slMinInfectados.hide();
	    	break;
    	}


    }

    if(btMostrarInfectados.onClick){
      mostrarInfectados = !mostrarInfectados;
      if(mostrarInfectados)
      	btMostrarInfectados.actual_prim = color_in;
      else
      	btMostrarInfectados.actual_prim = color(#bbbbbb);
      delay(50);
    }


    if(btMostrarRecuperados.onClick){
      mostrarRecuperados = !mostrarRecuperados;
      if(mostrarRecuperados)
      	btMostrarRecuperados.actual_prim = color_re;
      else
      	btMostrarRecuperados.actual_prim = color(#bbbbbb);
      delay(50);
    }


    if(btMostrarSusceptibles.onClick){
      mostrarSusceptibles = !mostrarSusceptibles;
      if(mostrarSusceptibles)
      	btMostrarSusceptibles.actual_prim = color_su;
      else
      	btMostrarSusceptibles.actual_prim = color(#bbbbbb);
      delay(50);
    }


    if(btMostrarMuertos.onClick){
      mostrarMuertos = !mostrarMuertos;
      if(mostrarMuertos)
      	btMostrarMuertos.actual_prim = color_mu;
      else
      	btMostrarMuertos.actual_prim = color(#bbbbbb);
      delay(50);
    }

    if (btMostrarInfectados.onMe || btMostrarRecuperados.onMe || btMostrarSusceptibles.onMe || btMostrarMuertos.onMe){
			fill(color_su);
			rect(mouseX+16,mouseY+5,70,22);
			textSize(10);
    	fill(#FFFFFF);
    }
    if(btMostrarInfectados.onMe){
    	text("Infectados",mouseX+20,mouseY+20);
    }

    if(btMostrarRecuperados.onMe){
    	text("Recuperados",mouseX+20,mouseY+20);
    }

    if(btMostrarSusceptibles.onMe){
    	text("Susceptibles",mouseX+20,mouseY+20);
    }

    if(btMostrarMuertos.onMe){
    	text("Fallecidos",mouseX+20,mouseY+20);
    }



    if (btInfo.onMe){
			fill(color_su);
			rect(mouseX+16,mouseY+5,200,70);
			textSize(10);
    	fill(#FFFFFF);
    	text("Máx infectados: ".concat(str(max(historia[0]))),mouseX+20,mouseY+20);
    	text("Acumulado Infectados: ".concat(str(sum(historia[0]))),mouseX+20,mouseY+35);
    	text("Recuperados: ".concat(str(max(historia[0]))),mouseX+20,mouseY+50);
    	text("Fallecidos: ".concat(str(max(historia[3]))),mouseX+20,mouseY+65);
    }


    if(btStop.onClick){
      btPlay.text = "Play";
    	ready = false;
    	start = false;
    	clear();
    	slDias.isActive=true;
    	slDimensiones.isActive=true;
    	btStop.hide();
    }
}
 
void mouseDragged(){
  xo = xo - (mouseX - pmouseX)/cellSize;
  yo = yo - (mouseY - pmouseY)/cellSize;
  if(matrixSize/cellSize+xo>d){
    xo = d-matrixSize/cellSize;
  }
  if(matrixSize/cellSize+yo>d){
    yo = d-matrixSize/cellSize;
  }
  if(xo<0){
    xo = 0;
  }
  if(yo<0){
    yo = 0;
  }
}

int sum(int data[]){
	int suma = 0;
	int i;
	for(i = 0; i< data.length; i++){
		suma+=data[i];
	}
	return suma;
}
void mouseClicked(){
}
