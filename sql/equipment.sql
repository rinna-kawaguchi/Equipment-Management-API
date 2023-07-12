CREATE TABLE equipments (
  equipment_id int unsigned AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL,
  number VARCHAR(20) NOT NULL,
  location VARCHAR(20) NOT NULL,
  PRIMARY KEY(equipment_id)
);

INSERT INTO equipments (equipment_id, name, number, location) VALUES (1, "真空ポンプA", "A1-C001A", "Area1");
INSERT INTO equipments (equipment_id, name, number, location) VALUES (2, "吸込ポンプB", "A2-C002B", "Area2");
INSERT INTO equipments (equipment_id, name, number, location) VALUES (3, "吐出ポンプC", "A3-C003C", "Area3");
