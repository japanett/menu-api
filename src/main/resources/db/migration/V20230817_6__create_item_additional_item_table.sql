CREATE TABLE IF NOT EXISTS menuapi.item_additional_item
(
    idt_item                 BIGINT UNSIGNED NOT NULL COMMENT 'PK identificador unico',
    idt_additional_item      BIGINT UNSIGNED NOT NULL COMMENT 'PK identificador unico',

    PRIMARY KEY item_additem_pk(idt_item, idt_additional_item)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT 'Tabela para armazenar relacionamento bidirecional de items e adicionais'
