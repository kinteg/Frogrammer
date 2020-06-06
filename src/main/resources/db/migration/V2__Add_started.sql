insert into post (id, post_title, main_text, preview, created, updated, status)
values (1, 'OOO моя оборона1', 'Важный текст 1', 'Что-то1', '20.11.1999', '20.11.1999', 'ACTIVE')
ON CONFLICT DO NOTHING;

insert into post (id, post_title, main_text, preview, created, updated, status)
values (2, 'OOO моя оборона2', 'Важный текст 2', 'Что-то2', '20.11.1999', '20.11.1999', 'ACTIVE')
ON CONFLICT DO NOTHING;

insert into post (id, post_title, main_text, preview, created, updated, status)
values (3, 'OOO моя оборона3', 'Важный текст 3', 'Что-то3', '20.11.1999', '20.11.1999', 'ACTIVE')
ON CONFLICT DO NOTHING;

insert into tag (id, tag_title, created, updated, status)
values (1, 'Spring', '20.11.1999', '20.11.1999', 'ACTIVE')
ON CONFLICT DO NOTHING;

insert into tag (id, tag_title, created, updated, status)
values (2, 'Java', '20.11.1999', '20.11.1999', 'ACTIVE')
ON CONFLICT DO NOTHING;

insert into tag (id, tag_title, created, updated, status)
values (3, 'ООП', '20.11.1999', '20.11.1999', 'ACTIVE')
ON CONFLICT DO NOTHING;

insert into post_tag (tag_id, post_id)
values (1, 1)
ON CONFLICT DO NOTHING;

insert into post_tag (tag_id, post_id)
values (1, 2)
ON CONFLICT DO NOTHING;

insert into post_tag (tag_id, post_id)
values (2, 2)
ON CONFLICT DO NOTHING;

insert into usr (id, username, password, email, first_name, last_name, created, updated, status)
values (1, 'jojo', '$2a$04$AyyQ.Zg/pBJDDR5q.JlM2.I8/lrCW/z2iAKtGE2eZYgFl2r8ti8.K', 'jojo.com', 'Ivan', 'Ivanovich', '20.11.1999', '20.11.1999', 'ACTIVE')
on conflict DO NOTHING;

insert into role (id, name, created, updated, status)
values (1, 'ROLE_ADMIN', '20.11.1999', '20.11.1999', 'ACTIVE'),
       (2, 'ROLE_USER', '20.11.1999', '20.11.1999', 'ACTIVE')
on conflict DO NOTHING;

insert into user_role (user_id, role_id)
values (1, 1), (1, 2);