INSERT INTO ADDRESS (ID,STREET,HOUSE_NUMBER,ZIP_CODE,PRBR_ID) VALUES (1, 'Street_prbr_1_id_1', '23', '07095',1);
INSERT INTO HOUSE (ID,NUM,ADDRESS_ID,PRBR_ID) VALUES (1, '23', 1, 1);
INSERT INTO HOUSE (ID,NUM,ADDRESS_ID,PRBR_ID) VALUES (2, '25', 1, 1);
INSERT INTO HOUSE (ID,NUM,ADDRESS_ID,PRBR_ID) VALUES (3, '27', 1, 1);

INSERT INTO ADDRESS (ID,STREET,HOUSE_NUMBER,ZIP_CODE,PRBR_ID) VALUES (2, 'Street_prbr_1_id_2', '231', '07097',1);
INSERT INTO HOUSE (ID,NUM,ADDRESS_ID,PRBR_ID) VALUES (4, '2', 2, 1);
INSERT INTO HOUSE (ID,NUM,ADDRESS_ID,PRBR_ID) VALUES (5, '3', 2, 1);
INSERT INTO HOUSE (ID,NUM,ADDRESS_ID,PRBR_ID) VALUES (6, '8', 2, 1);

INSERT INTO ADDRESS (ID,STREET,HOUSE_NUMBER,ZIP_CODE,PRBR_ID) VALUES (3, 'Street_prbr_1_id_3', '5', '07092',1);
INSERT INTO ADDRESS (ID,STREET,HOUSE_NUMBER,ZIP_CODE,PRBR_ID) VALUES (4, 'Street_prbr_1_id_4', '67', '07079',1);

INSERT INTO ADDRESS (ID,STREET,HOUSE_NUMBER,ZIP_CODE,PRBR_ID) VALUES (5, 'Street_prbr_2_id_5', '123', '07097',2);
INSERT INTO ADDRESS (ID,STREET,HOUSE_NUMBER,ZIP_CODE,PRBR_ID) VALUES (15, 'Street_prbr_2_id_15', '34', '07097',2);
INSERT INTO ADDRESS (ID,STREET,HOUSE_NUMBER,ZIP_CODE,PRBR_ID) VALUES (17, 'Street_prbr_2_id_17', '123', '07097',2);
INSERT INTO ADDRESS (ID,STREET,HOUSE_NUMBER,ZIP_CODE,PRBR_ID) VALUES (18, 'Street_prbr_2_id_18', '3453', '07093',2);

INSERT INTO ADDRESS (ID,STREET,HOUSE_NUMBER,ZIP_CODE,PRBR_ID) VALUES (6, 'Street_prbr_3_id_6', '1', '07090',3);
INSERT INTO ADDRESS (ID,STREET,HOUSE_NUMBER,ZIP_CODE,PRBR_ID) VALUES (7, 'Street_prbr_3_id_7', '111', '07095',3);

INSERT INTO ADDRESS (ID,STREET,HOUSE_NUMBER,ZIP_CODE,PRBR_ID) VALUES (8, 'Street_prbr_1_id_8', '23', '07095',1);
INSERT INTO ADDRESS (ID,STREET,HOUSE_NUMBER,ZIP_CODE,PRBR_ID) VALUES (9, 'Street_prbr_1_id_9', '231', '07097',1);
INSERT INTO ADDRESS (ID,STREET,HOUSE_NUMBER,ZIP_CODE,PRBR_ID) VALUES (10, 'Street_prbr_1_id_10', '5', '07092',1);
INSERT INTO ADDRESS (ID,STREET,HOUSE_NUMBER,ZIP_CODE,PRBR_ID) VALUES (11, 'Street_prbr_1_id_11', '67', '07079',1);
INSERT INTO ADDRESS (ID,STREET,HOUSE_NUMBER,ZIP_CODE,PRBR_ID) VALUES (12, 'Street_prbr_1_id_12', '123', '07097',1);
INSERT INTO ADDRESS (ID,STREET,HOUSE_NUMBER,ZIP_CODE,PRBR_ID) VALUES (13, 'Street_prbr_1_id_13', '1', '07090',1);
INSERT INTO ADDRESS (ID,STREET,HOUSE_NUMBER,ZIP_CODE,PRBR_ID) VALUES (14, 'Street_prbr_1_id_14', '111', '07095',1);

