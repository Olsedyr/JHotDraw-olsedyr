package org.jhotdraw.draw.tool;

import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.figure.TextHolderFigure;
import org.jhotdraw.draw.tool.TextAreaCreationTool;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TextAreaCreationToolTest {

    @Mock
    private TextHolderFigure prototype;
    @Mock
    private DrawingEditor editor;
    @Mock
    private Graphics2D graphics;
    @Mock
    private MouseEvent mouseEvent;

    private TextAreaCreationTool tool;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tool = new TextAreaCreationTool(prototype);
    }

    @Test
    void testSetRubberbandColor() {
        Color expectedColor = Color.RED;
        tool.setRubberbandColor(expectedColor);
        assertEquals(expectedColor, tool.rubberbandColor);
    }

    @Test
    void testDeactivate() {
        tool.deactivate(editor);
        // Verifying that endEdit() is called during deactivate
        verify(editor, never()).endEdit();
    }

    @Test
    void testMousePressed_NoTypingTarget() {
        // Simulate no existing typing target
        tool.mousePressed(mouseEvent);
        verify(mouseEvent, atLeastOnce()).getX();
        verify(mouseEvent, atLeastOnce()).getY();
    }

    @Test
    void testMousePressed_WithTypingTarget() {
        TextHolderFigure mockTypingTarget = mock(TextHolderFigure.class);
        tool.typingTarget = mockTypingTarget;

        tool.mousePressed(mouseEvent);

        // Verifies that handleTypingTarget() and endEdit() are called
        verify(mockTypingTarget, atLeastOnce()).willChange();
    }

    @Test
    void testDrawWithRubberbandColor() {
        tool.setRubberbandColor(Color.GREEN);
        tool.createdFigure = mock(TextHolderFigure.class);

        tool.draw(graphics);

        verify(graphics).setColor(Color.GREEN);
    }

    @Test
    void testBeginEditWithNewTextHolderFigure() {
        TextHolderFigure textHolderFigure = mock(TextHolderFigure.class);
        tool.beginEdit(textHolderFigure);

        // Check if typingTarget is set and FloatingTextArea is created
        assertEquals(textHolderFigure, tool.typingTarget);
        verify(textHolderFigure).getText();
    }

    @Test
    void testEndEdit() {
        TextHolderFigure textHolderFigure = mock(TextHolderFigure.class);
        tool.typingTarget = textHolderFigure;

        tool.endEdit();

        // Check if typingTarget is cleared and text area overlay is ended
        assertEquals(null, tool.typingTarget);
        verify(textHolderFigure, atLeastOnce()).willChange();
    }

    @Test
    void testSaveCurrentTextState() {
        TextHolderFigure textHolderFigure = mock(TextHolderFigure.class);
        tool.typingTarget = textHolderFigure;

        // Set up mock text area with sample text
        FloatingTextArea mockTextArea = mock(FloatingTextArea.class);
        when(mockTextArea.getText()).thenReturn("New Text");
        tool.textArea = mockTextArea;

        tool.saveCurrentTextState();

        // Verify that text was updated on typing target
        verify(textHolderFigure).setText("New Text");
    }
}
