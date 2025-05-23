package com.back;

import com.google.gson.Gson;
import java.io.*;

public class FileHelper {
    private static final Gson gson = new Gson();

    // JSON 저장
    public static void saveToJsonFile(WiseSaying wiseSaying, String filePath) {
        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs(); // 디렉토리 없으면 생성
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                gson.toJson(wiseSaying, writer);
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + filePath, e);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            gson.toJson(wiseSaying, writer);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + filePath, e);
        }
    }

    // JSON 로드
    public static WiseSaying loadFromJsonFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return gson.fromJson(reader, WiseSaying.class);
        } catch (IOException e) {
            throw new RuntimeException("파일 로드 실패: " + filePath, e);
        }
    }

    // 마지막 ID 저장
    public static void saveLastId(int lastId, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(String.valueOf(lastId));
        } catch (IOException e) {
            throw new RuntimeException("lastId 저장 실패", e);
        }
    }

    // 마지막 ID 읽기
    public static int loadLastId(String filePath) {
        File file = new File(filePath);
        if (!file.exists()){
            return 0;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            throw new RuntimeException("lastId 로드 실패", e);
        }
    }
}