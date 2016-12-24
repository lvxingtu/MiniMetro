package javafxTest;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KadirF on 20/12/2016.
 */
public class GameView {

    HashMap<Station,fxStation> stations;
    HashMap<Train,fxTrain> trains;
    HashMap<Client,fxClient> clients;
    HashMap<model.Line,ArrayList<Shape>> lineLinks;
    HashMap<model.Line,Shape[]> lineEnds;
    List<Shape> links = new ArrayList<>();

    private Group group;

    public GameView(Group g) {
        stations = new HashMap<>();
        trains = new HashMap<>();
        group = g;
        lineLinks = new HashMap<>();
        lineEnds = new HashMap<>();
    }

    public void createLine(Line l,Shape end1, Shape end2) {
        ArrayList<Shape> list = new ArrayList<>();
        lineLinks.put(l,list);

        Shape[] ends = new Shape[2];
        ends[0] = end2; ends[1] = end1;
        lineEnds.put(l,ends);
    }

    public void setLineEnd (Line l, Shape end,boolean inFirst) {
        Shape [] ends  = lineEnds.get(l);
        if(inFirst)
            ends[0] = end;
        else
            ends[1] = end;
    }

    public void removeEnds(Line l) {
        Shape [] ends = lineEnds.get(l);
        group.getChildren().remove(ends[0]);
        group.getChildren().remove(ends[1]);
    }

    public Shape getLineLink(Line l,boolean inFirst) {
        if(inFirst)
            return lineLinks.get(l).get(0);
        else
            return lineLinks.get(l).get(lineLinks.get(l).size()-1);
    }

    public void addLineLink(Line l,Shape link,boolean inFirst) {
        ArrayList<Shape> list = lineLinks.get(l);
        if(inFirst) {
            list.add(0, link);
            System.err.println("ADD IN FIRST");
        }
        else {
            list.add(link);
            System.err.println("ADD IN LAST");
        }
        links.add(link);
    }

    public void removeLineLink(Line l, boolean inFirst) {
        Shape s;
        if(inFirst) {
            s = lineLinks.get(l).remove(0);
            System.err.println("REMOVING IN FIRST");
        }
        else {
            s = lineLinks.get(l).remove(lineLinks.get(l).size() - 1);
            System.err.println("REMOVING IN LAST");
        }

        group.getChildren().remove(s);
        links.remove(s);

        if(lineLinks.get(l).size()==0)
            lineLinks.remove(l);
    }

    public void addNode(Node n) {
        group.getChildren().add(n);
        System.err.println("ADDED NODE");
    }

    public void put(Station s) {
        stations.put(s,new fxStation(s));
        group.getChildren().add(stations.get(s).shape);
    }

    public void put(Train t) {
        trains.put(t,new fxTrain(t));
        group.getChildren().add(trains.get(t));
    }

    public void put(Client c) {
        clients.put(c,new fxClient(c));
        group.getChildren().add(clients.get(c).shape);

    }

    public fxTrain get(Train t) {
        return trains.get(t);
    }

    public fxStation get(Station s) {
        return stations.get(s);
    }

    public fxClient get(Client c) { return clients.get(c);}


    public boolean intersects (Shape f) {
        for(Shape l : links) {
            Shape intersect = Shape.intersect(f, l);
            if(intersect.getBoundsInLocal().getWidth() != -1) {
                System.err.println("INTERSECTS !! ");
                return true;
            }
        }
        return false;
    }
}