CREATE USER volgacoin_user WITH PASSWORD '123456';


CREATE TABLE IF NOT EXISTS public.user_data
(
    id bigint NOT NULL,
    username text COLLATE pg_catalog."default" NOT NULL,
    clicks bigint NOT NULL,
    energy double precision NOT NULL,
    last_login timestamp with time zone NOT NULL,
    CONSTRAINT user_data_pkey PRIMARY KEY (id)
);
GRANT ALL ON TABLE public.user_data TO volgacoin_user;