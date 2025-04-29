import java.io.*;
import java.util.*;

public class Main {

    // Mapas para almacenar información de vendedores, productos y ventas
    static Map<Long, String> sellers = new HashMap<>();
    static Map<Integer, String> products = new HashMap<>();
    static Map<Integer, Double> prices = new HashMap<>();
    static Map<Long, Map<String, Integer>> salesBySeller = new HashMap<>();
    static Map<String, Integer> totalProductSales = new HashMap<>();

    public static void main(String[] args) {
        try {
            readSellers("vendedores.txt");         // Leer archivo de vendedores
            readProducts("productos.txt");         // Leer archivo de productos
            readSalesFiles();                      // Leer archivos de ventas
            generateSellerReport("reporte_vendedores.txt"); // Generar reporte por vendedor
            generateProductReport("reporte_productos.txt"); // Generar reporte por producto

            System.out.println("✅ Reportes Generados Correctamente.");
        } catch (Exception e) {
            System.err.println("❌ Error al procesar archivos: " + e.getMessage());
        }
    }

    // Leer información de los vendedores
    public static void readSellers(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");
            long docNumber = Long.parseLong(parts[1]);
            String fullName = parts[2] + " " + parts[3];
            sellers.put(docNumber, fullName); // Asociar número de documento con nombre completo
        }
        reader.close();
    }

    // Leer productos y sus precios
    public static void readProducts(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");
            int id = Integer.parseInt(parts[0]);
            String productName = parts[1];
            double price = Double.parseDouble(parts[2].replace(",", ".")); // Reemplazar coma por punto
            products.put(id, productName);
            prices.put(id, price);
        }
        reader.close();
    }

    // Leer archivos individuales de ventas
    public static void readSalesFiles() throws IOException {
        File currentDir = new File(".");
        File[] files = currentDir.listFiles((dir, name) -> name.startsWith("ventas_") && name.endsWith(".txt"));
        if (files == null) return;

        for (File file : files) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String header = reader.readLine(); // Primera línea: ID del vendedor
            long sellerId = Long.parseLong(header.split(";")[1]);

            Map<String, Integer> sellerSales = salesBySeller.getOrDefault(sellerId, new HashMap<>());

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(";");
                int productId = Integer.parseInt(parts[0]);
                int quantity = Integer.parseInt(parts[1]);

                String productName = products.get(productId);
                sellerSales.put(productName, sellerSales.getOrDefault(productName, 0) + quantity);
                totalProductSales.put(productName, totalProductSales.getOrDefault(productName, 0) + quantity);
            }

            salesBySeller.put(sellerId, sellerSales);
            reader.close();
        }
    }

    // Generar reporte de ventas por vendedor
    public static void generateSellerReport(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        for (Map.Entry<Long, Map<String, Integer>> entry : salesBySeller.entrySet()) {
            long sellerId = entry.getKey();
            String sellerName = sellers.getOrDefault(sellerId, "Desconocido");

            writer.write("Vendedor: " + sellerName + " (" + sellerId + ")\n");

            double totalSales = 0;
            for (Map.Entry<String, Integer> sale : entry.getValue().entrySet()) {
                String productName = sale.getKey();
                int quantity = sale.getValue();
                double price = prices.get(getProductId(productName));
                double subtotal = quantity * price;
                totalSales += subtotal;

                writer.write(" - " + productName + ": " + quantity + " unidades ($" + String.format("%.2f", subtotal) + ")\n");
            }

            writer.write(" Total ventas: $" + String.format("%.2f", totalSales) + "\n\n");
        }
        writer.close();
    }

    // Generar reporte de ventas por producto
    public static void generateProductReport(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        for (Map.Entry<String, Integer> entry : totalProductSales.entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue();
            double price = prices.get(getProductId(productName));
            double total = quantity * price;

            writer.write(productName + " - Total vendido: " + quantity + " unidades ($" + String.format("%.2f", total) + ")\n");
        }
        writer.close();
    }

    // Obtener ID de producto por nombre
    public static int getProductId(String productName) {
        for (Map.Entry<Integer, String> entry : products.entrySet()) {
            if (entry.getValue().equals(productName)) {
                return entry.getKey();
            }
        }
        return -1; // Si no se encuentra
    }
}
