INSERT INTO COUNTRY (ID,CODE,LABEL) VALUES (1,'US','United States of America');
INSERT INTO COUNTRY (ID,CODE,LABEL) VALUES (2,'GB','United Kingdom');

INSERT INTO USER_AWARD (ID,CODE,LABEL,DESCRIPTION) VALUES (1,'adv_cd_2019','Advent of Code 2019','A wonderful description');

INSERT INTO PERMISSION(ID,CODE,LABEL) VALUES (1,'UPDATE_ARTICLE','Update Article');
INSERT INTO PERMISSION(ID,CODE,LABEL) VALUES (2,'read','read');

INSERT INTO ROLE(ID,CODE,LABEL) VALUES (1,'admin','admin');

-- accounts use 'supersecretpassword'
INSERT INTO USER (ID,ALIAS,HASH_PASSWORD,EMAIL,AVATAR_LINK,ROLE_ID,DISABLED,BIOGRAPHY,GITHUB_USERNAME,DISCORD_USERNAME,COUNTRY_ID,JOIN_DATE) VALUES (1, 'system', '', '', '', 1, false, '', '', '', 1, 0);
INSERT INTO USER (ID,ALIAS,HASH_PASSWORD,EMAIL,AVATAR_LINK,ROLE_ID,DISABLED,BIOGRAPHY,GITHUB_USERNAME,DISCORD_USERNAME,COUNTRY_ID,JOIN_DATE) VALUES (2, 'Lambo', '$2a$10$OGVw0XltDpCZS949tqDhu.4ShJLI9sNCdmbjlCb3.PEkk.T0csCGi', 'lambo@cs.dev', 'lambo.jpg', 1, false, 'Push my PR!', 'lambocreeper', 'LamboCreeper#666', 2, 1570406400000);
INSERT INTO USER (ID,ALIAS,HASH_PASSWORD,EMAIL,AVATAR_LINK,ROLE_ID,DISABLED,BIOGRAPHY,GITHUB_USERNAME,DISCORD_USERNAME,COUNTRY_ID,JOIN_DATE) VALUES (3, 'Iffy', '$2a$10$OGVw0XltDpCZS949tqDhu.4ShJLI9sNCdmbjlCb3.PEkk.T0csCGi', 'iffy@cs.dev', 'iffy.jpg', null, false, 'Red sparkles and glitter', '', 'Iffy#<3<3', 2, 1570492800000);
INSERT INTO USER (ID,ALIAS,HASH_PASSWORD,EMAIL,AVATAR_LINK,ROLE_ID,DISABLED,BIOGRAPHY,GITHUB_USERNAME,DISCORD_USERNAME,COUNTRY_ID,JOIN_DATE) VALUES (4, 'Atrum', '$2a$10$OGVw0XltDpCZS949tqDhu.4ShJLI9sNCdmbjlCb3.PEkk.T0csCGi', 'atrum@cs.dev', 'atrum.jpg', null, false, 'Wut?', '', 'Atrum#1337', 2, 1570492800000);

INSERT INTO USER_TO_USER_AWARD (USER_ID,USER_AWARD_ID) VALUES (3,1);

INSERT INTO USER_TO_PERMISSION (USER_ID,PERMISSION_ID) VALUES (2,1),(2,2),(3,1);
INSERT INTO ROLE_TO_PERMISSION (ROLE_ID,PERMISSION_ID) VALUES (1,1), (1,2);

INSERT INTO CONTRIBUTOR_LIST (ID) VALUES (1);

INSERT INTO CONTRIBUTOR (ID,ALIAS,CONTRIBUTOR_LIST_ID,USER_ID) VALUES (1,'Some dude',1,null);
INSERT INTO CONTRIBUTOR (ID,ALIAS,CONTRIBUTOR_LIST_ID,USER_ID) VALUES (2,null,1,3);
INSERT INTO CONTRIBUTOR (ID,ALIAS,CONTRIBUTOR_LIST_ID,USER_ID) VALUES (3,null,1,2);

INSERT INTO SHOWCASE (ID,APPROVED,DESCRIPTION,LINK,TITLE,CONTRIBUTOR_LIST_ID,USER_ID) VALUES (1,true,'Showcase Description','http://www.example.com/1','Showcase Title',1,2);

INSERT INTO TAG (ID,LABEL) VALUES (1,'101'), (2,'programming');
INSERT INTO TAG_SET (ID) VALUES (1), (2);
INSERT INTO TAG_SET_TO_TAG (ID,TAG_ID,TAG_SET_ID) VALUES (1,1,1), (2,2,1);
INSERT INTO ARTICLE (ID,REVISION_ID,TITLE,TITLE_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON) VALUES (1,1,'Introduction To Programming','introduction-to-programming',4,0,3,50), (2,null,'Unpublished Article','unpublished-article',3,0,3,0);
INSERT INTO ARTICLE_REVISION (ID,ARTICLE_ID,DESCRIPTION,CONTENT,TAG_SET_ID,CREATED_BY,CREATED_ON) VALUES (1,1,'A basic overview of programming','Some long text talking about the introduction to programming',1,4,50);
INSERT INTO ARTICLE_REVISION (ID,ARTICLE_ID,DESCRIPTION,CONTENT,TAG_SET_ID,CREATED_BY,CREATED_ON) VALUES (2,1,'Older overview of programming','Older version of the long text talking about the introduction to programming',1,4,0);
INSERT INTO ARTICLE_REVISION (ID,ARTICLE_ID,DESCRIPTION,CONTENT,TAG_SET_ID,CREATED_BY,CREATED_ON) VALUES (3,2,'A revision for the unpublished article','This revision will not be seen, as the parent article is unpublished',2,3,0);