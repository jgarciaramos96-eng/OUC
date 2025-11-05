INSERT INTO "digitalsession" (email,description, status)
VALUES ('juanpa@gmail.com', 'digital user 1 content ', 'AVAILABLE')
     , ('fperez@gmail.com', 'digital user 2 content', 'AVAILABLE')
     , ('jmgarcia@terra.es', 'digital user 3 content', 'AVAILABLE')
     , ('vidal_ccccc@gmail.com', 'digital user 4 content', 'AVAILABLE')
     , ('neil@gmail.com', 'digital user 5 content', 'AVAILABLE')
;

INSERT INTO "digitalitem" (digital_session_id,description, lat, lon, link, status)
VALUES (1, 'digital user 1 item', 10.11, 1.01, 'www.item1user1.net', 'AVAILABLE')
     , (1, 'digital user 1 item', 10.33, 1.33, 'www.item2user1.net', 'AVAILABLE')
     , (3, 'digital user 3 item', 30.11, 3.01, 'www.item1user3.net', 'AVAILABLE')
     , (4, 'digital user 4 item', 40.11, 4.01, 'www.item1user4.net', 'AVAILABLE')
     , (4, 'digital user 4 item', 40.22, 4.02, 'www.item2user4.net', 'AVAILABLE')
;
