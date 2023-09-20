CREATE TABLE IF NOT EXISTS menuapi.menu
(
    idt_menu               BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'PK identificador unico',
    idt_establishment      BIGINT UNSIGNED NOT NULL COMMENT 'PK do estabelecimento',
    idt_customer           BINARY(16)      NOT NULL COMMENT 'PK do customer',
    dat_update             TIMESTAMP       NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualizacao do registro',
    dat_creation           TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criacao do registro',

    PRIMARY KEY menu_pk(idt_menu),
    CONSTRAINT  menuestab_uk UNIQUE (idt_establishment)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT 'Tabela para armazenar menu de um estabelecimento'
