SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `dsauer_bp_2` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
USE `dsauer_bp_2`;

-- -----------------------------------------------------
-- Table `dsauer_bp_2`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dsauer_bp_2`.`user` ;

CREATE  TABLE IF NOT EXISTS `dsauer_bp_2`.`user` (
  `idUser` INT NOT NULL AUTO_INCREMENT ,
  `ime` VARCHAR(20) NULL ,
  `prezime` VARCHAR(30) NULL ,
  `email` VARCHAR(50) NULL ,
  `korIme` VARCHAR(20) NULL ,
  `lozinka` VARCHAR(30) NOT NULL ,
  `stanjeRac` DOUBLE NULL ,
  PRIMARY KEY (`idUser`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


-- -----------------------------------------------------
-- Table `dsauer_bp_2`.`log`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dsauer_bp_2`.`log` ;

CREATE  TABLE IF NOT EXISTS `dsauer_bp_2`.`log` (
  `idUser` INT NOT NULL ,
  `datum` DATETIME NULL ,
  `url` VARCHAR(100) NULL ,
  `zahtjev` VARCHAR(200) NULL ,
  INDEX `fk_log_user` (`idUser` ASC) ,
  CONSTRAINT `fk_log_user`
    FOREIGN KEY (`idUser` )
    REFERENCES `dsauer_bp_2`.`user` (`idUser` )
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
