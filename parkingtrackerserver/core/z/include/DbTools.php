

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

/* Include the required files */
require_once '_config.php';
require_once 'libs/htmlpurifier-4.6.0/Library/HTMLPurifier.auto.php';
require 'Connect.php';

/**
 * Class to handle all Db interactions
 * CRUD - Create, Read, Update and Delete
 *
 * @author Francisco Mateo
 * Edited by Bereket Kifle
 *
 * Documentation added by:
 * @author : David Krawchuk
 * @date : October 6 2014
 */
class DbTools {
    
    // Variable declarations.
    private $connection;
    private $config;
    private $purifier;

    /*
     * __construct :
     * i.) Overridden default constructor.
     * ii.) Creates a new connection object and attempts to setup a mysql connection.
     *
     * @param : none
     * @return : DbTools instance.
     */
    function __construct() {
        $db = new Connect();
        $this->connection = $db->connect();
        $this->config = HTMLPurifier_Config::createDefault();
        $this->purifier = new HTMLPurifier($this->config);
    }
    
    /*
     * getZones : Performs a database query for the parking zones.
     * @param : none
     * @return : hashed-array of quary response parameters.
     */
    function getZones() {
        //checkColors();
        
        // Select all rows from zones database table.
        $query = 'SELECT * FROM zones';
        
        /*
         Prepares the SQL query, and returns a statement handle to be used for
         further operations on the statement. The query must consist of a single 
         SQL statement.
         */
        $stmt = $this->connection;
        
        /*
         *  1.) Bind those objects to a set of variables.
         *  2.) Create a hashed array with the key = 'zones';
         *   A.) While there is results avaliable to fetch from the query.
         *    i.) In the Hashed Array $json create and assign key/value pairs.
         *    ii.) Create a hashed array $result with key = 'zones' / values =
         *          $json array variables pushed one at a time onto the stack.
         *  3.) Print the json results to output.
         *  4.) Close the myqli connection.
         *  
         */
        
        $result['zones'] = array();
        
        if($returnedQuery = $stmt->query($query)){
            while($row = $returnedQuery->fetch_assoc())
            {
                array_push($result['zones'], $row);
            }
            
            // Print results to output.
            echo json_encode($result);
            
            // Close mysqli connection.
            $stmt->close();
            
            return $result;
        }
        
        else {
            return NULL;
        }
        
    }

    /*
     * getGrid() : Performs a database query for all posible grid points.
     * @param : none 
     * @return : hashed-array of query responses.
     */
    function getGrid() {
        //checkColors();
        
        // Select all rows from the grid_points table.
        $query = 'SELECT * FROM grid_points';
        
        /*
         Prepares the SQL query, and returns a statement handle to be used for
         further operations on the statement. The query must consist of a single
         SQL statement.
         */
        $stmt = $this->connection;
        
        /*
         *  1.) Bind those objects to a set of variables.
         *  2.) Create a hashed array with the key = 'zones';
         *   A.) While there is results avaliable to fetch from the query.
         *    i.) In the Hashed Array $json create and assign key/value pairs.
         *    ii.) Create a hashed array $result with key = 'zones' / values =
         *          $json array variables pushed one at a time onto the stack.
         *  3.) Print the json results to output.
         *  4.) Close the myqli connection.
         *
         */
        $result['grid_points'] = array();
        
        if($returnedQuery = $stmt->query($query)){
            while($row = $returnedQuery->fetch_assoc())
            {
                array_push($result['grid_points'], $row);
            }
            
            // Print results to output.
            echo json_encode($result);
            
            // Close mysqli connection.
            $stmt->close();
            
            return $result;
        }
        
        else {
            return NULL;
        }
    }

    /*
     * getAvail() : Performs a database query for all avaliable parking zones.
     * @param : none
     * @return : Hashed array of query responses.
     */
    function getAvail() {
        //checkColors();
        
        // Select all rows from table availability.
        $query = 'SELECT * FROM availability';
        
        /*
         Prepares the SQL query, and returns a statement handle to be used for
         further operations on the statement. The query must consist of a single
         SQL statement.
         */
        $stmt = $this->connection;
        
        
        /*
         *  1.) Bind those objects to a set of variables.
         *  2.) Create a hashed array with the key = 'zones';
         *   A.) While there is results avaliable to fetch from the query.
         *    i.) In the Hashed Array $json create and assign key/value pairs.
         *    ii.) Create a hashed array $result with key = 'zones' / values =
         *          $json array variables pushed one at a time onto the stack.
         *  3.) Print the json results to output.
         *  4.) Close the myqli connection.
         *
         */
        
        $result['availability'] = array();
        
        if($returnedQuery = $stmt->query($query)){
            while($row = $returnedQuery->fetch_assoc())
            {
                array_push($result['availability'], $row);
            }
            
            // Print results to output.
            echo json_encode($result);
            
            // Close mysqli connection.
            $stmt->close();
            
            return $result;
        }
        
        else {
            return NULL;
        }
    }
    
