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
color color_su = #333344;
color color_ex = #553344;
color color_in = #ff3366;
color color_as = #ff3399;
color color_re = #33ee66;
color color_mu = #000000;

// Cells
int[][] cells;
int[][] cellsBuffer;

int[] data;

//Ui
Radio radio_infeccion;

void randomMatrix(){
  int state;
  float gauss [][];
  gauss = gaussMatrix(radio_infeccion.radio);
  int min_x = int(radio_infeccion.x)-300-(radio_infeccion.radio);
  int max_x = int(radio_infeccion.xo)-300-(radio_infeccion.radio);
  int min_y = int(radio_infeccion.y)-radio_infeccion.radio;
  int max_y = int(radio_infeccion.yo)-radio_infeccion.radio;
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

void model_init(){
  day = 0;
  ready = false;
  strokeWeight(1);
  background(#000000);
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

void  model_draw(){

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
        vecindad = int(pow ((2*radio + 1) , 2) - 1);
        min_infectados = int((slMinInfectados.value/100) * vecindad);
        intervalo = int(50/(slVelocidad.value/100));
        iteration();
        ultimo_tiempo = millis();
        day++;
      }
    }
  }else{
    day = total_days;
  }

}

void iteration() {
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

void restart(){
    model_init();
    randomMatrix();
}


void clear(){
  for (int x=0; x<d; x++) {
    for (int y=0; y<d; y++) {
      cells[x][y] = SUSCEPTIBLE;
    }
  }
}

void keyPressed() {
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

void mouseWheel(MouseEvent event) {
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
