import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Mapa extends PApplet {

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

public void setup() {
  strokeJoin(ROUND);
  icon = loadImage("icon.png");
  surface.setTitle("Simulador expansión vírica");
  surface.setIcon(icon);
  
  background(0xff353744);
  
  quick12 = createFont("Quicksand-Bold.ttf", 11);
  quickTitle = createFont("Quicksand-Medium.ttf", 15);

  panel = loadImage("panel.png");
  slDensidad				= new Slider("Densidad inicial",40,300,200,70,0,100);
  slEnfermar				= new Slider("Prob. enfermar",40,350,200,30,0,100);
  slInfeccioso			= new Slider("Prob. ser infeccioso",40,400,200,60,0,100);
  slReInfeccioso		= new Slider("Recuperación (infeccioso)",40,450,200,5,0,100);
  slReAsintomatico	= new Slider("Recuperación (asintomático)",40,500,200,2,0,100);
  slMorir						= new Slider("Prob. de morir",40,550,200,0.1f,0,100);
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
 
public void goTo(float x2, float y2){
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
public void draw() {
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
      	d = PApplet.parseInt(slDimensiones.value);
      	total_days = PApplet.parseInt(slDias.value);
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
      	btMostrarInfectados.actual_prim = color(0xffbbbbbb);
      delay(50);
    }


    if(btMostrarRecuperados.onClick){
      mostrarRecuperados = !mostrarRecuperados;
      if(mostrarRecuperados)
      	btMostrarRecuperados.actual_prim = color_re;
      else
      	btMostrarRecuperados.actual_prim = color(0xffbbbbbb);
      delay(50);
    }


    if(btMostrarSusceptibles.onClick){
      mostrarSusceptibles = !mostrarSusceptibles;
      if(mostrarSusceptibles)
      	btMostrarSusceptibles.actual_prim = color_su;
      else
      	btMostrarSusceptibles.actual_prim = color(0xffbbbbbb);
      delay(50);
    }


    if(btMostrarMuertos.onClick){
      mostrarMuertos = !mostrarMuertos;
      if(mostrarMuertos)
      	btMostrarMuertos.actual_prim = color_mu;
      else
      	btMostrarMuertos.actual_prim = color(0xffbbbbbb);
      delay(50);
    }

    if (btMostrarInfectados.onMe || btMostrarRecuperados.onMe || btMostrarSusceptibles.onMe || btMostrarMuertos.onMe){
			fill(color_su);
			rect(mouseX+16,mouseY+5,70,22);
			textSize(10);
    	fill(0xffFFFFFF);
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
    	fill(0xffFFFFFF);
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
 
public void mouseDragged(){
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

public int sum(int data[]){
	int suma = 0;
	int i;
	for(i = 0; i< data.length; i++){
		suma+=data[i];
	}
	return suma;
}
public void mouseClicked(){
}
class Button extends UI{
String text;
int text_size;
  Button(int x,int y,int w,int h, String text,int text_size){
    super(x,y);
    this.xo = w;
    this.yo = h;
    this.text_size = text_size;
    this.text = text;
    primAvailable=true;
    bgAvailable=true;
    secAvailable=true;
  }

  Button(int x,int y,int r, int c){
    super(x,y);
    this.xo = r;
    this.yo = r;
    isColored = false;
    actual_prim=c;

    text="";
  }

  public void display(int c){
    isOverMe();
    if (c == 0)
      fill(actual_prim);
    else
      fill(actual_bg);
    noStroke();
    if (actual_prim == color_su || c == 1){
      strokeWeight(2);
      stroke(0xff555566);
    }
    circle(x+yo/2,y+yo/2,yo);
    if(xo > 0){
      rect(x+yo/2,y,xo-yo,yo);
      circle(x-yo/2+xo,y+yo/2,yo);
    }
    fill(actual_white);
    if(text.equals("Play")){
      triangle(x+xo/2-text_size,y+yo/2+text_size,x+xo/2-text_size,y+yo/2-text_size,x+xo/2+text_size,y+yo/2);
      return;
    }
    if(text.equals("Pause")){
      rect(x+xo/2-text_size-text_size/2,y+yo/2-text_size,text_size,text_size*2);
      rect(x+xo/2-text_size+text_size,y+yo/2-text_size,text_size,text_size*2);
      return;
    }
    if (text_size > 0){
      textFont(quick12);
      textSize(text_size);
      text(text,x+xo/2-PApplet.parseInt(text_size),y+yo/2+PApplet.parseInt(text_size/3));
    }
  }
}
public float[][] gaussMatrix(int longitud){
  
  float matrix[];
  float matrix2[];
  float matrix3[];
  float matrix4[];
  
  float gauss[][];
  matrix = new float [longitud];
  matrix2 = new float [longitud];
  matrix3 = new float [longitud];
  matrix4 = new float [longitud];
  gauss = new float [longitud][longitud];
  
  for (int i = 0; i < longitud; i++){
     matrix[i] = randomGaussian();
     matrix2[i] = randomGaussian();
  }
  matrix = sort(matrix);
  matrix = subset(matrix, longitud/2,longitud/2);
  matrix3 = subset(matrix, 0,longitud/2);
  matrix2 = sort(matrix2);
  matrix2 = subset(matrix2, longitud/2,longitud/2);
  matrix4 = subset(matrix, 0,longitud/2);
  
  for (int i = 0; i < longitud/2; i++){
     matrix[i] = norm(matrix[i],min(matrix),max(matrix));
     matrix2[i] = norm(matrix2[i],min(matrix2),max(matrix2));
     matrix3[i] = norm(matrix3[i],min(matrix3),max(matrix3));
     matrix4[i] = norm(matrix4[i],min(matrix4),max(matrix4));
  }
  
  matrix = concat(matrix, reverse(matrix3));
  matrix2 = concat(matrix2, reverse(matrix4));
  
  for (int i = 0; i<longitud; i++){
    for (int j = 0; j<longitud; j++){
      gauss[i][j] = matrix[i] * matrix2[j];
    }
    println();
  }
  return gauss;
}

boolean mostrarInfectados = true;
boolean mostrarRecuperados = true;
boolean mostrarSusceptibles = true;
boolean mostrarMuertos = true;
int i;
int size = 3;
int max_y;
int maximos[];
int actual_day;
float actual_x,actual_y;

public void graficar(int x, int y, int w, int h, int data[]){
  if (day < total_days)
    actual_day = day;
  else
    actual_day = total_days-1;
  historia[0][actual_day] = data[0];
  historia[1][actual_day] = data[1];
  historia[2][actual_day] = data[2];
  historia[3][actual_day] = data[3];
  
  maximos = new int[4];
  i = 0;

  if (mostrarInfectados){
    maximos[i] = max(historia[0]);
    i++;
  }
  if (mostrarRecuperados){
    maximos[i] = max(historia[1]);
    i++;
  }
  if (mostrarSusceptibles){
    maximos[i] = max(historia[2]);
    i++;
  }
  if (mostrarMuertos){
    maximos[i] = max(historia[3]);
    i++;
  }

  //stroke(51, 51, 68);
  //fill(11, 11, 28,230);
  //rect(x,y-h,w,h);
  for (i = 0; i<day; i++){
      max_y = max(maximos);
      noStroke();
      fill(color_in);
      if(mostrarInfectados)
        circle(map(i, 0, day, x, x+h),map(historia[0][i],0,max_y,y,y-h), size);
      fill(color_re);
      if(mostrarRecuperados)
        circle(map(i, 0, day, x, x+h),map(historia[1][i],0,max_y,y,y-h), size);
      fill(color_su);
      if(mostrarSusceptibles)
        circle(map(i, 0, day, x, x+h),map(historia[2][i],0,max_y,y,y-h), size);
      fill(color_mu);
      if(mostrarMuertos)
        circle(map(i, 0, day, x, x+h),map(historia[3][i],0,max_y,y,y-h), size);
  }

  textSize(10);
  fill(0xffFFFFFF);
  text(str(max_y), x+5,y-h+7);
  text("0", x+5,y+7);
  text(day, x+w-5,y+7);
  if((mouseX > x && mouseX < x+w)  && (mouseY < y && mouseY > y-h)){
      actual_x = map(mouseX, x, x+w,0,day);
      actual_y = map(mouseY, y, y-h,0,max_y);
      text(str(PApplet.parseInt(actual_x)).concat(" , ").concat(str(PApplet.parseInt(actual_y))),mouseX+5,mouseY+5);
  }

}
int xo;
int yo;
int xi = 300;
int yi = 0;

boolean ready;

//Dimension de la ventana
int matrixSize = 600;
  
// Dimension de la matriz
int d = 600;

//Dimensiones m áximas y mínimas
final static int MIN_D = 50;
final static int MAX_D = 2000;


//Támaños máximos y mínimos
int MIN_S = 2;
final static int MAX_S = 50;

// Tamaño de la célula
int cellSize;

// Time
int intervalo = 50;
int ultimo_tiempo = 0;

int total_days = 400;
int day = 0;
int[][] historia;

//Colors
int color_su = 0xff333344;
int color_ex = 0xff553344;
int color_in = 0xffff3366;
int color_as = 0xffff3399;
int color_re = 0xff33ee66;
int color_mu = 0xff000000;

// Cells
int[][] cells;
int[][] cellsBuffer;

int[] data;

//Ui
Radio radio_infeccion;

public void randomMatrix(){
  int state;
  float gauss [][];
  gauss = gaussMatrix(radio_infeccion.radio);
  int min_x = PApplet.parseInt(radio_infeccion.x)-300-(radio_infeccion.radio);
  int max_x = PApplet.parseInt(radio_infeccion.xo)-300-(radio_infeccion.radio);
  int min_y = PApplet.parseInt(radio_infeccion.y)-radio_infeccion.radio;
  int max_y = PApplet.parseInt(radio_infeccion.yo)-radio_infeccion.radio;
  for(int y=0; y < d; y++){
    for(int x=0; x < d; x++){
      state = SUSCEPTIBLE;
      cells[x][y] = state;
      if(x >= min_x/cellSize && x < max_x/cellSize){
        if(y >= min_y/cellSize && y < max_y/cellSize){
          float  z = random(1);
          if (z < gauss[x- min_x/cellSize][y- min_y/cellSize]){
            //z = random(1);
            if (z < p_ex )
              state = EXPUESTO;
            else
              state = SUSCEPTIBLE;
            cells[x][y] = state;
          }
        }
      }

    }
  }
}

public void model_init(){
  day = 0;
  ready = false;
  strokeWeight(1);
  background(0xff000000);
  cellSize = MIN_S;
  xo=yo=0;

  //Inicialización de arrays
  cells = new int[d][d];
  cellsBuffer = new int[d][d];
  data = new int[4];
  historia = new int[4][total_days];
  radio_infeccion = new Radio(0, 0, 20);
  radio_infeccion.isActive = true;

  mostrarInfectados = true;
  mostrarRecuperados = true;
  mostrarSusceptibles = true;
  mostrarMuertos = true;
}

public void  model_draw(){

  if(!start){
    delay(100);
    return;
  }

  if (!ready){
    radio_infeccion.display();
    if(radio_infeccion.onClick){
      p_ex = slDensidad.value/100;
      radio_infeccion.isActive =false;
      randomMatrix();
      ready = true;
    }
    return;
  }

  noStroke();
  //Dibujar cell
  for (int x=0; x<matrixSize/cellSize; x++) {
    for (int y=0; y<matrixSize/cellSize; y++) {
      
      switch(cells[x+xo][y+yo]){
        case SUSCEPTIBLE:
          fill(color_su);
          break;
        case EXPUESTO:
          fill(color_ex);
          break;
        case INFECCIOSO:
          fill(color_in);
          break;
        case ASINTOMATICO:
          fill(color_as);
          break;
        case RECUPERADO:
          fill(color_re);
          break;
        case MUERTO:
          fill(color_mu);
          break;
      }
      rect ((x*cellSize)+xi, (y*cellSize)+yi, cellSize, cellSize);
    }
  }
  
  if(day < total_days){
    // Contar milisegundos
    if (millis()-ultimo_tiempo>intervalo) {
      if (play) {
        p_en    = slEnfermar.value/100;
        p_in    = slInfeccioso.value/100;
        p_rein  = slReInfeccioso.value/100;
        p_reas  = slReAsintomatico.value/100;
        p_me    = slMorir.value/100; 
        radio = scVecindad.nivel;
        vecindad = PApplet.parseInt(pow ((2*radio + 1) , 2) - 1);
        min_infectados = PApplet.parseInt((slMinInfectados.value/100) * vecindad);
        intervalo = PApplet.parseInt(50/(slVelocidad.value/100));
        iteration();
        ultimo_tiempo = millis();
        day++;
      }
    }
  }else{
    day = total_days;
  }

}

public void iteration() {
  // Guardamos el estado actual.
  for (int x=0; x<d; x++) {
    for (int y=0; y<d; y++) {
      cellsBuffer[x][y] = cells[x][y];
    }
  }
  
  // Recorrido por cada celula
  for (int x=0; x<d; x++) {
    for (int y=0; y<d; y++) {
      int state = cells[x][y];
      cells[x][y] = applyRule(state, x, y);
      movimiento(x,y);
    }
  }
}

public void restart(){
    model_init();
    randomMatrix();
}


public void clear(){
  for (int x=0; x<d; x++) {
    for (int y=0; y<d; y++) {
      cells[x][y] = SUSCEPTIBLE;
    }
  }
}

public void keyPressed() {
  if (key=='r' || key == 'R') {
    // Restart
    restart();
  }
  
  if (key==' ') {
    play = !play;
    if (!play)
      btPlay.text = "Play";
    else
      btPlay.text = "Pause";
  }
}

public void mouseWheel(MouseEvent event) {
  if (mouseX >=300){
    int e = event.getCount();
    if (e == -1){
      if (!ready)
        radio_infeccion.aumentarRadio();
       else
         if (cellSize < MAX_S)
           cellSize += 1;
    }else{
      if(!ready)
        radio_infeccion.disminuirRadio();
      else{
        if (cellSize > MIN_S){
          xo=yo=0;
        cellSize -= 1;
        }
      }
    }
  }
}
class Panel extends UI{
  ArrayList<UI> elements;
  Panel(int x, int y, int w, int h){
    super(x,y);
    this.xo = w;
    this.yo = h;
    bgAvailable=false;
  }
  
  public void display(){
    isOverMe();
    noStroke();
    fill(actual_bg);
    rect(x,y,xo,yo);
  }
}
class Radio extends UI{
int radio;
boolean isActive;

  Radio(int x,int y,int r){
    super(x,y);
    this.radio = r;
    xo = x+2*r;
    yo = y+2*r;
  }
  
  public void display(){
  	if (!isActive)
  		return;
  	onClick = false;
    isOverMe();
    noFill();
    stroke(0xffFFFFFF);
    strokeWeight(4);
    if(mouseX >= 300+radio){
    	if (mouseX <= 900-radio)
    		if (mouseX > 585 && mouseX < 615)
    			x = 600;
    		else
    			x=PApplet.parseInt(mouseX);
    	else
    		x = 900-radio;
    }else
    	x = 300+radio;

    if(mouseY >= radio){
    	if (mouseY <= 600-radio)
    		if (mouseY > 285 && mouseY < 315)
    			y = 300;
    		else
    			y=PApplet.parseInt(mouseY);
    	else
    		y = 600-radio;
    }else
    	y = radio; 

    xo = x+2*radio;
    yo = y+2*radio;

    circle(x,y,radio*2);
  }

  public void aumentarRadio(){
  	if (radio <= 300)
  		radio+=4;
  	else
  		radio = 300;
  }
  public void disminuirRadio(){
  	if (radio >= 10)
  		radio-=4;
  	else
  		radio = 10;
  }
  
}
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

	public void display(){
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
          			nivel = PApplet.parseInt(map(distance,medida*2,l/2,2.0f,PApplet.parseFloat(niveles)));
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

final static int PORCENTAJE = 0;
final static int DIMENSIONES = 1;
final static int NUMERO = 2;

class Slider extends UI{

String titulo;
float value;
float posicion=0;
float min_value;
float max_value;
boolean isActive;
int format;

final static int ALTO = 8;
final static int RADIO_SELECTOR = 12;


  Slider(String titulo,int x,int y,int w, float v, float min, float max){
    super(x,y);
    this.xo = w;
    this.yo = ALTO;
    this.titulo = titulo;
    value = v;
    primAvailable=true;
    min_value = min;
    max_value = max;
    posicion = map(value, min_value,max_value,x+ALTO/2,x+xo+ALTO/2);
    isActive = true;
    format = PORCENTAJE;
  }
  
  public void display(){
    isOverMe();
    noStroke();
    if(isActive)
      fill(actual_bg);
    else
      fill(actual_nd);
    circle(x+ALTO/2,y+ALTO/2,ALTO);
    rect(x+ALTO/2,y,xo,ALTO);
    circle(x+ALTO/2+(xo),y+ALTO/2,ALTO);
    if(isActive)
      fill(actual_prim);
    else
      fill(actual_nd);
    circle(posicion,y+ALTO/2,RADIO_SELECTOR);

    if(onClick && isVisible && isActive){
      if(mouseX>= x+ALTO/2)
        if(mouseX <= x+ALTO/2+xo)
          posicion = mouseX;
        else
          posicion = x+ALTO/2+xo;
      else
        posicion = x+ALTO/2;
    }

    value = map(posicion,x+ALTO/2,x+xo+ALTO/2,min_value,max_value);
    textFont(quick12);
    fill(actual_sec);
    switch (format){
      case PORCENTAJE:
        text(str(value).concat("%"),x,y+ALTO+10);
        break;
      case DIMENSIONES:
        text(str(PApplet.parseInt(value)).concat("x").concat(str(PApplet.parseInt(value))),x,y+ALTO+10);
        break;
      case NUMERO:
        text(str(PApplet.parseInt(value)),x,y+ALTO+10);
        break;
    }

    textFont(quickTitle);
    fill(actual_white);
    text(titulo, x,y-ALTO);
  }

  public void changeFormat(int form){
    format = form;
  }
}
   abstract class UI{
  final int BG = color(0xff454852);
  final int BG_F = color(0xff676A74);
  final int BG_P = color(0xff232630);
  
  final int PRIM = color(0xffFA6B30);
  final int PRIM_F = color(0xffFC8D52);
  final int PRIM_P = color(0xffD84910);
  final int SEC = color(0xffA1613B);
  final int SEC_F = color(0xffED8F58);
  final int SEC_P = color(0xff875132);
  final int WHITE = color(0xffFFFFFF);
  final int BLACK = color(0xff000000);
  final int NO_DISPONIBLE = color(0xffbbbbbb);
  
  
  
  int opacity;
  int  actual_bg;
  int  actual_prim;
  int  actual_sec;
  int actual_white;
  int actual_black;
  int actual_nd;

  float x, y, xo, yo;
  boolean onMe;
  boolean onClick;

  boolean bgAvailable;  
  boolean primAvailable;
  boolean secAvailable;
  
  boolean isVisible;
  boolean isColored;

  UI(int x, int y){
    isVisible = true;
    isColored = true;
    opacity = 255;
    this.x= x;
    this.y =y;
  }
  
  public int getColor(int c){
    return color(red(c),green(c),blue(c),opacity);
  }
  
  public void isOverMe(){
    if ((mouseX >= x && mouseX <= x+xo)  && (mouseY >= y && mouseY <= y+yo )){
        onMe= true;
      if(mousePressed){
        if (bgAvailable)  actual_bg = getColor(BG_P);
        if (primAvailable)  actual_prim = getColor(PRIM_P);
        if (secAvailable)  actual_sec = getColor(SEC_P);
        onClick = true;
      }else{
        if (bgAvailable)  actual_bg = getColor(BG_F);
        if (primAvailable)  actual_prim = getColor(PRIM_F);
        if (secAvailable)  actual_sec = getColor(SEC_F);
        onClick = false;
      }
    }else{
      if (isColored){
        actual_bg = getColor(BG);
        actual_prim = getColor(PRIM);
        actual_sec = getColor(SEC);
        actual_white = getColor(WHITE);
        actual_black = getColor(BLACK);
        actual_nd = getColor(NO_DISPONIBLE);
      }
      onMe= false;
    }
  }
  
  public void hide(){
    opacity = 0;
    isVisible = false;
    }

  public void show(){
    opacity = 255;
    isVisible= true;
  }
  
}
static final int  SUSCEPTIBLE = 1;  //Susceptible.
static final int  EXPUESTO = 2;      //Expuesto.
static final int  INFECCIOSO = 3;    //Infeccioso.
static final int  ASINTOMATICO = 4;  //Asintomático.
static final int  RECUPERADO = 5;    //Recuperado.
static final int  MUERTO = 6;        //Muerto-Vacio.

float p_mn    = 0.01f;      // Probabilidad de morir por causas naturales.
float p_ex;        // Probabilidad de pasar a un estado de expuesto espontáneamente.
float p_en;        // Probabilidad de enfermar una vez que se ha expuesto;
float p_in;        /* Probabilidad de ser infeccioso una vez que se ha enfermado.
                      Probabilidad de ser asintomático una vez que se ha enfermado: 1-p_in*/
float p_rein;      // Probabilidad de recuperarse si se es infeccioso.
float p_reas;      // Probabilidad de recuperarse si se es asintomático.
float p_me;        // Probabilidad de morir por enfermedad

int min_infectados;  // Mínimo de infectados para que haya transmisión.
int radio;            // Radio de la vecindad de Moore.
int vecindad;  //vecindad de Moore.

public int infectarse(int state, int infectados){
  if (state == SUSCEPTIBLE){
    float  z = random(1);
    if (infectados >= min_infectados )
      return EXPUESTO;
    else
      return SUSCEPTIBLE;
  }
  return state;
}

public int enfermar(int state){
  if (state == EXPUESTO){
    float  z = random(1);
    if (p_en > z){
      z = random(1);
      if (p_in < z)
        return INFECCIOSO;
      else
        return ASINTOMATICO;
    }
  }
  return state;
}

public int recuperarse(int state){
  if (state == ASINTOMATICO){
    float  z = random(1);
    if (p_reas > z){
        return RECUPERADO;
    }
  }else if (state == INFECCIOSO){
    float  z = random(1);
    if (p_rein > z){
        return RECUPERADO;
    }
  }
  return state;
}

public int morir(int state){
  if (state == ASINTOMATICO || state == INFECCIOSO){
    float  z = random(1);
    if (p_me > z){
        return MUERTO;
    }
  }
  return state;
}


public int natural(int state){
  float  z1 = random(1);
  float  z2 = random(1);
  if (p_mn > z1)
    return MUERTO;
  if (state == MUERTO){
    if (p_mn > z2)
      return SUSCEPTIBLE;
  }
  return state;
}

public void movimiento(int x, int y){
  if (((x>0+radio-1)&&(x<d-(radio-1)))&&((y>0+radio-1)&&(y<d-(radio-1)))){
    int z1 = PApplet.parseInt(random(-1*radio,radio));
    int z2 = PApplet.parseInt(random(-1*radio,radio));
    int aux = cells[x][y];
    cells[x][y] = cells[x+z1][y+z2];
    cells[x+z1][y+z2] = aux;
  }
}

public int applyRule(int state, int x, int y){
  int new_state = 0;
  new_state = infectarse(state, countInfectados(x,y));
  new_state = enfermar(new_state);
  new_state = recuperarse(new_state);
  new_state = morir(new_state);
  //new_state = natural(new_state);
  return new_state;
}


public int countInfectados(int x, int y){
  int infectados = 0; // Contador de infectados

  for (int xx=x-radio; xx<=x+radio;xx++) {
    for (int yy=y-radio; yy<=y+radio;yy++) {  
      
      if (((xx>=0+radio-1)&&(xx<d-(radio-1)))&&((yy>=0+radio-1)&&(yy<d-(radio-1)))){
        if (!((xx==x)&&(yy==y))){
          if (cellsBuffer[xx][yy]==INFECCIOSO || cellsBuffer[xx][yy]==ASINTOMATICO){
            infectados ++;
          }
        }
      }
    }
  }
  return infectados;
}

public int[] contar(){
  int infectados = 0;
  int recuperados = 0;
  int susceptibles = 0;
  int muertos = 0;
  int output[] = new int[4];
    for (int x=0; x<d; x++) {
      for (int y=0; y<d; y++) {
        if (cellsBuffer[x][y] == INFECCIOSO || cellsBuffer[x][y] == ASINTOMATICO )
          infectados++;
        if (cellsBuffer[x][y] == RECUPERADO )
          recuperados++;
        if (cellsBuffer[x][y] == SUSCEPTIBLE)
          susceptibles++;
        if (cellsBuffer[x][y] == MUERTO)
          muertos++;
      }
    }
    output[0] = infectados;
    output[1] = recuperados;
    output[2] = susceptibles;
    output[3] = muertos;
    return output;
}
static abstract class Times{
  final static int SUAV = 10;
  final static int VEL = 3;
}
  public void settings() {  size(900, 600);  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Mapa" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
