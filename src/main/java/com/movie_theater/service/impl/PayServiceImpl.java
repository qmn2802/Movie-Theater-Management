package com.movie_theater.service.impl;

import com.google.gson.Gson;
import com.movie_theater.dto.jsonDTO.TransactionDTO;
import com.movie_theater.service.PayService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class PayServiceImpl implements PayService {
    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();
    @Override
    public boolean isTransactionCodeExisted(String transactionCode) {
        int currentPage = 1;
        int totalPages = 1;
        String fromDate = getFromDate();

        while (currentPage <= totalPages) {
            Request request = new Request.Builder()
                    .url("https://oauth.casso.vn/v2/transactions?fromDate="+ fromDate +"&page=" + currentPage + "&pageSize=100&sort=ASC")
                    .get()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Apikey AK_CS.f451ad80de4411ee9bf047310018d428.5t3I3NC26xIiexWVNxVtxLuIPlVLhfBktPgl1JpiwHqvNEG9ZUyP8j5JkQA1F9EceOX0lDJy")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body().string();
                TransactionDTO transaction = gson.fromJson(responseBody, TransactionDTO.class);

                boolean found = transaction.data.getRecords().stream().anyMatch(record -> record.getDescription().contains(transactionCode));
                if (found) {
                    return true;
                }
                totalPages = transaction.data.getTotalPages();
                currentPage++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
    @Override
    public boolean isTransactionCompleted(String transactionCode, long amount) {
        int currentPage = 1;
        int totalPages = 1;
        String fromDate = getFromDate();

        while (currentPage <= totalPages) {
            Request request = new Request.Builder()
                    .url("https://oauth.casso.vn/v2/transactions?fromDate=" + fromDate + "&page=" + currentPage + "&pageSize=100&sort=ASC")
                    .get()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Apikey AK_CS.f451ad80de4411ee9bf047310018d428.5t3I3NC26xIiexWVNxVtxLuIPlVLhfBktPgl1JpiwHqvNEG9ZUyP8j5JkQA1F9EceOX0lDJy")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body().string();
                TransactionDTO transaction = gson.fromJson(responseBody, TransactionDTO.class);

                boolean found = transaction.data.getRecords().stream().anyMatch(record ->
                        record.getDescription().contains(transactionCode) && record.getAmount() == amount);
                if (found) {
                    return true;
                }
                totalPages = transaction.data.getTotalPages();
                currentPage++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    @Override
    public String createTransactionCode() {
        return generateRandomString(8);
    }

    private String getFromDate(){
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(10);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return yesterday.format(formatter);
    }

    private String generateRandomString(int length) {
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charSet.length());
            sb.append(charSet.charAt(index));
        }
        return sb.toString();
    }
}
