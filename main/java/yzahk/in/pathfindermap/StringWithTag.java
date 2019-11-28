/**
 * Created by Ákos Nikházy 2019
 *
 * Simple object for the spinners in
 * main activity.
 */

package yzahk.in.pathfindermap;

public class StringWithTag {

    public String string;
    String tag;

    StringWithTag(String stringPart, String tagPart) {
        string = stringPart;
        tag = tagPart;
    }

    @Override
    public String toString() {
        return string;
    }

}