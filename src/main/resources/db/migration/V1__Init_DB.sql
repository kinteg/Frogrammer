create sequence hibernate_sequence start 1 increment 1;

create table post
(
    post_id    int8 not null,
    post_title text not null,
    main_text  text not null,
    preview    text not null,
    primary key (post_id)
);

create table tag
(
    tag_id    int8 not null,
    tag_title text not null,
    primary key (tag_id)
);

create table post_tag
(
    post_tag_id int8 not null,
    tag_id      int8 not null,
    post_id     int8 not null,
    primary key (tag_id),
    foreign key (tag_id) references tag (tag_id),
    foreign key (post_id) references post (post_id)
);