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

package uwp.cs.edu.parkingtracker;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Contains all constant points that make up all the zones
 * */
public final class CONSTANTS {

    // Prevent instantiation, even from the native class
    private CONSTANTS() {
        throw new AssertionError();
    }

    // Defines a key for extra Intent
    public static final String DATA_STATUS = "uwp.cs.parkingtracker.STATUS";

    // Defines a key for extra Intent
    public static final String DATA_AMOUNT = "uwp.cs.parkingtracker.AMOUNT";
    //shared preferences
    public static final String PREFS_NAME = "UwpNavSettings";

    // REST related strings
    public static final String REST_URL = "http://cinnamon.cs.uwp.edu/parking/app.php/";
    public static final String VOTE = "v/";
    public static final String VOTE_AVG = "va/";
    public static final String TEST = "test";
    public static final String USER = "android";
    public static final String PASS = "m@g!calTh!ng$";

    public static final String RESET = "reset/";

    // Http Methods
    public static final String GET = "GET";
    public static final String PUT = "PUT";


    // Student Center Coordinates Begin Used for default map view
    public static final double STUDENT_CENTER_C_LAT = 42.648266;
    public static final double STUDENT_CENTER_C_LNG = -87.853041;

    //Talent (T) Coordinates Begin
    static final LatLng T_ROW_1_POINT_1 = new LatLng(42.649226, -87.848384);
    static final LatLng T_ROW_1_POINT_2 = new LatLng(42.649222, -87.847613);
    static final LatLng T_ROW_1_POINT_3 = new LatLng(42.649190, -87.846760);

    static final LatLng T_ROW_2_POINT_1 = new LatLng(42.648808, -87.848421);
    static final LatLng T_ROW_2_POINT_2 = new LatLng(42.648801, -87.847605);
    static final LatLng T_ROW_2_POINT_3 = new LatLng(42.648785, -87.846757);

    static final LatLng T_ROW_3_POINT_1 = new LatLng(42.648367, -87.848478);
    static final LatLng T_ROW_3_POINT_2 = new LatLng(42.648358, -87.847644);
    static final LatLng T_ROW_3_POINT_3 = new LatLng(42.648333, -87.846739);

    static final LatLng T_ROW_4_POINT_1 = new LatLng(42.647981, -87.848403);
    static final LatLng T_ROW_4_POINT_2 = new LatLng(42.647946, -87.847631);
    static final LatLng T_ROW_4_POINT_3 = new LatLng(42.647981, -87.846827);
    //Talent (T) Coordinates End

    //Talent (T) Polygons Begin
    static final PolygonOptions T_ZONE_1 = new PolygonOptions()
            .add(T_ROW_1_POINT_1, T_ROW_1_POINT_2, T_ROW_2_POINT_2, T_ROW_2_POINT_1);
    static final PolygonOptions T_ZONE_2 = new PolygonOptions()
            .add(T_ROW_1_POINT_2, T_ROW_1_POINT_3, T_ROW_2_POINT_3, T_ROW_2_POINT_2);
    static final PolygonOptions T_ZONE_3 = new PolygonOptions()
            .add(T_ROW_2_POINT_1, T_ROW_2_POINT_2, T_ROW_3_POINT_2, T_ROW_3_POINT_1);
    static final PolygonOptions T_ZONE_4 = new PolygonOptions()
            .add(T_ROW_2_POINT_2, T_ROW_2_POINT_3, T_ROW_3_POINT_3, T_ROW_3_POINT_2);
    static final PolygonOptions T_ZONE_5 = new PolygonOptions()
            .add(T_ROW_3_POINT_1, T_ROW_3_POINT_2, T_ROW_4_POINT_2, T_ROW_4_POINT_1);
    static final PolygonOptions T_ZONE_6 = new PolygonOptions()
            .add(T_ROW_3_POINT_2, T_ROW_3_POINT_3, T_ROW_4_POINT_3, T_ROW_4_POINT_2);
    //Talent (T) Polygons End

    //Student Center (A) Coordinates Begin
    static final LatLng A_ROW_1_POINT_1 = new LatLng(42.649125, -87.853941);

