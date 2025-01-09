package org.jhotdraw.draw;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.figure.Figure;
import org.assertj.core.api.Assertions;
import java.util.List;

public class Then extends Stage<Then> {
    @ExpectedScenarioState
    Drawing drawing;

    @ExpectedScenarioState
    List<Figure> figures;

    public Then the_selected_object_should_be_brought_to_the_front_of_the_drawing() {
        List<Figure> children = drawing.getChildren();
        Assertions.assertThat(children).last().isSameAs(figures.get(1));
        return self();
    }

    public Then the_selected_object_should_be_moved_to_the_bottom_layer_of_the_drawing() {
        List<Figure> children = drawing.getChildren();
        Assertions.assertThat(children).first().isSameAs(figures.get(1));
        return self();
    }

    public Then the_order_should_be_A_C_B() {
        List<Figure> children = drawing.getChildren();
        Assertions.assertThat(children).containsExactly(figures.get(1), figures.get(0), figures.get(2));
        return self();
    }
}
