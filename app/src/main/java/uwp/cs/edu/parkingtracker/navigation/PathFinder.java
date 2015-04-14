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
//import java.util.List;
//import java.util.Map;
//
//import org.jgrapht.alg.DijkstraShortestPath;
//import org.jgrapht.graph.DefaultWeightedEdge;
//
//import com.google.android.gms.maps.model.PolylineOptions;
//
//import edu.uwp.campusnavigation.config.Building;
//import edu.uwp.campusnavigation.config.Level;
//import edu.uwp.campusnavigation.config.Level.LevelName;
//import edu.uwp.campusnavigation.config.Node;
//import edu.uwp.campusnavigation.config.Node.NodeType;
//import edu.uwp.campusnavigation.config.Room;
//import edu.uwp.campusnavigation.config.Stairs;
//
///**
// * This abstract class provides the functionality for finding paths.
// *
// * @author Thomas Rippl
// *
// */
//public abstract class PathFinder {
//
//	/**
//	 * Searches for a path from the given start {@link Room} to the given
//	 * destination {@link Room} within the given {@link Building}. If no path
//	 * could be found, an empty {@link Map} is returned.
//	 *
//	 * @param building
//	 *            - in which the {@link Room}s are located.
//	 * @param start
//	 *            - the start {@link Room}.
//	 * @param destination
//	 *            - the destination {@link Room}.
//	 * @return A {@link Map} of @link{@link LevelName}s representing the levels
//	 *         and {@link PolylineOptions} representing the path for the given
//	 *         level to the destination. (e.g. path for level1: start to
//	 *         staircase, path for level2: staircase to destination).
//	 */
//	public abstract Map<LevelName, PolylineOptions> findPath(Building building,
//			Room start, Room destination);
//
//	/**
//	 * Searches for the closest stairs from the given start {@link Node} on the
//	 * given {@link Level} to reach the given destination level. If no path to
//	 * any stairs is available, <code>null</code> is returned.
//	 *
//	 * @param startLevel
//	 *            - on which to search for the closest stairs.
//	 * @param start
//	 *            - the start {@link Node}.
//	 * @param destinationLevel
//	 *            - to be reached by stairs.
//	 * @return The {@link Node} representing the closest stairs, or
//	 *         <code>null</code> if no path to any stairs is available
//	 */
//	protected Node findClosestStairsToLevel(Level startLevel, Node start,
//			Level destinationLevel) {
//		List<Node> startLevelStairs = startLevel
//				.getNodesByType(NodeType.STAIRS);
//		List<Node> destLevelStairs = destinationLevel
//				.getNodesByType(NodeType.STAIRS);
//
//		for (int i = startLevelStairs.size() - 1; i >= 0; i--) {
//
//			Stairs startStairs = (Stairs) startLevelStairs.get(i);
//			boolean stairsConnect = false;
//
//			for (Node destNode : destLevelStairs) {
//				Stairs destStairs = (Stairs) destNode;
//				if (startStairs.stairsConnectWithEachOther(destStairs)) {
//					stairsConnect = true;
//					break;
//				}
//			}
//
//			if (!stairsConnect) {
//				startLevelStairs.remove(i);
//			}
//		}
//
//		return findClosestNodeInList(startLevel, start, startLevelStairs);
//	}
//
//	/**
//	 * Searches for the closest node in the given {@link List} from the given
//	 * start {@link Node}. The {@link Node}s in the {@link List} as well as the
//	 * start {@link Node}, are expected to be on the same level. If no path to
//	 * any node is available, <code>null</code> is returned.
//	 *
//	 * @param level
//	 *            - on which to search for the closest node.
//	 * @param start
//	 *            - the start {@link Node}.
//	 * @param nodes
//	 *            - the {@link List} to find the closest {@link Node} in.
//	 * @return The closest {@link Node} in the {@link List}, or
//	 *         <code>null</code> if no path to any node is available
//	 */
//	protected Node findClosestNodeInList(Level level, Node start,
//			List<Node> nodes) {
//		Node closestNode = null;
//		double pathLength = Double.POSITIVE_INFINITY;
//
//		for (Node node : nodes) {
//			DijkstraShortestPath<Node, DefaultWeightedEdge> dsp = new DijkstraShortestPath<Node, DefaultWeightedEdge>(
//					level.getGraph(), start, node);
//			if (dsp.getPathLength() < pathLength) {
//				pathLength = dsp.getPathLength();
//				closestNode = node;
//			}
//		}
//		return closestNode;
//	}
//
//}
