import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateInfoFiles {
    // Arreglos con datos base para generar vendedores y productos
    private static final String[] NOMBRES = {"Carlos", "Maria", "Luis", "Ana", "Javier", "Sofía"};
    private static final String[] APELLIDOS = {"Gómez", "Pérez", "Rodríguez", "Fernández", "López"};
    private static final String[] TIPOS_DOCUMENTO = {"CC", "TI", "CE"};
    private static final String[] PRODUCTOS = {"Gasolina", "GNV", "Diésel", "Lubricante"};

    // Lista para guardar los IDs generados de los vendedores
    private static final List<Long> VENDEDOR_IDS = new ArrayList<>();

    public static void main(String[] args) {
        try {
            createSalesManInfoFile(5);     // Genera archivo de información de vendedores
            createProductsFile(5);         // Genera archivo de productos
            createSalesMenFiles(5);        // Genera archivos de ventas individuales
            System.out.println("✅ Archivos generados exitosamente.");
        } catch (Exception e) {
            System.err.println("❌ Error generando archivos: " + e.getMessage());
        }
    }

    // Método para generar archivo de vendedores
    public static void createSalesManInfoFile(int salesmanCount) throws IOException {
        try (FileWriter writer = new FileWriter("vendedores.txt")) {
            Random random = new Random();
            for (int i = 0; i < salesmanCount; i++) {
                String tipoDocumento = TIPOS_DOCUMENTO[random.nextInt(TIPOS_DOCUMENTO.length)];
                long numeroDocumento = 10000000 + random.nextInt(90000000); // Número aleatorio de 8 cifras
                String nombre = NOMBRES[random.nextInt(NOMBRES.length)];
                String apellido = APELLIDOS[random.nextInt(APELLIDOS.length)];

                VENDEDOR_IDS.add(numeroDocumento); // Guardar el ID para crear el archivo correspondiente

                writer.write(tipoDocumento + ";" + numeroDocumento + ";" + nombre + ";" + apellido + "\n");
            }
            writer.flush(); // Asegura que se escriban los datos
        }
    }

    // Método para generar archivo con productos y precios
    public static void createProductsFile(int productsCount) throws IOException {
        try (FileWriter writer = new FileWriter("productos.txt")) {
            Random random = new Random();
            for (int i = 0; i < productsCount; i++) {
                int idProducto = i + 1;
                String nombreProducto = PRODUCTOS[random.nextInt(PRODUCTOS.length)];
                double precio = 1000 + (5000 * random.nextDouble());

                writer.write(idProducto + ";" + nombreProducto + ";" + String.format("%.2f", precio) + "\n");
            }
            writer.flush();
        }
    }

    // Método para crear archivos de ventas individuales por cada vendedor
    public static void createSalesMenFiles(int salesmanCount) throws IOException {
        Random random = new Random();

        for (int i = 0; i < salesmanCount; i++) {
            long idVendedor = VENDEDOR_IDS.get(i); // Obtener ID de vendedor ya generado
            String fileName = "ventas_" + idVendedor + ".txt";

            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write("CC;" + idVendedor + "\n"); // Primera línea: tipo y número de documento

                int numVentas = random.nextInt(5) + 1; // Cantidad de ventas entre 1 y 5
                for (int j = 0; j < numVentas; j++) {
                    int idProducto = random.nextInt(4) + 1; // IDs de producto del 1 al 4
                    int cantidad = random.nextInt(10) + 1;  // Cantidad entre 1 y 10

                    writer.write(idProducto + ";" + cantidad + ";\n");
                }
                writer.flush();
            }
        }
    }
}
