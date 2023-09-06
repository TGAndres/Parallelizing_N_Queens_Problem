package BackTrack;

import Tablero.Tablero;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 *
 * @author matm2
 */
public class BackTrack {  
    Tablero tablero;
    int n;
    
    //JFrame
    public JFrame frame = new JFrame("Proyecto Problema de las n-reinas");
        
    //JPanel (un panel por casilla)
    JPanel panel[][];
    
    //JLabel (un label por casilla)
    JLabel reinas[][];
    
    //Para adaptar el tamaño de las imagenes
    BufferedImage bi;

    public BackTrack(int n) {
        this.n = n;
        tablero = new Tablero(n);
        
        //Interfaz gráfica:
        panel = new JPanel[n][n];
        reinas = new JLabel[n][n];
        
        //El buffer recibe la imagen
        try{
            bi = ImageIO.read(new File("Images/reina.png"));
        }catch(IOException e){
    
        }
        
        //Crear los paneles
        for(int i=0; i<n; i++)
            for(int j = 0; j<n; j++){
                panel[i][j] = new JPanel();
                
                //Cada panel tiene la imagen de la reina
                reinas[i][j] = new JLabel(
                        //La imagen se adapta al tamaño del panel
                        new ImageIcon(bi.getScaledInstance(650/n,650/n,Image.SCALE_DEFAULT)),SwingConstants.CENTER);
                panel[i][j].add(reinas[i][j]);
                
                //Darle color a la casilla
                if((i+j)%2 == 0){
                    panel[i][j].setBackground(Color.white);
                }else{
                    panel[i][j].setBackground(Color.black);
                }
                
                //Definir el tamaño del panel de acuerdo a la cantidad de casillas
                panel[i][j].setSize(800/n,800/n);
                frame.getContentPane().add(panel[i][j]);
            }
        frame.setSize(800,800);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLayout(new GridLayout(n,n));
    }
    
    public static void main(String[] args) {
        int n = 20; //Tamaño del tablero
        
        BackTrack bt = new BackTrack(n);

        long ti = System.currentTimeMillis();
        bt.probarColumna(0);
        long tf = System.currentTimeMillis();
        System.out.println("Tardó "+(tf-ti)+" milisegundos en resolver el problema");
        
        bt.mostrarTablero();
    }
    
    //Este método es básicamente todo el algoritmo
    public boolean probarColumna(int columna){
        int i;
        //"columnaLlena" indica si todas las 
        //posiciones de la columna están bloqueadas
        boolean columnaLlena = true;
        //"bloqueoIndirecto" indica si la posición está indirectamente 
        //bloqueada por la reina en la columna anterior
        boolean bloqueoIndirecto;
        mostrarTablero();
        
        //Probar combinaciones con reina en cada una 
        //de las n casillas de la columna
        for(i = 0; i<n ; i++){
            //Solo pasa el if si la casilla estaba disponible
            if(tablero.agregar(i, columna)){
                //La columna no está completamente bloqueada puesto que
                //se encontró una posición que no lo está
                columnaLlena = false;
                //Recursividad hasta que sea la última columna
                if(columna<n-1){
                    //El booleano sirve para saber si se logró 
                    //llegar a una solución con la reina en esa posición
                    bloqueoIndirecto = probarColumna(columna+1);
                    if(bloqueoIndirecto){
                        //Si no se logró llegar a una solución, 
                        //se quita la reina
                        tablero.quitar(i, columna);
                        //La columna se vuelve a marcar como bloqueada puesto
                        //que esta posición esta bloqueada indirectamente
                        columnaLlena = true;
                    }
                }
            }
        }
        return columnaLlena;
    }
    
    public void mostrarTablero(){
        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++)
                //Checar si la casilla contiene una reina
                if(tablero.casillas[i][j].ocupadoPor != -1){
                    //La contiene, la reina en la casilla se hace visible
                    reinas[i][j].setVisible(true);
                }else{
                    //No la contiene, la reina en la casilla es invisible
                    reinas[i][j].setVisible(false);
                }
        }
    }
}
