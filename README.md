## CEN3031Fall22MWF-Group 6

# **Linen Tracking program for Commercial Laundry**

# **Project Information**:

***Develop a commercial laundry management program that will:***
  - Keep track of soiled linen weight by customers 
  - Keep track of clean linen weight by customers
  - Filter pounds of clean/soiled by customers
  - Filter pounds clean/soiled by days
  - Generate invoices for accounting
  - Create new customers
  - Make past customers inactive

# **Project Description**:
## *Background*
  Commercial laundries process millions of pounds of linen annually. Keeping track of soiled and clean linen pounds 
is essential to the business’s profitability. The ability to store and manipulate data to forecast labor, 
linen purchases, and other operating  costs is critical to running a successful commercial laundry.

  In this project, we will develop a program to collect data entered by a laundry staff and store it in a database.  
There will be a simple JAVA-based GUI to interact with the user. We will also provide features for 
managers to manipulate the data to make short and long-term decisions.

  The program will allow the creation of new customers, store them in a database, and take in user data 
for each customer. Users can also manipulate the data to filter desired results, such as 
“pounds delivered to a customer within a time period” or “total soiled linen pounds received within a given period. 
The program will also allow the user to generate invoices for the customer in terms of cost per pound.

We will use SQLite for the database and Java GUI to interact with the customer,

JDBC API will connect JAVA with the database. The SQLite queries will be executed using JAVA.

Software used:

Database: SqLite
User Interface : Java
Connecting Java to SqLite: sqlite-jdbc-3.39.3.0

## *Basic Design*

## *How to install*
1. Download Java JDK 17 and JRE 17 from https://adoptium.net/
2. Clone the repo into the directory of your choice.
3. Download and install Maven https://maven.apache.org/, for instructions reference https://www.geeksforgeeks.org/how-to-install-apache-maven-on-windows/
4. Go into the folder "cen3031-fall22-mwf-semester-project-group-6" in a command prompt and type "mvn clean package"

The project should be runnable and ready to use.


## *How to execute*
1. Go into the target folder and either run "java -jar .\cen3031-fall22-mwf-semester-project-group-6-1.0-jar-with-dependencies.jar" or double click the .jar file that says it contains dependencies

# **Team members**:
- Bryan Castro-Nunez
- Austin Franklin
- Evan Overly
- Tyler Taylor
- Prasad Jeyapalan