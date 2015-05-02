/*
 * Copyright 2014 University Of Wisconsin Parkside
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uwp.cs.edu.parkingtracker.navigation;

import java.util.ArrayList;


/**
 * Created by Able on 4/27/2015.
 */
class Vertex implements Comparable<Vertex> {

    public final String name;
    public ArrayList<Edge> adjacencies = new ArrayList();
    public ArrayList<String> neighbors = new ArrayList();
    public double minDistance = Double.POSITIVE_INFINITY;
    public Vertex previous;
    public double lat;
    public double lon;

    public Vertex(String argName) {
        name = argName;
    }

    public String toString() {
        return name;
    }

    public int compareTo(Vertex other) {
        return Double.compare(minDistance, other.minDistance);
    }

}


