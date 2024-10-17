CREATE TABLE `auto_accidents` (
  `id` int(11) NOT NULL,
  `type` int(11) DEFAULT NULL,
  `status` enum('Open','Closed') DEFAULT NULL,
  `dept_id` int(11) DEFAULT NULL,
  `deptPhone` varchar(30) DEFAULT NULL,
  `accidDate` date DEFAULT NULL,
  `accidTime` varchar(20) DEFAULT NULL,
  `reported` date DEFAULT NULL,
  `accidLocation` varchar(100) DEFAULT NULL,
  `empInjured` char(1) DEFAULT NULL,
  `damage` text,
  `totalCost` double DEFAULT NULL,
  `autoDamage` char(1) DEFAULT NULL,
  `autoPaid` char(1) DEFAULT NULL,
  `totalCostP` double DEFAULT NULL,
  `propDamage` char(1) DEFAULT NULL,
  `propPaid` char(1) DEFAULT NULL,
  `subToInsur` char(1) DEFAULT NULL,
  `workComp` char(1) DEFAULT NULL,
  `whatProp` varchar(50) DEFAULT NULL,
  `repairInfo` varchar(500) DEFAULT NULL,
  `otherType` varchar(35) DEFAULT NULL,
  `paidByCity` double DEFAULT '0',
  `paidByInsur` double DEFAULT '0',
  `miscByCity` double DEFAULT '0',
  `paidByRisk` double DEFAULT '0',
  `deptContact` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
;;
;;
CREATE TABLE `citydept` (
  `dept` mediumint(9) NOT NULL AUTO_INCREMENT,
  `deptName` varchar(30) NOT NULL,
  `busCat` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`dept`)
) ENGINE=InnoDB;
;;
;;
 CREATE TABLE `claim_auto_people` (
  `person_id` int(11) NOT NULL,
  `risk_id` int(11) NOT NULL,
  KEY `person_id` (`person_id`),
  KEY `risk_id` (`risk_id`),
  CONSTRAINT `claim_auto_people_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `people` (`id`),
  CONSTRAINT `claim_auto_people_ibfk_2` FOREIGN KEY (`risk_id`) REFERENCES `risk_sequences` (`id`)
) ENGINE=InnoDB;
;;
;;
CREATE TABLE `claim_people` (
  `person_id` int(11) DEFAULT NULL,
  `risk_id` int(11) DEFAULT NULL,
  KEY `person_id` (`person_id`),
  KEY `risk_id` (`risk_id`),
  CONSTRAINT `claim_people_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `people` (`id`),
  CONSTRAINT `claim_people_ibfk_2` FOREIGN KEY (`risk_id`) REFERENCES `risk_sequences` (`id`)
) ENGINE=InnoDB;
;;
;;
CREATE TABLE `departments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `division` varchar(30) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
;;
CREATE TABLE `deptrelated` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dept_id` int(11) NOT NULL,
  `related_id` int(11) NOT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `supervisor` varchar(70) DEFAULT NULL,
  `type` enum('Tort','Legal') DEFAULT 'Tort',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
;;
;;
CREATE TABLE `disaster_emps` (
  `empid` int(11) NOT NULL,
  `risk_id` int(11) NOT NULL,
  PRIMARY KEY (`empid`,`risk_id`),
  KEY `risk_id` (`risk_id`)
) ENGINE=InnoDB;
;;
;;
 CREATE TABLE `disaster_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
;;
;;
CREATE TABLE `disasters` (
  `id` int(11) NOT NULL,
  `type` int(11) DEFAULT NULL,
  `status` enum('Open','Closed') DEFAULT NULL,
  `dept_id` int(11) DEFAULT NULL,
  `deptPhone` varchar(30) DEFAULT NULL,
  `accidDate` date DEFAULT NULL,
  `accidTime` varchar(20) DEFAULT NULL,
  `reported` date DEFAULT NULL,
  `accidLocation` varchar(100) DEFAULT NULL,
  `empInjured` char(1) DEFAULT NULL,
  `damage` varchar(500) DEFAULT NULL,
  `totalCost` double DEFAULT NULL,
  `autoDamage` char(1) DEFAULT NULL,
  `autoPaid` char(1) DEFAULT NULL,
  `totalCostP` double DEFAULT NULL,
  `propDamage` char(1) DEFAULT NULL,
  `propPaid` char(1) DEFAULT NULL,
  `subToInsur` char(1) DEFAULT NULL,
  `workComp` char(1) DEFAULT NULL,
  `whatProp` varchar(50) DEFAULT NULL,
  `repairInfo` varchar(500) DEFAULT NULL,
  `otherType` varchar(35) DEFAULT NULL,
  `paidByCity` double DEFAULT '0',
  `paidByInsur` double DEFAULT '0',
  `miscByCity` double DEFAULT '0',
  `paidByRisk` double DEFAULT '0',
  `insur_id` int(11) DEFAULT NULL,
  `deptContact` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
;;
CREATE TABLE `employees` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` varchar(50) DEFAULT NULL,
  `name` varchar(30) DEFAULT NULL,
  `title` varchar(70) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `dept_id` int(11) DEFAULT NULL,
  `deptPhone` varchar(30) DEFAULT NULL,
  `supervisor` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
