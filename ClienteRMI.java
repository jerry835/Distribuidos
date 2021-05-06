import java.rmi.Naming;
/**El cliente ClienteMatricesRMI obtiene una referencia al objeto remoto 
 * utilizando el método lookup(), esta referencia es utilizada para 
 * invocar los métodos remotos. */
public class ClienteRMI {
    // Tamaño de la matriz
    static final int N = 8;
    // Declaración de matrices originales
    static float[][] A = new float[N][N];
    static float[][] B = new float[N][N];
    static float[][] C = new float[N][N];

    public static void main(String[] args) throws Exception {
        
        // en este caso el objeto remoto se llama "matrices", notar que se utiliza el puerto default 1099
        // se usa la dirección ip privada de cada nodo en el que se ejecuta un servidor en lugar de  "localhost"
        // en el primer caso, si se utiliza localhost puesto que será el nodo 0
        String url1 = "rmi://10.1.0.5/mmatrices";
        String url2 = "rmi://10.1.0.6/mmatrices";
        
        // Inicializando las matrices
        for (int i = 0; i < N; i++){
            for (int j = 0; j < N; j++){
                A[i][j] = 2 * i - j;
                B[i][j] = 2 * i + j;               
            }
        } 
        
        // transpone la matriz B, la matriz traspuesta queda en B
     
	for (int i = 0; i < N; i++){
      		for (int j = 0; j < i; j++){
        		float x = B[i][j];
        		B[i][j] = B[j][i];
        		B[j][i] = x;
      		}
	}
        
        // obtiene una referencia que "apunta" al objeto remoto asociado a la URL
        InterfaceRMI r1 = (InterfaceRMI)Naming.lookup(url1);
        InterfaceRMI r2 = (InterfaceRMI)Naming.lookup(url2);

        float[][]A1 = separa_matriz(A,0);
        float[][]A2 = separa_matriz(A, N/2);
        float[][]B1 = separa_matriz(B, 0); 
        float[][]B2 = separa_matriz(B, N/2);

        float[][]C1 = r1.multiplica_matrices(A1, B1,N);
        float[][]C2 = r1.multiplica_matrices(A1, B2,N);
        float[][]C3 = r2.multiplica_matrices(A2, B1,N);
        float[][]C4 = r2.multiplica_matrices(A2, B2,N);

        acomoda_matriz(C, C1, 0, 0);
        acomoda_matriz(C, C2, 0, N/2);
        acomoda_matriz(C, C3, N/2, 0);
        acomoda_matriz(C, C4, N/2, N/2);
        
        if (N == 8){

            imprimir_matriz(A, N, N, "A");
            imprimir_matriz(B, N, N, "B transpuesta");
            imprimir_matriz(C, N, N, "C");
            System.out.println("checksum = " + checksum(C));

        } else {
            System.out.println("checksum = " + checksum(C));
        }
    // fin main
    }    
    /**Método acomoda_matriz
     * Permite construir la matriz C a partir de las matrices C1, C2, C3 y C4.
     * @param C
     * @param A
     * @param renglon
     * @param columna
     */
	static void acomoda_matriz(float[][] C,float[][] A,int renglon,int columna){
		for (int i = 0; i < N/2; i++)
    		for (int j = 0; j < N/2; j++)
      		C[i + renglon][j + columna] = A[i][j];
	}
    /**
     * Imprime una matriz
     * @param m
     * @param filas
     * @param columnas
     * @param s
     * @throws RemoteException
     */
    static void imprimir_matriz(float[][] m, int filas, int columnas, String s) {
       
        System.out.println("\nImprimiendo " + s);
        for (int i = 0; i< filas; i++){
            for (int j = 0; j < columnas; j++){
                System.out.print(m[i][j] + " ");
            }
            System.out.println("");
        }
    // fin método imprimir matriz
    }

    /**
     * Método que calcula el checksum
     * @param m
     * @return long checksum
     */

    static double checksum(float[][] m) {
        
        double s = 0;
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                s += m[i][j];
        return s;
    // fin método checksum    
    }
    /**
     * Método que divide una matriz A dado un renglón inicial.
     * @param A
     * @param inicio
     * @return An[N/2][N]
     */
	static float[][] separa_matriz(float[][] A,int inicio){
  		float[][] M = new float[N/2][N];
  		for (int i = 0; i < N/2; i++)
    		for (int j = 0; j < N; j++)
      		M[i][j] = A[i + inicio][j];
  		return M;
	}
// fin clase ClienteMatricesRMI    
}
