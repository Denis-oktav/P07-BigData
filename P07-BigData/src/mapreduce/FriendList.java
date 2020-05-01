/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapreduce;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.Map;
import org.bson.Document;

/**
 *
 * @author Denis
 */
public class FriendList {
    public static void main(String[] args) {
        try {
            MongoDatabase db = Koneksi.sambungDB();
            MongoCollection<Document> collection = db.getCollection("friendlist");
            FindIterable<Document> friendList = collection.find();
            ArrayList<Document> friends = new ArrayList<>();
            for (Document friend : friendList){
                friends.add(friend);
            }
            ArrayList<Document> MapResult = MapReduce.map(friends);
            Map<ArrayList, ArrayList<ArrayList>> groupResult = MapReduce.group(MapResult);
            MapReduce.group(MapResult);
            Map<ArrayList, ArrayList> reduceResult = MapReduce.reduce(groupResult);
            
            System.out.println("------Friend List---------------");
            friends.forEach((f) -> {
                String name = f.getString("name");
                ArrayList<String> theFriends = (ArrayList)f.get("friends");
                System.out.println("[" + name + "] berteman dengan: ");
                for (int i = 0; i < theFriends.size(); i++) {
                    System.out.println("\t"+ (i+1) + ". " + theFriends.get(i));
                    
                }
            });
            System.out.println("\n\n");
            
            System.out.println("------Map Result-------------");
            MapResult.forEach((k) -> {
                ArrayList names = (ArrayList)k.get("key");
                ArrayList theFriend = (ArrayList)k.get("value");
                System.out.println("Teman dari " + names.toString() + "\n");
                for (int i = 0; i < theFriend.size(); i++) {
                    System.out.println("\t"+ (i+1) + ". " + theFriend.get(i));
                    
                }
            });
            System.out.println("\n\n");
            
            System.out.println("------Group Result-------------");
            groupResult.entrySet().forEach((k) -> {
                ArrayList names = (ArrayList)k.getKey();
                ArrayList<ArrayList> group = (ArrayList)k.getValue();
                System.out.println("Group Pertemanan dari " + names.toString());
                int n = 1;
                for (ArrayList g : group) {
                    System.out.println("\tGroup " + n);
                    int ls = 1;
                    for (Object gm : g) {
                        System.out.println("\t" + ls + ". " + gm);
                        ls++;
                    }
                    n++;
                }
            });
            System.out.println("\n\n");
            
            System.out.println("------Reduce Result-------------");
            reduceResult.entrySet().forEach((k) -> {
                ArrayList names = (ArrayList)k.getKey();
                ArrayList fic = (ArrayList)k.getValue();
                String status ;
                if(fic.size() > 0) {
                    status = " Memiliki Teman yang sama : \n";
                }else {
                    status = " TIDAK Memiliki Teman yang sama \n";
                }    
                System.out.println(names.get(0)+" dan " +names.get(1) + status);
                for (int i = 0; i < fic.size(); i++) {
                    System.out.println("\t" + (i+1) + ". " + fic.get(i)); 
                }
                });
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
}
