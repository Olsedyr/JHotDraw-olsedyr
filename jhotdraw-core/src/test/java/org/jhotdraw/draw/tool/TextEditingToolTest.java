package org.jhotdraw.draw.tool;

import static org.junit.Assert.*;
import org.jhotdraw.draw.figure.TextHolderFigure;
import org.jhotdraw.draw.tool.TextEditingTool;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TextEditingToolTest {

    private TextEditingTool textEditingTool;
    private TextHolderFigure mockTextHolderFigure;

    @Before
    public void setUp() {
        // Create a mock TextHolderFigure
        mockTextHolderFigure = Mockito.mock(TextHolderFigure.class);
        // Initialize the TextEditingTool with the mock figure
        textEditingTool = new TextEditingTool(mockTextHolderFigure);
    }

    @Test
    public void testBeginEdit() {
        // Simulate starting an edit
        textEditingTool.beginEdit(mockTextHolderFigure);

        // Assert that the typingTarget is set correctly
        assertEquals(mockTextHolderFigure, textEditingTool.typingTarget);
        // Check if textField is initialized
        assertNotNull(textEditingTool.getTextField());
    }

    @Test
    public void testEndEdit() {
        // Start editing first
        textEditingTool.beginEdit(mockTextHolderFigure);

        // Set some text in the textField
        String newText = "Hello World!";
        textEditingTool.getTextField().setText(newText);

        // Call endEdit to finalize the editing
        textEditingTool.endEdit();

        // Verify that the text in the mockTextHolderFigure is set correctly
        Mockito.verify(mockTextHolderFigure).setText(newText);
    }

    @Test
    public void testEndEditNoChange() {
        // Start editing first
        textEditingTool.beginEdit(mockTextHolderFigure);

        // Call endEdit without changing the text
        textEditingTool.endEdit();

        // Verify that the setText was not called when there was no new text
        Mockito.verify(mockTextHolderFigure, Mockito.never()).setText(Mockito.anyString());
    }
}
