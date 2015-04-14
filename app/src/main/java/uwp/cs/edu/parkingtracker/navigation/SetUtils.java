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
//import java.util.Set;
//
//import edu.uwp.campusnavigation.config.Node;
//
///**
// * This class provides functions on {@link Set}s
// *
// * @author Thomas Rippl
// *
// */
//public abstract class SetUtils {
//
//	/**
//	 * Returns the {@link Node} from the given {@link Set}, that has the given
//	 * id. If the no node could be found, <code>null</code> is returned.
//	 *
//	 * @param nodeSet
//	 *            - the set to be searched in.
//	 * @param id
//	 *            - the id of the node to be searched for.
//	 * @return The node with the given id, or <code>null</code> if the node
//	 *         could not be found
//	 */
//	public static Node findNodeById(Set<Node> nodeSet, String id) {
//		for (Node node : nodeSet) {
//			if (node.getId().equals(id)) {
//				return node;
//			}
//		}
//		return null;
//	}
//
//}
