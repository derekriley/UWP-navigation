/*
 * Copyright 2014 Thomas Rippl, Adam Olszewski, Brandon Willoughby, Able Rouse
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.uwp.campusnavigation.routing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.google.android.gms.maps.model.PolylineOptions;

import edu.uwp.campusnavigation.config.Building;
import edu.uwp.campusnavigation.config.Level;
import edu.uwp.campusnavigation.config.Level.LevelName;
import edu.uwp.campusnavigation.config.Node;
import edu.uwp.campusnavigation.config.Room;
import edu.uwp.campusnavigation.config.Stairs;
import edu.uwp.campusnavigation.tools.PolylineUtils;

/**
 * This implementation of the {@link PathFinder} interface is to be used, when
 * there are no restrictions (e.g. "no stairs") for the path to be found.
 * 
 * @author Thomas Rippl
 * 
 */
public class DefaultPathFinder extends PathFinder {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<LevelName, PolylineOptions> findPath(Building building,
			Room start, Room destination) {

		Map<LevelName, PolylineOptions> paths = new HashMap<LevelName, PolylineOptions>();
		String startLevelString = start.getRoomNumber().substring(0, 2);
		String destinationLevelString = destination.getRoomNumber().substring(
				0, 2);
		Level startLevel = building.getLevelByName(LevelName
				.valueOf(startLevelString));

		if (startLevelString.equalsIgnoreCase(destinationLevelString)) {
			paths.putAll(findPathBetweenRoomsOnSameLevel(startLevel, start,
					destination));
		} else {
			Level destinationLevel = building.getLevelByName(LevelName
					.valueOf(destinationLevelString));
			paths.putAll(findPathBetweenRoomsOnDifferentLevels(start,
					startLevel, destination, destinationLevel));
		}

		return paths;
	}

	/**
	 * Searches for a path from the given start {@link Room} to the given
	 * destination {@link Room} within the given {@link Level}. If no path could
	 * be found, an empty {@link Map} is returned.
	 * 
	 * @param level
	 *            - the {@link Level} the {@link Room}s are located on.
	 * @param start
	 *            - the start {@link Room}.
	 * @param destination
	 *            - the destination {@link Room}.
	 * @return A {@link Map} of @link{@link String}s representing the levels and
	 *         {@link PolylineOptions} representing the path for the given level
	 *         to the destination. This map only has one entry.
	 */
	private Map<LevelName, PolylineOptions> findPathBetweenRoomsOnSameLevel(
			Level level, Room start, Room destination) {
		Map<LevelName, PolylineOptions> paths = new HashMap<LevelName, PolylineOptions>();

		// find path from start to destination
		List<DefaultWeightedEdge> path = DijkstraShortestPath.findPathBetween(
				level.getGraph(), start, destination);

		paths.put(level.getLevelName(),
				PolylineUtils.createPolylineForPath(level.getGraph(), path));

		return paths;
	}

	/**
	 * Searches for a path from the given start {@link Room} located on the
	 * given start {@link Level} to the given destination {@link Room} located
	 * on the given destination {@link Level}. If no path could be found, an
	 * empty {@link Map} is returned.
	 * 
	 * @param startRoom
	 *            - the start {@link Room}.
	 * @param startLevel
	 *            - the {@link Level} the start room is located on.
	 * @param destinationRoom
	 *            - the destination {@link Room}.
	 * @param destinationLevel
	 *            - the {@link Level} the destination room is located on.
	 * @return A {@link Map} of @link{@link String}s representing the levels and
	 *         {@link PolylineOptions} representing the path for the given level
	 *         to the destination. (e.g. path for level1: start to staircase,
	 *         path for level2: staircase to destination).
	 */
	private Map<LevelName, PolylineOptions> findPathBetweenRoomsOnDifferentLevels(
			Room startRoom, Level startLevel, Room destinationRoom,
			Level destinationLevel) {

		Map<LevelName, PolylineOptions> paths = new HashMap<LevelName, PolylineOptions>();

		// find closest stairs to the destination room to direct to
		Stairs stairs = (Stairs) findClosestStairsToLevel(destinationLevel,
				destinationRoom, startLevel);

		if (stairs != null) {

			// find path from destination to closest stairs
			List<DefaultWeightedEdge> destinationLevelPath = DijkstraShortestPath
					.findPathBetween(destinationLevel.getGraph(), destinationRoom, stairs);

			// find path from closest stairs to start
			Node startLevelStairs = startLevel.getStairsByHelperLatLng(
					stairs.getHelperLat(), stairs.getHelperLng());
			List<DefaultWeightedEdge> startLevelPath = DijkstraShortestPath
					.findPathBetween(startLevel.getGraph(),
							startLevelStairs, startRoom);

			// add results to map
			paths.put(startLevel.getLevelName(), PolylineUtils
					.createPolylineForPath(startLevel.getGraph(),
							startLevelPath));
			paths.put(
					destinationLevel.getLevelName(),
					PolylineUtils.createPolylineForPath(
							destinationLevel.getGraph(), destinationLevelPath));
		}

		return paths;
	}
}
