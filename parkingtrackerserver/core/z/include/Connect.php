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
 *   @author : Francisco Mateo
 *   Edited by: Bereket Kifle, David Krawchuk
 *
 */

/*
 Requirement statement needed for the database constants. 
 */
require_once '_config.php';

/*
 * Class to create a connection to the server.
 *
 * @author Francisco Mateo
 *
 * Documentation added:
 * @author : David Krawchuk
 * @date : October 6 2014
 */
class Connect {

    private $connection;
    
    
    public function __contruct() { }

    /**
     * connect: Call to connect creates a mysqli object with the default constants set in 
     *  _config.php. If successful the function returns an object representation of an mysql 
     *  connection.
     * @param : none
     * @return : mysql object.
     */
    public function connect()
    {
        try {
            //Procedural function call to mysqli constructor.
            $this->connection = new mysqli(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME);
        } catch (Exception $e) {
            // Print error to browser.
            // Kill php session.
            echo "Service unavailable, failed to connect to MySQL.";
            exit();
        }
        
        return $this->connection;
    }

}

?>
