///*
// * Copyright 2014 Thomas Rippl, Adam Olszewski, Brandon Willoughby, Able Rouse
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * 	http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package uwp.cs.edu.parkingtracker.navigation;
//
//import edu.uwp.campusnavigation.config.Node;
//
///**
// * This class provides the functionality to calculate the distance between two
// * objects.
// *
// * @author Thomas Rippl
// *
// */
//public abstract class DistanceCalculator {
//
//	/**
//	 * The radius of the earth.
//	 */
//	private static final int RADIUS = 6371;
//
//	/**
//	 * Calculates the distance between the two given latitude and longitude
//	 * pairs.
//	 *
//	 * @param lat1
//	 *            - start latitude.
//	 * @param lat2
//	 *            - end latitude.
//	 * @param lon1
//	 *            - start longitude.
//	 * @param lon2
//	 *            - end longitude.
//	 * @return the distance between the two given latitude and longitude pairs.
//	 */
//	public static double calculateDistance(double lat1, double lat2,
//			double lon1, double lon2) {
//
//		Double latDistance = deg2rad(lat2 - lat1);
//		Double lonDistance = deg2rad(lon2 - lon1);
//		Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
//				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
//				* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
//		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//		double distance = RADIUS * c * 1000; // convert to meters
//		return distance;
//	}
//
//	/**
//	 * Calculates the distance between the two given @link Node}s, by calling
//	 * {@link #calculateDistance(double, double, double, double)} with their
//	 * latitude and longitude values.
//	 *
//	 * @param node1
//	 *            - start node.
//	 * @param node2
//	 *            - end node.
//	 * @return the distance between the two nodes.
//	 */
//	public static double calculateDistance(Node node1, Node node2) {
//		return calculateDistance(node1.getLat(), node2.getLat(),
//				node1.getLng(), node2.getLng());
//	}
//
//	/**
//	 * Converts the given degree to radius.
//	 *
//	 * @param deg
//	 *            - to be converted to radius.
//	 * @return the radius value of the degree.
//	 */
//	private static double deg2rad(double deg) {
//		return (deg * Math.PI / 180.0);
//	}
//}
