CREATE TABLE IF NOT EXISTS menuapi.additional_item
(
    idt_additional_item    BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'PK identificador unico',
    idt_menu               BIGINT UNSIGNED NOT NULL COMMENT 'FK da tabela menu',
    des_name               VARCHAR(100)    NOT NULL COMMENT 'Nome do item',
    des_description        VARCHAR(255)    NULL     COMMENT 'Descricao do item',
    des_price              DECIMAL(9,2)    NOT NULL COMMENT 'Preco do produto',
    dat_update             TIMESTAMP       NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualizacao do registro',
    dat_creation           TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criacao do registro',

    PRIMARY KEY add_item_pk(idt_additional_item),
    FOREIGN KEY menu_additem_fk (idt_menu) REFERENCES menu (idt_menu)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT 'Tabela para armazenar items adicionais'
