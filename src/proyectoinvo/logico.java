/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoinvo;
 import java.util.Random;
import java.util.ArrayList;

/**
 *
 * @author Rafael
 */
public class logico {
    
    public Double cInv, cOrden,FcE,FsE; //Costos usados
    
    
    int menor, mayor; // Q menor y mayor, dada por el usuario
    int invIni;       // inventario Inicial
    int qMenor,qMayor; // Q menor calculada y Q mayor calculada
    int lMenor,lMayor; // l menor y mayor, dada por el usuario
    int rMenor,rMayor; // r menor y mayor calculada en una funcion
   
    ArrayList<Integer> arrayDem,arrayPD,arrayEsp,arrayPEsp,arrayEntre,arrayPEntre; // Arreglos para guardar los datos dados
    ArrayList<Double> arrayCostos; // Arreglo de costos y inventario Inicial
    ArrayList<Integer> acumDemanda,acumEspera,acumEntrega; // Acumulado de las probabilidades de demanda esperay entrega.
    ArrayList<ArrayList> arrayResult; // Matriz que guarda todos los resultados de cada simulacion
    ArrayList<ArrayList<Integer>> tablaElmejor;
   
    int contResultados,elMejor;  // Cuantos resultados tuve y cual es el mejor
    int annio; // Año por el cual nos guiaremos
  
   //Costructor de prueba
    public logico(int w){
         correrTabla(50,75);
         invIni=w;
    }
   //Constructor 
   public logico(ArrayList<Integer> arrayDem, ArrayList<Integer> proDem, ArrayList<Integer> diasEsp, ArrayList<Integer> probaEspe, ArrayList<Integer> diasEntregas, ArrayList<Integer> probEntregas, ArrayList<Double> costos, int dias) {
      
       arrayResult = new ArrayList();
       acumDemanda= new ArrayList();
       acumEspera=new ArrayList();
       acumEntrega=new ArrayList();
       tablaElmejor= new ArrayList();
       
       //Recibo toda la informacion     
       this.arrayDem=arrayDem;
       this.arrayPD=proDem;
       this.arrayEsp=diasEsp;
       this.arrayPEsp=probaEspe;
       this.arrayEntre=diasEntregas;
       this.arrayPEntre=probEntregas;
       this.arrayCostos=costos;
       
       //Guardo la que mas voy a usar
        invIni=arrayCostos.get(0).intValue();       
        cInv=arrayCostos.get(1);
        cOrden= arrayCostos.get(2);
        FcE=arrayCostos.get(3); 
        FsE=arrayCostos.get(4);
        annio=dias;
        contResultados=-1;
        
        menor=calMenor(arrayDem);
        mayor=calMayor(arrayDem);
        
        lMenor=calMenor(arrayEntre);
        lMayor=calMayor(arrayEntre);

        calcularQyR(); // Calculo q y r
        funcionAcumulada(); // Calculo el acumulado de todas las probabilidades
        empezar(); // Empiezo la simulacion
      
   }
   
    public int calMenor(ArrayList<Integer> array){
        int menor=array.get(0);
        int aux=0;
        for(int i=1;i<array.size();i++){
            aux=array.get(i);
            if(aux<menor){
                menor=aux;
            }
        }
       return menor;
   }
    
    public int calMayor(ArrayList<Integer> array){
        int mayor=array.get(0);
        int aux=0;
        for(int i=1;i<array.size();i++){
            aux=array.get(i);
            if(aux>mayor){
                mayor=aux;
            }
        }
       return mayor;
   }
   
   public void empezar(){
    // Empieza la simulacion va desde el q menor al mayor y para cada uno de estos
    // Va desde el r menor al mayor llamando a la funcion que hace la simulacion de la tabla para cada uno
          for(int i=qMenor;i<=qMayor;i++){
           for(int j=rMenor;j<=rMayor;j++){   
               correrTabla(i,j);
           }
       }
   }
   
   
   public void calcularQyR(){
       
       //Se calcula las q
       Double qMenoraux=Math.sqrt(  ((2*cOrden*menor*365*(cInv+FsE))/(cInv*FsE))  );
       Double qMayoraux=Math.sqrt(  ((2*cOrden*mayor*365*(cInv+FcE))/(cInv*FcE))  );
       // Obtengo el valor entero
      qMenor=qMenoraux.intValue();
      qMayor=qMayoraux.intValue(); 

      // Se calculan las R.
       rMenor=lMenor*menor;
       rMayor=lMayor*mayor;
   
   }
   
