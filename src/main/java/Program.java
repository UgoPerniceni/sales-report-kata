import model.ColumnInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Program {
    public static void main(String[] args){

        System.out.println("=== Sales Viewer ===");
        //extract the command name from the args
        String command = args.length > 0 ? args[0] : "unknown";
        String file = args.length >= 2 ? args[1] : "src/main/java/data/data.csv";
        //read content of our data file
        //[2012-10-30] rui : actually it only works with this file, maybe it's a good idea to pass file //name as parameter to this app later?
        String[] dataContentString = new String[0];
        try {
            dataContentString = Files.readAllLines(Paths.get(file)).toArray(new String[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //if command is print
        if (command.equals("print"))
        {
            //get the header line
            String line1 = dataContentString[0];
            //get other content lines
            // String[] otherLines = dataContentString[1..(dataContentString.length)];
            String[] otherLines = Arrays.copyOfRange(dataContentString, 1, dataContentString.length);

            List<ColumnInfo> columnInfos = new ArrayList<>();


            //build the header of the table with column names from our data file
            int i = 0;
            for (String columnName: line1.split(","))
            {
                columnInfos.add(new ColumnInfo(i++, columnName.length(), columnName));
            }


            String headerString = "";

            for (ColumnInfo columnInfo: columnInfos) {
                headerString += padLeft(columnInfo.getName(), 16) + " | ";
            }


            // var headerString  = String.join((  " | ", columnInfos.Select(x=>x.name).Select((val,ind) => val.PadLeft(16))));

            System.out.println("+" + "-".repeat(headerString.length() + 2) + "+");
            System.out.println("| " + headerString + " |");
            System.out.println("+" + "-".repeat(headerString.length() + 2)  + "+");

/*            Console.WriteLine("+" + new String('-', headerString.Length + 2) + "+");
            Console.WriteLine("| " + headerString + " |");
            Console.WriteLine("+" + new String('-', headerString.Length +2 ) + "+");*/

            //then add each line to the table
            for (String line: otherLines)
            {
                //extract columns from our csv line and add all these cells to the line
                var cells = line.split(",");
                // var tableLine  = String.Join(" | ", line.split(",").Select((val,ind) => val.PadLeft(16)));


                String tableLine = "";

                for (String sentence: line.split(",")) {
                    tableLine += padLeft(sentence, 16) + " | ";
                }

                System.out.println("| " + tableLine + " |");
            }

            System.out.println("+" + "-".repeat(headerString.length() + 2) + "+");
            // if command is report
        }
        else if (command.equals("report")) {
            //get all the lines without the header in the first line
            String[] otherLines = Arrays.copyOfRange(dataContentString, 1, dataContentString.length);
            //declare variables for our conters
            int number1 = 0, number2 = 0;
            double number4 = 0.0, number5 = 0.0, number3 = 0;
            HashSet<String> clients = new HashSet<String>();
            // DateTime last = DateTime.MinValue;
            LocalDate last = LocalDate.MIN;
            //do the counts for each line
            for (String line: otherLines) {
                //get the cell values for the line
                String[] cells = line.split(",");


                //!TODO remove extra space
                for (int i = 0; i < cells.length; i++) {
                    cells[i] = cells[i].trim();
                }

                number1++;//increment the total of sales
                //to count the number of clients, we put only distinct names in a hashset
                //then we'll count the number of entries
                if (!clients.contains(cells[1])) clients.add(cells[1]);

                number2 += Integer.parseInt(cells[2]);//we sum the total of items sold here
                number3 += Double.parseDouble(cells[3]);//we sum the amount of each sell
                //we compare the current cell date with the stored one and pick the higher
                // last = DateTime.Parse(cells[4]) > last ? DateTime.Parse(cells[4]) : last;
                // last = LocalDateTime.parse(LocalDateTime.parse(cells[4]).isAfter(last) ? LocalDateTime.parse(cells[4]) : last);


                String pattern = "yyyy-MM-dd";
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

                LocalDate localDateTime = LocalDate.from(formatter.parse(cells[4]));

                last = localDateTime.isAfter(last) ? localDateTime : last;
            }

            //we compute the average basket amount per sale
            number4 = Math.round((number3/number1) * 100.0) / 100.0;

            //we compute the average item price sold
            number5 = Math.round((number3/number2) * 100.0) / 100.0;

            System.out.println("+" + "-".repeat(45) + "+");

            System.out.println("| " + padLeft("Number of sales", 30) +  " | " + padLeft(""+number1, 10) + " |");
            System.out.println("| " + padLeft("Number of clients", 30) +  " | " + padLeft(""+clients.size(), 10) + " |");

            System.out.println("| " + padLeft("Total items sold", 30) +  " | " + padLeft(""+number2, 10) + " |");
            System.out.println("| " + padLeft("Total sales amount", 30) +  " | " + padLeft("" + String.format("%.2f", number3), 10) + " |");

            System.out.println("| " + padLeft("Average amount/sale", 30) +  " | " + padLeft(""+number4, 10) + " |");
            System.out.println("| " + padLeft("Average item price", 30) +  " | " + padLeft(""+number5, 10) + " |");

            System.out.println("+" + "-".repeat(45) + "+");

        }
        else {
            System.out.println("[ERR] your command is not valid ");
            System.out.println("Help: ");
            System.out.println("    - [print]  : show the content of our commerce records in data.csv");
            System.out.println("    - [report] : show a summary from data.csv records ");
        }
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }
}
