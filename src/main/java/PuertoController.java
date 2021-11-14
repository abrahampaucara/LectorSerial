import java.sql.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import gnu.io.CommPort;
import gnu.io.SerialPort;
import gnu.io.CommPortIdentifier;

public class PuertoController {
    void connect(final String portName) throws Exception {
        final CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            System.out.println("Error: El puerto se encunetra en uso.");
        }
        else {
            final CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
            if (commPort instanceof SerialPort) {
                System.out.println("Se conecto al puerto SERIAL");
                final SerialPort serialPort = (SerialPort)commPort;
                serialPort.setSerialPortParams(9600, 8, 1, 0);
                final InputStream in = serialPort.getInputStream();
                final OutputStream out = serialPort.getOutputStream();
                new Thread(new SerialReader(in)).start();
                //new Thread(new SerialWriter(out)).start();
            }
            else {
                System.out.println("Error: Solo puertos seriales son aceptados.");
            }
        }
    }

    public static void main(final String[] args) throws IOException {
        String serialPort = "COM1";
        try {
            System.out.println ("Por favor escoga el puerto Serial que al que se conectara:");
            System.out.println ("Si es COM1 teclee: 1");
            System.out.println ("Si es COM2 teclee: 2");
            System.out.println ("Si es COM3 teclee: 3");
            System.out.println ("Si es COM4 teclee: 4");
            System.out.println ("Si es COM5 teclee: 5");
            System.out.println ("Si es COM6 teclee: 6");
            System.out.println ("Si es COM6 teclee: 7");
            System.out.println ("Si es COM6 teclee: 8");
            System.out.println ("Si es COM6 teclee: 9");
            System.out.println ("Si es COM6 teclee: 10");
            String entradaTeclado = "";
            Scanner entradaEscaner = new Scanner (System.in); //Creación de un objeto Scanner
            entradaTeclado = entradaEscaner.nextLine (); //Invocamos un método sobre un objeto Scanner
            if(entradaTeclado.equals("1")){
                serialPort = "COM1";
            }
            if(entradaTeclado.equals("2")){
                serialPort = "COM2";
            }
            if(entradaTeclado.equals("3")){
                serialPort = "COM3";
            }
            if(entradaTeclado.equals("4")){
                serialPort = "COM4";
            }
            if(entradaTeclado.equals("5")){
                serialPort = "COM5";
            }
            if(entradaTeclado.equals("6")){
                serialPort = "COM6";
            }
            if(entradaTeclado.equals("7")){
                serialPort = "COM7";
            }
            if(entradaTeclado.equals("8")){
                serialPort = "COM8";
            }
            if(entradaTeclado.equals("9")){
                serialPort = "COM9";
            }
            if(entradaTeclado.equals("10")){
                serialPort = "COM10";
            }
            System.out.println ("El puerto elegido es: \"" + serialPort +"\"");
            new PuertoController().connect(serialPort);
            //new PuertoController().connect("COM3");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class SerialReader implements Runnable
    {
        InputStream in;


        public SerialReader(final InputStream in) {
            this.in = in;
        }

        @Override
        public void run() {
            final byte[] buffer = new byte[2048];
            int len = -1;

            try {
                while ((len = this.in.read(buffer)) > -1) {
                    final String datos = new String(buffer, 0, len);
                    System.out.print(datos);


                    final FileWriter fw = new FileWriter(new File("C:\\Users\\abrah\\Documents\\imd\\datos\\fichero4.txt"), true);
                    fw.write(datos);
                    fw.close();
                    try {
                        Thread.sleep(100L);
                        String myDriver = "com.mysql.jdbc.Driver";
                        String myUrl = "jdbc:mysql://localhost/bascula";
                        Connection conn = null;
                        try {
                            conn = DriverManager.getConnection(myUrl, "root", "");
                            String query = " insert into registros (nombre, peso, pesos)"
                                    + " values (?, ?,?)";
                            PreparedStatement preparedStmt = conn.prepareStatement(query);
                            preparedStmt.setString (1, "Test");
                            preparedStmt.setString (2, "1");
                            preparedStmt.setString (3, datos);
                            preparedStmt.execute();
                            conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                    catch (InterruptedException ex) {
                        Logger.getLogger(PuertoController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class SerialWriter implements Runnable
    {
        OutputStream out;

        public SerialWriter(final OutputStream out) {
            this.out = out;
        }

        @Override
        public void run() {
            try {
                int c = 0;
                while ((c = System.in.read()) > -1) {
                    this.out.write(c);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


