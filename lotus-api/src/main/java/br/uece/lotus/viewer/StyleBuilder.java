package br.uece.lotus.viewer;

/**
 * Created by emerson on 26/02/15.
 */
public class StyleBuilder {

    StringBuilder buffer = new StringBuilder();

    public static String fill(String color) {
        StringBuilder sb = new StringBuilder();
        sb.append("-fx-fill: ");
        sb.append(color);
        sb.append(";");
        return sb.toString();
    }

    public static String font(String color, String weight, int size) {
        StringBuilder sb = new StringBuilder();
        sb.append("-fx-text-fill: ");
        sb.append(color);
        sb.append(";-fx-font-weight: ");
        sb.append(weight);
        sb.append(";-fx-font-size: ");
        sb.append(size);
        sb.append(";");
        return sb.toString();
    }

    public static String stroke(String color, int width) {
        StringBuilder sb = new StringBuilder();
        sb.append("-fx-stroke: ");
        sb.append(color);
        sb.append(";-fx-stroke-width: ");
        sb.append(width);
        sb.append(";");
        return sb.toString();
    }
}
