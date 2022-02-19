import model.ColumnInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Runner {
    public void execute(String[] args) {
        
        System.out.println("=== Sales Viewer ===");
        //extract the command name from the args

        String command = args.length > 0 ? args[0] : "unknown";
        String file = args.length >= 2 ? args[1] : "src/main/java/data/data.csv";
        //read content of our data file

        String[] dataContentString = new String[0];
        try {
            dataContentString = Files.readAllLines(Paths.get(file)).toArray(new String[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (command.equals("print"))
        {
            //get the header line
            String headerLine = dataContentString[0];

            //get other content lines
            String[] tableLines = Arrays.copyOfRange(dataContentString, 1, dataContentString.length);

            //build the header of the table with column names from our data file
            List<ColumnInfo> columnInfos = new ArrayList<>();
            updateColumnInfos(headerLine, columnInfos);

            String headersString = parseHeaders(columnInfos);
            printHeaders(parseHeaders(columnInfos));

            printTableRows(tableLines);

            drawLine(headersString);
        }
        else if (command.equals("report")) {
            //get all the lines without the header in the first line
            String[] tableLines = Arrays.copyOfRange(dataContentString, 1, dataContentString.length);
            //declare variables for our conters
            int numberOfSales = 0, totalItemsSold = 0;
            double averageAmountSale, averageItemPrice, totalSalesAmount = 0;
            HashSet<String> clients = new HashSet<>();

            LocalDate last = LocalDate.MIN;
            //do the counts for each line
            for (String line: tableLines) {
                //get the cell values for the line
                String[] cells = line.split(",");

                trimCells(cells);

                numberOfSales++;//increment the total of sales
                //to count the number of clients, we put only distinct names in a hashset
                //then we'll count the number of entries
                clients.add(cells[1]);

                totalItemsSold += Integer.parseInt(cells[2]);//we sum the total of items sold here
                totalSalesAmount += Double.parseDouble(cells[3]);//we sum the amount of each sell
                //we compare the current cell date with the stored one and pick the higher

                final String pattern = "yyyy-MM-dd";
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                LocalDate localDateOfBuy = LocalDate.from(formatter.parse(cells[4]));

                last = localDateOfBuy.isAfter(last) ? localDateOfBuy : last;
            }

            //we compute the average basket amount per sale
            averageAmountSale = Math.round((totalSalesAmount/numberOfSales) * 100.0) / 100.0;

            //we compute the average item price sold
            averageItemPrice = Math.round((totalSalesAmount/totalItemsSold) * 100.0) / 100.0;

            System.out.print(
                    "+" + "-".repeat(45) + "+\n" +
                    "| " + padLeft("Number of sales", 30) +  " | " + padLeft(""+numberOfSales, 10) + " |\n" +
                    "| " + padLeft("Number of clients", 30) +  " | " + padLeft(""+clients.size(), 10) + " |\n" +
                    "| " + padLeft("Total items sold", 30) +  " | " + padLeft(""+totalItemsSold, 10) + " |\n" +
                    "| " + padLeft("Total sales amount", 30) +  " | " + padLeft("$" + String.format("%.2f", totalSalesAmount), 10) + " |\n" +
                    "| " + padLeft("Average amount/sale", 30) +  " | " + padLeft("$"+averageAmountSale, 10) + " |\n" +
                    "| " + padLeft("Average item price", 30) +  " | " + padLeft("$"+averageItemPrice, 10) + " |\n" +
                    "+" + "-".repeat(45) + "+\n"
            );
        }
        else {
            System.out.print(
                    "[ERR] your command is not valid \n" +
                    "Help: \n" +
                    "    - [print]  : show the content of our commerce records in data.csv\n" +
                    "    - [report] : show a summary from data.csv records \n"
            );
        }

    }

    private void printTableRows(String[] tableLines) {
        for (String line: tableLines) {
            String tableLine = parseTableRow(line);
            System.out.println("| " + tableLine + " |");
        }
    }

    private String parseTableRow(String line) {
        String result = "";

        for (String sentence: line.split(",")) {
            result = result.concat(padLeft(sentence, 16) + " | ");
        }
        return result;
    }

    private static void printHeaders(String headersString) {
        drawLine(headersString);
        System.out.println("| " + headersString + " |");
        drawLine(headersString);
    }

    private static void updateColumnInfos(String headerLine, List<ColumnInfo> columnInfos) {
        int i = 0;
        for (String columnName: headerLine.split(",")) {
            columnInfos.add(new ColumnInfo(i++, columnName.length(), columnName));
        }
    }

    private static String parseHeaders(List<ColumnInfo> columnInfos) {
        String result = "";
        for (ColumnInfo columnInfo: columnInfos) {
            result = result.concat(padLeft(columnInfo.getName(), 16) + " | ");
        }
        return result;
    }

    public static void trimCells(String[] cells) {
        for (int i = 0; i < cells.length; i++) {
            cells[i] = cells[i].trim();
        }
    }

    public static String padLeft(String input, int offset) {
        return String.format("%" + offset + "s", input);
    }

    public static void drawLine(String input) {
        System.out.println("+" + "-".repeat(input.length() + 2) + "+");
    }
}
