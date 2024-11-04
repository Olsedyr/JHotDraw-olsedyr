package org.jhotdraw.draw.tool;

import org.jgiven.Stage;
import org.jgiven.annotation.AfterScenario;
import org.jgiven.annotation.BeforeScenario;
import org.jgiven.annotation.Given;
import org.jgiven.annotation.Then;
import org.jgiven.annotation.When;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class TextEditingToolTest extends Scenario<TextEditingToolTest.GivenStage, TextEditingToolTest.WhenStage, TextEditingToolTest.ThenStage> {

    private TextEditingTool tool;
    private TextHolderFigure mockTypingTarget;
    private FloatingTextField mockTextField;
    private DrawingEditor mockEditor;
    private DrawingView mockView;
    private MouseEvent mockMouseEvent;
    private KeyEvent mockKeyEvent;
    private ActionEvent mockActionEvent;

    @BeforeScenario
    public void setUp() {
        mockTypingTarget = mock(TextHolderFigure.class);
        mockTextField = mock(FloatingTextField.class);
        mockEditor = mock(DrawingEditor.class);
        mockView = mock(DrawingView.class);
        mockMouseEvent = mock(MouseEvent.class);
        mockKeyEvent = mock(KeyEvent.class);
        mockActionEvent = mock(ActionEvent.class);

        when(mockEditor.getActiveView()).thenReturn(mockView);
        when(mockView.isEnabled()).thenReturn(true);

        tool = new TextEditingTool(mockTypingTarget);
        tool.textField = mockTextField;
        tool.editor = mockEditor;
    }

    @AfterScenario
    public void tearDown() {
        // Clean up if necessary after each scenario
    }

    // Given stage
    public static class GivenStage extends ScenarioStage<GivenStage> {

        @Given("the TextEditingTool is initialized with a typing target")
        public void givenTheTextEditingToolIsInitializedWithTypingTarget() {
            // Initialization happens in setUp method
        }

        @Given("the typing target is null")
        public void givenTheTypingTargetIsNotSet() {
            tool.typingTarget = null;
        }

        @Given("the text has changed in the text field")
        public void givenTheTextHasChanged() {
            when(mockTextField.getText()).thenReturn("new text");
            when(mockTypingTarget.getText()).thenReturn("old text");
        }

        @Given("the text has not changed in the text field")
        public void givenTheTextHasNotChanged() {
            when(mockTextField.getText()).thenReturn("");
            when(mockTypingTarget.getText()).thenReturn("old text");
        }

        @Given("the tool is not in editing mode")
        public void givenTheToolIsNotInEditingMode() {
            tool.typingTarget = null;
        }
    }

    // When stage
    public static class WhenStage extends ScenarioStage<WhenStage> {

        @When("the edit process begins")
        public void whenTheEditProcessBegins() {
            tool.beginEdit(mockTypingTarget);
        }

        @When("the edit process ends")
        public void whenTheEditProcessEnds() {
            tool.endEdit();
        }

        @When("the cursor is updated while editing")
        public void whenTheCursorIsUpdatedWhileEditing() {
            tool.typingTarget = mockTypingTarget;
            tool.updateCursor(mockView, new Point());
        }

        @When("the cursor is updated while not editing")
        public void whenTheCursorIsUpdatedWhileNotEditing() {
            tool.updateCursor(mockView, new Point());
        }

        @When("the mouse is pressed on a valid target")
        public void whenTheMouseIsPressedOnValidTarget() {
            tool.mousePressed(mockMouseEvent);
        }

        @When("the mouse is pressed without a valid target")
        public void whenTheMouseIsPressedWithoutValidTarget() {
            tool.typingTarget = null;
            tool.mousePressed(mockMouseEvent);
        }
    }

    // Then stage
    public static class ThenStage extends ScenarioStage<ThenStage> {

        @Then("the tool should be in editing mode")
        public void thenTheToolShouldBeInEditingMode() {
            assertTrue(tool.isEditing());
        }

        @Then("the tool should not be in editing mode")
        public void thenTheToolShouldNotBeInEditingMode() {
            assertFalse(tool.isEditing());
        }

        @Then("an overlay should be created for the text field")
        public void thenAnOverlayShouldBeCreatedForTheTextField() {
            verify(mockTextField).createOverlay(any(), eq(mockTypingTarget));
        }

        @Then("the text field should receive focus")
        public void thenTheTextFieldShouldReceiveFocus() {
            verify(mockTextField).requestFocus();
        }

        @Then("the new text should be set on the typing target")
        public void thenTheNewTextShouldBeSetOnTheTypingTarget() {
            verify(mockTypingTarget).setText("new text");
            verify(mockTypingTarget).willChange();
            verify(mockTypingTarget).changed();
            verify(mockTextField).endOverlay();
        }

        @Then("the typing target should not be modified")
        public void thenTheTypingTargetShouldNotBeModified() {
            verify(mockTypingTarget, times(0)).setText(anyString());
            verify(mockTextField).endOverlay();
        }

        @Then("the cursor should be set to the default cursor")
        public void thenTheCursorShouldBeSetToDefaultCursor() {
            verify(mockView).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        @Then("the cursor should change to crosshair")
        public void thenTheCursorShouldChangeToCrosshair() {
            verify(mockView).setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }

        @Then("the editing process should start")
        public void thenTheEditingProcessShouldStart() {
            verify(mockTextField).createOverlay(any(), eq(mockTypingTarget));
            verify(mockTextField).requestFocus();
        }

        @Then("no action should be taken")
        public void thenNoActionShouldBeTaken() {
            verify(mockTextField, times(0)).createOverlay(any(), any());
            verify(mockTextField, times(0)).requestFocus();
        }
    }
}
