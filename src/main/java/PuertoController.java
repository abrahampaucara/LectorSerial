import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import gnu.io.CommPort;
import gnu.io.SerialPort;
import gnu.io.CommPortIdentifier;

public class PuertoController {
    void connect(final String portName, final int portBauds, final String tabla) throws Exception {
        final CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            System.out.println("Error: El puerto se encunetra en uso.");
        }
        else {
            final CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
            if (commPort instanceof SerialPort) {
                System.out.println("Se conecto al puerto SERIAL");
                final SerialPort serialPort = (SerialPort)commPort;
                serialPort.setSerialPortParams(portBauds, 8, 1, 0);
                final InputStream in = serialPort.getInputStream();
                final OutputStream out = serialPort.getOutputStream();
                new Thread(new Reader.SerialReader(in, tabla)).start();
            }
            else {
                System.out.println("Error: Solo puertos seriales son aceptados.");
            }
        }
    }

    /*public static class SerialReader implements Runnable {
        InputStream in;
        String tabla;

        public SerialReader(final InputStream in, final String tabla) {
            this.in = in;
            this.tabla = tabla;
        }

        @Override
        public void run() {
            final byte[] buffer = new byte[2048];
            int len = -1;

            try {
                while ((len = this.in.read(buffer)) > -1) {
                    final String datos = new String(buffer, 0, len);
                    System.out.print(datos);

                    try {
                        Thread.sleep(100L);
                        String myDriver = "com.mysql.jdbc.Driver";
                        String myUrl = "jdbc:mysql://localhost/bascula";
                        Connection conn = null;
                        try {
                            conn = DriverManager.getConnection(myUrl, "root", "");
                            String query = " insert into "+this.tabla+" (peso)"
                                    + " values (?)";
                            PreparedStatement preparedStmt = conn.prepareStatement(query);
                            preparedStmt.setString (1, datos.trim());
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
    }*/

    /*public static void main(final String[] args) {

        String portName = args[0];
        String portBaud = args[1];
        String tabla = args[2];
        int portBauds = Integer.parseInt(portBaud);
        try {
            new PuertoController().connect(portName,portBauds,tabla);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}


