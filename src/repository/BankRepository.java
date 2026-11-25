package repository;

import models.Bank;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BankRepository {
    private static final String URL =
            "https://ewib.nbp.pl/plewibnra?dokNazwa=plewibnra.txt";

    public List<Bank> loadBanks()
            throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest
                .newBuilder(URI.create(URL))
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();

        HttpResponse<byte[]> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofByteArray()
        );

        if (response.statusCode() != 200) {
            throw new IOException(
                    "NBP HTTP status: " + response.statusCode()
            );
        }

        String fileText = new String(
                response.body(),
                Charset.forName("CP852")
        );

        return parseBanks(fileText);
    }

    List<Bank> parseBanks(String fileText)
            throws IOException {

        List<Bank> banks = new ArrayList<>();

        try (Scanner lines = new Scanner(fileText)) {
            while (lines.hasNextLine()) {
                String[] fields =
                        lines.nextLine().split("\t", -1);

                if (fields.length <= 4) {
                    continue;
                }

                String name = fields[1].trim();
                String settlementNumber = fields[4].trim();

                if (!name.isEmpty()
                        && settlementNumber.matches("\\d{8}")) {

                    banks.add(new Bank(settlementNumber, name));
                }
            }
        }

        if (banks.isEmpty()) {
            throw new IOException(
                    "No valid bank records found in NBP file."
            );
        }

        return banks;
    }
}
