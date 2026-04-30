import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public static List<Transaction> getProducts() {
        List<Transaction> products = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader("src/main/resources/transactions.csv");
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                LocalDate date = LocalDate.parse(parts[0]);
                LocalTime time = LocalTime.parse(parts[1]);
                String description = parts[2];
                String vendor = parts[3];
                double amount = Float.parseFloat(parts[4]);

                Transaction product = new Transaction(date, time, description, vendor, amount);
                products.add(product);
            }

            reader.close();

        } catch (IOException e) {
            System.out.println("There was a problem reading the inventory file.");
        } catch (Exception ex) {
            System.out.println("Something went from with the file.");
        }

        return products;
    }
    public static void writeProduct(Transaction product){
        try{
            File file = new File("src/main/resources/transactions.csv");
            FileWriter fileWriter = new FileWriter(file, true);
            if (file.length() > 0) {
                fileWriter.write(System.lineSeparator());
            }
            fileWriter.write(String.format("%tF|%tT|%s|%s|%.2f", product.getDate(), product.getTime(),product.getDescription(), product.getVendor(), product.getAmount()));
            fileWriter.close();
        }
        catch(IOException ex){
            System.out.println("Error writing to file.");
        }
    }

}