    static final LatLng A_ROW_2_POINT_1 = new LatLng(42.648511, -87.854008);
    static final LatLng A_ROW_2_POINT_2 = new LatLng(42.648516, -87.853350);
    static final LatLng A_ROW_2_POINT_3 = new LatLng(42.648455, -87.852173);

    static final LatLng A_ROW_3_POINT_1 = new LatLng(42.648095, -87.854203);
    static final LatLng A_ROW_3_POINT_2 = new LatLng(42.648077, -87.853363);
    static final LatLng A_ROW_3_POINT_3 = new LatLng(42.648071, -87.852174);

    static final LatLng A_ROW_4_POINT_1 = new LatLng(42.647685, -87.854255);
    static final LatLng A_ROW_4_POINT_2 = new LatLng(42.647670, -87.853375);
    static final LatLng A_ROW_4_POINT_3 = new LatLng(42.647653, -87.852202);

    static final LatLng A_ROW_5_POINT_1 = new LatLng(42.647479, -87.854288);
    static final LatLng A_ROW_5_POINT_2 = new LatLng(42.647420, -87.853387);
    static final LatLng A_ROW_5_POINT_3 = new LatLng(42.647421, -87.852199);
    //Student Center (A) Coordinates End

    //Student Center (A) Polygons Begin
    static final PolygonOptions A_ZONE_1 = new PolygonOptions()
            .add(A_ROW_1_POINT_1, A_ROW_2_POINT_3, A_ROW_2_POINT_2, A_ROW_2_POINT_1);
    static final PolygonOptions A_ZONE_2 = new PolygonOptions()
            .add(A_ROW_2_POINT_1, A_ROW_2_POINT_2, A_ROW_3_POINT_2, A_ROW_3_POINT_1);
    static final PolygonOptions A_ZONE_3 = new PolygonOptions()
            .add(A_ROW_2_POINT_2, A_ROW_2_POINT_3, A_ROW_3_POINT_3, A_ROW_3_POINT_2);
    static final PolygonOptions A_ZONE_4 = new PolygonOptions()
            .add(A_ROW_3_POINT_1, A_ROW_3_POINT_2, A_ROW_4_POINT_2, A_ROW_4_POINT_1);
    static final PolygonOptions A_ZONE_5 = new PolygonOptions()
            .add(A_ROW_3_POINT_2, A_ROW_3_POINT_3, A_ROW_4_POINT_3, A_ROW_4_POINT_2);
    static final PolygonOptions A_ZONE_6 = new PolygonOptions()
            .add(A_ROW_4_POINT_1, A_ROW_4_POINT_2, A_ROW_5_POINT_2, A_ROW_5_POINT_1);
    static final PolygonOptions A_ZONE_7 = new PolygonOptions()
            .add(A_ROW_4_POINT_2, A_ROW_4_POINT_3, A_ROW_5_POINT_3, A_ROW_5_POINT_2);
    //Student Center (A) Polygons End

    //Rita Main (B) Coordinates Begin
    static final LatLng B_ROW_1_POINT_1 = new LatLng(42.645479, -87.859190);
    static final LatLng B_ROW_1_POINT_2 = new LatLng(42.645479, -87.858650);
    static final LatLng B_ROW_1_POINT_3 = new LatLng(42.645479, -87.858105);
    static final LatLng B_ROW_1_POINT_4 = new LatLng(42.645446, -87.857409);

    static final LatLng B_ROW_2_POINT_1 = new LatLng(42.645105, -87.859239);
    static final LatLng B_ROW_2_POINT_2 = new LatLng(42.645094, -87.858665);
    static final LatLng B_ROW_2_POINT_3 = new LatLng(42.645089, -87.858115);
    static final LatLng B_ROW_2_POINT_4 = new LatLng(42.645065, -87.857405);

    static final LatLng B_ROW_3_POINT_1 = new LatLng(42.644728, -87.859244);
    static final LatLng B_ROW_3_POINT_2 = new LatLng(42.644719, -87.858675);
    static final LatLng B_ROW_3_POINT_3 = new LatLng(42.644705, -87.858349);
    static final LatLng B_ROW_3_POINT_4 = new LatLng(42.644705, -87.858128);
    static final LatLng B_ROW_3_POINT_5 = new LatLng(42.644671, -87.857422);

