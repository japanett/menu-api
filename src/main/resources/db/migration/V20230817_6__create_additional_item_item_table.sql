CREATE TABLE IF NOT EXISTS menuapi.additional_item_item
(
    item_id                 BIGINT UNSIGNED NOT NULL COMMENT 'PK identificador unico',
    additional_item_id      BIGINT UNSIGNED NOT NULL COMMENT 'PK identificador unico',

    PRIMARY KEY item_pk(item_id, additional_item_id)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT 'Tabela para armazenar items'
