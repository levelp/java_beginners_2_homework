import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Contact {
    private String name; // Фамилия Имя Отчество
    private List<String> phones; // Список телефонов

    public Contact(String name, String... phones) {
        this(name, Arrays.asList(phones));
    }

    public Contact(String name, List<String> phones) {
        this.name = name;
        this.phones = new ArrayList<>(phones);
    }

    public String getName() {
        return name;
    }

    public List<String> getPhones() {
        return new ArrayList<>(phones);
    }
}