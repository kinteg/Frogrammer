create sequence hibernate_sequence start 1 increment 1;

create table usr
(
    id         int8      not null,
    username   text      not null,
    password   text      not null,
    email      text      not null,
    first_name text      not null,
    last_name  text      not null,
    created    timestamp not null,
    updated    timestamp not null,
    status     text      not null,
    primary key (id)
);

create table role
(
    id      int8      not null,
    name    text      not null,
    created timestamp not null,
    updated timestamp not null,
    status  text      not null,
    primary key (id)
);

create table user_role
(
    user_id int8 not null,
    role_id int8 not null,
    primary key (user_id, role_id),
    foreign key (user_id) references usr (id),
    foreign key (role_id) references role (id)
);

create table post
(
    id         int8      not null,
    post_title text      not null,
    main_text  text      not null,
    preview    text      not null,
    created    timestamp not null,
    updated    timestamp not null,
    status     text      not null,
    user_id    int8      not null,
    primary key (id),
    foreign key (user_id) references usr(id)
);

create table tag
(
    id        int8      not null,
    tag_title text      not null,
    created   timestamp not null,
    updated   timestamp not null,
    status    text      not null,
    primary key (id)
);

create table post_tag
(
    tag_id  int8 not null,
    post_id int8 not null,
    primary key (tag_id, post_id),
    foreign key (tag_id) references tag (id),
    foreign key (post_id) references post (id)
);