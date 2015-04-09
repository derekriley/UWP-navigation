


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
 * Documentation added:
 * @author : David Krawchuk
 * @date : October 6 2014
 *
 *  i.) This document contains only constants.
 *
 *  ii.) Known files using :
 *    1.) connect.php file.
 *
 *  iii.) Syntax of constant declaration:
 *    1.) define('CONSTANT', direct_text_replacement);
 *
 *  iiii.) Note that define is a function call from the PHP library.
 */

// Database configuration
define('DB_USERNAME', 'myuser');
define('DB_PASSWORD', 'pwd123');
define('DB_HOST', 'localhost');
define('DB_NAME', 'parkingtracker');
define('DB_USERBASE_NAME', 'userbase');
    
define('QUERY_EXECUTED_SUCCESSFULLY', 0);
define('USER_CREATE_FAILED', 1);
define('USER_ALREADY_EXISTED', 2);
?>
