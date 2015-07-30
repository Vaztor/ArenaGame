package com.parse.starter;


        import android.app.Application;
        import android.graphics.Path;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.os.Bundle;
        import android.app.Activity;
        import android.view.Menu;
        import android.widget.Toast;

        import com.parse.FindCallback;
        import com.parse.GetCallback;
        import com.parse.Parse;
        import com.parse.ParseACL;
        import com.parse.ParseAnalytics;
        import com.parse.ParseCrashReporting;
        import com.parse.ParseQuery;
        import com.parse.ParseUser;
        import com.parse.ParseObject;

        import org.json.JSONArray;

        import java.text.ParseException;
        import java.util.ArrayList;
        import java.util.List;

/**
 * Created by rbeyh_000 on 7/21/2015.
 */
public class GameLogic {

    ParseObject myGladiator = null;
    ArrayList<ParseObject> gladiatorList = new ArrayList<ParseObject>();
    ParseObject myMap = null;
    ParseObject myMatch = null;
    boolean alreadyMoved = false;
    int currentXPos = 0;
    int currentYPos = 0;
    enum currentDirection{N, NE, E, SE, S, SW, W, NW};

    public GameLogic(ParseObject gladiator)
    {
        myGladiator = gladiator;
    }

    public ParseObject joinMatch(String gladId)
    {
        myMatch = joinMatchParse(gladId);

        if(myMatch == null)
            myMatch = createMatch(gladId);

        return myMatch;
    }

    public void playerMove(ParseObject glad, currentDirection type)
    {
        ArrayList<Integer> ArrayList = (ArrayList<Integer>)glad.get("location");
        int[] estimation = new int[2];

        estimation  = getDestination(ArrayList.get(0), ArrayList.get(1), type);

        if(!alreadyMoved && !isCellOccupied(estimation[0], estimation[1], myMap))
        {
            glad.put("location", moveGladiator(glad.getObjectId(), ArrayList.get(0), ArrayList.get(1), estimation[0], estimation[1]) );
        }

        return;
    }

    private int[] getDestination(int x,int y ,currentDirection type)
    {
        int[] estimation = new int[2];

        switch(type) {
            case N:
                y++;
                break;

            case NE:
                x++;
                y++;
                break;

            case E:
                x++;
                break;

            case SE:
                x++;
                y--;
                break;

            case S:
                y--;
                break;

            case SW:
                x--;
                y--;
                break;

            case W:
                x--;
                break;

            case NW:
                x--;
                y++;
                break;

            default:
                break;


        }

        estimation[0] = x;
        estimation[1] = y;

        return estimation;
    }

    public void playerAttack(ParseObject glad, ParseObject enemy)
    {
        int dmg = 0;

        if(withinRange(glad, enemy))
        {
            
            dmg = glad.getInt("attack") - enemy.getInt("defence");
            enemy.put("health", (enemy.getInt("health") - dmg));
            updateParse(enemy);


            if (enemy.getInt("health") <= 0)
            {
                //death -- remove from glad list -- in parse remove id from users col and players--;
                endGame();
            }
        }
        else
        {
            //toast not in range
        }

        return;
    }

    public void playerEndTurn() {
        int i = 0;

        for(i = 0; i < gladiatorList.size(); i++)
            update(gladiatorList.get(i));

        update(myGladiator);
        update(myMap);
        update(myMatch);

        //parse call to increment turn
        endTurn();

        return;
    }

    protected boolean withinRange(ParseObject glad, ParseObject enemy)
    {
        boolean result = false;
        //these calls may be incorrectly getting the location array
        ArrayList<Integer> gladLoc = (ArrayList<Integer>)glad.get("location");
        ArrayList<Integer> enemyLoc = (ArrayList<Integer>)enemy.get("location");

        int x1 = Math.abs(gladLoc.get(0));
        int x2 = Math.abs(enemyLoc.get(0));
        int y1 = Math.abs(gladLoc.get(1));
        int y2= Math.abs(enemyLoc.get(1));

        int xdist = Math.abs(x2 - x1);
        int ydist = Math.abs(y2 - y1);

        int gladRange = glad.getInt("range");

        if(gladRange <= (xdist + ydist))
            result = true;

        return result;
    }

    //incomplete
    private void death(ParseObject glad)
    {
        int index;

        index = gladiatorList.indexOf(glad);

        if(index >= 0)
            gladiatorList.remove(index);

        //parse calls


        return;
    }
}
