package org.jhotdraw.draw.tool;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit5.ScenarioTest;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.TextHolderFigure;
import org.jhotdraw.draw.text.FloatingTextField;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TextEditingToolTest extends ScenarioTest<TextEditingToolTest.GivenStage, TextEditingToolTest.WhenStage, TextEditingToolTest.ThenStage> {

    // Story 1: Editing Tool Initialization
    @Test
    public void tool_is_in_editing_mode_when_typing_target_is_set() {
        // Given the tool is initialized.
        given().the_text_editing_tool_is_initialized()
                .and().typing_target_is_set();

        // When editing.
        when().edit_is_started_on_the_typing_target();

        // Then it should be in editing mode.
        // Then it should be set as the typing target.
        then().tool_is_in_editing_mode()
                .and().verify_overlay_created_and_focus_requested();
    }

    // Story 2: Ending Edit with Changed Text
    @Test
    public void ending_edit_with_text_change_updates_target_text() {
        // Given the text has changed (the new text is "new text" and the old text is "old text").
        given().the_text_editing_tool_is_initialized()
                .and().typing_target_is_set()
                .and().text_in_text_field_is("new text")
                .and().text_in_typing_target_is("old text");

        // When the edit ends.
        when().edit_is_ended();

        // Then the text should be updated.
        // Then the typing target should be null.
        then().typing_target_text_is_set_to("new text")
                .and().typing_target_is_null();
    }

    // Story 3: Ending Edit with Unchanged Text
    @Test
    public void ending_edit_without_text_change_does_not_update_target_text() {
        // Given the text has not changed (the new text is empty and the old text is "old text").
        given().the_text_editing_tool_is_initialized()
                .and().typing_target_is_set()
                .and().text_in_text_field_is("")
                .and().text_in_typing_target_is("old text");

        // When the edit ends.
        when().edit_is_ended();

        // Then the text should not be updated.
        // Then the typing target should be null.
        then().typing_target_is_null();
    }

    // Story 4: Updating Cursor When Editing is Active
    @Test
    public void cursor_is_updated_to_default_when_editing() {
        // Given the editing is active with a typing target.
        given().the_text_editing_tool_is_initialized()
                .and().typing_target_is_set();

        // When the cursor is updated.
        when().update_cursor_with_point();

        // Then the cursor should be set to default.
        then().cursor_is_set_to(Cursor.DEFAULT_CURSOR);
    }

    // Story 5: Updating Cursor When Editing is Inactive
    @Test
    public void cursor_is_updated_to_crosshair_when_not_editing() {
        // Given the editing is not active (no typing target).
        given().the_text_editing_tool_is_initialized()
                .and().typing_target_is_not_set();

        // When the cursor is updated.
        when().update_cursor_with_point();

        // Then the cursor should be set to crosshair.
        then().cursor_is_set_to(Cursor.CROSSHAIR_CURSOR);
    }


    // Story 7: Mouse Pressed Without Typing Target
    @Test
    public void mouse_pressed_does_nothing_if_no_target_exists() {
        // Given the mouse is pressed without a typing target.
        given().the_text_editing_tool_is_initialized()
                .and().typing_target_is_not_set();

        // When mouse is pressed.
        when().mouse_is_pressed();

        // Then it should do nothing.
        then().verify_no_overlay_created_and_no_focus_requested();
    }

    // Stage classes

    public static class GivenStage extends Stage<GivenStage> {

        @ProvidedScenarioState
        private TextEditingTool tool;

        @ProvidedScenarioState
        private TextHolderFigure mockTypingTarget;

        @ProvidedScenarioState
        private FloatingTextField mockTextField;

        @ProvidedScenarioState
        private DrawingEditor mockEditor;

        @ProvidedScenarioState
        private DrawingView mockView;

        @ProvidedScenarioState
        private MouseEvent mockMouseEvent;

        @ProvidedScenarioState
        private KeyEvent mockKeyEvent;

        @ProvidedScenarioState
        private ActionEvent mockActionEvent;

        @ProvidedScenarioState
        private Drawing mockDrawing;

        public GivenStage the_text_editing_tool_is_initialized() {
            mockTypingTarget = mock(TextHolderFigure.class);
            mockTextField = mock(FloatingTextField.class);
            mockEditor = mock(DrawingEditor.class);
            mockView = mock(DrawingView.class);
            mockMouseEvent = mock(MouseEvent.class);
            mockKeyEvent = mock(KeyEvent.class);
            mockActionEvent = mock(ActionEvent.class);

            mockDrawing = mock(Drawing.class);
            Mockito.when(mockEditor.getActiveView()).thenReturn(mockView);
            Mockito.when(mockView.isEnabled()).thenReturn(true);

            tool = new TextEditingTool(mockTypingTarget);
            tool.textField = mockTextField;
            tool.editor = mockEditor;

            Mockito.when(tool.getDrawing()).thenReturn(mockDrawing);
            return this;
        }

        public GivenStage typing_target_is_set() {
            tool.typingTarget = mockTypingTarget;
            return this;
        }

        public GivenStage typing_target_is_not_set() {
            tool.typingTarget = null;
            return this;
        }

        public GivenStage text_in_text_field_is(String text) {
            Mockito.when(mockTextField.getText()).thenReturn(text);
            return this;
        }

        public GivenStage text_in_typing_target_is(String text) {
            Mockito.when(mockTypingTarget.getText()).thenReturn(text);
            return this;
        }
    }

    public static class WhenStage extends Stage<WhenStage> {

        @ProvidedScenarioState
        private TextEditingTool tool;

        public WhenStage edit_is_started_on_the_typing_target() {
            tool.beginEdit(tool.typingTarget);
            return this;
        }

        public WhenStage edit_is_ended() {
            tool.endEdit();
            return this;
        }

        public WhenStage update_cursor_with_point() {
            tool.updateCursor(tool.editor.getActiveView(), new Point());
            return this;
        }

        public WhenStage mouse_is_pressed() {
            tool.mousePressed(tool.editor.getActiveView().getMouseEvent());
            return this;
        }
    }

    public static class ThenStage extends Stage<ThenStage> {

        @ProvidedScenarioState
        private TextEditingTool tool;

        @ProvidedScenarioState
        private TextHolderFigure mockTypingTarget;

        @ProvidedScenarioState
        private FloatingTextField mockTextField;

        public ThenStage tool_is_in_editing_mode() {
            assertTrue(tool.isEditing());
            return this;
        }

        public ThenStage tool_is_not_in_editing_mode() {
            assertFalse(tool.isEditing());
            return this;
        }

        public ThenStage typing_target_text_is_set_to(String text) {
            verify(mockTypingTarget).setText(text);
            return this;
        }

        public ThenStage typing_target_is_null() {
            assertNull(tool.typingTarget);
            return this;
        }

        public ThenStage verify_overlay_created_and_focus_requested() {
            verify(mockTextField, times(1)).createOverlay(any(), eq(mockTypingTarget));
            verify(mockTextField, times(1)).requestFocus();
            return this;
        }

        public ThenStage verify_no_overlay_created_and_no_focus_requested() {
            verify(mockTextField, times(0)).createOverlay(any(), any());
            verify(mockTextField, times(0)).requestFocus();
            return this;
        }

        public ThenStage cursor_is_set_to(int cursorType) {
            verify(tool.editor.getActiveView()).setCursor(Cursor.getPredefinedCursor(cursorType));
            return this;
        }
    }
}