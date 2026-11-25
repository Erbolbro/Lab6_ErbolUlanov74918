
import models.Bank;
import repository.BankRepository;
import servise.BankService;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        try (Scanner input = new Scanner(System.in)) {
            System.out.print("Enter bank code (3 digits): ");
            String code = input.nextLine().trim();

            if (!code.matches("\\d{3}")) {
                System.out.println("Please enter exactly 3 digits.");
                return;
            }

            BankService service =
                    new BankService(new BankRepository());

            try {
                List<Bank> banks = service.findByCode(code);

                if (banks.isEmpty()) {
                    System.out.println("Bank not found.");
                    return;
                }

                System.out.println("Bank number: " + code);

                for (Bank bank : banks) {
                    System.out.println(
                            "Bank name: " + bank.getName()
                    );
                }

                if (banks.size() > 1) {
                    System.out.println(
                            "Several banks match this prefix."
                    );
                }

            } catch (IOException e) {
                System.out.println(
                        "Cannot download/read NBP data: "
                                + e.getMessage()
                );
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Download interrupted.");
            }
        }
    }
}
