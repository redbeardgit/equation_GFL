DROP DATABASE IF EXISTS db_equation;
CREATE DATABASE db_equation;
\connect db_equation;

CREATE TABLE public.equations (
	id serial4 NOT NULL,
	"expression" text NOT NULL,
	root_count int4 NULL DEFAULT 0,
	CONSTRAINT equations_pk PRIMARY KEY (id)
);
CREATE TABLE public.roots (
	equation_id int4 NOT NULL,
	root float8 NOT NULL,
	CONSTRAINT roots_pk PRIMARY KEY (equation_id, root),
	CONSTRAINT roots_fk FOREIGN KEY (equation_id) REFERENCES public.equations(id)
);
