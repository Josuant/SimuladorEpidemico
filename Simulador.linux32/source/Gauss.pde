float[][] gaussMatrix(int longitud){
  
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