    /*
     * update() : Updates the database table 'availibilty'.
     * 
     * @param : $zone - User selected zone value.
     *          $vote - User selected vote value.
     * @return : none.
     */
    function update($zone, $vote) {
        /* Purify incoming parameters.*/
        $clean_zone = $this->purifier->purify($zone);
        $clean_vote = $this->purifier->purify($vote);

        
        // Get the singleton slim instance.
        $app = \Slim\Slim::getInstance();

        // Variable declarations set with defualt values.
        $updateQuery = null;
        $availQuery = null;
        $result = null;

        /*
         * Set of conditional statements testing user selected vote.
         *  i.) The selected vote updates the database table 'avaliability'
         *       and increments the rows in the appropriate column by one.
         *
         *      Note: The rows are the id = ? and are bound  by the statement
         *                     $stmt->bind_param('i', $zone);
         *      Note2: Switch statement would be more elegant and clear solution. (DK)
         */
        if ($clean_vote == 1) {
            $updateQuery = 'UPDATE availability
                            SET near_empty = near_empty + 1
                            WHERE id = ?';
            $availQuery = 'SELECT near_empty
                           FROM availability
                           WHERE id = ?';
        }
        elseif ($clean_vote == 2) {
            $updateQuery = 'UPDATE availability
                            SET part_full = part_full + 1
                            WHERE id = ?';
            $availQuery = 'SELECT part_full
                           FROM availability
                           WHERE id = ?';
        }
        elseif ($clean_vote == 3) {
            $updateQuery = 'UPDATE availability
                            SET near_full = near_full + 1
                            WHERE id = ?';
            $availQuery = 'SELECT near_full
                           FROM availability
                           WHERE id = ?';
        }
        
        // Create a mysqli query.
        $stmt = $this->connection->prepare($updateQuery);
        // Attach the zone to the id in the SQL where statement.
        $stmt->bind_param('i', $clean_zone);
        // Perform query.
        $stmt->execute();
        // Release memory for reuse held by variable.
        $stmt = null;
        
        // Perform new mysqli query.
        $stmt = $this->connection->prepare($availQuery);
        // Attach the zone parameater to the id in the SQL WHERE statement.
        $stmt->bind_param('i', $clean_zone);
        // Execute query request.
        $stmt->execute();
        // Bind the query result to the result variable.
        $stmt->bind_result($result);
        
        // While results exist print those results to output.
        while ($stmt->fetch()) {
            echo $result;
        }

        /*
         * Not sure of the reasoning behind this...
         *  I assume the following : 
         *   if the returned results in the array are significant the update the tables to 
         *   represent the appropriate changes.
         */
        if ($result >= 3) {
            $this->refreshTables($clean_zone, $clean_vote);
        }

        // Close the mysqli connection.
        $stmt->close();

    }

    /*
     * refreshTables() : This function updates the tables: avilability, zones.
     *
     * @param : $zone - User selected zone value.
     *          $vote - User selected vote value.
     * @return : none.
     */
    private function refreshTables($zone, $vote) {
        /* Purify incoming parameters.*/
        $clean_zone = $this->purifier->purify($zone);
        $clean_vote = $this->purifier->purify($vote);
        
        // Get the slim singleton instance.
        $app = \Slim\Slim::getInstance();
        
        // Variable declaration and initializiation with default values.
        $reQuery1 = null;
        $reQuery2 = 'UPDATE availability
                     SET near_empty = 0,
                         part_full = 0,
                         near_full = 0
                     WHERE id = ?';
        
        /*
         * Set of conditional statements testing user selected vote.
         *  i.) The selected vote updates the database table 'zones'
         *       and increments the rows in the appropriate column by one.
         *
         *      Note: The rows are the id = ? and are bound  by the statement
         *                     $stmt->bind_param('i', $zone);
         *      Note2: Switch statement would be more elegant and clear solution. (DK)
         */
        if ($clean_vote == 1) {
            $reQuery1 = 'UPDATE zones
                            SET spots_aval = spots,
                                color = 0
                            WHERE id = ?';
        }
        elseif ($clean_vote == 2) {
            $reQuery1 = 'UPDATE zones
                            SET spots_aval = spots/2,
                                color = 1
                            WHERE id = ?';
        }
        elseif ($clean_vote == 3) {
            $reQuery1 = 'UPDATE zones
                            SET spots_aval = spots/5,
                                color = 2
                            WHERE id = ?';
        }
        
        /*
         * I.) Prepares the SQL query, and returns a statement handle to be used for
         *      further operations on the statement. The query must consist of a single
         *      SQL statement.
         *
         * II.) $reQuery1 - updates the zone tables.
         *
         */
        $stmt = $this->connection->prepare($reQuery1);
        // Bind the user selected zone to the id parameter in the WHERE SQL statement.
        $stmt->bind_param('i', $clean_zone);
        // Execute the mysqli query.
        $stmt->execute();

        /*
         * I.) Prepares the SQL query, and returns a statement handle to be used for
         *      further operations on the statement. The query must consist of a single
         *      SQL statement.
         *
         * II.) $reQuery2 - updates the avaliability table.
         *
         */
        $stmt = $this->connection->prepare($reQuery2);
        // Bind the user selected zone to the id parameter in the WHERE SQL statement.
        $stmt->bind_param('i', $clean_zone);
        // Execute the mysqli query.
        $stmt->execute();
        // Close the mySqli connection.
        $stmt->close();
    }
    
