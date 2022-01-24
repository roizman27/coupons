create table `COMPANIES` (
`ID` int primary key auto_increment,
`NAME` varchar(50),
`EMAIL` varchar(50),
`PASSWORD`  varchar(50)

);
select * from Companies;

create table `CUSTOMERS` (
`ID` int primary key auto_increment,
`FIRST_NAME` varchar(50),
`LAST_NAME` varchar(50),
`EMAIL` varchar(50),
`PASSWORD` varchar(50)
);
select*from customers;


create table `CATEGORIES` (
`ID` int primary key,
`NAME` varchar(50));
 insert into CATEGORIES values (1, 'Food');
 insert into CATEGORIES values (2, 'Electricity');
 insert into CATEGORIES values (3, 'Restaurant');
 insert into CATEGORIES values (4, 'Vacation');
select*from categories;

create table `COUPONS` (
`ID` int primary key auto_increment,
`COMPANY_ID` int, 
`CATEGORY_ID` int,
`TITLE` varchar(50),
`DESCRIPTION` varchar(50),
`START_DATE` date,
`END_DATE` date,
`AMOUNT` integer,
`PRICE` double,
`IMAGE` varchar(50),
 foreign key(COMPANY_ID) REFERENCES COMPANIES(ID),
 FOREIGN KEY(CATEGORY_ID) REFERENCES CATEGORIES(ID)
 ON DELETE CASCADE
);
 select * from `COUPONS`;

create table `CUSTOMERS_VS_COUPONS`(
`CUSTOMER_ID` int,
`COUPON_ID` int,
FOREIGN KEY (CUSTOMER_ID) references companies(id),
FOREIGN KEY (COUPON_ID) references coupons(id)
ON DELETE CASCADE
); 
 select * from customers_vs_coupons;




drop table if exists `CUSTOMERS_VS_COUPONS`;
drop table if exists `CATEGORIES`;
drop table if exists `COUPONS`;
drop table if exists `COMPANIES`;
drop table if exists `CUSTOMERS`;