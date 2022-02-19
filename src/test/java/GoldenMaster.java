import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class GoldenMaster {
    @Test
    public void TestPrint() {
        String resultExpected =
                "=== Sales Viewer ===\n" +
                        "+-------------------------------------------------------------------------------------------------+\n" +
                        "|          orderid |         userName |    numberOfItems |    totalOfBasket |        dateOfBuy |  |\n" +
                        "+-------------------------------------------------------------------------------------------------+\n" +
                        "|                1 |            peter |                3 |           123.00 |       2021-11-30 |  |\n" +
                        "|                2 |             paul |                1 |           433.50 |       2021-12-11 |  |\n" +
                        "|                3 |            peter |                1 |           329.99 |       2021-12-18 |  |\n" +
                        "|                4 |             john |                5 |           467.35 |       2021-12-30 |  |\n" +
                        "|                5 |             john |                1 |            88.00 |       2022-01-04 |  |\n" +
                        "+-------------------------------------------------------------------------------------------------+\n";

        testOutput("print", resultExpected);
    }

    @Test
    public void TestReport() {
        String resultExpected = "=== Sales Viewer ===\n" +
                "+---------------------------------------------+\n" +
                "|                Number of sales |          5 |\n" +
                "|              Number of clients |          3 |\n" +
                "|               Total items sold |         11 |\n" +
                "|             Total sales amount |   $1441,84 |\n" +
                "|            Average amount/sale |    $288.37 |\n" +
                "|             Average item price |    $131.08 |\n" +
                "+---------------------------------------------+\n" +
                "\n";

        testOutput("report", resultExpected);
    }

    @Test
    public void TestError() {
        String resultExpected = "=== Sales Viewer ===\n" +
                "[ERR] your command is not valid \n" +
                "Help: \n" +
                "    - [print]  : show the content of our commerce records in data.csv\n" +
                "    - [report] : show a summary from data.csv records ";

        testOutput("", resultExpected);
    }

    public void testOutput(String arg, String resultExpected) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;
        System.setOut(ps);

        String[] args = {arg};
        new Runner().execute(args);

        System.out.flush();
        System.setOut(old);

        System.out.println(baos);

        assert(resultExpected.trim().equals(baos.toString().trim()));
    }

}
