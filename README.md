# RestfulApiPostgresql
crud restful api which working with postgresql using jdbc 



/persons :

    GET:
        1)/             			return each Person from DB with attached apartments
        2)/{id}         			return person by id
        3)/persons     			return data of each person from List (requires List<Long> listIds)
        4)/count        			return each person and count of attached apartments  
    
    POST:
        1)/             			insert Person(single entity or attached apartments) (requires Person)
        2)/bind/{personId}&{apartmentId}    creating new bind
        3)/listBind     			insert list of binds (requires personId and List<Long> apartmentsIds)
    
    PUT:
	1)/{id}					update Person`s name/surname by id (requires Person)

    DELETE:
	1)/{id}					delete Person by id
	2)/bind/{personId}&{apartmentId} delete Bind 


/apartments :


    GET: 
	1)/					return each Apartment from DB with attached persons
	2)/{id}					return apartment by id
	3)/apartments				return data of each apartment from List of ids (requires List<Long> listIds)
	4)/count				return each apartment and count of attached persons 

    POST:
	1)/					insert new Apartment (requires Apartment)
	2)/listBinds				insert List of binds (requires apartmentId and List<Long> personIds)

    PUT:
	1)/{id}					update Apartment`s area/address by id (requires Apartment)

    DELETE:
	1)/{id}					delete Apartment by id
