CREATE TABLE IF NOT EXISTS basic_salary_info(
	id INT AUTO_INCREMENT PRIMARY KEY,
	perm_admin_salary DOUBLE,
	perm_teach_salary DOUBLE,
	annual_bonus DOUBLE,
	family_bonus DOUBLE,
	research_bonus DOUBLE,
	library_bonus DOUBLE
);

CREATE TABLE IF NOT EXISTS departments(
	dep_id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS employees(
	emp_id INT AUTO_INCREMENT PRIMARY KEY,
	fname VARCHAR(255),
	lname VARCHAR(255),
	started_at DATE,
	left_at DATE,
	address VARCHAR(255),
	phone VARCHAR(255),
	IBAN VARCHAR(255),
	bank_name VARCHAR(255),
	department_id INT,
	FOREIGN KEY (department_id) REFERENCES departments(dep_id),
	is_married VARCHAR(255)	
);

CREATE TABLE IF NOT EXISTS emp_children(
	child_id INT AUTO_INCREMENT PRIMARY KEY,
	age INT,
	emp_id INT,
	FOREIGN KEY (emp_id) REFERENCES employees(emp_id)
);

CREATE TABLE IF NOT EXISTS salaries(
	sal_id INT AUTO_INCREMENT PRIMARY KEY,
	b_salary DOUBLE,
	family_bonus DOUBLE,
	after_bonus_sal DOUBLE
);

CREATE TABLE IF NOT EXISTS emp_salaries(
	sal_id INT,
	emp_id INT,
	FOREIGN KEY (sal_id) REFERENCES salaries(sal_id),
	FOREIGN KEY (emp_id) REFERENCES employees(emp_id)
);

CREATE TABLE IF NOT EXISTS payments(
	bill_id INT AUTO_INCREMENT PRIMARY KEY,
	paid_at DATE,
	ammount DOUBLE,
	emp_id INT,
	FOREIGN KEY(emp_id) REFERENCES employees(emp_id)
);

CREATE TABLE IF NOT EXISTS perm_admin_salaries(
	sal_id INT,
	annual_bonus DOUBLE,
	FOREIGN KEY(sal_id) REFERENCES salaries(sal_id)
);

CREATE TABLE IF NOT EXISTS perm_teach_salaries(
	sal_id INT,
	annual_bonus DOUBLE,
	research_bonus DOUBLE,
	FOREIGN KEY(sal_id) REFERENCES salaries(sal_id)
);

CREATE TABLE IF NOT EXISTS temp_admin_salaries(
	sal_id INT,
	start_date DATE,
	end_date DATE,
	promotion_date DATE,
	FOREIGN KEY(sal_id) REFERENCES salaries(sal_id)
);

CREATE TABLE IF NOT EXISTS temp_teach_salaries(
	sal_id INT,
	start_date DATE,
	end_date DATE,
	promotion_date DATE,
	library_bonus DOUBLE,
	FOREIGN KEY(sal_id) REFERENCES salaries(sal_id)
);


INSERT INTO basic_salary_info (`perm_admin_salary`, `perm_teach_salary`, `annual_bonus`, `family_bonus`, `research_bonus`, `library_bonus`) VALUES ('800', '950', '15', '5', '100', '100');

START TRANSACTION;
INSERT INTO departments(name) VALUES
("Department of Computer Science"),
("Department of Biology"),
("Department of Applied Mathematics"),
("Department of Mathematics"),
("Department of Physics");
COMMIT;

START TRANSACTION;
INSERT INTO employees(fname,lname,started_at,left_at,address,phone,IBAN,bank_name,department_id,is_married) VALUES("Dimitris","Angelis","2020-01-19",NULL,"Kosma Pap	 3","6969696969","GR0000000","Peiraus",1,"yes");
SET @last_id = LAST_INSERT_ID();
INSERT INTO emp_children(age,emp_id) VALUES(10,@last_id),(19,@last_id);
INSERT INTO salaries(b_salary,family_bonus,after_bonus_sal) VALUES(800,15,800+0.15*800);
SET @last_id2 = LAST_INSERT_ID();
INSERT INTO emp_salaries(sal_id,emp_id) VALUES(@last_id2,@last_id);
INSERT INTO perm_admin_salaries(sal_id,annual_bonus) VALUES(@last_id2,0);
COMMIT;

START TRANSACTION;
INSERT INTO employees(fname,lname,started_at,left_at,address,phone,IBAN,bank_name,department_id,is_married) VALUES("Pantelis","Elef","2020-01-19",NULL,"Kentro 1","6969696969","GR0000000","alpha",2,"no");
SET @last_id = LAST_INSERT_ID();
INSERT INTO salaries(b_salary,family_bonus,after_bonus_sal) VALUES(950,0,950+100);
SET @last_id2 = LAST_INSERT_ID();
INSERT INTO emp_salaries(sal_id,emp_id) VALUES(@last_id2,@last_id);
INSERT INTO perm_teach_salaries(sal_id,annual_bonus,research_bonus) VALUES(@last_id2,0,100);
COMMIT;



START TRANSACTION;
INSERT INTO employees(fname,lname,started_at,left_at,address,phone,IBAN,bank_name,department_id,is_married) VALUES("Giannakis","Johnoyo","2020-01-19",NULL,"Koltsida 1","969696966969","GR0000000","Peiraus",3,"no");
SET @last_id = LAST_INSERT_ID();
INSERT INTO emp_children(age,emp_id) VALUES(1,@last_id);
INSERT INTO salaries(b_salary,family_bonus,after_bonus_sal) VALUES(950,5,950+950*0.05+100);
SET @last_id2 = LAST_INSERT_ID();
INSERT INTO emp_salaries(sal_id,emp_id) VALUES(@last_id2,@last_id);
INSERT INTO perm_teach_salaries(sal_id,annual_bonus,research_bonus) VALUES(@last_id2,0,100);
COMMIT;



START TRANSACTION;
INSERT INTO employees(fname,lname,started_at,left_at,address,phone,IBAN,bank_name,department_id,is_married) VALUES("Thodoris","Katsirampos","2020-01-19",NULL,"Paxni","696969696969","GR0000000","Alpha",4,"no");
SET @last_id = LAST_INSERT_ID();
INSERT INTO salaries(b_salary,family_bonus,after_bonus_sal) VALUES(500,0,500);
SET @last_id2 = LAST_INSERT_ID();
INSERT INTO emp_salaries(sal_id,emp_id) VALUES(@last_id2,@last_id);
INSERT INTO temp_admin_salaries(sal_id,start_date,end_date,promotion_date) VALUES(@last_id2,"2020-01-19","2021-01-19",NULL);
COMMIT;


START TRANSACTION;
INSERT INTO employees(fname,lname,started_at,left_at,address,phone,IBAN,bank_name,department_id,is_married) VALUES("Orestis","Faltakas","2020-01-19",NULL,"TEI","9669969696","GR0000000","Alpha",5,"yes");
SET @last_id = LAST_INSERT_ID();
INSERT INTO emp_children(age,emp_id) VALUES(1,@last_id),(19,@last_id),(20,@last_id);
INSERT INTO salaries(b_salary,family_bonus,after_bonus_sal) VALUES(600,20,600+600*0.2+100);
SET @last_id2 = LAST_INSERT_ID();
INSERT INTO emp_salaries(sal_id,emp_id) VALUES(@last_id2,@last_id);
INSERT INTO temp_teach_salaries(sal_id,start_date,end_date,promotion_date,library_bonus) VALUES(@last_id2,"2020-01-19","2021-01,19",NULL,100);
COMMIT;



START TRANSACTION;
INSERT INTO employees(fname,lname,started_at,left_at,address,phone,IBAN,bank_name,department_id,is_married) VALUES("Sia","K","2020-01-19",NULL,"Koule","6969696969","GR0000000","Alpha",5,"yes");
SET @last_id = LAST_INSERT_ID();
INSERT INTO emp_children(age,emp_id) VALUES(1,@last_id),(19,@last_id),(20,@last_id);
INSERT INTO salaries(b_salary,family_bonus,after_bonus_sal) VALUES(600,20,600+600*0.2+100);
SET @last_id2 = LAST_INSERT_ID();
INSERT INTO emp_salaries(sal_id,emp_id) VALUES(@last_id2,@last_id);
INSERT INTO temp_teach_salaries(sal_id,start_date,end_date,promotion_date,library_bonus) VALUES(@last_id2,"2020-01-19","2021-01,19",NULL,100);
COMMIT;


START TRANSACTION;
INSERT INTO employees(fname,lname,started_at,left_at,address,phone,IBAN,bank_name,department_id,is_married) VALUES("Mike","Dod","2020-01-19",NULL,"Kentro 2","69696969","GR0000000","Peiraus",1,"no");
SET @last_id = LAST_INSERT_ID();
INSERT INTO salaries(b_salary,family_bonus,after_bonus_sal) VALUES(800,0,800);
SET @last_id2 = LAST_INSERT_ID();
INSERT INTO emp_salaries(sal_id,emp_id) VALUES(@last_id2,@last_id);
INSERT INTO perm_admin_salaries(sal_id,annual_bonus) VALUES(@last_id2,0);
COMMIT;



START TRANSACTION;
INSERT INTO employees(fname,lname,started_at,left_at,address,phone,IBAN,bank_name,department_id,is_married) VALUES("Vaggelas","Marg","2020-01-19",NULL,"Jumbo","69969696","GR0000000","Peiraus",2,"no");
SET @last_id = LAST_INSERT_ID();
INSERT INTO salaries(b_salary,family_bonus,after_bonus_sal) VALUES(950,0,950+100);
SET @last_id2 = LAST_INSERT_ID();
INSERT INTO emp_salaries(sal_id,emp_id) VALUES(@last_id2,@last_id);
INSERT INTO perm_teach_salaries(sal_id,annual_bonus,research_bonus) VALUES(@last_id2,0,100);
COMMIT;


INSERT INTO payments(paid_at,ammount,emp_id) VALUES ('2020-01-20',920,1);
INSERT INTO payments(paid_at,ammount,emp_id) VALUES ('2020-01-20',1050,2);
INSERT INTO payments(paid_at,ammount,emp_id) VALUES ('2020-01-20',1097.5,3);
INSERT INTO payments(paid_at,ammount,emp_id) VALUES ('2020-01-20',500,4);
INSERT INTO payments(paid_at,ammount,emp_id) VALUES ('2020-01-20',820,5);
INSERT INTO payments(paid_at,ammount,emp_id) VALUES ('2020-01-20',820,6);
INSERT INTO payments(paid_at,ammount,emp_id) VALUES ('2020-01-20',800,7);
INSERT INTO payments(paid_at,ammount,emp_id) VALUES ('2020-01-20',1050,8);








