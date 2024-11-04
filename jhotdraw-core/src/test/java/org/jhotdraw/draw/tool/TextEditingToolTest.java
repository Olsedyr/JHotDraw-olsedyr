package org.jhotdraw.draw.tool;

import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.TextHolderFigure;
import org.jhotdraw.draw.text.FloatingTextField;
import org.jgiven.Stage;
import org.jgiven.annotation.BeforeStage;
import org.jgiven.annotation.Scenario;
import org.jgiven.annotation.Then;
import org.jgiven.annotation.When;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * JGiven BDD tests for the {@link TextEditingTool} class.
 */
public class TextEditingToolTest {

    /** The TextEditingTool instance under test. */
    private TextEditingTool tool;

    /** Mock for the figure being edited. */
    private TextHolderFigure mockTypingTarget;

    /** Mock for the text field used for editing text. */
    private FloatingTextField mockTextField;

    /** Mock for the drawing editor. */
    private DrawingEditor mockEditor;

    /** Mock for the drawing view. */
    private DrawingView mockView;

    /** Mock for mouse events. */
    private MouseEvent mockMouseEvent;

    /** Mock for the drawing object. */
    private Drawing mockDrawing;

    @BeforeStage
    public void setUp() {
        mockTypingTarget = mock(TextHolderFigure.class);
        mockTextField = mock(FloatingTextField.class);
        mockEditor = mock(DrawingEditor.class);
        mockView = mock(DrawingView.class);
        mockMouseEvent = mock(MouseEvent.class);
        mockDrawing = mock(Drawing.class);

        when(mockEditor.getActiveView()).thenReturn(mockView);
        when(mockView.isEnabled()).thenReturn(true);

        tool = new TextEditingTool(mockTypingTarget);
        tool.textField = mockTextField;
        tool.editor = mockEditor;

        when(tool.getDrawing()).thenReturn(mockDrawing);
    }

    @Scenario
    public class whenEditing {

        @When("the tool is initialized")
        public void the_tool_is_initialized() {
            tool.beginEdit(mockTypingTarget);
        }

        @Then("it should be in editing mode")
        public void it_should_be_in_editing_mode() {
            assertTrue(tool.isEditing());
        }

        @Then("it should be set as typing target")
        public void it_should_set_typing_target() {
            assertEquals(mockTypingTarget, tool.typingTarget);
        }
    }

    @Scenario
    public class whenEndingEdit {

        @When("the text has changed")
        public void the_text_has_changed() {
            when(mockTextField.getText()).thenReturn("new text");
            when(mockTypingTarget.getText()).thenReturn("old text");
            tool.endEdit();
        }

        @Then("the text should be updated")
        public void the_text_should_be_updated() {
            verify(mockTypingTarget).setText("new text");
            verify(mockTypingTarget, times(1)).willChange();
            verify(mockTypingTarget, times(1)).changed();
            verify(mockTextField, times(1)).endOverlay();
            assertNull(tool.typingTarget);
        }

        @When("the text has not changed")
        public void the_text_has_not_changed() {
            when(mockTextField.getText()).thenReturn("");
            when(mockTypingTarget.getText()).thenReturn("old text");
            tool.endEdit();
        }

        @Then("the text should not be updated")
        public void the_text_should_not_be_updated() {
            verify(mockTypingTarget, times(0)).setText(anyString());
            verify(mockTextField, times(1)).endOverlay();
            assertNull(tool.typingTarget);
        }
    }

    @Scenario
    public class whenUpdatingCursor {

        @When("the editing is active")
        public void the_editing_is_active() {
            tool.typingTarget = mockTypingTarget;
            tool.updateCursor(mockView, new Point());
        }

        @Then("the cursor should be set to default")
        public void the_cursor_should_be_set_to_default() {
            verify(mockView).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        @When("the editing is not active")
        public void the_editing_is_not_active() {
            tool.typingTarget = null;
            tool.updateCursor(mockView, new Point());
        }

        @Then("the cursor should be set to crosshair")
        public void the_cursor_should_be_set_to_crosshair() {
            verify(mockView).setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }
    }

    @Scenario
    public class whenMousePressed {

        @When("the mouse is pressed on the typing target")
        public void the_mouse_is_pressed_on_the_typing_target() {
            tool.mousePressed(mockMouseEvent);
        }

        @Then("it should start editing if the target exists")
        public void it_should_start_editing_if_target_exists() {
            verify(mockTextField, times(1)).createOverlay(any(), eq(mockTypingTarget));
            verify(mockTextField, times(1)).requestFocus();
        }

        @When("the mouse is pressed without a typing target")
        public void the_mouse_is_pressed_without_a_typing_target() {
            tool.typingTarget = null;
            tool.mousePressed(mockMouseEvent);
        }

        @Then("it should do nothing")
        public void it_should_do_nothing() {
            verify(mockTextField, times(0)).createOverlay(any(), any());
            verify(mockTextField, times(0)).requestFocus();
        }
    }
}
