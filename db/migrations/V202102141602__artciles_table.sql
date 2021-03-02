create table articles (
    url text primary key,
    blog_id text,
    content text default null,
    language text default null
);
