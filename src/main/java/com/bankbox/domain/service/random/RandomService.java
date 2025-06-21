package com.bankbox.domain.service.random;

import com.bankbox.domain.entity.*;
import com.bankbox.domain.service.creditcard.impl.CreditCardService;
import com.bankbox.domain.service.creditcardtransaction.impl.CreditCardTransactionService;
import com.bankbox.domain.service.customer.impl.CustomerService;
import com.bankbox.domain.service.transaction.v1.TransactionService;
import com.bankbox.infra.dto.CreditCardTransactionRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Service
public class RandomService {

  private final CustomerService customerService;
  private final CreditCardTransactionService creditCardTransactionService;
  private final CreditCardService creditCardService;
  private final TransactionService transactionService;

  private final List<String> firstNames = new ArrayList<>();
  private final List<String> surnames = new ArrayList<>();
  private final List<String> merchantNames = new ArrayList<>();

  private static final String NAMES_FILE_PATH = "/Users/mbp16/Documents/Projects/bankbox-api/src/main/resources/names.txt";
  private static final String SURNAMES_FILE_PATH = "/Users/mbp16/Documents/Projects/bankbox-api/src/main/resources/surnames.txt";
  private static final String MERCHANT_NAMES_FILE_PATH = "/Users/mbp16/Documents/Projects/bankbox-api/src/main/resources/merchantnames.txt";

  public RandomService(CustomerService customerService, CreditCardTransactionService creditCardTransactionService, CreditCardService creditCardService, TransactionService transactionService) {
    this.customerService = customerService;
    this.firstNames.addAll(extractNamesFromFile(NAMES_FILE_PATH));
    this.surnames.addAll(extractSurnamesFromFile(SURNAMES_FILE_PATH));
    this.merchantNames.addAll(extractNamesFromFile(MERCHANT_NAMES_FILE_PATH));
    this.creditCardTransactionService = creditCardTransactionService;
    this.creditCardService = creditCardService;
    this.transactionService = transactionService;
  }

  public RandomSummary generateRandomCustomers(RandomConfiguration configuration) {
    Long lastCustomerId = customerService.retrieveLastCustomerId();
    Integer generatedCustomers = 0;

    for (String name : firstNames) {
      if (generatedCustomers >= configuration.getMaximum()) {
        break;
      }

      Integer generatedWithSameFirstName = generateRandomCustomerWithFirstName(configuration, generatedCustomers, name);
      generatedCustomers += generatedWithSameFirstName;
    }

    addTransactionsBetweenUsers(Objects.nonNull(lastCustomerId) ? lastCustomerId : 0L);

    return new RandomSummary(generatedCustomers);
  }

  public Integer generateRandomCustomerWithFirstName(
    RandomConfiguration configuration,
    Integer currentGeneratedCustomers,
    String firstName
  ) {
    long startTime = System.currentTimeMillis();
    Random random = new Random();
    int times = random.nextInt(configuration.getMaximumPerFirstName() - 1) + 1;

    Integer generatedCustomers = 0;

    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    for (int i = 0; i < times; i++) {
      if (currentGeneratedCustomers + generatedCustomers >= configuration.getMaximum()) {
        break;
      }

      executor.submit(() -> {
        String firstSurname = surnames.get(random.nextInt(surnames.size()));
        String secondSurname = surnames.get(random.nextInt(surnames.size()));

        Customer customer = Customer.builder().
          name(String.format("%s %s %s", firstName, firstSurname, secondSurname)).
          cpf(new RandomCode(RandomCodeType.NUMERIC, 11).getCode()).
          bankAccounts(new ArrayList<>()).
          build();
        customer.setPassword(new RandomCode(RandomCodeType.ALPHANUMERIC, 6).getCode());

        Customer createdCustomer = customerService.createCustomer(customer);
        List<CreditCard> creditCards = creditCardService.retrieveByCustomerId(createdCustomer.getId());

        addRandomCreditCardTransactions(createdCustomer, creditCards);
      });
      generatedCustomers++;
    }

    executor.shutdown();
    try {
      executor.awaitTermination(1, TimeUnit.HOURS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      System.out.println("Error while waiting for tasks to finish: " + e.getMessage());
    }

    long endTime = System.currentTimeMillis();

    System.out.println("Generated " + generatedCustomers + " customers with first name: " + firstName + " in " + (endTime - startTime) + " ms");

    return generatedCustomers;
  }

  private void addRandomCreditCardTransactions(Customer customer, List<CreditCard> creditCards) {
    creditCards.stream().forEach((creditCard) -> {
      Stream.of(1, 1, 1, 1, 1).parallel().forEach(installments -> {
        String merchantName = merchantNames.get(new Random().nextInt(merchantNames.size()));
        BigDecimal value = BigDecimal.valueOf(new Random().nextDouble() * 1000).setScale(2, BigDecimal.ROUND_HALF_UP);
        LocalDateTime processedAt = LocalDateTime.now().minusDays(new Random().nextInt(30)).minusMonths(new Random().nextInt(24));

        CreditCardTransactionRequest transactionRequest = new CreditCardTransactionRequest();
        transactionRequest.creditCardId = creditCard.getId();
        transactionRequest.merchantName = merchantName;
        transactionRequest.category = CategoryEnum.values()[new Random().nextInt(CategoryEnum.values().length)];
        transactionRequest.installments = new Random().nextInt(5) + 1;
        transactionRequest.value = value;
        transactionRequest.processedAt = processedAt;

        creditCardTransactionService.createTransaction(transactionRequest);
      });
    });

    if (!creditCards.isEmpty()) {
      creditCardService.generateUnifiedCardForCustumer(customer.getId());
    }
  }

  private void addTransactionsBetweenUsers(Long fromCustomerId) {
    Long customersSize = customerService.countCustomers() > 0 ? customerService.countCustomers() : 1L;

    Random random = new Random();

    Long sourceBankAccountId = random.nextInt(customersSize.intValue()-fromCustomerId.intValue()) + 1L;
    Long targetBankAccountId = random.nextInt(customersSize.intValue()-fromCustomerId.intValue()) + 1L;

    Long numberOfTransactionsForUser = 5L;

    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    executor.submit(() -> {
      for (long i = fromCustomerId; i < customersSize; i++) {
        for (int j = 0; j < numberOfTransactionsForUser; j++) {
          Transaction transaction = new Transaction();

          transaction.setValue(BigDecimal.valueOf((random.nextInt(1000) + 1) * 5).setScale(2, BigDecimal.ROUND_HALF_UP));
          transaction.setBeneficiary(new BankAccount(sourceBankAccountId));
          transaction.setSource(new BankAccount(targetBankAccountId));
          transaction.setType(TransactionType.PIX);
          transaction.setPerformedAt(LocalDateTime.now().minusDays(random.nextInt(30)).minusMonths(random.nextInt(24)));

          transactionService.saveTransaction(transaction);
        }
      }
    });

    executor.shutdown();

    try {
      executor.awaitTermination(1, TimeUnit.HOURS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      System.out.println("Error while waiting for tasks to finish: " + e.getMessage());
    }
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