    static final LatLng B_ROW_4_POINT_1 = new LatLng(42.644529, -87.859247);
    static final LatLng B_ROW_4_POINT_2 = new LatLng(42.644486, -87.858366);
    static final LatLng B_ROW_4_POINT_3 = new LatLng(42.644463, -87.857418);
    //Rita Main (B) Coordinates End



    //Rita Main (B) Polygons Begin
    static final PolygonOptions B_ZONE_1 = new PolygonOptions()
            .add(B_ROW_1_POINT_1, B_ROW_1_POINT_2, B_ROW_2_POINT_2, B_ROW_2_POINT_1);
    static final PolygonOptions B_ZONE_2 = new PolygonOptions()
            .add(B_ROW_1_POINT_2, B_ROW_1_POINT_3, B_ROW_2_POINT_3, B_ROW_2_POINT_2);
    static final PolygonOptions B_ZONE_3 = new PolygonOptions()
            .add(B_ROW_1_POINT_3, B_ROW_1_POINT_4, B_ROW_2_POINT_4, B_ROW_2_POINT_3);
    static final PolygonOptions B_ZONE_4 = new PolygonOptions()
            .add(B_ROW_2_POINT_1, B_ROW_2_POINT_2, B_ROW_3_POINT_2, B_ROW_3_POINT_1);
    static final PolygonOptions B_ZONE_5 = new PolygonOptions()
            .add(B_ROW_2_POINT_2, B_ROW_2_POINT_3, B_ROW_3_POINT_4, B_ROW_3_POINT_2);
    static final PolygonOptions B_ZONE_6 = new PolygonOptions()
            .add(B_ROW_2_POINT_3, B_ROW_2_POINT_4, B_ROW_3_POINT_5, B_ROW_3_POINT_4);
    static final PolygonOptions B_ZONE_7 = new PolygonOptions()
            .add(B_ROW_3_POINT_1, B_ROW_3_POINT_3, B_ROW_4_POINT_2, B_ROW_4_POINT_1);
    static final PolygonOptions B_ZONE_8 = new PolygonOptions()
            .add(B_ROW_3_POINT_3, B_ROW_3_POINT_5, B_ROW_4_POINT_3, B_ROW_4_POINT_2);
    //Rita Main (B) Polygons End

    //Rita Overflow (C) Coordinates Begin
    static final LatLng C_ROW_1_POINT_1 = new LatLng(42.644346, -87.858951);
    static final LatLng C_ROW_1_POINT_2 = new LatLng(42.644347, -87.858080);
    static final LatLng C_ROW_1_POINT_3 = new LatLng(42.644342, -87.857582);

    static final LatLng C_ROW_2_POINT_1 = new LatLng(42.643981, -87.858963);
    static final LatLng C_ROW_2_POINT_2 = new LatLng(42.643957, -87.858093);
    static final LatLng C_ROW_2_POINT_3 = new LatLng(42.643940, -87.857292);

    static final LatLng C_ROW_3_POINT_1 = new LatLng(42.643708, -87.85871);
    static final LatLng C_ROW_3_POINT_2 = new LatLng(42.643674, -87.858041);
    static final LatLng C_ROW_3_POINT_3 = new LatLng(42.643665, -87.857145);

    static final LatLng C_ROW_4_POINT_1 = new LatLng(42.643407, -87.858041);
    static final LatLng C_ROW_4_POINT_2 = new LatLng(42.643386, -87.857100);
    //Rita Overflow (C) Coordinates End

    //Rita Overflow (C) Polygons Begin
    static final PolygonOptions C_ZONE_1 = new PolygonOptions()
            .add(C_ROW_1_POINT_1, C_ROW_1_POINT_2, C_ROW_2_POINT_2, C_ROW_2_POINT_1);
    static final PolygonOptions C_ZONE_2 = new PolygonOptions()
            .add(C_ROW_1_POINT_2, C_ROW_1_POINT_3, C_ROW_2_POINT_3, C_ROW_2_POINT_2);
    static final PolygonOptions C_ZONE_3 = new PolygonOptions()
            .add(C_ROW_2_POINT_1, C_ROW_2_POINT_2, C_ROW_3_POINT_2, C_ROW_4_POINT_1, C_ROW_3_POINT_1);
    static final PolygonOptions C_ZONE_4 = new PolygonOptions()
            .add(C_ROW_2_POINT_2, C_ROW_2_POINT_3, C_ROW_3_POINT_3, C_ROW_3_POINT_2);
    static final PolygonOptions C_ZONE_5 = new PolygonOptions()
            .add(C_ROW_3_POINT_2, C_ROW_3_POINT_3, C_ROW_4_POINT_2, C_ROW_4_POINT_1);
    //Rita Overflow (C) Polygons End