    /*
     * expert() : Updates the database with expert mode submitted values.
     *
     * @param : $zone - User selected zone value.
     *          $vote - User selected vote value.
     * @return : none.
     *
     */
    function expert($zone, $vote) {
    /* Purify incoming parameters.*/
    $clean_zone = $this->purifier->purify($zone);
    $clean_vote = $this->purifier->purify($vote);
	
    // Get the slim singleton instance.
	$app = \Slim\Slim::getInstance();

    // Refresh database tables: zone, avaliability. With expert supplied parameters.
	$this->refreshTables($clean_zone, $clean_vote);
    echo "zone reset";
    }

    /*
     * vectorsub() : Subtract vector data from zone information from users.
     *
     * @param : $zone
     * @return : none
     */
    function vectorsub($zone) {
    /* Purify incoming parameters.*/
    $clean_zone = $this->purifier->purify($zone);
	
    // Get the slim singleton instance.
	$app = \Slim\Slim::getInstance();

    // Variable declaration and initialization.
	$spots = null;
	$spots_aval = null;
	$color = null;
	$vQuery1 = 'UPDATE zones
		    SET spots_aval = spots_aval - 1
		    WHERE id = ?';
	$vQuery2 = 'SELECT spots, spots_aval, color
		    FROM zones
		    WHERE id = ?';

    /* Perform mysqli query with the $zone parameter.
     *  i.) Update the zones table and set the rows WHERE id = $zone
     *       in column spots_aval to one less than current value.
     */
	$stmt = $this->connection->prepare($vQuery1);
	$stmt->bind_param('i', $clean_zone);
	$stmt->execute();

    /*
     * Perform mysqli query with the $clean_zone parameter.
     *  i.) SELECT columns (spots, spots_aval, color) from the table zones
     *       WHERE id = $clean_zones.
     */
	$stmt = $this->connection->prepare($vQuery2);
	$stmt->bind_param('i', $clean_zone);
	$stmt->execute();
	
    // Bind the results to the variable.
    $stmt->bind_result($spots, $spots_aval, $color);
    
    /*
     * I.) While there is values avaliable from the query.
     *  1.) Print to output the spots, spots avaliable, and color.
     *  2.) Refresh the database tables dependent upon the following algorithm:
     *       i.)
     */
	while ($stmt->fetch()) {
	    echo "$spots  $spots_aval  $color";
        }
	
	if (($spots_aval <= ($spots / 5))) {
	    $this->refreshTables($clean_zone, 3);
	} elseif (($spots_aval <= ($spots / 2)) and ($spots_aval > ($spots / 5)) and ($color <> 1)) {
	    $this->refreshTables($clean_zone, 2);
	} elseif (($spots_aval > ($spots / 2)) and ($color <> 0)) {
	    $this->refreshTables($clean_zone, 1);
	}
    
    // Close mysqli connection.
	$stmt->close();
    }

