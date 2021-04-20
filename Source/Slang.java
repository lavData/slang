/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.slangproject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mwg
 */


public class Slang {
    private static Map<String, ArrayList<String>> multiMap = new HashMap<String, ArrayList<String>>();

    public static void setMultiMap(Map<String, ArrayList<String>> multiMap) {
        Slang.multiMap = multiMap;
    }
    private static Map<String, ArrayList<String>> historyMap = new HashMap<String, ArrayList<String>>();

    public static Map<String, ArrayList<String>> getHistoryMap() {
        return historyMap;
    }

    public static  Map<String, ArrayList<String>> getMultiMap() {
        return multiMap;
    }

    
    public static void readFile()
    {
        multiMap.clear();
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new FileReader("slang.txt"));
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage()+ "The file was not found");
            Logger.getLogger(Slang.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        
        try{
            while((line = br.readLine()) != null){
                String[] tempStrings = line.split("`");
                ArrayList<String> arrayList = new ArrayList<>();
                String key = tempStrings[0];
                if(tempStrings.length > 1){
                    tempStrings = tempStrings[1].split("[|]");
                    for(int i = 0; i <tempStrings.length; i++){
                        tempStrings[i] = tempStrings[i].trim();
                        arrayList.add(tempStrings[i]);
                        }
                    Slang.multiMap.put(key, arrayList);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Slang.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(Slang.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static ArrayList<String> findSlangWord(String tempString)
    {   
        ArrayList<String> get = Slang.multiMap.get(tempString);
        if(Slang.multiMap.get(tempString) == null){
            return null;
        }
        Slang.historyMap.put(tempString, Slang.multiMap.get(tempString));
        return Slang.multiMap.get(tempString);
    }
    
    
    public static Object[] definition(String tempString){
        ArrayList<String> result;
        result = new ArrayList<>();
        Set<Map.Entry<String, ArrayList<String>>> set = multiMap.entrySet();
        for (Map.Entry<String, ArrayList<String>> entry : set) {
            for(String find : entry.getValue()){
                if(find.contains(tempString)){
                    result.add(entry.getKey());
                    Slang.historyMap.put(entry.getKey(), Slang.multiMap.get(entry.getKey()));
                }
            }
        }        
        Object[] strings = result.toArray();
        return strings;
    }
    
    public static void addSlangWord(String NewSlangs, String value, int choose){
        String[] valueStrings = null;
        if(value.length() > 0){
                valueStrings = value.split("[|]");
                for(int i = 0; i <valueStrings.length; i++){
                    valueStrings[i] = valueStrings[i].trim();
                }
            }
        if(choose == 0 || choose == 1){ // 0 nghĩa là chưa tồn tại slang, 1 là overwrite, 2 là duplicate
            Slang.multiMap.put(NewSlangs, new ArrayList<>());
            Slang.multiMap.get(NewSlangs).addAll(Arrays.asList(valueStrings));
        }
        else if(choose == 2){
            Slang.multiMap.get(NewSlangs).add(value);
        }
    }

    public static void removeSlangWordValue(String keyString, String value){
        Slang.multiMap.get(keyString).remove(value);
    }
    public static void deleteSlang(String delString){
        Slang.multiMap.remove(delString);
    }
    public static Object random(){
        Set<Map.Entry<String, ArrayList<String>>> set = multiMap.entrySet();
        var randomMap = set.toArray();
        Random rd = new Random();
        int randomSlang = rd.nextInt(set.size());
        
        return randomMap[randomSlang];
    }
    public static String[] quizzSlang(){
        Set<String> set = multiMap.keySet();
        var randomMap = set.toArray();
        Random rd = new Random();
        int[] randomSlang = new int[]{0,0,0,0};
        for(int i = 0; i < 4; i++){
            randomSlang[i] = rd.nextInt(set.size());
            for(int k = 0; k < 4; k++){
                if(k == i){
                    continue;
                }
                while(randomSlang[k] == randomSlang[i]){
                    randomSlang[i] = rd.nextInt(set.size());
                }
            }
        }
        String[] randomStrings = new String[]{(String)randomMap[randomSlang[0]]
                ,(String)randomMap[randomSlang[1]]
                ,(String)randomMap[randomSlang[2]]
                ,(String)randomMap[randomSlang[3]]};
        return randomStrings;
    }
}
