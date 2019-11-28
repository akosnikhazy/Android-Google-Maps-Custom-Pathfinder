/**
 * Created by Ákos Nikházy 2019
 */

package yzahk.in.pathfindermap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.view.Window.FEATURE_NO_TITLE;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public static final String mapActivity = "yzahk.in.pathfindermap.DATA";

    private Spinner fromWhere;
    private Spinner toWhere;
    private Spinner avoid;

    private ListView avoidList;
    private TextView listDescription;

    private ArrayList<String> avoidListArray = new ArrayList<>();
    private ArrayAdapter avoidListArrayAdapter;
    private ArrayList<String> avoidedMessageList = new ArrayList<>();


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /**
         * get rid of the bar
         */
        requestWindowFeature(FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        /**
         *   GUI ini
         */
        fromWhere = findViewById(R.id.fromWhere);
        toWhere = findViewById(R.id.toWhere);

        avoid = findViewById(R.id.avoid);
        avoidList = findViewById(R.id.avoidList);
        avoidList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        avoidList.setVisibility(View.GONE);

        listDescription = findViewById(R.id.listDescription);
        listDescription.setVisibility(View.GONE);

        Button startButton = findViewById(R.id.startButton);

        /**
         *  spinners
         */
        List<StringWithTag> listFrom = new ArrayList<StringWithTag>();
        List<StringWithTag> listTo = new ArrayList<StringWithTag>();


        /**
         * We load the nodes and edges, they are
         * csv files in the raw folder
         *
         * this is a list of nodes that we want as starting points
         * preferably we should list many of them with names.
         *
         * menu_from_nodes.csv
         *
         * Example: 1,1412,1673,Node Name,1,1
         *
         * (ID,posX,posY,Node Name,level,importance)
         *
         */
        InputStream inputStreamFromNodes = getResources().openRawResource(R.raw.menu_from_nodes);
        new CSVParse(inputStreamFromNodes);
        List fromNodeList = CSVParse.read();

        listFrom.add(new StringWithTag(getString(R.string.dropdownFromWhere), "0"));

        for (Integer i = 0; i < fromNodeList.size(); i++) {
            String[] node = (String[]) fromNodeList.get(i);
            listFrom.add(new StringWithTag(node[3], node[0]));
        }

        InputStream inputStreamToNodes = getResources().openRawResource(R.raw.menu_to_nodes);
        new CSVParse(inputStreamToNodes);
        List toNodeList = CSVParse.read();

        listTo.add(new StringWithTag(getString(R.string.dropdownToWhere), "0"));

        for (Integer i = 0; i < toNodeList.size(); i++) {
            String[] node = (String[]) toNodeList.get(i);
            listTo.add(new StringWithTag(node[3], node[0]));
        }



        /**
         * menu_edges.csv
         *
         * Example: 1,2,121,3,Edge name
         *
         * (id,from node,to node,length,Edge name)
         *
         */
        InputStream inputStreamEdges = getResources().openRawResource(R.raw.menu_edges);
        new CSVParse(inputStreamEdges);

        Edge[] edges = CSVParse.readEdge();

        List<StringWithTag> avoidEdgeList = new ArrayList<StringWithTag>();
        avoidEdgeList.add(new StringWithTag("placeholder", "0"));
        for (Integer i = 0; i < edges.length; i++) {

            avoidEdgeList.add(new StringWithTag(edges[i].getEdgeName(), Integer.toString(edges[i].getEdgeId())));

        }

        List<StringWithTag> listAvoid = new ArrayList<StringWithTag>(avoidEdgeList);
        listAvoid.set(0, new StringWithTag(getString(R.string.dropdownAvoid), "0"));


        ArrayAdapter<StringWithTag> adapterFrom = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listFrom);

        listTo.set(0, new StringWithTag(getString(R.string.dropdownToWhere), "0"));


        ArrayAdapter<StringWithTag> adapterTo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listTo);
        ArrayAdapter<StringWithTag> adapterAvoid = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listAvoid);

        fromWhere.setAdapter(adapterFrom);
        toWhere.setAdapter(adapterTo);
        avoid.setAdapter(adapterAvoid);

        avoidListArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, avoidListArray);

        avoidList.setAdapter(avoidListArrayAdapter);


        avoidList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                avoidedMessageList.remove(position);
                avoidListArray.remove(position);
                avoidList.setAdapter(avoidListArrayAdapter);

                if (avoidListArrayAdapter.getCount() == 0) {

                    avoidList.setVisibility(View.GONE);
                    listDescription.setVisibility(View.GONE);

                }

            }


        });


        avoid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StringWithTag toAvoid = (StringWithTag) parent.getSelectedItem();
                if (toAvoid.tag != "0") {
                    if (!avoidListArray.contains(toAvoid.string)) {

                        avoidList.setVisibility(View.VISIBLE);
                        avoidListArray.add(toAvoid.string);
                        avoidList.setAdapter(avoidListArrayAdapter);
                        avoidedMessageList.add(toAvoid.tag);

                        listDescription.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getApplicationContext(), toAvoid.string + getString(R.string.error_already_avoided), Toast.LENGTH_SHORT).show();
                    }
                    avoid.setSelection(0);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /**
         *  Start gomb beállítása
         */

        startButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    goToMap();
                }
                return false;
            }

        });


    }

    public void goToMap() {

        StringWithTag fromSelected = (StringWithTag) fromWhere.getSelectedItem();
        StringWithTag toSelected = (StringWithTag) toWhere.getSelectedItem();

        String avoidString = "";
        Boolean firstAvoid = true;
        for (String avoid : avoidedMessageList) {
            if (firstAvoid) {
                avoidString += avoid;
                firstAvoid = !firstAvoid;
                continue;
            }
            avoidString += ";" + avoid;

        }


        if (fromSelected.tag == "0" || toSelected.tag == "0") {
            Toast.makeText(getApplicationContext(), getString(R.string.error_nothing_selected), Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, MapsActivity.class);
            //Intent intent = new Intent(this, FullscreenActivity.class);

            intent.putExtra(mapActivity, fromSelected.tag + ";" + toSelected.tag + ";" + avoidString);
            this.finish();
            startActivity(intent);
        }

    }
}