    //SAC Main (D) Coordinates Begin
    static final LatLng D_ROW_1_POINT_1 = new LatLng(42.641491, -87.856943);
    static final LatLng D_ROW_1_POINT_2 = new LatLng(42.641751, -87.856552);
    static final LatLng D_ROW_1_POINT_3 = new LatLng(42.641906, -87.856310);
    static final LatLng D_ROW_1_POINT_4 = new LatLng(42.641875, -87.855965);

    static final LatLng D_ROW_2_POINT_1 = new LatLng(42.641251, -87.856630);
    static final LatLng D_ROW_2_POINT_2 = new LatLng(42.641507, -87.856232);
    static final LatLng D_ROW_2_POINT_3 = new LatLng(42.641748, -87.855838);

    static final LatLng D_ROW_3_POINT_1 = new LatLng(42.640997, -87.856339);
    static final LatLng D_ROW_3_POINT_2 = new LatLng(42.641208, -87.856019);
    static final LatLng D_ROW_3_POINT_3 = new LatLng(42.641273, -87.855944);
    static final LatLng D_ROW_3_POINT_4 = new LatLng(42.641413, -87.855725);
    static final LatLng D_ROW_3_POINT_5 = new LatLng(42.641519, -87.855567);

    static final LatLng D_ROW_4_POINT_1 = new LatLng(42.640796, -87.856064);
    static final LatLng D_ROW_4_POINT_2 = new LatLng(42.640992, -87.855772);
    static final LatLng D_ROW_4_POINT_3 = new LatLng(42.641187, -87.855450);

    static final LatLng D_ROW_5_POINT_1 = new LatLng(42.640517, -87.855700);
    static final LatLng D_ROW_5_POINT_2 = new LatLng(42.640719, -87.855413);
    static final LatLng D_ROW_5_POINT_3 = new LatLng(42.640719, -87.855413);
    //SAC Main (D) Coordinates End

    //SAC Main (D) Polygons Begin
    static final PolygonOptions D_ZONE_1 = new PolygonOptions()
            .add(D_ROW_1_POINT_1, D_ROW_1_POINT_2, D_ROW_2_POINT_2, D_ROW_2_POINT_1);
    static final PolygonOptions D_ZONE_2 = new PolygonOptions()
            .add(D_ROW_1_POINT_2, D_ROW_1_POINT_3, D_ROW_1_POINT_4, D_ROW_2_POINT_3, D_ROW_2_POINT_2);
    static final PolygonOptions D_ZONE_3 = new PolygonOptions()
            .add(D_ROW_2_POINT_1, D_ROW_2_POINT_2, D_ROW_3_POINT_3, D_ROW_3_POINT_2, D_ROW_3_POINT_1);
    static final PolygonOptions D_ZONE_4 = new PolygonOptions()
            .add(D_ROW_2_POINT_2, D_ROW_2_POINT_3, D_ROW_3_POINT_5, D_ROW_3_POINT_4, D_ROW_3_POINT_3);
    static final PolygonOptions D_ZONE_5 = new PolygonOptions()
            .add(D_ROW_3_POINT_1, D_ROW_3_POINT_2, D_ROW_4_POINT_2, D_ROW_4_POINT_1);
    static final PolygonOptions D_ZONE_6 = new PolygonOptions()
            .add(D_ROW_3_POINT_2, D_ROW_3_POINT_3, D_ROW_3_POINT_4, D_ROW_4_POINT_3, D_ROW_4_POINT_2);
    static final PolygonOptions D_ZONE_7 = new PolygonOptions()
            .add(D_ROW_4_POINT_1, D_ROW_4_POINT_2, D_ROW_5_POINT_2, D_ROW_5_POINT_1);
    static final PolygonOptions D_ZONE_8 = new PolygonOptions()
            .add(D_ROW_4_POINT_2, D_ROW_4_POINT_3, D_ROW_5_POINT_3, D_ROW_5_POINT_2);
    //SAC Main (D) Polygons End

