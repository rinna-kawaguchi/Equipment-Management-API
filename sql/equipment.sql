CREATE TABLE equipments (
  equipment_id int unsigned AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL,
  number VARCHAR(20) NOT NULL,
  location VARCHAR(20) NOT NULL,
  PRIMARY KEY(equipment_id)
);

INSERT INTO equipments (equipment_id, name, number, location) VALUES (1, "循環ポンプA", "A1-C001A", "Area1");
INSERT INTO equipments (equipment_id, name, number, location) VALUES (2, "循環ポンプB", "A1-C001B", "Area1");
INSERT INTO equipments (equipment_id, name, number, location) VALUES (3, "真空ポンプ", "A2-C001", "Area2");
INSERT INTO equipments (equipment_id, name, number, location) VALUES (4, "吸込弁", "A2-F001", "Area2");
INSERT INTO equipments (equipment_id, name, number, location) VALUES (5, "吐出弁", "A2-F002", "Area2");
INSERT INTO equipments (equipment_id, name, number, location) VALUES (6, "電源盤", "A2-E001", "Area2");
