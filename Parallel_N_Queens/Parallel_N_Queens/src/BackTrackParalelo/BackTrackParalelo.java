package BackTrackParalelo;

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
public class BackTrackParalelo{
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
    
    public BackTrackParalelo(int n){
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
        
        BackTrackParalelo btp = new BackTrackParalelo(n);
        btp.mostrarTablero();
        
        long ti = System.currentTimeMillis();
        btp.resolver();
        long tf = System.currentTimeMillis();
        System.out.println("Tardó "+(tf-ti)+" milisegundos en resolver el problema");
    }
    
    public void resolver(){
        int i;
        HiloCasilla[] hilos = new HiloCasilla[n];
        
        //Crear e iniciar los hilos
        for(i = 0; i<n; i++){
            hilos[i] = new HiloCasilla(tablero,i,0);
            hilos[i].start();   
        }
        
        //Buscar cual es el primer hilo en resolverlo
        while(true){
            i = (i+1)%n; //Recorra infinitamente el arreglo de hilos
            if(hilos[i].seLogro){
                tablero = hilos[i].tablero;
                break;
            }
        }
        
        //Terminar los demás hilos
        for(int j = 0; j<n; j++){
            if(hilos[j].isAlive()){
                hilos[j].interrupt();
            } 
        }
        
        //mostrar solución
        mostrarTablero();
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
