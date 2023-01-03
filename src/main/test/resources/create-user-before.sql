delete from user_role;
delete from usr;

insert into usr(id, active, password, username) values
(1, true, '$2a$08$h1pSJHFLKdBDm3d7c6bh7u6wfKqd9dXaBEJlpThOzsgMm99sZfLem', 'admin'),
(2, true, '$2a$08$/LcG.obdYJeqO9z4y4NbZOyQsrhOAQANi/jFmqCj8K3N2qdelyuDG', 'Mike');

insert into user_role(user_id, roles) values
(1, 'USER'), (1, 'ADMIN'),
(2, 'USER');