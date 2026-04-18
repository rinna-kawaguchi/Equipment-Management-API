CREATE TABLE equipment_check_type_settings (
  setting_id INT UNSIGNED AUTO_INCREMENT,
  equipment_id INT UNSIGNED NOT NULL,
  check_type_id INT UNSIGNED NOT NULL,
  period_value SMALLINT UNSIGNED NOT NULL,
  period_unit ENUM('day', 'week', 'month', 'year') NOT NULL,
  PRIMARY KEY(setting_id),
  UNIQUE KEY uq_ects_equipment_checktype (equipment_id, check_type_id),
  CONSTRAINT fk_ects_equipment FOREIGN KEY (equipment_id) REFERENCES equipments (equipment_id) ON DELETE CASCADE,
  CONSTRAINT fk_ects_check_type FOREIGN KEY (check_type_id) REFERENCES check_types (check_type_id) ON DELETE RESTRICT
);

INSERT INTO equipment_check_type_settings (equipment_id, check_type_id, period_value, period_unit) VALUES (1, 1, 1, 'year');
INSERT INTO equipment_check_type_settings (equipment_id, check_type_id, period_value, period_unit) VALUES (1, 2, 5, 'year');
INSERT INTO equipment_check_type_settings (equipment_id, check_type_id, period_value, period_unit) VALUES (2, 1, 1, 'year');
INSERT INTO equipment_check_type_settings (equipment_id, check_type_id, period_value, period_unit) VALUES (2, 2, 5, 'year');
INSERT INTO equipment_check_type_settings (equipment_id, check_type_id, period_value, period_unit) VALUES (4, 3, 1, 'year');
INSERT INTO equipment_check_type_settings (equipment_id, check_type_id, period_value, period_unit) VALUES (4, 4, 10, 'year');
INSERT INTO equipment_check_type_settings (equipment_id, check_type_id, period_value, period_unit) VALUES (5, 3, 1, 'year');
INSERT INTO equipment_check_type_settings (equipment_id, check_type_id, period_value, period_unit) VALUES (5, 4, 10, 'year');
INSERT INTO equipment_check_type_settings (equipment_id, check_type_id, period_value, period_unit) VALUES (6, 5, 20, 'year');