   public void funcionAcumulada(){
       int aux=0;
       int cont=0;
   // 2 4  6 12 20 24 15 10 5  2 = 100  Esto hay que validarlo antes de llamar a esta clase
   // 2 6 12 24 44 68 83 93 98 100
  
   
   // Para cada uno de las distribuciones de probabilidad calculo su acumulada.
     for(int i=0;i<arrayPD.size();i++){
        aux = arrayPD.get(i);
        cont=cont+aux;
        this.acumDemanda.add(cont);
     }
    
     cont=0;
      for(int j=0;j<arrayPEntre.size();j++){
        aux =arrayPEntre.get(j);
        cont=cont+aux;
        this.acumEntrega.add(cont);
     }
      
      cont=0;
       for(int y=0;y<arrayPEsp.size();y++){
        aux =arrayPEsp.get(y);
        cont=cont+aux;
        this.acumEspera.add(cont);
     }          
   }
   
   
   // Funcion principal que hace la simulacion lara cada q y r que recibe
   // " La simulacion de la tabla con los 365 dias del año
   public void correrTabla(int i, int j){
       // Variables Auxiliares utilizadas En esta funcion
       Random  rnd = new Random(100);
      
       Double costoInventario=0.0,costoOrden=0.0, costoFaltante=0.0,costoTotal=0.0;
       
       ArrayList resultados;
       ArrayList<Integer> faltante,tiempoEspera,dias;
       ArrayList<ArrayList<Integer>> tabla;
       
       
       Integer q= i;
       Integer r= j;
       
       Double rn,rn1,rn2;
       int inv = this.invIni;
       int demanda,invFinal,tiempoEntrega=-1;
       int invPromedio=0,invPDiario=0;
       int orden=0,aux2=0,aux3=0,aux=0;
       int auxFaltante=0;
       int auxEspera=0;
       int auxQ=0;
       int auxOrden=0,auxTE=0,auxrn2=0,auxtes=0,auxrn1=0;
       
       // Inicializo los arreglos..
       faltante=new ArrayList();
       tiempoEspera=new ArrayList();
       resultados= new ArrayList();
       tabla = new ArrayList();
       dias= new ArrayList();
        
       for(int dia=1;dia<=annio;dia++){//Aqui hace la logica de una tabla 
     
        //Para cada dia..
           if(tiempoEntrega==0){ //Reviso si llego una entrega si llegosumo la Q
               inv = inv + q;
               tiempoEntrega=-1;
           }
          
           //Veo si puedo satisfacer demandas en espera
               for(int n=0;n<tiempoEspera.size();n++){          
                aux2 = tiempoEspera.get(n);
                aux3 = faltante.get(n);
                if(inv>0){ 
                  if(aux2>=0){// Hay una espera q se puede satisfacer
                      if(inv>=aux3){// se satisface completa
                          costoFaltante=costoFaltante+aux3*FcE;
                          inv=inv-aux3;
                          tiempoEspera.set(n, -2);
                      }else{
                         // Se satisface una parte
                          faltante.set(n,aux=aux3-inv);  
                          inv=0;
                          costoFaltante=costoFaltante+((aux3-aux)*FcE);
                      }
                      
                  }
                }
                   if(aux2==-1){//Reviso si a alguno se le agoto el tiempo de espera
                     costoFaltante=costoFaltante+aux3*FsE;
                     tiempoEspera.set(n, -2);
                  }
               }
           
          
        
           // Calculo la demanda
           rn=(rnd.nextDouble() * 100);
          
           demanda=numAleatorio(rn  ,arrayDem,acumDemanda);
           // Inventario Final
           invFinal=inv-demanda;
           
           // Veo si tengo faltante o no
           if(invFinal<0){
               auxFaltante=demanda-inv;
               invFinal=0;
               faltante.add(auxFaltante);
             rn1=(rnd.nextDouble() * 100);
              auxrn1=rn1.intValue();
               auxEspera=numAleatorio(rn1,arrayEsp,acumEspera);
               auxtes=auxEspera;
               if(auxEspera==0){
                   // Aqui tengo que sumar uno de faltante sin espera por que o espera nada
                   costoFaltante=costoFaltante+auxFaltante*FsE;
                   auxEspera=-2;
               }
               tiempoEspera.add(auxEspera);
           }else{
               auxrn1=0;
               auxtes=0;
               auxFaltante=0;
               faltante.add(auxFaltante);
               tiempoEspera.add(-2);
           }
          // inventario promedio
           invPromedio=(invFinal+inv)/2;
          // La suma del inventario promedio diario
           invPDiario=invPDiario+invPromedio;
           
           // Veo si puedo hacer un pedido
           if(tiempoEntrega<0){
               if( invFinal<=r ){
              rn2=(rnd.nextDouble() * 100);
              auxrn2=rn2.intValue();
              tiempoEntrega= numAleatorio(rn2,arrayEntre,acumEntrega);
              auxTE=tiempoEntrega;
              orden++;
              auxOrden=orden;
               }
           }else{  
               auxrn2=0;
               auxTE=0;
                auxOrden=0;
                tiempoEntrega--; 
           }
           
          // Guardo los datos
           dias.add(dia);
           dias.add(inv);
           dias.add(rn.intValue());
           dias.add(demanda);
           dias.add(invFinal);
           dias.add(invPromedio);
           dias.add(auxFaltante);
           dias.add(auxOrden);
           dias.add(auxrn2);
           dias.add(auxTE);
           dias.add(auxrn1);
           dias.add(auxtes);
           tabla.add(dias);
           dias= new ArrayList();
         // Calculo el inventario del nuevo dia
           inv = invFinal;
         //Le resto 1 a los tiempos de espera
         for(int t=0;t<(tiempoEspera.size()-1);t++){
             auxQ=tiempoEspera.get(t);
             if(auxQ!=-2){
             tiempoEspera.set(t, auxQ-1);
             }
         
         }
         
       } // Fin del for de 365
      
         // Calculo los costos
          costoOrden=orden*cOrden;
          costoInventario= (invPDiario*(cInv/365));
          costoTotal=costoInventario+costoOrden+costoFaltante;
       
           //Guardo los resultados
           resultados.add(i);
           resultados.add(j);
           resultados.add(costoInventario);
           resultados.add(costoOrden);
           resultados.add(costoFaltante);
           resultados.add(costoTotal);
           arrayResult.add(resultados);
           contResultados++;
           
           // Esto es para poder hacer la comparacion la primera vez
           if(contResultados==0){
               tablaElmejor=tabla;
               elMejor=0;
           }
           // Comparacion a ver si es mejor que el mejor
          Double costoAnt= Double.valueOf( (arrayResult.get(elMejor).get(5).toString()) );
          
          
            if( costoTotal <  costoAnt   ){
               elMejor=contResultados;
               tablaElmejor=tabla;
           
           }
             
       
   } 
   
