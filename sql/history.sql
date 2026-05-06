CREATE TABLE histories (
  check_history_id int unsigned AUTO_INCREMENT,
  equipment_id INT UNSIGNED NOT NULL,
  check_type_id INT UNSIGNED NOT NULL,
  implementation_date DATE NOT NULL,
  result VARCHAR(50) NOT NULL,
  PRIMARY KEY(check_history_id),
  CONSTRAINT fk_histories_equipment FOREIGN KEY (equipment_id) REFERENCES equipments (equipment_id) ON DELETE CASCADE,
  CONSTRAINT fk_histories_check_type FOREIGN KEY (check_type_id) REFERENCES check_types (check_type_id) ON DELETE RESTRICT
);

INSERT INTO histories (check_history_id, equipment_id, check_type_id, implementation_date, result) VALUES (1, 1, 1, "2020-09-03", "補修塗装を実施");
INSERT INTO histories (check_history_id, equipment_id, check_type_id, implementation_date, result) VALUES (2, 1, 2, "2021-09-03", "ケーシングの腐食が進んでいたため交換を実施");
INSERT INTO histories (check_history_id, equipment_id, check_type_id, implementation_date, result) VALUES (3, 1, 1, "2022-09-03", "良");
INSERT INTO histories (check_history_id, equipment_id, check_type_id, implementation_date, result) VALUES (4, 2, 1, "2021-10-23", "良");
INSERT INTO histories (check_history_id, equipment_id, check_type_id, implementation_date, result) VALUES (5, 2, 2, "2022-10-23", "良");
INSERT INTO histories (check_history_id, equipment_id, check_type_id, implementation_date, result) VALUES (6, 4, 3, "2021-12-09", "良");
INSERT INTO histories (check_history_id, equipment_id, check_type_id, implementation_date, result) VALUES (7, 4, 4, "2022-12-09", "良");
INSERT INTO histories (check_history_id, equipment_id, check_type_id, implementation_date, result) VALUES (8, 5, 3, "2023-03-09", "良");
