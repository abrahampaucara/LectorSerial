public class Principal {

    public static void main(String[] args) {

        String portName = args[0];
        String portBaud = args[1];
        String tabla = args[2];
        int portBauds = Integer.parseInt(portBaud);
        try {
            new PuertoController().connect(portName,portBauds,tabla);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
