insert into post (post_id, post_title, main_text, preview)
values (1, 'OOO моя оборона1', 'Важный текст 1', 'Что-то1')
ON CONFLICT DO NOTHING;

insert into post (post_id, post_title, main_text, preview)
values (2, 'OOO моя оборона2', 'Важный текст 2', 'Что-то2')
ON CONFLICT DO NOTHING;

insert into post (post_id, post_title, main_text, preview)
values (3, 'OOO моя оборона3', 'Важный текст 3', 'Что-то3')
ON CONFLICT DO NOTHING;

insert into tag (tag_id, tag_title)
values (1, 'Spring')
ON CONFLICT DO NOTHING;

insert into tag (tag_id, tag_title)
values (2, 'Java')
ON CONFLICT DO NOTHING;

insert into tag (tag_id, tag_title)
values (3, 'ООП')
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