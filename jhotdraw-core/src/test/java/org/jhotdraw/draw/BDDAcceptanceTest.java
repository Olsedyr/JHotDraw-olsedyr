package org.jhotdraw.draw;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

public class BDDAcceptanceTest extends ScenarioTest<Given, When, Then> {
    static {
        System.setProperty("jgiven.report.config.file", "src/test/resources/jgiven.properties");
    }

    @Test
    public void bring_to_front_scenario() {
        given().a_drawing_with_multiple_objects();
        when().I_select_an_object_and_click_the_bring_to_front_button();
        then().the_selected_object_should_be_brought_to_the_front_of_the_drawing();
    }

    @Test
    public void send_to_back_scenario() {
        given().a_drawing_with_multiple_objects();
        when().I_select_an_object_and_click_the_send_to_back_button();
        then().the_selected_object_should_be_moved_to_the_bottom_layer_of_the_drawing();
    }

    @Test
    public void arrange_scenario_without_changing_other_orders() {
        given().a_drawing_with_multiple_objects();
        when().I_select_an_object_and_click_the_send_to_back_button();
        then().the_order_should_be_A_C_B();
    }
}
