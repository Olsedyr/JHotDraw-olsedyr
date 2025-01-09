package org.jhotdraw.draw;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.RectangleFigure;
import java.util.ArrayList;
import java.util.List;


public class Given extends Stage<Given> {
    @ProvidedScenarioState
    Drawing drawing;

    @ProvidedScenarioState
    List<Figure> figures = new ArrayList<>();


    public Given a_drawing_with_multiple_objects() {
        drawing = new QuadTreeDrawing();

        Figure figure1, figure2, figure3;
        figure1 = new RectangleFigure();
        figure2 = new RectangleFigure();
        figure3 = new RectangleFigure();

        figures.add(figure1);
        figures.add(figure2);
        figures.add(figure3);
        drawing.basicAdd(0, figure1);
        drawing.basicAdd(1, figure2);
        drawing.basicAdd(2, figure3);

        return self();
    }

}
