import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.OutputStream;
import java.io.InputStream;

public class ArduinoCommunication {

    public static void main(String[] args) {
        SerialPort serialPort = null;
        try {
            // Define the port name. Change it to the appropriate port on your system.
            String portName = "COM5"; // Windows example
            // String portName = "/dev/ttyUSB0"; // Linux example
            // String portName = "/dev/cu.usbmodem14101"; // macOS example

            // Open the port and set the parameters
            serialPort = openPort(portName);
            configurePort(serialPort);

            // Get the output stream from the port
            OutputStream outputStream = serialPort.getOutputStream();
            InputStream inputStream = serialPort.getInputStream();

            // Main loop
            while (true) {
                // Send "1" to the Arduino
                outputStream.write("1".getBytes());
                System.out.println("Sent data: 1");
                Thread.sleep(5000); // Wait for 5 seconds

                // Send "0" to the Arduino
                outputStream.write("0".getBytes());
                System.out.println("Sent data: 0");
                Thread.sleep(5000); // Wait for 5 seconds

                // Read data from Arduino if needed
                int available = inputStream.available();
                if (available > 0) {
                    byte[] buffer = new byte[available];
                    inputStream.read(buffer);
                    String data = new String(buffer);
                    System.out.println("Received data from Arduino: " + data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (serialPort != null) {
                serialPort.close();
                System.out.println("Serial port closed.");
            }
        }
    }

    private static SerialPort openPort(String portName) throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            System.err.println("Error: Port is currently in use");
            System.exit(1);
        }

        SerialPort serialPort = (SerialPort) portIdentifier.open("ArduinoCommunication", 2000);
        return serialPort;
    }

    private static void configurePort(SerialPort serialPort) throws Exception {
        serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
    }
}