    //SAC Overflow (E) Coordinates Begin
    static final LatLng E_ROW_1_POINT_1 = new LatLng(42.643047, -87.858129);
    static final LatLng E_ROW_1_POINT_2 = new LatLng(42.643041, -87.857582);

    static final LatLng E_ROW_2_POINT_1 = new LatLng(42.642373, -87.858154);
    static final LatLng E_ROW_2_POINT_2 = new LatLng(42.642354, -87.857605);
    //SAC Overflow (E) Coordinates End

    //SAC Overflow (E) Polygons Begin
    static final PolygonOptions E_ZONE_1 = new PolygonOptions()
            .add(E_ROW_1_POINT_1, E_ROW_1_POINT_2, E_ROW_2_POINT_2, E_ROW_2_POINT_1);
    //SAC Overflow (E) Polygons End

    //Student Center Outline Coordinates Begin
    static final LatLng SC_ROW_1_POINT_1 = new LatLng (42.648062, -87.855849);
    static final LatLng SC_ROW_1_POINT_2 = new LatLng (42.648048, -87.854766);
    static final LatLng SC_ROW_2_POINT_1 = new LatLng (42.647773, -87.854766);
    static final LatLng SC_ROW_2_POINT_2 = new LatLng (42.647765, -87.85448);
    static final LatLng SC_ROW_3_POINT_1 = new LatLng (42.647343, -87.855845);
    static final LatLng SC_ROW_3_POINT_2 = new LatLng (42.647331, -87.854499);

    static final PolygonOptions SC_OUTLINE = new PolygonOptions()
            .add(SC_ROW_1_POINT_1, SC_ROW_1_POINT_2, SC_ROW_2_POINT_1, SC_ROW_2_POINT_2,
                    SC_ROW_3_POINT_2, SC_ROW_3_POINT_1);
    //Student Center Outline Coordinates End

    //Molinaro Hall Outline Coordinates Begin
    static final LatLng MOLN_ROW_1_POINT_1 = new LatLng(42.647207, -87.856888);
    static final LatLng MOLN_ROW_1_POINT_2 = new LatLng(42.647195, -87.855965);
    static final LatLng MOLN_ROW_2_POINT_1 = new LatLng(42.647023, -87.856901);
    static final LatLng MOLN_ROW_2_POINT_2 = new LatLng(42.647003, -87.854994);
    static final LatLng MOLN_ROW_2_POINT_3 = new LatLng(42.647017, -87.857083);
    static final LatLng MOLN_ROW_3_POINT_1 = new LatLng(42.646816, -87.85708);
    static final LatLng MOLN_ROW_3_POINT_2 = new LatLng(42.646773, -87.855586);
    static final LatLng MOLN_ROW_4_POINT_1 = new LatLng(42.646647, -87.855586);
    static final LatLng MOLN_ROW_4_POINT_2 = new LatLng(42.646651, -87.855028);

    static final PolygonOptions MOLN_OUTLINE = new PolygonOptions()
            .add(MOLN_ROW_1_POINT_1, MOLN_ROW_1_POINT_2, MOLN_ROW_2_POINT_3, MOLN_ROW_4_POINT_2,
                    MOLN_ROW_4_POINT_1, MOLN_ROW_3_POINT_2, MOLN_ROW_3_POINT_1, MOLN_ROW_2_POINT_1,
                    MOLN_ROW_2_POINT_2);
    //Molinaro Hall Outline Coordinates End

    //Greenquist Hall Outline Coordinates Begin
    static final LatLng GRNQ_ROW_1_POINT_1 = new LatLng(42.646652, -87.855866);
    static final LatLng GRNQ_ROW_1_POINT_2 = new LatLng(42.646654, -87.8549);
    static final LatLng GRNQ_ROW_2_POINT_1 = new LatLng(42.646244, -87.855216);
    static final LatLng GRNQ_ROW_2_POINT_2 = new LatLng(42.646254, -87.854913);
    static final LatLng GRNQ_ROW_3_POINT_1 = new LatLng(42.64578, -87.855897);
    static final LatLng GRNQ_ROW_3_POINT_2 = new LatLng(42.6458, -87.855234);

