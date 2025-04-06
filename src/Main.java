import java.io.*;
import java.util.*;


public class Main {

	public static void main(String[] args) {
		 try {
	            generateSalesReport();
	            generateProductReport();
	            System.out.println("Reportes generados exitosamente.");
	        } catch (Exception e) {
	            System.err.println("Error generando reportes: " + e.getMessage());
	        }
	    }

	    // Genera un reporte de ventas por vendedor (ordenado de mayor a menor)
	    public static void generateSalesReport() throws IOException {
	        File folder = new File(".");
	        File[] files = folder.listFiles((dir, name) -> name.startsWith("ventas_") && name.endsWith(".txt"));

	        Map<String, Double> salesData = new HashMap<>();

	        for (File file : files) {
	            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	                String vendedor = br.readLine().split(";")[1]; // Obtener ID del vendedor
	                double total = 0.0;
	                String line;
	                while ((line = br.readLine()) != null) {
	                    String[] data = line.split(";");
	                    int cantidad = Integer.parseInt(data[1]);
	                    total += cantidad * 5000; // Simulación del precio de productos
	                }
	                salesData.put(vendedor, salesData.getOrDefault(vendedor, 0.0) + total);
	            }
	        }

	        // Ordenar vendedores por ventas de mayor a menor
	        List<Map.Entry<String, Double>> sortedList = new ArrayList<>(salesData.entrySet());
	        sortedList.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

	        try (FileWriter writer = new FileWriter("reporte_vendedores.csv")) {
	            for (Map.Entry<String, Double> entry : sortedList) {
	                writer.write(entry.getKey() + ";" + entry.getValue() + "\n");
	            }
	        }
	    }

	    // Genera un reporte de productos vendidos por cantidad (ordenado de mayor a menor)
	    public static void generateProductReport() throws IOException {
	        File folder = new File(".");
	        File[] files = folder.listFiles((dir, name) -> name.startsWith("ventas_") && name.endsWith(".txt"));

	        Map<String, Integer> productData = new HashMap<>();

	        for (File file : files) {
	            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	                br.readLine(); // Saltar la primera línea (ID del vendedor)
	                String line;
	                while ((line = br.readLine()) != null) {
	                    String[] data = line.split(";");
	                    String producto = "Producto" + data[0]; // Simulación del nombre de producto
	                    int cantidad = Integer.parseInt(data[1]);
	                    productData.put(producto, productData.getOrDefault(producto, 0) + cantidad);
	                }
	            }
	        }

	        // Ordenar productos por cantidad vendida de mayor a menor
	        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(productData.entrySet());
	        sortedList.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

	        try (FileWriter writer = new FileWriter("reporte_productos.csv")) {
	            for (Map.Entry<String, Integer> entry : sortedList) {
	                writer.write(entry.getKey() + ";" + entry.getValue() + "\n");
	            }
	        }
	    }
	
	}


