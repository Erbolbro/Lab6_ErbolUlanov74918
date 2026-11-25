package servise;

import models.Bank;
import repository.BankRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BankService {
    private final BankRepository repository;

    public BankService(BankRepository repository) {
        this.repository = repository;
    }

    public List<Bank> findByCode(String code)
            throws IOException, InterruptedException {

        if (code == null || !code.matches("\\d{3}")) {
            throw new IllegalArgumentException(
                    "Bank code must have 3 digits."
            );
        }

        List<Bank> allBanks = repository.loadBanks();
        Map<String, Bank> uniqueBanks = new LinkedHashMap<>();

        for (Bank bank : allBanks) {
            if (bank.matchesCode(code)) {
                uniqueBanks.putIfAbsent(bank.getName(), bank);
            }
        }

        return new ArrayList<>(uniqueBanks.values());
    }
}
