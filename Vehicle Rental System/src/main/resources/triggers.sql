DELIMITER $$
CREATE TRIGGER `vehicle_before_update` BEFORE UPDATE ON `vehicle` FOR EACH ROW BEGIN
if old.version <> new.version THEN
signal sqlstate '45000' set message_text = 'The Update has not been successful. Please try again with latest data.';
end if;
   SET NEW.version = NEW.version + 1;
END
$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER `client_before_update` BEFORE UPDATE ON `client_record` FOR EACH ROW BEGIN
if old.version <> new.version THEN
signal sqlstate '45000' set message_text = 'The Update has not been successful. Please try again with latest data.';
end if;
   SET NEW.version = NEW.version + 1;
END
$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER `reservation_before_update` BEFORE UPDATE ON `reservation` FOR EACH ROW BEGIN
if old.version <> new.version THEN
signal sqlstate '45000' set message_text = 'The Update has not been successful. Please try again with latest data.';
end if;
   SET NEW.version = NEW.version + 1;
END
$$
DELIMITER ;

