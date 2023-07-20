CREATE TABLE plans (
  check_plan_id int unsigned AUTO_INCREMENT,
  equipment_id int NOT NULL,
  check_type VARCHAR(10) NOT NULL,
  period VARCHAR(10) NOT NULL,
  deadline DATE,
  PRIMARY KEY(check_plan_id)
);

INSERT INTO plans (check_plan_id, equipment_id, check_type, period, deadline) VALUES (1, 1, "簡易点検", "1年", "2023-09-30");
INSERT INTO plans (check_plan_id, equipment_id, check_type, period, deadline) VALUES (2, 1, "本格点検", "5年", "2026-09-30");
INSERT INTO plans (check_plan_id, equipment_id, check_type, period, deadline) VALUES (3, 2, "簡易点検", "1年", "2023-10-30");
INSERT INTO plans (check_plan_id, equipment_id, check_type, period, deadline) VALUES (4, 2, "本格点検", "4年", "2024-10-30");
INSERT INTO plans (check_plan_id, equipment_id, check_type, period, deadline) VALUES (5, 3, "簡易点検", "1年", "2023-11-30");
INSERT INTO plans (check_plan_id, equipment_id, check_type, period, deadline) VALUES (6, 3, "本格点検", "3年", "2025-11-30");
