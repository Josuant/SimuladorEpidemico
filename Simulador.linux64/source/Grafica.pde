
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

void graficar(int x, int y, int w, int h, int data[]){
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
  fill(#FFFFFF);
  text(str(max_y), x+5,y-h+7);
  text("0", x+5,y+7);
  text(day, x+w-5,y+7);
  if((mouseX > x && mouseX < x+w)  && (mouseY < y && mouseY > y-h)){
      actual_x = map(mouseX, x, x+w,0,day);
      actual_y = map(mouseY, y, y-h,0,max_y);
      text(str(int(actual_x)).concat(" , ").concat(str(int(actual_y))),mouseX+5,mouseY+5);
  }

}
