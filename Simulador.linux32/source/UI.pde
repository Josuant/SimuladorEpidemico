   abstract class UI{
  final color BG = color(#454852);
  final color BG_F = color(#676A74);
  final color BG_P = color(#232630);
  
  final color PRIM = color(#FA6B30);
  final color PRIM_F = color(#FC8D52);
  final color PRIM_P = color(#D84910);
  final color SEC = color(#A1613B);
  final color SEC_F = color(#ED8F58);
  final color SEC_P = color(#875132);
  final color WHITE = color(#FFFFFF);
  final color BLACK = color(#000000);
  final color NO_DISPONIBLE = color(#bbbbbb);
  
  
  
  int opacity;
  color  actual_bg;
  color  actual_prim;
  color  actual_sec;
  color actual_white;
  color actual_black;
  color actual_nd;

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
  
  color getColor(color c){
    return color(red(c),green(c),blue(c),opacity);
  }
  
  void isOverMe(){
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
  
  void hide(){
    opacity = 0;
    isVisible = false;
    }

  void show(){
    opacity = 255;
    isVisible= true;
  }
  
}
