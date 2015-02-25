<?php
    /*   ParkingTracker PHP is the application layer facilitating communication between the ParkingTracker APP and local database.
     *   Copyright (C) 2014 University Of Wisconsin Parkside
     *
     *   This program is free software: you can redistribute it and/or modify
     *   it under the terms of the GNU General Public License as published by
     *   the Free Software Foundation, either version 3 of the License, or
     *   (at your option) any later version.
     *
     *   This program is distributed in the hope that it will be useful,
     *   but WITHOUT ANY WARRANTY; without even the implied warranty of
     *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     *   GNU General Public License for more details.
     *
     *   You should have received a copy of the GNU General Public License
     *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
     *
     *   @author : David Krawchuk
     *
     */
    
    // Include the database constants.
    include_once '_config.php';
    
    
    /* Class : UserValidation
     *      Facilitates the creation and validation of user credentials.
     * @author : David Krawchuk
     * @date : 11/2014
     */
    class UserValidation {
        
        // Create a user from passed paramters and insert values into usrs database encrypted.
        static function createUser($user_name, $password)
        {
            // Check parameters for valid lengths.
            if((strlen($user_name) < 57) && (strlen($password) < 57))
            {
                // Encrypt password
                $encrypt_password = crypt($password);
                
                // Connect to the user database.
                $mysqli = new mysqli(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_USERBASE_NAME);
                
                // If connection returns an error, print error and exit.
                if ($mysqli->connect_errno) {
                    printf("Connect failed: %s\n", $mysqli->connect_error);
                    exit();
                }
                
                /* Prepared statement, stage 1: prepare to insert into users table*/
                if (!($stmt = $mysqli->prepare("INSERT INTO users VALUES (NULL, ?, ?)"))) {
                    echo "Prepare failed: (" . $mysqli->errno . ") " . $mysqli->error;
                    exit();
                }
                
                /* Prepared statement, stage 2: bind and execute */
                if (!$stmt->bind_param("ss", $user_name, $encrypt_password)) {
                    echo "Binding parameters failed: (" . $stmt->errno . ") " . $stmt->error;
                    exit();
                }
                
                if (!$stmt->execute()) {
                    echo "Execute failed: (" . $stmt->errno . ") " . $stmt->error;
                    exit();
                }
                
                /* Insert default user value into privileges */
                if (!$mysqli->query("INSERT INTO privileges VALUES (NULL, 1)"))
                {
                    echo "Execute failed: (" . $stmt->errno . ") " . $stmt->error;
                    exit();
                }
                
                /* close statement and connection */
                $stmt->close();
                $mysqli->close();
            }
            else
                exit();
        }
        
        /*
          Called to validate user before a vote is submitted.
         */
        static function validateUser($user_name, $password)
        {
            // Connect to the user database.
            $mysqli = new mysqli(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_USERBASE_NAME);
            
            // If connection returns an error, print error and exit.
            if ($mysqli->connect_errno) {
                printf("Connect failed: %s\n", $mysqli->connect_error);
                exit();
            }
            
            /* Prepared statement, stage 1: prepare */
            if (!($stmt = $mysqli->prepare("SELECT * FROM users WHERE name = (?)"))) {
                echo "Prepare failed: (" . $mysqli->errno . ") " . $mysqli->error;
                exit();
            }
            
            /* Prepared statement, stage 2: bind and execute */
            if (!$stmt->bind_param("s", $user_name)) {
                echo "Binding parameters failed: (" . $stmt->errno . ") " . $stmt->error;
                exit();
            }
            
            if (!$stmt->execute()) {
                echo "Execute failed: (" . $stmt->errno . ") " . $stmt->error;
                exit();
            }
            
            $res = $stmt->get_result();
            $row = $res->fetch_assoc();
            
            if(strcmp($user_name, $row['name']) == $row['name'])
            {
                if(crypt($password, $row['password']) == $row['password'])
                {
                    /* close statement and connection */
                    $stmt->close();
                    $mysqli->close();
                    /* return success */
                    return 1;
                }
            }
            else
            {
                /* close statement and connection */
                $stmt->close();
                $mysqli->close();
                /* return failure */
                return 0;
            }
        }
    }
    ?>