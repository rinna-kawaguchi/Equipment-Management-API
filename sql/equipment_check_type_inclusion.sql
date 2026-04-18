CREATE TABLE equipment_check_type_inclusions (
  inclusion_id INT UNSIGNED AUTO_INCREMENT,
  equipment_id INT UNSIGNED NOT NULL,
  upper_check_type_id INT UNSIGNED NOT NULL,
  lower_check_type_id INT UNSIGNED NOT NULL,
  PRIMARY KEY(inclusion_id),
  UNIQUE KEY uq_ecti_unique (equipment_id, upper_check_type_id, lower_check_type_id),
  CONSTRAINT fk_ecti_equipment FOREIGN KEY (equipment_id) REFERENCES equipments (equipment_id) ON DELETE CASCADE,
  CONSTRAINT fk_ecti_upper_check_type FOREIGN KEY (upper_check_type_id) REFERENCES check_types (check_type_id) ON DELETE RESTRICT,
  CONSTRAINT fk_ecti_lower_check_type FOREIGN KEY (lower_check_type_id) REFERENCES check_types (check_type_id) ON DELETE RESTRICT,
  CONSTRAINT chk_ecti_no_self_inclusion CHECK (upper_check_type_id <> lower_check_type_id)
);