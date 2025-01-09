package org.jhotdraw.draw;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.annotation.ScenarioState;
import org.jhotdraw.draw.action.ArrangeActionType;
import org.jhotdraw.draw.figure.Figure;

import java.util.List;

public class When extends Stage<When> {
    @ExpectedScenarioState
    Drawing drawing;

    @ExpectedScenarioState
    List<Figure> figures;

    public When I_select_an_object_and_click_the_bring_to_front_button() {
        drawing.arrange(figures.get(1), ArrangeActionType.BRING_TO_FRONT);
        return self();
    }

    public When I_select_an_object_and_click_the_send_to_back_button() {
        drawing.arrange(figures.get(1), ArrangeActionType.SEND_TO_BACK);
        return self();
    }
}
