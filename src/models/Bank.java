package models;

public class Bank {
    private String settlementNumber;
    private String name;

    public Bank(String code, String name) {
        this.settlementNumber = code;
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public boolean matchesCode(String inputCode) {
        return settlementNumber.startsWith(inputCode);
    }
}