;;
CREATE TABLE `emprelated` (
  `employee_id` int(11) NOT NULL DEFAULT '0',
  `risk_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`employee_id`,`risk_id`),
  CONSTRAINT `empRelated_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`)
) ENGINE=InnoDB;
;;
CREATE TABLE `insurancerelated` (
  `insurance_id` int(11) NOT NULL DEFAULT '0',
  `risk_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`insurance_id`,`risk_id`),
  CONSTRAINT `insuranceRelated_ibfk_1` FOREIGN KEY (`insurance_id`) REFERENCES `insurances` (`id`)
) ENGINE=InnoDB ;
;;
CREATE TABLE `insurances` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company` varchar(50) DEFAULT NULL,
  `status` enum('','Pending','Accepted','Denied','Closed') DEFAULT NULL,
  `adjuster` varchar(50) DEFAULT NULL,
  `adjusterPhone` varchar(30) DEFAULT NULL,
  `adjusterEmail` varchar(50) DEFAULT NULL,
  `deductible` double DEFAULT '0',
  `claimNum` varchar(30) DEFAULT NULL,
  `policy` enum('Auto','Gen','Prop','Med','B.I.') DEFAULT NULL,
  `amountPaid` double DEFAULT NULL,
  `attorney` varchar(50) DEFAULT NULL,
  `attorneyPhone` varchar(30) DEFAULT NULL,
  `attorneyEmail` varchar(50) DEFAULT NULL,
  `sent` date DEFAULT NULL,
  `decisionDate` date DEFAULT NULL,
  `type` enum('City','Defendant','Claimant') DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `policy_num` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
;;
;;
CREATE TABLE `law_firms` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `address` varchar(80) DEFAULT NULL,
  `contact` varchar(80) DEFAULT NULL,
  `phones` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;
;;
;;
CREATE TABLE `legalrelated` (
  `id` int(11) NOT NULL,
  `id2` int(11) NOT NULL,
  `type` enum('Tort Claim','Recovery Action','Internal Accident','Worker Comp','Natural Disaster Accident','Auto Accident') DEFAULT NULL,
  `type2` enum('Tort Claim','Recovery Action','Internal Accident','Worker Comp','Natural Disaster Accident','Auto Accident') DEFAULT NULL,
  PRIMARY KEY (`id`,`id2`)
) ENGINE=InnoDB;
;;
 CREATE TABLE `legaltorts` (
  `lid` int(11) NOT NULL,
  `tid` int(11) NOT NULL,
  PRIMARY KEY (`lid`,`tid`)
) ENGINE=InnoDB;
;;
;;
 CREATE TABLE `misc_accident_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
