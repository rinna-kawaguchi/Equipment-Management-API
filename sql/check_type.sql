CREATE TABLE check_types (
  check_type_id INT UNSIGNED AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL,
  PRIMARY KEY(check_type_id),
  UNIQUE KEY uq_check_types_name (name)
);

INSERT INTO check_types (check_type_id, name) VALUES (1, '簡易点検');
INSERT INTO check_types (check_type_id, name) VALUES (2, '本格点検');
INSERT INTO check_types (check_type_id, name) VALUES (3, '漏洩確認');
INSERT INTO check_types (check_type_id, name) VALUES (4, '分解点検');
INSERT INTO check_types (check_type_id, name) VALUES (5, '取替');
