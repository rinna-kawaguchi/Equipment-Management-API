CREATE TABLE plans (
  check_plan_id int unsigned AUTO_INCREMENT,
  equipment_id INT UNSIGNED NOT NULL,
  check_type_id INT UNSIGNED NOT NULL,
  period_value SMALLINT UNSIGNED NOT NULL,
  period_unit ENUM('day', 'week', 'month', 'year') NOT NULL,
  deadline DATE,
  is_manual_override TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY(check_plan_id),
  UNIQUE KEY uq_plans_equipment_checktype (equipment_id, check_type_id),
  CONSTRAINT fk_plans_equipment FOREIGN KEY (equipment_id) REFERENCES equipments (equipment_id) ON DELETE CASCADE,
  CONSTRAINT fk_plans_check_type FOREIGN KEY (check_type_id) REFERENCES check_types (check_type_id) ON DELETE RESTRICT
);

INSERT INTO plans (check_plan_id, equipment_id, check_type_id, period_value, period_unit, deadline) VALUES (1, 1, 1, 1, 'year', "2023-09-03");
INSERT INTO plans (check_plan_id, equipment_id, check_type_id, period_value, period_unit, deadline) VALUES (2, 1, 2, 5, 'year', "2026-09-03");
INSERT INTO plans (check_plan_id, equipment_id, check_type_id, period_value, period_unit, deadline) VALUES (3, 2, 1, 1, 'year', "2023-10-23");
INSERT INTO plans (check_plan_id, equipment_id, check_type_id, period_value, period_unit, deadline) VALUES (4, 2, 2, 5, 'year', "2027-10-23");
INSERT INTO plans (check_plan_id, equipment_id, check_type_id, period_value, period_unit, deadline) VALUES (5, 4, 3, 1, 'year', "2023-12-09");
INSERT INTO plans (check_plan_id, equipment_id, check_type_id, period_value, period_unit, deadline) VALUES (6, 4, 4, 10, 'year', "2032-12-09");
INSERT INTO plans (check_plan_id, equipment_id, check_type_id, period_value, period_unit, deadline) VALUES (7, 5, 3, 1, 'year', "2024-03-09");
INSERT INTO plans (check_plan_id, equipment_id, check_type_id, period_value, period_unit, deadline) VALUES (8, 5, 4, 10, 'year', "2025-03-09");
INSERT INTO plans (check_plan_id, equipment_id, check_type_id, period_value, period_unit, deadline) VALUES (9, 6, 5, 20, 'year', "2030-01-30");