;;
;;
CREATE TABLE `misc_accidents` (
  `id` int(11) NOT NULL,
  `type` int(11) DEFAULT NULL,
  `status` enum('Open','Closed') DEFAULT NULL,
  `dept_id` int(11) DEFAULT NULL,
  `deptPhone` varchar(30) DEFAULT NULL,
  `accidDate` date DEFAULT NULL,
  `accidTime` varchar(20) DEFAULT NULL,
  `reported` date DEFAULT NULL,
  `accidLocation` varchar(100) DEFAULT NULL,
  `empInjured` char(1) DEFAULT NULL,
  `damage` varchar(500) DEFAULT NULL,
  `totalCost` double DEFAULT NULL,
  `autoDamage` char(1) DEFAULT NULL,
  `autoPaid` char(1) DEFAULT NULL,
  `totalCostP` double DEFAULT NULL,
  `propDamage` char(1) DEFAULT NULL,
  `propPaid` char(1) DEFAULT NULL,
  `subToInsur` char(1) DEFAULT NULL,
  `workComp` char(1) DEFAULT NULL,
  `whatProp` varchar(50) DEFAULT NULL,
  `repairInfo` varchar(500) DEFAULT NULL,
  `otherType` varchar(35) DEFAULT NULL,
  `paidByCity` double DEFAULT '0',
  `paidByInsur` double DEFAULT '0',
  `miscByCity` double DEFAULT '0',
  `paidByRisk` double DEFAULT '0',
  `deptContact` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;
;;
;;
CREATE TABLE `notes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `risk_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `date` date DEFAULT NULL,
  `note_text` varchar(1028) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `notes_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB;
;;
 CREATE TABLE `notification_logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `receiver` varchar(160) DEFAULT NULL,
  `message` varchar(1024) DEFAULT NULL,
  `process_date` date DEFAULT NULL,
  `error_msg` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB ;
;;
CREATE TABLE `payments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `risk_id` int(11) DEFAULT NULL,
  `paidDate` date NOT NULL,
  `amount` decimal(12,2) DEFAULT NULL,
  `paidBy` varchar(50) DEFAULT NULL,
  `paidMethod` enum('Cash','Check','M.O.','C.C.','B.C.','Other') DEFAULT NULL,
  `checkNo` varchar(15) DEFAULT NULL,
  `receiptDate` date DEFAULT NULL,
  `clerk` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
;;
;;
CREATE TABLE `people` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lname` varchar(50) DEFAULT NULL,
  `fname` varchar(30) DEFAULT NULL,
  `streetNum` varchar(10) DEFAULT NULL,
  `streetDir` varchar(2) DEFAULT NULL,
  `streetName` varchar(50) DEFAULT NULL,
  `streetType` varchar(4) DEFAULT NULL,
  `sudType` varchar(4) DEFAULT NULL,
  `sudNum` varchar(20) DEFAULT NULL,
  `city` varchar(30) DEFAULT NULL,
  `state` varchar(2) DEFAULT NULL,
  `zip` varchar(12) DEFAULT NULL,
  `phoneh` varchar(30) DEFAULT NULL,
  `phonew` varchar(30) DEFAULT NULL,
  `phonec` varchar(30) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `ssn` varchar(12) DEFAULT NULL,
  `addrUpdate` date DEFAULT NULL,
  `mi` varchar(5) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `nameTitle` enum('','Mr.','Mrs.','Ms.') DEFAULT '',
  `contact` varchar(70) DEFAULT NULL,
  `juvenile` varchar(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
;;
;;
CREATE TABLE `risk_autos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `risk_id` int(11) DEFAULT NULL,
  `vin` varchar(20) DEFAULT NULL,
  `autoPlate` varchar(20) DEFAULT NULL,
  `autoMake` varchar(20) DEFAULT NULL,
  `autoModel` varchar(20) DEFAULT NULL,
  `autoYear` varchar(4) DEFAULT NULL,
  `autoNum` varchar(10) DEFAULT NULL,
  `owner` enum('City','Citizen') DEFAULT 'City',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
;;
;;
CREATE TABLE `risk_estimates` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `risk_id` int(11) DEFAULT NULL,
  `type` enum('Auto','Prop') DEFAULT NULL,
  `company` varchar(50) DEFAULT NULL,
  `cost` double DEFAULT NULL,
  `chosen` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `risk_id` (`risk_id`)
) ENGINE=InnoDB;
;;
;;
 CREATE TABLE `risk_file_seq` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
;;
;;
 CREATE TABLE `risk_files` (
  `id` int(11) NOT NULL,
  `risk_id` int(11) NOT NULL,
  `added_by_id` int(11) NOT NULL,
  `date` date DEFAULT NULL,
  `name` varchar(70) NOT NULL,
  `old_name` varchar(255) NOT NULL,
  `notes` varchar(1024) DEFAULT NULL,
  UNIQUE KEY `id` (`id`),
  KEY `added_by_id` (`added_by_id`),
  CONSTRAINT `risk_files_ibfk_1` FOREIGN KEY (`added_by_id`) REFERENCES `users` (`id`),
  CONSTRAINT `risk_files_ibfk_2` FOREIGN KEY (`id`) REFERENCES `risk_file_seq` (`id`)
) ENGINE=InnoDB;
;;
;;
CREATE TABLE `risk_sequences` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` enum('claim','legal','wcomp','safety','disaster','miscAccident','autoAccident') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
;;
CREATE TABLE `riskclaimtypes` (
  `type` varchar(30) NOT NULL,
  UNIQUE KEY `type` (`type`)
) ENGINE=InnoDB ;
;;
;;
CREATE TABLE `risklegaltypes` (
  `type` varchar(30) NOT NULL,
  UNIQUE KEY `type` (`type`)
) ENGINE=MyISAM;
;;
CREATE TABLE `risksafety` (
  `id` int(11) NOT NULL,
  `type` varchar(30) DEFAULT NULL,
  `status` enum('Open','Closed') DEFAULT NULL,
  `accidDate` date DEFAULT NULL,
  `accidTime` varchar(20) DEFAULT NULL,
  `reported` date DEFAULT NULL,
  `accidLocation` varchar(100) DEFAULT NULL,
  `damage` varchar(500) DEFAULT NULL,
  `estPlace` varchar(50) DEFAULT NULL,
  `estCost` double DEFAULT NULL,
  `estPlace2` varchar(50) DEFAULT NULL,
  `estCost2` double DEFAULT NULL,
  `estPlace3` varchar(50) DEFAULT NULL,
  `estCost3` double DEFAULT NULL,
  `chosenDealer` varchar(5) DEFAULT NULL,
  `totalCost` double DEFAULT NULL,
  `autoDamage` char(1) DEFAULT NULL,
  `autoPaid` char(1) DEFAULT NULL,
  `estPlaceP` varchar(50) DEFAULT NULL,
  `estCostP` double DEFAULT NULL,
  `estPlaceP2` varchar(50) DEFAULT NULL,
  `estCostP2` double DEFAULT NULL,
  `estPlaceP3` varchar(50) DEFAULT NULL,
  `estCostP3` double DEFAULT NULL,
  `chosenDealerP` varchar(5) DEFAULT NULL,
  `totalCostP` double DEFAULT NULL,
  `propDamage` char(1) DEFAULT NULL,
  `propPaid` char(1) DEFAULT NULL,
  `subToInsur` char(1) DEFAULT NULL,
  `empInjured` char(1) DEFAULT NULL,
  `workComp` char(1) DEFAULT NULL,
  `whatProp` varchar(50) DEFAULT NULL,
  `repairInfo` varchar(500) DEFAULT NULL,
  `deductible` double DEFAULT '0',
  `otherType` varchar(35) DEFAULT NULL,
  `paidByCity` double DEFAULT '0',
  `paidByInsur` double DEFAULT '0',
  `miscByCity` double DEFAULT '0',
  `recordOnly` char(1) DEFAULT NULL,
  `paidByRisk` double DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
;;
;;
CREATE TABLE `risksafetytypes` (
  `type` varchar(30) NOT NULL,
  UNIQUE KEY `type` (`type`)
) ENGINE=InnoDB;
;;
;;
 CREATE TABLE `riskunifiedtypes` (
  `type` int(11) NOT NULL,
  `typeDesc` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`type`)
) ENGINE=InnoDB;
;;
;;
CREATE TABLE `tortclaims` (
  `id` int(11) NOT NULL,
  `type` varchar(30) DEFAULT NULL,
  `status` enum('Open','Closed') NOT NULL,
  `deductible` double DEFAULT NULL,
  `paidByCity` double DEFAULT NULL,
  `paidByInsur` double DEFAULT NULL,
  `requestAmount` double DEFAULT NULL,
  `settled` double DEFAULT NULL,
  `miscByCity` double DEFAULT NULL,
  `cityAutoInc` char(1) DEFAULT NULL,
  `incidentDate` date DEFAULT NULL,
  `incident` varchar(1000) DEFAULT NULL,
  `comments` varchar(1000) DEFAULT NULL,
  `opened` date DEFAULT NULL,
  `received` date DEFAULT NULL,
  `closed` date DEFAULT NULL,
  `filed` char(1) DEFAULT NULL,
  `subInsur` char(1) DEFAULT NULL,
  `expires` date DEFAULT NULL,
  `cityTotalCost` double DEFAULT NULL,
  `paidByCity2City` double DEFAULT NULL,
  `paidByInsur2City` double DEFAULT NULL,
  `deductible2` double DEFAULT NULL,
  `otherType` varchar(35) DEFAULT NULL,
  `recordOnly` char(1) DEFAULT NULL,
  `paidByRisk` double DEFAULT '0',
  `law_firm_id` int(11) DEFAULT NULL,
  `denialLetterDate` date DEFAULT NULL,
  `deadlineDate` date DEFAULT NULL,
  `lawsuit` char(1) DEFAULT NULL,
  `bodilyInvolved` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
;;
 CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` varchar(70) DEFAULT NULL,
  `name` varchar(70) DEFAULT NULL,
  `role` enum('Edit','Admin','Admin:Edit') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `userid` (`userid`)
) ENGINE=InnoDB;
;;
;;
 CREATE TABLE `vslegal_people` (
  `person_id` int(11) DEFAULT NULL,
  `risk_id` int(11) DEFAULT NULL,
  KEY `person_id` (`person_id`),
  KEY `risk_id` (`risk_id`),
  CONSTRAINT `vslegal_people_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `people` (`id`),
  CONSTRAINT `vslegal_people_ibfk_2` FOREIGN KEY (`risk_id`) REFERENCES `vslegals` (`id`)
) ENGINE=InnoDB;
;;
;;
 CREATE TABLE `vslegals` (
  `id` int(11) NOT NULL,
  `type` varchar(30) DEFAULT NULL,
  `status` enum('Open','Pending','Court','Closed') DEFAULT NULL,
  `caseNum` varchar(50) DEFAULT NULL,
  `location` varchar(100) DEFAULT NULL,
  `damageAmnt` double DEFAULT '0',
  `cityAutoInc` char(1) DEFAULT NULL,
  `doi` date DEFAULT NULL,
  `filed` date DEFAULT NULL,
  `judgment` date DEFAULT NULL,
  `proSupp` date DEFAULT NULL,
  `closed` date DEFAULT NULL,
  `insured` char(1) DEFAULT NULL,
  `description` varchar(2056) DEFAULT NULL,
  `allDocuments` varchar(500) DEFAULT NULL,
  `deptRecoverDate` date DEFAULT NULL,
  `deptCollectDate` date DEFAULT NULL,
  `deptToRisk` char(1) DEFAULT NULL,
  `deptToRiskDate` date DEFAULT NULL,
  `riskFirstDate` date DEFAULT NULL,
  `toProsecutorDate` date DEFAULT NULL,
  `convictionDate` date DEFAULT NULL,
  `collectDate` date DEFAULT NULL,
  `riskToInsur` char(1) DEFAULT NULL,
  `riskToInsurDate` date DEFAULT NULL,
  `insurRecoveryDate` date DEFAULT NULL,
  `insurCollectDate` date DEFAULT NULL,
  `deductible` double DEFAULT '0',
  `otherDetails` varchar(2000) DEFAULT NULL,
  `otherType` varchar(35) DEFAULT NULL,
  `paidByCity` double DEFAULT '0',
  `paidByInsur` double DEFAULT '0',
  `miscByCity` double DEFAULT '0',
  `recordOnly` char(1) DEFAULT NULL,
  `paidByRisk` double DEFAULT '0',
  `paidByDef` double DEFAULT '0',
  `unableToCollect` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
;;
;;
CREATE TABLE `witness_people` (
  `person_id` int(11) DEFAULT NULL,
  `risk_id` int(11) DEFAULT NULL,
  KEY `person_id` (`person_id`),
  CONSTRAINT `witness_people_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `people` (`id`)
) ENGINE=InnoDB;
;;
CREATE TABLE `workercomps` (
  `id` int(11) NOT NULL,
  `status` enum('Open','Closed') DEFAULT NULL,
  `empPhone` varchar(12) DEFAULT NULL,
  `accidentDate` date DEFAULT NULL,
  `injuryType` varchar(80) DEFAULT NULL,
  `compensable` enum('','Yes','No','Disputed') DEFAULT NULL,
  `timeOffWork` varchar(30) DEFAULT NULL,
  `payTtd` double DEFAULT NULL,
  `payPpi` double DEFAULT NULL,
  `payMed` double DEFAULT NULL,
  `mmi` char(1) DEFAULT NULL,
  `ableBackWork` enum('','Yes','Yes w/Restrictions','No') DEFAULT NULL,
  `disputeAmount` double DEFAULT NULL,
  `disputeReason` varchar(500) DEFAULT NULL,
  `disputeTypeTtd` char(1) DEFAULT NULL,
  `disputeTypePpi` char(1) DEFAULT NULL,
  `disputeTypeMed` char(1) DEFAULT NULL,
  `back2WorkDate` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
;;
alter table auto_accidents add outOfDuty char(1);
alter table vslegals add outOfDuty char(1);
alter table misc_accidents add outOfDuty char(1);
alter table risksafety add outOfDuty char(1);