    static final PolygonOptions GRNQ_OUTLINE = new PolygonOptions()
            .add(GRNQ_ROW_1_POINT_1, GRNQ_ROW_1_POINT_2, GRNQ_ROW_2_POINT_2, GRNQ_ROW_2_POINT_1,
                    GRNQ_ROW_3_POINT_2, GRNQ_ROW_3_POINT_1);
    //Greenquist Hall Outline Coordinates End

    //Wyllie Hall Outline Coordinates Begin
    static final LatLng WHYL_ROW_1_POINT_1 = new LatLng(42.645925, -87.855242);
    static final LatLng WHYL_ROW_1_POINT_2 = new LatLng(42.645923, -87.854458);
    static final LatLng WHYL_ROW_2_POINT_1 = new LatLng(42.645798, -87.855591);
    static final LatLng WHYL_ROW_2_POINT_2 = new LatLng(42.6458, -87.855227);
    static final LatLng WHYL_ROW_3_POINT_1 = new LatLng(42.644963, -87.855573);
    static final LatLng WHYL_ROW_3_POINT_2 = new LatLng(42.644967, -87.854523);

    static final PolygonOptions WHYL_OUTLINE = new PolygonOptions()
            .add(WHYL_ROW_1_POINT_1, WHYL_ROW_1_POINT_2,WHYL_ROW_3_POINT_2, WHYL_ROW_3_POINT_1,
                    WHYL_ROW_2_POINT_1, WHYL_ROW_2_POINT_2);
    //Wyllie Hall Outline Coordinates End

    //Rita Hall Outline Coordinates Begin
    static final LatLng RITA_ROW_1_POINT_1 = new LatLng (42.645668, -87.85703);
    static final LatLng RITA_ROW_1_POINT_2 = new LatLng (42.645659, -87.856179);
    static final LatLng RITA_ROW_2_POINT_1 = new LatLng (42.645353, -87.856202);
    static final LatLng RITA_ROW_2_POINT_2 = new LatLng (42.645345, -87.855583);
    static final LatLng RITA_ROW_3_POINT_1 = new LatLng (42.644846, -87.85626);
    static final LatLng RITA_ROW_3_POINT_2 = new LatLng (42.644853, -87.8556);
    static final LatLng RITA_ROW_4_POINT_1 = new LatLng (42.644567, -87.857059);
    static final LatLng RITA_ROW_4_POINT_2 = new LatLng (42.644555, -87.856259);

    static final PolygonOptions RITA_OUTLINE = new PolygonOptions()
            .add(RITA_ROW_1_POINT_1, RITA_ROW_1_POINT_2, RITA_ROW_2_POINT_1, RITA_ROW_2_POINT_2,
                    RITA_ROW_3_POINT_2, RITA_ROW_3_POINT_1, RITA_ROW_4_POINT_2, RITA_ROW_4_POINT_1);
    //Rita Hall Outline Coordinates End

    //SAC Outline Coordinates Begin
    static final LatLng SAC_ROW_1_POINT_1 = new LatLng(42.643081, -87.857589);
    static final LatLng SAC_ROW_1_POINT_2 = new LatLng(42.643051, -87.855677);
    static final LatLng SAC_ROW_2_POINT_1 = new LatLng(42.642096, -87.857624);
    static final LatLng SAC_ROW_2_POINT_2 = new LatLng(42.642046, -87.855669);

    static final PolygonOptions SAC_OUTLINE = new PolygonOptions()
            .add(SAC_ROW_1_POINT_1, SAC_ROW_1_POINT_2, SAC_ROW_2_POINT_2, SAC_ROW_2_POINT_1);
    //SAC Outline Coordinates End

    // Map Constants
    public static final float DEFAULT_ZOOM_FACTOR = 17;
    public static final float DEFAULT_LINE_WIDTH = 5;

    public static final Map<String, LatLng> buildings = new HashMap<>();
    public static final Map<String, PolygonOptions> buildingOutlines = new HashMap<>();
    public static final Map<String, LatLng> parkingLots = new HashMap<>();
    public static final Map<String, PolygonOptions> zones = new HashMap<>();

