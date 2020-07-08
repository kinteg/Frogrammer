insert into usr (id, username, password, email, first_name, last_name, created, updated, status)
values (1, 'jojo12', '$2a$04$AyyQ.Zg/pBJDDR5q.JlM2.I8/lrCW/z2iAKtGE2eZYgFl2r8ti8.K', 'jojo.com', 'Ivan', 'Ivanovich',
        '20.11.1999', '20.11.1999', 'ACTIVE')
on conflict DO NOTHING;

insert into usr (id, username, password, email, first_name, last_name, created, updated, status)
values (2, 'jojo123', '$2a$04$AyyQ.Zg/pBJDDR5q.JlM2.I8/lrCW/z2iAKtGE2eZYgFl2r8ti8.K', 'jojo.com1', 'Ivan', 'Ivanovich',
        '20.11.1999', '20.11.1999', 'ACTIVE')
on conflict DO NOTHING;

insert into usr (id, username, password, email, first_name, last_name, created, updated, status)
values (3, 'jojo1234', '$2a$04$AyyQ.Zg/pBJDDR5q.JlM2.I8/lrCW/z2iAKtGE2eZYgFl2r8ti8.K', 'jojo.com2', 'Ivan', 'Ivanovich',
        '20.11.1999', '20.11.1999', 'ACTIVE')
on conflict DO NOTHING;

insert into post (id, post_title, main_text, preview, created, updated, status, user_id)
values (1, 'OOO моя оборона1', 'Важный текст 1', 'Что-то1', '20.11.1999', '20.11.1999', 'ACTIVE', 1)
ON CONFLICT DO NOTHING;

insert into post (id, post_title, main_text, preview, created, updated, status, user_id)
values (2, 'OOO моя оборона2', 'Важный текст 2', 'Что-то2', '20.11.1999', '20.11.1999', 'ACTIVE', 1)
ON CONFLICT DO NOTHING;

insert into post (id, post_title, main_text, preview, created, updated, status, user_id)
values (3, 'OOO моя оборона3', 'Важный текст 3', 'Что-то3', '20.11.1999', '20.11.1999', 'ACTIVE', 1)
ON CONFLICT DO NOTHING;

insert into tag (id, tag_title, description, created, updated)
values (0, 'Default', 'Default', '20.11.1999', '20.11.1999')
ON CONFLICT DO NOTHING;

insert into tag (id, tag_title, description, created, updated)
values (1, 'Spring', 'This is Spring', '20.11.1999', '20.11.1999')
ON CONFLICT DO NOTHING;

insert into tag (id, tag_title, description, created, updated)
values (2, 'Java', 'This is Java', '20.11.1999', '20.11.1999')
ON CONFLICT DO NOTHING;

insert into tag (id, tag_title, description, created, updated)
values (3, 'ООП', 'This is ООП', '20.11.1999', '20.11.1999')
ON CONFLICT DO NOTHING;

insert into post_tag (tag_id, post_id)
values (0, 1)
ON CONFLICT DO NOTHING;

insert into post_tag (tag_id, post_id)
values (1, 1)
ON CONFLICT DO NOTHING;

insert into post_tag (tag_id, post_id)
values (0, 2)
ON CONFLICT DO NOTHING;

insert into post_tag (tag_id, post_id)
values (1, 2)
ON CONFLICT DO NOTHING;

insert into post_tag (tag_id, post_id)
values (2, 2)
ON CONFLICT DO NOTHING;

insert into role (id, name, created)
values (1, 'ROLE_ADMIN', '20.11.1999'),
       (2, 'ROLE_USER', '20.11.1999'),
       (3, 'ROLE_MODERATOR', '20.11.1999')
on conflict DO NOTHING;

insert into user_role (user_id, role_id)
values (1, 1), (1, 2), (1, 3),
       (2, 2), (2, 3),
       (3, 2);