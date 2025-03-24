import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.nio.file.Paths;
import java.util.Random;

public class GenerateInfoFiles {
    // Se crean los arreglos de tipo String.
    private static final String[] NOMBRES = {"Carlos", "Maria", "Luis", "Ana", "Javier", "Sofía"};
    private static final String[] APELLIDOS = {"Gómez", "Pérez", "Rodríguez", "Fernández", "López"};
    private static final String[] TIPOS_DOCUMENTO = {"CC", "TI", "CE"};
    private static final String[] PRODUCTOS = {"Gasolina", "GNV", "Diésel", "Lubricante"};
    
    public static void main(String[] args) {
        try {
            // Obtener la ruta absoluta del directorio del proyecto
            String projectDir = System.getProperty("user.dir");  // Obtener el directorio de trabajo actual
            String rutaDatos = projectDir + File.separator + "datos";  // Carpeta 'datos' en la raíz del proyecto

            // Crear la carpeta 'datos' si no existe
            File directory = new File(rutaDatos);
            if (!directory.exists()) {
                boolean created = directory.mkdir();  // Intentar crear la carpeta
                if (created) {
                    System.out.println("Carpeta 'datos' creada correctamente en: " + rutaDatos);
                } else {
                    System.err.println("No se pudo crear la carpeta 'datos'.");
                }
            } else {
                System.out.println("La carpeta 'datos' ya existe en: " + rutaDatos);
            }

            // Generar los archivos CSV
            createSalesManInfoFile(rutaDatos, 5);
            createProductsFile(rutaDatos, 5);
            createSalesMenFiles(rutaDatos, 5);
            System.out.println("✅ Archivos generados exitosamente.");
        } catch (Exception e) {
            System.err.println("❌ Error generando archivos: " + e.getMessage());
        }
    }

    // Método para generar un archivo con la información de los vendedores
    public static void createSalesManInfoFile(String path, int salesmanCount) throws IOException {
        try (FileWriter writer = new FileWriter(path + File.separator + "vendedores.csv")) {
            Random random = new Random();
            for (int i = 0; i < salesmanCount; i++) {
                String tipoDocumento = TIPOS_DOCUMENTO[random.nextInt(TIPOS_DOCUMENTO.length)];
                long numeroDocumento = 10000000 + random.nextInt(90000000);
                String nombre = NOMBRES[random.nextInt(NOMBRES.length)];
                String apellido = APELLIDOS[random.nextInt(APELLIDOS.length)];
                
                writer.write(tipoDocumento + ";" + numeroDocumento + ";" + nombre + ";" + apellido + "\n");
            }
            writer.flush(); // Asegura que los datos se escriban
        }
    }

    // Método para generar un archivo con información de productos
    public static void createProductsFile(String path, int productsCount) throws IOException {
        try (FileWriter writer = new FileWriter(path + File.separator + "productos.csv")) {
            Random random = new Random();
            for (int i = 0; i < productsCount; i++) {
                int idProducto = i + 1;
                String nombreProducto = PRODUCTOS[random.nextInt(PRODUCTOS.length)];
                double precio = 1000 + (5000 * random.nextDouble());

                writer.write(idProducto + ";" + nombreProducto + ";" + String.format("%.2f", precio) + "\n");
            }
            writer.flush(); // Asegura que los datos se escriban
        }
    }

    // Método para generar archivos de ventas por vendedor
    public static void createSalesMenFiles(String path, int salesmanCount) throws IOException {
        Random random = new Random();

        for (int i = 0; i < salesmanCount; i++) {
            long idVendedor = 10000000 + random.nextInt(90000000);
            String fileName = path + File.separator + "ventas_" + idVendedor + ".csv";

            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write("CC;" + idVendedor + "\n"); // Primera línea: ID del vendedor
                
                int numVentas = random.nextInt(5) + 1; // Entre 1 y 5 productos vendidos
                for (int j = 0; j < numVentas; j++) {
                    int idProducto = random.nextInt(4) + 1; // IDs del 1 al 4
                    int cantidad = random.nextInt(10) + 1; // Cantidad entre 1 y 10
                    
                    writer.write(idProducto + ";" + cantidad + ";\n");
                }
                writer.flush(); // Asegura que los datos se escriban
            }
        }
    }
}
