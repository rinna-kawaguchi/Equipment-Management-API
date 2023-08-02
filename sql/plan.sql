CREATE TABLE plans (
  check_plan_id int unsigned AUTO_INCREMENT,
  equipment_id int NOT NULL,
  check_type VARCHAR(10) NOT NULL,
  period VARCHAR(10) NOT NULL,
  deadline DATE,
  PRIMARY KEY(check_plan_id)
);

INSERT INTO plans (check_plan_id, equipment_id, check_type, period, deadline) VALUES (1, 1, "簡易点検", "1年", "2023-09-03");
INSERT INTO plans (check_plan_id, equipment_id, check_type, period, deadline) VALUES (2, 1, "本格点検", "5年", "2026-09-03");
INSERT INTO plans (check_plan_id, equipment_id, check_type, period, deadline) VALUES (3, 2, "簡易点検", "1年", "2023-10-23");
INSERT INTO plans (check_plan_id, equipment_id, check_type, period, deadline) VALUES (4, 2, "本格点検", "5年", "2027-10-23");
INSERT INTO plans (check_plan_id, equipment_id, check_type, period, deadline) VALUES (5, 4, "漏洩確認", "1年", "2023-12-09")
;
INSERT INTO plans (check_plan_id, equipment_id, check_type, period, deadline) VALUES (6, 4, "分解点検", "10年", "2032-12-09")
;
INSERT INTO plans (check_plan_id, equipment_id, check_type, period, deadline) VALUES (7, 5, "漏洩確認", "1年", "2024-03-09")
;
INSERT INTO plans (check_plan_id, equipment_id, check_type, period, deadline) VALUES (8, 5, "分解点検", "10年", "2025-03-09")
;
INSERT INTO plans (check_plan_id, equipment_id, check_type, period, deadline) VALUES (9, 6, "取替", "20年", "2030-01-30");