    /*
     * vectoradd() : Add vector data from zone information from users.
     *
     * @param : $zone - 
     * @return : none
     *
     */
    function vectoradd($zone) {
    /* Purify incoming parameters.*/
    $clean_zone = $this->purifier->purify($zone);

    // Get Slim singleton instance.
	$app = \Slim\Slim::getInstance();

    // Variable declaration and assignments.
	$spots = null;
	$spots_aval = null;
	$color = null;
	$vQuery1 = 'UPDATE zones
		    SET spots_aval = spots_aval + 1
		    WHERE id = ?';
	$vQuery2 = 'SELECT spots, spots_aval, color
		    FROM zones
		    WHERE id = ?';

    /*
     * Perform mysqli query with the $zone parameter.
     *  i.) UPDATE table zones. Set rows with col = spots_aval
     *       WHERE id = $clean_zones to current value plus one.
     */
	$stmt = $this->connection->prepare($vQuery1);
	$stmt->bind_param('i', $clean_zone);
	$stmt->execute();

    /*
     * Perform mysqli query with the $clean_zone parameter.
     *  i.) Select the columns spots, spots_aval, color From the table zones
     *       WHERE the id = $clean_zones.
     */
	$stmt = $this->connection->prepare($vQuery2);
	$stmt->bind_param('i', $clean_zone);
	$stmt->execute();
        
    // Bind the results to the variable $stmt.
	$stmt->bind_result($spots, $spots_aval, $color);
    
    /*
     * I.) While results exists.
     *  1.) Print the results to output.
     */
	while ($stmt->fetch()) {
	    echo "$spots  $spots_aval  $color";
	}
        
    /* 
     * If the spots_aval falls within the following conditions print the specified colot to output. 
     *
     */
	if (($spots_aval > ($spots / 2))) {
	    $this->changeColor($clean_zone, 1);
	} elseif (($spots_aval <= ($spots / 2)) and ($spots_aval > ($spots / 5)) and ($color <> 1)) {
	    $this->changeColor($clean_zone, 2);
	} elseif (($spots_aval < ($spots / 5)) and ($color <> 2)) {
	    $this->changeColor($clean_zone, 3);
	}

    // Close the mysqli connection.
	$stmt->close();
    }

    /*
     * changeColor() : Change colors based on the user selected data.
     * @param : $zone - User selected zone value.
     *          $vote - User selected vote value.
     * @return : none
     */
    private function changeColor($zone, $vote) {
    /* Purify incoming parameters.*/    
    $clean_zone = $this->purifier->purify($zone);
    $clean_vote = $this->purifier->purify($vote);
    
    // Get the Slim singleton instance
	$app = \Slim\Slim::getInstance();
	
    // Variable declaration and initialization.
	$reQuery1 = null;
	$reQuery2 = 'UPDATE availability
		     SET near_empty = 0,
			 part_full = 0,
		 	 near_full = 0
		     WHERE id = ?';

    /*
     * If the $vote paramter falls within the following conditions update that database table zones.
     *  Note: Think a switch statement would be more elegant. (DK)
     */
	if ($clean_vote == 1) {
	    $reQuery1 = 'UPDATE zones
			    SET color = 0
			    WHERE id = ?';
	} elseif ($clean_vote == 2) {
	    $reQuery1 = 'UPDATE zones
			    SET color = 1
			    WHERE id = ?';
	} elseif ($clean_vote == 3) {
	    $reQuery1 = 'UPDATE zones
			    SET color = 1
			    WHERE id = ?';
	}

    /*
     * Perform an mysqli query with the parameter $reQuery1.
     *  i.) Update the table zones, WHERE id = $clean_vote.
     */
	$stmt = $this->connection->prepare($reQuery1);
	$stmt->bind_param('i', $clean_zone);
	$stmt->execute();

    /*
     * Perform a mysqli query with the parameter $reQuer2.
     *  i.) Clear the avaliablity table.
     */
	$stmt = $this->connection->prepare($reQuery2);
	$stmt->bind_param('i', $clean_zone);
	$stmt->execute();
    
    // Close the mysqli connection.
	$stmt->close();
    }
    
    /*
     * resetZones() : Reset the zones to default values.
     * @param : $vote - User selected vote value.
     * @return : none
     */
    function resetZones($vote) {
    $clean_vote = $this->purifier->purify($vote);
    
    // Get the Slim singleton instance value.
	$app = \Slim\Slim::getInstance();

    // Variable declaration and initialiazation.
	$query1 = null;
	$query2 = 'UPDATE availability
		   SET near_empty = 0,
		       part_full = 0,
		       near_full = 0';

    /*
     * If the $vote falls within the following conditions UPDATE the table zones by setting the 
     *   columns spots_aval to the appropriate values upon matching the condition. 
     *  Note: Swith statement :) (DK)
     */
	if ($clean_vote == 0) {
	    $query1 = 'UPDATE zones
			  SET spots_aval = spots,
			      color = 0';
	} else {
	    $query1 = 'UPDATE zones
			  SET spots_aval = spots/5,
			      color = 2';
	}

    // Prepare the mysqli statement and execute.
	$stmt = $this->connection->prepare($query1);
	$stmt->execute();
	$stmt = $this->connection->prepare($query2);
	$stmt->execute();

    // Close the mysqli communication.
	$stmt->close();

    echo "Zones reset";
    }
}

?>
