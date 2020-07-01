package uj.java.pwj2019.w7;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FloridaInsurance {

    public static void main(String[] args) {
        List<InsuranceEntry> insuranceEntryList = loadInsuranceEntryList();
        try {

            Files.write(
                    Paths.get("count.txt"),
                    String.valueOf(insuranceEntryList.stream()
                            .collect(
                                    Collectors.groupingBy(i->i.country(), Collectors.counting())
                            )
                            .size()
                    ).getBytes()
            );

            Files.write(
                    Paths.get("tiv2012.txt"),
                    insuranceEntryList.stream()
                            .map(InsuranceEntry::tiv2012)
                            .reduce(BigDecimal::add)
                            .get()
                            .toString()
                            .getBytes()
            );

            insuranceEntryList.stream()
                    .map(
                            i-> new Pair(i.country(), i.tiv2012().subtract(i.tiv2011()))
                    )
                    .collect(Collectors.groupingBy(
                            Pair::getFirst,
                            Collectors.mapping(Pair::getSecond, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                            )
                    )
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                    .limit(10)
                    .forEach(
                            i->{
                                try {
                                    if(Files.notExists(Paths.get("most_valuable.txt"))){
                                        Files.createFile(Paths.get("most_valuable.txt"));
                                        Files.write(
                                                Paths.get("most_valuable.txt"),
                                                new String("country,value\n")
                                                .getBytes()
                                        );
                                    }
                                    Files.write(
                                            Paths.get("most_valuable.txt"),
                                            new String(i.getKey()+","+i.getValue().toString()+"\n")
                                            .getBytes(),
                                            //String.valueOf(5).getBytes()
                                            StandardOpenOption.APPEND
                                    );
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                    );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static List<InsuranceEntry> loadInsuranceEntryList(){
        List<InsuranceEntry> insuranceEntryList = new ArrayList<>();

        try {
            ZipFile zipFile=new ZipFile("FL_insurance.csv.zip");
            ZipEntry entry=zipFile.getEntry("FL_insurance.csv");
            InputStream inputStream=zipFile.getInputStream(entry);
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line=bufferedReader.readLine();
            String[] insuranceEntryArgsTitles=line.split(",");

            while((line=bufferedReader.readLine())!=null){
                String[] insuranceEntryArgs=line.split(",");
                Map<String, String> mappedInsuranceLine=new HashMap<>();

                for(int i=0; i<insuranceEntryArgs.length; i++)
                    mappedInsuranceLine.put(insuranceEntryArgsTitles[i], insuranceEntryArgs[i]);

                insuranceEntryList.add(new InsuranceEntry(
                        Integer.parseInt(mappedInsuranceLine.get("policyID")),
                        mappedInsuranceLine.get("county"),
                        new BigDecimal(mappedInsuranceLine.get("tiv_2011")),
                        new BigDecimal(mappedInsuranceLine.get("tiv_2012")),
                        mappedInsuranceLine.get("line").equals("COMMERCIAL") ? Line.COMMERCIAL : Line.RESIDENTIAL,
                        mappedInsuranceLine.get("construction"),
                        Double.parseDouble(mappedInsuranceLine.get("point_latitude")),
                        Double.parseDouble(mappedInsuranceLine.get("point_longitude"))
                ));
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return insuranceEntryList;
    }
}
