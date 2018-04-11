INSERT INTO users( user_name, user_pass ) VALUES( 'Administrator', '12345' );
INSERT INTO user_roles VALUES( 'Administrator', 'Administrator' );
INSERT INTO user_roles VALUES( 'Administrator', 'SystemDeveloper' );
INSERT INTO user_roles VALUES( 'Administrator', 'Annotator' );

INSERT INTO categories (name, entity) VALUES ('Root', 'publications')

<#include 'dictionaries'/>
