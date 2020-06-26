
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
  
  void display(){
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
        text(str(int(value)).concat("x").concat(str(int(value))),x,y+ALTO+10);
        break;
      case NUMERO:
        text(str(int(value)),x,y+ALTO+10);
        break;
    }

    textFont(quickTitle);
    fill(actual_white);
    text(titulo, x,y-ALTO);
  }

  void changeFormat(int form){
    format = form;
  }
}
