CREATE TABLE histories (
  check_history_id int unsigned AUTO_INCREMENT,
  equipment_id int NOT NULL,
  implementation_date DATE NOT NULL,
  check_type VARCHAR(10) NOT NULL,
  result VARCHAR(50) NOT NULL,
  PRIMARY KEY(check_history_id)
);

INSERT INTO histories (check_history_id, equipment_id, implementation_date, check_type, result)
VALUES (1, 1, "2022-09-30", "簡易点検", "良");
INSERT INTO histories (check_history_id, equipment_id, implementation_date, check_type, result)
VALUES (2, 1, "2021-09-30", "本格点検", "良");
INSERT INTO histories (check_history_id, equipment_id, implementation_date, check_type, result)
VALUES (3, 2, "2022-10-30", "簡易点検", "良");
INSERT INTO histories (check_history_id, equipment_id, implementation_date, check_type, result)
VALUES (4, 2, "2020-10-30", "本格点検", "良");
INSERT INTO histories (check_history_id, equipment_id, implementation_date, check_type, result)
VALUES (5, 3, "2021-11-30", "簡易点検", "良");
INSERT INTO histories (check_history_id, equipment_id, implementation_date, check_type, result)
VALUES (6, 3, "2020-11-30", "簡易点検", "補修塗装を実施");
INSERT INTO histories (check_history_id, equipment_id, implementation_date, check_type, result)
VALUES (7, 3, "2022-11-30", "本格点検", "ケーシングの腐食が進んでいたため交換を実施");
INSERT INTO histories (check_history_id, equipment_id, implementation_date, check_type, result)
VALUES (8, 3, "2019-11-30", "本格点検", "良");
