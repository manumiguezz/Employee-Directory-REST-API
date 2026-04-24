INSERT INTO employee (first_name, last_name, email) VALUES
    ('Manuel', 'Miguez', 'manum@mail.com'),
    ('Luca', 'Vinelli', 'lucav@mail.com'),
    ('Inaki', 'Marino', 'inaki@mail.com'),
    ('Juan', 'Pucheta', 'juanip@mail.com');

INSERT INTO users (username, password, enabled) VALUES
    ('ramiro', '{noop}examplepass', TRUE),
    ('matias', '{noop}examplepass', TRUE),
    ('alejo', '{noop}examplepass', TRUE);

INSERT INTO authorities (username, authority) VALUES
    ('ramiro', 'ROLE_EMPLOYEE'),
    ('matias', 'ROLE_EMPLOYEE'),
    ('matias', 'ROLE_MANAGER'),
    ('alejo', 'ROLE_EMPLOYEE'),
    ('alejo', 'ROLE_MANAGER'),
    ('alejo', 'ROLE_ADMIN');
