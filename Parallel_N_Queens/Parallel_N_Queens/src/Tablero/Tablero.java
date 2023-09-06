package Tablero;

/**
 *
 * @author matm2
 */
public class Tablero{
    public int n;
    public Casilla[][] casillas;

    public Tablero(int n) {
        this.n = n;
        casillas = new Casilla[n][n];
        for(int i = 0; i<n; i++)
            for(int j = 0; j<n; j++)
                casillas[i][j] = new Casilla();   
    }
    
    public boolean agregar(int fila, int columna){
        //Primero checar que la casilla no esté bloqueada
        if(casillas[fila][columna].bloqueadoPor.isEmpty()){
            int i,j;
            //Ocupar casilla
            casillas[fila][columna].ocupadoPor = columna;
            
            //Marcar como bloqueadas las casillas debajo
            for(i = fila+1; i<n; i++)         
                casillas[i][columna].bloqueadoPor.add(columna);  
                        
            //Marcar como bloqueadas las casillas a la derecha
            for(i = columna+1; i<n; i++)
                casillas[fila][i].bloqueadoPor.add(columna);
            
            //Marcar como bloqueadas las casillas en la diagonal descendente
            i = fila+1;
            j = columna+1;
            while(i<n && j<n){
                casillas[i][j].bloqueadoPor.add(columna);
                i++;
                j++;   
            }
        
            //Marcar como bloqueadas las casillas en la diagonal ascendente
            i = fila-1;
            j = columna+1;
            while(i >= 0 && j < n){
                casillas[i][j].bloqueadoPor.add(columna);
                i--;
                j++;
            }            
            return true;
        }else{
            return false;
        }     
    }
    
    public boolean quitar(int fila, int columna){
        //Checar primero que sí esté ocupada
        if(casillas[fila][columna].ocupadoPor != -1){
            int i,j;
            //Marcar como desocupada
            casillas[fila][columna].ocupadoPor = -1;
            
            //El casteo a Integer es para quitar el objeto y no el índice
            //Marcar como desbloqueadas las casillas debajo 
            for(i = fila+1; i<n; i++)
                casillas[i][columna].bloqueadoPor.remove(Integer.valueOf(columna));
            
            //Marcar como desbloqueadas las casillas a la derecha
            for(i = columna+1; i<n; i++)
                casillas[fila][i].bloqueadoPor.remove(Integer.valueOf(columna));
            
            //Marcar como desbloqueadas las casillas en la diagonal descendente
            i = fila+1;
            j = columna+1;
            while(i<n && j<n){
                casillas[i][j].bloqueadoPor.remove(Integer.valueOf(columna));
                i++;
                j++;
            }        
        
            //Marcar como desbloqueadas las casillas en la diagonal ascendente
            i = fila-1;
            j = columna+1;
            while(i >= 0 && j < n){
                casillas[i][j].bloqueadoPor.remove(Integer.valueOf(columna));
                i--;
                j++;
            }
                
            return true;
        }else{
            return false;
        }
    }
}
