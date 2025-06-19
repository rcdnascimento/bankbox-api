INSERT INTO consent_role (name, description)
VALUES ('READ_ACCOUNTS', 'Permissão para visualizar contas bancárias conectadas pelo usuário.'),
       ('READ_TRANSACTIONS', 'Permissão para acessar o histórico de transações da conta do usuário.'),
       ('WRITE_PAYMENTS', 'Permissão para iniciar pagamentos em nome do usuário.'),
       ('MANAGE_CONSENTS', 'Permissão para criar e revogar consentimentos do usuário.'),
       ('READ_BALANCES', 'Permissão para acessar o saldo atual das contas conectadas.');
