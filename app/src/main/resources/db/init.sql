CREATE TABLE IF NOT EXISTS Users
(
    "userId" VARCHAR
(
    255
) PRIMARY KEY,
    "email" VARCHAR
(
    255
) UNIQUE NOT NULL,
    "passwordHash" VARCHAR
(
    255
) NOT NULL,
    "firstName" VARCHAR
(
    255
),
    "lastName" VARCHAR
(
    255
),
    "phoneNumber" VARCHAR
(
    20
),
    "address" TEXT,
    "createdAt" BIGINT NOT NULL,
    "updatedAt" BIGINT
    );

CREATE TABLE IF NOT EXISTS Vehicles
(
    "vehicleId" VARCHAR
(
    255
) PRIMARY KEY,
    "userId" VARCHAR
(
    255
) NOT NULL,
    "make" VARCHAR
(
    255
),
    "model" VARCHAR
(
    255
),
    "year" INTEGER,
    "licensePlate" VARCHAR
(
    20
),
    "vin" VARCHAR
(
    255
),
    "fuelLevel" FLOAT,
    "latitude" FLOAT,
    "longitude" FLOAT,
    "mileage" INTEGER,
    "status" VARCHAR
(
    50
),
    "lockstatus" VARCHAR
(
    10
),
    "lastServiceDate" BIGINT,
    "createdAt" BIGINT NOT NULL,
    "updatedAt" BIGINT
    );

-- Insert vehicle record only if it doesn't exist
INSERT INTO Vehicles ("vehicleId", "userId", "make", "model", "year", "licensePlate", "vin", "fuelLevel",
                      "latitude", "longitude", "mileage", "status", "lockstatus", "lastServiceDate", "createdAt",
                      "updatedAt")
VALUES ('VIN1', -- vehicleId (UUID or String)
        'adminhexad', -- userId (foreign key from Users table)
        'Skoda', -- make
        'Octavia', -- model
        2021, -- year
        'SK12345', -- licensePlate
        'TMBJG7NE7L0123456', -- vin
        60.5, -- fuelLevel (in percentage)
        48.8566, -- latitude
        2.3522, -- longitude
        12000, -- mileage (in kilometers)
        'active', -- status ('active', 'inactive', etc.)
        'locked',
        EXTRACT(EPOCH FROM NOW())::BIGINT, -- lastServiceDate (timestamp in seconds)
        EXTRACT(EPOCH FROM NOW())::BIGINT, -- createdAt (timestamp in seconds)
        EXTRACT(EPOCH FROM NOW())::BIGINT -- updatedAt (timestamp in seconds, nullable)
       ) ON CONFLICT ("vehicleId") DO NOTHING; -- Do nothing if the vehicle already exists

CREATE TABLE IF NOT EXISTS VehicleHistory
(
    historyId
    SERIAL
    PRIMARY
    KEY,
    vehicleId
    VARCHAR
(
    255
),
    userId VARCHAR
(
    255
),
    action VARCHAR
(
    10
), -- 'lock' or 'unlock'
    timestamp BIGINT, -- Store as UNIX timestamp for when the action was taken
    createdAt BIGINT NOT NULL -- When the record was created
    );

CREATE TABLE IF NOT EXISTS subscriptions
(
    id
    SERIAL
    PRIMARY
    KEY,    -- Auto-incremented ID (Primary Key)
    subscriptionId
    SERIAL, -- Auto-incremented subscriptionId
    userId
    VARCHAR
(
    255
) NOT NULL, -- User ID (foreign key can be added if applicable)
    vehicleId VARCHAR
(
    255
) NOT NULL, -- Vehicle ID (foreign key can be added if applicable)
    serviceType VARCHAR
(
    255
) NOT NULL, -- Type of service
    startDate BIGINT NOT NULL, -- Start date (represented as a timestamp or Unix epoch)
    endDate BIGINT NOT NULL, -- End date (represented as a timestamp or Unix epoch)
    status VARCHAR
(
    50
) NOT NULL, -- Subscription status
    paymentStatus VARCHAR
(
    50
) NOT NULL, -- Payment status
    renewalDate BIGINT NOT NULL, -- Renewal date (represented as a timestamp or Unix epoch)
    createdAt BIGINT NOT NULL, -- Created timestamp
    updatedAt BIGINT -- Updated timestamp (nullable)
    );



ALTER TABLE Vehicles
    ADD COLUMN "isEngineOn" BOOLEAN DEFAULT false;


CREATE TABLE travelhistory
(
    historyId         SERIAL PRIMARY KEY,        -- Auto-incrementing ID for each trip
    vehicleId         VARCHAR(255)     NOT NULL, -- ID of the vehicle
    userId            VARCHAR(255)     NOT NULL, -- ID of the user
    startLatitude     DOUBLE PRECISION NOT NULL, -- Starting latitude
    startLongitude    DOUBLE PRECISION NOT NULL, -- Starting longitude
    endLatitude       DOUBLE PRECISION NOT NULL, -- Ending latitude
    endLongitude      DOUBLE PRECISION NOT NULL, -- Ending longitude
    travelTime        BIGINT           NOT NULL, -- Travel time in seconds or milliseconds
    distanceTravelled DOUBLE PRECISION NOT NULL, -- Distance traveled in kilometers/miles
    createdAt         BIGINT           NOT NULL  -- Timestamp when the trip record was created
);


CREATE TABLE car_top_speeds
(
    id            SERIAL PRIMARY KEY,
    make          VARCHAR(255) NOT NULL,
    model         VARCHAR(255) NOT NULL,
    country       VARCHAR(255) NOT NULL,
    top_speed_kmh INTEGER      NOT NULL,
    top_speed_mph INTEGER      NOT NULL
);

INSERT INTO car_top_speeds (make, model, country, top_speed_kmh, top_speed_mph)
VALUES ('Toyota', 'Supra', 'Japan', 250, 155),
       ('Honda', 'Civic Type R', 'Japan', 272, 169),
       ('Ford', 'Mustang GT', 'USA', 250, 155),
       ('Chevrolet', 'Corvette C8', 'USA', 312, 194),
       ('Porsche', '911 Turbo S', 'Germany', 320, 199),
       ('BMW', 'M5 Competition', 'Germany', 305, 189),
       ('Mercedes', 'AMG GT R', 'Germany', 318, 197),
       ('Ferrari', 'SF90 Stradale', 'Italy', 340, 211),
       ('Lamborghini', 'Aventador SVJ', 'Italy', 352, 218),
       ('Bugatti', 'Chiron', 'France', 420, 261),
       ('Nissan', 'GT-R', 'Japan', 315, 196),
       ('McLaren', '720S', 'UK', 341, 212),
       ('Aston Martin', 'Vantage', 'UK', 314, 195),
       ('Volkswagen', 'Golf R', 'Germany', 250, 155),
       ('Subaru', 'WRX STI', 'Japan', 250, 155),
       ('Skoda', 'Octavia vRS', 'Czech Republic', 250, 155),
       ('Skoda', 'Superb Sportline', 'Czech Republic', 250, 155);
