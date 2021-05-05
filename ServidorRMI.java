import java.rmi.Naming;

public class ServidorRMI
{
  public static void main(String[] args) throws Exception
  {
    String url = "rmi://localhost/prueba";// url del puerto por defecto
    ClaseRMI obj = new ClaseRMI();// instancia del objeto remoto

     Naming.rebind(url,obj);// registra la instancia en el rmiregistry
  }
}
