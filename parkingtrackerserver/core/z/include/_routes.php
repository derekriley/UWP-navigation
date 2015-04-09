

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
    
function jsonResponse($httpStatus, $response) {
    
    /* Function call to the slim framework. 
     *  When the parameter field is left empty returns the default instance allready created
     *   in the index.php file. If no instance has been created returns null.
     */
    $app = \Slim\Slim::getInstance();
    
    /**
     * Set the HTTP response status code
     * @param : int
     */
    $app->status($httpStatus);
    
    /**
     * Set the HTTP response Content-Type
     * @param  string   $type   
     * 
     * i.) The Content-Type for the Response (ie. text/html)
     */
    $app->contentType('application/json');

    /* i.) echo : is one of many print functions of the php library.
     * ii.) json_encode : Returns the JSON representation of a value.
     */
    echo json_encode($response);
}

/**
 * Shows zones.
 * method GET
 * url /z
 */
function getZones() {
    $db = new DbTools();
    $db->getZones();
}

/**
 * Shows grid points
 * method GET
 * url /g
 */
function getGrid() {
    $db = new DbTools();
    $db->getGrid();
}

/**
 * Shows availability
 * method GET
 * url /a
 */
function getAvail() {
    $db = new DbTools();
    $db->getAvail();
}

function update($zone, $vote) {
    $db = new DbTools();
    $db->update($zone, $vote);
}

function expert($zone, $vote, $username, $password) {
    // Check to see if the user : name and password, are valid.
    if(UserValidation::validateUser($username, $password) == 1){
        $db = new DbTools();
        $db->expert($zone, $vote);
    }
    /* Exit application */
    exit();
}

function vectorsub($zone, $username, $password) {
    // Check to see if the user : name and password, are valid.
    if(UserValidation::validateUser($username, $password) == 1){
    $db = new DbTools();
    $db->vectorsub($zone);
    }
    /* Exit application */
    exit();
}

function vectoradd($zone, $username, $password) {
    // Check to see if the user : name and password, are valid.
    if(UserValidation::validateUser($username, $password) == 1){
    $db = new DbTools();
    $db->vectoradd($zone);
    }
    /* Exit application */
    exit();
}

function resetZones($vote, $username, $password) {
    // Check to see if the user : name and password, are valid.
    if(UserValidation::validateUser($username, $password) == 1){
    $db = new DbTools();
    $db->resetZones($vote);
    }
    /* Exit application */
    exit();
}

/* Temporary function to add new users to the database, granting the new
 *  user rank of (1) : non-expert.
 */
function addUser($user_name, $password) {
    UserValidation::createUser($user_name, $password);
}

/*
 Fallback function created during the setup proceedure of PHP documentation.
 */
function hello() {
    echo "<h1>Nothing to see here at: <code>/</code></h1>";
}

?>
