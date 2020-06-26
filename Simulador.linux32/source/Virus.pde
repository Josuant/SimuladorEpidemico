static final int  SUSCEPTIBLE = 1;  //Susceptible.
static final int  EXPUESTO = 2;      //Expuesto.
static final int  INFECCIOSO = 3;    //Infeccioso.
static final int  ASINTOMATICO = 4;  //Asintomático.
static final int  RECUPERADO = 5;    //Recuperado.
static final int  MUERTO = 6;        //Muerto-Vacio.

float p_mn    = 0.01;      // Probabilidad de morir por causas naturales.
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

int infectarse(int state, int infectados){
  if (state == SUSCEPTIBLE){
    float  z = random(1);
    if (infectados >= min_infectados )
      return EXPUESTO;
    else
      return SUSCEPTIBLE;
  }
  return state;
}

int enfermar(int state){
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

int recuperarse(int state){
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

int morir(int state){
  if (state == ASINTOMATICO || state == INFECCIOSO){
    float  z = random(1);
    if (p_me > z){
        return MUERTO;
    }
  }
  return state;
}


int natural(int state){
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

void movimiento(int x, int y){
  if (((x>0+radio-1)&&(x<d-(radio-1)))&&((y>0+radio-1)&&(y<d-(radio-1)))){
    int z1 = int(random(-1*radio,radio));
    int z2 = int(random(-1*radio,radio));
    int aux = cells[x][y];
    cells[x][y] = cells[x+z1][y+z2];
    cells[x+z1][y+z2] = aux;
  }
}

int applyRule(int state, int x, int y){
  int new_state = 0;
  new_state = infectarse(state, countInfectados(x,y));
  new_state = enfermar(new_state);
  new_state = recuperarse(new_state);
  new_state = morir(new_state);
  //new_state = natural(new_state);
  return new_state;
}


int countInfectados(int x, int y){
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

int[] contar(){
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
