/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 5.7.27 : Database - alb1183_tfg
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `token` */

DROP TABLE IF EXISTS `token`;

CREATE TABLE `token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` varchar(30) NOT NULL,
  `token` varchar(80) NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `userFK` (`user`),
  CONSTRAINT `userFK` FOREIGN KEY (`user`) REFERENCES `usuario` (`user`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=latin1;

/*Data for the table `token` */

insert  into `token`(`id`,`user`,`token`,`date`) values 
(35,'alberto','55eb9d5a-da6d-4956-bd30-ce3491024672','2019-08-05 12:23:55');
insert  into `token`(`id`,`user`,`token`,`date`) values 
(39,'alberto','6f49d720-8ed1-4e85-ae00-bf26db5ebca1','2019-08-05 12:30:32');
insert  into `token`(`id`,`user`,`token`,`date`) values 
(40,'alberto','399985ac-b789-476f-b7fa-0b8103fe867a','2019-08-05 12:31:48');
insert  into `token`(`id`,`user`,`token`,`date`) values 
(41,'sensor','5a5cb556-1371-4e4e-a1a0-14d0c4931214','2019-08-05 12:59:10');

/*Table structure for table `usuario` */

DROP TABLE IF EXISTS `usuario`;

CREATE TABLE `usuario` (
  `user` varchar(30) NOT NULL,
  `cert` text NOT NULL,
  `hash` text NOT NULL,
  PRIMARY KEY (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `usuario` */

insert  into `usuario`(`user`,`cert`,`hash`) values 
('alberto','MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtqxihxe/HdJCehG6pEoa4XSmVwJ6Zf4Z8FcT1pz1hzCR0yosxWXX6OvQzlYDTQBtXKLACt08mlchRTe+E4FXOcuGlc5XnGKNUbmrzj5jTm+8IEGvjHqKXk668D22+8Yt9kjTjgrQHW/G5CtsoOfhBpckiShB4KUA+15okD4x/ou0Xn76wv1vI7oT9cvDA9Qd18GfJPliux8ztGfeqWv20H+XYBoyMj4HkAwli6Si1mXHiGqf5+cGPyzvRRaXR4MUChCryZbSo6j2YbpcfMU0z8qw4fSObSzFdNBID+5YDY3al/Zoyc3x411HrrdLgxBAmLnSJ28D94SISrqTunrp0wIDAQAB','67e9182c873f8d503e6df1ffa44d8765757a02b03221b9e716a411131d27d914');
insert  into `usuario`(`user`,`cert`,`hash`) values 
('sensor','MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtqxihxe/HdJCehG6pEoa4XSmVwJ6Zf4Z8FcT1pz1hzCR0yosxWXX6OvQzlYDTQBtXKLACt08mlchRTe+E4FXOcuGlc5XnGKNUbmrzj5jTm+8IEGvjHqKXk668D22+8Yt9kjTjgrQHW/G5CtsoOfhBpckiShB4KUA+15okD4x/ou0Xn76wv1vI7oT9cvDA9Qd18GfJPliux8ztGfeqWv20H+XYBoyMj4HkAwli6Si1mXHiGqf5+cGPyzvRRaXR4MUChCryZbSo6j2YbpcfMU0z8qw4fSObSzFdNBID+5YDY3al/Zoyc3x411HrrdLgxBAmLnSJ28D94SISrqTunrp0wIDAQAB','67e9182c873f8d503e6df1ffa44d8765757a02b03221b9e716a411131d27d914');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