    static {
        buildings.put("Greenquist Hall", new LatLng(42.646377, -87.855475));
        buildings.put("Sports and Activity Center", new LatLng(42.64253, -87.856661));
        buildings.put("Wyllie Hall", new LatLng(42.645505, -87.855124));
        buildings.put("Rita Tallent Picken", new LatLng(42.645257, -87.856353));
        buildings.put("Molinaro Hall", new LatLng(42.647024, -87.856256));
        buildings.put("Student Center", new LatLng(42.64782, -87.855225));
        //buildings.put("Library", new LatLng(42.645545, -87.854982));
        //buildings.put("University Apartments", new LatLng(42.648881, -87.857133));
        //buildings.put("Ranger Hall", new LatLng(42.646904, -87.857972));
        //buildings.put("Pike River Suites", new LatLng(42.648607, -87.854778));
        //buildings.put("Student Health And Counseling Center", new LatLng(42.64796, -87.846231));
        //buildings.put("Tallent Hall", new LatLng(42.647518, -87.847593));
        //buildings.put("Facilities Management Complex", new LatLng(42.649597, -87.847796));
        parkingLots.put("Parking Lot A", new LatLng(42.648019, -87.853306));
        parkingLots.put("Parking Lot B", new LatLng(42.645004, -87.858295));
        parkingLots.put("Parking Lot C", new LatLng(42.643951, -87.858026));
        parkingLots.put("Parking Lot D", new LatLng(42.6427, -87.857822));
        parkingLots.put("Parking Lot E", new LatLng(42.641504, -87.856192));
        parkingLots.put("Parking Lot T", new LatLng(42.64854, -87.847608));

        buildingOutlines.put("Student Center",SC_OUTLINE);
        buildingOutlines.put("Molinaro Hall", MOLN_OUTLINE);
        buildingOutlines.put("Greenquist Hall", GRNQ_OUTLINE);
        buildingOutlines.put("Wyllie Hall", WHYL_OUTLINE);
        buildingOutlines.put("The Rita", RITA_OUTLINE);
        buildingOutlines.put("Sports and Activity Center", SAC_OUTLINE);

        zones.put("Tallent_1", T_ZONE_1);
        zones.put("Tallent_2", T_ZONE_2);
        zones.put("Tallent_3", T_ZONE_3);
        zones.put("Tallent_4", T_ZONE_4);
        zones.put("Tallent_5", T_ZONE_5);
        zones.put("Tallent_6", T_ZONE_6);
        zones.put("Student_Center_1", A_ZONE_1);
        zones.put("Student_Center_2", A_ZONE_2);
        zones.put("Student_Center_3", A_ZONE_3);
        zones.put("Student_Center_4", A_ZONE_4);
        zones.put("Student_Center_5", A_ZONE_5);
        zones.put("Student_Center_6", A_ZONE_6);
        zones.put("Student_Center_7", A_ZONE_7);
        zones.put("Rita_Main_1", B_ZONE_1);
        zones.put("Rita_Main_2", B_ZONE_2);
        zones.put("Rita_Main_3", B_ZONE_3);
        zones.put("Rita_Main_4", B_ZONE_4);
        zones.put("Rita_Main_5", B_ZONE_5);
        zones.put("Rita_Main_6", B_ZONE_6);
        zones.put("Rita_Main_7", B_ZONE_7);
        zones.put("Rita_Main_8", B_ZONE_8);
        zones.put("Rita_Overflow_1", C_ZONE_1);
        zones.put("Rita_Overflow_2", C_ZONE_2);
        zones.put("Rita_Overflow_3", C_ZONE_3);
        zones.put("Rita_Overflow_4", C_ZONE_4);
        zones.put("Rita_Overflow_5", C_ZONE_5);
        zones.put("SAC_Main_1", D_ZONE_1);
        zones.put("SAC_Main_2", D_ZONE_2);
        zones.put("SAC_Main_3", D_ZONE_3);
        zones.put("SAC_Main_4", D_ZONE_4);
        zones.put("SAC_Main_5", D_ZONE_5);
        zones.put("SAC_Main_6", D_ZONE_6);
        zones.put("SAC_Main_7", D_ZONE_7);
        zones.put("SAC_Main_8", D_ZONE_8);
        zones.put("SAC_Overflow_1", E_ZONE_1);
    }
}
