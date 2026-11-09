import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Metodos {
    public void conexio(FTPClient ftpClient) throws IOException {
        ftpClient.connect("ftpupload.net", 21);
    }

    public void desconexio(FTPClient ftpClient) throws IOException {
        ftpClient.disconnect();
    }

    //5
    public boolean login(FTPClient ftpClient) throws IOException {
        ftpClient.connect("ftpupload.net", 21);
        return ftpClient.login("if0_43334517", "ijX7SxyUtuAYX");
    }

    public boolean logout(FTPClient ftpClient) throws IOException {
        return ftpClient.logout();
    }

    public void mostrarCarpetas(FTPClient ftpClient) throws IOException {
        FTPFile[] fitxers = ftpClient.listFiles();

        for (FTPFile fitxer : fitxers) {
            String tipus = "";
            System.out.println(fitxer.getName() + tipus);
        }
    }

    public void mostrarHtdocs(FTPClient ftpClient) throws IOException {
        FTPFile[] fitxers = ftpClient.listFiles("/htdocs");
        for (FTPFile fitxer : fitxers) {
            String tipus = "";
            System.out.println(fitxer.getName() + tipus);
        }
    }

    public void llegirFixer(FTPClient ftpClient, String nomFitxer) {
        String rutaRemota = "/htdocs/" + nomFitxer;

        try (InputStream inputStream = ftpClient.retrieveFileStream(rutaRemota)) {

            if (inputStream == null) {
                throw new FileNotFoundException("El fitxer '" + nomFitxer + "' no existeix.");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String linia;
            System.out.println("Contingut :");
            while ((linia = reader.readLine()) != null) {
                System.out.println(linia);
            }

            ftpClient.completePendingCommand();

        } catch (Exception e) {
            System.out.println("S'ha produït un error en llegir.");
            registrarExcepcio("Error en llegir el fitxer " + rutaRemota, e, Level.SEVERE);
        }
    }

    public void afegirFitxer(FTPClient ftpClient, String rutaLocal, String nombreRemoto) {
        String rutaRemota = "/htdocs/" + nombreRemoto;

        try (InputStream inputStream = new FileInputStream(rutaLocal)) {

            boolean pujat = ftpClient.storeFile(rutaRemota, inputStream);

            if (pujat) {
                System.out.println("El fitxer s'ha pujat correctament ");
            } else {
                throw new IOException("El servidor ha rebutjat el fitxer.");
            }

        } catch (Exception e) {
            System.out.println("S'ha produit un error al pujar.");
            registrarExcepcio("Error en pujar el fitxer local " + rutaLocal, e, Level.SEVERE);
        }
    }

    public void crearFitxerLogs() throws Exception {
        Logger logger = Logger.getLogger("RegistreFTP");

        FileHandler fileHandler = new FileHandler("registre_ftp.log", true);

        fileHandler.setFormatter(new SimpleFormatter());

        logger.addHandler(fileHandler);

        logger.info("logs correcte.");

        System.out.println("Fitxer de logs creat.");
    }
    public void registrarExcepcio(String missatge, Exception e, Level nivell) {
        Logger logger = Logger.getLogger("RegistreFTP");
        logger.log(nivell, missatge, e);
    }

    public void mostrarInfoSistema() {

        Runtime runtime = Runtime.getRuntime();
        long memoriaTotal = runtime.totalMemory();
        long memoriaLliure = runtime.freeMemory();
        long memoriaUtilitzadaBytes = memoriaTotal - memoriaLliure;
        double memoriaUtilitzadaMB = (double) memoriaUtilitzadaBytes / (1024 * 1024);

        System.out.println("Memòria utilitzada: " + String.format("%.2f", memoriaUtilitzadaMB) + " MB");


        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        int numFils = threadMXBean.getThreadCount();
        System.out.println("Número de fils en execució: " + numFils);


        System.out.println("\n--- Informació dels fils ---");
        ThreadInfo[] filsInfo = threadMXBean.dumpAllThreads(false, false);
        for (ThreadInfo fil : filsInfo) {
            System.out.println("ID: " + fil.getThreadId() +
                    " | Nom: " + fil.getThreadName() +
                    " | Estat: " + fil.getThreadState());
        }
    }
}
