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

package edu.uwp.campusnavigation.tools;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import edu.uwp.campusnavigation.config.Node;

/**
 * This class provides functions to create {@link PolylineOptions}
 * 
 * @author Thomas Rippl
 * 
 */
public abstract class PolylineUtils {

	/**
	 * Creates {@link PolylineOptions} to display the given path, using the
	 * given graph.
	 * 
	 * @param graph
	 *            - containing the edges of the path.
	 * @param path
	 *            - to be displayed.
	 * @return the {@link PolylineOptions} representing the given path. If the
	 *         path is <code>null</code>, empty PolylineOptions are returned.
	 */
	public static PolylineOptions createPolylineForPath(
			Graph<Node, DefaultWeightedEdge> graph,
			List<DefaultWeightedEdge> path) {
		PolylineOptions po = new PolylineOptions().color(Color.GREEN);
		if (path == null) {
			return po;
		}
		for (int i = 0; i < path.size(); i++) {
			DefaultWeightedEdge edge = path.get(i);
			if (i == 0) {
				Node start = (Node) graph.getEdgeSource(edge);
				po.add(new LatLng(start.getLat(), start.getLng()));
			}
			Node point = (Node) graph.getEdgeTarget(edge);
			po.add(new LatLng(point.getLat(), point.getLng()));
		}
		return po;
	}
}
