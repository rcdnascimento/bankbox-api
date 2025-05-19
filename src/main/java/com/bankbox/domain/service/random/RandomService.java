package com.bankbox.domain.service.random;

import com.bankbox.domain.entity.*;
import com.bankbox.domain.service.customer.impl.CustomerService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class RandomService {

  private final CustomerService customerService;
  private final List<String> firstNames = new ArrayList<>();
  private final List<String> surnames = new ArrayList<>();

  private static final String NAMES_FILE_PATH = "/Users/mbp16/Documents/Projects/bankbox-api/src/main/resources/names.txt";
  private static final String SURNAMES_FILE_PATH = "/Users/mbp16/Documents/Projects/bankbox-api/src/main/resources/surnames.txt";

  public RandomService(CustomerService customerService) {
    this.customerService = customerService;
    this.firstNames.addAll(extractNamesFromFile(NAMES_FILE_PATH));
    this.surnames.addAll(extractSurnamesFromFile(SURNAMES_FILE_PATH));
  }

  public RandomSummary generateRandomCustomers(RandomConfiguration configuration) {
    Integer generatedCustomers = 0;
    for (String name : firstNames) {
      if (generatedCustomers >= configuration.getMaximum()) {
        break;
      }

      Integer generatedWithSameFirstName = generateRandomCustomerWithFirstName(configuration, generatedCustomers, name);
      generatedCustomers += generatedWithSameFirstName;
    }

    return new RandomSummary(generatedCustomers);
  }

  public Integer generateRandomCustomerWithFirstName(
    RandomConfiguration configuration,
    Integer currentGeneratedCustomers,
    String firstName
  ) {
    Random random = new Random();
    int times = random.nextInt(configuration.getMaximumPerFirstName()-1) + 1;

    Integer generatedCustomers = 0;

    for (int i = 0; i < times; i++) {
      if (currentGeneratedCustomers+generatedCustomers >= configuration.getMaximum()) {
        break;
      }

      String firstSurname = surnames.get(random.nextInt(surnames.size()));
      String secondSurname = surnames.get(random.nextInt(surnames.size()));

      Customer customer = Customer.builder().
        name(String.format("%s %s %s", firstName, firstSurname, secondSurname)).
        cpf(new RandomCode(RandomCodeType.NUMERIC, 11).getCode()).
        bankAccounts(new ArrayList<>()).
        build();
      customer.setPassword(new RandomCode(RandomCodeType.ALPHANUMERIC, 6).getCode());

      customerService.createCustomer(customer);
      generatedCustomers++;
    }

    return generatedCustomers;
  }

  public List<String> extractNamesFromFile(String filePath) {
    List<String> lines = new ArrayList<>();

    try {
      Scanner scanner = new Scanner(new File(String.format("%s", filePath)));
      while (true) {
        if (!scanner.hasNextLine()) {
          break;
        }

        String input = scanner.nextLine();
        if (input.isEmpty()) {
          break;
        }

        lines.add(input);
      }
      return lines;
    } catch (Exception exception) {
      System.out.println("Error: " + exception.getMessage());
    }

    return List.of();
  }

  public List<String> extractSurnamesFromFile(String filePath) {
    List<String> lines = new ArrayList<>();

    try {
      Scanner scanner = new Scanner(new File(filePath));
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (!line.isEmpty()) {
          lines.add(line);
        }
      }
      scanner.close();
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }

    return lines;
  }
}