  // Metodo que recibe una numero aleatorio con dos arreglos de dist de probabilidad
  // Retorna el numero que se debe usar.
   private int numAleatorio(Double alea,ArrayList<Integer> numero, ArrayList<Integer> prob){
       for(int i=0;i<prob.size();i++){
           if(alea <= Double.valueOf( prob.get(i)) ){
               return numero.get(i);
           }
       }
       return -1;
   }
   
   
   
   
 // Metodos GET para obtener todo lo que necesitamos de esta clase en la otra clase.
   
   public ArrayList<ArrayList> getResultados(){
       return arrayResult;
   }
   
   public ArrayList getResultados(Integer q,Integer r){
       for(int i=0;i<arrayResult.size();i++){
           if((Integer.valueOf(arrayResult.get(i).get(0).toString()).equals(q)) && (Integer.valueOf(arrayResult.get(i).get(1).toString()).equals(r)) ){
               return arrayResult.get(i);
           }        
       }
       return null;
   }
   
   public ArrayList<ArrayList<Integer>> getTablaMejor(){
       return tablaElmejor;
   }
   
   public ArrayList getElmejor(){
       return arrayResult.get(elMejor);
   }
   
   public int getIndexMejor(){
       return elMejor;
   }
   
   public String getMejorQ(){
       return arrayResult.get(elMejor).get(0).toString();
   }
    public String getMejorR(){
       return arrayResult.get(elMejor).get(1).toString();
   }
   public String getMejorCostoInventario(){
       return arrayResult.get(elMejor).get(2).toString();
   }
   public String getMejorCostoOrden(){
       return arrayResult.get(elMejor).get(3).toString();
   }
   public String getMejorCostoFaltante(){
       return arrayResult.get(elMejor).get(4).toString();
   }
   public String getMejorCosto(){
       return arrayResult.get(elMejor).get(5).toString();
   }
   
   public double getCostoInventario(){
      return cInv;
   }
   
   public double getCostoOrde(){
       return cOrden;
   }
   public double getCostoFcE(){   
       return FcE;
   }
   public double getCostoFsE(){
       return FsE;
   }
   public int getInvetarioIni(){
       return invIni;
   }
   
    
}
