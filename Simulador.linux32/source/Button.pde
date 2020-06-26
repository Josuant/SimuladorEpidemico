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

  Button(int x,int y,int r, color c){
    super(x,y);
    this.xo = r;
    this.yo = r;
    isColored = false;
    actual_prim=c;

    text="";
  }

  void display(int c){
    isOverMe();
    if (c == 0)
      fill(actual_prim);
    else
      fill(actual_bg);
    noStroke();
    if (actual_prim == color_su || c == 1){
      strokeWeight(2);
      stroke(#555566);
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
      text(text,x+xo/2-int(text_size),y+yo/2+int(text_size/3));
    }
  }
}
