package com.back;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;


public class Main {
    static final String DB_PATH = "db/wiseSaying/";
    static final String LAST_PATH = DB_PATH + "lastId.txt";
    static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) {
        System.out.println("== 명언 앱==");
        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.print("명령) ");
            String cmd = scanner.nextLine().trim();

            if(cmd.equals("종료")){
                break;
            }
            else if(cmd.equals("등록")){
                int lastId = FileHelper.loadLastId(LAST_PATH);
                int newId = lastId + 1;

                System.out.print("명언 : ");
                String wiseSayingContent = scanner.nextLine().trim();
                System.out.print("작가 : ");
                String wiseSayingAuthor = scanner.nextLine().trim();

                if(wiseSayingContent.isEmpty() || wiseSayingAuthor.isEmpty()){
                    System.out.println("명언이나 작가는 비워둘 수 없습니다");
                    continue;
                }
                WiseSaying ws = new WiseSaying(newId, wiseSayingContent, wiseSayingAuthor);
                String jsonFilePath = String.format(DB_PATH + "%d.json", newId);
                FileHelper.saveToJsonFile(ws, jsonFilePath);

                // 2. 마지막 id를 lastId.txt에 저장
                FileHelper.saveLastId(newId, LAST_PATH);
                System.out.printf("%d번 명언이 등록되었습니다.\n",newId);
            }
            else if(cmd.equals("목록")){
                File folder = new File(DB_PATH);
                File[] files = folder.listFiles((dir, name) -> name.endsWith(".json") && !name.equals("lastId.txt"));

                if( files==null || files.length==0){
                    System.out.println("등록된 명언이 없습니다.");
                    continue;
                }

                System.out.println("----------------------");

                Arrays.sort(files,(a,b) -> {
                    int id1 = Integer.parseInt(a.getName().replace(".json", ""));
                    int id2 = Integer.parseInt(b.getName().replace(".json", ""));
                    return Integer.compare(id2, id1);
                });

                for(File file : files){
                    WiseSaying ws = FileHelper.loadFromJsonFile(file.getAbsolutePath());
                    System.out.printf("%d / %s / %s\n",ws.getId(),ws.getAuthor(), ws.getContent());
                }
            }

            else if (cmd.startsWith("삭제?id=")) {
                int id = parseId(cmd, "삭제?id=");
                if(id == -1) continue;

                File file = new File(DB_PATH + id + ".json");
                if(file.exists()){
                    file.delete();
                    System.out.printf("%d번 명언이 삭제되었습니다.\n",id);
                }else{
                    System.out.printf("%d번 명언은 존재하지 않습니다.\n",id);
                }
            }
            else if(cmd.startsWith("수정?id=")){
                int id = parseId(cmd, "수정?id=");
                if(id == -1) continue;

                File file = new File(DB_PATH + id + ".json");
                if(!file.exists()){
                    System.out.printf("%d번 명언은 존재하지 않습니다.\n",id);
                    continue;
                }

                WiseSaying old = FileHelper.loadFromJsonFile(file.getAbsolutePath());
                System.out.printf("명언(기존) : %s\n", old.content);
                System.out.print("명언 : ");
                String newContent = scanner.nextLine().trim();

                System.out.printf("작가(기존) : %s\n", old.author);
                System.out.print("작가 : ");
                String newAuthor = scanner.nextLine().trim();

                WiseSaying update = new WiseSaying(id, newContent, newAuthor);
                FileHelper.saveToJsonFile(update, DB_PATH + id + ".json");
                System.out.printf("%d번 명언이 수정되었습니다.\n",id);
            }
            else{
                System.out.println("잘못된 입력입니다.");
            }
        }
        scanner.close();
    }

    static int parseId(String cmd, String subs){
        try{
            return Integer.parseInt(cmd.substring(subs.length()));
        } catch(Exception e) {
            System.out.println("올바르지 않은 id입니다");
            return -1;
        }
    }
}