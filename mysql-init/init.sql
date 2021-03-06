DROP DATABASE IF EXISTS `air-company-management-system`;
CREATE SCHEMA `air-company-management-system` DEFAULT CHARACTER SET utf8 ;

GRANT ALL PRIVILEGES ON *.* TO 'testproject'@'%';
GRANT ALL PRIVILEGES ON `air-company-management-system`.* TO 'testproject'@'%';

USE `air-company-management-system`;

#--/*Autogenerated by Spring Data JPA*/
DROP TABLE IF EXISTS `company_types`;
CREATE TABLE `company_types` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `airplane_types`;
CREATE TABLE `airplane_types` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_4cbf4gew84hm1k4rcvkp2tw4x` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `air_companies`;
CREATE TABLE `air_companies` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `founded_at` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `company_type_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_hiqexqpqm1sndbu6it33566u3` (`name`),
  KEY `FKowxyys8d4r9e0ttcnyf2hjpon` (`company_type_id`),
  CONSTRAINT `FKowxyys8d4r9e0ttcnyf2hjpon` FOREIGN KEY (`company_type_id`) REFERENCES `company_types` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `countries`;
CREATE TABLE `countries` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_1pyiwrqimi3hnl3vtgsypj5r` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `flight_status`;
CREATE TABLE `flight_status` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_a90eu13vw9jgg2i8fycbjadi6` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `airplanes`;
CREATE TABLE `airplanes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `factory_serial_number` varchar(255) NOT NULL,
  `flight_distance` double DEFAULT NULL,
  `fuel_capacity` double DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `number_of_flights` int DEFAULT NULL,
  `air_company_id` bigint DEFAULT NULL,
  `airplane_type_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_op6m39hnobht73jexruudjsf0` (`factory_serial_number`),
  KEY `FKsnx5fllu72d38u8e7cghe6ofq` (`air_company_id`),
  KEY `FKawbsoppph0sipvmro60fdnwa2` (`airplane_type_id`),
  CONSTRAINT `FKawbsoppph0sipvmro60fdnwa2` FOREIGN KEY (`airplane_type_id`) REFERENCES `airplane_types` (`id`),
  CONSTRAINT `FKsnx5fllu72d38u8e7cghe6ofq` FOREIGN KEY (`air_company_id`) REFERENCES `air_companies` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `flights`;
CREATE TABLE `flights` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `delay_started_at` datetime DEFAULT NULL,
  `distance` int DEFAULT NULL,
  `ended_at` datetime DEFAULT NULL,
  `estimated_flight_time` varchar(255) DEFAULT NULL,
  `started_at` datetime DEFAULT NULL,
  `air_company_id` bigint DEFAULT NULL,
  `airplane_id` bigint DEFAULT NULL,
  `departure_country_id` bigint DEFAULT NULL,
  `destination_country_id` bigint DEFAULT NULL,
  `status_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4dg00qjfx5f8hw79i6ebdhluu` (`air_company_id`),
  KEY `FKg5m8ip0f64g1nm42q8bf6s5ak` (`airplane_id`),
  KEY `FKdrghh9r7bfsuj437xc1wkbu0u` (`departure_country_id`),
  KEY `FKokn6fcb15s2hbn3odiqrmn6ge` (`destination_country_id`),
  KEY `FKidii0fa16kousyppakiwjlmsf` (`status_id`),
  CONSTRAINT `FK4dg00qjfx5f8hw79i6ebdhluu` FOREIGN KEY (`air_company_id`) REFERENCES `air_companies` (`id`),
  CONSTRAINT `FKdrghh9r7bfsuj437xc1wkbu0u` FOREIGN KEY (`departure_country_id`) REFERENCES `countries` (`id`),
  CONSTRAINT `FKg5m8ip0f64g1nm42q8bf6s5ak` FOREIGN KEY (`airplane_id`) REFERENCES `airplanes` (`id`),
  CONSTRAINT `FKidii0fa16kousyppakiwjlmsf` FOREIGN KEY (`status_id`) REFERENCES `flight_status` (`id`),
  CONSTRAINT `FKokn6fcb15s2hbn3odiqrmn6ge` FOREIGN KEY (`destination_country_id`) REFERENCES `countries` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#--/*Company Type*/
INSERT INTO `air-company-management-system`.`company_types` (`name`) VALUES ('PASSENGER');
INSERT INTO `air-company-management-system`.`company_types` (`name`) VALUES ('CARGO');
#--/*Airplane Type*/
INSERT INTO `air-company-management-system`.`airplane_types` (`name`) VALUES ('SMALL');
INSERT INTO `air-company-management-system`.`airplane_types` (`name`) VALUES ('MEDIUM');
INSERT INTO `air-company-management-system`.`airplane_types` (`name`) VALUES ('LARGE');

