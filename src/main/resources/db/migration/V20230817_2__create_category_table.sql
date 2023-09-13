CREATE TABLE IF NOT EXISTS menuapi.category
(
    idt_category        BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'PK identificador unico',
    idt_menu            BIGINT UNSIGNED NOT NULL COMMENT 'FK da tabela menu',
    des_name            VARCHAR(100)    NOT NULL COMMENT 'Nome da categoria',
    dat_update          TIMESTAMP       NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualizacao do registro',
    dat_creation        TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criacao do registro',

    PRIMARY KEY category_pk(idt_category),
    FOREIGN KEY menu_category_fk (idt_menu) REFERENCES menu (idt_menu)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT 'Tabela para armazenar items'
