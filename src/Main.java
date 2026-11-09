import org.apache.commons.net.ftp.FTPClient;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        Metodos m = new Metodos();
        FTPClient ftpClient = new FTPClient();
        m.conexio(ftpClient);
        m.login(ftpClient);
        m.afegirFitxer(ftpClient, "src/Hola.txt", "Hola.txt");
        m.crearFitxerLogs();
        m.mostrarInfoSistema();
    }
}