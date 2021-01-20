How to run test project:
* download .zip or clone git repository
* run cmd in root folder and run command "docker-compose up --build"


API Air Company
1. GET /api/air-companies - request to get all details about all air companies.
2. GET /api/air-companies/{id} - request to get all details about a specific air company by id (Long).
3. POST /api/air-companies - request to create a new air company in DB with
* name (String)
* company type (should be one from the list {PASSENGER, CARGO})
Example:
{
    "name": "Ryanair" ,
    "companyType": "PASSENGER"
}
4. PUT /api/air-companies/{id} - request to update a specific air company by id (Long). Values that can be updated:
* name (String)
* company type (should be one from the list {PASSENGER, CARGO})
Example:
{
    "name": "Ryanair" ,
    "companyType": "CARGO"
}
5. DELETE /api/air-companies/{id} - request to delete all details about a specific air company by id (Long).


API Airplane
1. GET /api/airplanes - request to get all details about all airplanes.
2. GET /api/airplanes/{id} - request to get all details about a specific airplane by id (Long).
3. POST/api/airplanes - request to create a new airplane in DB with 
* name (String)
* factorySerialNumber (String)
* airCompanyId (Long or -1 - if the company is not assigned)
* numberOfFlights (Integer)
* flightDistance (Double)
* fuelCapacity (Double)
* type (should be one from the list {SMALL, MEDIUM, LARGE})
Example with an assigned company:
{
    "name": "Boeing 737" ,
    "factorySerialNumber": "B15463521",
    "airCompanyId": 12,
    "numberOfFlights": 53,
    "flightDistance": 654.56,
    "fuelCapacity": 158,
    "type": "MEDIUM"
}
Example with not an assigned company:
{
    "name": "Boeing 737" ,
    "factorySerialNumber": "B15463521",
    "airCompanyId": null,
    "numberOfFlights": 53,
    "flightDistance": 654.56,
    "fuelCapacity": 158,
    "type": "MEDIUM"
}
4. PUT /api/airplanes/{id} - request to update a specific airplane by id (Long). Values that can be updated:
* name (String)
* numberOfFlights (Integer)
* flightDistance (Double)
* fuelCapacity (Double)
* type (should be one from the list {SMALL, MEDIUM, LARGE})
Example:
{
    "name": "Boeing 737",
    "numberOfFlights": 54,
    "flightDistance": 785.56,
    "fuelCapacity": 158,
    "type": "MEDIUM"
}
5. DELETE /api/airplanes/{id} - request to delete all details about a specific airplane by id (Long).
6. PATCH /api/airplanes/{id}/reassign?airCompanyId={airCompanyId} - request to reassign airCompanyId (Integer) for a specific airplane by id (Long).


API Flight
1. GET /api/flights - request to get all details about all flights.
2. GET /api/flights/{id} - request to get all details about a specific flight by id (Long).
3. POST /api/flights - request to create a new flight in DB with
* airCompanyId (Long)
* airplaneId (Long)
* departureCountry (String) - should be from the list of countries in the file “/resources/sql/Static Util Entity init.sql”
* destinationCountry (String) - should be from the list of countries in the file “/resources/sql/Static Util Entity init.sql”
* distance (Double)
* estimatedFlightTime (String) - in the format HH:MM
Example:
{
    "airCompanyId": 12,
    "airplaneId": 5,
    "departureCountry": "German",
    "destinationCountry": "Poland",
    "distance": 609,
    "estimatedFlightTime": "00:41"
}
4. PUT/api/flights/{id} - request to update a specific flight by id (Long). Values that can be updated:
* airCompanyId (Long)
* airplaneId (Long)
* departureCountry (String) - should be from the list of countries in the file “/resources/sql/Static Util Entity init.sql”
* destinationCountry (String) - should be from the list of countries in the file “/resources/sql/Static Util Entity init.sql”
* distance (Double)
* estimatedFlightTime (String) - in the format HH:MM
Example:
{
    "airCompanyId": 12,
    "airplaneId": 8,
    "departureCountry": "German",
    "destinationCountry": "Poland",
    "distance": 609,
    "estimatedFlightTime": "00:45"
}
5. DELETE/api/flights/{id} - request to delete all details about a specific flight by id (Long).
6. PATCH /api/flights/{id}/status - request to change status (should be one from the list {PENDING, DELAYED, ACTIVE, COMPLETED}) for a specific flight by id (Long).
Example:
{
    "status": "ACTIVE"
}
* How the statuses can be changed:
  PENDING is set when flight was created
  Next possible status can be DELAYED or ACTIVE.
  After DELAYED only ACTIVE can be set.
  COMPLETED can be set only after ACTIVE.
7. GET /api/flights/specific-filter/1 - request to find all air company flights by status (should be one from the list {PENDING, DELAYED, ACTIVE, COMPLETED}) and by airCompanyName (String).
Example:
{
    "airCompanyName": "Ryanair",
    "status":"DELAYED"
}
8. GET /api/flights/specific-filter/2 - request to find all flights in ACTIVE status and started more than 24 hours ago.
Example:
{
    "status":"ACTIVE",
    "hoursAgo": 26,
}
9. GET /api/flights/specific-filter/3 -  request to find all flights in COMPLETED status and difference between started and ended time is bigger than estimated flight time.