INSERT INTO COMPANY (ID,NAME,TEST_NAME,ZIP,PRBR_ID) VALUES (1,'Test_IntyerInc', 'IntyerInc', 123 ,1);
INSERT INTO DEPARTMENT (ID, NAME, COMPANY_ID,PRBR_ID) VALUES (1,'Sellers', 1, 1);
INSERT INTO DEPARTMENT (ID, NAME, COMPANY_ID,PRBR_ID) VALUES (2, 'Bayers', 1, 1);

INSERT INTO EMPLOYEE (ID, NAME, SURNAME, ADDRESS_ID, DEPARTMENT_ID, PRBR_ID) VALUES (1, 'Jon','Snow', 1, 1, 1);
INSERT INTO EMPLOYEE (ID, NAME, SURNAME, ADDRESS_ID, DEPARTMENT_ID, PRBR_ID) VALUES (2, 'Robin','Good', 2, 1, 1);

INSERT INTO COMPANY (ID,NAME, TEST_NAME, ZIP, PRBR_ID) VALUES (2, null,'Intel', null ,2);
INSERT INTO DEPARTMENT (ID, NAME, COMPANY_ID,PRBR_ID) VALUES (3,'Sellers', 2, 2);
INSERT INTO DEPARTMENT (ID, NAME, COMPANY_ID,PRBR_ID) VALUES (4, 'Bayers', 2, 2);

INSERT INTO EMPLOYEE (ID, NAME, SURNAME, ADDRESS_ID, DEPARTMENT_ID, PRBR_ID) VALUES (3, 'Robin','Bad', 5, 3, 2);

