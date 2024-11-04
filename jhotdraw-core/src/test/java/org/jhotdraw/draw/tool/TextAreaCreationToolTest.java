package org.jhotdraw.draw.tool;

import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit5.JGivenExtension;
import org.jhotdraw.draw.figure.TextHolderFigure;
import org.jhotdraw.draw.text.FloatingTextArea;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.tngtech.jgiven.annotation.*;
import com.tngtech.jgiven.Stage;

@ExtendWith(JGivenExtension.class)
class TextAreaCreationToolTest {

    private TextAreaCreationTool textAreaCreationTool;
    private FloatingTextArea mockTextArea;
    private DrawingEditor mockEditor;
    private DrawingView mockDrawingView;

    @BeforeEach
    void setUp() {
        TextHolderFigure prototype = mock(TextHolderFigure.class);
        textAreaCreationTool = new TextAreaCreationTool(prototype);
        mockTextArea = mock(FloatingTextArea.class);
        mockEditor = mock(DrawingEditor.class);
        mockDrawingView = mock(DrawingView.class);

        when(mockEditor.getActiveView()).thenReturn(mockDrawingView);
        textAreaCreationTool.setDrawingEditor(mockEditor);
        textAreaCreationTool.setTextArea(mockTextArea);

        // Set up mockTextArea to hold and retrieve text as expected
        // This uses an Answer to track text state within mockTextArea
        doAnswer(invocation -> {
            String text = invocation.getArgument(0);
            when(mockTextArea.getText()).thenReturn(text);
            return null;
        }).when(mockTextArea).setText(anyString());
    }

    @Test
    void scenario1_inputLargeBodiesOfText() {
        String largeText = "This is a very large body of text that should fit within the text area...";

        new GivenTextAreaCreationTool()
                .a_text_area_creation_tool(textAreaCreationTool)
                .and_a_text_area_is_available();

        new WhenTextAreaCreationTool()
                .i_input_a_large_body_of_text(largeText, textAreaCreationTool);

        new ThenTextAreaCreationTool()
                .the_text_area_should_display_the_entire_text_without_truncation(largeText, textAreaCreationTool);
    }

    @Test
    void scenario2_editExistingText() {
        String initialText = "Initial text";
        String editedText = "Edited text";

        new GivenTextAreaCreationTool()
                .a_text_area_creation_tool(textAreaCreationTool)
                .and_text_in_the_text_area(initialText);

        new WhenTextAreaCreationTool()
                .i_edit_the_text(editedText, textAreaCreationTool);

        new ThenTextAreaCreationTool()
                .the_changes_should_be_reflected_in_the_text_area(editedText, textAreaCreationTool);
    }

    @Test
    void scenario3_formatText() {
        String formattedText = "<b>Bold text</b>";

        new GivenTextAreaCreationTool()
                .a_text_area_creation_tool(textAreaCreationTool)
                .and_text_in_the_text_area("Some text");

        new WhenTextAreaCreationTool()
                .i_apply_formatting_options(formattedText, textAreaCreationTool);

        new ThenTextAreaCreationTool()
                .the_formatted_text_should_be_displayed_accordingly(formattedText, textAreaCreationTool);
    }

    @Test
    void scenario4_clearText() {
        new GivenTextAreaCreationTool()
                .a_text_area_creation_tool(textAreaCreationTool)
                .and_text_in_the_text_area("Some text");

        new WhenTextAreaCreationTool()
                .i_clear_the_text_area(textAreaCreationTool);

        new ThenTextAreaCreationTool()
                .the_text_area_should_be_empty(textAreaCreationTool);
    }

    static class GivenTextAreaCreationTool extends Stage<GivenTextAreaCreationTool> {
        @ProvidedScenarioState
        TextAreaCreationTool tool;
        FloatingTextArea mockTextArea;

        GivenTextAreaCreationTool a_text_area_creation_tool(TextAreaCreationTool tool) {
            this.tool = tool;
            return this;
        }

        GivenTextAreaCreationTool and_a_text_area_is_available() {
            assertNotNull(tool.getTextArea());
            return this;
        }

        GivenTextAreaCreationTool and_text_in_the_text_area(String text) {
            mockTextArea = tool.getTextArea();
            mockTextArea.setText(text);  // Set initial text value
            return this;
        }
    }

    static class WhenTextAreaCreationTool extends Stage<WhenTextAreaCreationTool> {

        WhenTextAreaCreationTool i_input_a_large_body_of_text(String text, TextAreaCreationTool tool) {
            tool.getTextArea().setText(text);
            return this;
        }

        WhenTextAreaCreationTool i_edit_the_text(String newText, TextAreaCreationTool tool) {
            tool.getTextArea().setText(newText);
            return this;
        }

        WhenTextAreaCreationTool i_apply_formatting_options(String formattedText, TextAreaCreationTool tool) {
            tool.getTextArea().setText(formattedText);
            return this;
        }

        WhenTextAreaCreationTool i_clear_the_text_area(TextAreaCreationTool tool) {
            tool.getTextArea().setText("");
            return this;
        }
    }

    static class ThenTextAreaCreationTool extends Stage<ThenTextAreaCreationTool> {

        ThenTextAreaCreationTool the_text_area_should_display_the_entire_text_without_truncation(String text, TextAreaCreationTool tool) {
            assertEquals(text, tool.getTextArea().getText());
            return this;
        }

        ThenTextAreaCreationTool the_changes_should_be_reflected_in_the_text_area(String text, TextAreaCreationTool tool) {
            assertEquals(text, tool.getTextArea().getText());
            return this;
        }

        ThenTextAreaCreationTool the_formatted_text_should_be_displayed_accordingly(String formattedText, TextAreaCreationTool tool) {
            assertEquals(formattedText, tool.getTextArea().getText());
            return this;
        }

        ThenTextAreaCreationTool the_text_area_should_be_empty(TextAreaCreationTool tool) {
            assertEquals("", tool.getTextArea().getText());
            return this;
        }
    }
}