#--/*Flight Status*/
INSERT INTO `air-company-management-system`.`flight_status` (`name`) VALUES ('PENDING');
INSERT INTO `air-company-management-system`.`flight_status` (`name`) VALUES ('DELAYED');
INSERT INTO `air-company-management-system`.`flight_status` (`name`) VALUES ('ACTIVE');
INSERT INTO `air-company-management-system`.`flight_status` (`name`) VALUES ('COMPLETED');
#--/*Countries*/
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Afghanistan');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Åland Islands');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Albania');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Algeria');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('American Samoa');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('AndorrA');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Angola');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Anguilla');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Antarctica');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Antigua and Barbuda');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Argentina');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Armenia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Aruba');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Australia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Austria');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Azerbaijan');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Bahamas');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Bahrain');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Bangladesh');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Barbados');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Belarus');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Belgium');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Belize');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Benin');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Bermuda');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Bhutan');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Bolivia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Bosnia and Herzegovina');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Botswana');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Bouvet Island');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Brazil');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('British Indian Ocean Territory');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Brunei Darussalam');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Bulgaria');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Burkina Faso');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Burundi');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Cambodia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Cameroon');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Canada');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Cape Verde');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Cayman Islands');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Central African Republic');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Chad');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Chile');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('China');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Christmas Island');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Cocos (Keeling) Islands');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Colombia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Comoros');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Congo');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Cook Islands');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Costa Rica');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Cote DIvoire');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Croatia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Cuba');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Cyprus');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Czech Republic');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Denmark');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Djibouti');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Dominica');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Dominican Republic');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Ecuador');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Egypt');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('El Salvador');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Equatorial Guinea');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Eritrea');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Estonia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Ethiopia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Falkland Islands (Malvinas)');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Faroe Islands');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Fiji');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Finland');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('France');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('French Guiana');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('French Polynesia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('French Southern Territories');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Gabon');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Gambia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Georgia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Germany');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Ghana');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Gibraltar');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Greece');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Greenland');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Grenada');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Guadeloupe');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Guam');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Guatemala');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Guernsey');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Guinea');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Guinea-Bissau');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Guyana');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Haiti');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Heard Island and Mcdonald Islands');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Holy See (Vatican City State)');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Honduras');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Hong Kong');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Hungary');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Iceland');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('India');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Indonesia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Iran');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Iraq');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Ireland');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Isle of Man');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Israel');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Italy');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Jamaica');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Japan');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Jersey');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Jordan');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Kazakhstan');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Kenya');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Kiribati');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Korea');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Kuwait');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Kyrgyzstan');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Lao PeopleS Democratic Republic');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Latvia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Lebanon');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Lesotho');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Liberia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Libyan Arab Jamahiriya');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Liechtenstein');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Lithuania');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Luxembourg');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Macao');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Macedonia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Madagascar');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Malawi');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Malaysia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Maldives');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Mali');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Malta');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Marshall Islands');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Martinique');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Mauritania');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Mauritius');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Mayotte');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Mexico');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Micronesia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Moldova');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Monaco');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Mongolia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Montserrat');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Morocco');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Mozambique');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Myanmar');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Namibia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Nauru');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Nepal');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Netherlands');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Netherlands Antilles');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('New Caledonia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('New Zealand');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Nicaragua');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Niger');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Nigeria');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Niue');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Norfolk Island');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Northern Mariana Islands');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Norway');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Oman');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Pakistan');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Palau');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Palestinian Territory');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Panama');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Papua New Guinea');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Paraguay');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Peru');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Philippines');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Pitcairn');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Poland');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Portugal');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Puerto Rico');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Qatar');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Reunion');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Romania');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Russian Federation');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('RWANDA');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Saint Helena');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Saint Kitts and Nevis');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Saint Lucia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Saint Pierre and Miquelon');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Saint Vincent and the Grenadines');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Samoa');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('San Marino');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Sao Tome and Principe');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Saudi Arabia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Senegal');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Serbia and Montenegro');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Seychelles');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Sierra Leone');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Singapore');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Slovakia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Slovenia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Solomon Islands');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Somalia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('South Africa');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('South Georgia and the South Sandwich Islands');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Spain');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Sri Lanka');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Sudan');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Suriname');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Svalbard and Jan Mayen');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Swaziland');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Sweden');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Switzerland');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Syrian Arab Republic');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Taiwan');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Tajikistan');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Tanzania');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Thailand');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Timor-Leste');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Togo');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Tokelau');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Tonga');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Trinidad and Tobago');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Tunisia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Turkey');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Turkmenistan');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Turks and Caicos Islands');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Tuvalu');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Uganda');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Ukraine');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('United Arab Emirates');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('United Kingdom');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('United States');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('United States Minor Outlying Islands');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Uruguay');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Uzbekistan');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Vanuatu');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Venezuela');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Viet Nam');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Virgin Islands');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Wallis and Futuna');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Western Sahara');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Yemen');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Zambia');
INSERT INTO `air-company-management-system`.`countries` (`name`) VALUES ('Zimbabwe');