INSERT INTO ARTICLE (ID, NAME, TITLE, CONTENT ,PRBR_ID) VALUES (1, 'learn-react',
        'The Fastest Way to Learn React',
            '`Welcome! Today we''re going to be talking about the fastest way to
            learn React. We''ll be discussing some topics such as proin congue
            ligula id risus posuere, vel eleifend ex egestas. Sed in turpis leo.
            Aliquam malesuada in massa tincidunt egestas. Nam consectetur varius turpis,
            non porta arcu porttitor non. In tincidunt vulputate nulla quis egestas. Ut
            eleifend ut ipsum non fringilla. Praesent imperdiet nulla nec est luctus, at
            sodales purus euismod.`,
            `Donec vel mauris lectus. Etiam nec lectus urna. Sed sodales ultrices dapibus.
            Nam blandit tristique risus, eget accumsan nisl interdum eu. Aenean ac accumsan
            nisi. Nunc vel pulvinar diam. Nam eleifend egestas viverra. Donec finibus lectus
            sed lorem ultricies, eget ornare leo luctus. Morbi vehicula, nulla eu tempor
            interdum, nibh elit congue tellus, ac vulputate urna lorem nec nisi. Morbi id
            consequat quam. Vivamus accumsan dui in facilisis aliquet.`,
            `Etiam nec lectus urna. Sed sodales ultrices dapibus.
            Nam blandit tristique risus, eget accumsan nisl interdum eu. Aenean ac accumsan
            nisi. Nunc vel pulvinar diam. Nam eleifend egestas viverra. Donec finibus lectus
            sed lorem ultricies, eget ornare leo luctus. Morbi vehicula, nulla eu tempor
            interdum, nibh elit congue tellus, ac vulputate urna lorem nec nisi. Morbi id
            consequat quam. Vivamus accumsan dui in facilisis aliquet.`', 1);

INSERT INTO ARTICLE (ID, NAME, TITLE, CONTENT ,PRBR_ID) VALUES (2, 'learn-node',
        'How to Build a Node Server in 10 Minutes',
            '`In this article, we''re going to be talking looking at a very quick way
            to set up a Node.js server. We''ll be discussing some topics such as proin congue
            ligula id risus posuere, vel eleifend ex egestas. Sed in turpis leo.
            Aliquam malesuada in massa tincidunt egestas. Nam consectetur varius turpis,
            non porta arcu porttitor non. In tincidunt vulputate nulla quis egestas. Ut
            eleifend ut ipsum non fringilla. Praesent imperdiet nulla nec est luctus, at
            sodales purus euismod.`,
            `Donec vel mauris lectus. Etiam nec lectus urna. Sed sodales ultrices dapibus.
            Nam blandit tristique risus, eget accumsan nisl interdum eu. Aenean ac accumsan
            nisi. Nunc vel pulvinar diam. Nam eleifend egestas viverra. Donec finibus lectus
            sed lorem ultricies, eget ornare leo luctus. Morbi vehicula, nulla eu tempor
            interdum, nibh elit congue tellus, ac vulputate urna lorem nec nisi. Morbi id
            consequat quam. Vivamus accumsan dui in facilisis aliquet.`,
            `Etiam nec lectus urna. Sed sodales ultrices dapibus.
            Nam blandit tristique risus, eget accumsan nisl interdum eu. Aenean ac accumsan
            nisi. Nunc vel pulvinar diam. Nam eleifend egestas viverra. Donec finibus lectus
            sed lorem ultricies, eget ornare leo luctus. Morbi vehicula, nulla eu tempor
            interdum, nibh elit congue tellus, ac vulputate urna lorem nec nisi. Morbi id
            consequat quam. Vivamus accumsan dui in facilisis aliquet.`', 1);

INSERT INTO ARTICLE (ID, NAME, TITLE, CONTENT ,PRBR_ID) VALUES (3, 'my-thoughts-on-resumes',
        'My Thoughts on Resumes',
            '`Today is the day I talk about something which scares most people: resumes.
            In reality, I''m not sure why people have such a hard time with proin congue
            ligula id risus posuere, vel eleifend ex egestas. Sed in turpis leo.
            Aliquam malesuada in massa tincidunt egestas. Nam consectetur varius turpis,
            non porta arcu porttitor non. In tincidunt vulputate nulla quis egestas. Ut
            eleifend ut ipsum non fringilla. Praesent imperdiet nulla nec est luctus, at
            sodales purus euismod.`,
            `Donec vel mauris lectus. Etiam nec lectus urna. Sed sodales ultrices dapibus.
            Nam blandit tristique risus, eget accumsan nisl interdum eu. Aenean ac accumsan
            nisi. Nunc vel pulvinar diam. Nam eleifend egestas viverra. Donec finibus lectus
            sed lorem ultricies, eget ornare leo luctus. Morbi vehicula, nulla eu tempor
            interdum, nibh elit congue tellus, ac vulputate urna lorem nec nisi. Morbi id
            consequat quam. Vivamus accumsan dui in facilisis aliquet.`,
            `Etiam nec lectus urna. Sed sodales ultrices dapibus.
            Nam blandit tristique risus, eget accumsan nisl interdum eu. Aenean ac accumsan
            nisi. Nunc vel pulvinar diam. Nam eleifend egestas viverra. Donec finibus lectus
            sed lorem ultricies, eget ornare leo luctus. Morbi vehicula, nulla eu tempor
            interdum, nibh elit congue tellus, ac vulputate urna lorem nec nisi. Morbi id
            consequat quam. Vivamus accumsan dui in facilisis aliquet.`', 1);

INSERT INTO ARTICLE_COMMENT (ID, ARTICLE_ID, POSTED_BY, CONTENT ,PRBR_ID) VALUES (1, 1, 'Vasiy','Great job', 1);