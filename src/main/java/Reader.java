import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Reader {

    public static class SerialReader implements Runnable {
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
    }

}
