

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
 Require the slim framework for the project.
 */
require './libs/Slim/Slim.php';
require_once './include/DbTools.php';
include('./include/_routes.php');
include('./include/UserValidation.php');


/*
 Register the slim framework's autoloader function.
 */
\Slim\Slim::registerAutoloader();

/*
 Instantiate a Slim application.
 */
$app = new \Slim\Slim();
    
/*
 I) Provide initial application settings.
   a)  multiple settings use:
         $app->config(array(
            'debug' =>,
            ....));
 */
$app->config('debug', true);

/*
 Define a set of HTTP Get routes.
    i.) Get routes are defined as key=value pairs.
    ii.) Data is visible to everyone.
    iii.) Less secure than post.
 */
$app->get('/', 'hello');
$app->get('/z/', 'getZones');
$app->get('/g/', 'getGrid');
$app->get('/a/', 'getAvail');

$app->get('/update/:zone/:vote/:username/:password', function($zone, $vote, $username, $password) use ($app) {
	update($zone, $vote, $username, $password);
});

$app->get('/expert/:zone/:vote/:username/:password', function($zone, $vote, $username, $password) use ($app) {
        expert($zone, $vote, $username, $password);
});

$app->get('/vectorsub/:zone/:username/:password', function($zone, $username, $password) use ($app) {
       vectorsub($zone, $username, $password);
});

$app->get('/vectoradd/:zone/:username/:password', function($zone, $username, $password) use ($app) {
	vectoradd($zone, $username, $password);
});

$app->get('/reset/:vote/:username/:password', function($vote, $username, $password) use ($app) {
	resetZones($vote, $username, $password);
});

$app->get('/adduser/:name/:password', function($user_name, $password) use($app) {
          adduser($user_name, $password);
          });

/*
 Run the slim application.
 */
$app->run();

?>
