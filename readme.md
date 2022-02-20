# Sales report kata

## Context

This existing app reads a csv file, do some aggregations and calculations and then prints a clean and nice report about the results of the sales. The apps works great and has lots of comments explaining the details of the code! 

so, what's the problem ?

## What's my job ?

_(disclaimer : obviously if you have a simple app like that, that fits your needs it's probably good enough, but if you want to add new functionalities and make it richer over time, it won't)_

- discuss the app, what's good & what's bad ?
- discuss about the building blocks and hidden structure
- how should you test it ?
- start refactoring & add tests

## What have we done ?

0. Translate code from C# to Java
1. Create a GoldenMaster in order to test the functional code
2. Rename the variables (some of them was already well named)
3. Extract some methods to avoid nesting levels in IF or FOR blocks
4. Add some tests for these new methods
5. Extract the core code from the main method

## What haven't we done that we should have ?

1. Test all methods
2. Extract some more methods
3. Implements design patterns
4. 

## Code

Program.cs:
```c#
public static class Program  
{  
 	public static void Main(string[] args)  
 	{ //add a title to our app    
		Console.WriteLine("=== Sales Viewer ===");  
 //extract the command name from the args    
		string command = args.Length > 0 ? args[0] : "unknown";    
		string file = args.Length >= 2 ? args[1] : "./data.csv";  
		 //read content of our data file    
			  //[2012-10-30] rui : actually it only works with this file, maybe it's a good idea to pass file //name as parameter to this app later?    
		string[] dataContentString = File.ReadAllLines(file);    
		//if command is print    
		if (command == "print")    
      	{    
			//get the header line    
			string line1 = dataContentString[0];    
			//get other content lines    
			string[] otherLines = dataContentString[1..(dataContentString.Length)];  
			var columnInfos = new List<(int index, int size, string name)>();  
			 //build the header of the table with column names from our data file    
			int i = 0;  
			foreach (var columName in line1.Split(','))  
			{ 
				columnInfos.Add((i++, columName.Length, columName));  
			}  
			var headerString  = String.Join(  " | ",   
					columnInfos.Select(x=>x.name).Select(  
			 		(val,ind) => val.PadLeft(16)));  
			Console.WriteLine("+" + new String('-', headerString.Length + 2) + "+");  
			Console.WriteLine("| " + headerString + " |");  
			Console.WriteLine("+" + new String('-', headerString.Length +2 ) + "+");  

			 //then add each line to the table    
			foreach (string line in otherLines)    
			{   
				//extract columns from our csv line and add all these cells to the line    
				var cells = line.Split(',');  
			 	var tableLine  = String.Join(" | ",   
						line.Split(',').Select(  
			 				(val,ind) => val.PadLeft(16)));  
			 	Console.WriteLine($"| {tableLine} |");  
			}
			
			Console.WriteLine("+" + new String('-', headerString.Length+2) + "+");  
			// if command is report  
		}   
		else if (command == "report")    
		{    
			//get all the lines without the header in the first line    
			string[] otherLines = dataContentString[1..(dataContentString.Length)];    
			//declare variables for our conters    
			int number1 = 0, number2 = 0;    
			double number4 = 0.0, number5 = 0.0, number3 = 0;    
			HashSet<string> clients = new HashSet<string>();    
			DateTime last = DateTime.MinValue;    
			//do the counts for each line    
			foreach (var line in otherLines)    
			{ 
				//get the cell values for the line    
				var cells = line.Split(',');    
				number1++;//increment the total of sales    
				//to count the number of clients, we put only distinct names in a hashset 
				//then we'll count the number of entries 
				if (!clients.Contains(cells[1])) clients.Add(cells[1]);    
				number2 += int.Parse(cells[2]);//we sum the total of items sold here    
				number3 += double.Parse(cells[3]);//we sum the amount of each sell    
				//we compare the current cell date with the stored one and pick the higher
				last = DateTime.Parse(cells[4]) > last ? DateTime.Parse(cells[4]) : last;    
			}   
			//we compute the average basket amount per sale    
			number4 = Math.Round(number3 / number1,2);    
			//we compute the average item price sold    
			number5 = Math.Round(number3 / number2,2);    
			Console.WriteLine($"+{new String('-',45)}+");  
			Console.WriteLine($"| {" Number of sales".PadLeft(30)} | {number1.ToString().PadLeft(10)} |");  
			Console.WriteLine($"| {" Number of clients".PadLeft(30)} | {clients.Count.ToString().PadLeft(10)} |");  
			Console.WriteLine($"| {" Total items sold".PadLeft(30)} | {number2.ToString().PadLeft(10)} |");  
			Console.WriteLine($"| {" Total sales amount".PadLeft(30)} | {Math.Round(number3,2).ToString().PadLeft(10)} |");  
			Console.WriteLine($"| {" Average amount/sale".PadLeft(30)} | {number4.ToString().PadLeft(10)} |");  
			Console.WriteLine($"| {" Average item price".PadLeft(30)} | {number5.ToString().PadLeft(10)} |");  
			Console.WriteLine($"+{new String('-',45)}+");  
		}
		else    
		{    
			Console.WriteLine("[ERR] your command is not valid ");    
			Console.WriteLine("Help: ");    
			Console.WriteLine("    - [print]  : show the content of our commerce records in data.csv");    
			Console.WriteLine("    - [report] : show a summary from data.csv records ");    
		}  
 }}
```

expected output when ran:
```shell
SalesReporter(main) ▶ dotnet run -- report                                                                     
  ____            _                 __     __  _                                  
 / ___|    __ _  | |   ___   ___    \ \   / / (_)   ___  __      __   ___   _ __  
 \___ \   / _` | | |  / _ \ / __|    \ \ / /  | |  / _ \ \ \ /\ / /  / _ \ | '__|                                  
  ___) | | (_| | | | |  __/ \__ \     \ V /   | | |  __/  \ V  V /  |  __/ | |                                     
 |____/   \__,_| |_|  \___| |___/      \_/    |_|  \___|   \_/\_/    \___| |_|                                                                                                                     
┌─────────────────────────┐
│   === Sales Report ===  │
└─────────────────────────┘
╔══════════════════════╤══════════╗
║  Number of sales     │        5 ║
║  Number of clients   │        3 ║
║  Total items sold    │       11 ║
║  Total sales amount  │ $1441.84 ║
║  Average amount/sale │  $288.37 ║
║  Average item price  │  $131.08 ║
╚══════════════════════╧══════════╝
 report generated on 02/16/2022 09:54:48

```

example of a simple golden master test to capture the output:
```csharp
[_Fact_]
public void Test1()
{
	using var writer = new _StringWriter_();
 	Console.SetOut(writer);
 	Console.SetError(writer);
	Program.Main(new string[]{});
	var sut = writer.ToString();
	Check.That(sut).IsEqualTo(
	$"Hello World!{Environment.NewLine}");
}
```

data source data.csv:
```csv
orderid,userName,numberOfItems,totalOfBasket,dateOfBuy  
1, peter, 3, 123.00, 2021-11-30  
2, paul, 1, 433.50, 2021-12-11  
3, peter, 1, 329.99, 2021-12-18  
4, john, 5, 467.35, 2021-12-30  
5, john, 1, 88.00, 2022-01-04
```
