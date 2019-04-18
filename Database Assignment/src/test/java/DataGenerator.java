import com.github.javafaker.Faker;
import com.sun.org.apache.bcel.internal.generic.Select;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.*;
import com.mysql.jdbc.Driver;
import java.util.Scanner;



public class DataGenerator {


    // CSV file header
    private static final Object[] FILE_HEADER = { "Empoyee Name","Empoyee Code", "In Time", "Out Time", "Duration", "Is Working Day" };
    static final String JBDC_DRIVER="com.mysql.jbdc.Driver";
    static final String DB_URL="jdbc:mysql://35.233.189.159:3306/TheEmployeeDatabase";
    static final String USER="bryce901";
    static final String PASS="poop";

    public static void main (String[] args)
    {
        Faker faker = new Faker();
        String numOfTuples="";
        Connection conn=null;
        Statement stmt=null;
        Scanner input=new Scanner(System.in);
        Integer mainTableNumber=0;
        Integer ID=0;
        //for randomizing it down below it must be initalized
        String theid="";
        String name="";
        String firstName="";
        String LastName="";
        String jobTitle="";
        String companyName="";
        String streetAddress="";
        String cityName="";
        String country="";
        String beerHop="";
        String beerStyle="";
        String faveBeerName="";
        String industryName="";
        String theFileName="";
        Integer numTuplesInt=0;
        FileWriter MainfileWriter=null;


        //reister jbdc driver
        try {
            System.out.println("Connecting to the database...");
            conn=DriverManager.getConnection(DB_URL,USER,PASS);
            System.out.println("Connected to database successfully");
            System.out.println("What the name of the file you want to be created");
            theFileName=input.nextLine();
            System.out.println("How many tuples do you want");
            numOfTuples=input.nextLine();
            stmt=conn.createStatement();


            //Creating Main Table
            String TableDropMain="DROP TABLE IF EXISTS MainInfoTable;";
            String CreateMain="CREATE TABLE MainInfoTable (ID VARCHAR (25) PRIMARY KEY, FirstName VARCHAR(25),\n" +
                    "                      LastName VARCHAR(25),CompanyName varchar (45),StreetAddress varchar(45),FavoriteBeer varchar (45));";
            stmt.executeUpdate(TableDropMain);
            stmt.executeUpdate(CreateMain);

            //Creating Simple Table
            String simpleTableDrop="DROP TABLE IF EXISTS simpleTable;";
            String CreateSimple="CREATE TABLE simpleTable (ID VARCHAR (20) PRIMARY KEY, FirstName VARCHAR(25),\n" +
                    "                      LastName VARCHAR(25));";
            stmt.executeUpdate(simpleTableDrop);
            stmt.executeUpdate(CreateSimple);

            //Creating Address table
            String addressTableDrop="DROP TABLE IF EXISTS addressTable;";
            String createAddressTable="CREATE TABLE addressTable (StreetAdress varchar (30), CityName VARCHAR(50),\n" +
                    "                      Country VARCHAR(50));";
            stmt.executeUpdate(addressTableDrop);
            stmt.executeUpdate(createAddressTable);

            //Creating Company Table
            String companyTableDrop="DROP TABLE IF EXISTS companyTable;";
            String createCompanyTable="CREATE TABLE companyTable (CompanyName varchar (30),\n" +
                    "                      Industry VARCHAR(50));";
            stmt.executeUpdate(companyTableDrop);
            stmt.executeUpdate(createCompanyTable);

            //Creating Beer Table
            String beerTableDrop="DROP TABLE IF EXISTS beerTable;";
            String createBeerTable="CREATE TABLE beerTable (BeerName varchar (50), Hop VARCHAR(50),\n" +
                    "                      Style VARCHAR(100));";
            stmt.executeUpdate(beerTableDrop);
            stmt.executeUpdate(createBeerTable);


            numTuplesInt=Integer.parseInt(numOfTuples);

        }  catch (SQLException e) {
            e.printStackTrace();
        }


        while (mainTableNumber<numTuplesInt)
        {
            //now printing all tuples to the csv
            firstName=faker.name().firstName();
            LastName=faker.name().lastName();
            jobTitle=faker.job().title();
            companyName=faker.company().name();
            streetAddress = faker.address().streetAddress(); // 60018 Sawayn Brooks Suite 449
            cityName=faker.address().cityName();
            country=faker.address().country();
            beerHop=faker.beer().hop();
            beerStyle=faker.beer().style();
            faveBeerName=faker.beer().name();
            industryName=faker.company().industry();

            //ID is set to zero up top and is now counting and printing to the csv
            ID++;
            String sID=ID.toString();
            try{
                MainfileWriter=new FileWriter(theFileName,true);
                MainfileWriter.append(sID+",");
                MainfileWriter.append(firstName+","+LastName+","+jobTitle+","+companyName+","+streetAddress+","+cityName+","+country+","+beerHop+","+beerStyle+","+faveBeerName+","+industryName+"\n");
                MainfileWriter.close();

            }
            catch (Exception e)
            {
            }

            mainTableNumber++;
        }

        //writing to simpleTable
        try {
            BufferedReader br=new BufferedReader(new FileReader(theFileName));
            String line="";
            String csvSplitBy=",";
            while((line=br.readLine())!=null){
                String [] data=line.split(csvSplitBy);
                String studentIDS =data[0];
                String thefirstName=data[1];
                String lastName=data[2];
                String insertIntoSimple="INSERT into simpleTable (ID, FirstName,LastName) values (?,?,?);";
                PreparedStatement preparedStmt=conn.prepareStatement(insertIntoSimple);
                preparedStmt.setString(1,studentIDS);
                preparedStmt.setString(2,thefirstName);
                preparedStmt.setString(3,lastName);
                preparedStmt.execute();


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //writing mainTable
        try {
            BufferedReader br=new BufferedReader(new FileReader(theFileName));
            String line="";
            String csvSplitBy=",";
            while((line=br.readLine())!=null){
                String [] data=line.split(csvSplitBy);
                String studentIDS =data[0];
                String thefirstName=data[1];
                String lastName=data[2];
                String CompanyName=data[4];
                String StreetAddress=data[5];
                String favBeer=data[10];

                String insertIntoMain="INSERT into MainInfoTable (ID, FirstName,LastName,CompanyName,StreetAddress,FavoriteBeer) values (?,?,?,?,?,?);";
                PreparedStatement preparedStmt=conn.prepareStatement(insertIntoMain);
                preparedStmt.setString(1,studentIDS);
                preparedStmt.setString(2,thefirstName);
                preparedStmt.setString(3,lastName);
                preparedStmt.setString(4,CompanyName);
                preparedStmt.setString(5,StreetAddress);
                preparedStmt.setString(6,favBeer);
                preparedStmt.execute();


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        //writing to address table
        try {
            BufferedReader br=new BufferedReader(new FileReader(theFileName));
            String line="";
            String csvSplitBy=",";
            while((line=br.readLine())!=null){
                String [] data=line.split(csvSplitBy);
                String TheStreetAddress=data[5];
                String TheCityName=data[6];
                String TheCountry=data[7];
                String insertIntoAddressTable="INSERT into addressTable (StreetAdress,CityName,Country) values (?,?,?);";
                PreparedStatement preparedStmt=conn.prepareStatement(insertIntoAddressTable);
                preparedStmt.setString(1,TheStreetAddress);
                preparedStmt.setString(2,TheCityName);
                preparedStmt.setString(3,TheCountry);

                preparedStmt.execute();


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //writing to beer table
        try {
            BufferedReader br=new BufferedReader(new FileReader(theFileName));
            String line="";
            String csvSplitBy=",";
            while((line=br.readLine())!=null){
                String [] data=line.split(csvSplitBy);
                String TheBeerName=data[10];
                String TheHopName=data[9];
                String TheStyleName=data[8];
                String insertIntoAddressTable="INSERT into beerTable (BeerName,Hop,Style) values (?,?,?);";
                PreparedStatement preparedStmt=conn.prepareStatement(insertIntoAddressTable);
                preparedStmt.setString(1,TheBeerName);
                preparedStmt.setString(2,TheHopName);
                preparedStmt.setString(3,TheStyleName);
                preparedStmt.execute();


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //inserting into company table
        try {
            BufferedReader br=new BufferedReader(new FileReader(theFileName));
            String line="";
            String csvSplitBy=",";
            while((line=br.readLine())!=null){
                String [] data=line.split(csvSplitBy);
                String TheCompanyNames=data[4];
                String TheIndustryNames=data[11];
                String insertIntoAddressTable="INSERT into companyTable (CompanyName,Industry) values (?,?);";
                PreparedStatement preparedStmt=conn.prepareStatement(insertIntoAddressTable);
                preparedStmt.setString(1,TheCompanyNames);
                preparedStmt.setString(2,TheIndustryNames);
                preparedStmt.execute();


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}

